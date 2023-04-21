package com.brekeke.report.history.dao.impl;

import java.util.Properties;
import com.brekeke.report.engine.common.Constants;
import com.brekeke.report.engine.util.TextFileUtil;
import com.brekeke.report.history.common.ReportType;
import com.brekeke.report.history.dao.DailyReportDao;
import com.brekeke.report.history.entity.DailyReport;

/**
 * @author madawei
 * @version 1.0
 */
public class DailyReportDaoImpl implements DailyReportDao {

    private Properties dailyReportPp = new Properties();

    ;

    private final String DAILY_REPORT_FILE_PATH = Constants.ROOTPATH + "WEB-INF/conf/history/timer.properties";

    private final String FLAG = "timer.daily.flag";

    private final String START = "timer.daily.start";

    private final String END = "timer.daily.end";

    private final String TYPE_TRUNK_REPORT_BY_DNIS = "timer.daily.type.trunk_report_by_dnis";

    private final String TYPE_TRUNK_REPORT_BY_TIME_ZONE = "timer.daily.type.trunk_report_by_time_zone";

    private final String TYPE_TRUNK_REPORT_BY_DNIS_AND_TIME_ZONE = "timer.daily.type.trunk_report_by_dnis_and_time_zone";

    private final String TYPE_ACD_REPORT_BY_AGENT = "timer.daily.type.acd_report_by_agent";

    private final String TYPE_ACD_REPORT_BY_ACD = "timer.daily.type.acd_report_by_acd";

    private final String TYPE_ACD_REPORT_BY_DNIS = "timer.daily.type.acd_report_by_dnis";

    private final String TYPE_AGENT_REPORT_BY_AGENT = "timer.daily.type.agent_report_by_agent";

    private final String TYPE_AGENT_REPORT_BY_GROUP = "timer.daily.type.agent_report_by_group";

    public DailyReport searchDailyReport() throws Exception {
        DailyReport dailyReport = new DailyReport();
        dailyReportPp = TextFileUtil.loadPropertyFile(DAILY_REPORT_FILE_PATH);
        dailyReport.setFlag(Boolean.valueOf(dailyReportPp.getProperty(FLAG)));
        dailyReport.setStart(dailyReportPp.getProperty(START));
        dailyReport.setEnd(dailyReportPp.getProperty(END));
        dailyReport.setTrunkReportByDnisCSV(ReportType.isCSVType(dailyReportPp.getProperty(TYPE_TRUNK_REPORT_BY_DNIS)));
        dailyReport.setTrunkReportByDnisPDF(ReportType.isPDFType(dailyReportPp.getProperty(TYPE_TRUNK_REPORT_BY_DNIS)));
        dailyReport.setTrunkReportByTimeZoneCSV(ReportType.isCSVType(dailyReportPp.getProperty(TYPE_TRUNK_REPORT_BY_TIME_ZONE)));
        dailyReport.setTrunkReportByTimeZonePDF(ReportType.isPDFType(dailyReportPp.getProperty(TYPE_TRUNK_REPORT_BY_TIME_ZONE)));
        dailyReport.setTrunkReportByDnisAndTimeZoneCSV(ReportType.isCSVType(dailyReportPp.getProperty(TYPE_TRUNK_REPORT_BY_DNIS_AND_TIME_ZONE)));
        dailyReport.setTrunkReportByDnisAndTimeZonePDF(ReportType.isPDFType(dailyReportPp.getProperty(TYPE_TRUNK_REPORT_BY_DNIS_AND_TIME_ZONE)));
        dailyReport.setAcdReportByAgentCSV(ReportType.isCSVType(dailyReportPp.getProperty(TYPE_ACD_REPORT_BY_AGENT)));
        dailyReport.setAcdReportByAgentPDF(ReportType.isPDFType(dailyReportPp.getProperty(TYPE_ACD_REPORT_BY_AGENT)));
        dailyReport.setAcdReportByAcdCSV(ReportType.isCSVType(dailyReportPp.getProperty(TYPE_ACD_REPORT_BY_ACD)));
        dailyReport.setAcdReportByAcdPDF(ReportType.isPDFType(dailyReportPp.getProperty(TYPE_ACD_REPORT_BY_ACD)));
        dailyReport.setAcdReportByDnisCSV(ReportType.isCSVType(dailyReportPp.getProperty(TYPE_ACD_REPORT_BY_DNIS)));
        dailyReport.setAcdReportByDnisPDF(ReportType.isPDFType(dailyReportPp.getProperty(TYPE_ACD_REPORT_BY_DNIS)));
        dailyReport.setAgentReportByAgentCSV(ReportType.isCSVType(dailyReportPp.getProperty(TYPE_AGENT_REPORT_BY_AGENT)));
        dailyReport.setAgentReportByAgentPDF(ReportType.isPDFType(dailyReportPp.getProperty(TYPE_AGENT_REPORT_BY_AGENT)));
        dailyReport.setAgentReportByGroupCSV(ReportType.isCSVType(dailyReportPp.getProperty(TYPE_AGENT_REPORT_BY_GROUP)));
        dailyReport.setAgentReportByGroupPDF(ReportType.isPDFType(dailyReportPp.getProperty(TYPE_AGENT_REPORT_BY_GROUP)));
        return dailyReport;
    }

    public void updateDailyReport(DailyReport dailyReport) throws Exception {
        dailyReportPp.setProperty(FLAG, dailyReport.getFlag().toString());
        dailyReportPp.setProperty(START, dailyReport.getStart());
        dailyReportPp.setProperty(END, dailyReport.getEnd());
        dailyReportPp.setProperty(TYPE_TRUNK_REPORT_BY_DNIS, ReportType.getTypeString(dailyReport.getTrunkReportByDnisCSV(), dailyReport.getTrunkReportByDnisPDF()));
        dailyReportPp.setProperty(TYPE_TRUNK_REPORT_BY_TIME_ZONE, ReportType.getTypeString(dailyReport.getTrunkReportByTimeZoneCSV(), dailyReport.getTrunkReportByTimeZonePDF()));
        dailyReportPp.setProperty(TYPE_TRUNK_REPORT_BY_DNIS_AND_TIME_ZONE, ReportType.getTypeString(dailyReport.getTrunkReportByDnisAndTimeZoneCSV(), dailyReport.getTrunkReportByDnisAndTimeZonePDF()));
        dailyReportPp.setProperty(TYPE_ACD_REPORT_BY_AGENT, ReportType.getTypeString(dailyReport.getAcdReportByAgentCSV(), dailyReport.getAcdReportByAgentPDF()));
        dailyReportPp.setProperty(TYPE_ACD_REPORT_BY_ACD, ReportType.getTypeString(dailyReport.getAcdReportByAcdCSV(), dailyReport.getAcdReportByAcdPDF()));
        dailyReportPp.setProperty(TYPE_ACD_REPORT_BY_DNIS, ReportType.getTypeString(dailyReport.getAcdReportByDnisCSV(), dailyReport.getAcdReportByDnisPDF()));
        dailyReportPp.setProperty(TYPE_AGENT_REPORT_BY_AGENT, ReportType.getTypeString(dailyReport.getAgentReportByAgentCSV(), dailyReport.getAgentReportByAgentPDF()));
        dailyReportPp.setProperty(TYPE_AGENT_REPORT_BY_GROUP, ReportType.getTypeString(dailyReport.getAgentReportByGroupCSV(), dailyReport.getAgentReportByGroupPDF()));
        TextFileUtil.storePropertyFile(dailyReportPp, DAILY_REPORT_FILE_PATH);
    }
}
