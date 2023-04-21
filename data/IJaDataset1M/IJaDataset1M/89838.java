package net.jforerunning.gui.charts;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;

/**
 * A JFreeChart axis for use in charts that display (accumulated) training time.
 * Expects the usual double 1.0 = 1 second data and displays an axis ticked with
 * [h+]h:mm values.
 * 
 * @author jens
 *
 */
public class TrainingTimeAxis extends NumberAxis {

    public TrainingTimeAxis(String title) {
        super(title);
        setNumberFormatOverride(new TimeFromDoubleFormat(false));
        setLabel("hh:mm");
        setAutoRangeIncludesZero(true);
        setMinuteBasedTickUnits();
    }

    private void setMinuteBasedTickUnits() {
        TickUnits units = new TickUnits();
        units.add(new NumberTickUnit(60));
        units.add(new NumberTickUnit(120));
        units.add(new NumberTickUnit(300));
        units.add(new NumberTickUnit(600));
        units.add(new NumberTickUnit(1200));
        units.add(new NumberTickUnit(1800));
        units.add(new NumberTickUnit(3600));
        units.add(new NumberTickUnit(9000));
        units.add(new NumberTickUnit(18000));
        units.add(new NumberTickUnit(36000));
        units.add(new NumberTickUnit(72000));
        units.add(new NumberTickUnit(108000));
        units.add(new NumberTickUnit(180000));
        units.add(new NumberTickUnit(360000));
        setStandardTickUnits(units);
    }
}
