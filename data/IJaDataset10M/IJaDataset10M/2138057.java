package com.liferay.portlet.expando.service;

/**
 * <a href="ExpandoTableLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.liferay.portlet.expando.service.ExpandoTableLocalService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.expando.service.ExpandoTableLocalService
 *
 */
public class ExpandoTableLocalServiceUtil {

    public static com.liferay.portlet.expando.model.ExpandoTable addExpandoTable(com.liferay.portlet.expando.model.ExpandoTable expandoTable) throws com.liferay.portal.SystemException {
        return getService().addExpandoTable(expandoTable);
    }

    public static com.liferay.portlet.expando.model.ExpandoTable createExpandoTable(long tableId) {
        return getService().createExpandoTable(tableId);
    }

    public static void deleteExpandoTable(long tableId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().deleteExpandoTable(tableId);
    }

    public static void deleteExpandoTable(com.liferay.portlet.expando.model.ExpandoTable expandoTable) throws com.liferay.portal.SystemException {
        getService().deleteExpandoTable(expandoTable);
    }

    public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery);
    }

    public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery, start, end);
    }

    public static com.liferay.portlet.expando.model.ExpandoTable getExpandoTable(long tableId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getExpandoTable(tableId);
    }

    public static java.util.List<com.liferay.portlet.expando.model.ExpandoTable> getExpandoTables(int start, int end) throws com.liferay.portal.SystemException {
        return getService().getExpandoTables(start, end);
    }

    public static int getExpandoTablesCount() throws com.liferay.portal.SystemException {
        return getService().getExpandoTablesCount();
    }

    public static com.liferay.portlet.expando.model.ExpandoTable updateExpandoTable(com.liferay.portlet.expando.model.ExpandoTable expandoTable) throws com.liferay.portal.SystemException {
        return getService().updateExpandoTable(expandoTable);
    }

    public static com.liferay.portlet.expando.model.ExpandoTable addDefaultTable(java.lang.String className) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().addDefaultTable(className);
    }

    public static com.liferay.portlet.expando.model.ExpandoTable addDefaultTable(long classNameId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().addDefaultTable(classNameId);
    }

    public static com.liferay.portlet.expando.model.ExpandoTable addTable(java.lang.String className, java.lang.String name) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().addTable(className, name);
    }

    public static com.liferay.portlet.expando.model.ExpandoTable addTable(long classNameId, java.lang.String name) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().addTable(classNameId, name);
    }

    public static void deleteTable(long tableId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().deleteTable(tableId);
    }

    public static void deleteTable(java.lang.String className, java.lang.String name) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().deleteTable(className, name);
    }

    public static void deleteTable(long classNameId, java.lang.String name) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().deleteTable(classNameId, name);
    }

    public static void deleteTables(java.lang.String className) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().deleteTables(className);
    }

    public static void deleteTables(long classNameId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().deleteTables(classNameId);
    }

    public static com.liferay.portlet.expando.model.ExpandoTable getDefaultTable(java.lang.String className) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getDefaultTable(className);
    }

    public static com.liferay.portlet.expando.model.ExpandoTable getDefaultTable(long classNameId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getDefaultTable(classNameId);
    }

    public static com.liferay.portlet.expando.model.ExpandoTable getTable(long tableId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getTable(tableId);
    }

    public static com.liferay.portlet.expando.model.ExpandoTable getTable(java.lang.String className, java.lang.String name) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getTable(className, name);
    }

    public static com.liferay.portlet.expando.model.ExpandoTable getTable(long classNameId, java.lang.String name) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getTable(classNameId, name);
    }

    public static java.util.List<com.liferay.portlet.expando.model.ExpandoTable> getTables(java.lang.String className) throws com.liferay.portal.SystemException {
        return getService().getTables(className);
    }

    public static java.util.List<com.liferay.portlet.expando.model.ExpandoTable> getTables(long classNameId) throws com.liferay.portal.SystemException {
        return getService().getTables(classNameId);
    }

    public static com.liferay.portlet.expando.model.ExpandoTable updateTable(long tableId, java.lang.String name) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().updateTable(tableId, name);
    }

    public static ExpandoTableLocalService getService() {
        if (_service == null) {
            throw new RuntimeException("ExpandoTableLocalService is not set");
        }
        return _service;
    }

    public void setService(ExpandoTableLocalService service) {
        _service = service;
    }

    private static ExpandoTableLocalService _service;
}
