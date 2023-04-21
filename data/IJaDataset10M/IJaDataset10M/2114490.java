package com.liferay.portal.service.spring;

/**
 * <a href="OrgLaborLocalService.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public interface OrgLaborLocalService {

    public com.liferay.portal.model.OrgLabor addOrgLabor(java.lang.String organizationId, java.lang.String typeId, int sunOpen, int sunClose, int monOpen, int monClose, int tueOpen, int tueClose, int wedOpen, int wedClose, int thuOpen, int thuClose, int friOpen, int friClose, int satOpen, int satClose) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public void deleteOrgLabor(java.lang.String orgLaborId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public com.liferay.portal.model.OrgLabor getOrgLabor(java.lang.String orgLaborId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;

    public java.util.List getOrgLabors(java.lang.String organizationId) throws com.liferay.portal.SystemException;

    public com.liferay.portal.model.OrgLabor updateOrgLabor(java.lang.String orgLaborId, int sunOpen, int sunClose, int monOpen, int monClose, int tueOpen, int tueClose, int wedOpen, int wedClose, int thuOpen, int thuClose, int friOpen, int friClose, int satOpen, int satClose) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException;
}
