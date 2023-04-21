package com.tredart.strategy.impl;

import static com.tredart.entities.ESide.LONG;
import static com.tredart.entities.ESide.SHORT;
import static com.tredart.strategy.IParameterIds.BUY_CLOSE_TOLERANCE;
import static com.tredart.strategy.IParameterIds.BUY_OPEN_TOLERANCE;
import static com.tredart.strategy.IParameterIds.MOVING_AVG_LENGTH;
import static com.tredart.strategy.IParameterIds.PRODUCT;
import static com.tredart.strategy.IParameterIds.SELL_CLOSE_TOLERANCE;
import static com.tredart.strategy.IParameterIds.SELL_OPEN_TOLERANCE;
import com.tredart.entities.ESide;
import com.tredart.entities.IPrice;
import com.tredart.entities.impl.Product;
import com.tredart.entities.impl.SpreadBetTransaction;
import com.tredart.math.SlidingMeanAverage;
import com.tredart.simulator.IConfiguration;
import com.tredart.simulator.impl.Configuration;
import com.tredart.utils.TimeStamp;

/**
 * Implementation of the {@IStrategy}.
 * 
 * @author george
 * @author fdegrazia
 */
public class SMAWithToleranceStrategy extends AbstractStrategy {

    private Product product;

    private int movingAverageLength;

    private Double buyOpenTolerance = 0d;

    private Double buyCloseTolerance = null;

    private Double sellOpenTolerance = 0d;

    private Double sellCloseTolerance = null;

    protected SlidingMeanAverage slidingAverage;

    /**
     * Returns the mid price for the price.
     * 
     * @param price
     *            the price
     * @return the mid price
     */
    protected double getMidPrice(final IPrice price) {
        return (price.getBuy() + price.getSell()) / 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean run(final TimeStamp ts) {
        final IPrice price = find(product, ts);
        if (price == null) {
            return false;
        }
        if (slidingAverage == null) {
            initialise(product, ts, movingAverageLength, getMidPrice(price));
        } else {
            slidingAverage.slide(getMidPrice(price));
        }
        final ESide currentSide = getTradingSystem().getCurrentSide(product);
        final SpreadBetTransaction newTransaction = makeDecision(product, buyOpenTolerance, sellOpenTolerance, buyCloseTolerance, sellCloseTolerance, price, slidingAverage.getAverage(), currentSide);
        if (newTransaction != null) {
            getTradingSystem().addTransaction(newTransaction);
        }
        return true;
    }

    /**
     * Initialise the moving average.
     * 
     * @param theProduct
     *            the product to load prices for
     * @param ts
     *            the timestamp from which the past data should be retrieved
     * @param theLength
     *            of the moving average data
     * @param tsPrice
     *            the price at ts
     */
    protected void initialise(final Product theProduct, final TimeStamp ts, final int theLength, final double tsPrice) {
        slidingAverage = new SlidingMeanAverage(theLength);
        final double[] theValues = new double[theLength];
        theValues[theLength - 1] = tsPrice;
        TimeStamp processingTs = ts.addDays(-1);
        int numProcessed = 1;
        while (numProcessed < theLength) {
            final IPrice price = getTradingSystem().getPrice(theProduct, processingTs);
            if (price != null) {
                final double mid = (price.getBuy() + price.getSell()) / 2;
                numProcessed++;
                theValues[theLength - numProcessed] = mid;
            }
            processingTs = processingTs.addDays(-1);
        }
        slidingAverage.initialise(theValues);
    }

    /**
     * Make the decision:<br/>
     * <ul>
     * <li>price &gt; moving average and position is not LONG =&gt; BUY</li>
     * <li>price = moving average =&gt; DO_NOTHING</li>
     * <li>price &lt; moving average and position is not SHORT =&gt; SELL.</li>
     * </ul>
     * 
     * @param theProduct
     *            the product
     * @param theBuyOpenTolerance
     *            the tolerance
     * @param theSellOpenTolerance
     *            the tolerance
     * @param theBuyCloseTolerance
     *            the tolerance for when to close a LONG position, may be null
     * @param theSellCloseTolerance
     *            the tolerance for when to close a SHORT position, may be null
     * @param thePrice
     *            the current price
     * @param movingAverage
     *            the moving average
     * @param currentPosition
     *            the current position
     * @return the decision
     */
    protected SpreadBetTransaction makeDecision(final Product theProduct, final Double theBuyOpenTolerance, final Double theSellOpenTolerance, final Double theBuyCloseTolerance, final Double theSellCloseTolerance, final IPrice thePrice, final double movingAverage, final ESide currentPosition) {
        final double midPrice = getMidPrice(thePrice);
        final int betSize;
        if (currentPosition == ESide.UNDEFINED) {
            betSize = 1;
        } else {
            betSize = 2;
        }
        final double movingAveragePlusBuyTolerance = movingAverage * (1 + theBuyOpenTolerance);
        if (midPrice > movingAveragePlusBuyTolerance && currentPosition != LONG) {
            final SpreadBetTransaction transaction = new SpreadBetTransaction();
            transaction.setProduct(theProduct);
            transaction.setInitialPrice(thePrice.getBuy());
            transaction.setBetSize(betSize);
            transaction.setBuy(true);
            transaction.computeAndSetStopLoss(thePrice, theProduct.getDefaultStopLoss());
            return transaction;
        }
        final double movingAverageMinusSellTolerance = movingAverage * (1 - theSellOpenTolerance);
        if (midPrice < movingAverageMinusSellTolerance && currentPosition != SHORT) {
            final SpreadBetTransaction transaction = new SpreadBetTransaction();
            transaction.setProduct(theProduct);
            transaction.setInitialPrice(thePrice.getSell());
            transaction.setBetSize(betSize);
            transaction.setBuy(false);
            transaction.computeAndSetStopLoss(thePrice, theProduct.getDefaultStopLoss());
            return transaction;
        }
        if (theBuyCloseTolerance != null) {
            final double closeBuyAverage = movingAverage * (1 + theBuyCloseTolerance);
            if (midPrice < closeBuyAverage && currentPosition == LONG) {
                final SpreadBetTransaction transaction = new SpreadBetTransaction();
                transaction.setProduct(theProduct);
                transaction.setInitialPrice(thePrice.getSell());
                transaction.setBetSize(1);
                transaction.setBuy(false);
                transaction.computeAndSetStopLoss(thePrice, theProduct.getDefaultStopLoss());
                return transaction;
            }
        }
        if (theSellCloseTolerance != null) {
            final double closeSellAverage = movingAverage * (1 - theSellCloseTolerance);
            if (midPrice > closeSellAverage && currentPosition == SHORT) {
                final SpreadBetTransaction transaction = new SpreadBetTransaction();
                transaction.setProduct(theProduct);
                transaction.setInitialPrice(thePrice.getBuy());
                transaction.setBetSize(1);
                transaction.setBuy(true);
                transaction.computeAndSetStopLoss(thePrice, theProduct.getDefaultStopLoss());
                return transaction;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public IConfiguration getConfiguration() {
        final Configuration config = new Configuration();
        config.addParameter(new ProductParameter(PRODUCT, "The product which the strategy runs on", product));
        config.addParameter(new IntegerParameter(MOVING_AVG_LENGTH, "The length of the moving average", movingAverageLength));
        config.addParameter(new DoubleParameter(BUY_OPEN_TOLERANCE, "The Buy Open Tolerance", buyOpenTolerance, 0, 1.0, false));
        config.addParameter(new DoubleParameter(BUY_CLOSE_TOLERANCE, "The Buy Close Tolerance", buyCloseTolerance, 0, 1.0, true));
        config.addParameter(new DoubleParameter(SELL_OPEN_TOLERANCE, "The Sell Open Tolerance", sellOpenTolerance, 0, 1.0, false));
        config.addParameter(new DoubleParameter(SELL_CLOSE_TOLERANCE, "The Sell Close Tolerance", sellCloseTolerance, 0, 1.0, true));
        return config;
    }

    /**
     * {@inheritDoc}
     */
    public void setConfiguration(final IConfiguration config) {
        if (config.getParameter(PRODUCT) != null) {
            product = (Product) config.getParameter(PRODUCT).getValue();
        }
        if (config.getParameter(MOVING_AVG_LENGTH) != null) {
            movingAverageLength = (Integer) config.getParameter(MOVING_AVG_LENGTH).getValue();
        }
        if (config.getParameter(BUY_OPEN_TOLERANCE) != null) {
            buyOpenTolerance = (Double) config.getParameter(BUY_OPEN_TOLERANCE).getValue();
        }
        if (config.getParameter(BUY_CLOSE_TOLERANCE) != null) {
            buyCloseTolerance = (Double) config.getParameter(BUY_CLOSE_TOLERANCE).getValue();
        }
        if (config.getParameter(SELL_OPEN_TOLERANCE) != null) {
            sellOpenTolerance = (Double) config.getParameter(SELL_OPEN_TOLERANCE).getValue();
        }
        if (config.getParameter(SELL_CLOSE_TOLERANCE) != null) {
            sellCloseTolerance = (Double) config.getParameter(SELL_CLOSE_TOLERANCE).getValue();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearState() {
        super.clearState();
        slidingAverage = null;
    }

    /**
     * Returns the current moving average as it us currently stored in the
     * strategy.
     * 
     * @return the current moving average
     */
    public double getMovingAverage() {
        return slidingAverage == null ? 0d : slidingAverage.getAverage();
    }

    /**
     * Returns the moving average length as it is currently set up in the
     * strategy.
     * 
     * @return the moving average length
     */
    public int getMovingAverageLength() {
        return movingAverageLength;
    }

    /**
     * Returns the product on which this strategy is acting.
     * 
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Returns the last price that the strategy came accross.
     * 
     * @return
     */
    public double getLastPrice() {
        return slidingAverage == null ? 0d : slidingAverage.getLastValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPrice find(final Product product, final TimeStamp ts) {
        return getTradingSystem().getPrice(product, ts);
    }
}
