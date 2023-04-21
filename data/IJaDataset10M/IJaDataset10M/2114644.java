package nts.node;

import nts.io.Log;

public abstract class DiscardableNode extends BaseNode {

    public final boolean discardable() {
        return true;
    }

    public FontMetric addShortlyOn(Log log, FontMetric metric) {
        return metric;
    }
}
