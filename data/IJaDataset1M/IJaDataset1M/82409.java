package org.bejug.javacareers.jobs.dao.hibernate;

import org.bejug.javacareers.jobs.dao.CountryDao;
import org.bejug.javacareers.jobs.model.Country;
import org.bejug.javacareers.jobs.model.Region;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import java.util.Iterator;
import java.util.List;

/**
 * Country Hibernate DAO implementation.
 *
 * @author Sven Schauwvliege (Last modified by $Author: stephan_janssen $)
 * @version $Revision: 1.3 $ - $Date: 2005/10/11 08:43:10 $
 */
public class CountryDaoHibernateImpl extends HibernateDaoSupport implements CountryDao {

    /** 
     * {@inheritDoc}
     */
    public void store(Country country) throws DataAccessException {
        for (Iterator i = country.getRegion().iterator(); i.hasNext(); ) {
            ((Region) i.next()).setModificationDate(country.getModificationDate());
        }
        getHibernateTemplate().save(country);
    }

    /** 
     * {@inheritDoc}
     */
    public List getCountries() throws DataAccessException {
        return getHibernateTemplate().loadAll(Country.class);
    }

    /** 
     * {@inheritDoc}
     */
    public void deleteCountry(Country country) throws DataAccessException {
        getHibernateTemplate().delete(country);
    }

    /** 
     * {@inheritDoc}
     */
    public void deleteCountry(Integer id) throws DataAccessException {
        Country country = (Country) getHibernateTemplate().load(Country.class, id);
        getHibernateTemplate().delete(country);
    }

    /** 
     * {@inheritDoc}
     */
    public Country getCountry(Integer id) throws DataAccessException {
        return (Country) getHibernateTemplate().load(Country.class, id);
    }

    /**
     * {@inheritDoc}
     */
    public Country getCountryByName(String country) {
        List countrys = getHibernateTemplate().findByNamedQueryAndNamedParam("findCountryByName", "name", country);
        Country countr = null;
        if (countrys.size() != 0) {
            countr = (Country) countrys.get(0);
        }
        return countr;
    }
}
