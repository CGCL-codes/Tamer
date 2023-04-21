package com.etc.report;

import com.etc.controller.ExchangeController;
import com.etc.controller.PartnerController;
import com.etc.controller.ProductCategoryController;
import com.etc.controller.beans.ExchangeBeans;
import com.etc.controller.beans.PartnerBeans;
import com.etc.controller.beans.ProductCategoryBeans;
import com.etc.db.oracle.Connector;
import com.etc.report.beans.YearlyPurchaseNoteBeans;
import com.etc.util.NumberUtils;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.log4j.Logger;

/**
 *
 * @author magicbank
 */
public class YearlyPurchaseNoteReport {

    public static Logger log4j = Logger.getLogger(YearlyPurchaseNoteReport.class);

    public static HashMap<Object, Object> getDataSourceQuantity(HashMap<Object, Object> param) {
        Collection<Object> data = new ArrayList<Object>();
        Connection conn = Connector.getConnection();
        Long sd1 = (Long) param.get("SDATE1");
        Long sd2 = (Long) param.get("SDATE2");
        Long sd3 = (Long) param.get("SDATE3");
        Long sd4 = (Long) param.get("SDATE4");
        Long ed1 = (Long) param.get("EDATE1");
        Long ed2 = (Long) param.get("EDATE2");
        Long ed3 = (Long) param.get("EDATE3");
        Long ed4 = (Long) param.get("EDATE4");
        String pacode = (String) param.get("PACODE");
        try {
            Statement stm = conn.createStatement();
            ResultSet raw = stm.executeQuery("SELECT PORDER_LIST_V.PAID, PORDER_LIST_V.CATID, PORDER_LIST_V.PROID, PORDER_LIST_V.PROCODE, PORDER_LIST_V.PRONAME, PORDER_LIST_V.UNIT, PORDER_LIST_V.CURRENCY FROM PORDER_LIST_V LEFT JOIN PARTNER ON PORDER_LIST_V.PAID = PARTNER.ID WHERE PARTNER.CODE LIKE '%" + pacode + "%' AND PORDER_LIST_V.RPODATE > " + sd1 + " AND PORDER_LIST_V.RPODATE < " + ed4 + " GROUP BY PORDER_LIST_V.PAID, PARTNER.CODE, PORDER_LIST_V.CATID, PORDER_LIST_V.PROID, PORDER_LIST_V.PROCODE, PORDER_LIST_V.PRONAME, PORDER_LIST_V.UNIT, PORDER_LIST_V.CURRENCY ORDER BY PARTNER.CODE ASC, PORDER_LIST_V.PROCODE ASC");
            while (raw.next()) {
                PartnerBeans partner = PartnerController.getPartner(raw.getInt("PAID"));
                ProductCategoryBeans category = ProductCategoryController.getProductCategory(raw.getInt("CATID"));
                String unit = raw.getString("UNIT");
                ExchangeBeans exchange = ExchangeController.getExchange(raw.getInt("CURRENCY"));
                BigDecimal first = getQuantityQuarter(partner.getId(), raw.getInt("PROID"), exchange.getId(), sd1, ed1);
                BigDecimal second = getQuantityQuarter(partner.getId(), raw.getInt("PROID"), exchange.getId(), sd2, ed2);
                BigDecimal thirt = getQuantityQuarter(partner.getId(), raw.getInt("PROID"), exchange.getId(), sd3, ed3);
                BigDecimal fourth = getQuantityQuarter(partner.getId(), raw.getInt("PROID"), exchange.getId(), sd4, ed4);
                BigDecimal sum = first.add(second).add(thirt).add(fourth);
                data.add(new YearlyPurchaseNoteBeans(partner.getCode(), partner.getName(), Integer.toString(category.getId()), category.getName(), raw.getString("PROCODE"), raw.getString("PRONAME"), unit, exchange.getUcode(), NumberUtils.format(first), NumberUtils.format(second), NumberUtils.format(thirt), NumberUtils.format(fourth), NumberUtils.format(sum)));
            }
            raw.close();
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception.", ex);
        }
        Connector.close(conn);
        param.put("SDATE", sd1);
        param.put("EDATE", ed4);
        param.put("LIST", new JRBeanCollectionDataSource(data));
        return param;
    }

    public static HashMap<Object, Object> getDataSourceAmount(HashMap<Object, Object> param) {
        Collection<Object> data = new ArrayList<Object>();
        Connection conn = Connector.getConnection();
        Long sd1 = (Long) param.get("SDATE1");
        Long sd2 = (Long) param.get("SDATE2");
        Long sd3 = (Long) param.get("SDATE3");
        Long sd4 = (Long) param.get("SDATE4");
        Long ed1 = (Long) param.get("EDATE1");
        Long ed2 = (Long) param.get("EDATE2");
        Long ed3 = (Long) param.get("EDATE3");
        Long ed4 = (Long) param.get("EDATE4");
        String pacode = (String) param.get("PACODE");
        try {
            Statement stm = conn.createStatement();
            ResultSet raw = stm.executeQuery("SELECT PORDER_LIST_V.PAID, PORDER_LIST_V.CATID, PORDER_LIST_V.PROID, PORDER_LIST_V.PROCODE, PORDER_LIST_V.PRONAME, PORDER_LIST_V.UNIT, PORDER_LIST_V.CURRENCY FROM PORDER_LIST_V LEFT JOIN PARTNER ON PORDER_LIST_V.PAID = PARTNER.ID WHERE PARTNER.CODE LIKE '%" + pacode + "%' AND PORDER_LIST_V.RPODATE > " + sd1 + " AND PORDER_LIST_V.RPODATE < " + ed4 + " GROUP BY PORDER_LIST_V.PAID, PARTNER.CODE, PORDER_LIST_V.CATID, PORDER_LIST_V.PROID, PORDER_LIST_V.PROCODE, PORDER_LIST_V.PRONAME, PORDER_LIST_V.UNIT, PORDER_LIST_V.CURRENCY ORDER BY PARTNER.CODE ASC, PORDER_LIST_V.PROCODE ASC");
            while (raw.next()) {
                PartnerBeans partner = PartnerController.getPartner(raw.getInt("PAID"));
                ProductCategoryBeans category = ProductCategoryController.getProductCategory(raw.getInt("CATID"));
                String unit = raw.getString("UNIT");
                ExchangeBeans exchange = ExchangeController.getExchange(raw.getInt("CURRENCY"));
                BigDecimal first = getAmountQuarter(partner.getId(), raw.getInt("PROID"), exchange.getId(), sd1, ed1);
                BigDecimal second = getAmountQuarter(partner.getId(), raw.getInt("PROID"), exchange.getId(), sd2, ed2);
                BigDecimal thirt = getAmountQuarter(partner.getId(), raw.getInt("PROID"), exchange.getId(), sd3, ed3);
                BigDecimal fourth = getAmountQuarter(partner.getId(), raw.getInt("PROID"), exchange.getId(), sd4, ed4);
                BigDecimal sum = first.add(second).add(thirt).add(fourth);
                data.add(new YearlyPurchaseNoteBeans(partner.getCode(), partner.getName(), Integer.toString(category.getId()), category.getName(), raw.getString("PROCODE"), raw.getString("PRONAME"), unit, exchange.getUcode(), NumberUtils.format(first), NumberUtils.format(second), NumberUtils.format(thirt), NumberUtils.format(fourth), NumberUtils.format(sum)));
            }
            raw.close();
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception.", ex);
        }
        Connector.close(conn);
        param.put("SDATE", sd1);
        param.put("EDATE", ed4);
        param.put("LIST", new JRBeanCollectionDataSource(data));
        return param;
    }

    public static BigDecimal getQuantityQuarter(int paid, int proid, int currency, long sd, long ed) {
        BigDecimal qty = BigDecimal.ZERO;
        Connection conn = Connector.getConnection();
        try {
            Statement stm = conn.createStatement();
            ResultSet raw = stm.executeQuery("SELECT SUM(QTY) QTY FROM PORDER_LIST_V WHERE PAID = " + paid + " AND PROID = " + proid + " AND CURRENCY = " + currency + " AND RPODATE > " + sd + " AND RPODATE < " + ed);
            if (raw.next()) {
                qty = raw.getBigDecimal("QTY") == null ? BigDecimal.ZERO : raw.getBigDecimal("QTY");
            }
            raw.close();
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception.", ex);
        }
        Connector.close(conn);
        return qty;
    }

    public static BigDecimal getAmountQuarter(int paid, int proid, int currency, long sd, long ed) {
        BigDecimal qty = BigDecimal.ZERO;
        Connection conn = Connector.getConnection();
        try {
            Statement stm = conn.createStatement();
            ResultSet raw = stm.executeQuery("SELECT SUM(QTY * PRICE) QTY FROM PORDER_LIST_V WHERE PAID = " + paid + " AND PROID = " + proid + " AND CURRENCY = " + currency + " AND RPODATE > " + sd + " AND RPODATE < " + ed);
            if (raw.next()) {
                qty = raw.getBigDecimal("QTY") == null ? BigDecimal.ZERO : raw.getBigDecimal("QTY");
            }
            raw.close();
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception.", ex);
        }
        Connector.close(conn);
        return qty;
    }
}
