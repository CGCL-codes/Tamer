package org.pnp.util;

import java.util.HashMap;
import java.util.Map;
import org.pnp.vo.Stock;

public class StringParser {

    public static Map<String, Stock> praseStockString(String allStockInfo) {
        Map<String, Stock> stockMap = new HashMap<String, Stock>();
        String[] stocks = allStockInfo.split(";");
        String stock = "";
        Stock stockObj;
        String stockDetailStr;
        String[] stockDetails;
        for (int i = 0; i < stocks.length; i++) {
            stock = stocks[i];
            if (stock == null || stock == "") {
                continue;
            }
            stockObj = new Stock();
            int start = stock.lastIndexOf("_");
            int end = stock.indexOf("=");
            String tempStockCode = stock.substring(start + 1, end);
            stockObj.setStockCode(tempStockCode);
            start = end + 2;
            end = stock.length() - 1;
            stockDetailStr = stock.substring(start, end);
            stockDetails = stockDetailStr.split(",");
            stockObj.setStockName(stockDetails[0]);
            stockObj.setTodayOpeningPrice(Double.parseDouble(stockDetails[1]));
            stockObj.setYTDClosingPrice(Double.parseDouble(stockDetails[2]));
            stockObj.setCurrentPrice(Double.parseDouble(stockDetails[3]));
            stockObj.setTodayHighestPrice(Double.parseDouble(stockDetails[4]));
            stockObj.setTodayLowestPrice(Double.parseDouble(stockDetails[5]));
            stockObj.setCompetedBuyPrice(Double.parseDouble(stockDetails[6]));
            stockObj.setCompetedSalePrice(Double.parseDouble(stockDetails[7]));
            stockObj.setTradeQuantity(stockDetails[8]);
            stockObj.setTradeAmount(stockDetails[9]);
            stockObj.setBuy1Quantity(Double.parseDouble(stockDetails[10]));
            stockObj.setBuy1Price(Double.parseDouble(stockDetails[11]));
            stockObj.setBuy2Quantity(Double.parseDouble(stockDetails[12]));
            stockObj.setBuy2Price(Double.parseDouble(stockDetails[13]));
            stockObj.setBuy3Quantity(Double.parseDouble(stockDetails[14]));
            stockObj.setBuy3Price(Double.parseDouble(stockDetails[15]));
            stockObj.setBuy4Quantity(Double.parseDouble(stockDetails[16]));
            stockObj.setBuy4Price(Double.parseDouble(stockDetails[17]));
            stockObj.setBuy5Quantity(Double.parseDouble(stockDetails[18]));
            stockObj.setBuy5Price(Double.parseDouble(stockDetails[19]));
            stockObj.setSale1Quantity(Double.parseDouble(stockDetails[20]));
            stockObj.setSale1Price(Double.parseDouble(stockDetails[21]));
            stockObj.setSale2Quantity(Double.parseDouble(stockDetails[22]));
            stockObj.setSale2Price(Double.parseDouble(stockDetails[23]));
            stockObj.setSale3Quantity(Double.parseDouble(stockDetails[24]));
            stockObj.setSale3Price(Double.parseDouble(stockDetails[25]));
            stockObj.setSale4Quantity(Double.parseDouble(stockDetails[26]));
            stockObj.setSale4Price(Double.parseDouble(stockDetails[27]));
            stockObj.setSale5Quantity(Double.parseDouble(stockDetails[28]));
            stockObj.setSale5Price(Double.parseDouble(stockDetails[29]));
            stockObj.setDate(stockDetails[30]);
            stockObj.setTime(stockDetails[31]);
            stockMap.put(tempStockCode, stockObj);
        }
        return stockMap;
    }
}
