package org.slasoi.businessManager.billingEngine.service;

import java.util.List;
import org.slasoi.businessManager.common.model.billing.EmAccountEventType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface AccountEventTypeManager {

    @Transactional(propagation = Propagation.REQUIRED)
    public abstract List<EmAccountEventType> getAccountEventTypes();

    @Transactional(propagation = Propagation.REQUIRED)
    public abstract EmAccountEventType getEmAccountEventTypeById(Long id);

    @Transactional(propagation = Propagation.REQUIRED)
    public abstract void saveAccountEventType(EmAccountEventType object);

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public abstract void deleteAccountEventTypes(List<Long> ids);
}
