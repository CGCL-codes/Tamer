package org.posterita.model;

import org.compiere.model.MPriceListVersion;

public class UDIMPriceListVersion extends UDIPO {

    public UDIMPriceListVersion(MPriceListVersion priceListVersion) {
        super(priceListVersion);
    }

    public MPriceListVersion getMPriceListVersion() {
        return (MPriceListVersion) getPO();
    }

    public void setM_DiscountSchema_ID(int discountSchemaID) {
        getMPriceListVersion().setM_DiscountSchema_ID(discountSchemaID);
    }

    public void setM_PriceList_ID(int priceListID) {
        getMPriceListVersion().setM_PriceList_ID(priceListID);
    }
}
