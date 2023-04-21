package trstudio.beansmetric.metric;

import trstudio.beansmetric.core.LevelSource;
import trstudio.beansmetric.core.MetricDescriptor;
import trstudio.beansmetric.core.MetricType;

/**
 * Abstractness.
 * 
 * @author Sebastien Villemain
 */
public class Abstractness extends MetricDescriptor {

    public void setDefaults() {
        super.setDefaults();
        id = MetricType.RMA;
        name = "Abstractness";
        level = LevelSource.PACKAGE_FRAGMENT;
        propagateSum = false;
    }
}
