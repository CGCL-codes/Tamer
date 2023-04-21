package com.openbravo.bean.erp.viewbean;

import cn.ekuma.data.dao.bean.I_Category;
import cn.ekuma.data.dao.bean.I_ViewBean;

public class ProductCategoryAnalysisInfo implements I_ViewBean, I_Category {

    private String productCategoryId;

    private String productCategoryParentId;

    private String productCategoryName;

    double multiply;

    double buyPrice;

    double sellPrice;

    double profits;

    @Override
    public String getKey() {
        return productCategoryId;
    }

    @Override
    public String getID() {
        return productCategoryId;
    }

    @Override
    public String getM_sParentID() {
        return productCategoryParentId;
    }

    @Override
    public String getName() {
        return productCategoryName;
    }

    @Override
    public boolean isHot() {
        return false;
    }

    @Override
    public void setHot(boolean m_bHot) {
    }

    @Override
    public void setID(String sID) {
        this.productCategoryId = sID;
    }

    @Override
    public void setM_sParentID(String m_sParentID) {
        this.productCategoryParentId = m_sParentID;
    }

    @Override
    public void setName(String sName) {
        productCategoryName = sName;
    }

    public double getMultiply() {
        return multiply;
    }

    public void setMultiply(double multiply) {
        this.multiply = multiply;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getProfits() {
        return profits;
    }

    public void setProfits(double profits) {
        this.profits = profits;
    }
}
