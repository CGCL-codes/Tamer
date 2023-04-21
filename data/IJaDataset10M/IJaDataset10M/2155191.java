package info.monitorenter.gui.chart.axis.scalepolicy;

import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.IAxisScalePolicy;
import info.monitorenter.gui.chart.LabeledValue;
import info.monitorenter.gui.chart.axis.AAxisTransformation;
import info.monitorenter.util.Range;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

/**
 * Very basic implementation that has to be used with implementation of
 * {@link AAxisTransformation} to have the scale transformed.
 * <p>
 * 
 * @author Bill Schoolfield (contributor)
 * 
 * @author Achim Westermann (modification)
 * 
 */
public class AxisScalePolicyTransformation implements IAxisScalePolicy {

    /**
   * Uses the transformation function callbacks ({@link AAxisTransformation#transform(double)}, {@link AAxisTransformation#untransform(double)}) 
   * of the {@link AAxisTransformation} this instance may be used with to have the scale transformed accordingly. 
   * <p>
   */
    @SuppressWarnings("unchecked")
    public List<LabeledValue> getScaleValues(final Graphics2D g2d, final IAxis<?> axis) {
        AAxisTransformation<AxisScalePolicyTransformation> axisTransformation = (AAxisTransformation<AxisScalePolicyTransformation>) axis;
        final List<LabeledValue> collect = new LinkedList<LabeledValue>();
        LabeledValue label;
        final Range domain = axis.getRange();
        double min = domain.getMin();
        double max = domain.getMax();
        min = axisTransformation.transform(min);
        max = axisTransformation.transform(max);
        double range = max - min;
        double exp = 0.;
        double val = axisTransformation.untransform(0);
        while (val <= axis.getMax()) {
            if (val >= axis.getMin()) {
                label = new LabeledValue();
                label.setValue(val);
                label.setLabel(axis.getFormatter().format(label.getValue()));
                label.setMajorTick(true);
                label.setValue((axisTransformation.transform(label.getValue()) - min) / range);
                collect.add(label);
            }
            exp += 1.;
            val = axisTransformation.untransform(exp);
        }
        return collect;
    }

    public void initPaintIteration(IAxis<?> axis) {
    }
}
