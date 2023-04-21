package com.koossery.adempiere.fe.beans.request;

public class M_BOMBean {

    private static final long serialVersionUID = 1L;

    private String boMType;

    private String boMUse;

    private String description;

    private String help;

    private int m_BOM_ID;

    private int m_ChangeNotice_ID;

    private int m_Product_ID;

    private String name;

    private String isProcessing;

    private String isActive;

    public String getBoMType() {
        return boMType;
    }

    public void setBoMType(String boMType) {
        this.boMType = boMType;
    }

    public String getBoMUse() {
        return boMUse;
    }

    public void setBoMUse(String boMUse) {
        this.boMUse = boMUse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public int getM_BOM_ID() {
        return m_BOM_ID;
    }

    public void setM_BOM_ID(int m_BOM_ID) {
        this.m_BOM_ID = m_BOM_ID;
    }

    public int getM_ChangeNotice_ID() {
        return m_ChangeNotice_ID;
    }

    public void setM_ChangeNotice_ID(int m_ChangeNotice_ID) {
        this.m_ChangeNotice_ID = m_ChangeNotice_ID;
    }

    public int getM_Product_ID() {
        return m_Product_ID;
    }

    public void setM_Product_ID(int m_Product_ID) {
        this.m_Product_ID = m_Product_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(String isProcessing) {
        this.isProcessing = isProcessing;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
