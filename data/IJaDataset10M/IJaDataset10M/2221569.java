package googlechartwrapper.style;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import googlechartwrapper.ChartTypeFeature;
import googlechartwrapper.util.AppendableFeature;
import googlechartwrapper.util.IFeatureAppender;

/**
 * Specifies a GridLine <a href=
 * "http://code.google.com/intl/de-DE/apis/chart/styles.html#financial_markers">
 * http
 * ://code.google.com/intl/de-DE/apis/chart/styles.html#financial_markers</a>
 * <br />
 * <h2>Important</h2> <br />
 * You need 4 datasets. <br />
 * <b>Note</b> <br />
 * To draw financial markers, your chart must include at least four data sets. The bottom of the vertical line is drawn on the data set provided in the <data set index>. The bottom of the rectangle is drawn on the next data set. The top of the rectangle is drawn on the data set which has an index of <data set index> + 2, and the top of the vertical line is drawn on the data set which has an index of <data set index> + 3.
 * 
 * 
 * @author steffan
 * 
 */
public class FinancialMarker implements IFeatureAppender {

    private Color color;

    private int dataSetIndex;

    private IDataPoint dataPoint;

    private Priority priority;

    private int size;

    /**
	 * Constructs a financialMarker.
	 * 
	 * @param dataSetIndex value >=0
	 * @param dataPoint {@link DataPoint} can build all dataPoints
	 * @param priority {@link Priority}
	 * @param size size
	 *            the size of the marker in pixels, value >=0
	 * 
	 * @throws IllegalArgumentException
	 */
    public FinancialMarker(int dataSetIndex, IDataPoint dataPoint, int size, Priority priority) {
        if (dataPoint == null) throw new IllegalArgumentException("dataPoint can not be null");
        if (priority == null) throw new IllegalArgumentException("priority can not be null");
        if (size < 0) throw new IllegalArgumentException("size can not be < 0");
        this.dataSetIndex = dataSetIndex;
        this.dataPoint = dataPoint;
        this.priority = priority;
        this.size = size;
    }

    /**
	 * Constructs a financialMarker with color.
	 * 
	 * @param color
	 * @param dataSetIndex value >=0
	 * @param dataPoint {@link DataPoint} can build all dataPoints
	 * @param priority {@link Priority}
	 * @param size size
	 *            the size of the marker in pixels, value >=0
	 * 
	 * @throws IllegalArgumentException
	 */
    public FinancialMarker(Color color, int dataSetIndex, IDataPoint dataPoint, int size, Priority priority) {
        if (color == null) throw new IllegalArgumentException("color can not be null");
        if (dataPoint == null) throw new IllegalArgumentException("dataPoint can not be null");
        if (priority == null) throw new IllegalArgumentException("priority can not be null");
        if (size < 0) throw new IllegalArgumentException("size can not be < 0");
        this.color = color;
        this.dataSetIndex = dataSetIndex;
        this.dataPoint = dataPoint;
        this.priority = priority;
        this.size = size;
    }

    public List<AppendableFeature> getAppendableFeatures(List<? extends IFeatureAppender> otherAppenders) {
        StringBuilder builder = new StringBuilder();
        builder.append("F,");
        if (color != null) {
            builder.append(Integer.toHexString(this.color.getRGB()).substring(2, 8));
        }
        builder.append(',');
        builder.append(this.dataSetIndex);
        builder.append(',');
        builder.append(this.dataPoint.getAppendableString());
        builder.append(',');
        builder.append(this.size);
        builder.append(',');
        builder.append(this.priority.getPriority());
        List<AppendableFeature> feature = new ArrayList<AppendableFeature>();
        feature.add(new AppendableFeature(builder.toString(), ChartTypeFeature.ChartData));
        return feature;
    }

    /**
	 * 
	 * @author steffan
	 * 
	 */
    public enum Priority {

        First(1), Default(0), Last(-1);

        private int priority;

        Priority(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return this.priority;
        }
    }

    /**
	 * Interface for the {@link DataPoint} factory.
	 * 
	 * @author steffan
	 * 
	 */
    public interface IDataPoint {

        /**
		 * The method build the complete datapoint string.
		 * 
		 * @return the complete and appendable string
		 */
        public String getAppendableString();
    }

    /**
	 * Provides a factory for the
	 * {@link FinancialMarker #FinancialMarker(int, IDataPoint, Priority) and {
	 * @link FinancialMarker #FinancialMarker(Color, int, IDataPoint, Priority)}
	 * constructor.
	 * 
	 * 
	 * @author steffan
	 * 
	 */
    public static class DataPoint {

        /**
		 * nobody should ever construct an object of this class
		 */
        private DataPoint() {
        }

        ;

        /**
		 * The financialmarker will be set on every datapoint.
		 * 
		 * @return {@link IDataPoint}
		 */
        public static IDataPoint newDrawEachPoint() {
            return new IDataPoint() {

                public String getAppendableString() {
                    return "-1";
                }
            };
        }

        ;

        /**
		 * The financialmarker will be drawn every nth datapoint
		 * 
		 * @param n
		 *            the nthe position
		 * @return {@link IDataPoint}
		 * 
		 * @throws IllegalArgumentException
		 *             if n is < 0
		 */
        public static IDataPoint newDrawNPoint(final int n) {
            if (n < 0) throw new IllegalArgumentException("n can not < 0");
            return new IDataPoint() {

                public String getAppendableString() {
                    return "-" + String.valueOf(n);
                }
            };
        }

        ;

        /**
		 * Draw at position n the financialmarker. <br />
		 * A decimal number is possible for interpolation.
		 * 
		 * @param n
		 *            value >= 0
		 * 
		 * @return {@link IDataPoint}
		 * 
		 * @throws IllegalArgumentException
		 *             if n < 0;
		 */
        public static IDataPoint newDrawPoint(final float n) {
            if (n < 0) throw new IllegalArgumentException("n can not be < 0");
            return new IDataPoint() {

                public String getAppendableString() {
                    return String.valueOf(n);
                }
            };
        }

        ;

        /**
		 * Draws a financialmarker every nth point were low is the lower bound
		 * and high the higher bound.
		 * 
		 * @param low
		 *            first datapoint in range
		 * @param high
		 *            last datapoint in range
		 * @param n
		 *            every nth datapoint, value >= 0
		 * 
		 * @return {@link IDataPoint}
		 * 
		 * @throws IllegalArgumentException
		 *             if < 0 and/or high < low
		 */
        public static IDataPoint newDrawNPoint(final int low, final int high, final int n) {
            if (high < low) throw new IllegalArgumentException("high can not be < low");
            if (n < 0) throw new IllegalArgumentException("n can not be < 0");
            return new IDataPoint() {

                public String getAppendableString() {
                    StringBuilder builder = new StringBuilder();
                    builder.append(low);
                    builder.append(":");
                    builder.append(high);
                    builder.append(":");
                    builder.append(n);
                    return builder.toString();
                }
            };
        }
    }
}
