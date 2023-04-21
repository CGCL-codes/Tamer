package edu.upmc.opi.caBIG.caTIES.uuid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Expression;
import edu.upmc.opi.caBIG.caTIES.database.domain.IdentifiedPathologyReport;
import edu.upmc.opi.caBIG.caTIES.database.domain.IdentifiedPatient;
import edu.upmc.opi.caBIG.caTIES.database.domain.IdentifiedSection;
import edu.upmc.opi.caBIG.caTIES.database.domain.impl.IdentifiedPatientImpl;
import edu.upmc.opi.caBIG.common.CaBIG_UUIdGenerator;

public class UUIDPipeline {

    /**
     * Field logger.
     */
    private static Logger logger = Logger.getLogger(UUIDPipeline.class);

    /**
     * The session pvt.
     */
    private Session sessionPvt;

    /**
     * The Constant BATCH_SIZE.
     */
    private static final int BATCH_SIZE = 500;

    /**
     * The Constructor.
     */
    public UUIDPipeline() {
        logger.debug("******** Initiating Private Data Store Session ********");
        Configuration cfgPvt = new Configuration();
        SessionFactory sessionFactoryPvt = cfgPvt.configure("edu/upmc/opi/caBIG/caTIES/uuid/config/UUIDPipelinePvt.cfg.xml").buildSessionFactory();
        sessionPvt = sessionFactoryPvt.openSession();
        List ips = sessionPvt.createCriteria(IdentifiedPatientImpl.class).add(Expression.isNull("uuid")).setMaxResults(BATCH_SIZE).list();
        while (true) {
            for (int x = 0; x < ips.size(); x++) {
                IdentifiedPatient ip = (IdentifiedPatient) ips.get(x);
                if (ip.getUuid() == null) {
                    ip.setUuid(CaBIG_UUIdGenerator.getUUID());
                }
                Collection iprs = (Collection) ip.getPathologyReportCollection();
                for (Iterator i = iprs.iterator(); i.hasNext(); ) {
                    IdentifiedPathologyReport ipr = (IdentifiedPathologyReport) i.next();
                    if (ipr.getUuid() == null) {
                        ipr.setUuid(CaBIG_UUIdGenerator.getUUID());
                    }
                    Collection iss = (Collection) ipr.getIdentifiedSectionCollection();
                    for (Iterator itr = iss.iterator(); itr.hasNext(); ) {
                        IdentifiedSection is = (IdentifiedSection) itr.next();
                        if (is.getUuid() == null) {
                            is.setUuid(CaBIG_UUIdGenerator.getUUID());
                        }
                    }
                }
                Transaction txPvt = sessionPvt.beginTransaction();
                sessionPvt.saveOrUpdate(ip);
                sessionPvt.flush();
                txPvt.commit();
            }
            logger.debug("**************Fetching Next Batch***************");
            ips = sessionPvt.createCriteria(IdentifiedPatientImpl.class).add(Expression.isNull("uuid")).setMaxResults(BATCH_SIZE).list();
            logger.debug("************************************************");
        }
    }

    /**
     * The main method.
     * 
     * @param args the args
     */
    public static void main(String args[]) {
        new UUIDPipeline();
    }
}
