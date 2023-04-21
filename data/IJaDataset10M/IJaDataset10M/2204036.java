package org.blueoxygen.cimande.category.actions;

import java.sql.Timestamp;
import org.blueoxygen.cimande.category.Category;
import org.blueoxygen.cimande.descriptors.Descriptor;

/**
 * @author dwi miyanto [mee_andto@yahoo.com ]
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class UpdateCategory extends CategoryForm {

    private String id;

    private String iparentcode = "";

    protected Category parentCategory;

    protected Descriptor categoryDescriptor = new Descriptor();

    public String execute() {
        category = (Category) manager.getById(Category.class, getId());
        if (getCode().equalsIgnoreCase("")) {
            addActionError("please input code");
            return INPUT;
        }
        if (getDescription().equalsIgnoreCase("")) {
            addActionError("please input description");
            return INPUT;
        }
        categoryDescriptor = (Descriptor) manager.getById(Descriptor.class, getDescriptor_id());
        parentCategory = (Category) manager.getById(Category.class, getIparentcode());
        category.setCode(getCode());
        category.setDescription(getDescription());
        category.setUrl_category_image(getUrl_category_image());
        category.setCategoryDescriptor(categoryDescriptor);
        category.setParentCategory(parentCategory);
        logInfo = category.getLogInformation();
        logInfo.setLastUpdateBy(getCurrentUser().getId());
        logInfo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        logInfo.setActiveFlag(getActiveFlag());
        category.setLogInformation(logInfo);
        manager.save(category);
        iparentcode = category.getId();
        return SUCCESS;
    }

    public Descriptor getCategoryDescriptor() {
        return categoryDescriptor;
    }

    public void setCategoryDescriptor(Descriptor categoryDescriptor) {
        this.categoryDescriptor = categoryDescriptor;
    }

    public String getIparentcode() {
        return iparentcode;
    }

    public void setIparentcode(String iparentcode) {
        this.iparentcode = iparentcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
}
