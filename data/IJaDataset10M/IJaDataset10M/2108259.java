package com.commsen.stopwatch.util;

import com.commsen.stopwatch.Report;
import com.commsen.stopwatch.reports.MemoryStopwatchReport;

/**
 * @author Milen Dyankov
 * 
 */
public class ReportsToTextConverter {

    int fixed;

    public ReportsToTextConverter(int columnLength) {
        this.fixed = columnLength;
    }

    /**
	 * 
	 * @see com.commsen.stopwatch.jmx.StopwatchManagerMBean#getReportsAsString(java.lang.String,
	 *      java.lang.String)
	 */
    public String convert(Report[] reports) {
        if (reports == null || reports.length == 0) return null;
        StringBuffer result = new StringBuffer();
        StringBuffer header = new StringBuffer("|");
        StringBuffer divider = new StringBuffer("+");
        header.append(fixedString("group")).append("|");
        divider.append(fixedLine()).append("+");
        header.append(fixedString("label")).append("|");
        divider.append(fixedLine()).append("+");
        header.append(fixedString("count")).append("|");
        divider.append(fixedLine()).append("+");
        header.append(fixedString("min time")).append("|");
        divider.append(fixedLine()).append("+");
        header.append(fixedString("max time")).append("|");
        divider.append(fixedLine()).append("+");
        header.append(fixedString("average time")).append("|");
        divider.append(fixedLine()).append("+");
        header.append(fixedString("total time")).append("|");
        divider.append(fixedLine()).append("+");
        if (reports[0] instanceof MemoryStopwatchReport) {
            header.append(fixedString("min memory")).append("|");
            divider.append(fixedLine()).append("+");
            header.append(fixedString("max memory")).append("|");
            divider.append(fixedLine()).append("+");
            header.append(fixedString("average memory")).append("|");
            divider.append(fixedLine()).append("+");
        }
        String headerDivider = divider.toString().replaceAll("-", "=");
        result.append(headerDivider).append("\n").append(header).append("\n").append(headerDivider).append("\n");
        for (int i = 0; i < reports.length; i++) {
            result.append("|").append(fixedString(reports[i].getGroup())).append("|").append(fixedString(reports[i].getLabel())).append("|").append(fixedString("" + reports[i].getCount())).append("|").append(fixedString("" + reports[i].getMinTime())).append("|").append(fixedString("" + reports[i].getMaxTime())).append("|").append(fixedString("" + reports[i].getAverageTime())).append("|").append(fixedString("" + reports[i].getTotalTime())).append("|");
            if (reports[i] instanceof MemoryStopwatchReport) {
                MemoryStopwatchReport memoryUsageReport = (MemoryStopwatchReport) reports[i];
                result.append(fixedString("" + memoryUsageReport.getMinMemory())).append("|").append(fixedString("" + memoryUsageReport.getMaxMemory())).append("|").append(fixedString("" + memoryUsageReport.getAverageMemory())).append("|");
            }
            result.append("\n").append(divider).append("\n");
        }
        return result.toString();
    }

    private String fixedLine() {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < fixed; i++) result.append("-");
        return result.toString();
    }

    private String fixedString(String s) {
        int l = s.length();
        if (l == fixed) {
            return s;
        } else if (l < fixed) {
            StringBuffer result = new StringBuffer(s);
            for (int i = s.length(); i < fixed; i++) result.append(" ");
            return result.toString();
        } else {
            return s.substring(0, fixed - 3) + "...";
        }
    }
}
