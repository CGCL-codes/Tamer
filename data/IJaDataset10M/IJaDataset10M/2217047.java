package org.gary.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lengreen.core.interfaces.Entity;
import org.gary.base.dao.CategoryDao;
import org.gary.base.model.Category;
import org.gary.base.service.CategoryService;
import org.gary.core.dto.CategoryDto;
import org.gary.core.factories.impl.BasicServiceFactoryImpl;
import org.gary.core.util.ResultMessage;
import org.springframework.stereotype.Component;

/**
 * 类别Service类
 * @author lengreen
 * 
 */
@Component("categoryService")
public class CategoryServiceImpl extends BasicServiceFactoryImpl<Category> implements CategoryService {

    private CategoryDao categoryDao;

    /**
	 * 重写basicServicefactoryimpl中的Save方法， 重写以加入日志模块，并记录日志
	 */
    @Override
    public String save(Category category) {
        getLoggerService().makeLog(category.getClass().getName(), "开始保存一个类别" + category);
        getBasicDaoFactory().save(category);
        getLoggerService().makeLog(category.getClass().getName(), "保存一个类别成功" + category);
        return ResultMessage.category.ADD_SUCCESS;
    }

    @Override
    public boolean checkNameExist(String name, String lang) {
        if (categoryDao.checkNameExist(name, lang) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * category for product
	 */
    public List<Object[]> list(String whoCategory, String lang) {
        return categoryDao.list(whoCategory, lang);
    }

    @Override
    public List<Object[]> listParent(String whoCategory, String lang) {
        return categoryDao.listParent(whoCategory, lang);
    }

    @Override
    public List<CategoryDto> preDelete(int categoryId, Class<? extends Entity> entity) {
        List<Category> children = categoryDao.children(categoryId);
        CategoryDto dto = null;
        if (children != null) {
            if (children.size() >= 1) {
                List<CategoryDto> result = new ArrayList<CategoryDto>();
                for (Category c : children) {
                    dto = new CategoryDto();
                    dto.setId(c.getId());
                    dto.setName(c.getName());
                    dto.setCount(categoryDao.associateCount(c.getId(), entity));
                    result.add(dto);
                }
                dto = new CategoryDto();
                dto.setId(categoryId);
                dto.setName(load(Category.class, categoryId).getName());
                dto.setCount(categoryDao.associateCount(categoryId, entity));
                result.add(dto);
                return result;
            }
        }
        return null;
    }

    @Override
    public Category loadCategoryByName(String categoryName, String lang) {
        return categoryDao.loadCategoryByName(categoryName, lang);
    }

    @Resource
    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    @Override
    public Category getCategory(int id) {
        return load(Category.class, id);
    }

    @Override
    public List<Category> getList(String whoCategory, Object lang) {
        return categoryDao.getList(whoCategory, lang);
    }

    /**
	 * 获取子类别
	 */
    @Override
    public List<Category> children(int parentId) {
        return categoryDao.children(parentId);
    }

    @Override
    public <T> List<T> loadXxxByCategory(Class<T> entity, int categoryId) {
        return categoryDao.loadXxxByCategory(entity, categoryId);
    }
}
