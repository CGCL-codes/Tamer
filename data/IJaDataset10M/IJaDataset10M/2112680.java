package com.zara.store.client.clientmodel;

import java.math.BigDecimal;
import com.zara.store.common.PalcItemViewVO;
import com.zara.store.common.ProductVO;

public class RetailBasketLine implements BasketLine {

    private static final long serialVersionUID = 1L;

    private boolean offer = true;

    private BigDecimal amount = BigDecimal.ONE;

    private PalcItemViewVO palcItemViewVO;

    private ProductVO productVO;

    public void setOffer(boolean offer) {
        this.offer = offer;
    }

    public void setAmount(BigDecimal amount) {
        if (palcItemViewVO == null) {
            if (amount.compareTo(productVO.getStock()) > 0) {
                return;
            }
        }
        this.amount = amount;
    }

    public BigDecimal getLineTotal() {
        return amount.multiply(getPrice());
    }

    public BigDecimal getPrice() {
        return (offer && productVO.getSalePrice() != null) ? productVO.getSalePrice() : productVO.getUnitPrice();
    }

    public RetailBasketLine(BasketProduct product) {
        this.productVO = product.getProductVO();
        this.palcItemViewVO = product.getPalcItemViewVO();
        this.offer = hasOfferPrice();
    }

    public String getProductName() {
        return productVO.getName();
    }

    public boolean getOffer() {
        return offer;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getProductId() {
        return productVO.getId();
    }

    public boolean hasOfferPrice() {
        return productVO.getSalePrice() != null;
    }

    public BigDecimal getAmountPendingOnLastPalc() {
        return (palcItemViewVO == null) ? null : palcItemViewVO.getAmountPendingOnLastPalc();
    }

    public BigDecimal getAmountSoldLastWeek() {
        return (palcItemViewVO == null) ? null : palcItemViewVO.getAmountSoldLastWeek();
    }

    public boolean isAvailableInOfad() {
        return (palcItemViewVO == null) ? false : palcItemViewVO.isAvailableInOfad();
    }

    public BigDecimal getStock() {
        return productVO.getStock();
    }

    public BigDecimal getOrderStock() {
        return productVO.getOrderStock();
    }

    public String getProductBarcode() {
        return productVO.getBarcode();
    }
}
