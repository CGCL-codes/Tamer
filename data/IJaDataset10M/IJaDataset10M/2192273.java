package com.openbravo.bean;

import cn.ekuma.data.dao.bean.I_AutoGeneratorStringKey;
import cn.ekuma.data.dao.bean.I_ModifiedLogBean;
import java.util.Date;

/**
 *
 * @author adrianromero
 */
public class InventoryLine implements I_ModifiedLogBean<String>, I_AutoGeneratorStringKey {

    private String id;

    private double m_dMultiply;

    private double m_dPrice;

    private String m_sProdID;

    private String m_sProdName;

    private String attsetid;

    private String attsetinstid;

    private String attsetinstdesc;

    private Date lastModified;

    /** Creates a new instance of InventoryLine */
    public InventoryLine(Product oProduct) {
        m_sProdID = oProduct.getID();
        m_sProdName = oProduct.getName();
        m_dMultiply = 1.0;
        m_dPrice = oProduct.getPriceBuy();
        attsetid = oProduct.getAttributeSetID();
        attsetinstid = null;
        attsetinstdesc = null;
    }

    public InventoryLine(Product oProduct, double dpor, double dprice) {
        m_sProdID = oProduct.getID();
        m_sProdName = oProduct.getName();
        m_dMultiply = dpor;
        m_dPrice = dprice;
        attsetid = oProduct.getAttributeSetID();
        attsetinstid = null;
        attsetinstdesc = null;
    }

    public String getProductID() {
        return m_sProdID;
    }

    public String getProductName() {
        return m_sProdName;
    }

    public void setProductName(String sValue) {
        if (m_sProdID == null) {
            m_sProdName = sValue;
        }
    }

    public double getMultiply() {
        return m_dMultiply;
    }

    public void setMultiply(double dValue) {
        m_dMultiply = dValue;
    }

    public double getPrice() {
        return m_dPrice;
    }

    public void setPrice(double dValue) {
        m_dPrice = dValue;
    }

    public double getSubValue() {
        return m_dMultiply * m_dPrice;
    }

    public String getProductAttSetInstId() {
        return attsetinstid;
    }

    public void setProductAttSetInstId(String value) {
        attsetinstid = value;
    }

    public String getProductAttSetId() {
        return attsetid;
    }

    public String getProductAttSetInstDesc() {
        return attsetinstdesc;
    }

    public void setProductAttSetInstDesc(String value) {
        attsetinstdesc = value;
    }

    public String getKey() {
        return this.id;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public void setKey(String key) {
        this.id = key;
    }
}
