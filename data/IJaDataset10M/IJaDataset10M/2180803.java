package org.slasoi.businessManager.common.service;

import java.util.List;
import org.slasoi.businessManager.common.model.EmBillingFrecuency;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface BillingFrecuenciesManager {

    @Transactional(propagation = Propagation.REQUIRED)
    public abstract List<EmBillingFrecuency> getBillingFrecuencies();

    @Transactional(propagation = Propagation.REQUIRED)
    public abstract EmBillingFrecuency getBillingFrecuencyById(Long id);

    @Transactional(propagation = Propagation.REQUIRED)
    public abstract void saveBillingFrecuency(EmBillingFrecuency object);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public abstract void deleteBillingFrecuencies(List<Long> ids);
}
