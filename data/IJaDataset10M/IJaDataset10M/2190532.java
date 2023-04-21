package net.community.apps.common.test.chart;

import java.util.Map;
import net.community.chest.jfree.jfreechart.plot.thermometer.ThermometerSubRangeValue;
import org.jfree.chart.plot.ThermometerPlot;

/**
 * <P>Copyright 2010 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jun 22, 2010 10:08:55 AM
 */
public abstract class EntryThermometerPlot extends ThermometerPlot {

    /**
	 * 
	 */
    private static final long serialVersionUID = -265749417541529907L;

    private final TimeSeriesTypeCase _tsType;

    public final TimeSeriesTypeCase getSeriesType() {
        return _tsType;
    }

    public abstract Map.Entry<Number, Number> getRangeBoundaries();

    public abstract Map.Entry<Number, Number> getSubRangeBoundaries(ThermometerSubRangeValue subType);

    protected EntryThermometerPlot(TimeSeriesTypeCase tsType) throws IllegalArgumentException {
        if (null == (_tsType = tsType)) throw new IllegalArgumentException("No TS type specified");
        {
            final Map.Entry<Number, Number> r = getRangeBoundaries();
            if (r != null) setRange(r.getKey().doubleValue(), r.getValue().doubleValue());
        }
        for (final ThermometerSubRangeValue v : ThermometerSubRangeValue.VALUES) {
            final Map.Entry<Number, Number> r = getSubRangeBoundaries(v);
            if (null == r) continue;
            setSubrange(v.getRangeValue(), r.getKey().doubleValue(), r.getValue().doubleValue());
        }
    }

    public static final EntryThermometerPlot createEntryThermometerPlot(final TimeSeriesTypeCase tsType) {
        if (null == tsType) return null;
        switch(tsType) {
            case DIA:
                return new DiaThermometerPlot();
            case SIS:
                return new SisThermometerPlot();
            case HR:
                return new HRThermometerPlot();
            default:
                throw new IllegalArgumentException("createEntryThermometerPlot(" + tsType + ") unknwon type");
        }
    }
}
