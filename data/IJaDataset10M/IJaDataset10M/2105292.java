package asi.elves.script;

import java.util.*;
import asi.elves.*;

/**
 *
 * Select a subset of a TimeSeries
 */
public class Select extends ScheduleSource {

    private TimeSeries m_value;

    private int m_first;

    private int m_last;

    /**
     * Select a subset of a TimeSeries
     *
     * @param value Original time series
     * @param first initial point (inclusive)
     * @param last final point (exclusive)
     */
    public Select(TimeSeries value, int first, int last) {
        super(Arrays.asList(value), MergerType.EXACT);
        m_value = value;
        m_first = first;
        m_last = last;
    }

    @Override
    protected List<Date> datesImpl(Memoizer<Schedule, Date> storageDates) {
        List<Date> valueDates = super.datesImpl(storageDates);
        List<Date> theDates = valueDates.subList(m_first, m_last);
        return theDates;
    }

    public void values(Path path, Memoizer<TimeSeries, Double> storage, Memoizer<Schedule, Date> storageDates) {
        List<Double> values = storage.get(m_value);
        storage.put(this, values.subList(m_first, m_last));
    }
}
