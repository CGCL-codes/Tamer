package repast.simphony.data.logging.gather.aggregate;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import repast.simphony.data.logging.gather.DataMapping;

public class SkewnessMapping<T> extends AbstractStatsAggregateMapping<T> {

    public static final String TITLE = "Skewness";

    public SkewnessMapping(DataMapping<Number, T> decorated) {
        super(TITLE, decorated);
    }

    @Override
    protected Double getStatValue(DescriptiveStatistics stats, Iterable valueSource) {
        return stats.getSkewness();
    }
}
