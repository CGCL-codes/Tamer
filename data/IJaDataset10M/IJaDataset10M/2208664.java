package org.nightlabs.jfire.geography;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.jdo.FetchPlan;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import org.nightlabs.jdo.NLJDOHelper;
import org.nightlabs.jdo.ObjectID;
import org.nightlabs.jfire.base.BaseSessionBeanImpl;
import org.nightlabs.jfire.geography.id.CityID;
import org.nightlabs.jfire.geography.id.CountryID;
import org.nightlabs.jfire.geography.id.LocationID;
import org.nightlabs.jfire.geography.id.RegionID;
import org.nightlabs.jfire.organisation.Organisation;
import org.nightlabs.util.CollectionUtil;

/**
 * @ejb.bean name="jfire/ejb/JFireGeography/GeographyManager"
 *           jndi-name="jfire/ejb/JFireGeography/GeographyManager"
 *           type="Stateless"
 *           transaction-type="Container"
 *
 * @ejb.util generate="physical"
 * @ejb.transaction type="Required"
 */
public abstract class GeographyManagerBean extends BaseSessionBeanImpl implements SessionBean {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(GeographyManagerBean.class);

    @Override
    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {
        if (logger.isDebugEnabled()) logger.debug(this.getClass().getName() + ".setSessionContext(" + sessionContext + ")");
        super.setSessionContext(sessionContext);
    }

    @Override
    public void unsetSessionContext() {
        super.unsetSessionContext();
    }

    /**
	 * @ejb.create-method
	 * @ejb.permission role-name="_Guest_"
	 */
    public void ejbCreate() throws CreateException {
        if (logger.isDebugEnabled()) logger.debug(this.getClass().getName() + ".ejbCreate()");
    }

    /**
	 * {@inheritDoc}
	 *
	 * @ejb.permission unchecked="true"
	 */
    @Override
    public void ejbRemove() throws EJBException, RemoteException {
        if (logger.isDebugEnabled()) logger.debug(this.getClass().getName() + ".ejbRemove()");
    }

    /**
	 * @ejb.interface-method
	 * @ejb.transaction type="Supports"
	 * @ejb.permission role-name="_Guest_"
	 */
    @Override
    public String ping(String message) {
        return super.ping(message);
    }

    @Override
    public void ejbActivate() throws EJBException, RemoteException {
        if (logger.isDebugEnabled()) logger.debug(this.getClass().getName() + ".ejbActivate()");
    }

    @Override
    public void ejbPassivate() throws EJBException, RemoteException {
        if (logger.isDebugEnabled()) logger.debug(this.getClass().getName() + ".ejbPassivate()");
    }

    /**
	 * @ejb.interface-method
	 * @ejb.transaction type="Required"
	 * @ejb.permission role-name="_System_"
	 */
    public void initialise() {
        GeographyImplJDO.register();
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 * @!ejb.transaction type="Supports" @!This usually means that no transaction is opened which is significantly faster and recommended for all read-only EJB methods! Marco.
	 */
    public Collection<CountryID> getCountryIDs() {
        PersistenceManager pm = getPersistenceManager();
        try {
            Query q = pm.newQuery(Country.class);
            q.setResult("JDOHelper.getObjectId(this)");
            Collection<CountryID> res = CollectionUtil.castCollection((Collection<?>) q.execute());
            return new HashSet<CountryID>(res);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 * @!ejb.transaction type="Supports" @!This usually means that no transaction is opened which is significantly faster and recommended for all read-only EJB methods! Marco.
	 */
    public Collection<RegionID> getRegionIDs(CountryID countryID) {
        PersistenceManager pm = getPersistenceManager();
        try {
            Query q = pm.newQuery(Region.class);
            q.setResult("JDOHelper.getObjectId(this)");
            if (countryID != null) q.setFilter("JDOHelper.getObjectId(this.country) == :countryID");
            Collection<RegionID> res = CollectionUtil.castCollection((Collection<?>) q.execute(countryID));
            return new HashSet<RegionID>(res);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 * @!ejb.transaction type="Supports" @!This usually means that no transaction is opened which is significantly faster and recommended for all read-only EJB methods! Marco.
	 */
    public Collection<CityID> getCityIDs(RegionID regionID) {
        PersistenceManager pm = getPersistenceManager();
        try {
            Query q = pm.newQuery(City.class);
            q.setResult("JDOHelper.getObjectId(this)");
            if (regionID != null) q.setFilter("JDOHelper.getObjectId(this.region) == :regionID");
            Collection<CityID> res = CollectionUtil.castCollection((Collection<?>) q.execute(regionID));
            return new HashSet<CityID>(res);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 * @!ejb.transaction type="Supports" @!This usually means that no transaction is opened which is significantly faster and recommended for all read-only EJB methods! Marco.
	 */
    public Collection<LocationID> getLocationIDs(CityID cityID) {
        PersistenceManager pm = getPersistenceManager();
        try {
            Query q = pm.newQuery(Location.class);
            q.setResult("JDOHelper.getObjectId(this)");
            if (cityID != null) q.setFilter("JDOHelper.getObjectId(this.city) == :cityID");
            Collection<LocationID> res = CollectionUtil.castCollection((Collection<?>) q.execute(cityID));
            return new HashSet<LocationID>(res);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 * @!ejb.transaction type="Supports" @!This usually means that no transaction is opened which is significantly faster and recommended for all read-only EJB methods! Marco.
	 */
    public Collection<Country> getCountries(Collection<CountryID> countryIDs, String[] fetchGroups, int maxFetchDepth) {
        PersistenceManager pm = getPersistenceManager();
        try {
            return NLJDOHelper.getDetachedObjectList(pm, countryIDs, Country.class, fetchGroups, maxFetchDepth);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 * @!ejb.transaction type="Supports" @!This usually means that no transaction is opened which is significantly faster and recommended for all read-only EJB methods! Marco.
	 */
    public Collection<Region> getRegions(Collection<RegionID> regionIDs, String[] fetchGroups, int maxFetchDepth) {
        PersistenceManager pm = getPersistenceManager();
        try {
            return NLJDOHelper.getDetachedObjectList(pm, regionIDs, Region.class, fetchGroups, maxFetchDepth);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 * @!ejb.transaction type="Supports" @!This usually means that no transaction is opened which is significantly faster and recommended for all read-only EJB methods! Marco.
	 */
    public Collection<City> getCities(Collection<CityID> cityIDs, String[] fetchGroups, int maxFetchDepth) {
        PersistenceManager pm = getPersistenceManager();
        try {
            return NLJDOHelper.getDetachedObjectList(pm, cityIDs, City.class, fetchGroups, maxFetchDepth);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 * @!ejb.transaction type="Supports" @!This usually means that no transaction is opened which is significantly faster and recommended for all read-only EJB methods! Marco.
	 */
    public Collection<Location> getLocations(Collection<LocationID> locationIDs, String[] fetchGroups, int maxFetchDepth) {
        PersistenceManager pm = getPersistenceManager();
        try {
            return NLJDOHelper.getDetachedObjectList(pm, locationIDs, Location.class, fetchGroups, maxFetchDepth);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 */
    public Country importCountry(CountryID countryID, boolean get, String[] fetchGroups, int maxFetchDepth) {
        if (countryID == null) throw new IllegalArgumentException("countryID must not be null!");
        PersistenceManager pm = getPersistenceManager();
        try {
            Country country;
            try {
                country = (Country) pm.getObjectById(countryID);
            } catch (JDOObjectNotFoundException x) {
                country = null;
            }
            if (country == null) {
                country = Geography.sharedInstance().getCountry(countryID, true);
                country = country.copyForJDOStorage();
                country = pm.makePersistent(country);
            }
            if (!get) return null;
            pm.getFetchPlan().setMaxFetchDepth(maxFetchDepth);
            if (fetchGroups != null) pm.getFetchPlan().setGroups(fetchGroups);
            return pm.detachCopy(country);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 */
    public Region importRegion(RegionID regionID, boolean get, String[] fetchGroups, int maxFetchDepth) {
        PersistenceManager pm = getPersistenceManager();
        try {
            Region region;
            try {
                region = (Region) pm.getObjectById(regionID);
            } catch (JDOObjectNotFoundException x) {
                region = null;
            }
            if (region == null) {
                region = Geography.sharedInstance().getRegion(regionID, true);
                CountryID countryID = CountryID.create(region);
                Country persistentCountry = (Country) pm.getObjectById(countryID);
                region = region.copyForJDOStorage(persistentCountry);
                region = persistentCountry.addRegion(region);
            }
            if (!get) return null;
            pm.getFetchPlan().setMaxFetchDepth(maxFetchDepth);
            if (fetchGroups != null) pm.getFetchPlan().setGroups(fetchGroups);
            return pm.detachCopy(region);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 */
    public City importCity(CityID cityID, boolean get, String[] fetchGroups, int maxFetchDepth) {
        PersistenceManager pm = getPersistenceManager();
        try {
            City city;
            try {
                city = (City) pm.getObjectById(cityID);
            } catch (JDOObjectNotFoundException x) {
                city = null;
            }
            if (city == null) {
                city = Geography.sharedInstance().getCity(cityID, true);
                RegionID regionID = RegionID.create(city.getRegion());
                Region persistentRegion = (Region) pm.getObjectById(regionID);
                city = city.copyForJDOStorage(persistentRegion);
                city = persistentRegion.addCity(city);
            }
            if (!get) return null;
            pm.getFetchPlan().setMaxFetchDepth(maxFetchDepth);
            if (fetchGroups != null) pm.getFetchPlan().setGroups(fetchGroups);
            return pm.detachCopy(city);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 */
    public Location importLocation(LocationID locationID, boolean get, String[] fetchGroups, int maxFetchDepth) {
        PersistenceManager pm = getPersistenceManager();
        try {
            Location location;
            try {
                location = (Location) pm.getObjectById(locationID);
            } catch (JDOObjectNotFoundException x) {
                location = null;
            }
            if (location == null) {
                location = Geography.sharedInstance().getLocation(locationID, true);
                CityID cityID = CityID.create(location.getCity());
                City persistentCity = (City) pm.getObjectById(cityID);
                location = location.copyForJDOStorage(persistentCity);
                location = persistentCity.addLocation(location);
            }
            if (!get) return null;
            pm.getFetchPlan().setMaxFetchDepth(maxFetchDepth);
            if (fetchGroups != null) pm.getFetchPlan().setGroups(fetchGroups);
            return pm.detachCopy(location);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 */
    public byte[] getCSVData(String csvType, String countryID) {
        PersistenceManager pm = getPersistenceManager();
        pm.getFetchPlan().setMaxFetchDepth(1);
        pm.getFetchPlan().setGroup(FetchPlan.ALL);
        try {
            InitialContext initialContext = new InitialContext();
            try {
                byte[] data = CSV.getCSVData(pm, Organisation.getRootOrganisationID(initialContext), csvType, countryID);
                if (data == null) {
                    Geography.sharedInstance().needCountries();
                    Geography.sharedInstance().needRegions(countryID);
                    Geography.sharedInstance().needCities(countryID);
                    Geography.sharedInstance().needDistricts(countryID);
                    Geography.sharedInstance().needZips(countryID);
                    Geography.sharedInstance().needLocations(countryID);
                    data = CSV.getCSVData(pm, Organisation.getRootOrganisationID(initialContext), csvType, countryID);
                }
                return data;
            } finally {
                initialContext.close();
            }
        } catch (NamingException x) {
            throw new RuntimeException(x);
        } finally {
            pm.close();
        }
    }

    /**
	 * @ejb.interface-method
	 * @ejb.permission role-name="_Guest_"
	 */
    public Object getGeographyObject(ObjectID objectID, String[] fetchGroups, int maxFetchDepth) {
        PersistenceManager pm = getPersistenceManager();
        try {
            pm.getFetchPlan().setMaxFetchDepth(maxFetchDepth);
            if (fetchGroups != null) pm.getFetchPlan().setGroups(fetchGroups);
            pm.getExtent(CSV.class);
            Object obj = pm.getObjectById(objectID, true);
            return pm.detachCopy(obj);
        } finally {
            pm.close();
        }
    }
}
