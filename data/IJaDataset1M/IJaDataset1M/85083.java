package edu.unc.med.lccc.tcgasra.datalogic;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import edu.unc.med.lccc.tcgasra.hibernateobj.HibernateSessionFactory;
import edu.unc.med.lccc.tcgasra.hibernateobj.SampleAttribute;

public class SampleAttibuteHibDAO {

    /**
	 * get sampleAttribute by sampleid
	 * 
	 * @param SampleId
	 * @return a sampleAttribute or null
	 */
    public List<SampleAttribute> getSampleAttributeBySampleId(Integer sampleID) {
        Session session = null;
        Transaction tx = null;
        session = HibernateSessionFactory.getSession();
        tx = session.beginTransaction();
        List sampleAttribute = session.createSQLQuery("select * from sample_attribute as s where s.sample_id=" + sampleID).addEntity("s", SampleAttribute.class).list();
        return sampleAttribute;
    }
}
