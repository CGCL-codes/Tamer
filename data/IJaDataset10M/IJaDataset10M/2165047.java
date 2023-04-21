package org.slasoi.businessManager.billingEngine.mockups;

import org.slasoi.bslamanager.main.context.BusinessContextService;
import org.slasoi.gslam.core.context.SLAManagerContext;

public class BusinessContextServiceMockup implements BusinessContextService {

    protected SLAManagerContext businessContext;

    public BusinessContextServiceMockup() {
    }

    public SLAManagerContext getBusinessContext() {
        return businessContext;
    }

    public void setBusinessContext(SLAManagerContext context) {
        this.businessContext = context;
    }
}
