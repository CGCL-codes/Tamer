package com.idna.gav.service.ga;

import org.apache.log4j.Logger;
import com.idna.common.domain.idsearch.IdSearch;
import com.idna.common.domain.idsearch.IdSearchAddress;

public class AddressCleanserServiceImplHungaryTest extends SingleCountryAddressCleanserServiceImplTest {

    public static final Logger LOG = Logger.getLogger(AddressCleanserServiceImplGermanyTest.class);

    public void testCleanseAddressIsFailureStandardised() throws Exception {
        request.getAddresses().get(0).setPostcode("123");
        request.getAddresses().get(0).setStreet("Harm");
        IdSearch result = getAddressCleanserService().cleanseAddress(request, searchFeatureID);
        IdSearchAddress address = result.getAddresses().get(0);
        assertEquals(address.getPostTown(), "");
        assertEquals(address.getPostcode(), "123");
    }

    @Override
    protected void initAddressLines() {
        premise = "6";
        street = "Harmincad utca";
        locality = "Budapest";
        postcode = "1051";
        countryCode = "HUN";
    }
}
