package trstudio.beansmetric.propagator;

import java.util.List;
import trstudio.beansmetric.core.LevelSource;
import trstudio.beansmetric.core.Max;
import trstudio.beansmetric.core.MetricType;
import trstudio.beansmetric.core.metricsource.MetricSource;

/**
 * Propage les maximums.
 *
 * @author Sebastien Villemain
 * @author Frank Sauer
 */
public class MaxMax extends Propagator {

    public MaxMax(MetricType id, LevelSource per) {
        super(id, per, id);
    }

    public void compute(MetricSource source) {
        List<Max> maxes = source.getMaximaFromChildren(getId(), getScope());
        Max max = Max.createFromMetrics(getId(), getScope(), maxes);
        if (max != null) {
            source.setMaximum(max);
        }
    }

    public Propagator createNextLevel() {
        return this;
    }

    public String toString() {
        return "MaxMax(" + getId() + "," + getScope().toString() + ")";
    }
}
