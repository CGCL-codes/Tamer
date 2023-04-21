package com.openbravo.bean;

import cn.ekuma.data.dao.bean.I_AutoGeneratorStringKey;
import cn.ekuma.data.dao.bean.I_ModifiedLogBean;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class AttributeValue implements I_ModifiedLogBean<String>, I_AutoGeneratorStringKey {

    private String id;

    private String attributeID;

    private String value;

    private Date lastModified;

    public String getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(String attributeID) {
        this.attributeID = attributeID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getKey() {
        return id;
    }

    public void setKey(String key) {
        this.id = key;
    }
}
