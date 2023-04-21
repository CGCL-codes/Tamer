package org.broadleafcommerce.pricing.service.workflow;

import org.broadleafcommerce.order.domain.Order;
import org.broadleafcommerce.workflow.ProcessContext;

public class PricingContext implements ProcessContext {

    public static final long serialVersionUID = 1L;

    private boolean stopEntireProcess = false;

    private Order seedData;

    public void setSeedData(Object seedObject) {
        seedData = (Order) seedObject;
    }

    public boolean stopProcess() {
        this.stopEntireProcess = true;
        return stopEntireProcess;
    }

    public boolean isStopped() {
        return stopEntireProcess;
    }

    public Order getSeedData() {
        return seedData;
    }
}
