package org.zeroexchange.resource.money.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.zeroexchange.dao.DAOFactory;
import org.zeroexchange.dao.resource.SupplyDAO;
import org.zeroexchange.model.resource.participant.Supply;
import org.zeroexchange.model.user.User;

/**
 * @author black
 *
 */
public abstract class AbstractCurrencyManager implements CurrencyManager {

    @Autowired
    private DAOFactory daoFactory;

    private BigDecimal maxRate = new BigDecimal(5).setScale(2);

    private BigDecimal minRate = new BigDecimal(1).setScale(2);

    private BigDecimal minimalAveragePrice = new BigDecimal(10).setScale(2);

    public void setMinimalAveragePrice(BigDecimal minimalAveragePrice) {
        this.minimalAveragePrice = minimalAveragePrice;
    }

    public void setMinRate(BigDecimal minRate) {
        this.minRate = minRate;
    }

    public void setMaxRate(BigDecimal maxRate) {
        this.maxRate = maxRate;
    }

    protected BigDecimal getMaxRate() {
        return maxRate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getHourRate(BigDecimal hourPrice, String currencyCode, User user) {
        BigDecimal rate = hourPrice.divide(getRateScale(currencyCode), RoundingMode.HALF_UP);
        if (rate.compareTo(maxRate) > 0) {
            rate = maxRate;
        } else if (rate.compareTo(minRate) < 0) {
            rate = minRate;
        }
        return rate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getHourPrice(BigDecimal rate, String currencyCode, User user) {
        return getRateScale(currencyCode).multiply(rate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOutlayAllowed(String currencyCode, User user, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        }
        BigDecimal availableAmount = getAvailableAmount(currencyCode, user);
        BigDecimal allowedDebt = getAllowedDebt(currencyCode, user);
        return availableAmount.add(amount).compareTo(allowedDebt) >= 0;
    }

    /**
     * Returns scale of the rate.
     */
    private BigDecimal getRateScale(String currencyCode) {
        SupplyDAO supplyDAO = daoFactory.getDAOForEntity(Supply.class);
        BigDecimal averagePrice = supplyDAO.getAverageSoldPrice();
        if (minimalAveragePrice.compareTo(averagePrice) > 0) {
            averagePrice = minimalAveragePrice;
        }
        BigDecimal maximalPrice = averagePrice.multiply(new BigDecimal(2));
        return maximalPrice.divide(getMaxRate(), RoundingMode.HALF_UP);
    }
}
