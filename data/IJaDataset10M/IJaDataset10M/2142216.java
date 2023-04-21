package com.tegsoft.tobe.ui.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkmax.zul.Filedownload;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Timer;
import com.tegsoft.tobe.db.Counter;
import com.tegsoft.tobe.db.command.Command;
import com.tegsoft.tobe.db.command.DbParameter;
import com.tegsoft.tobe.db.connection.Connection;
import com.tegsoft.tobe.util.Compare;
import com.tegsoft.tobe.util.Converter;
import com.tegsoft.tobe.util.FileUtil;
import com.tegsoft.tobe.util.NullStatus;
import com.tegsoft.tobe.util.UiUtil;

public class Jasperreport extends Iframe implements org.zkoss.zul.api.Iframe {

    private static final long serialVersionUID = 1L;

    private static final String TASK_ODT = "odt";

    private static final String TASK_CSV = "csv";

    private static final String TASK_XLS = "xls";

    private static final String TASK_RTF = "rtf";

    private static final String TASK_XML = "xml";

    private static final String TASK_PDF = "pdf";

    private static final String TASK_HTML = "html";

    private String commandName;

    private String report;

    private String type = TASK_HTML;

    private final ArrayList<DbParameter> dbParameters = new ArrayList<DbParameter>();

    private String resourceBundle;

    private int refreshTime = 0;

    public Jasperreport() {
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
        InputStream is = null;
        Command command = null;
        try {
            AMedia amedia = null;
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            for (Iterator<Object> it = UiUtil.getActiveForm().getParameters().keySet().iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                parameters.put(key, UiUtil.getActiveForm().getParameters().getProperty(key));
            }
            parameters.put("UNITUID", UiUtil.getUNITUID());
            parameters.put("UID", UiUtil.getUID());
            parameters.put("UUID", Counter.getUUID().toString());
            parameters.put("UNITUIDNAME", UiUtil.getSessionAttribute("UNITUIDNAME"));
            parameters.put("USERNAME", UiUtil.getSessionAttribute("USERNAME"));
            parameters.put("REPORT_FILE_RESOLVER", new FileUtil());
            parameters.put("REPORT_LOCALE", UiUtil.getLocale());
            ResourceBundle resourceBundle = new ResourceBundle() {

                @Override
                public Enumeration<String> getKeys() {
                    return null;
                }

                @Override
                protected Object handleGetObject(String key) {
                    try {
                        return UiUtil.getMessageKeys().getProperty(key);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return key;
                }
            };
            parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
            for (int i = 0; i < dbParameters.size(); i++) {
                parameters.put(dbParameters.get(i).getParameterName(), dbParameters.get(i).getValue());
            }
            if (Compare.equal(getType(), TASK_PDF)) {
                is = new FileInputStream(FileUtil.find(getReport() + ".jasper"));
                byte[] reportResult = null;
                if (NullStatus.isNull(getCommandName())) {
                    reportResult = JasperRunManager.runReportToPdf(is, parameters, Connection.getActive());
                } else {
                    command = UiUtil.getDbCommand(getCommandName());
                    command.executeReader();
                    reportResult = JasperRunManager.runReportToPdf(is, parameters, new JRResultSetDataSource(command.getResultSet()));
                    command.close();
                }
                amedia = new AMedia(getReport() + "." + getType(), getType(), null, new ByteArrayInputStream(reportResult));
                setContent(amedia);
            } else if (Compare.equal(getType(), TASK_HTML)) {
                File file = FileUtil.find(getReport() + ".jasper");
                String dirName = UiUtil.getParameter("RealPath.Context") + File.separator + "tempreports" + File.separator + UiUtil.getSessionId();
                String fileName = Counter.getUUID() + ".html";
                String destFileName = dirName + File.separator + fileName;
                new File(dirName).mkdirs();
                if (NullStatus.isNull(getCommandName())) {
                    JasperRunManager.runReportToHtmlFile(file.getAbsolutePath(), destFileName, parameters, Connection.getActive());
                } else {
                    command = UiUtil.getDbCommand(getCommandName());
                    command.executeReader();
                    JasperRunManager.runReportToHtmlFile(file.getAbsolutePath(), destFileName, parameters, new JRResultSetDataSource(command.getResultSet()));
                    command.close();
                }
                setSrc("/tempreports/" + UiUtil.getSessionId() + "/" + fileName);
            } else if (Compare.equal(getType(), TASK_XLS)) {
                command = UiUtil.getDbCommand(getCommandName());
                command.executeReader();
                ResultSet rs = command.getResultSet();
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("Report");
                HSSFRow headerRow = sheet.createRow(0);
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    String message = UiUtil.getActiveForm().getMessageKeys().getProperty(columnName);
                    if (NullStatus.isNotNull(message)) {
                        headerRow.createCell(i - 1).setCellValue(message);
                    } else {
                        headerRow.createCell(i - 1).setCellValue(columnName);
                    }
                }
                int i = 0;
                while (rs.next()) {
                    HSSFRow excelRow = sheet.createRow(i + 1);
                    for (int j = 1; j <= rs.getMetaData().getColumnCount(); j++) {
                        String columnName = rs.getMetaData().getColumnName(j);
                        if (NullStatus.isNull(rs.getObject(columnName))) {
                            continue;
                        }
                        excelRow.createCell(j - 1).setCellValue(Converter.asNotNullString(rs.getObject(columnName)));
                    }
                    i++;
                }
                command.close();
                String dirName = UiUtil.getParameter("RealPath.Context") + File.separator + "tempreports" + File.separator + UiUtil.getSessionId();
                String fileName = Counter.getUUID() + ".xls";
                String destFileName = dirName + File.separator + fileName;
                new File(dirName).mkdirs();
                File destFile = new File(destFileName);
                FileOutputStream fos = new FileOutputStream(destFile);
                workbook.write(fos);
                fos.close();
                final AMedia media = new AMedia(destFile, "xls", null);
                Filedownload.save(media);
            } else {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                is = new FileInputStream(FileUtil.find(getReport() + ".jasper"));
                JasperPrint jasperPrint;
                if (NullStatus.isNull(getCommandName())) {
                    jasperPrint = JasperFillManager.fillReport(is, parameters, Connection.getActive());
                } else {
                    command = UiUtil.getDbCommand(getCommandName());
                    command.executeReader();
                    jasperPrint = JasperFillManager.fillReport(is, parameters, new JRResultSetDataSource(command.getResultSet()));
                    command.close();
                }
                JRAbstractExporter exporter = null;
                if (Compare.equal(getType(), TASK_CSV)) {
                    exporter = new JRCsvExporter();
                } else if (Compare.equal(getType(), TASK_ODT)) {
                    exporter = new JROdtExporter();
                } else if (Compare.equal(getType(), TASK_RTF)) {
                    exporter = new JRRtfExporter();
                } else if (Compare.equal(getType(), TASK_XML)) {
                    exporter = new JRXmlExporter();
                }
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
                exporter.exportReport();
                output.close();
                amedia = new AMedia(getReport() + "." + getType(), getType(), null, new ByteArrayInputStream(output.toByteArray()));
                setContent(amedia);
            }
            if (is != null) {
                is.close();
                is = null;
            }
            if (command != null) {
                command.close();
                command = null;
            }
            Connection.closeActive();
        } catch (Exception ex) {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
            if (command != null) {
                try {
                    command.close();
                    command = null;
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
            UiUtil.handleException(ex);
        }
    }

    public void refresh() {
        setReport(getReport());
    }

    public ArrayList<DbParameter> getDbParameters() {
        return dbParameters;
    }

    public static void setReportType(Event event) throws Exception {
        String reportFrame = UiUtil.getComponentTask(event.getTarget()).getPropertyAsString("reportFrame");
        if (NullStatus.isNotNull(reportFrame)) {
            ((Jasperreport) UiUtil.findComponent(reportFrame)).setType(((Image) event.getTarget()).getTooltiptext());
            ((Jasperreport) UiUtil.findComponent(reportFrame)).refresh();
            return;
        }
        String reportTabbox = UiUtil.getComponentTask(event.getTarget()).getPropertyAsString("reportTabbox");
        if (NullStatus.isNotNull(reportTabbox)) {
            Tabbox jasperTabbox = (Tabbox) UiUtil.findComponent(reportTabbox);
            Object component = jasperTabbox.getSelectedPanel().getChildren().get(0);
            if (component != null) {
                if (Jasperreport.class.isAssignableFrom(component.getClass())) {
                    Jasperreport jasperreport = (Jasperreport) component;
                    jasperreport.setType(((Image) event.getTarget()).getTooltiptext());
                    jasperreport.refresh();
                    return;
                }
            }
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(String resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) throws Exception {
        this.refreshTime = refreshTime;
        if (refreshTime > 0) {
            Timer timer = new Timer();
            timer.setRepeats(true);
            timer.setDelay(refreshTime);
            timer.addEventListener(Events.ON_TIMER, new EventListener() {

                public void onEvent(Event arg0) throws Exception {
                    if (UiUtil.getActiveForm() != arg0.getTarget().getParent()) {
                        return;
                    }
                    refresh();
                }
            });
            UiUtil.addComponent(UiUtil.getActiveForm(), timer);
        }
    }
}
