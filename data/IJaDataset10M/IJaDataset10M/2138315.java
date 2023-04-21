package org.openlogbooks.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.junit.Test;
import org.openlogbooks.TestCaseBase;
import org.openlogbooks.ulm.model.Country;
import org.openlogbooks.ulm.model.Organisation;
import org.openlogbooks.ulm.model.OrganisationCountry;

public class CountryDaoTest extends TestCaseBase {

    private static final long serialVersionUID = 912839123L;

    /**
	 * Create a country and an organisation, then assign the Organisation to the
	 * country which should result in an organisation country
	 */
    @SuppressWarnings({ "boxing" })
    @Test
    public final void test_addOrganisationToCountry() throws Exception {
        Country country = new Country();
        country.setAnsiCode("GB");
        country.setName("United Kingdom");
        country.setCommonName("UK");
        Organisation org = new Organisation();
        org.setCommonName("NAUI");
        org.setEmail("info@naui.org");
        org.setHeadOfficeAddress1("address1");
        org.setHeadOfficeAddress2("address2");
        org.setHeadOfficeAddress3("address3");
        org.setName("NAUI");
        Organisation persistedOrganisation = organisationDao.makePersistent(org);
        country.addOrganisation(persistedOrganisation);
        Country persistedCountry = countryDao.makePersistent(country);
        assertNotNull("There were no organisations asociated with the country", persistedCountry.getOrganisationCountries());
        assertTrue(persistedCountry.getOrganisationCountries().size() >= 1);
        for (OrganisationCountry oci : persistedCountry.getOrganisationCountries()) {
            log.info("Found org country: " + oci.toString());
        }
    }

    @Test
    public final void test_getById() throws Exception {
        Country country = countryDao.findById(1L, LockMode.NONE);
        assertNotNull(country);
    }

    @Test
    public final void test_getAll() throws Exception {
        final List<Country> countryList = countryDao.findAll();
        assertNotNull(countryList);
        for (Country country : countryList) {
            log.info("Country: " + country.toString());
        }
    }

    Log log = LogFactory.getLog(CountryDaoTest.class);
}
