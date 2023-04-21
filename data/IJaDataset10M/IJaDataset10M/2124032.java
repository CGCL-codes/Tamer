package edu.upmc.opi.caBIG.caTIES.client.vr.statistics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class StatisticsReportPanel extends JPanel {

    DefaultPieDataset pieData;

    DefaultCategoryDataset barData;

    ChartDataPanel chartDataPanel;

    Logger logger = Logger.getLogger(this.getClass());

    Paint genderBinPaint = new GradientPaint(0.0F, 0.0F, new Color(180, 228, 255), 0.0F, 0.0F, new Color(77, 187, 255));

    Paint ageBinPaint = new GradientPaint(0.0F, 0.0F, new Color(255, 248, 176), 0.0F, 0.0F, new Color(254, 230, 156));

    Paint collectionYearBinPaint = new GradientPaint(0.0F, 0.0F, new Color(229, 255, 225), 0.0F, 0.0F, new Color(136, 255, 123));

    Paint organizationBinPaint = new GradientPaint(0.0F, 0.0F, new Color(255, 223, 223), 0.0F, 0.0F, new Color(255, 128, 125));

    Paint conceptsBinPaint = new GradientPaint(0.0F, 0.0F, new Color(242, 223, 255), 0.0F, 0.0F, new Color(195, 125, 255));

    private String headerHelpText = "Click on column header to sort";

    private JLabel headerhelpLabel;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }

    public StatisticsReportPanel() {
        initGUI();
    }

    private void initGUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerhelpLabel = new JLabel(headerHelpText);
        headerhelpLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
        headerhelpLabel.setForeground(Color.darkGray);
        headerhelpLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    }

    public void initReport(ChartData data) {
        this.removeAll();
        JFreeChart chart = null;
        if (data.getChartType() == ChartData.BAR_CHART) {
            barData = new DefaultCategoryDataset();
            chart = ChartFactory.createBarChart(null, data.getPrimaryBin(), data.getYAxisLabel(), barData, PlotOrientation.VERTICAL, true, false, false);
        } else if (data.getChartType() == ChartData.PIE_CHART) {
            pieData = new DefaultPieDataset();
            chart = ChartFactory.createPieChart(null, pieData, true, false, false);
        }
        if (chart == null) return;
        ChartPanel chartPanel = new ChartPanel(chart);
        Dimension d = this.getVisibleRect().getSize();
        double width = d.getWidth() - 20;
        double height = d.getHeight() - 20;
        logger.debug("Available Size:" + width + ":" + height);
        if (height < width * 0.75) {
            width = (int) (height * 1.3);
        } else {
            height = (int) (width * 0.75);
        }
        chartPanel.setPreferredSize(new Dimension((int) width, (int) height));
        chartPanel.setMaximumSize(new Dimension((int) width, (int) height));
        logger.debug("Chart Size:" + width + ":" + height);
        applyAesthetics(chartPanel, data);
        this.add(chartPanel);
        this.add(Box.createVerticalStrut(10));
        this.add(headerhelpLabel);
        chartDataPanel = new ChartDataPanel(data);
        chartDataPanel.setMaximumSize(new Dimension(5000, 120));
        chartDataPanel.setPreferredSize(new Dimension(400, 120));
        chartDataPanel.setMinimumSize(new Dimension(400, 120));
        JPanel p = new JPanel(new BorderLayout());
        p.add(chartDataPanel, BorderLayout.WEST);
        this.add(p);
        chartPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        chartDataPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        p.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        p.setOpaque(false);
    }

    private void applyAesthetics(ChartPanel chartPanel, ChartData data) {
        chartPanel.getChart().setBackgroundPaint(new Color(240, 240, 240));
        chartPanel.setBorder(new CompoundBorder(new LineBorder(new Color(191, 191, 191), 1), new LineBorder(new Color(240, 240, 240), 10)));
        if (data.getChartType() == ChartData.BAR_CHART) {
            CategoryPlot plot = chartPanel.getChart().getCategoryPlot();
            BarRenderer barrenderer = (BarRenderer) plot.getRenderer();
            barrenderer.setBaseSeriesVisible(true);
            barrenderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            barrenderer.setItemLabelsVisible(true);
            barrenderer.setItemMargin(.1F);
            CategoryAxis categoryaxis = plot.getDomainAxis();
            categoryaxis.setCategoryMargin(0.1F);
            if (data.getSecondaryBin() == ChartData.NONE_BIN) barrenderer.setPaint(getPaintForBin(data.getPrimaryBin()));
        } else if (data.getChartType() == ChartData.PIE_CHART) {
            PiePlot plot = (PiePlot) (chartPanel.getChart().getPlot());
            plot.setCircular(true);
            Paint paint = new Color(26, 205, 255);
            plot.setSectionPaint(0, paint);
            paint = new Color(255, 157, 215);
            plot.setSectionPaint(1, paint);
            paint = new Color(179, 255, 207);
            plot.setSectionPaint(2, paint);
            paint = new Color(255, 126, 51);
            plot.setSectionPaint(3, paint);
            paint = new Color(126, 51, 255);
            plot.setSectionPaint(4, paint);
            paint = new Color(137, 237, 78);
            plot.setSectionPaint(5, paint);
            paint = new Color(255, 255, 220);
            plot.setSectionPaint(6, paint);
            paint = new Color(255, 0, 0);
            plot.setSectionPaint(7, paint);
            paint = new Color(255, 51, 180);
            plot.setSectionPaint(8, paint);
            paint = new Color(179, 227, 255);
            plot.setSectionPaint(9, paint);
            paint = new Color(255, 179, 227);
        }
        LegendTitle legend = chartPanel.getChart().getLegend();
        legend.setBackgroundPaint(new Color(240, 240, 240));
        legend.setBorder(0, 0, 0, 0);
    }

    private Paint getPaintForBin(String bin) {
        if (bin == ChartData.AGE_BIN) return ageBinPaint; else if (bin == ChartData.GENDER_BIN) return genderBinPaint; else if (bin == ChartData.COLLECTION_YEAR_BIN) return collectionYearBinPaint; else if (bin == ChartData.CONCEPTS_BIN) return conceptsBinPaint; else if (bin == ChartData.ORGANIZATION_BIN) return organizationBinPaint; else return Color.gray;
    }

    public void updateData(ChartData data) {
        chartDataPanel.updateData(data);
        if (data.getChartType() == ChartData.BAR_CHART) {
            if (data.getSecondaryBin() != ChartData.NONE_BIN) {
                for (int i = 0; i < data.getPrimaryCutpoints().length; i++) {
                    for (int j = 0; j < data.getSecondaryCutpoints().length; j++) {
                        String key = data.getPrimaryCutpoints()[i].getFieldName() + ChartData.SEPARATOR + data.getSecondaryCutpoints()[j].getFieldName();
                        ChartDatapoint value = data.getData().get(key);
                        Integer intval = new Integer(0);
                        if (value != null) {
                            intval = (Integer) value.getCount();
                        }
                        barData.setValue(intval, data.getSecondaryCutpoints()[j].getFieldName(), data.getPrimaryCutpoints()[i].getFieldName());
                        logger.debug("Setting " + data.getPrimaryCutpoints()[i] + data.getSecondaryCutpoints()[j] + ": " + intval);
                    }
                }
            } else {
                for (int i = 0; i < data.getPrimaryCutpoints().length; i++) {
                    String key = data.getPrimaryCutpoints()[i].getFieldName();
                    ChartDatapoint value = data.getData().get(key);
                    Integer intval = new Integer(0);
                    if (value != null) {
                        intval = (Integer) value.getCount();
                    }
                    barData.setValue(intval, data.getPrimaryBin(), data.getPrimaryCutpoints()[i].getFieldName());
                    logger.debug("Setting " + data.getPrimaryCutpoints()[i].getFieldName() + ": " + intval);
                }
            }
        } else if (data.getChartType() == ChartData.PIE_CHART) {
            for (int i = 0; i < data.getPrimaryCutpoints().length; i++) {
                String key = data.getPrimaryCutpoints()[i].getFieldName();
                ChartDatapoint value = data.getData().get(key);
                Integer intval = new Integer(0);
                if (value != null) {
                    intval = (Integer) value.getCount();
                }
                pieData.setValue(data.getPrimaryCutpoints()[i].getFieldName(), intval);
            }
        }
    }

    static class LabelGenerator extends StandardCategoryItemLabelGenerator {

        private String s;

        public String generateLabel(CategoryDataset categorydataset, int i, int j) {
            return s;
        }

        LabelGenerator(String s) {
            this.s = s;
        }
    }

    public void updateUIForQueryDone() {
    }
}
