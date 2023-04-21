package uk.ac.city.soi.everestplus.database;

import uk.ac.city.soi.database.PersistentEntity;
import uk.ac.city.soi.everestplus.core.QoS;

/**
 * @author Davide Lorenzoli
 * 
 * @date Jul 19, 2010
 */
public class QoSEntity extends QoS implements PersistentEntity {

    private long entityId;

    /**
	 * @param specificationId
	 * @param qosId
	 * @param value
	 * @param timestamp
	 */
    public QoSEntity(String specificationId, String qosId, double value, long timestamp) {
        super(specificationId, qosId, value, timestamp);
        setStartTime(0);
        setStopTime(0);
    }

    /**
	 * @param specificationId
	 * @param qosId
	 * @param value
	 * @param timestamp
	 * @param startTime
	 * @param stopTime
	 */
    public QoSEntity(String specificationId, String qosId, double value, long timestamp, long startTime, long stopTime) {
        super(specificationId, qosId, value, timestamp);
        setStartTime(startTime);
        setStopTime(stopTime);
    }

    /**
	 * @return the entityId
	 */
    public long getEntityId() {
        return entityId;
    }

    /**
	 * @param entityId the entityId to set
	 */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        String result = new String();
        result += getClass().getName() + "{" + "specificationId=" + getPredictionPolicyId() + "," + "qosId=" + getQosId() + "," + "value=" + getValue() + "," + "timestamp=" + getTimestamp() + "}";
        return result;
    }
}
