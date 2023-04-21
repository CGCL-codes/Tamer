package org.chartsy.htdcp;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import org.chartsy.main.ChartFrame;
import org.chartsy.main.chart.Indicator;
import org.chartsy.main.data.DataItem;
import org.chartsy.main.data.Dataset;
import org.chartsy.main.utils.DefaultPainter;
import org.chartsy.main.utils.Range;
import org.chartsy.main.utils.SerialVersion;
import org.chartsy.talib.TaLibInit;
import org.chartsy.talib.TaLibUtilities;
import org.openide.nodes.AbstractNode;

/**
 * The Hilbert Transform - Dominant Cycle Period
 *
 * @author joshua.taylor
 */
public class HilbertTransformDCP extends Indicator {

    private static final long serialVersionUID = SerialVersion.APPVERSION;

    public static final String FULL_NAME = "Hilbert Transform DC Period (HT-DC Period)";

    public static final String HASHKEY = "htdcperiod";

    private IndicatorProperties properties;

    private int lookback;

    private double[] output;

    private transient MInteger outBegIdx;

    private transient MInteger outNbElement;

    private transient Core core;

    private Dataset calculatedDataset;

    public HilbertTransformDCP() {
        super();
        properties = new IndicatorProperties();
    }

    @Override
    public String getName() {
        return FULL_NAME;
    }

    @Override
    public String getLabel() {
        return properties.getLabel();
    }

    @Override
    public String getPaintedLabel(ChartFrame cf) {
        return "";
    }

    @Override
    public Indicator newInstance() {
        return new HilbertTransformDCP();
    }

    @Override
    public boolean hasZeroLine() {
        return false;
    }

    @Override
    public boolean getZeroLineVisibility() {
        return false;
    }

    @Override
    public Color getZeroLineColor() {
        return null;
    }

    @Override
    public Stroke getZeroLineStroke() {
        return null;
    }

    @Override
    public boolean hasDelimiters() {
        return false;
    }

    @Override
    public boolean getDelimitersVisibility() {
        return false;
    }

    @Override
    public double[] getDelimitersValues() {
        return new double[] {};
    }

    @Override
    public Color getDelimitersColor() {
        return null;
    }

    @Override
    public Stroke getDelimitersStroke() {
        return null;
    }

    @Override
    public Color[] getColors() {
        return new Color[] { properties.getColor() };
    }

    @Override
    public boolean getMarkerVisibility() {
        return properties.getMarker();
    }

    @Override
    public AbstractNode getNode() {
        return new IndicatorNode(properties);
    }

    @Override
    public LinkedHashMap getHTML(ChartFrame cf, int i) {
        LinkedHashMap ht = new LinkedHashMap();
        DecimalFormat df = new DecimalFormat("#,##0.00");
        double[] values = getValues(cf, i);
        String[] labels = { "Hilbert Transform DC Period:" };
        ht.put(getLabel(), " ");
        if (values.length > 0) {
            Color[] colors = getColors();
            for (int j = 0; j < values.length; j++) {
                ht.put(getFontHTML(colors[j], labels[j]), getFontHTML(colors[j], df.format(values[j])));
            }
        }
        return ht;
    }

    @Override
    public Range getRange(ChartFrame cf) {
        Range range = super.getRange(cf);
        return range;
    }

    @Override
    public void paint(Graphics2D g, ChartFrame cf, Rectangle bounds) {
        Dataset dataset = visibleDataset(cf, HASHKEY);
        if (dataset != null) {
            if (maximized) {
                Range range = getRange(cf);
                DefaultPainter.line(g, cf, range, bounds, dataset, properties.getColor(), properties.getStroke());
            }
        }
    }

    @Override
    public double[] getValues(ChartFrame cf) {
        Dataset d = visibleDataset(cf, HASHKEY);
        if (d != null) return new double[] { d.getLastClose() };
        return new double[] {};
    }

    @Override
    public double[] getValues(ChartFrame cf, int i) {
        Dataset d = visibleDataset(cf, HASHKEY);
        if (d != null) return new double[] { d.getCloseAt(i) };
        return new double[] {};
    }

    @Override
    public void calculate() {
        Dataset initial = getDataset();
        int count = 0;
        if (initial != null && !initial.isEmpty()) count = initial.getItemsCount();
        output = new double[count];
        outBegIdx = new MInteger();
        outNbElement = new MInteger();
        core = TaLibInit.getCore();
        lookback = core.htDcPeriodLookback();
        core.htDcPeriod(0, count - 1, initial.getCloseValues(), outBegIdx, outNbElement, output);
        output = TaLibUtilities.fixOutputArray(output, lookback);
        calculatedDataset = Dataset.EMPTY(initial.getItemsCount());
        for (int i = 0; i < output.length; i++) calculatedDataset.setDataItem(i, new DataItem(initial.getTimeAt(i), output[i]));
        addDataset(HASHKEY, calculatedDataset);
    }
}
