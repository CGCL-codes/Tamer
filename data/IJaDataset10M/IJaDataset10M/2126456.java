package de.bugbusters.cdoptimizer.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import de.bugbusters.binpacking.model.Bin;
import de.bugbusters.binpacking.model.BinPackingResult;
import de.bugbusters.binpacking.model.Element;
import de.bugbusters.binpacking.model.LongValueSize;
import de.bugbusters.cdoptimizer.binpacking.VolumeSize;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import utils.string.ByteFormat;

/**
 * @author Sven Kiesewetter
 */
public class ResultChartPanel extends ChartPanel implements PropertyChangeListener {

    private static final NumberFormat FILE_SIZE_FORMAT = new ByteFormat();

    static {
        FILE_SIZE_FORMAT.setMaximumFractionDigits(2);
    }

    public ResultChartPanel() {
        super(ChartFactory.createStackedBarChart("Result Chart", "CDs", null, new DefaultCategoryDataset(), PlotOrientation.VERTICAL, false, true, false));
        setBorder(BorderFactory.createTitledBorder("Chart"));
        JFreeChart chart = super.getChart();
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis axis = new NumberAxis("fill level");
        axis.setLowerBound(0);
        plot.setRangeAxis(axis);
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{0}", FILE_SIZE_FORMAT));
        renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator("{0}: {2} ({3})", FILE_SIZE_FORMAT));
        renderer.setItemLabelsVisible(true);
        renderer.setPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, Math.toRadians(-90)));
    }

    public void updateChart(BinPackingResult binPackingResult) {
        JFreeChart chart = super.getChart();
        chart.setTitle(binPackingResult.getUsedAlgorithm().getAlgorithmName() + " Bin Packing");
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        axis.setUpperBound(((VolumeSize) binPackingResult.getBinCapacity()).getValue());
        axis.setTickUnit(new NumberTickUnit(((VolumeSize) binPackingResult.getBinCapacity()).getValue() / 14, FILE_SIZE_FORMAT));
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) plot.getDataset();
        dataset.clear();
        List bins = new ArrayList(binPackingResult.getBins());
        Collections.sort(bins, new BinIdComparator());
        for (Iterator iterator = bins.iterator(); iterator.hasNext(); ) {
            Bin bin = (Bin) iterator.next();
            for (Iterator elems = bin.getElements().iterator(); elems.hasNext(); ) {
                Element element = (Element) elems.next();
                dataset.addValue(((LongValueSize) element.getSize()).getValue(), element.getId(), bin.getId());
            }
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (Mainframe.PROPERTY_BIN_PACKING_RESULT.equals(evt.getPropertyName())) {
            updateChart((BinPackingResult) evt.getNewValue());
        }
    }
}
