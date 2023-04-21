package com.etc.report;

import com.etc.bin.base.Constant;
import com.etc.controller.PartnerController;
import com.etc.controller.ProductCategoryController;
import com.etc.controller.beans.PartnerBeans;
import com.etc.controller.beans.ProductCategoryBeans;
import com.etc.db.oracle.Connector;
import com.etc.report.beans.DailyDeliveryBeans;
import com.etc.util.CurrencyUtils;
import com.etc.util.Misc;
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
public class DailyDeliveryReport {

    public static Logger log4j = Logger.getLogger(DailyDeliveryReport.class);

    public static HashMap<Object, Object> getDataSource(HashMap<Object, Object> param) {
        Collection<Object> data = new ArrayList<Object>();
        Connection conn = Connector.getConnection();
        Long sdate = (Long) param.get("SDATE");
        Long edate = (Long) param.get("EDATE");
        String paCode = (String) param.get("PACODE");
        Integer patype = (Integer) param.get("PATYPE");
        try {
            String ext = "";
            if (patype > 0) {
                ext = "AND PARTNER.TYPE = " + patype;
            }
            Statement stm = conn.createStatement();
            ResultSet raw = stm.executeQuery("SELECT DEBITNOTE_LIST_V.ID, DEBITNOTE_LIST_V.RDNDATE, DEBITNOTE_LIST_V.DNCODE, SALE_LIST_V.PROCODE, SALE_LIST_V.PRONAME, DEBITNOTE_LIST_V.QTY, SALE_LIST_V.PRICE, SALE_LIST_V.FPRICE, SALE_LIST_V.COST, SALE_LIST_V.CATID, DEBITNOTE_LIST_V.PAID FROM DEBITNOTE_LIST_V, SALE_LIST_V, PARTNER WHERE DEBITNOTE_LIST_V.SILID = SALE_LIST_V.ID AND DEBITNOTE_LIST_V.PAID = PARTNER.ID AND DEBITNOTE_LIST_V.RDNDATE > " + sdate + " AND DEBITNOTE_LIST_V.RDNDATE < " + edate + " AND PARTNER.CODE LIKE '%" + paCode + "%' " + ext + " ORDER BY DEBITNOTE_LIST_V.RDNDATE ASC");
            while (raw.next()) {
                PartnerBeans partner = PartnerController.getPartner(raw.getInt("PAID"));
                ProductCategoryBeans category = ProductCategoryController.getProductCategory(raw.getInt("CATID"));
                BigDecimal qty = raw.getBigDecimal("QTY");
                BigDecimal cost = raw.getBigDecimal("COST").setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal price = raw.getBigDecimal("PRICE").setScale(2, BigDecimal.ROUND_HALF_UP).multiply(raw.getBigDecimal("FPRICE")).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal pamount = price.multiply(qty);
                BigDecimal camount = cost.multiply(qty);
                BigDecimal margin = pamount.subtract(camount);
                BigDecimal pmargin = BigDecimal.ZERO;
                if (pamount.compareTo(BigDecimal.ZERO) == 0) {
                    pmargin = margin.divide(margin, 7, BigDecimal.ROUND_HALF_UP).multiply(Constant.HUNDRED);
                } else {
                    pmargin = margin.divide(pamount, 7, BigDecimal.ROUND_HALF_UP).multiply(Constant.HUNDRED);
                }
                String pocode = getPOCode(raw.getInt("ID"));
                data.add(new DailyDeliveryBeans(partner.getCode(), partner.getName(), category.getName(), raw.getString("PROCODE"), raw.getString("PRONAME"), raw.getString("DNCODE"), pocode, Misc.dateFormat(raw.getLong("RDNDATE"), "dd/MM/yy"), NumberUtils.format(qty), CurrencyUtils.format(price, CurrencyUtils.REPORT), CurrencyUtils.format(pamount, CurrencyUtils.AMOUNT), CurrencyUtils.format(cost, CurrencyUtils.REPORT), CurrencyUtils.format(camount, CurrencyUtils.AMOUNT), CurrencyUtils.format(margin, CurrencyUtils.AMOUNT), CurrencyUtils.format(pmargin, CurrencyUtils.AMOUNT)));
            }
            raw.close();
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception.", ex);
        }
        Connector.close(conn);
        param.put("SDATE", Misc.dateFormat(sdate, "dd MMMMM yyyy"));
        param.put("EDATE", Misc.dateFormat(edate, "dd MMMMM yyyy"));
        param.put("LIST", new JRBeanCollectionDataSource(data));
        return param;
    }

    public static String getPOCode(int id) {
        String code = "";
        Connection conn = Connector.getConnection();
        try {
            Statement stm = conn.createStatement();
            ResultSet raw = stm.executeQuery("SELECT SALE_ORDER_LIST_V.SOCODE CODE FROM SALE_ORDER_LIST_V, SALE_LIST, DEBITNOTE_LIST WHERE SALE_ORDER_LIST_V.ID = SALE_LIST.SOLID AND SALE_LIST.ID = DEBITNOTE_LIST.SILID AND DEBITNOTE_LIST.ID = " + id);
            if (raw.next()) {
                code = raw.getString("CODE");
            }
            raw.close();
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception.", ex);
        }
        Connector.close(conn);
        return code;
    }
}
