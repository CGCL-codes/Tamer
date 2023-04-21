package org.kineticsystem.blender.frequency;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYDataImageAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultHeatMapDataset;
import org.jfree.data.general.HeatMapDataset;
import org.jfree.data.general.HeatMapUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.kineticsystem.blender.utils.ActionServer;
import org.kineticsystem.blender.utils.ExportAction;
import org.kineticsystem.commons.layout.Cell;
import org.kineticsystem.commons.layout.TetrisLayout;

/**
 * This is an application to analyze the luminosity of a black and white image.
 */
public class FrequencyPanel extends JPanel implements ChangeListener, ChartChangeListener, ActionServer {

    /** Generated serial number. */
    private static final long serialVersionUID = -4513406790407025961L;

    /** Connected actions. */
    private Map<String, Action> actions;

    private HeatMapDataset dataset;

    private JFreeChart picChart;

    private JFreeChart curveChart;

    private JSlider slider;

    private Crosshair crosshair;

    private Range lastXRange;

    /** Default constructor. */
    public FrequencyPanel() {
        Font titleFont = new Font("Times New Roman", Font.PLAIN, 17);
        Font tickLabelFont = new Font("Times New Roman", Font.PLAIN, 10);
        Font labelFont = new Font("Times New Roman", Font.PLAIN, 12);
        picChart = ChartFactory.createScatterPlot("Picture", "X axis", "Y axis", new XYSeriesCollection(), PlotOrientation.VERTICAL, false, false, false);
        picChart.getTitle().setFont(titleFont);
        dataset = createMapDataset(2);
        PaintScale ps = new GrayPaintScale(0, 1.0, 255);
        BufferedImage image = HeatMapUtilities.createHeatMapImage(dataset, ps);
        XYDataImageAnnotation ann = new XYDataImageAnnotation(image, 0, 0, 1, 1, true);
        XYPlot picPlot = (XYPlot) picChart.getPlot();
        picPlot.setDomainPannable(true);
        picPlot.setRangePannable(true);
        picPlot.getRenderer().addAnnotation(ann, Layer.BACKGROUND);
        NumberAxis picX = (NumberAxis) picPlot.getDomainAxis();
        NumberAxis picY = (NumberAxis) picPlot.getRangeAxis();
        picX.setLowerMargin(0);
        picX.setUpperMargin(0);
        picX.setTickUnit(new NumberTickUnit(0.2));
        picX.setTickLabelFont(tickLabelFont);
        picX.setLabelFont(labelFont);
        picY.setLowerMargin(0);
        picY.setUpperMargin(0);
        picY.setTickUnit(new NumberTickUnit(0.2));
        picY.setTickLabelFont(tickLabelFont);
        picY.setLabelFont(labelFont);
        picChart.addChangeListener(this);
        ChartPanel picPanel = new ChartPanel(picChart);
        picPanel.setPreferredSize(new Dimension(300, 300));
        CrosshairOverlay overlay = new CrosshairOverlay();
        crosshair = new Crosshair(0);
        crosshair.setPaint(Color.RED);
        overlay.addRangeCrosshair(crosshair);
        picPanel.addOverlay(overlay);
        crosshair.setLabelVisible(false);
        XYSeriesCollection dataset = new XYSeriesCollection();
        curveChart = ChartFactory.createXYLineChart("Brightness", "X axis", "Brightness", dataset, PlotOrientation.VERTICAL, false, false, false);
        curveChart.getTitle().setFont(titleFont);
        XYSplineRenderer renderer = new XYSplineRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesShapesVisible(0, false);
        XYPlot curvePlot = (XYPlot) curveChart.getPlot();
        curvePlot.setBackgroundPaint(Color.WHITE);
        curvePlot.setDomainGridlinePaint(Color.GRAY);
        curvePlot.setRangeGridlinePaint(Color.GRAY);
        curvePlot.setRenderer(renderer);
        NumberAxis curveX = (NumberAxis) curvePlot.getDomainAxis();
        NumberAxis curveY = (NumberAxis) curvePlot.getRangeAxis();
        curveX.setLowerMargin(0);
        curveX.setUpperMargin(0);
        curveX.setTickUnit(new NumberTickUnit(0.2));
        curveX.setTickLabelFont(tickLabelFont);
        curveX.setLabelFont(labelFont);
        curveY.setUpperMargin(0);
        curveY.setLowerMargin(0);
        curveY.setAutoRange(false);
        curveY.setMinorTickCount(10);
        curveY.setMinorTickMarksVisible(false);
        curveY.setTickUnit(new NumberTickUnit(0.2));
        curveY.setTickLabelFont(tickLabelFont);
        curveY.setLabelFont(labelFont);
        slider = new JSlider(0, 500, 250);
        slider.addChangeListener(this);
        slider.setOrientation(JSlider.HORIZONTAL);
        slider.setBackground(Color.WHITE);
        ChartPanel curvePanel = new ChartPanel(curveChart);
        curvePanel.setPreferredSize(new Dimension(300, 300));
        TetrisLayout layout = new TetrisLayout(2, 2);
        layout.setColWeight(0, 100);
        layout.setColWeight(1, 100);
        layout.setRowWeight(0, 100);
        layout.setRowWeight(1, 0);
        setLayout(layout);
        setBackground(Color.WHITE);
        Cell cell = new Cell();
        cell.setFill(Cell.PROPORTIONAL);
        add(picPanel, cell);
        add(curvePanel, cell);
        cell.setFill(Cell.BOTH);
        cell.setCols(2);
        add(slider, cell);
        this.picChart.setNotify(true);
        Action exportPictureAction = new ExportAction("Export picture", "FrequencyPicture", picPanel);
        Action exportFunctionAction = new ExportAction("Export function", "FrequencyFunction", curvePanel);
        Action loadImageAction = new OpenImageAction();
        Action saveImageAction = new SaveImageAction();
        actions = new HashMap<String, Action>();
        actions.put("EXPORT_PICTURE", exportPictureAction);
        actions.put("EXPORT_FUNCTION", exportFunctionAction);
        actions.put("LOAD", loadImageAction);
        actions.put("SAVE", saveImageAction);
    }

    public void stateChanged(ChangeEvent e) {
        crosshair.setValue(slider.getValue() / 500d);
        int xIndex = slider.getValue();
        XYDataset d = HeatMapUtilities.extractRowFromHeatMapDataset(dataset, xIndex, "Y2");
        curveChart.getXYPlot().setDataset(d);
    }

    /**
     * See if the axis ranges have changed in the main chart and, if so,
     * update the subchart.
     * @param event
     */
    public void chartChanged(ChartChangeEvent event) {
        XYPlot picPlot = (XYPlot) picChart.getPlot();
        if (!picPlot.getDomainAxis().getRange().equals(lastXRange)) {
            lastXRange = picPlot.getDomainAxis().getRange();
            XYPlot curvePlot = (XYPlot) curveChart.getPlot();
            curvePlot.getDomainAxis().setRange(lastXRange);
        }
        stateChanged(new ChangeEvent(this));
    }

    /** {@inheritDoc} */
    public Map<String, Action> getActions() {
        return actions;
    }

    private static HeatMapDataset createMapDataset(int selection) {
        DefaultHeatMapDataset d = new DefaultHeatMapDataset(501, 501, 0, 1, 0, 1);
        switch(selection) {
            case 0:
                for (int i = 0; i < 501; i++) {
                    for (int j = 0; j < 501; j++) {
                        double r = Math.sqrt(Math.pow(250.5 - i, 2) + Math.pow(250.5 - j, 2));
                        double z = 0.5 + 0.1d * Math.cos((double) i * Math.PI * 16d / 500d) + 0.4d * Math.cos((double) r * Math.PI * 4d / 500d);
                        d.setZValue(i, j, z);
                    }
                }
                break;
            case 1:
                for (int i = 0; i < 501; i++) {
                    for (int j = 0; j < 501; j++) {
                        double r = Math.sqrt(Math.pow(250.5 - i, 2) + Math.pow(250.5 - j, 2));
                        double z = 0.4 + 0.4d * Math.cos((double) r * Math.PI * 1.2d / 500d);
                        d.setZValue(i, j, z);
                    }
                }
                break;
            case 2:
                for (int i = 0; i < 501; i++) {
                    for (int j = 0; j < 501; j++) {
                        double r = Math.sqrt(Math.pow(250.5 - i, 2) + Math.pow(250.5 - j, 2));
                        double z = 0.4 + 0.1d * Math.cos((double) i * Math.PI * 16d / 500d);
                        d.setZValue(i, j, z);
                    }
                }
                break;
            case 3:
                for (int i = 0; i < 501; i++) {
                    for (int j = 0; j < 501; j++) {
                        double r = Math.sqrt(Math.pow(250.5 - i, 2) + Math.pow(250.5 - j, 2));
                        double z = 0.4 + 0.1d * Math.cos((double) i * Math.PI * 16d / 500d) + 0.4d * Math.cos((double) r * Math.PI * 1.2d / 500d);
                        d.setZValue(i, j, z);
                    }
                }
                break;
        }
        return d;
    }

    private class OpenImageAction extends AbstractAction {

        /** Serial number. */
        private static final long serialVersionUID = -8611356558051523914L;

        public OpenImageAction() {
            putValue(Action.NAME, "Load image");
        }

        public void actionPerformed(ActionEvent event) {
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(FrequencyPanel.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                DefaultHeatMapDataset d = new DefaultHeatMapDataset(501, 501, 0, 1, 0, 1);
                BufferedImage img = null;
                try {
                    img = ImageIO.read(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                int width = img.getWidth();
                int height = img.getHeight();
                int minWidth = Math.min(width, 501);
                int minHeight = Math.min(height, 501);
                for (int x = 0; x < minWidth; x++) {
                    for (int y = 0; y < minHeight; y++) {
                        int value = img.getRGB(x, y);
                        int b = value & 0x000000FF;
                        int g = value >> 8 & 0x000000FF;
                        int r = value >> 16 & 0x000000FF;
                        double z = (0.299d * r + 0.587d * g + 0.114d * b) / 255d;
                        d.setZValue(x, 500 - y, z);
                    }
                }
                dataset = d;
                PaintScale ps = new GrayPaintScale(0, 1.0, 255);
                BufferedImage image = HeatMapUtilities.createHeatMapImage(dataset, ps);
                XYDataImageAnnotation ann = new XYDataImageAnnotation(image, 0, 0, 1, 1, true);
                XYPlot picPlot = (XYPlot) picChart.getPlot();
                picPlot.getRenderer().removeAnnotations();
                picPlot.getRenderer().addAnnotation(ann, Layer.BACKGROUND);
            }
        }
    }

    private class SaveImageAction extends AbstractAction {

        /** Serial number. */
        private static final long serialVersionUID = -7729683790853538507L;

        public SaveImageAction() {
            putValue(Action.NAME, "Save image");
        }

        public void actionPerformed(ActionEvent event) {
            PaintScale ps = new GrayPaintScale(0, 1.0, 255);
            BufferedImage image = HeatMapUtilities.createHeatMapImage(dataset, ps);
            try {
                File outputfile = new File("c:/Saved.png");
                ImageIO.write(image, "png", outputfile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
