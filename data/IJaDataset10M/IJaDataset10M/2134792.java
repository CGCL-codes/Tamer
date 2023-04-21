package org.gtugs.service;

import org.gtugs.domain.Chapter;
import org.gtugs.domain.Country;
import org.gtugs.repository.CountryDao;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import java.util.List;

/**
 * @author jasonacooper@google.com (Jason Cooper)
 */
public class SimpleCountryManager implements CountryManager {

    private static final String COUNTRIES_MEMCACHE_KEY = "countries";

    private CountryDao countryDao;

    public List<Country> getCountries() {
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
        if (!ms.contains(COUNTRIES_MEMCACHE_KEY)) {
            ms.put(COUNTRIES_MEMCACHE_KEY, countryDao.getCountries());
        }
        return (List<Country>) ms.get(COUNTRIES_MEMCACHE_KEY);
    }

    public void storeCountry(Country country) {
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
        countryDao.storeCountry(country);
        ms.delete(COUNTRIES_MEMCACHE_KEY);
    }

    public void setCountryDao(CountryDao countryDao) {
        this.countryDao = countryDao;
    }
}
