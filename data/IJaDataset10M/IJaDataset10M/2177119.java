package com.excelsior.portfolio.strategy;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import org.joda.time.LocalDate;
import com.excelsior.core.identifier.ExchangeIdentifier;
import com.excelsior.core.identifier.MarketDataIdentifier;
import com.excelsior.core.reference.CurrencyCode;
import com.excelsior.marketdata.item.DefaultEquityMarketDataItem;
import com.excelsior.marketdata.item.EquityMarketDataItem;
import com.excelsior.marketdata.item.EquityOptionMarketDataItem;
import com.excelsior.marketdata.item.MarketDataItem;
import com.excelsior.marketdata.item.PutOrCall;
import com.excelsior.marketdata.master.MarketDataMaster;

/**
 * The Class OptionStrategyBuilder.
 */
public class OptionStrategyBuilder {

    /** The instance. */
    private static OptionStrategyBuilder instance = null;

    /** The market data master. */
    private MarketDataMaster marketDataMaster = null;

    /**
	 * Instance.
	 * 
	 * @param marketDataMaster the market data master
	 * 
	 * @return the option strategy builder
	 */
    public static OptionStrategyBuilder instance(MarketDataMaster marketDataMaster) {
        if (instance == null) {
            instance = new OptionStrategyBuilder();
            instance.setMarketDataMaster(marketDataMaster);
        }
        return instance;
    }

    /**
	 * Gets the market data master.
	 * 
	 * @return the market data master
	 */
    protected MarketDataMaster getMarketDataMaster() {
        return marketDataMaster;
    }

    /**
	 * Sets the market data master.
	 * 
	 * @param marketDataMaster the new market data master
	 */
    protected void setMarketDataMaster(MarketDataMaster marketDataMaster) {
        this.marketDataMaster = marketDataMaster;
    }

    /**
	 * Builds the call spread.
	 * 
	 * @param underlyingId the underlying id
	 * @param quantity the quantity
	 * @param currentPrice the current price
	 * @param maturity the maturity
	 * @param minOtmStrikePercentageAbove the min otm strike percentage above
	 * @param maxOtmStrikePercentageAbove the max otm strike percentage above
	 * 
	 * @return the call spread
	 * 
	 * @throws CannotBuildCallSpreadException the cannot build spread exception
	 */
    public VerticalSpread buildCallSpread(MarketDataIdentifier underlyingId, int quantity, Double currentPrice, int startMaturity, int endMaturity, double minOtmStrikePercentageAbove, double maxOtmStrikePercentageAbove) throws CannotBuildSpreadException {
        return buildVerticalSpread(new SpreadDefinition(underlyingId, quantity, currentPrice, startMaturity, endMaturity, minOtmStrikePercentageAbove, maxOtmStrikePercentageAbove), PutOrCall.CALL);
    }

    /**
	 * Builds the put spread.
	 * 
	 * @param underlyingId the underlying id
	 * @param quantity the quantity
	 * @param currentPrice the current price
	 * @param maturity the maturity
	 * @param minOtmStrikePercentageAbove the min otm strike percentage above
	 * @param maxOtmStrikePercentageAbove the max otm strike percentage above
	 * 
	 * @return the call spread
	 * 
	 * @throws CannotBuildSpreadException the cannot build spread exception
	 */
    public VerticalSpread buildPutSpread(MarketDataIdentifier underlyingId, int quantity, Double currentPrice, int startMaturity, int endMaturity, double minOtmStrikePercentageAbove, double maxOtmStrikePercentageAbove) throws CannotBuildSpreadException {
        return buildVerticalSpread(new SpreadDefinition(underlyingId, quantity, currentPrice, startMaturity, endMaturity, minOtmStrikePercentageAbove, maxOtmStrikePercentageAbove), PutOrCall.PUT);
    }

    /**
	 * Builds the vertical spread.
	 * @param spreadDefinition 
	 * @param right. Put or call.
	 * 
	 * @return the call spread
	 * 
	 * @throws CannotBuildCallSpreadException the cannot build call spread exception
	 */
    protected VerticalSpread buildVerticalSpread(SpreadDefinition spreadDefinition, PutOrCall right) throws CannotBuildSpreadException {
        VerticalSpread callSpread = new DefaultVerticalSpread();
        callSpread.setQuantity(spreadDefinition.quantity);
        String symbol = spreadDefinition.underlyingId.getValue();
        try {
            LocalDate today = LocalDate.fromCalendarFields(Calendar.getInstance());
            LocalDate expirationFromDate = today.plusDays(spreadDefinition.startMaturity);
            LocalDate expirationToDate = expirationFromDate.plusDays(spreadDefinition.endMaturity);
            EquityMarketDataItem underlying = new DefaultEquityMarketDataItem(spreadDefinition.underlyingId.getValue(), ExchangeIdentifier.SMART, Currency.getInstance(CurrencyCode.USD.name()));
            AtmOptionFilter atmFilter = new AtmOptionFilter(underlying, spreadDefinition.currentPrice, expirationFromDate, expirationToDate, right);
            Collection<MarketDataItem> lowerStrikeLegs = marketDataMaster.getEquityOptionMarketDataItems(atmFilter);
            EquityOptionMarketDataItem lowerStrikeLeg = null;
            if (lowerStrikeLegs.size() >= 1) {
                EquityOptionMarketDataItem[] arrayOfLowerStrikeLegs = lowerStrikeLegs.toArray(new EquityOptionMarketDataItem[lowerStrikeLegs.size()]);
                Arrays.sort(arrayOfLowerStrikeLegs, new StrikeComparator(spreadDefinition.currentPrice));
                lowerStrikeLeg = arrayOfLowerStrikeLegs[0];
                lowerStrikeLeg.setIsAtmOption(true);
                callSpread.setLowerStrikeLeg(lowerStrikeLeg);
                OtmOptionFilter otmFilter = new OtmOptionFilter(lowerStrikeLeg, spreadDefinition.currentPrice, spreadDefinition.minOtmStrikePercentageAbove, spreadDefinition.maxOtmStrikePercentageAbove, right);
                Collection<MarketDataItem> higherStrikeLegs = marketDataMaster.getEquityOptionMarketDataItems(otmFilter);
                if (higherStrikeLegs.size() >= 1) {
                    EquityOptionMarketDataItem[] arrayOfHigherStrikeLegs = higherStrikeLegs.toArray(new EquityOptionMarketDataItem[higherStrikeLegs.size()]);
                    Arrays.sort(arrayOfHigherStrikeLegs, new StrikeComparator(spreadDefinition.currentPrice));
                    EquityOptionMarketDataItem higherStrikeLeg = arrayOfHigherStrikeLegs[0];
                    callSpread.setHigherStrikeLeg(higherStrikeLeg);
                } else {
                    throw new CannotBuildSpreadException("Cannot find higher strike option for call spread on: " + symbol);
                }
            } else {
                throw new CannotBuildSpreadException("Cannot find at the money option for call spread on: " + symbol);
            }
        } catch (Exception e) {
            throw new CannotBuildSpreadException(e.getMessage());
        }
        return callSpread;
    }
}
