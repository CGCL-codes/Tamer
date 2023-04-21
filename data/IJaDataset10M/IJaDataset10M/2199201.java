package com.joejag.mavenstats.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.joejag.mavenstats.client.service.ChartGenerator;
import com.joejag.mavenstats.server.dao.FakeProjectsDao;
import com.joejag.mavenstats.server.reports.JFreechartable;
import com.joejag.mavenstats.server.reports.impls.MostUsedDependenciesByGroupingReport;
import com.joejag.mavenstats.server.reports.impls.MostUsedDependenciesByNameReport;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.general.DefaultPieDataset;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class ChartGeneratorImpl extends RemoteServiceServlet implements ChartGenerator {

    private HashMap<String, JFreechartable> reports = new HashMap<String, JFreechartable>();

    public ChartGeneratorImpl() {
        JFreechartable mostUsedDependenciesReportByGrouping = new MostUsedDependenciesByGroupingReport(FakeProjectsDao.instance());
        JFreechartable mostUsedDependenciesReportByName = new MostUsedDependenciesByNameReport(FakeProjectsDao.instance());
        reports.put(mostUsedDependenciesReportByGrouping.getReportName(), mostUsedDependenciesReportByGrouping);
        reports.put(mostUsedDependenciesReportByName.getReportName(), mostUsedDependenciesReportByName);
    }

    public String generateReport(String reportName) {
        if (!reports.containsKey(reportName)) return "";
        return attachChartToSession(reports.get(reportName).getJFreeChart());
    }

    private String attachChartToSession(JFreeChart chart) {
        String chartName = "";
        try {
            HttpSession session = getThreadLocalRequest().getSession();
            chartName = ServletUtilities.saveChartAsJPEG(chart, 600, 300, null, session);
        } catch (Exception e) {
        }
        return chartName;
    }

    public String generateSampleChart(String teamname) {
        JFreeChart chart = ChartFactory.createPieChart(teamname, createDataSet(teamname), false, false, false);
        return attachChartToSession(chart);
    }

    private DefaultPieDataset createDataSet(String teamname) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue(teamname, 10.0);
        dataset.setValue("Two", 50.0);
        dataset.setValue("Three", 30.0);
        return dataset;
    }
}
