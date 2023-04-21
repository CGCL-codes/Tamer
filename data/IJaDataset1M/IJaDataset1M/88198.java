package org.dueam.mall.web.admin;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.dueam.mall.common.WebUtils;
import org.dueam.mall.entity.CategoryEntity;
import org.dueam.mall.service.CategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class CategoryAction extends ActionSupport {

    private static final long serialVersionUID = 6041008607359275848L;

    @Autowired
    CategoryManager categoryManager;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SkipValidation
    public void prepare() throws Exception {
    }

    @SkipValidation
    public String doDefault() throws Exception {
        return SUCCESS;
    }

    @SkipValidation
    public String list() throws Exception {
        List<CategoryEntity> list = this.categoryManager.findAll();
        WebUtils.put("list", list);
        return SUCCESS;
    }

    @Override
    @SkipValidation
    public String execute() throws Exception {
        return list();
    }

    @SkipValidation
    public String remove() throws Exception {
        long id = NumberUtils.toLong(WebUtils.get("id"), 0);
        if (id > 0) {
            this.categoryManager.delete(id);
        }
        return list();
    }

    @SkipValidation
    public String save() throws Exception {
        String title = WebUtils.get("title");
        if (StringUtils.isNotEmpty(title)) {
            categoryManager.save(new CategoryEntity(title));
        }
        return list();
    }

    @SkipValidation
    public String enable() throws Exception {
        long id = NumberUtils.toLong(WebUtils.get("id"), 0);
        if (id > 0) {
            categoryManager.updateCategoryStatus(id, CategoryEntity.ENABLE);
        }
        return list();
    }

    @SkipValidation
    public String disable() throws Exception {
        long id = NumberUtils.toLong(WebUtils.get("id"), 0);
        if (id > 0) {
            this.categoryManager.updateCategoryStatus(id, CategoryEntity.DISABLE);
        }
        return list();
    }

    @SkipValidation
    public String editInput() throws Exception {
        long id = NumberUtils.toLong(WebUtils.get("id"), 0);
        CategoryEntity entity = null;
        if (id > 0) {
            entity = this.categoryManager.get(id);
        }
        if (entity == null) {
            return list();
        }
        this.title = entity.getName();
        WebUtils.put("id", id);
        return "edit-input";
    }

    @Validations(requiredStrings = { @RequiredStringValidator(type = ValidatorType.FIELD, fieldName = "title", message = "请输入类目名词!") })
    public String edit() throws Exception {
        String title = WebUtils.get("title");
        long id = NumberUtils.toLong(WebUtils.get("id"), 0);
        if (StringUtils.isNotEmpty(title) && id > 0) {
            this.categoryManager.update(id, title);
        }
        return list();
    }
}
