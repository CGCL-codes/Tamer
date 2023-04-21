package org.cakethursday.modules.taxonomy.web;

import org.cakethursday.modules.taxonomy.domain.Category;
import org.cakethursday.modules.taxonomy.hibernate.CategoryDAO;
import org.cakethursday.modules.taxonomy.hibernate.SubCategoryDAO;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaxonomyService {

    private CategoryDAO categoryDAO;

    private SubCategoryDAO subCategoryDAO;

    @Autowired
    public TaxonomyService(CategoryDAO categoryDAO, SubCategoryDAO subCategoryDAO) {
        this.categoryDAO = categoryDAO;
        this.subCategoryDAO = subCategoryDAO;
    }

    public List<Category> getCategories() {
        List<Category> categoryList = categoryDAO.getCategories();
        for (Category category : categoryList) {
            Hibernate.initialize(category.getSubCategoryList());
        }
        return categoryList;
    }
}
