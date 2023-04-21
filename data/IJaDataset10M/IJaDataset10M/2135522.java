package com.csft.market.background.simulation;

import java.util.Calendar;
import java.util.List;
import com.csft.market.domain.Price;
import com.csft.market.domain.Simulation;
import com.csft.market.domain.Stock;
import com.csft.market.domain.StockSimulation;
import com.csft.market.domain.StockSimulationDao;

public class BuyAt52WeekLowAndSellAt5PercentLoss extends Strategy {

    public BuyAt52WeekLowAndSellAt5PercentLoss(StockSimulationDao stockSimulationDao, Simulation simulation, Stock stock, List<Price> prices) {
        super(stockSimulationDao, simulation, stock, prices);
    }

    @Override
    public void simulate() {
        double cash = 10000;
        long share = 0;
        boolean positioned = false;
        double high = 0;
        Calendar oneYearAgo = Calendar.getInstance();
        oneYearAgo.add(Calendar.YEAR, -1);
        for (int i = 0; i < prices.size(); i++) {
            Price price = prices.get(prices.size() - 1 - i);
            if (price.getCalendar().before(oneYearAgo)) {
                continue;
            }
            if (price.getOneYearLow() == true && positioned == false) {
                double purchasePrice = price.getAdjustedClose();
                share = (long) (cash / purchasePrice);
                cash -= share * purchasePrice;
                nTrade++;
                positioned = true;
            }
            if (positioned && share > 0) {
                if (high < price.getAdjustedClose()) {
                    high = price.getAdjustedClose();
                }
                if (price.getAdjustedClose() < 0.95 * high) {
                    double sellPrice = price.getAdjustedClose();
                    cash += share * sellPrice;
                    share = 0;
                    nTrade++;
                }
            }
            balance = cash + share * price.getAdjustedClose();
        }
        StockSimulation ss = new StockSimulation();
        ss.setBalance(balance);
        ss.setnTrade(nTrade);
        ss.setnShare(share);
        ss.setSimulation(simulation);
        ss.setStock(stock);
        stockSimulationDao.save(ss);
    }
}
