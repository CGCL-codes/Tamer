package net.sealisland.gt8.client.effect.equalizer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Properties;
import javax.swing.JPanel;
import net.sealisland.gt8.client.parameter.ParameterMonitor;
import net.sealisland.gt8.client.patch.PatchModelled;
import net.sealisland.gt8.model.Parameter;
import net.sealisland.gt8.model.Patch;
import net.sealisland.swing.util.ColorToolkit;
import net.sealisland.swing.util.SwingCoalescer;
import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class EqualizerAccessory extends JPanel implements PatchModelled {

    private static final Logger log = Logger.getLogger(EqualizerAccessory.class);

    private JFreeChart chart;

    private EqualizerDataset equalizer;

    private ParameterMonitor lowCutMonitor;

    private ParameterMonitor lowEqMonitor;

    private ParameterMonitor lowMidFrequencyMonitor;

    private ParameterMonitor lowMidEqMonitor;

    private ParameterMonitor lowMidQMonitor;

    private ParameterMonitor highMidFrequencyMonitor;

    private ParameterMonitor highMidEqMonitor;

    private ParameterMonitor highMidQMonitor;

    private SwingCoalescer updateCoalescer;

    private ParameterMonitor highEqMonitor;

    private ParameterMonitor highCutMonitor;

    private ParameterMonitor levelMonitor;

    public EqualizerAccessory() {
        super();
        this.equalizer = new EqualizerDataset();
        chart = createChart();
        updateCoalescer = new SwingCoalescer(100, false) {

            public void run() {
                updateChart();
            }
        };
        setOpaque(false);
        setPreferredSize(new Dimension(400, 300));
        createMonitors();
    }

    private void createMonitors() {
        lowCutMonitor = new ParameterMonitor(null, new Parameter.Listener() {

            public void parameterChanged(Parameter parameter) {
                equalizer.setLowCut((parameter == null) ? 0 : parameter.numericValue());
                updateCoalescer.trigger();
            }
        }, true);
        lowEqMonitor = new ParameterMonitor(null, new Parameter.Listener() {

            public void parameterChanged(Parameter parameter) {
                equalizer.setLowEq((parameter == null) ? 0 : parameter.numericValue());
                updateCoalescer.trigger();
            }
        }, true);
        lowMidFrequencyMonitor = new ParameterMonitor(null, new Parameter.Listener() {

            public void parameterChanged(Parameter parameter) {
                equalizer.setLowMidFrequency((parameter == null) ? 0 : parameter.numericValue());
                updateCoalescer.trigger();
            }
        }, true);
        lowMidEqMonitor = new ParameterMonitor(null, new Parameter.Listener() {

            public void parameterChanged(Parameter parameter) {
                equalizer.setLowMidEq((parameter == null) ? 0 : parameter.numericValue());
                updateCoalescer.trigger();
            }
        }, true);
        lowMidQMonitor = new ParameterMonitor(null, new Parameter.Listener() {

            public void parameterChanged(Parameter parameter) {
                equalizer.setLowMidQ((parameter == null) ? 0 : parameter.numericValue());
                updateCoalescer.trigger();
            }
        }, true);
        highMidFrequencyMonitor = new ParameterMonitor(null, new Parameter.Listener() {

            public void parameterChanged(Parameter parameter) {
                equalizer.setHighMidFrequency((parameter == null) ? 0 : parameter.numericValue());
                updateCoalescer.trigger();
            }
        }, true);
        highMidEqMonitor = new ParameterMonitor(null, new Parameter.Listener() {

            public void parameterChanged(Parameter parameter) {
                equalizer.setHighMidEq((parameter == null) ? 0 : parameter.numericValue());
                updateCoalescer.trigger();
            }
        }, true);
        highMidQMonitor = new ParameterMonitor(null, new Parameter.Listener() {

            public void parameterChanged(Parameter parameter) {
                equalizer.setHighMidQ((parameter == null) ? 0 : parameter.numericValue());
                updateCoalescer.trigger();
            }
        }, true);
        highEqMonitor = new ParameterMonitor(null, new Parameter.Listener() {

            public void parameterChanged(Parameter parameter) {
                equalizer.setHighEq((parameter == null) ? 0 : parameter.numericValue());
                updateCoalescer.trigger();
            }
        }, true);
        highCutMonitor = new ParameterMonitor(null, new Parameter.Listener() {

            public void parameterChanged(Parameter parameter) {
                equalizer.setHighCut((parameter == null) ? 0 : parameter.numericValue());
                updateCoalescer.trigger();
            }
        }, true);
        levelMonitor = new ParameterMonitor(null, new Parameter.Listener() {

            public void parameterChanged(Parameter parameter) {
                equalizer.setLevel((parameter == null) ? 0 : parameter.numericValue());
                updateCoalescer.trigger();
            }
        }, true);
    }

    public void setProperties(Properties properties) {
        initialiseChart(properties);
        initialiseMonitors(properties);
    }

    private void initialiseMonitors(Properties properties) {
        lowCutMonitor.setParameterId(properties.getProperty("low-cut"));
        lowEqMonitor.setParameterId(properties.getProperty("low-eq"));
        lowMidFrequencyMonitor.setParameterId(properties.getProperty("low-middle-frequency"));
        lowMidEqMonitor.setParameterId(properties.getProperty("low-middle-eq"));
        lowMidQMonitor.setParameterId(properties.getProperty("low-middle-q"));
        highMidFrequencyMonitor.setParameterId(properties.getProperty("high-middle-frequency"));
        highMidEqMonitor.setParameterId(properties.getProperty("high-middle-eq"));
        highMidQMonitor.setParameterId(properties.getProperty("high-middle-q"));
        highEqMonitor.setParameterId(properties.getProperty("high-eq"));
        highCutMonitor.setParameterId(properties.getProperty("high-cut"));
        levelMonitor.setParameterId(properties.getProperty("level"));
    }

    private void initialiseChart(Properties properties) {
        double frequencyMin = Double.parseDouble(properties.getProperty("frequency-min"));
        double frequencyMax = Double.parseDouble(properties.getProperty("frequency-max"));
        double frequencyResolution = Double.parseDouble(properties.getProperty("frequency-res"));
        double levelMin = Double.parseDouble(properties.getProperty("level-min"));
        double levelMax = Double.parseDouble(properties.getProperty("level-max"));
        chart.getXYPlot().getRangeAxis().setRange(levelMin, levelMax);
        chart.getXYPlot().getDomainAxis().setRange(frequencyMin, frequencyMax);
        equalizer.setFrequencyRange(frequencyMin, frequencyMax, frequencyResolution);
    }

    public void setPatch(Patch patch) {
        lowCutMonitor.setPatch(patch);
        lowEqMonitor.setPatch(patch);
        lowMidFrequencyMonitor.setPatch(patch);
        lowMidEqMonitor.setPatch(patch);
        lowMidQMonitor.setPatch(patch);
        highMidFrequencyMonitor.setPatch(patch);
        highMidEqMonitor.setPatch(patch);
        highMidQMonitor.setPatch(patch);
        highCutMonitor.setPatch(patch);
        highEqMonitor.setPatch(patch);
        levelMonitor.setPatch(patch);
    }

    protected JFreeChart createChart() {
        Font tickFont = getFont().deriveFont(Font.PLAIN, 9.0f);
        Font labelFont = getFont().deriveFont(Font.PLAIN, 11.0f);
        NumberAxis levelAxis = new NumberAxis("Level");
        levelAxis.setAutoRange(false);
        levelAxis.setLabelFont(labelFont);
        levelAxis.setTickLabelFont(tickFont);
        LogarithmicAxis frequencyAxis = new LogarithmicAxis("Frequency");
        frequencyAxis.setAutoRange(false);
        frequencyAxis.setVerticalTickLabels(true);
        frequencyAxis.setLabelFont(labelFont);
        frequencyAxis.setTickLabelFont(tickFont);
        XYItemRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);
        XYItemRenderer areaRenderer = new XYAreaRenderer();
        areaRenderer.setBaseSeriesVisibleInLegend(false);
        setRenderProperties(0, Color.getHSBColor(0.0f, 0.75f, 0.75f), 1, true, lineRenderer, areaRenderer);
        setRenderProperties(1, Color.getHSBColor(0.15f, 0.75f, 0.75f), 1, true, lineRenderer, areaRenderer);
        setRenderProperties(2, Color.getHSBColor(0.3f, 0.75f, 0.75f), 1, true, lineRenderer, areaRenderer);
        setRenderProperties(3, Color.getHSBColor(0.45f, 0.75f, 0.75f), 1, true, lineRenderer, areaRenderer);
        setRenderProperties(4, Color.getHSBColor(0.6f, 0.75f, 0.75f), 1, true, lineRenderer, areaRenderer);
        setRenderProperties(5, Color.getHSBColor(0.75f, 0.75f, 0.75f), 1, true, lineRenderer, areaRenderer);
        setRenderProperties(6, Color.getHSBColor(0.9f, 0.75f, 0.75f), 1, true, lineRenderer, areaRenderer);
        setRenderProperties(7, new Color(0, 0, 0), 3, false, lineRenderer, areaRenderer);
        XYPlot plot = new XYPlot();
        plot.setDomainAxis(frequencyAxis);
        plot.setRangeAxis(levelAxis);
        plot.setDataset(0, equalizer);
        plot.setDataset(1, equalizer);
        plot.setRenderer(0, areaRenderer);
        plot.setRenderer(1, lineRenderer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
        JFreeChart chart = new JFreeChart(plot);
        chart.getLegend().setItemFont(labelFont);
        chart.setAntiAlias(true);
        return chart;
    }

    protected void updateChart() {
        equalizer.updateSamples();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        long time = -System.currentTimeMillis();
        Graphics2D g2 = (Graphics2D) g;
        Insets insets = getInsets();
        Rectangle bounds = new Rectangle(insets.left, insets.top, getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom);
        chart.draw(g2, bounds);
        time += System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug(String.format("Repainted equalizer: time=%dms", time));
        }
    }

    private static void setRenderProperties(int series, Color color, int strokeWidth, boolean filled, XYItemRenderer lineRenderer, XYItemRenderer areaRenderer) {
        areaRenderer.setSeriesVisible(series, filled);
        lineRenderer.setSeriesPaint(series, color);
        lineRenderer.setSeriesStroke(series, new BasicStroke(strokeWidth));
        areaRenderer.setSeriesPaint(series, ColorToolkit.alpha(color, 63));
    }
}
