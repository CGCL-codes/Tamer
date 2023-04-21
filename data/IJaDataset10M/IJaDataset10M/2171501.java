package trstudio.beansmetric.metric;

import trstudio.beansmetric.core.LevelSource;
import trstudio.beansmetric.core.MetricDescriptor;
import trstudio.beansmetric.core.MetricType;

/**
 * Number of static methods.
 *
 * @author Sebastien Villemain
 */
public class NumberOfStaticMethods extends MetricDescriptor {

    public void setDefaults() {
        super.setDefaults();
        id = MetricType.NUMBER_OF_STATIC_METHODS;
        name = "Number of static methods";
        level = LevelSource.CLASS_TYPE;
    }
}
