package model.market;

public class RiskFreeFinanceCharge implements FinanceCharge {

    public double getPeriodFinanceCharge(Position pos, MarketInformation marketInfo, MarketManager manager, double currentRiskFree, double indexDivYield, double indexCapitalReturn) {
        return -pos.getWorth(marketInfo.getMarketPrices()) * currentRiskFree / manager.getConfig().getPeriodsInYear();
    }
}
