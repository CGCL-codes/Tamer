package com.kwoksys.action.admin.attribute;

import org.apache.struts.action.ActionForm;

/**
 * Action form for adding/editing custom attribute.
 */
public class CustomAttributeForm extends ActionForm {

    private Integer objectTypeId;

    private Integer attrId;

    private String attrName;

    private Integer attrGroupId;

    private String attrGroupName;

    private Integer attrType;

    private String attrOption;

    private int attrConvertUrl;

    private String attrUrl;

    public Integer getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(Integer objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrUrl() {
        return attrUrl;
    }

    public void setAttrUrl(String attrUrl) {
        this.attrUrl = attrUrl;
    }

    public Integer getAttrType() {
        return attrType;
    }

    public void setAttrType(Integer attrType) {
        this.attrType = attrType;
    }

    public String getAttrOption() {
        return attrOption;
    }

    public void setAttrOption(String attrOption) {
        this.attrOption = attrOption;
    }

    public int getAttrConvertUrl() {
        return attrConvertUrl;
    }

    public void setAttrConvertUrl(int attrConvertUrl) {
        this.attrConvertUrl = attrConvertUrl;
    }

    public String getAttrGroupName() {
        return attrGroupName;
    }

    public void setAttrGroupName(String attrGroupName) {
        this.attrGroupName = attrGroupName;
    }

    public Integer getAttrGroupId() {
        return attrGroupId;
    }

    public void setAttrGroupId(Integer attrGroupId) {
        this.attrGroupId = attrGroupId;
    }
}
