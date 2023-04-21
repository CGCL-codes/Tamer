package org.koossery.adempiere.core.contract.dto.product;

import java.math.BigDecimal;
import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;

public class M_ProductPriceDTO extends KTADempiereBaseDTO {

    private static final long serialVersionUID = 1L;

    private int m_PriceList_Version_ID;

    private int m_Product_ID;

    private BigDecimal priceLimit;

    private BigDecimal priceList;

    private BigDecimal priceStd;

    private String isActive;

    public int getM_PriceList_Version_ID() {
        return m_PriceList_Version_ID;
    }

    public void setM_PriceList_Version_ID(int m_PriceList_Version_ID) {
        this.m_PriceList_Version_ID = m_PriceList_Version_ID;
    }

    public int getM_Product_ID() {
        return m_Product_ID;
    }

    public void setM_Product_ID(int m_Product_ID) {
        this.m_Product_ID = m_Product_ID;
    }

    public BigDecimal getPriceLimit() {
        return priceLimit;
    }

    public void setPriceLimit(BigDecimal priceLimit) {
        this.priceLimit = priceLimit;
    }

    public BigDecimal getPriceList() {
        return priceList;
    }

    public void setPriceList(BigDecimal priceList) {
        this.priceList = priceList;
    }

    public BigDecimal getPriceStd() {
        return priceStd;
    }

    public void setPriceStd(BigDecimal priceStd) {
        this.priceStd = priceStd;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
