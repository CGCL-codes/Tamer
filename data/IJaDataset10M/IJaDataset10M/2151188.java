package org.kaiec.treemap.color;

import java.awt.Color;

/**
 *
 * @author kai
 */
public abstract class AbstractColorAlgorithm implements ColorAlgorithm {

    private float minimum, maximum, center;

    private Color minimumColor, maximumColor, centerColor;

    public float getCenter() {
        return center;
    }

    public void setCenter(float center) {
        this.center = center;
        constraints();
        setCurrentCenter(this.center);
    }

    public Color getCenterColor() {
        return centerColor;
    }

    public void setCenterColor(Color centerColor) {
        this.centerColor = centerColor;
        constraints();
        setCurrentCenterColor(this.centerColor);
    }

    public float getMaximum() {
        return maximum;
    }

    public void setMaximum(float maximum) {
        this.maximum = maximum;
        constraints();
        setCurrentMaximum(this.maximum);
    }

    public Color getMaximumColor() {
        return maximumColor;
    }

    public void setMaximumColor(Color maximumColor) {
        this.maximumColor = maximumColor;
        constraints();
        setCurrentMaximumColor(this.maximumColor);
    }

    public float getMinimum() {
        return minimum;
    }

    public void setMinimum(float minimum) {
        this.minimum = minimum;
        constraints();
        setCurrentMinimum(this.minimum);
    }

    public Color getMinimumColor() {
        return minimumColor;
    }

    public void setMinimumColor(Color minimumColor) {
        this.minimumColor = minimumColor;
        constraints();
        setCurrentMinimumColor(this.minimumColor);
    }

    public float getCurrentLowerRange() {
        return currentCenter - currentMinimum;
    }

    public float getCurrentRange() {
        return currentMaximum - currentMinimum;
    }

    public float getCurrentUpperRange() {
        return currentMaximum - currentCenter;
    }

    public float getLowerRange() {
        return center - minimum;
    }

    public float getRange() {
        return maximum - minimum;
    }

    public float getUpperRange() {
        return maximum - center;
    }

    protected float currentMinimum;

    protected float currentMaximum;

    protected float currentCenter;

    protected Color currentMinimumColor;

    protected Color currentMaximumColor;

    protected Color currentCenterColor;

    public Color getCurrentCenterColor() {
        return currentCenterColor;
    }

    public void setCurrentCenterColor(Color centerColor) {
        this.currentCenterColor = centerColor;
        constraints();
    }

    public Color getCurrentMaximumColor() {
        return currentMaximumColor;
    }

    public void setCurrentMaximumColor(Color maximumColor) {
        this.currentMaximumColor = maximumColor;
        constraints();
    }

    public Color getCurrentMinimumColor() {
        return currentMinimumColor;
    }

    public void setCurrentMinimumColor(Color minimumColor) {
        this.currentMinimumColor = minimumColor;
        constraints();
    }

    public float getCurrentCenter() {
        return currentCenter;
    }

    public void setCurrentCenter(float center) {
        this.currentCenter = center;
        constraints();
    }

    public float getCurrentMaximum() {
        return currentMaximum;
    }

    public void setCurrentMaximum(float maximum) {
        this.currentMaximum = maximum;
        constraints();
    }

    public float getCurrentMinimum() {
        return currentMinimum;
    }

    public void setCurrentMinimum(float minimum) {
        this.currentMinimum = minimum;
        constraints();
    }

    public void reset() {
        setCurrentCenter(center);
        setCurrentCenterColor(centerColor);
        setCurrentMaximum(maximum);
        setCurrentMaximumColor(maximumColor);
        setCurrentMinimum(minimum);
        setCurrentMinimumColor(minimumColor);
    }

    private void constraints() {
        if (minimum > maximum) minimum = maximum;
        if (maximum < minimum) maximum = minimum;
        if (center > maximum) center = maximum;
        if (center < minimum) center = minimum;
        if (currentMinimum < minimum) currentMinimum = minimum;
        if (currentMaximum > maximum) currentMaximum = maximum;
        if (currentMinimum > currentMaximum) currentMinimum = currentMaximum;
        if (currentMaximum < currentMinimum) currentMaximum = currentMinimum;
        if (currentCenter > currentMaximum) currentCenter = currentMaximum;
        if (currentCenter < currentMinimum) currentCenter = currentMinimum;
    }
}
