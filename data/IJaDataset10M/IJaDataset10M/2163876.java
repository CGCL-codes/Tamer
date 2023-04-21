package model.trader.trade;

import java.util.ArrayList;
import java.util.List;
import model.market.MarketInformation;
import model.market.MarketManager;
import model.market.Position;
import model.market.Trade;
import model.trader.TradeInstruction;
import model.trader.Trader;

public class DemandCurveStrategy implements TradeExecutionStrategy {

    DemandCurveTradeStructurer demandCurve = new DemandCurveTradeStructurer();

    public java.util.List<Trade> trade(Trader trader, Position pos, TradeInstruction[] instructions, MarketManager market, MarketInformation marketInfo) {
        List<Trade> ret = new ArrayList<Trade>();
        for (int i = 0; i < instructions.length; i++) {
            List<Trade> determineTrades = demandCurve.determineTrades(trader, pos, instructions[i], marketInfo);
            ret.addAll(determineTrades);
        }
        return ret;
    }
}
