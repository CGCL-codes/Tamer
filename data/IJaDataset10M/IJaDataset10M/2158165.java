package org.sensorweb.model.swe.observation;

/**
 * @author Xingchen Chu
 * @version 0.1
 *
 * <code> ObservationCollection </code>
 */
public class ObservationCollection extends ObservationCollectionType {

    public ObservationCollection() {
        super();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml version='1.0'>\n");
        sb.append("<ows:ObservationCollection id='" + getId() + "'>\n");
        sb.append(getBoundedBy() + "\n");
        sb.append(getTimeStamp() + "\n");
        sb.append(getObservationMembers() + "\n");
        sb.append("</ows:ObservationCollection>");
        return sb.toString();
    }
}
