package gate.util;

import java.io.Serializable;
import gate.FeatureMap;

/** A convenience implemetation of FeatureBearer.
  * @see FeatureBearer
  */
public abstract class AbstractFeatureBearer implements FeatureBearer, Serializable {

    static final long serialVersionUID = -2962478253218344471L;

    /** Get the feature set */
    public FeatureMap getFeatures() {
        return features;
    }

    /** Set the feature set */
    public void setFeatures(FeatureMap features) {
        this.features = features;
    }

    /** The feature set */
    protected FeatureMap features;
}
