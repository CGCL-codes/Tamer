package org.efs.openreports.actions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.efs.openreports.ORStatics;
import org.efs.openreports.engine.ChartReportEngine;
import org.efs.openreports.engine.input.ReportEngineInput;
import org.efs.openreports.engine.output.ChartEngineOutput;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.objects.chart.ChartValue;
import org.efs.openreports.util.LocalStrings;
import org.jfree.chart.imagemap.ImageMapUtilities;
import com.opensymphony.xwork2.ActionContext;

public class ChartReportAction extends QueryReportAction {

    private static final long serialVersionUID = -6645146769113032498L;

    protected static Logger log = Logger.getLogger(ChartReportAction.class);

    private String imageMap;

    private ChartValue[] chartValues;

    private long chartRequestId;

    public String execute() {
        ReportUser user = (ReportUser) ActionContext.getContext().getSession().get(ORStatics.REPORT_USER);
        report = (Report) ActionContext.getContext().getSession().get(ORStatics.REPORT);
        Map<String, Object> reportParameters = getReportParameterMap(user);
        ReportLog reportLog = new ReportLog(user, report, new Date());
        try {
            log.debug("Starting Chart Report: " + report.getName());
            reportLogProvider.insertReportLog(reportLog);
            ChartReportEngine chartReportEngine = new ChartReportEngine(dataSourceProvider, directoryProvider, propertiesProvider);
            ReportEngineInput input = new ReportEngineInput(report, reportParameters);
            ChartEngineOutput chartOutput = (ChartEngineOutput) chartReportEngine.generateReport(input);
            chartValues = chartOutput.getChartValues();
            if (chartValues.length == 0) {
                addActionError(getText(LocalStrings.ERROR_REPORT_EMPTY));
            }
            imageMap = ImageMapUtilities.getImageMap("chart", chartOutput.getChartRenderingInfo());
            HashMap<String, byte[]> imagesMap = new HashMap<String, byte[]>();
            imagesMap.put("ChartImage", chartOutput.getContent());
            session.put(ORStatics.IMAGES_MAP, imagesMap);
            reportLog.setEndTime(new Date());
            reportLog.setStatus(ReportLog.STATUS_SUCCESS);
            reportLogProvider.updateReportLog(reportLog);
            chartRequestId = reportLog.getEndTime().getTime();
            log.debug("Finished Chart Report: " + report.getName());
        } catch (Exception e) {
            addActionError(getText(e.getMessage()));
            reportLog.setMessage(getText(e.getMessage()));
            reportLog.setStatus(ReportLog.STATUS_FAILURE);
            reportLog.setEndTime(new Date());
            try {
                reportLogProvider.updateReportLog(reportLog);
            } catch (Exception ex) {
                log.error("Unable to create ReportLog: " + ex.getMessage());
            }
            return ERROR;
        }
        return SUCCESS;
    }

    public String getImageMap() {
        return imageMap;
    }

    public void setImageMap(String imageMap) {
        this.imageMap = imageMap;
    }

    public ChartValue[] getChartValues() {
        return chartValues;
    }

    public long getChartRequestId() {
        return chartRequestId;
    }

    public void setChartRequestId(long chartRequestId) {
        this.chartRequestId = chartRequestId;
    }
}
