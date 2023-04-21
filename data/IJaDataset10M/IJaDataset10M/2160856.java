package de.strategytester.tradingadvisor;

import java.util.HashSet;
import java.util.TreeSet;
import Trading.Symbol;
import de.fessermn.trading.TimeBase;
import de.strategytester.exitcriteria.ExitBreakout;
import de.strategytester.exitcriteria.ExitCriteriaType;
import de.strategytester.recommendation.TradingDirectionType;
import de.strategytester.recommendation.TradingRecommendation;
import de.strategytester.tickhandler.StorageContainer;
import de.strategytester.tickhandler.mappingfunction.AMultiTickMappingFunction;
import de.strategytester.tickhandler.mappingfunction.HistPriceValueType;
import de.strategytester.tickhandler.mappingfunction.MappingAverage;
import de.strategytester.tickhandler.mappingfunction.MappingDistance;
import de.strategytester.tickhandler.mappingfunction.MappingMinMax;
import de.strategytester.tickhandler.mappingfunction.MappingStorageObject;
import de.strategytester.tickhandler.mappingfunction.MappingTrueRange;
import de.strategytester.tickhandler.mappingfunction.MappingUnderlying;
import de.strategytester.tickhandler.mappingfunction.MappingAverage.AverageType;
import de.strategytester.tickhandler.mappingfunction.MappingMinMax.MinMaxType;
import de.strategytester.tickhandler.utils.RegisterInfo;

/**
 * The Class BreakoutTradingAdvisor.
 * <p>
 * Prepare the advisor by adding all the needed mapping functions to the
 * receiver list
 */
public class BreakoutTradingAdvisor extends ATradingAdvisor {

    private AMultiTickMappingFunction minLong = null;

    private AMultiTickMappingFunction maxLong = null;

    private AMultiTickMappingFunction curCloseMinLong = null;

    private AMultiTickMappingFunction curCloseMaxLong = null;

    private AMultiTickMappingFunction atr = null;

    private AMultiTickMappingFunction volatilityN = null;

    public BreakoutTradingAdvisor(HashSet<RegisterInfo> registerInfos) {
        this.registerInfos = registerInfos;
        final MappingUnderlying underlyingLow = new MappingUnderlying(HistPriceValueType.LOW_PRICE);
        final MappingUnderlying underlyingClose = new MappingUnderlying(HistPriceValueType.CLOSE_PRICE);
        this.minLong = new MappingMinMax(MinMaxType.MIN, underlyingLow, registerInfos, 20, 0, false, true);
        this.maxLong = new MappingMinMax(MinMaxType.MAX, underlyingLow, registerInfos, 20, 0, false, true);
        this.curCloseMinLong = new MappingDistance(underlyingClose, this.minLong, registerInfos, false, true);
        this.curCloseMaxLong = new MappingDistance(underlyingClose, this.maxLong, registerInfos, false, true);
        this.atr = new MappingTrueRange(registerInfos);
        this.volatilityN = new MappingAverage(AverageType.EXPONENTIAL, this.atr, registerInfos, 20);
        this.mappingFunctions.add(this.minLong);
        this.mappingFunctions.add(this.maxLong);
        this.mappingFunctions.add(this.curCloseMinLong);
        this.mappingFunctions.add(this.curCloseMaxLong);
        this.mappingFunctions.add(this.atr);
        this.mappingFunctions.add(this.volatilityN);
    }

    @Override
    protected void addTradingRecommendation(final TreeSet<TradingRecommendation> result, final StorageContainer storageContainer, final Symbol symbol, TimeBase timeBase) {
        final MappingStorageObject storageObject = storageContainer.getElement(symbol, timeBase, 0);
        final MappingStorageObject storageObjectPrev = storageContainer.getElement(symbol, timeBase, 1);
        if (storageObject != null) {
            final AMultiTickMappingFunction localMinLong = this.curCloseMinLong;
            if (localMinLong != null) {
                if (localMinLong.getUnderlyingValue(storageObject) < 0.0) {
                    final TradingRecommendation recommendation = new TradingRecommendation();
                    final Double primaryPrice = this.minLong.getUnderlyingValue(storageObjectPrev);
                    final Double n = this.volatilityN.getUnderlyingValue(storageObject);
                    recommendation.setPrice(primaryPrice);
                    recommendation.setDate(storageObject.getHistPrice().getEndDate());
                    final ExitBreakout exitBreakout = new ExitBreakout();
                    exitBreakout.setMinMaxType(MinMaxType.MIN);
                    recommendation.setExitCriteria(ExitCriteriaType.getDBExitCriteria(exitBreakout));
                    recommendation.setPrimaryStop(primaryPrice + 2 * n);
                    recommendation.setPrimaryTakeProfit(primaryPrice - 5 * n);
                    recommendation.setProbablity(0.35);
                    recommendation.setSymbol(symbol);
                    recommendation.setTradingDirectionType(TradingDirectionType.SHORT);
                    result.add(recommendation);
                }
            }
            if (this.curCloseMaxLong.getUnderlyingValue(storageObject) < 0) {
                final TradingRecommendation recommendation = new TradingRecommendation();
                final Double primaryPrice = this.minLong.getUnderlyingValue(storageObjectPrev);
                final Double n = this.volatilityN.getUnderlyingValue(storageObject);
                recommendation.setPrice(primaryPrice);
                recommendation.setDate(storageObject.getHistPrice().getEndDate());
                final ExitBreakout exitBreakout = new ExitBreakout();
                exitBreakout.setMinMaxType(MinMaxType.MIN);
                recommendation.setExitCriteria(ExitCriteriaType.getDBExitCriteria(exitBreakout));
                recommendation.setPrimaryStop(primaryPrice + 2 * n);
                recommendation.setPrimaryTakeProfit(primaryPrice - 5 * n);
                recommendation.setProbablity(0.35);
                recommendation.setSymbol(symbol);
                recommendation.setTradingDirectionType(TradingDirectionType.SHORT);
                result.add(recommendation);
            }
        }
    }
}
