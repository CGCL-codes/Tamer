package repast.simphony.data.logging.gather.aggregate;

import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class CountMapping<T> extends AbstractStatsAggregateMapping<T> {

    public static final String TITLE = "Count";

    public CountMapping() {
        super(TITLE, null);
    }

    public Double getColumnValue(Iterable<T> valueSource) {
        if (valueSource instanceof Collection) {
            return new Double(((Collection<?>) valueSource).size());
        }
        double i = 0;
        Iterator iter = valueSource.iterator();
        while (iter.hasNext()) {
            iter.next();
            i++;
        }
        return i;
    }

    @Override
    protected Double getStatValue(DescriptiveStatistics stats, Iterable<T> valueSource) {
        throw new UnsupportedOperationException("Operation not supported");
    }
}
