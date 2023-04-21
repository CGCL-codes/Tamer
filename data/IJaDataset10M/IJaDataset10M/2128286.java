package com.openbravo.bean.erp.viewbean;

import cn.ekuma.data.dao.bean.I_ViewBean;

/**
 *
 * @author Administrator
 */
public class TimeStoreLine_V implements I_ViewBean {

    public String m_ID;

    public String m_sRef;

    public String m_sCode;

    public String m_sName;

    public boolean m_bCom;

    public boolean m_bScale;

    public String category;

    public double m_dPriceBuy;

    public double m_dPriceSell;

    public double m_dCustomerPrice;

    public String attributeset;

    public double multiply;

    public String getAttributeset() {
        return attributeset;
    }

    public String getCategory() {
        return category;
    }

    public String getM_ID() {
        return m_ID;
    }

    public boolean isM_bCom() {
        return m_bCom;
    }

    public boolean isM_bScale() {
        return m_bScale;
    }

    public double getM_dCustomerPrice() {
        return m_dCustomerPrice;
    }

    public double getM_dPriceBuy() {
        return m_dPriceBuy;
    }

    public double getM_dPriceSell() {
        return m_dPriceSell;
    }

    public String getM_sCode() {
        return m_sCode;
    }

    public String getM_sName() {
        return m_sName;
    }

    public String getM_sRef() {
        return m_sRef;
    }

    public double getMultiply() {
        return multiply;
    }
}
