package org.chartsy.mesa;

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
 * The MESA Adaptive Moving Average by J. Ehlers.
 *
 * @author joshua.taylor
 */
public class MESA extends Indicator {

    private static final long serialVersionUID = SerialVersion.APPVERSION;

    public static final String FULL_NAME = "MESA Adaptive MA";

    public static final String MAMA_LINE = "mamaline";

    public static final String FAMA_LINE = "famaline";

    private IndicatorProperties properties;

    private int lookback;

    private double[] outputFama;

    private double[] outputMama;

    private double fastLimit;

    private double slowLimit;

    private transient MInteger outBegIdx;

    private transient MInteger outNbElement;

    private transient Core core;

    private Dataset calculatedDatasetMAMA;

    private Dataset calculatedDatasetFAMA;

    public MESA() {
        super();
        properties = new IndicatorProperties();
    }

    public String getName() {
        return FULL_NAME;
    }

    public String getLabel() {
        return properties.getLabel();
    }

    public String getPaintedLabel(ChartFrame cf) {
        return getLabel();
    }

    public Indicator newInstance() {
        return new MESA();
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

    public boolean hasDelimiters() {
        return false;
    }

    public boolean getDelimitersVisibility() {
        return false;
    }

    public double[] getDelimitersValues() {
        return new double[] {};
    }

    public Color getDelimitersColor() {
        return null;
    }

    public Stroke getDelimitersStroke() {
        return null;
    }

    public Color[] getColors() {
        return new Color[] { properties.getMamaLineColor(), properties.getFamaLineColor() };
    }

    public boolean getMarkerVisibility() {
        return properties.getMarker();
    }

    public AbstractNode getNode() {
        return new IndicatorNode(properties);
    }

    public LinkedHashMap getHTML(ChartFrame cf, int i) {
        LinkedHashMap ht = new LinkedHashMap();
        DecimalFormat df = new DecimalFormat("#,##0.00");
        double[] values = getValues(cf, i);
        String[] labels = { "MAMA:", "FAMA:" };
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

    public void paint(Graphics2D g, ChartFrame cf, Rectangle bounds) {
        Dataset upTrend = visibleDataset(cf, MAMA_LINE);
        Dataset downTrend = visibleDataset(cf, FAMA_LINE);
        if (upTrend != null && downTrend != null) {
            if (maximized) {
                Range range = getRange(cf);
                DefaultPainter.line(g, cf, range, bounds, upTrend, properties.getMamaLineColor(), properties.getMamaLineStroke());
                DefaultPainter.line(g, cf, range, bounds, downTrend, properties.getFamaLineColor(), properties.getFamaLineStroke());
            }
        }
    }

    public double[] getValues(ChartFrame cf) {
        Dataset mama = visibleDataset(cf, MAMA_LINE);
        Dataset fama = visibleDataset(cf, FAMA_LINE);
        double[] values = new double[2];
        if (mama != null && fama != null) {
            values[0] = mama.getLastClose();
            values[1] = fama.getLastClose();
        } else return new double[] {};
        return values;
    }

    public double[] getValues(ChartFrame cf, int i) {
        Dataset mama = visibleDataset(cf, MAMA_LINE);
        Dataset fama = visibleDataset(cf, FAMA_LINE);
        double[] values = new double[2];
        if (mama != null && fama != null) {
            values[0] = mama.getDataItem(i) != null ? mama.getCloseAt(i) : 0;
            values[1] = fama.getDataItem(i) != null ? fama.getCloseAt(i) : 0;
        } else return new double[] {};
        return values;
    }

    public void calculate() {
        Dataset initial = getDataset();
        int count = 0;
        if (initial != null && !initial.isEmpty()) count = initial.getItemsCount();
        outputFama = new double[count];
        outputMama = new double[count];
        fastLimit = properties.getFastLimit();
        slowLimit = properties.getSlowLimit();
        outBegIdx = new MInteger();
        outNbElement = new MInteger();
        core = TaLibInit.getCore();
        lookback = core.mamaLookback(fastLimit, slowLimit);
        core.mama(0, count - 1, initial.getCloseValues(), fastLimit, slowLimit, outBegIdx, outNbElement, outputMama, outputFama);
        outputFama = TaLibUtilities.fixOutputArray(outputFama, lookback);
        outputMama = TaLibUtilities.fixOutputArray(outputMama, lookback);
        calculatedDatasetMAMA = Dataset.EMPTY(initial.getItemsCount());
        for (int i = 0; i < outputMama.length; i++) calculatedDatasetMAMA.setDataItem(i, new DataItem(initial.getTimeAt(i), outputMama[i]));
        calculatedDatasetFAMA = Dataset.EMPTY(initial.getItemsCount());
        for (int i = 0; i < outputFama.length; i++) calculatedDatasetFAMA.setDataItem(i, new DataItem(initial.getTimeAt(i), outputFama[i]));
        addDataset(MAMA_LINE, calculatedDatasetMAMA);
        addDataset(FAMA_LINE, calculatedDatasetFAMA);
    }
}
