package net.nan21.lib;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import java.util.*;
import java.sql.*;
import java.io.*;

public class JasperReportGenerator {

    public static final int REPORT_TYPE_PDF = 0;

    public static final int REPORT_TYPE_HTML = 1;

    public static final int REPORT_TYPE_XLS = 2;

    public static final int REPORT_TYPE_CSV = 3;

    public static final int REPORT_TYPE_XML = 4;

    private JasperReport jasperReport = null;

    /**
	 * the constructor takes a few environment vars to initalise the class
	 * @param sourceFileName
	 * @param compilePath
	 * @param jasperClassPath
	 * @param cachePath
	 */
    public JasperReportGenerator(String sourceFileName, String compilePath, String jasperClassPath, String cachePath) {
        System.setProperty("jasper.reports.compile.class.path", jasperClassPath);
        System.setProperty("jasper.reports.compile.temp", compilePath);
        System.setProperty("jasper.reports.compile.keep.java.file", "false");
        try {
            File source = new File(sourceFileName);
            String fileName = source.getName();
            String cacheName = fileName.substring(0, (fileName.length() - 5)) + "jasper";
            File cached = new File(cachePath + File.separator + cacheName);
            boolean useCache = false;
            if (cached.exists()) {
                if (cached.lastModified() >= source.lastModified()) {
                    useCache = true;
                }
            }
            if (useCache) {
                try {
                    FileInputStream fileIn = new FileInputStream(cached);
                    ObjectInputStream objIn = new ObjectInputStream(fileIn);
                    this.jasperReport = (JasperReport) objIn.readObject();
                    objIn.close();
                } catch (Exception err) {
                    useCache = false;
                }
            }
            if (!useCache) {
                this.jasperReport = JasperCompileManager.compileReport(sourceFileName);
                try {
                    FileOutputStream fileOut = new FileOutputStream(cached);
                    ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
                    objOut.writeObject(this.jasperReport);
                    objOut.close();
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    /**
	 * compile and run a report
	 * @param destFile is the pdf to write to
	 * @param params a map of all the params for the report
	 * @param data is a datasource to perform the query on
	 */
    public void runReport(String destFile, Map<String, Object> params, int exportType, Connection conn) {
        JasperPrint jasperPrint;
        try {
            jasperPrint = JasperFillManager.fillReport(this.jasperReport, params, conn);
            if (exportType == REPORT_TYPE_PDF) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, destFile);
            } else if (exportType == REPORT_TYPE_HTML) {
                JasperExportManager.exportReportToHtmlFile(jasperPrint, destFile);
            } else if (exportType == REPORT_TYPE_XLS) {
                OutputStream outputStream = new FileOutputStream(new File(destFile));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JRXlsExporter exporter = new JRXlsExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
                exporter.exportReport();
                outputStream.write(byteArrayOutputStream.toByteArray());
                outputStream.flush();
                outputStream.close();
            } else if (exportType == REPORT_TYPE_CSV) {
                OutputStream outputStream = new FileOutputStream(new File(destFile));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JRCsvExporter exporter = new JRCsvExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
                exporter.exportReport();
                outputStream.write(byteArrayOutputStream.toByteArray());
                outputStream.flush();
                outputStream.close();
            } else if (exportType == REPORT_TYPE_XML) {
                OutputStream outputStream = new FileOutputStream(new File(destFile));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JRXmlExporter exporter = new JRXmlExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
                exporter.exportReport();
                outputStream.write(byteArrayOutputStream.toByteArray());
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * get the params for the report
	 * @param sourceFile is the report xml file to use
	 */
    public HashMap<String, String> getReportParams(String sourceFile) {
        HashMap<String, String> output = new HashMap<String, String>();
        try {
            JRParameter[] params = this.jasperReport.getParameters();
            for (int x = 0; x < params.length; x++) {
                String param = params[x].getName();
                String type = params[x].getValueClassName();
                output.put(param, type);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return output;
    }

    /**
	 * get the custom params for the report
	 * @param sourceFile
	 */
    public HashMap<String, String> getCustomReportParams(String sourceFile) {
        HashMap<String, String> output = new HashMap<String, String>();
        HashSet<String> systemParams = new HashSet<String>();
        systemParams.add("REPORT_RESOURCE_BUNDLE");
        systemParams.add("REPORT_DATE_FORMAT_FACTORY");
        systemParams.add("REPORT_CLASS_LOADER");
        systemParams.add("REPORT_TIME_ZONE");
        systemParams.add("REPORT_DATA_SOURCE");
        systemParams.add("REPORT_LOCALE");
        systemParams.add("REPORT_URL_HANDLER_FACTORY");
        systemParams.add("REPORT_PARAMETERS_MAP");
        systemParams.add("REPORT_CONNECTION");
        systemParams.add("IS_IGNORE_PAGINATION");
        systemParams.add("REPORT_VIRTUALIZER");
        systemParams.add("REPORT_SCRIPTLET");
        systemParams.add("REPORT_MAX_COUNT");
        try {
            JRParameter[] params = this.jasperReport.getParameters();
            for (int x = 0; x < params.length; x++) {
                if (!systemParams.contains(params[x].getName())) {
                    String param = params[x].getName();
                    String type = params[x].getValueClassName();
                    output.put(param, type);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return output;
    }
}
