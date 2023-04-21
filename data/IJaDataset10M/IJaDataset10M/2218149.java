package org.zeroexchange.location;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.zeroexchange.dao.DAOFactory;
import org.zeroexchange.dao.location.CityDAO;
import org.zeroexchange.dao.location.CountryDAO;
import org.zeroexchange.model.location.City;
import org.zeroexchange.model.location.Country;

/**
 * @author black
 *
 */
public class DefaultLocationReader implements LocationReader {

    @Autowired
    private DAOFactory daoFactory;

    @Override
    public List<Country> getCountries() {
        CountryDAO dao = daoFactory.getDAOForEntity(Country.class);
        return dao.getCountries();
    }

    @Override
    public List<City> getCities(Country country) {
        CityDAO dao = daoFactory.getDAOForEntity(City.class);
        return dao.getCities(country);
    }
}
