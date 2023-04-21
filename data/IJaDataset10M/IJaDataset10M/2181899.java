package br.ufmg.lcc.arangi.commons.report;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

/**
 * 
 * 
 * @author Marcelo Gomes
 *
 */
public class ExportReport {

    public static byte[] exportReportToPDF(JasperPrint report) {
        try {
            return JasperExportManager.exportReportToPdf(report);
        } catch (JRException e) {
            System.out.println("Erro na Gera��o do Relat�rio!");
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] exportReportToExcel(JasperPrint relatorio, Map parametros) {
        try {
            JRXlsExporter exporter = new JRXlsExporter();
            ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, relatorio);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.FALSE);
            if (parametros != null) {
                Iterator itParametros = parametros.keySet().iterator();
                while (itParametros.hasNext()) {
                    JRXlsExporterParameter chave = (JRXlsExporterParameter) itParametros.next();
                    exporter.setParameter(chave, parametros.get(chave));
                }
            }
            exporter.exportReport();
            return xlsReport.toByteArray();
        } catch (JRException e) {
            System.out.println("Erro na Gera��o do Arquivo em Excel!");
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] exportReportToCSV(JasperPrint relatorio, Map parametros) {
        try {
            JRCsvExporter exporter = new JRCsvExporter();
            ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, relatorio);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsReport);
            if (parametros != null) {
                Iterator itParametros = parametros.keySet().iterator();
                while (itParametros.hasNext()) {
                    JRCsvExporterParameter chave = (JRCsvExporterParameter) itParametros.next();
                    exporter.setParameter(chave, parametros.get(chave));
                }
            }
            exporter.exportReport();
            return xlsReport.toByteArray();
        } catch (JRException e) {
            System.out.println("Erro na Gera��o do Arquivo em CSV!");
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] exportReportToTXT(JasperPrint relatorio, Map parametros) {
        try {
            JRTextExporter exporter = new JRTextExporter();
            ByteArrayOutputStream txtReport = new ByteArrayOutputStream();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, relatorio);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, txtReport);
            if (parametros != null) {
                Iterator itParametros = parametros.keySet().iterator();
                while (itParametros.hasNext()) {
                    JRTextExporterParameter chave = (JRTextExporterParameter) itParametros.next();
                    exporter.setParameter(chave, parametros.get(chave));
                }
            }
            exporter.exportReport();
            return txtReport.toByteArray();
        } catch (JRException e) {
            System.out.println("Erro na Gera��o do Arquivo em TXT!");
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] exportReportToHTML(JasperPrint relatorio, Map parametros) {
        try {
            JRHtmlExporter exporter = new JRHtmlExporter();
            ByteArrayOutputStream htmlReport = new ByteArrayOutputStream();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, relatorio);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, htmlReport);
            if (parametros != null) {
                Iterator itParametros = parametros.keySet().iterator();
                while (itParametros.hasNext()) {
                    JRHtmlExporterParameter chave = (JRHtmlExporterParameter) itParametros.next();
                    exporter.setParameter(chave, parametros.get(chave));
                }
            }
            exporter.exportReport();
            return htmlReport.toByteArray();
        } catch (JRException e) {
            System.out.println("Erro na Gera��o do Arquivo em TXT!");
            e.printStackTrace();
        }
        return null;
    }
}
