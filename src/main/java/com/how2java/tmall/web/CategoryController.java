package com.how2java.tmall.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.CategoryService;
import com.how2java.tmall.util.ImageUtil;
import com.how2java.tmall.util.Page4Navigator;

@RestController
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	
	
	@GetMapping("/categories")
    public Page4Navigator<Category> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        Page4Navigator<Category> page =categoryService.list(start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return page;
    }

	@GetMapping("/categories/{id}")
	public Category get(@PathVariable("id") int id) throws Exception {
		//System.out.println("categories-get-id:" + id);

		return categoryService.get(id);
	}

	@PostMapping("/categories")
	public Object add(Category bean, MultipartFile image, HttpServletRequest request) throws Exception {
		//System.out.println("categories-add-bean:" + bean.getId());
		//System.out.println("categories-add-image:" + image);
		categoryService.add(bean);
		//System.out.println("categories-add-bean:" + bean.getId());

		saveOrUpdateImageFile(bean, image, request);
		return bean;
	}

	private void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request)
			throws IOException {
		File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder, bean.getId() + ".jpg");
		if (!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		image.transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);
		ImageIO.write(img, "jpg", file);

	}

	@DeleteMapping("/categories/{id}")
	public String delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception {
		//System.out.println("categories-delete-id:" + id);
		categoryService.delete(id);
		File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder, id + ".jpg");
		file.delete();

		return null;
	}

	@PutMapping("/categories/{id}")
	public Object update( MultipartFile image, Category bean,HttpServletRequest request) throws Exception {
		/*System.out.println("categories-update-bean:" + bean);
		System.out.println("categories-update-image:" + image);
		String name = request.getParameter("name");
        bean.setName(name);
        */
		categoryService.update(bean);
		if (image != null) {
			saveOrUpdateImageFile(bean, image, request);
		}

		return bean;
	}
}
