package com.okko.db.dao;

import java.util.List;
import com.okko.db.model.Category;

public interface ICategoryDAO {

    public void deleteCategory(Long categoryId);

    public List<Category> fetchCategories();

    public Category loadCategory(Long categoryId);

    public void saveCategory(Category category);

    public void saveOrUpdateCategory(Category category);

    public void updateCategory(Category category);
}
