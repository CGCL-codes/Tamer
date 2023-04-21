package org.ensembl.datamodel.impl;

import org.ensembl.datamodel.Location;
import org.ensembl.datamodel.SimpleFeature;

public class SimpleFeatureImpl extends BaseFeatureImpl implements SimpleFeature {

    /**
   * Used by the (de)serialization system to determine if the data 
   * in a serialized instance is compatible with this class.
   *
   * It's presence allows for compatible serialized objects to be loaded when
   * the class is compatible with the serialized instance, even if:
   *
   * <ul>
   * <li> the compiler used to compile the "serializing" version of the class
   * differs from the one used to compile the "deserialising" version of the
   * class.</li>
   *
   * <li> the methods of the class changes but the attributes remain the same.</li>
   * </ul>
   *
   * Maintainers must change this value if and only if the new version of
   * this class is not compatible with old versions. e.g. attributes
   * change. See Sun docs for <a
   * href="http://java.sun.com/j2se/1.4.2/docs/guide/serialization/">
   * details. </a>
   *
   */
    private static final long serialVersionUID = 1L;

    public SimpleFeatureImpl() {
        super();
    }

    public SimpleFeatureImpl(long internalID, Location location) {
        super(internalID, location);
    }

    private double score;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        buf.append("{").append(super.toString()).append("}, ");
        buf.append("score=").append(score);
        buf.append(" analysis=");
        buf.append(getAnalysis().toString());
        buf.append("]");
        return buf.toString();
    }
}
