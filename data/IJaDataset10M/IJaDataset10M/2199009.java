package edu.unc.med.lccc.tcgasra.datalogic;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.unc.med.lccc.tcgasra.hibernateobj.HibernateSessionFactory;
import edu.unc.med.lccc.tcgasra.hibernateobj.LaneAttribute;

public class LaneAttributeHibDAO {

    /**
	 * get LaneAttribute by laneId
	 * 
	 * @param laneId
	 * @return a sampleAttribute or null
	 */
    public List<LaneAttribute> getLaneAttributeByLaneId(Integer laneID) {
        Session session = null;
        Transaction tx = null;
        session = HibernateSessionFactory.getSession();
        tx = session.beginTransaction();
        List laneAttribute = session.createSQLQuery("select * from lane_attribute as la where la.lane_id=" + laneID).addEntity("la", LaneAttribute.class).list();
        return laneAttribute;
    }
}
