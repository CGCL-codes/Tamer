package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import orm.PMF;
import orm.Stock;
import pojo.StockDailyInfo;
import util.CrawlUtil;
import util.DateUtil;
import com.google.appengine.api.users.User;

public class StockServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        checkSign(req, resp);
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            Long key = new Long(req.getParameter("key"));
            PersistenceManager pm = PMF.get().getPersistenceManager();
            Stock stock = (Stock) pm.getObjectById(Stock.class, key);
            try {
                pm.deletePersistent(stock);
            } finally {
                pm.close();
            }
        }
        resp.sendRedirect("/stock.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        checkSign(req, resp);
        String selectedStock = req.getParameter("selectedStock");
        if (selectedStock != null && !selectedStock.isEmpty()) {
            User user = (User) req.getAttribute("currentUser");
            String code = selectedStock.substring(0, 8);
            String name = selectedStock.substring(9);
            Date startDate = DateUtil.formatString2Date(req.getParameter("startDate"), "yyyy-MM-dd");
            Date endDate = DateUtil.formatString2Date(req.getParameter("endDate"), "yyyy-MM-dd");
            Stock stock = new Stock(user, code, name, startDate, endDate);
            ArrayList<StockDailyInfo> infos = (ArrayList<StockDailyInfo>) CrawlUtil.CrawlStockPriceInfo(stock);
            float highPrice = 0;
            float lowPrice = Float.MAX_VALUE;
            for (StockDailyInfo info : infos) {
                if (DateUtil.formatDate(startDate, "yyyy-MM-dd").equals(DateUtil.formatDate(info.getDate(), "yyyy-MM-dd"))) {
                    stock.setOpenPrice(info.getOpenPrice());
                }
                if (DateUtil.formatDate(endDate, "yyyy-MM-dd").equals(DateUtil.formatDate(info.getDate(), "yyyy-MM-dd"))) {
                    stock.setEndPrice(info.getClosePrice());
                }
                if (info.getHighPrice() > highPrice) {
                    highPrice = info.getHighPrice();
                }
                if (info.getLowPrice() < lowPrice) {
                    lowPrice = info.getLowPrice();
                }
            }
            stock.setHighPrice(highPrice);
            stock.setLowPrice(lowPrice);
            PersistenceManager pm = PMF.get().getPersistenceManager();
            try {
                pm.makePersistent(stock);
            } finally {
                pm.close();
            }
        }
        resp.sendRedirect("/stock.jsp");
    }
}
