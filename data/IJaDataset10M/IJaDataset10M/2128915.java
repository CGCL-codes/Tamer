package wendyeq.iweb.blog.service;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wendyeq.iweb.blog.dao.CategoryDAO;
import wendyeq.iweb.blog.pojo.Category;

@Service
public class CategoryService {

    private static final Logger logger = Logger.getLogger(CategoryService.class);

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private ArticleService articleService;

    public void insert(String name) {
        logger.warn(categoryDAO);
        if (this.categoryDAO.findByName(name) == null) {
            this.categoryDAO.saveOrUpdate(new Category(name));
            logger.warn("CategoryService -- do insert!");
        } else {
            logger.warn("CategoryService -- do insert false!");
        }
    }

    public void update(String oldName, String newName) {
        this.articleService.updateCategory(oldName, newName);
        Category category = this.categoryDAO.findByName(oldName);
        category.setName(newName);
        logger.warn("newName" + category.getName());
        this.categoryDAO.saveOrUpdate(category);
    }

    public void delete(String id) {
        Category cat = this.categoryDAO.findById(id);
        this.articleService.updateByCategory(cat.getName());
        this.categoryDAO.delete(id);
    }

    public List<Category> findAll() {
        return this.categoryDAO.findAll();
    }

    public List<Category> findByPage(int min, int max) {
        return this.categoryDAO.findByPage(min, max);
    }
}
