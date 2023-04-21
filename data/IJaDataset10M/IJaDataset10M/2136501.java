package org.jCharts.axisChart.axis.scale;

import org.jCharts.axisChart.axis.scale.ScaleCalculator;

public class AutomaticScaleCalculator extends ScaleCalculator {

    /****************************************************************************************
	 *
	 ***************************************************************************************/
    public AutomaticScaleCalculator() {
    }

    /*********************************************************************************************
	 * Computes the axis increment taking into account the user specified criteria.
	 *
	 ********************************************************************************************/
    public void computeIncrement() {
        double powerOfTen = Math.pow(10.0d, Math.abs((double) super.getRoundingPowerOfTen()));
        double range;
        if ((super.getMinValue() >= 0) || (super.getMaxValue() < 0)) {
            range = Math.max(super.getMaxValue(), -super.getMinValue());
            super.increment = range / (super.getNumberOfScaleItems() - 1);
            this.roundTheIncrement(powerOfTen);
            if (super.getMinValue() >= 0) {
                super.setMinValue(0.0d);
                super.setMaxValue(super.increment * super.getNumberOfScaleItems());
            } else {
                super.setMaxValue(0.0d);
                super.setMinValue(-(super.increment * super.getNumberOfScaleItems()));
            }
        } else {
            super.setMinValue(super.round(super.getMinValue(), powerOfTen));
            if (super.getRoundingPowerOfTen() > 0) {
                super.setMinValue(super.getMinValue() - powerOfTen);
            } else {
                super.setMinValue(super.getMinValue() - (1 / powerOfTen));
            }
            range = super.getMaxValue() - super.getMinValue();
            super.increment = range / (super.getNumberOfScaleItems() - 1);
            super.roundTheIncrement(powerOfTen);
            super.setMaxValue(super.getMinValue() + (this.increment * super.getNumberOfScaleItems()));
        }
    }
}
