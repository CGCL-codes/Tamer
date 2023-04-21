package gridunit.report.document.charts;

import java.io.File;
import gridunit.report.chart.PieChart;
import gridunit.report.document.TestResultDocument;
import org.jfree.chart.ChartUtilities;
import org.smartfrog.services.junit.TestInfo;

/**
 * @author Alexandro de Souza Soares - alexandro@lsd.ufcg.edu.br
 * 
 * Description: An XML formatter.
 * 
 * @version 1.0 Date: 22/08/2005
 * 
 * Copyright (c) 2002-2005 Universidade Federal de Campina Grande
 */
public class ChartsTestResultDocument implements TestResultDocument {

    private long errorCount;

    private long osTypeCount;

    /**
     * A pie chart for error causes.
     */
    protected PieChart errorCausesPieChart;

    /**
     * Comment for <code>failureCausesPieChart</code>
     */
    protected PieChart failureCausesPieChart;

    /**
     * Full constructor.
     */
    public ChartsTestResultDocument() {
        errorCount = 0;
        osTypeCount = 0;
        errorCausesPieChart = new PieChart("Error Causes");
        failureCausesPieChart = new PieChart("Failure Causes");
    }

    public void init() {
    }

    public void deploy() throws Exception {
        File directory = new File("report/graphs");
        directory.mkdirs();
        ChartUtilities.saveChartAsJPEG(new File("report/graphs/errorcauses.jpg"), 1.0f, errorCausesPieChart.graphicalInstance(), 800, 600);
        ChartUtilities.saveChartAsJPEG(new File("report/graphs/failurecauses.jpg"), 1.0f, failureCausesPieChart.graphicalInstance(), 800, 600);
    }

    public void addSuccessfullyTest(TestInfo testInfo) {
    }

    public void addFailedTest(TestInfo testInfo) {
        String message = testInfo.getFault().getMessage();
        message = (message == null ? "null" : message);
        double value = failureCausesPieChart.getValue(message);
        failureCausesPieChart.addValue(message, value + 1);
    }

    public void addWrongTest(TestInfo testInfo) {
        String message = testInfo.getFault().getClassname();
        message = (message == null ? "null" : message);
        double value = errorCausesPieChart.getValue(message);
        errorCausesPieChart.addValue(message, value + 1);
    }
}
