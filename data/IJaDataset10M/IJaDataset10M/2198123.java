package org.zeroexchange.resource.stock;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.zeroexchange.model.resource.Resource;
import org.zeroexchange.model.resource.participant.ResourceTender;
import org.zeroexchange.model.resource.participant.TenderType;
import org.zeroexchange.model.user.User;
import org.zeroexchange.resource.read.ResourceInformant;

/**
 * @author black
 *
 */
public class GoodStockManager implements ResourceStockManager {

    private static final String ALIAS_GOOD = "good";

    @Autowired
    private ResourceInformant resourceInformant;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProcessingCategoryAlias() {
        return ALIAS_GOOD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getAmount(Resource resource, User user) {
        return new BigDecimal(Integer.MAX_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reserveAmount(ResourceTender tender, Resource resource) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void freeAmount(ResourceTender tender, Resource resource) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performAutoMovement(Resource resource) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutoMovementEnabled(Resource resource) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPriceManagementEnabled(Resource resource) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAmountManagementEnabled(Resource resource, TenderType tenderType) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getSupplyAmount(Resource resource) {
        return resourceInformant.getSupplyAmount(resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getConsumeAmount(Resource resource) {
        return resourceInformant.getConsumeAmount(resource);
    }
}
