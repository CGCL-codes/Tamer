package org.regilo.statistics.analytics.page;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.regilo.core.model.NoSuchConnectorException;
import org.regilo.core.ui.editors.pages.BasePage;
import org.regilo.core.utils.variables.Variable;
import org.regilo.core.utils.variables.VariableManager;
import org.regilo.statistics.analytics.jobs.GoogleJob;
import org.regilo.statistics.analytics.jobs.GoogleJobChangeListener;
import org.regilo.statistics.analytics.model.AnalyticsConnector;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IAxisSet;
import org.swtchart.IAxisTick;
import org.swtchart.ILegend;
import org.swtchart.ISeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ITitle;
import org.swtchart.ISeries.SeriesType;
import com.google.gdata.data.analytics.DataEntry;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.data.analytics.Dimension;
import com.google.gdata.data.analytics.Metric;

public class AnalyticsPage extends BasePage {

    public AnalyticsPage() {
        super("org.regilo.statistics.analytics.page.AnalyticsPage", "Analytics");
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);
        ColumnLayout layout = new ColumnLayout();
        layout.maxNumColumns = 2;
        layout.minNumColumns = 2;
        layout.topMargin = 5;
        layout.bottomMargin = 5;
        layout.leftMargin = 2;
        layout.rightMargin = 2;
        layout.horizontalSpacing = 20;
        layout.verticalSpacing = 17;
        form.getBody().setLayout(layout);
        AnalyticsConnector connector = (AnalyticsConnector) getDrupalSite().getConnector(AnalyticsConnector.TYPE);
        if (!connector.isValid()) {
            final Composite client = createSection(managedForm, "Analytics", "", 1);
            getToolkit().createLabel(client, "Analytics description...");
            Label notValid = getToolkit().createLabel(client, "Analytics data in properties are invalid! Click here to edit.");
        } else {
            try {
                String analyticsAccount = "UA-" + connector.getAnalyticsId() + "-1";
                Variable variable = new Variable("googleanalytics_account", analyticsAccount);
                VariableManager manager = new VariableManager(getBrowser());
                manager.variableSet(variable, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            createChartSection(managedForm, "Visit", "The total number of visits. A visit consists of a single-user session.", new String[] { "ga:visits" });
            createChartSection(managedForm, "New visits", "The number of visitors whose visit to your site was marked as a first-time visit. ", new String[] { "ga:newVisits" });
            createChartSection(managedForm, "Bounces", "The total number of single-page visits to your site.", new String[] { "ga:bounces" });
            createChartSection(managedForm, "Time on site", "The total duration of visitor sessions over a month.", new String[] { "ga:timeOnSite" });
        }
    }

    private void createChartSection(IManagedForm managedForm, String title, String description, String[] metrics) {
        final Composite client = createSection(managedForm, title, description, 1);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 130;
        final Chart chart = new Chart(client, SWT.NONE);
        chart.setLayoutData(gd);
        ILegend legend = chart.getLegend();
        legend.setVisible(false);
        ITitle graphTitle = chart.getTitle();
        graphTitle.setVisible(false);
        AnalyticsConnector connector = (AnalyticsConnector) getDrupalSite().getConnector(AnalyticsConnector.TYPE);
        Date endDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        c.roll(Calendar.MONTH, false);
        Date startDate = c.getTime();
        final GoogleJob job = new GoogleJob(title);
        job.setConnector(connector);
        job.setDimensions(new String[] { "ga:date" });
        job.setMetrics(metrics);
        job.setStartDate(startDate);
        job.setEndDate(endDate);
        job.addJobChangeListener(new GoogleJobChangeListener() {

            public void done(IJobChangeEvent event) {
                final DataFeed feed = job.getFeed();
                PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        updateChart(chart, feed);
                    }
                });
            }
        });
        job.schedule();
    }

    protected void updateChart(Chart chart, DataFeed feed) {
        List<DataEntry> entries = feed.getEntries();
        double[] data = new double[entries.size()];
        Date[] labels = new Date[entries.size()];
        int i = 0;
        for (DataEntry entry : entries) {
            List<Dimension> dimensions = entry.getDimensions();
            String valueX = dimensions.get(0).getValue();
            List<Metric> metrics = entry.getMetrics();
            String valueY = metrics.get(0).getValue();
            data[i] = Double.parseDouble(valueY);
            try {
                labels[i] = new SimpleDateFormat("yyyyMMdd").parse(valueX);
            } catch (ParseException e) {
                labels[i] = new Date();
            }
            i++;
        }
        ISeriesSet seriesSet = chart.getSeriesSet();
        ISeries series = seriesSet.createSeries(SeriesType.LINE, "line series");
        series.setYSeries(data);
        series.setXDateSeries(labels);
        IAxisSet axisSet = chart.getAxisSet();
        IAxisTick xTick = axisSet.getXAxis(0).getTick();
        DateFormat format = new SimpleDateFormat("dd-MM");
        xTick.setFormat(format);
        IAxis xAxis = axisSet.getXAxis(0);
        ITitle xAxisTitle = xAxis.getTitle();
        IAxis yAxis = axisSet.getYAxis(0);
        ITitle yAxisTitle = yAxis.getTitle();
        xAxisTitle.setVisible(false);
        yAxisTitle.setVisible(false);
        axisSet.adjustRange();
        chart.redraw();
    }
}
