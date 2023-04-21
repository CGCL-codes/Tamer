package net.community.apps.common.test.chart;

import java.util.Map;
import net.community.chest.jfree.jfreechart.plot.thermometer.ThermometerSubRangeValue;
import net.community.chest.util.map.MapEntryImpl;

/**
 * <P>Copyright 2010 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jun 22, 2010 11:34:16 AM
 */
public class DiaThermometerPlot extends EntryThermometerPlot {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7472762629444281845L;

    public static final Map.Entry<Number, Number> RANGE = new MapEntryImpl<Number, Number>(Integer.valueOf(40), Integer.valueOf(120));

    public static final Map.Entry<Number, Number> NORMAL_RANGE = new MapEntryImpl<Number, Number>(Integer.valueOf(60), Integer.valueOf(90)), WARNING_RANGE = new MapEntryImpl<Number, Number>(Integer.valueOf(NORMAL_RANGE.getValue().intValue() + 1), Integer.valueOf(100)), CRITICAL_RANGE = new MapEntryImpl<Number, Number>(Integer.valueOf(WARNING_RANGE.getValue().intValue() + 1), RANGE.getValue());

    public DiaThermometerPlot() {
        super(TimeSeriesTypeCase.DIA);
    }

    @Override
    public final Map.Entry<Number, Number> getRangeBoundaries() {
        return RANGE;
    }

    @Override
    public Map.Entry<Number, Number> getSubRangeBoundaries(ThermometerSubRangeValue subType) {
        if (null == subType) return null;
        switch(subType) {
            case CRITICAL:
                return CRITICAL_RANGE;
            case WARNING:
                return WARNING_RANGE;
            case NORMAL:
                return NORMAL_RANGE;
            default:
                throw new IllegalArgumentException("getSubRangeBoundaries(" + subType + ") unknown range");
        }
    }
}
