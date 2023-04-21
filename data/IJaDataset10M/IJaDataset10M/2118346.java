package com.liferay.portal.service.persistence;

/**
 * <a href="CountryUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class CountryUtil {

    public static com.liferay.portal.model.Country create(long countryId) {
        return getPersistence().create(countryId);
    }

    public static com.liferay.portal.model.Country remove(long countryId) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchCountryException {
        return getPersistence().remove(countryId);
    }

    public static com.liferay.portal.model.Country remove(com.liferay.portal.model.Country country) throws com.liferay.portal.SystemException {
        return getPersistence().remove(country);
    }

    public static com.liferay.portal.model.Country update(com.liferay.portal.model.Country country) throws com.liferay.portal.SystemException {
        return getPersistence().update(country);
    }

    public static com.liferay.portal.model.Country update(com.liferay.portal.model.Country country, boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().update(country, merge);
    }

    public static com.liferay.portal.model.Country updateImpl(com.liferay.portal.model.Country country, boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(country, merge);
    }

    public static com.liferay.portal.model.Country findByPrimaryKey(long countryId) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchCountryException {
        return getPersistence().findByPrimaryKey(countryId);
    }

    public static com.liferay.portal.model.Country fetchByPrimaryKey(long countryId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(countryId);
    }

    public static java.util.List findByActive(boolean active) throws com.liferay.portal.SystemException {
        return getPersistence().findByActive(active);
    }

    public static java.util.List findByActive(boolean active, int begin, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findByActive(active, begin, end);
    }

    public static java.util.List findByActive(boolean active, int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findByActive(active, begin, end, obc);
    }

    public static com.liferay.portal.model.Country findByActive_First(boolean active, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchCountryException {
        return getPersistence().findByActive_First(active, obc);
    }

    public static com.liferay.portal.model.Country findByActive_Last(boolean active, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchCountryException {
        return getPersistence().findByActive_Last(active, obc);
    }

    public static com.liferay.portal.model.Country[] findByActive_PrevAndNext(long countryId, boolean active, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchCountryException {
        return getPersistence().findByActive_PrevAndNext(countryId, active, obc);
    }

    public static java.util.List findWithDynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer) throws com.liferay.portal.SystemException {
        return getPersistence().findWithDynamicQuery(queryInitializer);
    }

    public static java.util.List findWithDynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer, int begin, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findWithDynamicQuery(queryInitializer, begin, end);
    }

    public static java.util.List findAll() throws com.liferay.portal.SystemException {
        return getPersistence().findAll();
    }

    public static java.util.List findAll(int begin, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(begin, end);
    }

    public static java.util.List findAll(int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findAll(begin, end, obc);
    }

    public static void removeByActive(boolean active) throws com.liferay.portal.SystemException {
        getPersistence().removeByActive(active);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByActive(boolean active) throws com.liferay.portal.SystemException {
        return getPersistence().countByActive(active);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static CountryPersistence getPersistence() {
        return _getUtil()._persistence;
    }

    public void setPersistence(CountryPersistence persistence) {
        _persistence = persistence;
    }

    private static CountryUtil _getUtil() {
        if (_util == null) {
            _util = (CountryUtil) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_UTIL);
        }
        return _util;
    }

    private static final String _UTIL = CountryUtil.class.getName();

    private static CountryUtil _util;

    private CountryPersistence _persistence;
}
