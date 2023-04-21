package com.etc.report;

import com.etc.controller.ProductCategoryController;
import com.etc.controller.beans.ProductCategoryBeans;
import com.etc.db.oracle.Connector;
import com.etc.report.beans.StockAnalysisBeans;
import com.etc.util.CurrencyUtils;
import com.etc.util.NumberUtils;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.log4j.Logger;

/**
 *
 * @author horizonx
 */
public class StockAnalysisReport {

    public static Logger log4j = Logger.getLogger(StockAnalysisReport.class);

    public static HashMap<Object, Object> getDataSource(HashMap<Object, Object> param) {
        Collection<Object> data = new ArrayList<Object>();
        Connection conn = Connector.getConnection();
        Long sdate = (Long) param.get("SDATE");
        String proCode = (String) param.get("PROCODE");
        try {
            Statement stm = conn.createStatement();
            ResultSet raw = stm.executeQuery("SELECT CID, PROCODE, PRONAME, UNITCODE, COST FROM LOG_PRODUCT_V WHERE PROCODE LIKE '%" + proCode + "%' AND LOGTIME <= " + sdate + " GROUP BY CID, PROCODE, PRONAME, UNITCODE, COST");
            while (raw.next()) {
                ProductCategoryBeans category = ProductCategoryController.getProductCategory(raw.getInt("CID"));
                String procode = raw.getString("PROCODE");
                String proname = raw.getString("PRONAME");
                BigDecimal qty = BigDecimal.ZERO;
                BigDecimal cost = raw.getBigDecimal("COST");
                Statement sm = conn.createStatement();
                ResultSet rs = sm.executeQuery("SELECT (SELECT CASE WHEN SUM(QTY) IS NULL THEN 0 ELSE SUM(QTY) END FROM LOG_PRODUCT_V WHERE PROCODE LIKE '" + procode + "' AND TYPE = 0 AND LOGTIME <= " + sdate + ")-(SELECT CASE WHEN SUM(QTY) IS NULL THEN 0 ELSE SUM(QTY) END FROM LOG_PRODUCT_V WHERE PROCODE LIKE '" + procode + "' AND TYPE = 1 AND  LOGTIME <= " + sdate + ") QTY FROM DUAL");
                if (rs.next()) {
                    qty = rs.getBigDecimal("QTY");
                }
                rs.close();
                sm.close();
                data.add(new StockAnalysisBeans(category.getName(), procode, proname, NumberUtils.format(qty), raw.getString("UNITCODE"), CurrencyUtils.format(cost, CurrencyUtils.REPORT), CurrencyUtils.format(cost.multiply(qty), CurrencyUtils.AMOUNT), NumberUtils.format(StockAnalysisReport.getPNQty(procode, sdate)), NumberUtils.format(StockAnalysisReport.getPOQty(procode, sdate)), "0.00"));
            }
            raw.close();
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception.", ex);
        }
        Connector.close(conn);
        param.put("DATE", new SimpleDateFormat("dd MMMMM yyyy", new Locale("en", "US")).format(new Date(sdate)));
        param.put("LIST", new JRBeanCollectionDataSource(data));
        return param;
    }

    public static BigDecimal getPNQty(String procode, long time) {
        BigDecimal qty = BigDecimal.ZERO;
        Connection conn = Connector.getConnection();
        try {
            Statement stm = conn.createStatement();
            ResultSet raw = stm.executeQuery("SELECT CASE WHEN SUM(QTY-INQTY) IS NULL THEN 0 ELSE SUM(QTY-INQTY) END QTY FROM PORDER_LIST_V WHERE PROCODE LIKE '" + procode + "' AND PODATE <= " + time);
            if (raw.next()) {
                qty = raw.getBigDecimal("QTY");
            }
            raw.close();
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception", ex);
        }
        Connector.close(conn);
        return qty;
    }

    public static BigDecimal getPOQty(String procode, long time) {
        BigDecimal qty = BigDecimal.ZERO;
        Connection conn = Connector.getConnection();
        try {
            Statement stm = conn.createStatement();
            ResultSet raw = stm.executeQuery("SELECT CASE WHEN SUM(QTY-SENDQTY) IS NULL THEN 0 ELSE SUM(QTY-SENDQTY) END QTY FROM SALE_ORDER_LIST_V WHERE PROCODE LIKE '" + procode + "' AND RSODATE <= " + time);
            if (raw.next()) {
                qty = raw.getBigDecimal("QTY");
            }
            raw.close();
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception", ex);
        }
        Connector.close(conn);
        return qty;
    }
}
