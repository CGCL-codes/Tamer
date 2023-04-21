package com.acv.service.i18n.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import com.acv.dao.catalog.locations.airports.model.Airport;
import com.acv.dao.common.model.City;
import com.acv.dao.common.model.Country;
import com.acv.dao.common.model.Currency;
import com.acv.dao.common.model.Language;
import com.acv.dao.common.model.Province;
import com.acv.dao.common.model.Title;
import com.acv.service.common.AirportManager;
import com.acv.service.common.CityManager;
import com.acv.service.common.CountryManager;
import com.acv.service.common.LanguageManager;
import com.acv.service.common.ProvinceManager;
import com.acv.service.common.TitleManager;
import com.acv.service.i18n.I18nCacheManager;
import com.acv.service.i18n.model.AirportList;
import com.acv.service.i18n.model.CountryProvinceMap;
import com.acv.service.i18n.model.I18nCacheableList;
import com.acv.util.ConvertUtil;

/**
 * The Class I18nCacheManagerImpl.
 *
 * @author Mickael Guesnon Class in charge of storing elements to the cache
 *         maps. Contains the translater map initialized with bundles at
 *         application loading.
 */
public class I18nCacheManagerImpl implements I18nCacheManager {

    private static final Logger log = Logger.getLogger(I18nCacheManagerImpl.class);

    private static Map<String, Map<String, String>> translater = new Hashtable<String, Map<String, String>>();

    private static Hashtable<String, I18nCacheableList> cityCaches = new Hashtable<String, I18nCacheableList>();

    private static Hashtable<String, I18nCacheableList> originCityCaches = new Hashtable<String, I18nCacheableList>();

    private static Hashtable<String, I18nCacheableList> destinationCityCaches = new Hashtable<String, I18nCacheableList>();

    private static Hashtable<String, I18nCacheableList> airportCaches = new Hashtable<String, I18nCacheableList>();

    private static Hashtable<String, I18nCacheableList> provinceCaches = new Hashtable<String, I18nCacheableList>();

    private static Hashtable<String, I18nCacheableList> countryCaches = new Hashtable<String, I18nCacheableList>();

    private static Hashtable<String, I18nCacheableList> titleCaches = new Hashtable<String, I18nCacheableList>();

    private static Hashtable<String, I18nCacheableList> languageCaches = new Hashtable<String, I18nCacheableList>();

    private static Hashtable<String, I18nCacheableList> currencyCaches = new Hashtable<String, I18nCacheableList>();

    private static Hashtable<String, CountryProvinceMap> countryProvinceMapCaches = new Hashtable<String, CountryProvinceMap>();

    private static Hashtable<String, I18nCacheableList> originAirportListCaches = new Hashtable<String, I18nCacheableList>();

    private static Hashtable<String, I18nCacheableList> destinationAirportListCaches = new Hashtable<String, I18nCacheableList>();

    private CityManager cityManager;

    private ProvinceManager provinceManager;

    private CountryManager countryManager;

    private TitleManager titleManager;

    private LanguageManager languageManager;

    private AirportManager airportManager;

    private SessionFactory sessionFactory;

    public void setLanguageManager(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    public void setTitleManager(TitleManager titleManager) {
        this.titleManager = titleManager;
    }

    public void setCityManager(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    public void setCountryManager(CountryManager countryManager) {
        this.countryManager = countryManager;
    }

    public void setProvinceManager(ProvinceManager provinceManager) {
        this.provinceManager = provinceManager;
    }

    static {
        try {
            for (String bundle : ConvertUtil.convertBundleToMap(ResourceBundle.getBundle("i18nBundleList")).values()) {
                for (String language : ConvertUtil.convertBundleToMap(ResourceBundle.getBundle("locale")).values()) {
                    Locale locale = new Locale(language);
                    translater.put(bundle + "_" + language, ConvertUtil.convertBundleToMap(ResourceBundle.getBundle("translations." + bundle, locale)));
                }
            }
        } catch (MissingResourceException e) {
            log.warn("Unable to find resource bundle for i18nCacheManager.", e);
        }
    }

    /**
	 * Clear caches.
	 */
    public static void clearCaches() {
        flush();
    }

    /**
	 * Clear all hashtables content.
	 */
    private static void flush() {
        cityCaches.clear();
        originCityCaches.clear();
        destinationCityCaches.clear();
        airportCaches.clear();
        originAirportListCaches.clear();
        destinationAirportListCaches.clear();
        provinceCaches.clear();
        countryCaches.clear();
        countryProvinceMapCaches.clear();
        titleCaches.clear();
        currencyCaches.clear();
        languageCaches.clear();
        log.info("I18nCaches successfully flushed");
    }

    public List<String> getLanguageCodeList() {
        List<String> result = new ArrayList<String>();
        for (Language language : loadLanguages()) {
            result.add(language.getLanguageCode());
        }
        return result;
    }

    /**
	 * Public method to get an i18n City List.
	 *
	 * @param langCode
	 *            The language code.
	 *
	 * @return the full city list from cache
	 */
    public I18nCacheableList getFullCityListFromCache(String langCode) {
        if (cityCaches.containsKey(langCode)) {
            return cityCaches.get(langCode);
        }
        I18nCacheableList cityList = getCitiesTranslatedList(loadCities(), langCode);
        if (!cityList.isEmpty()) {
            cityCaches.put(langCode, cityList);
        }
        return cityList;
    }

    public I18nCacheableList getOriginCityListFromCache(String langCode) {
        if (originCityCaches.containsKey(langCode)) {
            return originCityCaches.get(langCode);
        }
        List<City> originCities = new ArrayList<City>();
        for (City city : loadCities()) {
            if (city.isOrigin()) originCities.add(city);
        }
        I18nCacheableList cityList = getCitiesTranslatedList(originCities, langCode);
        if (!cityList.isEmpty()) {
            originCityCaches.put(langCode, cityList);
        }
        return cityList;
    }

    public I18nCacheableList getDestinationCityListFromCache(String langCode) {
        if (destinationCityCaches.containsKey(langCode)) {
            return destinationCityCaches.get(langCode);
        }
        List<City> destCities = new ArrayList<City>();
        for (City city : loadCities()) {
            if (city.isDestination()) destCities.add(city);
        }
        I18nCacheableList cityList = getCitiesTranslatedList(destCities, langCode);
        if (!cityList.isEmpty()) {
            destinationCityCaches.put(langCode, cityList);
        }
        return cityList;
    }

    /**
	 * Public method to get an i18n Airport List.
	 *
	 * @param langCode
	 *            The language code.
	 *
	 * @return the airport list from cache
	 */
    public I18nCacheableList getAirportListFromCache(String langCode) {
        if (airportCaches.containsKey(langCode)) {
            return airportCaches.get(langCode);
        }
        I18nCacheableList airportList = getAirportTranslatedList(loadAirports(), langCode);
        if (!airportList.isEmpty()) {
            airportCaches.put(langCode, airportList);
        }
        return airportList;
    }

    /**
	 * Public method to get an i18n Country List.
	 *
	 * @param langCode
	 *            The language code.
	 *
	 * @return the country list from cache
	 */
    public I18nCacheableList getCountryListFromCache(String langCode) {
        if (countryCaches.containsKey(langCode)) {
            return countryCaches.get(langCode);
        }
        I18nCacheableList countryList = getCountriesTranslatedList(loadCountries(), langCode);
        if (!countryList.isEmpty()) {
            countryCaches.put(langCode, countryList);
        }
        return countryList;
    }

    /**
	 * Public method to get an i18n Province List.
	 *
	 * @param langCode
	 *            The language code.
	 *
	 * @return the province list from cache
	 */
    public I18nCacheableList getProvinceListFromCache(String langCode) {
        if (provinceCaches.containsKey(langCode)) {
            return provinceCaches.get(langCode);
        }
        I18nCacheableList provinceList = getProvincesTranslatedList(loadProvinces(), langCode);
        if (!provinceList.isEmpty()) {
            provinceCaches.put(langCode, provinceList);
        }
        return provinceList;
    }

    /**
	 * Public method to get an i18n Title List.
	 *
	 * @param langCode
	 *            The language code.
	 *
	 * @return the title list from cache
	 */
    public I18nCacheableList getTitleListFromCache(String langCode) {
        if (titleCaches.containsKey(langCode)) {
            return titleCaches.get(langCode);
        }
        I18nCacheableList titleList = getTitlesTranslatedList(loadTitles(), langCode);
        if (!titleList.isEmpty()) {
            titleCaches.put(langCode, titleList);
        }
        return titleList;
    }

    /**
	 * Public method to get an i18n Language List.
	 *
	 * @param langCode
	 *            The language code.
	 *
	 * @return the language list from cache
	 */
    public I18nCacheableList getLanguageListFromCache(String langCode) {
        if (languageCaches.containsKey(langCode)) {
            return languageCaches.get(langCode);
        }
        I18nCacheableList languageList = getLanguagesTranslatedList(loadLanguages(), langCode);
        if (!languageList.isEmpty()) {
            languageCaches.put(langCode, languageList);
        }
        return languageList;
    }

    /**
	 * Public method to get an i18n Currency List.
	 *
	 * @param langCode
	 *            The language code.
	 *
	 * @return the currency list from cache
	 */
    public I18nCacheableList getCurrencyListFromCache(String langCode) {
        if (currencyCaches.containsKey(langCode)) {
            return currencyCaches.get(langCode);
        }
        List<City> cities = loadCities();
        Set<Currency> currencies = new HashSet<Currency>();
        for (City city : cities) {
            currencies.add(city.getCurrency());
        }
        I18nCacheableList currencyList = getCurrenciesTranslatedList(currencies, langCode);
        if (!currencyList.isEmpty()) {
            currencyCaches.put(langCode, currencyList);
        }
        return currencyList;
    }

    /**
	 * Public method to get an i18n Country Province Map.
	 *
	 * @param langCode
	 *            The language code.
	 *
	 * @return the country province map
	 */
    public CountryProvinceMap getCountryProvinceMap(String langCode) {
        if (countryProvinceMapCaches.containsKey(langCode)) {
            return countryProvinceMapCaches.get(langCode);
        }
        I18nCacheableList countryList = getCountryListFromCache(langCode);
        I18nCacheableList provinceList = getProvinceListFromCache(langCode);
        CountryProvinceMap result = new CountryProvinceMap();
        result.populate(countryList, provinceList, loadCountries());
        if (!result.isEmpty()) {
            countryProvinceMapCaches.put(langCode, result);
        }
        return result;
    }

    public I18nCacheableList getOriginAirportListFromCache(String langCode) {
        if (originAirportListCaches.containsKey(langCode)) {
            return originAirportListCaches.get(langCode);
        }
        I18nCacheableList cityList = getFullCityListFromCache(langCode);
        List<Airport> originAirports = new ArrayList<Airport>();
        for (Airport airport : loadAirports()) {
            if (airport.getCity() == null) {
                log.warn("Airport " + airport.getAirportCode() + " isn't linked to a City. Translation aborded");
                continue;
            }
            if (airport.getDefaultFlag()) if (airport.getCity().isOrigin()) originAirports.add(airport);
        }
        AirportList result = new AirportList();
        result.populate(cityList, originAirports);
        if (!result.isEmpty()) {
            originAirportListCaches.put(langCode, result);
        }
        return result;
    }

    public I18nCacheableList getDestinationAirportListFromCache(String langCode) {
        if (destinationAirportListCaches.containsKey(langCode)) {
            return destinationAirportListCaches.get(langCode);
        }
        I18nCacheableList cityList = getFullCityListFromCache(langCode);
        AirportList result = new AirportList();
        for (Airport airport : loadAirports()) {
            if (airport.getCity() == null) {
                log.warn("Airport " + airport.getAirportCode() + " isn't linked to a City. Translation aborded");
                continue;
            }
            if (airport.getDefaultFlag()) if (airport.getCity().isDestination()) {
                String destination = null;
                try {
                    destination = airport.getDestination().getContent().get(langCode.toUpperCase()).get("title");
                } catch (Exception e) {
                }
                if (destination != null && destination.length() > 1) {
                    result.put(airport.getId(), airport.getAirportCode(), destination);
                } else {
                }
            }
        }
        if (!result.isEmpty()) {
            destinationAirportListCaches.put(langCode, result);
        }
        return result;
    }

    /**
	 * Private method that load Cities from database.
	 *
	 * @return the list< city>
	 */
    private List<City> loadCities() {
        return cityManager.getCities();
    }

    /**
	 * Private method that load Provinces from database.
	 *
	 * @return the list< province>
	 */
    private List<Province> loadProvinces() {
        return provinceManager.getProvinces();
    }

    /**
	 * Private method that load Countries from database.
	 *
	 * @return the list< country>
	 */
    private List<Country> loadCountries() {
        return countryManager.getCountries();
    }

    /**
	 * Private method that load Titles from database.
	 *
	 * @return the list< title>
	 */
    private List<Title> loadTitles() {
        return titleManager.getTitles();
    }

    /**
	 * Private method that load Languages from database.
	 *
	 * @return the list< language>
	 */
    private List<Language> loadLanguages() {
        return languageManager.getLanguages();
    }

    /**
	 * Private method that load airports from database.
	 *
	 * @return the list< airport>
	 */
    private List<Airport> loadAirports() {
        return airportManager.search(null, null);
    }

    /**
	 * Private method in charge of translating a city list.
	 *
	 * @param cities
	 *            The list of cities to translate
	 * @param langCode
	 *            The language code of the requiered translation
	 *
	 * @return I18nList
	 */
    private I18nCacheableList getCitiesTranslatedList(List<City> cities, String langCode) {
        Date begin = new Date();
        int count = 0;
        I18nCacheableList translatedCities = new I18nCacheableList();
        for (City city : cities) {
            count++;
            if (city.getAirports().isEmpty()) {
                log.warn("City " + city.getCity() + " isn't linked to an airport. Translation aborded");
                continue;
            }
            try {
                translatedCities.put(city.getId(), city.getCity(), getTranslation(City.class.getSimpleName(), langCode, city.getId().toString()));
            } catch (NullPointerException e) {
                log.warn("Unable to get translation for " + city.toString());
                translatedCities.put(city.getId(), city.getCity(), city.getCity());
            }
        }
        Long cacheLoadingTime = new Date().getTime() - begin.getTime();
        if (log.isDebugEnabled()) log.debug("Elapsed time : " + cacheLoadingTime + " milliseconds for " + count + " cities");
        return translatedCities;
    }

    /**
	 * Private method in charge of translating a city list.
	 *
	 * @param airports
	 *            The list of airportsd to translate
	 * @param langCode
	 *            The language code of the requiered translation
	 *
	 * @return I18nList
	 */
    private I18nCacheableList getAirportTranslatedList(List<Airport> airports, String langCode) {
        Date begin = new Date();
        I18nCacheableList translatedAirport = new I18nCacheableList();
        for (Airport airport : airports) {
            if (airport.getCity() == null) {
                log.warn("Airport " + airport.getAirportCode() + " isn't linked to a City. Translation aborded");
                continue;
            }
            try {
                translatedAirport.put(airport.getId(), airport.getAirportCode(), getTranslation(City.class.getSimpleName(), langCode, airport.getCity().getId().toString()));
            } catch (NullPointerException e) {
                try {
                    log.warn("Unable to get translation for " + airport.getCity().toString());
                    translatedAirport.put(airport.getId(), airport.getAirportCode(), airport.getCity().getCity());
                } catch (Exception e1) {
                }
            }
        }
        Long cacheLoadingTime = new Date().getTime() - begin.getTime();
        if (log.isDebugEnabled()) log.debug("Elapsed time : " + cacheLoadingTime + " milliseconds");
        return translatedAirport;
    }

    /**
	 * Private method in charge of translating a province list.
	 *
	 * @param provinces
	 *            The list of provinces to translate
	 * @param langCode
	 *            The language code of the requiered translation
	 *
	 * @return I18nList
	 */
    private I18nCacheableList getProvincesTranslatedList(List<Province> provinces, String langCode) {
        I18nCacheableList translatedProvinces = new I18nCacheableList();
        for (Province province : provinces) {
            try {
                translatedProvinces.put(province.getId(), province.getProvinceCode(), getTranslation(Province.class.getSimpleName(), langCode, province.getId().toString()));
            } catch (NullPointerException e) {
                log.warn("Unable to get translation for " + province.toString());
                translatedProvinces.put(province.getId(), province.getProvinceCode(), province.getName());
            }
        }
        return translatedProvinces;
    }

    /**
	 * Private method in charge of translating a Country list.
	 *
	 * @param countries
	 *            The list of countries to translate
	 * @param langCode
	 *            The language code of the requiered translation
	 *
	 * @return I18nList
	 */
    private I18nCacheableList getCountriesTranslatedList(List<Country> countries, String langCode) {
        I18nCacheableList translatedCountries = new I18nCacheableList();
        for (Country country : countries) {
            try {
                translatedCountries.put(country.getId(), country.getCountryCode(), getTranslation(Country.class.getSimpleName(), langCode, country.getId().toString()));
            } catch (NullPointerException e) {
                log.warn("Unable to get translation for " + country.toString());
                translatedCountries.put(country.getId(), country.getCountryCode(), country.getName());
            }
        }
        return translatedCountries;
    }

    /**
	 * Private method in charge of translating a title list.
	 *
	 * @param titles
	 *            The list of titles to translate
	 * @param langCode
	 *            The language code of the requiered translation
	 *
	 * @return I18nList
	 */
    private I18nCacheableList getTitlesTranslatedList(List<Title> titles, String langCode) {
        I18nCacheableList translatedTitles = new I18nCacheableList();
        for (Title title : titles) {
            try {
                translatedTitles.put(title.getId(), title.getTitle(), getTranslation(Title.class.getSimpleName(), langCode, title.getId().toString()));
            } catch (NullPointerException e) {
                log.warn("Unable to get translation for " + title.toString());
                translatedTitles.put(title.getId(), title.getTitle(), title.getTitle());
            }
        }
        return translatedTitles;
    }

    /**
	 * Private method in charge of translating a language list.
	 *
	 * @param languages
	 *            The list of languages to translate
	 * @param langCode
	 *            The language code of the requiered translation
	 *
	 * @return I18nList
	 */
    private I18nCacheableList getLanguagesTranslatedList(List<Language> languages, String langCode) {
        I18nCacheableList translatedLanguages = new I18nCacheableList();
        for (Language language : languages) {
            try {
                translatedLanguages.put(language.getId(), language.getLanguageCode(), getTranslation(Language.class.getSimpleName(), langCode, language.getId().toString()));
            } catch (NullPointerException e) {
                log.warn("Unable to get translation for " + language.toString());
                translatedLanguages.put(language.getId(), language.getLanguageCode(), language.getLanguage());
            }
        }
        return translatedLanguages;
    }

    /**
	 * Private method in charge of translating a currency list.
	 *
	 * @param currencies
	 *            The list of currencies to translate
	 * @param langCode
	 *            The language code of the requiered translation
	 *
	 * @return I18nList
	 */
    private I18nCacheableList getCurrenciesTranslatedList(Set<Currency> currencies, String langCode) {
        I18nCacheableList translatedCurrencies = new I18nCacheableList();
        for (Currency currency : currencies) {
            try {
                translatedCurrencies.put(currency.getId(), currency.getCode(), getTranslation(Currency.class.getSimpleName(), langCode, currency.getId().toString()));
            } catch (NullPointerException e) {
                log.warn("Unable to get translation for " + currency.toString());
                translatedCurrencies.put(currency.getId(), currency.getCode(), currency.getCurrency());
            }
        }
        return translatedCurrencies;
    }

    /**
	 * Private method in charge of translating an object using the translater
	 * map.
	 *
	 * @param className
	 *            The class name of the object to translate
	 * @param langCode
	 *            The language code
	 * @param id
	 *            The id of the object to translate
	 *
	 * @return String translated object name.
	 *
	 * @throws NullPointerException
	 *             the null pointer exception
	 */
    private String getTranslation(String className, String langCode, String id) throws NullPointerException {
        return translater.get(className + "_" + langCode).get(id);
    }

    /**
	 * Sets the airport manager.
	 *
	 * @param airportManager
	 *            the new airport manager
	 */
    public void setAirportManager(AirportManager airportManager) {
        this.airportManager = airportManager;
    }

    public List<Airport> getAirportByCode(String airportCode) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("code", airportCode);
        return airportManager.search(parameters, null);
    }

    /**
	 * Gets the session factory.
	 *
	 * @return the session factory
	 */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
	 * Sets the session factory.
	 *
	 * @param sessionFactory
	 *            the new session factory
	 */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
