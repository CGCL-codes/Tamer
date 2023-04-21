package googlechartwrapper.data;

import googlechartwrapper.label.ChartLegend;
import googlechartwrapper.style.LineStyle;
import googlechartwrapper.util.Pair;
import java.awt.Color;
import java.util.Collections;
import java.util.List;

/**
 * @author steffan
 * 
 */
public class XYLineChartData {

    private Pair<List<Float>, List<Float>> dataSet = null;

    private Color color = null;

    private ChartLegend legend = null;

    private LineStyle style = null;

    /**
	 * 
	 * @param builder
	 * 
	 * @throws IllegalArgumentException
	 *             if builder is {@code null}
	 */
    public XYLineChartData(XYLineChartDataBuilder builder) {
        if (builder == null) throw new IllegalArgumentException("builder can not be null");
        dataSet = builder.dataSet;
        color = builder.color;
        legend = builder.legend;
        style = builder.style;
    }

    /**
	 * 
	 * @author steffan
	 * 
	 */
    public static class XYLineChartDataBuilder {

        private Pair<List<Float>, List<Float>> dataSet = null;

        private Color color = null;

        private ChartLegend legend = null;

        private LineStyle style = null;

        /**
		 * Constructs a new {@link XYLineChartData}.
		 * 
		 * @param dataSet
		 *            the first list can be empty, to space the data points
		 *            evenly along the x-axis
		 * 
		 * @throws IllegalArgumentException
		 *             if dataSet or member is {@code null}
		 */
        public XYLineChartDataBuilder(Pair<List<Float>, List<Float>> dataSet) {
            if (dataSet == null) throw new IllegalArgumentException("dataSet can not be null");
            if (dataSet.getFirst() == null) throw new IllegalArgumentException("first list can not be null");
            if (dataSet.getSecond() == null) throw new IllegalArgumentException("second list can not be null");
            List<Float> temp1 = Collections.unmodifiableList(dataSet.getFirst());
            List<Float> temp2 = Collections.unmodifiableList(dataSet.getSecond());
            for (Float current1 : temp1) {
                if (current1 == null) throw new IllegalArgumentException("member can not be null");
            }
            for (Float current2 : temp2) {
                if (current2 == null) throw new IllegalArgumentException("member can not be null");
            }
            this.dataSet = new Pair<List<Float>, List<Float>>(temp1, temp2);
        }

        /**
		 * Adds a color for the line.
		 * 
		 * @param color
		 * @return {@link XYLineChartData}
		 * 
		 * @throws IllegalArgumentException
		 *             if color is {@code null}
		 */
        public XYLineChartDataBuilder color(Color color) {
            if (color == null) throw new IllegalArgumentException("color can not be null");
            this.color = new Color(color.getRGB());
            return this;
        }

        /**
		 * Adds a {@link ChartLegend} for the line.
		 * 
		 * @param label
		 * @return {@link XYLineChartData}
		 * 
		 * @throws IllegalArgumentException
		 *             if label is {@code null}
		 */
        public XYLineChartDataBuilder legend(ChartLegend legend) {
            if (legend == null) throw new IllegalArgumentException("legend can not be null");
            this.legend = legend;
            return this;
        }

        /**
		 * Adds {@link LineStyle} to the line.
		 * 
		 * @param style
		 * @return {@link XYLineChartData}
		 * 
		 * @throws IllegalArgumentException
		 *             if style is {@code null}
		 */
        public XYLineChartDataBuilder style(LineStyle style) {
            if (style == null) throw new IllegalArgumentException("style can not be null");
            this.style = style;
            return this;
        }

        /**
		 * Builds the {@link XYLineChartData} object.
		 * 
		 * @return {@link XYLineChartData}
		 */
        public XYLineChartData build() {
            return new XYLineChartData(this);
        }
    }

    /**
	 * @return the dataSet
	 */
    public Pair<List<Float>, List<Float>> getDataSet() {
        return new Pair<List<Float>, List<Float>>(this.dataSet.getFirst(), this.dataSet.getSecond());
    }

    /**
	 * 
	 * @param dataSet
	 *            the dataSet to set
	 * 
	 * @throws IllegalArgumentException
	 *             if dataSet or member is {@code null}
	 */
    public void setDataSet(Pair<List<Float>, List<Float>> dataSet) {
        if (dataSet == null) throw new IllegalArgumentException("dataSet can not be null");
        if (dataSet.getFirst() == null) throw new IllegalArgumentException("first list can not be null");
        if (dataSet.getSecond() == null) throw new IllegalArgumentException("second list can not be null");
        List<Float> temp1 = Collections.unmodifiableList(dataSet.getFirst());
        List<Float> temp2 = Collections.unmodifiableList(dataSet.getSecond());
        for (Float current1 : temp1) {
            if (current1 == null) throw new IllegalArgumentException("member can not be null");
        }
        for (Float current2 : temp2) {
            if (current2 == null) throw new IllegalArgumentException("member can not be null");
        }
        this.dataSet = new Pair<List<Float>, List<Float>>(temp1, temp2);
    }

    /**
	 * @return the color
	 */
    public Color getColor() {
        return this.color == null ? null : new Color(color.getRGB());
    }

    /**
	 * Sets a new line color.
	 * @param color
	 *            the color to set
	 * 
	 * @throws IllegalArgumentException
	 *             if color is {@code null}
	 */
    public void setColor(Color color) {
        if (color == null) throw new IllegalArgumentException("color can not be null");
        this.color = new Color(color.getRGB());
    }

    /**
	 * @return the legend
	 */
    public ChartLegend getLegend() {
        return legend;
    }

    /**
	 * Sets a new {@link ChartLegend}.
	 * @param legend the legend to set
	 * 
	 * @throws IllegalArgumentException if legend is {@code null}
	 */
    public void setLegend(ChartLegend legend) {
        if (legend == null) throw new IllegalArgumentException("legend can not be null");
        this.legend = legend;
    }

    /**
	 * @return the style
	 */
    public LineStyle getStyle() {
        return style;
    }

    /**
	 * Sets a new {@link LineStyle}.
	 * @param style the style to set
	 * 
	 * @throws IllegalArgumentException if style is {@code null}
	 */
    public void setStyle(LineStyle style) {
        if (style == null) throw new IllegalArgumentException("style can not be null");
        this.style = style;
    }
}
