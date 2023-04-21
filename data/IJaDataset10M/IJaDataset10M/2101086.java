package org.broadleafcommerce.pricing.service.module;

import org.broadleafcommerce.order.domain.FulfillmentGroup;

public class USPSShippingModule implements ShippingModule {

    private String name = "USPSShippingPriceModule";

    public FulfillmentGroup calculateShippingForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
