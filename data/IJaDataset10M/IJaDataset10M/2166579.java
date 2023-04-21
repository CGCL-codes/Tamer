package org.plazmaforge.framework.report;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.plazmaforge.framework.core.IAcceptor;
import org.plazmaforge.framework.core.exception.ApplicationException;
import org.plazmaforge.framework.core.exception.DAOException;
import org.plazmaforge.framework.dao.connection.ConnectionFactory;
import org.plazmaforge.framework.platform.service.ServiceFactory;
import org.plazmaforge.framework.util.FileUtils;
import org.plazmaforge.framework.util.StringUtils;

/**
 * @author Oleh Hapon Date: 10.07.2004 Time: 12:54:34 $Id: AbstractReportEngine.java,v 1.15 2010/12/05 07:51:25 ohapon Exp $
 */
public abstract class AbstractReportEngine implements ReportEngine {

    protected abstract String getReportCompileFileExt();

    protected abstract String getReportDesignFileExt();

    protected abstract String getReportPrintFileExt();

    protected abstract String getReportFileExt(int fileType);

    public void executeReport(IReport report) throws ApplicationException {
        viewReport(fillReport(report));
    }

    protected Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException ex) {
            throw new DAOException(ex);
        }
    }

    protected void closeConnection(Connection cn) throws DAOException {
        try {
            if (cn != null && cn.isClosed()) {
                cn.close();
            }
        } catch (SQLException ex) {
            throw new DAOException(ex);
        }
    }

    /**
     * Return normalize compile file name
     * @param file name
     * @return
     */
    protected String getNormalizeCompileFileName(String fileName) {
        return getNormalizeFileName(fileName, getReportCompileFileExt());
    }

    /**
     * Return normalize design file name
     * @param fileName
     * @return
     */
    protected String getNormalizeDesignFileName(String fileName) {
        return getNormalizeFileName(fileName, getReportDesignFileExt());
    }

    /**
     * Return normalize print file name
     * @param fileName
     * @return
     */
    protected String getNormalizePrintFileName(String fileName) {
        return getNormalizeFileName(fileName, getReportPrintFileExt());
    }

    protected String getNormalizeFileName(String fileName, String ext) {
        return FileUtils.completeFileExt(fileName, ext, true);
    }

    protected String getNormalizeFileName(String fileName, int fileType) {
        return getNormalizeFileName(fileName, getReportFileExt(fileType));
    }

    protected void updateReport(IReport report, String fileName, int fileType) {
        report.setReportFileName(fileName);
        report.setReportPath("");
        String path = report.getReportFileName();
        int index = -1;
        if (report.isAsolutePath() || StringUtils.isEmpty(report.getPath())) {
            int index1 = path.lastIndexOf(IReport.PATH_SEPARATOR);
            int index2 = path.lastIndexOf(File.separator);
            index = Math.max(index1, index2);
            if (index > -1) {
                index++;
            }
        } else {
            String fn = getNormalizeFileName(report.getFileName(), fileType);
            index = path.indexOf(fn);
        }
        if (index < 1) {
            return;
        }
        report.setReportPath(path.substring(0, index));
    }

    public File getFile(IReport report) throws ApplicationException {
        return getFile(report, COMPILE_FILE_TYPE);
    }

    public File getFile(IReport report, int fileType) throws ApplicationException {
        String fileName = report.getFileName();
        List<String> fileList = new ArrayList<String>();
        updateReport(report, getNormalizeFileName(report.getAbsoluteFileName(), fileType), fileType);
        fileList.add(report.getReportFileName());
        File file = getFile(report, report.getReportFileName());
        if (file != null) {
            return file;
        }
        String[] names = report.getAvailableLocaleFileNames();
        if (names.length != 0) {
            for (int i = 0; i < names.length; i++) {
                updateReport(report, getNormalizeFileName(names[i], fileType), fileType);
                fileList.add(report.getReportFileName());
                file = getFile(report, report.getReportFileName());
                if (file != null) {
                    return file;
                }
            }
        }
        if (file == null) {
            throw new ApplicationException(getReportErrorMessage(fileName, fileList));
        }
        return file;
    }

    public InputStream getInputStream(IReport report) throws ApplicationException {
        return getInputStream(report, COMPILE_FILE_TYPE);
    }

    public InputStream getInputStream(IReport report, int fileType) throws ApplicationException {
        String fileName = report.getFileName();
        List<String> fileList = new ArrayList<String>();
        updateReport(report, getNormalizeFileName(report.getAbsoluteFileName(), fileType), fileType);
        fileList.add(report.getReportFileName());
        InputStream is = getInputStream(report, report.getReportFileName());
        if (is != null) {
            return is;
        }
        String[] names = report.getAvailableLocaleFileNames();
        if (names.length != 0) {
            for (int i = 0; i < names.length; i++) {
                updateReport(report, getNormalizeFileName(names[i], fileType), fileType);
                fileList.add(report.getReportFileName());
                is = getInputStream(report, report.getReportFileName());
                if (is != null) {
                    return is;
                }
            }
        }
        if (is == null) {
            throw new ApplicationException(getReportErrorMessage(fileName, fileList));
        }
        return is;
    }

    protected String getReportErrorMessage(String fileName, List<String> fileList) {
        StringBuffer buf = new StringBuffer();
        buf.append("Report [" + fileName + "] not found");
        if (fileList.size() == 1) {
            buf.append("\nAbsolute file: " + fileList.get(0));
        }
        buf.append("\nAbsolute file list:");
        for (String f : fileList) {
            buf.append("\n" + f);
        }
        return buf.toString();
    }

    protected File getFile(IReport report, String reportFileName) {
        return new ReportStorage().getFile(report, reportFileName);
    }

    protected InputStream getInputStream(IReport report, String reportFileName) {
        return new ReportStorage().getInputStream(report, reportFileName);
    }

    protected ReportService getReportService() throws ApplicationException {
        return (ReportService) ServiceFactory.getService(ReportService.class);
    }

    public Object fillReport(IReport report) throws ApplicationException {
        String reportId = report.getId();
        Map parameters = report.getParameters();
        return getReportService().fillReportById(reportId, parameters);
    }

    public void viewReport(Class klass, String name, Map parameters) throws ApplicationException {
        viewReport(fillReport(klass, name, parameters));
    }

    public Object fillReport(Class klass, String name, Map parameters) throws ApplicationException {
        return getReportService().fillReport(klass, name, parameters);
    }

    public Object createDataSourceByMapArray(Object[] mapArray) throws ApplicationException {
        return null;
    }

    protected void prepareReportData(IReport report, Map parameters, Connection cn) throws ApplicationException {
        if (report == null) {
            return;
        }
        IAcceptor acceptor = report.getAcceptor();
        if (acceptor == null) {
            return;
        }
        try {
            parameters.put(IAcceptor.REPORT_CONNECTION, cn);
            parameters.put(IAcceptor.REPORT_ENGINE, this);
            acceptor.prepareData(parameters);
        } finally {
            parameters.put(IAcceptor.REPORT_CONNECTION, null);
            parameters.put(IAcceptor.REPORT_ENGINE, null);
        }
    }

    public List<Object[]> getSQLResult(Connection cn, String sql, Map parameters) throws ApplicationException {
        return getReportService().getSQLResult(cn, sql, parameters);
    }

    public Object getSQLValue(Connection cn, String sql, Map parameters) throws ApplicationException {
        return getReportService().getSQLValue(cn, sql, parameters);
    }
}
