package org.sensorweb.model.swe.observation;

import java.util.*;
import org.sensorweb.model.gml.feature.BoundingShape;
import org.sensorweb.model.gml.feature.FeatureCollection;
import org.sensorweb.model.gml.feature.FeatureMember;
import org.sensorweb.model.gml.feature.FeatureMembers;

/**
 * @author Xingchen Chu
 * @version 0.1
 *
 * <code> ObservationCollectionBase </code>
 */
public class ObservationCollectionBase extends FeatureCollection implements SWEObservation {

    private ObservationMembers observationMembers;

    private Collection observationMember = new ArrayList();

    public ObservationCollectionBase() {
        super();
    }

    /**
	 * @param boundedBy
	 */
    public ObservationCollectionBase(BoundingShape boundedBy) {
        super(boundedBy);
    }

    /**
	 * @return Returns the observationMember.
	 */
    public Collection getObservationMember() {
        return observationMember;
    }

    /**
	 * @param observationMember The observationMember to set.
	 */
    public void setObservationMember(Collection observationMember) {
        this.observationMember = observationMember;
    }

    /**
	 * @return Returns the observationMembers.
	 */
    public ObservationMembers getObservationMembers() {
        return observationMembers;
    }

    /**
	 * @param observationMembers The observationMembers to set.
	 */
    public void setObservationMembers(ObservationMembers observationMembers) {
        this.observationMembers = observationMembers;
    }

    public void addFeatureMember(FeatureMember member) {
    }

    public Collection getFeatureMember() {
        return null;
    }

    public FeatureMembers getFeatureMembers() {
        return null;
    }

    public void removeFeatureMember(FeatureMember member) {
    }

    public void setFeatureMember(Collection featureMember) {
    }

    public void setFeatureMembers(FeatureMembers featureMembers) {
    }
}
