package com.etc.report;

import com.etc.controller.EnvironmentController;
import com.etc.controller.ProductController;
import com.etc.controller.beans.ProductBeans;
import com.etc.db.oracle.Connector;
import com.etc.report.beans.MaterialCalcBeans;
import com.etc.util.NumberUtils;
import com.etc.util.ReportSQL;
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
public class MaterialCalcReport {

    public static Logger log4j = Logger.getLogger(MaterialCalcReport.class);

    public static HashMap<Object, Object> getDataSource(HashMap<Object, Object> param) {
        String MAT_STORE = EnvironmentController.getEnvironment("MAT_STORE");
        Collection<Object> data = new ArrayList<Object>();
        Connection conn = Connector.getConnection();
        String id = (String) param.get("ID");
        BigDecimal qty = new BigDecimal((String) param.get("QTY"));
        try {
            ProductBeans product = ProductController.getProductById(Integer.parseInt(id));
            param.put("PROCODE", product.getCode());
            param.put("PRONAME", product.getName());
            param.put("QTY", NumberUtils.format(qty));
            param.put("UNIT", product.getUcode());
            Statement stm = conn.createStatement();
            ResultSet raw = stm.executeQuery("SELECT * FROM MATERIAL_INFO WHERE PID = " + product.getId());
            while (raw.next()) {
                ProductBeans component = ProductController.getProductById(raw.getInt("RID"));
                BigDecimal rem = ReportSQL.getProQtyByStore(conn, raw.getBigDecimal("RID"), new BigDecimal(MAT_STORE));
                BigDecimal cal = raw.getBigDecimal("QTY").multiply(qty);
                BigDecimal order = cal.subtract(rem);
                if (order.compareTo(BigDecimal.ZERO) < 0) {
                    order = BigDecimal.ZERO;
                }
                data.add(new MaterialCalcBeans(component.getCode(), component.getName(), NumberUtils.format(cal), component.getUcode(), NumberUtils.format(rem), NumberUtils.format(order)));
            }
            raw.close();
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception.", ex);
        }
        Connector.close(conn);
        param.put("LIST", new JRBeanCollectionDataSource(data));
        return param;
    }
}
