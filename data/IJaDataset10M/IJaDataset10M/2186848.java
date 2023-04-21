package trstudio.beansmetric.metric;

import trstudio.beansmetric.core.LevelSource;
import trstudio.beansmetric.core.MetricDescriptor;
import trstudio.beansmetric.core.MetricType;

/**
 * McCabe cyclomatic complexity.
 *
 * @author Sebastien Villemain
 */
public class McCabeCyclomaticComplexity extends MetricDescriptor {

    public void setDefaults() {
        super.setDefaults();
        id = MetricType.MCCABE;
        name = "McCabe cyclomatic complexity";
        level = LevelSource.METHOD;
        propagateSum = false;
        defaultMax = 10D;
        defaultHint = "Use extract-method to split the method up.";
    }
}
