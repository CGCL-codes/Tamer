package org.bejug.javacareers.jobs.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import org.bejug.javacareers.jobs.common.AbstractSpringContextDBUnitTests;
import org.bejug.javacareers.jobs.model.JobOffer;
import org.springframework.dao.DataAccessException;

/**
 * @author Sven Schauwvliege (Last modified by $Author: shally $)
 * @version $Revision: 1.14 $ - $Date: 2005/12/21 11:38:42 $
 */
public class JobOfferDaoImplTests extends AbstractSpringContextDBUnitTests {

    /**
     * the names of the tables need to be compaired
     */
    private static final String[] TABLE_NAMES = { "Resume", "Parameter", "Country", "Region", "Address", "Contact", "Organisation", "Person", "User", "Offer", "JobOffer", "Offer_OfferType", "Offer_Profile" };

    /**
     * the columns to ignore.
     */
    private static final String[] IGNORE = { "id", "modificationdate", "addressId", "contactId" };

    /**
     * the jobofferdao, injected through field-injection.
     */
    protected JobOfferDao jobOfferDao;

    /**
     * @return Returns the jobOfferDao.
     */
    public JobOfferDao getJobOfferDao() {
        return jobOfferDao;
    }

    /**
     * Test the Create, Read, Read all, Update and Delete functions for a JobOffer.
     *
     * @throws DataAccessException Thrown when a database exceptions occurs.
     */
    public void testCRUDOffer() throws DataAccessException {
        List offers = jobOfferDao.getOffers();
        assertTrue("read failed, amount of found objects not 5 but " + (offers.size() + 1), offers.size() == 5);
        assertTrue("read failed ", offers.get(0).getClass() == JobOffer.class);
        boolean change = true;
        for (Iterator i = offers.iterator(); i.hasNext(); ) {
            JobOffer jobOffer = (JobOffer) i.next();
            if (change) {
                getJobOfferDao().deleteOffer(jobOffer.getId());
            } else {
                getJobOfferDao().deleteOffer(jobOffer);
            }
            change = !change;
        }
        List emptyJobOffers = getJobOfferDao().getOffers();
        assertTrue("delete faled ", emptyJobOffers.size() == 0);
        for (Iterator i = offers.iterator(); i.hasNext(); ) {
            JobOffer jobOffer = (JobOffer) i.next();
            jobOffer.setVersion(null);
            jobOffer.setId(null);
            getJobOfferDao().store(jobOffer);
        }
        List foundJobOffers = getJobOfferDao().getOffers();
        assertTrue("save failed, amount of found objects not 5 but " + foundJobOffers.size() + 1, foundJobOffers.size() == 5);
        assertDBAsExpected(TABLE_NAMES, IGNORE);
    }

    /**
     * Test the Read and Update functions for a JobOffer.
     *
     * @throws DataAccessException Thrown when a database exceptions occurs.
     */
    public void testUpdateReadJobOffer() throws DataAccessException {
        JobOffer foundJobOffer = (JobOffer) getJobOfferDao().getOffer(new Integer(500));
        assertEquals("error in get by id", foundJobOffer.getTitle(), "Java developer");
        assertEquals("error in get by id", foundJobOffer.getId(), new Integer(500));
        foundJobOffer.setTitle("update");
        getJobOfferDao().store(foundJobOffer);
        JobOffer newJobOffer = (JobOffer) getJobOfferDao().getOffer(new Integer(500));
        assertEquals("error in update", newJobOffer.getTitle(), "update");
        assertEquals("error in update", newJobOffer.getId(), new Integer(500));
    }

    /**
     * @throws CacheException in case of error
     */
    public void testCaches() throws CacheException {
        CacheManager cacheManager = CacheManager.getInstance();
        String[] cacheNames = cacheManager.getCacheNames();
        List cacheList = new ArrayList();
        for (int i = 0; i < cacheNames.length; i++) {
            cacheList.add(cacheManager.getCache(cacheNames[i]));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onSetUpInTransaction() throws Exception {
        init(TABLE_NAMES);
    }
}
