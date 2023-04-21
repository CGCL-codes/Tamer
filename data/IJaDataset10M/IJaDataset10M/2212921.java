package de.toolforge.googlechartwrapper.color;

import java.util.ArrayList;
import java.util.List;
import de.toolforge.googlechartwrapper.ChartTypeFeature;
import de.toolforge.googlechartwrapper.Color;
import de.toolforge.googlechartwrapper.util.AppendableFeature;
import de.toolforge.googlechartwrapper.util.IFeatureAppender;

/**
 * Specifies a LinearGradient <a href="http://code.google.com/apis/chart/colors.html#linear_gradient">http://code.google.com/apis/chart/colors.html#linear_gradient</a>
 *  
 * @author steffan
 *
 */
public class LinearGradient implements IFeatureAppender {

    private GradientFillDestination fillDestination;

    private int angle;

    private float startOffset;

    private float endOffset;

    private Color startColor;

    private Color endColor;

    /**
	 * Constructs a lineargradient
	 * 
	 * @param fillDestination
	 * @param angle integer between 0 and 90
	 * @param startColor
	 * @param startOffset float between 0 and 1
	 * @param endColor
	 * @param endOffset float between 0 and 1
	 * 
	 * @throws IllegalArgumentException
	 */
    public LinearGradient(GradientFillDestination fillDestination, int angle, Color startColor, float startOffset, Color endColor, float endOffset) {
        if (fillDestination == null) throw new IllegalArgumentException("fillDestination can not be null");
        if (angle > 90 || angle < 0) throw new IllegalArgumentException("angle out of range");
        if (startColor == null) throw new IllegalArgumentException("startColor can not be null");
        if (endColor == null) throw new IllegalArgumentException("endColor can not be null");
        if (startOffset > 1.0f || startOffset < 0.0f) throw new IllegalArgumentException("startOffset out of range");
        if (endOffset > 1.0f || endOffset < 0.0f) throw new IllegalArgumentException("endOffset out of range");
        this.fillDestination = fillDestination;
        this.angle = angle;
        this.startColor = startColor;
        this.endColor = endColor;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.fillDestination = fillDestination;
    }

    /**
	 * Constructs a lineargradient
	 * 
	 * @param fillDestination
	 * @param angle integer between 0 and 90
	 * @param startColor
	 * @param startOffset float between 0 and 1
	 * @param endColor
	 * @param endOffset float between 0 and 1
	 * 
	 * @throws IllegalArgumentException
	 * @deprecated use
	 * {@link #LinearGradient(GradientFillDestination, int, Color, float, Color, float)}
	 */
    @Deprecated
    public LinearGradient(GradientFillDestination fillDestination, int angle, java.awt.Color awtStartColor, float startOffset, java.awt.Color awtEndColor, float endOffset) {
        if (fillDestination == null) throw new IllegalArgumentException("fillDestination can not be null");
        if (angle > 90 || angle < 0) throw new IllegalArgumentException("angle out of range");
        if (awtStartColor == null) throw new IllegalArgumentException("startColor can not be null");
        if (awtEndColor == null) throw new IllegalArgumentException("endColor can not be null");
        if (startOffset > 1.0f || startOffset < 0.0f) throw new IllegalArgumentException("startOffset out of range");
        if (endOffset > 1.0f || endOffset < 0.0f) throw new IllegalArgumentException("endOffset out of range");
        this.fillDestination = fillDestination;
        this.angle = angle;
        this.startColor = new Color(awtStartColor);
        this.endColor = new Color(awtEndColor);
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.fillDestination = fillDestination;
    }

    /**
	 * Return the fillDestination.
	 * 
	 * @return the fillDestination
	 */
    public GradientFillDestination getFillDestination() {
        return fillDestination;
    }

    /**
	 * @param fillDestination the fillDestination to set
	 * 
	 * @throws IllegalArgumentException if fillDestination <code>null</code>
	 */
    public void setFillDestination(GradientFillDestination fillDestination) {
        if (fillDestination == null) throw new IllegalArgumentException("fillDestination can not be null");
        this.fillDestination = fillDestination;
    }

    /**
	 * Returns the angle.
	 * 
	 * @return the angle
	 */
    public int getAngle() {
        return angle;
    }

    /**
	 * Sets the angle.
	 * 
	 * @param angle the angle to set, integer between 0 and 90
	 * 
	 * @throws IllegalArgumentException if angle out of range
	 */
    public void setAngle(int angle) {
        if (angle > 90 || angle < 0) throw new IllegalArgumentException("angle out of range");
        this.angle = angle;
    }

    /**
	 * Returns the startOffset.
	 * 
	 * @return the startOffset
	 */
    public float getStartOffset() {
        return startOffset;
    }

    /**
	 * Sets the startOffset.
	 * 
	 * @param startOffset the startOffset to set
	 * 
	 * @throws IllegalArgumentException if startOffset <code>null</code>
	 */
    public void setStartOffset(float startOffset) {
        if (startOffset > 1.0f || startOffset < 0.0f) throw new IllegalArgumentException("startOffset out of range");
        this.startOffset = startOffset;
    }

    /**
	 * Returns the endOffset.
	 * 
	 * @return the endOffset
	 */
    public float getEndOffset() {
        return endOffset;
    }

    /**
	 * Sets the endOffset.
	 * 
	 * @param endOffset the endOffset to set float between 0 and 1
	 * 
	 * @throws IllegalArgumentException if endOffset out of range
	 */
    public void setEndOffset(float endOffset) {
        if (endOffset > 1.0f || endOffset < 0.0f) throw new IllegalArgumentException("endOffset out of range");
        this.endOffset = endOffset;
    }

    /**
	 * Returns the startColor.
	 * 
	 * @return the startColor
	 */
    public Color getStartColor() {
        return startColor;
    }

    /**
	 * Sets the startColor
	 * 
	 * @param startColor the startColor to set
	 * 
	 * @throws IllegalArgumentException if startColor <code>null</code>
	 */
    public void setStartColor(Color startColor) {
        if (startColor == null) throw new IllegalArgumentException("startColor can not be null");
        this.startColor = startColor;
    }

    /**
	 * Return the endColor.
	 * 
	 * @return the endColor
	 */
    public Color getEndColor() {
        return endColor;
    }

    /**
	 * Sets the endColor.
	 * 
	 * @param endColor the endColor to set
	 * 
	 * @throws IllegalArgumentException if endColor <code>null</code>
	 */
    public void setEndColor(Color endColor) {
        if (startColor == null) throw new IllegalArgumentException("endColor can not be null");
        this.endColor = endColor;
    }

    public List<AppendableFeature> getAppendableFeatures(List<? extends IFeatureAppender> otherAppenders) {
        StringBuilder builder = new StringBuilder();
        builder.append(this.fillDestination.getDestination());
        builder.append(',');
        builder.append("lg");
        builder.append(',');
        builder.append(this.angle);
        builder.append(',');
        builder.append(startColor.getMatchingColorHexValue());
        builder.append(',');
        builder.append(this.startOffset);
        builder.append(',');
        builder.append(endColor.getMatchingColorHexValue());
        builder.append(',');
        builder.append(this.endOffset);
        List<AppendableFeature> feature = new ArrayList<AppendableFeature>();
        feature.add(new AppendableFeature(builder.toString(), ChartTypeFeature.LinearGradient));
        return feature;
    }

    /**
	  * The API provides background and chartareafill.
     * 
     * @author steffan
     *
     */
    public enum GradientFillDestination {

        /**
    	 * for background fill
    	 */
        Background("bg"), /**
   	  * for chart area fill
   	  */
        ChartArea("c");

        private String destination;

        GradientFillDestination(String destination) {
            this.destination = destination;
        }

        /**
   	 * Return the fillDestination
   	 * 
   	 * @return fillDestination
   	 */
        public String getDestination() {
            return this.destination;
        }
    }
}
