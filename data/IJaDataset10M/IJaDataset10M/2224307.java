package com.kwoksys.action.kb;

import org.apache.struts.action.ActionForm;

/**
 * ActionForm for adding/edting KB category.
 */
public class CategoryForm extends ActionForm {

    private Integer categoryId;

    private String categoryName;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
