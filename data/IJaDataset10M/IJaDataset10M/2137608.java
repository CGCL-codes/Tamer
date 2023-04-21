package com.tredart.strategy.impl;

import static com.tredart.strategy.IParameterIds.BUY_PRICE_THRESHOLD;
import static com.tredart.strategy.IParameterIds.PRODUCT;
import static com.tredart.strategy.IParameterIds.SELL_PRICE_THRESHOLD;
import com.tredart.entities.IPrice;
import com.tredart.entities.impl.Product;
import com.tredart.entities.impl.SpreadBetTransaction;
import com.tredart.simulator.IConfiguration;
import com.tredart.simulator.impl.Configuration;
import com.tredart.utils.TimeStamp;

/**
 * A simple naive implementation of {@link com.tredart.strategy.IStrategy}. This
 * implementation is configured with an instance of an {@link IProduct} and 2
 * thresholds, one for buy and one for sell. If the price of the product is
 * above the buy threshold, then the strategy will attempt to buy that product,
 * if the price is below the sell threshold then the strategy will attempt to
 * sell the product.
 * 
 * @author gnicoll
 * @author lw
 * 
 */
public class SimpleStrategy extends AbstractStrategy {

    /** product. */
    private Product product;

    /** Buy price threshold. */
    private double buyPriceThreshold = 10d;

    /** Sell price threshold. */
    private double sellPriceThreshold = 10d;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean run(final TimeStamp ts) {
        if (product == null) {
            throw new IllegalStateException("Product is not set");
        }
        final IPrice price = find(product, ts);
        if (price == null) {
            return false;
        }
        if (price.getBuy() > buyPriceThreshold) {
            final SpreadBetTransaction trans = new SpreadBetTransaction();
            trans.setProduct(product);
            trans.setInitialPrice(price.getBuy());
            trans.setStopLoss(9.8);
            trans.setBetSize(10);
            trans.setBuy(true);
            getTradingSystem().addTransaction(trans);
        } else if (price.getSell() < sellPriceThreshold) {
            final SpreadBetTransaction trans = new SpreadBetTransaction();
            trans.setProduct(product);
            trans.setInitialPrice(price.getSell());
            trans.setStopLoss(10.2);
            trans.setBetSize(10);
            trans.setBuy(false);
            getTradingSystem().addTransaction(trans);
        }
        return true;
    }

    /**
	 * {@inheritDoc}
	 */
    public IConfiguration getConfiguration() {
        final Configuration config = new Configuration();
        config.addParameter(new ProductParameter(PRODUCT, "The product which the strategy runs on", product));
        config.addParameter(new DoubleParameter(BUY_PRICE_THRESHOLD, "Price above which the strategy attempts to buy", buyPriceThreshold));
        config.addParameter(new DoubleParameter(SELL_PRICE_THRESHOLD, "Price below which the strategy attempts to sell", sellPriceThreshold));
        return config;
    }

    /**
     * {@inheritDoc}
     */
    public void setConfiguration(final IConfiguration config) {
        if (config.getParameter(PRODUCT) != null) {
            product = (Product) config.getParameter(PRODUCT).getValue();
        }
        if (config.getParameter(BUY_PRICE_THRESHOLD) != null) {
            buyPriceThreshold = (Double) config.getParameter(BUY_PRICE_THRESHOLD).getValue();
        }
        if (config.getParameter(SELL_PRICE_THRESHOLD) != null) {
            sellPriceThreshold = (Double) config.getParameter(SELL_PRICE_THRESHOLD).getValue();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPrice find(final Product product, final TimeStamp ts) {
        return getTradingSystem().getPrice(product, ts);
    }
}
