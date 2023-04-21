package trstudio.beansmetric.metric;

import trstudio.beansmetric.core.LevelSource;
import trstudio.beansmetric.core.MetricDescriptor;
import trstudio.beansmetric.core.MetricType;

/**
 * Instability.
 *
 * @author Sebastien Villemain
 */
public class Instability extends MetricDescriptor {

    public void setDefaults() {
        super.setDefaults();
        id = MetricType.RMI;
        name = "Instability";
        level = LevelSource.PACKAGE_FRAGMENT;
        propagateSum = false;
        setRequiredMetricIds(MetricType.EFFERENT_COUPLING, MetricType.AFFERENT_COUPLING);
    }
}
