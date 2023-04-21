package br.ufmg.saotome.arangi.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.log4j.Logger;
import br.ufmg.saotome.arangi.commons.BasicException;
import br.ufmg.saotome.arangi.dto.Parameter;

/**
 * 
 * @author Cesar Correia
 *
 */
public class JasperReportImpl implements IReport {

    private static Logger log = Logger.getLogger(JasperReportImpl.class);

    public byte[] fillReport(String templateReportFileName, Connection conn, int type) throws BasicException {
        byte[] bytes = null;
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(templateReportFileName, new HashMap(), conn);
            bytes = exportToBytes(jasperPrint, type);
        } catch (BasicException e) {
            throw e;
        } catch (Exception e) {
            throw BasicException.errorHandling("Error preparing report", "msgErrorPreparingReport", e, log);
        }
        return bytes;
    }

    public byte[] fillReport(String templateReportFileName, Connection conn, Parameter[] parameters, int type) throws BasicException {
        byte[] bytes = null;
        Map mapParameters = new HashMap();
        for (int i = 0; i < parameters.length; i++) {
            mapParameters.put(parameters[i].getName(), parameters[i].getValue());
        }
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(templateReportFileName, mapParameters, conn);
            bytes = exportToBytes(jasperPrint, type);
        } catch (BasicException e) {
            throw e;
        } catch (Exception e) {
            throw BasicException.errorHandling("Error preparing report", "msgErrorPreparingReport", e, log);
        }
        return bytes;
    }

    public byte[] fillReport(String templateReportFileName, List<Object[]> data, int type) throws BasicException {
        byte[] bytes = null;
        try {
            Parameter[] fields = getReportFields(templateReportFileName);
            BasicJRDataSource dataSource = new BasicJRDataSource(data, fields);
            JasperPrint jasperPrint = JasperFillManager.fillReport(templateReportFileName, new HashMap(), dataSource);
            bytes = exportToBytes(jasperPrint, type);
        } catch (BasicException e) {
            throw e;
        } catch (Exception e) {
            throw BasicException.errorHandling("Error preparing report", "msgErrorPreparingReport", e, log);
        }
        return bytes;
    }

    public byte[] fillReport(String templateReportFileName, List<Object[]> data, Parameter[] parameters, int type) throws BasicException {
        byte[] bytes = null;
        Map mapParameters = new HashMap();
        for (int i = 0; i < parameters.length; i++) {
            mapParameters.put(parameters[i].getName(), parameters[i].getValue());
        }
        try {
            Parameter[] fields = getReportFields(templateReportFileName);
            BasicJRDataSource dataSource = new BasicJRDataSource(data, fields);
            JasperPrint jasperPrint = JasperFillManager.fillReport(templateReportFileName, mapParameters, dataSource);
            bytes = exportToBytes(jasperPrint, type);
        } catch (BasicException e) {
            throw e;
        } catch (Exception e) {
            throw BasicException.errorHandling("Error preparing report", "msgErrorPreparingReport", e, log);
        }
        return bytes;
    }

    public byte[] fillReport(InputStream templateReportInputStream, Connection conn, int type) throws BasicException {
        byte[] bytes = null;
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(templateReportInputStream, new HashMap(), conn);
            bytes = exportToBytes(jasperPrint, type);
        } catch (BasicException e) {
            throw e;
        } catch (Exception e) {
            throw BasicException.errorHandling("Error preparing report", "msgErrorPreparingReport", e, log);
        }
        return bytes;
    }

    public byte[] fillReport(InputStream templateReportInputStream, Connection conn, Parameter[] parameters, int type) throws BasicException {
        byte[] bytes = null;
        Map mapParameters = new HashMap();
        for (int i = 0; i < parameters.length; i++) {
            mapParameters.put(parameters[i].getName(), parameters[i].getValue());
        }
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(templateReportInputStream, mapParameters, conn);
            bytes = exportToBytes(jasperPrint, type);
        } catch (BasicException e) {
            throw e;
        } catch (Exception e) {
            throw BasicException.errorHandling("Error preparing report", "msgErrorPreparingReport", e, log);
        }
        return bytes;
    }

    public byte[] fillReport(InputStream templateReportInputStream, List<Object[]> data, int type) throws BasicException {
        byte[] bytes = null;
        try {
            Parameter[] fields = getReportFields(templateReportInputStream);
            BasicJRDataSource dataSource = new BasicJRDataSource(data, fields);
            JasperPrint jasperPrint = JasperFillManager.fillReport(templateReportInputStream, new HashMap(), dataSource);
            bytes = exportToBytes(jasperPrint, type);
        } catch (BasicException e) {
            throw e;
        } catch (Exception e) {
            throw BasicException.errorHandling("Error preparing report", "msgErrorPreparingReport", e, log);
        }
        return bytes;
    }

    public byte[] fillReport(InputStream templateReportInputStream, List<Object[]> data, Parameter[] parameters, int type) throws BasicException {
        byte[] bytes = null;
        Map mapParameters = new HashMap();
        for (int i = 0; i < parameters.length; i++) {
            mapParameters.put(parameters[i].getName(), parameters[i].getValue());
        }
        try {
            Parameter[] fields = getReportFields(templateReportInputStream);
            BasicJRDataSource dataSource = new BasicJRDataSource(data, fields);
            JasperPrint jasperPrint = JasperFillManager.fillReport(templateReportInputStream, mapParameters, dataSource);
            bytes = exportToBytes(jasperPrint, type);
        } catch (BasicException e) {
            throw e;
        } catch (Exception e) {
            throw BasicException.errorHandling("Error preparing report", "msgErrorPreparingReport", e, log);
        }
        return bytes;
    }

    private byte[] exportToBytes(JasperPrint jasperPrint, int type) throws BasicException {
        byte[] bytes = null;
        try {
            switch(type) {
                case PDF:
                    bytes = JasperExportManager.exportReportToPdf(jasperPrint);
                    break;
                case XML:
                    bytes = JasperExportManager.exportReportToXml(jasperPrint).getBytes();
                    break;
                case HTML:
                    String tmpFile = System.getProperty("java.io.tmpdir") + File.pathSeparator + "reporttmpfile_" + new Date().getTime();
                    JasperExportManager.exportReportToHtmlFile(jasperPrint, tmpFile);
                    File file = new File(tmpFile);
                    FileInputStream fis = new FileInputStream(file);
                    bytes = new byte[fis.available()];
                    fis.read(bytes);
                    fis.close();
                    file.delete();
            }
        } catch (Exception e) {
            throw BasicException.errorHandling("Error exporting report", "msgErrorExportingReport", e, log);
        }
        return bytes;
    }

    public Parameter[] getReportFields(String templateReportFileName) throws BasicException {
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(templateReportFileName);
            JRField[] jrFields = jasperReport.getFields();
            Parameter[] parameters = new Parameter[jrFields.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = new Parameter();
                parameters[i].setName(jrFields[i].getName());
                parameters[i].setType(jrFields[i].getValueClassName());
            }
            return parameters;
        } catch (Exception e) {
            throw BasicException.errorHandling("Error exporting report", "msgErrorExportingReport", e, log);
        }
    }

    public Parameter[] getReportParameters(String templateReportFileName) throws BasicException {
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(templateReportFileName);
            JRParameter[] jrFields = jasperReport.getParameters();
            Parameter[] parameters = new Parameter[jrFields.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = new Parameter();
                parameters[i].setName(jrFields[i].getName());
                parameters[i].setType(jrFields[i].getValueClassName());
            }
            return parameters;
        } catch (Exception e) {
            throw BasicException.errorHandling("Error exporting report", "msgErrorExportingReport", e, log);
        }
    }

    public Parameter[] getReportFields(InputStream templateReportInputStream) throws BasicException {
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(templateReportInputStream);
            JRField[] jrFields = jasperReport.getFields();
            Parameter[] parameters = new Parameter[jrFields.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = new Parameter();
                parameters[i].setName(jrFields[i].getName());
                parameters[i].setType(jrFields[i].getValueClassName());
            }
            return parameters;
        } catch (Exception e) {
            throw BasicException.errorHandling("Error exporting report", "msgErrorExportingReport", e, log);
        }
    }

    public Parameter[] getReportParameters(InputStream templateReportInputStream) throws BasicException {
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(templateReportInputStream);
            JRParameter[] jrFields = jasperReport.getParameters();
            Parameter[] parameters = new Parameter[jrFields.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = new Parameter();
                parameters[i].setName(jrFields[i].getName());
                parameters[i].setType(jrFields[i].getValueClassName());
            }
            return parameters;
        } catch (Exception e) {
            throw BasicException.errorHandling("Error exporting report", "msgErrorExportingReport", e, log);
        }
    }
}
