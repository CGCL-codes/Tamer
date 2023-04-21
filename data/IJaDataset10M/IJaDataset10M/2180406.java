package dsb.bar.tks.schema.tests;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.apache.log4j.Logger;
import dsb.bar.tks.support.logging.Log4J;

public class EJBQLTests {

    private static EntityManagerFactory entityManagerFactory = null;

    private static final Logger logger = Logger.getLogger(EJBQLTests.class);

    public static void main(String[] args) {
        Log4J.configure();
        logger.info("Initializing JPA subsystem ...");
        entityManagerFactory = Persistence.createEntityManagerFactory("TKS");
        logger.info("Schema validated or generated (see logs).");
        doStuff();
    }

    private static void doStuff() {
        EntityManager em = entityManagerFactory.createEntityManager();
        String ejbQuery = "SELECT sp.id, sp.salesArticlePart, sp.validFrom FROM SalesArticlePrice sp WHERE sp.validFrom <= :date";
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 30);
        Date date = cal.getTime();
        Query q = em.createQuery(ejbQuery);
        q.setParameter("date", date, TemporalType.DATE);
        @SuppressWarnings("unchecked") List<Object[]> results = q.getResultList();
        for (Object[] os : results) {
            logger.info("Array follows:");
            for (Object o : os) {
                logger.info("Class: " + o.getClass());
                logger.info("Value: " + o);
            }
        }
    }
}
