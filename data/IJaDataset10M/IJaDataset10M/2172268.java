package org.bejug.javacareers.jobs.service;

import java.util.List;
import org.bejug.javacareers.common.exception.DuplicateUserNameException;
import org.bejug.javacareers.common.exception.LastAdminException;
import org.bejug.javacareers.common.search.SearchCriteria;
import org.bejug.javacareers.common.search.SearchCriteriaFactory;
import org.bejug.javacareers.generator.StubGenerator;
import org.bejug.javacareers.jobs.common.AbstractSpringContextTests;
import org.bejug.javacareers.jobs.model.Address;
import org.bejug.javacareers.jobs.model.JobOffer;
import org.bejug.javacareers.jobs.model.Organisation;
import org.bejug.javacareers.jobs.model.OrganisationType;
import org.bejug.javacareers.jobs.model.User;

/**
 * @author Peter Symoens (Last modified by $Author: shally $)
 * @version $Revision: 1.16 $ - $Date: 2005/12/20 15:36:47 $)
 * Todo: this unittest is not correct
 */
public class JobServiceImplTests extends AbstractSpringContextTests {

    /**
     * The admin service which gets set via an IoC setter injection.
     */
    protected AdminService adminService;

    /**
     * The job service reference which gets set via an IoC setter injection.
     */
    protected JobService jobService;

    /**
     * The SearchCriteriaFactory
     */
    protected SearchCriteriaFactory searchCriteriaFactory;

    /**
     * @return Returns the adminService.
     */
    public AdminService getAdminService() {
        return adminService;
    }

    /**
     * @return Returns the jobService.
     */
    public JobService getJobService() {
        return jobService;
    }

    /**
     * Test if the jobService is configured and injected.
     */
    public void testGetJobOfferDao() {
        assertNotNull(getJobService());
    }

    /**
     * Todo: Finish this test method!!
     */
    public void testGetOffers() {
        assertNotNull(getJobService());
        JobOffer jobOffer = StubGenerator.getStubJobOffer();
        User myUser = StubGenerator.getStubUser();
        Organisation org = StubGenerator.getStubOrganisation();
        Address address = StubGenerator.getStubAddress();
        OrganisationType type = StubGenerator.getStubOrganisationType();
        org.setOrganisationType(type);
        getAdminService().storeOrganisationType(type);
        org.setAddress(address);
        myUser.setOrganisation(org);
        jobOffer.setUser(myUser);
        try {
            getAdminService().storeUser(myUser);
        } catch (DuplicateUserNameException e) {
            assertTrue("Duplicate username", false);
            e.printStackTrace();
        } catch (LastAdminException e) {
            assertTrue("Last admin username in save", false);
        }
        getJobService().storeJobOffer(jobOffer);
        List list = getJobService().getJobOffers();
        assertNotNull(list);
        assertTrue(list.size() == 1);
        SearchCriteria searchCriteria = searchCriteriaFactory.createSearchCriteria(JobOffer.class);
        searchCriteria.addEqualsCriterium("description", "Test");
        List searchList = null;
        try {
            searchList = getJobService().getJobOffers(searchCriteria);
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
            assertTrue("illegal argument exception", false);
        }
        assertNotNull(searchList);
        assertTrue(searchList.size() == 1);
        int numberOfJobsInDatabase = getJobService().getJobOfferCount();
        if (numberOfJobsInDatabase != 1) {
            assertTrue(false);
        }
        SearchCriteria searchCriteriaIllegal = searchCriteriaFactory.createSearchCriteria(User.class);
        searchCriteriaIllegal.addEqualsCriterium("description", "Test");
        searchList = null;
        boolean exception = false;
        try {
            searchList = getJobService().getJobOffers(searchCriteriaIllegal);
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
            exception = true;
        }
        assertTrue("illegal argument exception", exception);
    }
}
