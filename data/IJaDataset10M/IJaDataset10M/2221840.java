package org.mili.jmibs.jfree;

import java.awt.*;
import java.util.List;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.data.category.*;
import org.mili.jmibs.api.*;

/**
 * This class defines a special implementation of interface BenchmarkSuiteResultRenderer that
 * renders the benchmark suite results in a jFreeChart representation.
 *
 * @author Michael Lieshoff
 * @version 1.0 16.06.2010
 * @since 1.1
 */
public class JFreeChartBarIterationIntervalBenchmarkSuiteResultRenderer implements BenchmarkSuiteResultRenderer<JFreeChart> {

    /**
     * creates a new string iteration interval benchmark suite result renderer.
     */
    protected JFreeChartBarIterationIntervalBenchmarkSuiteResultRenderer() {
        super();
    }

    /**
     * creates a new string iteration interval benchmark suite result renderer.
     *
     * @return new string iteration interval benchmark suite result renderer.
     */
    public static JFreeChartBarIterationIntervalBenchmarkSuiteResultRenderer create() {
        return new JFreeChartBarIterationIntervalBenchmarkSuiteResultRenderer();
    }

    @Override
    public JFreeChart render(BenchmarkSuiteResult bsr) {
        CategoryDataset dataset = this.createDataset(bsr.getExecuteResults());
        JFreeChart chart = this.createChart(bsr.getBenchmarkSuite().getName(), dataset);
        return chart;
    }

    private CategoryDataset createDataset(List<BenchmarkResult> lbr) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for (int i = 0, n = lbr.size(); i < n; i++) {
            BenchmarkResult br = lbr.get(i);
            IterationIntervalBenchmarkContext<?> iolbc = (IterationIntervalBenchmarkContext<?>) br.getBenchmarkContext();
            Benchmark b = iolbc.getBenchmark();
            int ic = iolbc.getIteration();
            Interval<?> iv = iolbc.getInterval();
            ds.addValue(br.getTotalTimeNanos(), String.valueOf(ic) + "/" + String.valueOf(iv), b.getName());
        }
        return ds;
    }

    private JFreeChart createChart(String title, CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(title, "Benchmark (Iteration/Interval)", "Time in ns", dataset, PlotOrientation.HORIZONTAL, true, true, false);
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f, 0.0f, new Color(0, 0, 64));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f, 0.0f, new Color(0, 64, 0));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f, 0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
        return chart;
    }
}
