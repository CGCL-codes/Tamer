package au.gov.nla.aons.mvc.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.imagemap.ImageMapUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultXYDataset;
import org.springframework.web.servlet.View;
import au.gov.nla.aons.mvc.util.FormatObsolescenceChartUtil;
import au.gov.nla.aons.obsolescence.dto.FormatObsolescenceSummary;
import au.gov.nla.aons.obsolescence.dto.SummaryRiskAssessment;

public class FormatObsolescenceTimeSeriesView implements View {

    private FormatObsolescenceChartUtil chartUtil = new FormatObsolescenceChartUtil();

    public String getContentType() {
        return "image/png";
    }

    public void render(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FormatObsolescenceSummary summary = (FormatObsolescenceSummary) model.get("summary");
        Integer width = (Integer) model.get("width");
        Integer height = (Integer) model.get("height");
        JFreeChart chart = chartUtil.createChart(summary);
        if (chart != null) {
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, width, height, info);
        }
    }

    public FormatObsolescenceChartUtil getChartUtil() {
        return chartUtil;
    }

    public void setChartUtil(FormatObsolescenceChartUtil chartUtil) {
        this.chartUtil = chartUtil;
    }
}
