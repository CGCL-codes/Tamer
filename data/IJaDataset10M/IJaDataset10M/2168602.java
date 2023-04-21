package org.digitall.common.cashflow.reports.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;
import org.digitall.lib.xml.basic.XMLBasicBook;
import org.digitall.lib.xml.basic.XMLBasicSheet;

public class PaymentsOrdersListReport extends XMLBasicBook {

    private String params = "";

    public PaymentsOrdersListReport() {
        this(null);
    }

    public PaymentsOrdersListReport(String _xmlFileName) {
        bookName = "Ordenes de Pago";
        doReportBody();
        xmlFile = xmlFileWriter(_xmlFileName);
        doReport();
    }

    private String mainColumnSizes() {
        StringBuffer tituloMasInicioTabla = new StringBuffer();
        tituloMasInicioTabla.append("      <Column ss:AutoFitWidth=\"0\" ss:Width=\"60\"/>");
        tituloMasInicioTabla.append("      <Column ss:Width=\"60\" />");
        tituloMasInicioTabla.append("      <Column ss:Width=\"200\"/>");
        tituloMasInicioTabla.append("      <Column ss:Width=\"100\"/>");
        tituloMasInicioTabla.append("      <Column ss:Width=\"60\"/>");
        return tituloMasInicioTabla.toString();
    }

    private String mainHeader() {
        StringBuffer filaTabla = new StringBuffer();
        filaTabla.append("         <Row>");
        filaTabla.append("            <Cell ss:MergeAcross=\"4\" ss:StyleID=\"fecha\"><Data ss:Type=\"String\">Fecha de Impresi�n: " + Proced.setFormatDate(Environment.currentDate, true) + "</Data></Cell>");
        filaTabla.append("         </Row>");
        filaTabla.append("         <Row>");
        filaTabla.append("            <Cell ss:MergeAcross=\"4\" ss:StyleID=\"fecha\"><Data ss:Type=\"String\">Modulo: Orden de Pago</Data></Cell>");
        filaTabla.append("         </Row>");
        filaTabla.append("         <Row>");
        filaTabla.append("            <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s23\"><Data ss:Type=\"String\">Ordenes de Pago</Data></Cell>");
        filaTabla.append("         </Row>");
        return filaTabla.toString();
    }

    private String mainContentHeader() {
        StringBuffer filaTabla = new StringBuffer();
        filaTabla.append("         <Row ss:Index=\"5\">");
        filaTabla.append("            <Cell ss:StyleID=\"encabezado\"><Data ss:Type=\"String\">Fecha</Data></Cell>");
        filaTabla.append("            <Cell ss:StyleID=\"encabezado\"><Data ss:Type=\"String\">Nro. Orden de Pago</Data></Cell>");
        filaTabla.append("            <Cell ss:StyleID=\"encabezado\"><Data ss:Type=\"String\">Destinatario</Data></Cell>");
        filaTabla.append("            <Cell ss:StyleID=\"encabezado\"><Data ss:Type=\"String\">Tipo</Data></Cell>");
        filaTabla.append("            <Cell ss:StyleID=\"encabezado\"><Data ss:Type=\"String\">($) Monto</Data></Cell>");
        filaTabla.append("         </Row>");
        return filaTabla.toString();
    }

    private String mainContent() {
        ResultSet PaymentsOrders = LibSQL.exFunction("cashflow.getallPaymentsOrders", "0,0");
        StringBuffer filaTabla = new StringBuffer();
        try {
            while (PaymentsOrders.next()) {
                filaTabla.append("        <Row>");
                filaTabla.append("    	     <Cell ss:StyleID=\"s35\"><Data ss:Type=\"String\">" + Proced.setFormatDate(PaymentsOrders.getString("date"), true) + "</Data></Cell>");
                filaTabla.append("           <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + PaymentsOrders.getString("number") + "</Data></Cell>");
                filaTabla.append("           <Cell ss:StyleID=\"s38\"><Data ss:Type=\"String\">" + PaymentsOrders.getString("entity") + "</Data></Cell>");
                filaTabla.append("           <Cell ss:StyleID=\"s35\"><Data ss:Type=\"String\">" + PaymentsOrders.getString("entitytype") + "</Data></Cell>");
                filaTabla.append("           <Cell ss:StyleID=\"sBorderArgMoney\"><Data ss:Type=\"Number\">" + PaymentsOrders.getDouble("amount") + "</Data></Cell>");
                filaTabla.append("        </Row>");
            }
        } catch (SQLException x) {
            Advisor.messageBox(x.getMessage(), "Error");
            x.printStackTrace();
        }
        return filaTabla.toString();
    }

    private void doReportBody() {
        XMLBasicSheet _mainSheet = new XMLBasicSheet() {

            public String tableColumnSizes() {
                return mainColumnSizes();
            }

            public String sheetHeader() {
                return mainHeader();
            }

            public String tableHeader() {
                return mainContentHeader();
            }

            public String tableBody() {
                return mainContent();
            }
        };
        _mainSheet.setSheetName("Ordenes de Pago");
        sheets.addElement(_mainSheet);
    }
}
