package org.nakedobjects.xat.integ.junit4.sample.services;

import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.Filter;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.xat.integ.junit4.sample.domain.Country;
import java.util.List;
import org.apache.log4j.Logger;

@Named("Countries")
public class CountryRepository extends AbstractFactoryAndRepository {

    private static final Logger LOGGER = Logger.getLogger(CountryRepository.class);

    /**
     * Lists all countries in the repository.
     */
    public List<Country> showAll() {
        return allInstances(Country.class, false);
    }

    /**
     * Returns the Country with given code
     */
    public Country findByCode(@Named("Code") final String code) {
        return firstMatch(Country.class, new Filter() {

            public boolean accept(final Object obj) {
                final Country pojo = (Country) obj;
                return code.equals(pojo.getCode());
            }
        }, false);
    }

    /**
     * Creates a new countryGBR.
     * 
     * <p>
     * For use by fixtures only.
     * 
     * @return
     */
    @Hidden
    public Country newCountry(final String code, final String name) {
        final Country country = newTransientInstance(Country.class);
        country.setCode(code);
        country.setName(name);
        makePersistent(country);
        return country;
    }
}
