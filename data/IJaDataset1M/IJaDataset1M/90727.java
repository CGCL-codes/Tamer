package royere.cwi.view.metrics;

import royere.cwi.util.RoyereException;

/**
* The metric visual which the implementor has attempted to create with a
* metric visual factory is unknown to the metric factory.
*/
public class UnknownMetricVisuals extends RoyereException {

    public UnknownMetricVisuals() {
        super();
    }

    public UnknownMetricVisuals(String s) {
        super(s);
    }
}
