package org.broadleafcommerce.core.pricing.service;

import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.pricing.service.exception.PricingException;

public interface PricingService {

    public Order executePricing(Order order) throws PricingException;
}
