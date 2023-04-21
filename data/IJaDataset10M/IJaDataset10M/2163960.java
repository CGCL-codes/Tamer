package org.slasoi.bslamanager.main.context.impl;

import org.slasoi.bslamanager.main.context.BusinessContextService;
import org.slasoi.gslam.core.context.SLAManagerContext;
import org.springframework.stereotype.Service;

@Service(value = "businessContextService")
public class BusinessContextServiceImpl implements BusinessContextService {

    protected SLAManagerContext bContext;

    public BusinessContextServiceImpl() {
    }

    public BusinessContextServiceImpl(SLAManagerContext context) {
        this.bContext = context;
    }

    public SLAManagerContext getBusinessContext() {
        return bContext;
    }

    public void setBusinessContext(SLAManagerContext context) {
        this.bContext = context;
    }
}
