package com.googlecode.gchartjava;

import static com.googlecode.gchartjava.collect.Preconditions.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.googlecode.gchartjava.collect.ImmutableList;
import com.googlecode.gchartjava.collect.Lists;
import com.googlecode.gchartjava.collect.Maps;
import com.googlecode.gchartjava.parameters.AxisTypes;

/**
 * Abstract type that is common to all charts with axes.
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
public abstract class AbstractAxisChart extends AbstractGraphChart {

    /** List of X axis labels. **/
    private final List<AxisLabelsImpl> xAxisLabels = Lists.newLinkedList();

    /** List of Y axis labels. **/
    private final List<AxisLabelsImpl> yAxisLabels = Lists.newLinkedList();

    /** List of top axis labels. **/
    private final List<AxisLabelsImpl> topAxisLabels = Lists.newLinkedList();

    /** List of right axis labels. **/
    private final List<AxisLabelsImpl> rightAxisLabels = Lists.newLinkedList();

    /**
     * Line style for grid. For internal purposes only. Thickness field is
     * ignored.
     */
    private LineStyle gridLineStyle;

    /** X axis step size for the grid. **/
    private double xAxisStepSize;

    /** Y axis step size for the grid. **/
    private double yAxisStepSize;

    /**
     * Abstract Axis chart constructor.
     */
    AbstractAxisChart() {
        super();
    }

    /**
     * Add X axis labels.
     *
     * @param axisLabels
     *            x axis labels. Cannot be null. axisLabels parameter is
     *            defensively copied.
     */
    public final void addXAxisLabels(final AxisLabels axisLabels) {
        checkNotNull(axisLabels, "axisLabel cannnot be null");
        xAxisLabels.add(AxisLabelsFactory.newAxisLabels((AxisLabelsImpl) axisLabels));
    }

    /**
     * Add Y axis information.
     *
     * @param axisLabels
     *            y axis information. Cannot be null. axisLabel parameter is
     *            defensively copied.
     */
    public final void addYAxisLabels(final AxisLabels axisLabels) {
        checkNotNull(axisLabels, "axisLabel cannnot be null");
        yAxisLabels.add(AxisLabelsFactory.newAxisLabels((AxisLabelsImpl) axisLabels));
    }

    /**
     * Add Top axis information.
     *
     * @param axisLabels
     *            top axis information. Cannot be null. axisLabel parameter is
     *            defensively copied.
     */
    public final void addTopAxisLabels(final AxisLabels axisLabels) {
        checkNotNull(axisLabels, "axisLabel cannnot be null");
        topAxisLabels.add(AxisLabelsFactory.newAxisLabels((AxisLabelsImpl) axisLabels));
    }

    /**
     * Add Right axis information.
     *
     * @param axisLabels
     *            right axis information. Cannot be null. axisLabels parameter
     *            is defensively copied.
     */
    public final void addRightAxisLabels(final AxisLabels axisLabels) {
        checkNotNull(axisLabels, "axisLabels cannnot be null");
        rightAxisLabels.add(AxisLabelsFactory.newAxisLabels((AxisLabelsImpl) axisLabels));
    }

    /**
     * Define a grid for this chart.
     *
     * @param xAxisStepSize
     *            x step size. must be > 0.
     * @param yAxisStepSize
     *            y step size. must be > 0.
     * @param lengthOfLineSegment
     *            length of line segment. must be >= 0.
     * @param lengthOfBlankSegment
     *            length of blank segment. must be > 0.
     */
    public final void setGrid(final double xAxisStepSize, final double yAxisStepSize, final int lengthOfLineSegment, final int lengthOfBlankSegment) {
        checkArgument(xAxisStepSize > 0, "xAxisStepSize must be positive: %s", xAxisStepSize);
        checkArgument(yAxisStepSize > 0, "yAxisStepSize must be positive: %s", yAxisStepSize);
        checkArgument(lengthOfLineSegment >= 0, "lengthOfLineSegment must be 0 or positive: %s", lengthOfLineSegment);
        checkArgument(lengthOfBlankSegment >= 0, "lengthOfBlankSegment must be 0 or positive: %s", lengthOfBlankSegment);
        this.xAxisStepSize = xAxisStepSize;
        this.yAxisStepSize = yAxisStepSize;
        gridLineStyle = LineStyle.newLineStyle(1, lengthOfLineSegment, lengthOfBlankSegment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareData() {
        super.prepareData();
        if (gridLineStyle != null) {
            parameterManager.setGridLineParameter(xAxisStepSize, yAxisStepSize, gridLineStyle.getLengthOfLineSegment(), gridLineStyle.getLengthOfBlankSegment());
        }
        if (!xAxisLabels.isEmpty() || !yAxisLabels.isEmpty() || !topAxisLabels.isEmpty() || !rightAxisLabels.isEmpty()) {
            final Map<AxisTypes, List<AxisLabelsImpl>> axisTypeMap = Maps.newEnumMap(AxisTypes.class);
            axisTypeMap.put(AxisTypes.RIGHT_Y_AXIS, rightAxisLabels);
            axisTypeMap.put(AxisTypes.TOP_X_AXIS, topAxisLabels);
            axisTypeMap.put(AxisTypes.LEFT_Y_AXIS, yAxisLabels);
            axisTypeMap.put(AxisTypes.BOTTOM_X_AXIS, xAxisLabels);
            int axisIndex = 0;
            for (Map.Entry<AxisTypes, List<AxisLabelsImpl>> entry : axisTypeMap.entrySet()) {
                for (AxisLabelsImpl axisLabel : entry.getValue()) {
                    parameterManager.addAxisTypes(entry.getKey());
                    if (axisLabel.getPositions().isEmpty() && !axisLabel.getLabels().isEmpty()) {
                        parameterManager.addAxisLabels(axisIndex, axisLabel.getLabels());
                    } else if (!axisLabel.getPositions().isEmpty() && !axisLabel.getLabels().isEmpty()) {
                        parameterManager.addAxisLabels(axisIndex, axisLabel.getLabels());
                        parameterManager.addAxisLabelPosition(axisIndex, axisLabel.getPositions());
                        parameterManager.addAxisRange(axisIndex, Data.MIN_VALUE, Data.MAX_VALUE);
                    } else if (!axisLabel.getPositions().isEmpty() && axisLabel.getLabels().isEmpty()) {
                        final List<Double> sortedpositions = convertToSortedDoubleList(axisLabel.getPositions());
                        parameterManager.addAxisLabelPosition(axisIndex, axisLabel.getPositions());
                        parameterManager.addAxisRange(axisIndex, sortedpositions.get(0), sortedpositions.get(sortedpositions.size() - 1));
                    } else if (axisLabel.getRange() != null) {
                        parameterManager.addAxisRange(axisIndex, axisLabel.getRange().getMin(), axisLabel.getRange().getMax());
                    }
                    if (axisLabel.getAxisStyle() != null) {
                        parameterManager.addAxisStyle(axisIndex, axisLabel.getAxisStyle().getColor(), axisLabel.getAxisStyle().getFontSize(), axisLabel.getAxisStyle().getAlignment());
                    }
                    axisIndex++;
                }
            }
        }
    }

    /**
     * A method to convert a number list to a double list.
     *
     * @param positions
     *            the axis positions list.
     * @return a double list containing positions.
     */
    private static List<Double> convertToSortedDoubleList(final ImmutableList<? extends Number> positions) {
        final List<Double> doubleList = Lists.newLinkedList();
        for (Number number : positions) {
            doubleList.add(number.doubleValue());
        }
        Collections.sort(doubleList);
        return doubleList;
    }
}
