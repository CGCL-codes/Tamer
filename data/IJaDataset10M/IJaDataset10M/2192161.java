package com.openbravo.pos.ticket;

public class ProviderInfo {

    private int m_iProviderID;

    private String m_sName;

    /** Creates new Provider */
    public ProviderInfo() {
        m_iProviderID = 0;
        m_sName = "";
    }

    public int getProviderID() {
        return m_iProviderID;
    }

    public void setProviderID(int iProviderID) {
        m_iProviderID = iProviderID;
    }

    public String getName() {
        return m_sName;
    }

    public void setName(String sName) {
        m_sName = sName;
    }

    public String toString() {
        return m_sName;
    }
}
