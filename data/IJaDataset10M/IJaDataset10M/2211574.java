package googlechartwrapper.label;

import googlechartwrapper.ChartTypeFeature;
import googlechartwrapper.util.AppendableFeature;
import googlechartwrapper.util.IFeatureAppender;
import java.util.ArrayList;
import java.util.List;

/**
 * Specifies a chart legend. <a
 * href="http://code.google.com/apis/chart/labels.html#chart_legend">
 * http://code.google.com/apis/chart/labels.html#chart_legend</a>
 * 
 * @author steffan
 * 
 */
public class ChartLegend implements IFeatureAppender {

    private List<String> labelList = new ArrayList<String>();

    private ChartLegendPosition chartLegendPosition = ChartLegendPosition.Right_Vertical;

    /**
	 * Add exactly one label to the list.
	 * 
	 * @param label
	 *            the label to add
	 * 
	 * @throws IllegalArgumentException
	 *             if label is {@code null}
	 */
    public ChartLegend(String label) {
        if (label == null) throw new IllegalArgumentException("label can not be null");
        this.labelList.add(label);
    }

    /**
	 * Add exactly one label to the list.
	 * 
	 * @param label
	 *            the label to add
	 * 
	 * @throws IllegalArgumentException
	 *             if label is {@code null}
	 * @throws IllegalArgumentException
	 *             if chartLegendPosition
	 */
    public ChartLegend(String label, ChartLegendPosition chartLegendPosition) {
        if (label == null) throw new IllegalArgumentException("label can not be null");
        if (chartLegendPosition == null) throw new IllegalArgumentException("chartLegendPosition can not be null");
        this.labelList.add(label);
        this.chartLegendPosition = chartLegendPosition;
    }

    /**
	 * Constructs a chart legend.
	 * 
	 * @param label
	 * 
	 * @throws IllegalArgumentException
	 *             if label is {@code null}
	 */
    public ChartLegend(List<String> label) {
        if (label == null) throw new IllegalArgumentException("label can not be null");
        this.labelList = label;
    }

    /**
	 * 
	 * @param labels
	 * @param chartLegendPosition
	 * 
	 * @throws IllegalArgumentException
	 *             if label is {@code null}
	 */
    public ChartLegend(List<String> label, ChartLegendPosition chartLegendPosition) {
        if (label == null) throw new IllegalArgumentException("label can not be null");
        if (chartLegendPosition == null) throw new IllegalArgumentException("chartLegendPosition");
        this.labelList = label;
        this.chartLegendPosition = chartLegendPosition;
    }

    public List<AppendableFeature> getAppendableFeatures(List<? extends IFeatureAppender> otherAppenders) {
        StringBuilder builder = new StringBuilder();
        for (String currentLabel : this.labelList) {
            builder.append(currentLabel);
            builder.append('|');
        }
        if (this.labelList.size() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        List<AppendableFeature> features = new ArrayList<AppendableFeature>();
        features.add(new AppendableFeature(builder.toString(), ChartTypeFeature.ChartDataAppender));
        return features;
    }

    /**
	 * @return the label
	 */
    public List<String> getLabel() {
        return labelList;
    }

    /**
	 * @param label
	 *            the label to set
	 * 
	 * @throws IllegalArgumentException
	 *             if label is <code>null</code>
	 */
    public void setLabel(List<String> label) {
        if (label == null) throw new IllegalArgumentException("label can not be null");
        this.labelList.addAll(label);
    }

    /**
	 * @return the chartLegendPosition
	 */
    public ChartLegendPosition getChartLegendPosition() {
        return chartLegendPosition;
    }

    /**
	 * @param chartLegendPosition
	 *            the chartLegendPosition to set, default is right
	 * 
	 * @throws IllegalArgumentException
	 *             if label is <code>null</code>
	 */
    public void setChartLegendPosition(ChartLegendPosition chartLegendPosition) {
        if (labelList == null) throw new IllegalArgumentException("label can not be null");
        this.chartLegendPosition = chartLegendPosition;
    }

    /**
	 * 
	 * @author steffan
	 * 
	 */
    public enum ChartLegendPosition {

        Bottom_Horizontal("b"), Top_Horizontal("t"), Bottom_Vertival("bv"), Top_Vertival("tv"), Left_Vertival("l"), Right_Vertical("r");

        private String position;

        ChartLegendPosition(String position) {
            this.position = position;
        }

        public String getPosition() {
            return this.position;
        }
    }
}
