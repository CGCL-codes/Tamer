package com.liferay.portlet.expando.service.impl;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.service.base.ExpandoTableServiceBaseImpl;
import java.util.List;

/**
 * <a href="ExpandoTableServiceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Raymond Augé
 * @author Brian Wing Shun Chan
 *
 */
public class ExpandoTableServiceImpl extends ExpandoTableServiceBaseImpl {

    public ExpandoTable addDefaultTable(String className) throws PortalException, SystemException {
        return expandoTableLocalService.addDefaultTable(className);
    }

    public ExpandoTable addDefaultTable(long classNameId) throws PortalException, SystemException {
        return expandoTableLocalService.addDefaultTable(classNameId);
    }

    public ExpandoTable addTable(String className, String name) throws PortalException, SystemException {
        return expandoTableLocalService.addTable(className, name);
    }

    public ExpandoTable addTable(long classNameId, String name) throws PortalException, SystemException {
        return expandoTableLocalService.addTable(classNameId, name);
    }

    public void deleteTable(long tableId) throws PortalException, SystemException {
        expandoTableLocalService.deleteTable(tableId);
    }

    public void deleteTable(String className, String name) throws PortalException, SystemException {
        expandoTableLocalService.deleteTable(className, name);
    }

    public void deleteTable(long classNameId, String name) throws PortalException, SystemException {
        expandoTableLocalService.deleteTable(classNameId, name);
    }

    public void deleteTables(String className) throws PortalException, SystemException {
        expandoTableLocalService.deleteTables(className);
    }

    public void deleteTables(long classNameId) throws PortalException, SystemException {
        expandoTableLocalService.deleteTables(classNameId);
    }

    public ExpandoTable getDefaultTable(String className) throws PortalException, SystemException {
        return expandoTableLocalService.getDefaultTable(className);
    }

    public ExpandoTable getDefaultTable(long classNameId) throws PortalException, SystemException {
        return expandoTableLocalService.getDefaultTable(classNameId);
    }

    public ExpandoTable getTable(long tableId) throws PortalException, SystemException {
        return expandoTableLocalService.getTable(tableId);
    }

    public ExpandoTable getTable(String className, String name) throws PortalException, SystemException {
        return expandoTableLocalService.getTable(className, name);
    }

    public ExpandoTable getTable(long classNameId, String name) throws PortalException, SystemException {
        return expandoTableLocalService.getTable(classNameId, name);
    }

    public List<ExpandoTable> getTables(String className) throws PortalException, SystemException {
        return expandoTableLocalService.getTables(className);
    }

    public List<ExpandoTable> getTables(long classNameId) throws PortalException, SystemException {
        return expandoTableLocalService.getTables(classNameId);
    }

    public ExpandoTable updateTable(long tableId, String name) throws PortalException, SystemException {
        return expandoTableLocalService.updateTable(tableId, name);
    }
}
