package com.liferay.portal.service.persistence;

/**
 * <a href="PortletPreferencesUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PortletPreferencesUtil {

    public static com.liferay.portal.model.PortletPreferences create(long portletPreferencesId) {
        return getPersistence().create(portletPreferencesId);
    }

    public static com.liferay.portal.model.PortletPreferences remove(long portletPreferencesId) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchPortletPreferencesException {
        return getPersistence().remove(portletPreferencesId);
    }

    public static com.liferay.portal.model.PortletPreferences remove(com.liferay.portal.model.PortletPreferences portletPreferences) throws com.liferay.portal.SystemException {
        return getPersistence().remove(portletPreferences);
    }

    public static com.liferay.portal.model.PortletPreferences update(com.liferay.portal.model.PortletPreferences portletPreferences) throws com.liferay.portal.SystemException {
        return getPersistence().update(portletPreferences);
    }

    public static com.liferay.portal.model.PortletPreferences update(com.liferay.portal.model.PortletPreferences portletPreferences, boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().update(portletPreferences, merge);
    }

    public static com.liferay.portal.model.PortletPreferences updateImpl(com.liferay.portal.model.PortletPreferences portletPreferences, boolean merge) throws com.liferay.portal.SystemException {
        return getPersistence().updateImpl(portletPreferences, merge);
    }

    public static com.liferay.portal.model.PortletPreferences findByPrimaryKey(long portletPreferencesId) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchPortletPreferencesException {
        return getPersistence().findByPrimaryKey(portletPreferencesId);
    }

    public static com.liferay.portal.model.PortletPreferences fetchByPrimaryKey(long portletPreferencesId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByPrimaryKey(portletPreferencesId);
    }

    public static java.util.List findByPlid(long plid) throws com.liferay.portal.SystemException {
        return getPersistence().findByPlid(plid);
    }

    public static java.util.List findByPlid(long plid, int begin, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findByPlid(plid, begin, end);
    }

    public static java.util.List findByPlid(long plid, int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findByPlid(plid, begin, end, obc);
    }

    public static com.liferay.portal.model.PortletPreferences findByPlid_First(long plid, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchPortletPreferencesException {
        return getPersistence().findByPlid_First(plid, obc);
    }

    public static com.liferay.portal.model.PortletPreferences findByPlid_Last(long plid, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchPortletPreferencesException {
        return getPersistence().findByPlid_Last(plid, obc);
    }

    public static com.liferay.portal.model.PortletPreferences[] findByPlid_PrevAndNext(long portletPreferencesId, long plid, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchPortletPreferencesException {
        return getPersistence().findByPlid_PrevAndNext(portletPreferencesId, plid, obc);
    }

    public static java.util.List findByO_O_P(long ownerId, int ownerType, long plid) throws com.liferay.portal.SystemException {
        return getPersistence().findByO_O_P(ownerId, ownerType, plid);
    }

    public static java.util.List findByO_O_P(long ownerId, int ownerType, long plid, int begin, int end) throws com.liferay.portal.SystemException {
        return getPersistence().findByO_O_P(ownerId, ownerType, plid, begin, end);
    }

    public static java.util.List findByO_O_P(long ownerId, int ownerType, long plid, int begin, int end, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException {
        return getPersistence().findByO_O_P(ownerId, ownerType, plid, begin, end, obc);
    }

    public static com.liferay.portal.model.PortletPreferences findByO_O_P_First(long ownerId, int ownerType, long plid, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchPortletPreferencesException {
        return getPersistence().findByO_O_P_First(ownerId, ownerType, plid, obc);
    }

    public static com.liferay.portal.model.PortletPreferences findByO_O_P_Last(long ownerId, int ownerType, long plid, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchPortletPreferencesException {
        return getPersistence().findByO_O_P_Last(ownerId, ownerType, plid, obc);
    }

    public static com.liferay.portal.model.PortletPreferences[] findByO_O_P_PrevAndNext(long portletPreferencesId, long ownerId, int ownerType, long plid, com.liferay.portal.kernel.util.OrderByComparator obc) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchPortletPreferencesException {
        return getPersistence().findByO_O_P_PrevAndNext(portletPreferencesId, ownerId, ownerType, plid, obc);
    }

    public static com.liferay.portal.model.PortletPreferences findByO_O_P_P(long ownerId, int ownerType, long plid, java.lang.String portletId) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchPortletPreferencesException {
        return getPersistence().findByO_O_P_P(ownerId, ownerType, plid, portletId);
    }

    public static com.liferay.portal.model.PortletPreferences fetchByO_O_P_P(long ownerId, int ownerType, long plid, java.lang.String portletId) throws com.liferay.portal.SystemException {
        return getPersistence().fetchByO_O_P_P(ownerId, ownerType, plid, portletId);
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

    public static void removeByPlid(long plid) throws com.liferay.portal.SystemException {
        getPersistence().removeByPlid(plid);
    }

    public static void removeByO_O_P(long ownerId, int ownerType, long plid) throws com.liferay.portal.SystemException {
        getPersistence().removeByO_O_P(ownerId, ownerType, plid);
    }

    public static void removeByO_O_P_P(long ownerId, int ownerType, long plid, java.lang.String portletId) throws com.liferay.portal.SystemException, com.liferay.portal.NoSuchPortletPreferencesException {
        getPersistence().removeByO_O_P_P(ownerId, ownerType, plid, portletId);
    }

    public static void removeAll() throws com.liferay.portal.SystemException {
        getPersistence().removeAll();
    }

    public static int countByPlid(long plid) throws com.liferay.portal.SystemException {
        return getPersistence().countByPlid(plid);
    }

    public static int countByO_O_P(long ownerId, int ownerType, long plid) throws com.liferay.portal.SystemException {
        return getPersistence().countByO_O_P(ownerId, ownerType, plid);
    }

    public static int countByO_O_P_P(long ownerId, int ownerType, long plid, java.lang.String portletId) throws com.liferay.portal.SystemException {
        return getPersistence().countByO_O_P_P(ownerId, ownerType, plid, portletId);
    }

    public static int countAll() throws com.liferay.portal.SystemException {
        return getPersistence().countAll();
    }

    public static PortletPreferencesPersistence getPersistence() {
        return _getUtil()._persistence;
    }

    public void setPersistence(PortletPreferencesPersistence persistence) {
        _persistence = persistence;
    }

    private static PortletPreferencesUtil _getUtil() {
        if (_util == null) {
            _util = (PortletPreferencesUtil) com.liferay.portal.kernel.bean.BeanLocatorUtil.locate(_UTIL);
        }
        return _util;
    }

    private static final String _UTIL = PortletPreferencesUtil.class.getName();

    private static PortletPreferencesUtil _util;

    private PortletPreferencesPersistence _persistence;
}
