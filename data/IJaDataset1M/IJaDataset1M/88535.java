package utility.webmailclient.app.usermanagement.delegate;

import utility.webmailclient.app.persistance.model.City;
import utility.webmailclient.app.persistance.model.Country;
import utility.webmailclient.app.persistance.model.State;
import utility.webmailclient.app.usermanagement.persistance.dao.GeoDAO;

public class GeoServiceManagerImpl implements GeoServiceManager {

    private GeoDAO geoDAO;

    public GeoDAO getGeoDAO() {
        return geoDAO;
    }

    public void setGeoDAO(GeoDAO geoDAO) {
        this.geoDAO = geoDAO;
    }

    @Override
    public City[] getCities(String country, String state) {
        return null;
    }

    @Override
    public Country[] getCountries() {
        return null;
    }

    @Override
    public State[] getStates(String country) {
        return null;
    }
}
