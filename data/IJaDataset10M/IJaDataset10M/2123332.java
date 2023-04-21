package com.soode.openospc.report;

import java.util.HashMap;
import java.util.Map;
import com.mysql.jdbc.Connection;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;

/**
 * @author Terakhir bin Wih
 * @email khirzrn@gmail.com
 * @version 1.0
 */
public class Report {

    public void createReport() throws JRException {
        JasperDesign jasperDesign = JasperManager.loadXmlDesign("MyReport.xml");
        JasperReport jasperReport = JasperManager.compileReport(jasperDesign);
        Map parameters = new HashMap();
        parameters.put("title", "A user-customized title");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
    }
}
