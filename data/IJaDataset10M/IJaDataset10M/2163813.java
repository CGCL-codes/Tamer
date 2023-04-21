package binky.reportrunner.ui.actions.admin;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.security.access.prepost.PreAuthorize;
import binky.reportrunner.data.RunnerHistoryEvent;
import binky.reportrunner.service.AuditService;
import binky.reportrunner.ui.actions.base.StandardRunnerAction;

public class GetAuditChartAction extends StandardRunnerAction {

    private static final long serialVersionUID = 1L;

    private AuditService auditService;

    private JFreeChart chart;

    private long fromDate;

    private long toDate;

    private String module;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String execute() throws Exception {
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        List<RunnerHistoryEvent> events = auditService.getSuccessEvents(module, new Date(fromDate), new Date(toDate));
        Map<String, TimeSeries> serieses = new HashMap<String, TimeSeries>();
        for (RunnerHistoryEvent e : events) {
            TimeSeries s = serieses.get(e.getMethod());
            if (s == null) {
                s = new TimeSeries(e.getMethod());
                serieses.put(e.getMethod(), s);
            }
            s.addOrUpdate(new Millisecond(e.getTimeStamp()), e.getRunTime());
        }
        for (TimeSeries s : serieses.values()) {
            dataSet.addSeries(s);
        }
        chart = ChartFactory.createTimeSeriesChart(module, "", "run time (ms)", dataSet, true, false, false);
        XYPlot linePlot = (XYPlot) chart.getPlot();
        linePlot.setDomainGridlinesVisible(true);
        linePlot.setRangeGridlinesVisible(true);
        linePlot.setRangeGridlinePaint(Color.black);
        linePlot.setDomainGridlinePaint(Color.black);
        TextTitle subTitle = new TextTitle("Successful Executions");
        subTitle.setFont(new Font("Arial", Font.ITALIC, 10));
        chart.addSubtitle(subTitle);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 12));
        DateAxis axis = (DateAxis) linePlot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss dd-MM-yyyy"));
        XYItemRenderer r = linePlot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
        }
        chart.setAntiAlias(true);
        chart.setTextAntiAlias(true);
        return SUCCESS;
    }

    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

    public JFreeChart getChart() {
        return chart;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public final List<String> getModules() {
        return auditService.getModuleNames();
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }
}
