package model.trader.trade;

import java.util.ArrayList;
import java.util.List;
import model.market.MarketInformation;
import model.market.Position;
import model.market.Trade;
import model.trader.TradeInstruction;
import model.trader.Trader;
import model.trader.TradeInstruction.IndicativePricePair;

public class DemandCurveTradeStructurer {

    public List<Trade> determineTrades(Trader trader, Position pos, TradeInstruction demandCurve, MarketInformation marketInfo) {
        List<Trade> ret = new ArrayList<Trade>();
        if (pos.getExecutedQuantity(demandCurve.getAsset()) < 0) {
            Trade closeMistake = new Trade(trader, demandCurve.getAsset(), -(pos.getExecutedQuantity(demandCurve.getAsset())));
            ret.add(closeMistake);
        }
        Trade directionalTrade = new Trade(trader, demandCurve.getAsset(), demandCurve.getNumberToTrade());
        ret.add(directionalTrade);
        List<IndicativePricePair> additional = demandCurve.getAdditional();
        int demandAtCurrentSimPrice = demandCurve.getNumberToTrade();
        int demandAtMarketPrice = demandCurve.getNumberToTrade();
        int totalCancelled = 0;
        for (IndicativePricePair pair : additional) {
            if (demandAtMarketPrice < 0 && demandAtCurrentSimPrice > 0 || demandAtMarketPrice > 0 && demandAtCurrentSimPrice < 0 || demandAtMarketPrice < 0 && pair.getTrade() > 0 || demandAtMarketPrice > 0 && pair.getTrade() < 0) {
                Trade compensate = new Trade(trader, demandCurve.getAsset(), -(demandCurve.getNumberToTrade() - totalCancelled), pair.getPrice());
                ret.add(compensate);
                break;
            }
            if (demandAtMarketPrice < 0 && demandAtCurrentSimPrice > pair.getTrade() || demandAtMarketPrice > 0 && demandAtCurrentSimPrice < pair.getTrade()) {
                continue;
            }
            int tradeNumber = (demandAtCurrentSimPrice - pair.getTrade());
            totalCancelled += tradeNumber;
            Trade compensate = new Trade(trader, demandCurve.getAsset(), -tradeNumber, pair.getPrice());
            demandAtCurrentSimPrice = (pair.getTrade());
            ret.add(compensate);
        }
        return ret;
    }
}
