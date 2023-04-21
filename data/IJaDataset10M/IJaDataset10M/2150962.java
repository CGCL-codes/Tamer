package org.slasoi.businessManager.common.service.promotion;

import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface PromotionManagement {

    public abstract List<PromotionType> getPromotionTypes();

    @Transactional(propagation = Propagation.REQUIRED)
    public abstract Promotion getPromotionByType(Long promoTypeId) throws Exception;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public abstract void savePromotion(Promotion promotion) throws Exception;
}
