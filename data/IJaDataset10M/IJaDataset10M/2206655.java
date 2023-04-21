package com.acv.connector.ocean.price;

import com.acv.common.model.entity.Price;
import com.acv.common.model.entity.PriceTag;
import com.acv.common.model.entity.Taxes;

public class PriceTagInfo implements PriceTag {

    private Price grossPrice = new Price(0f);

    private Taxes taxes = new Taxes();

    private boolean commissionable = false;

    public Price getGrossPrice() {
        return grossPrice;
    }

    public void setGrossPrice(Price grossPrice) {
        this.grossPrice = grossPrice;
    }

    public Taxes getTaxes() {
        return taxes;
    }

    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    public Price getTotalPrice() {
        Price totalPrice = new Price();
        totalPrice.add(grossPrice);
        return totalPrice;
    }

    public boolean isCommissionable() {
        return commissionable;
    }

    public void setCommissionable(boolean commissionable) {
        this.commissionable = commissionable;
    }
}
