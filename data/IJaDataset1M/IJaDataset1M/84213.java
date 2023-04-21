package br.perfiman.service.impl.patterns.strategy.brokerage;

import br.perfiman.model.Asset;
import br.perfiman.model.Trade;

/**
 * Strategy to calculates the brokerages's value using static values to each order
 * 
 * @author Wagner Costa
 * @since 28/04/2008
 * @version 1.0
 *
 */
public class BrokerageStaticStrategy extends BrokerageStrategy {

    @Override
    public Double getBrokerage(Trade trade) {
        switch(trade.getAsset().getType()) {
            case Asset.TYPE_FULL:
                return trade.getBroker().getFullPrice();
            case Asset.TYPE_FRACTIONAL:
                return trade.getBroker().getFractionalPrice();
            case Asset.TYPE_OPTIONS:
                return trade.getBroker().getOptionsPrice();
        }
        return null;
    }
}
