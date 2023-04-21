package uk.ac.city.soi.everestplus.database;

import java.util.ArrayList;
import uk.ac.city.soi.database.EntityManagerInterface;

/**
 * @author Davide Lorenzoli
 * 
 * @date Jun 22, 2011
 */
public interface QoSEntityManagerInterface extends EntityManagerInterface<Double> {

    /**
	 * Returns the last inserted QoS with timestamp less equal to 'timestamp',
	 * <code>null</code> if no QoS is found.
	 * 
	 * @param slaId
	 * @param guaranteedId
	 * @param qosId
	 * @param timestamp
	 * @return
	 */
    public Double selectLastByTimestamp(String slaId, String guaranteedId, String qosId, long timestamp);

    /**
	 * @param slaId
	 * @param guaranteedId
	 * @param qosId
	 * @param numberOfQoS
	 * @return
	 */
    public ArrayList<Double> selectLastN(String slaId, String guaranteedId, String qosId, long numberOfQoS);

    /**
	 * Returns the last inserted QoS according to qosName and specificationIds
	 * 
	 * @param slaId
	 * @param guaranteedId
	 * @param qosId
	 * @return
	 */
    public Double selectLast(String slaId, String guaranteedId, String qosId);

    /**
	 * @param slaId
	 * @param guaranteedId
	 * @param qosId
	 * @param timestamp
	 * @param limit
	 * @return
	 */
    public ArrayList<Double> selectByName(String slaId, String guaranteedId, String qosId, long timestamp, int limit);

    /**
	 * Counts inserted QoS with timestamp less equal to 'timestamp'
	 * 
	 * @param slaId
	 * @param guaranteedId
	 * @param qosId
	 * @param timestamp
	 * @return
	 */
    public int count(String slaId, String guaranteedId, String qosId, long timestamp);

    /**
	 * @param slaId
	 * @param guaranteedId
	 * @param qosId
	 * @return
	 */
    public int count(String slaId, String guaranteedId, String qosId);
}
