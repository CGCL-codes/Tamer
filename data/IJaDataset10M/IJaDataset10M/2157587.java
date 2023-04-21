package com.idna.gav.service.ga;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import com.idna.common.domain.idsearch.IdSearch;
import com.idna.common.domain.idsearch.IdSearchAddress;

@Ignore
public class AddressCleanserServiceImplFranceTest extends SingleCountryAddressCleanserServiceImplTest {

    public static final Logger LOG = Logger.getLogger(AddressCleanserServiceImplFranceTest.class);

    public void testCleanseAddressIsSuccessfulProcessed() throws Exception {
        IdSearch result = getAddressCleanserService().cleanseAddress(request, searchFeatureID);
        IdSearchAddress address = result.getAddresses().get(0);
        assertEquals(address.getPremise(), premise);
        assertEquals(address.getStreet(), street);
        assertEquals(address.getPostTown(), "Auch");
        assertEquals(address.getPostcode(), postcode);
        assertEquals(address.getCountryCode(), countryCode);
        assertEquals(address.getRegion(), "Gers");
    }

    public void testCleanseAddressIsFailureStandardised() throws Exception {
        request.getAddresses().get(0).setPostcode("123");
        IdSearch result = getAddressCleanserService().cleanseAddress(request, searchFeatureID);
        IdSearchAddress address = result.getAddresses().get(0);
        assertEquals(address.getRegion(), "Pas-de-Calais");
        assertEquals(address.getPostcode(), "00123");
    }

    @Override
    protected void initAddressLines() {
        premise = "18";
        street = "rue du Tapis Vert";
        locality = "Pas-de-Calais";
        postcode = "32000";
        countryCode = "FRA";
    }
}
