package com.dongsheng.manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.dongsheng.dao.StockDao;
import com.dongsheng.db.Stock;
import com.dongsheng.db.StocksPrice;
import com.dongsheng.util.Util;

public class FetchStockInfo {

    private static final Logger logger = Logger.getLogger(FetchStockInfo.class);

    private StockDao stockDao;

    public List<Stock> getSHStock(String fileName) {
        List<Stock> list = new ArrayList<Stock>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            reader.readLine();
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.replace("\"", "").replace(";", "").split(",");
                Stock stock = new Stock();
                stock.setStockNo(item[0]);
                stock.setStockName(item[1]);
                stock.setStockCategory(item[2]);
                stock.setStockMarket("ss");
                stock.setStockIssueDate("2007-01-01");
                list.add(stock);
            }
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return list;
    }

    public List<Stock> getSZStock(String fileName) {
        List<Stock> list = new ArrayList<Stock>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            reader.readLine();
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.replace("\"", "").replace(";", "").split(",");
                Stock stock = new Stock();
                stock.setStockNo(item[0]);
                stock.setStockName(item[1]);
                stock.setStockIssueDate(item[2]);
                stock.setStockCategory(item[6].split(" ")[0]);
                stock.setStockMarket("sz");
                list.add(stock);
            }
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return list;
    }

    public List<StocksPrice> getStockPrice(String fileName, String stockNo) {
        List<StocksPrice> list = new ArrayList<StocksPrice>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            reader.readLine();
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.replace("\"", "").replace(";", "").split(",");
                StocksPrice stPrice = new StocksPrice();
                stPrice.setStock(stockDao.getStockByNo(stockNo));
                stPrice.setStockDate(Util.formatDate(item[0]));
                stPrice.setPriceClose(new BigDecimal(item[4]));
                list.add(stPrice);
                line = null;
                item = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
	 * 
	 * @param fileName
	 * @param stockNo
	 * @param stock
	 * @param from start date on ...
	 * @return stock price list
	 */
    public List<StocksPrice> getStockPriceWithStockInfo(String fileName, String stockNo, Stock stock, Date fromDate, Date toDate) {
        List<StocksPrice> list = new ArrayList<StocksPrice>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            reader.readLine();
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.replace("\"", "").replace(";", "").split(",");
                if (!Util.formatDate(item[0]).before(fromDate) && !Util.formatDate(item[0]).after(toDate)) {
                    StocksPrice stPrice = new StocksPrice();
                    stPrice.setStock(stock);
                    stPrice.setStockDate(Util.formatDate(item[0]));
                    stPrice.setPriceClose(new BigDecimal(item[4]));
                    list.add(stPrice);
                }
                line = null;
                item = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public StockDao getStockDao() {
        return stockDao;
    }

    public void setStockDao(StockDao stockDao) {
        this.stockDao = stockDao;
    }
}
