package org.zkforge.timeplot.geometry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DefaultTimeGeometry implements TimeGeometry {

    private static int count = 0;

    private int timeGeometryId = count++;

    private String axisColor = "#606060";

    private String gridColor = "#000000";

    private float gridLineWidth = (float) 0.5;

    private String axisLabelsPlacement = "bottom";

    private int gridStep = 100;

    private int gridStepRange = 20;

    private Date min;

    private Date max;

    private String timeValuePosition = "bottom";

    private Map _attrs = new HashMap(7);

    public String getAxisColor() {
        return axisColor;
    }

    public void setAxisColor(String axisColor) {
        this.axisColor = axisColor;
    }

    public String getAxisLabelsPlacement() {
        return axisLabelsPlacement;
    }

    public void setAxisLabelsPlacement(String axisLabelsPlacement) {
        this.axisLabelsPlacement = axisLabelsPlacement;
    }

    public String getGridColor() {
        return gridColor;
    }

    public void setGridColor(String gridColor) {
        this.gridColor = gridColor;
    }

    public float getGridLineWidth() {
        return gridLineWidth;
    }

    public void setGridLineWidth(float gridLineWidth) {
        this.gridLineWidth = gridLineWidth;
    }

    public int getGridStep() {
        return gridStep;
    }

    public void setGridStep(int gridStep) {
        this.gridStep = gridStep;
    }

    public int getGridStepRange() {
        return gridStepRange;
    }

    public void setGridStepRange(int gridStepRange) {
        this.gridStepRange = gridStepRange;
    }

    public Date getMax() {
        return max;
    }

    public void setMax(Date max) {
        this.max = max;
    }

    public Date getMin() {
        return min;
    }

    public void setMin(Date min) {
        this.min = min;
    }

    public String getTimeValuePosition() {
        return timeValuePosition;
    }

    public void setTimeValuePosition(String timeValuePosition) {
        this.timeValuePosition = timeValuePosition;
    }

    public String getTimeGeometryId() {
        return "timeGeometry" + timeGeometryId;
    }

    public String getFormat(String timeRange) {
        return String.valueOf(_attrs.get(timeRange));
    }

    public Map getFormats() {
        return _attrs;
    }

    public boolean hasFormat(String timeRange) {
        return _attrs.containsKey(timeRange);
    }

    public String removeFormat(String timeRange) {
        return String.valueOf(_attrs.remove(timeRange));
    }

    public String setFormat(String timeRange, String format) {
        return String.valueOf(_attrs.put(timeRange, format));
    }
}
