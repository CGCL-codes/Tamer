package org.zeroexchange.dao.location;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.zeroexchange.dao.BaseStringPKDAO;
import org.zeroexchange.model.location.City;
import org.zeroexchange.model.location.Country;

/**
 * @author black
 *
 */
public class CityDAOImpl extends BaseStringPKDAO<City> implements CityDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<City> getProcessingClass() {
        return City.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<City> getCities(Country country) {
        return getSession().createCriteria(City.class).add(Restrictions.eq(City.FIELD_COUNTRY, country)).list();
    }
}
