package com.liferay.portlet.softwarecatalog.service.ejb;

import com.liferay.portlet.softwarecatalog.service.SCLicenseLocalService;
import com.liferay.portlet.softwarecatalog.service.SCLicenseLocalServiceFactory;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * <a href="SCLicenseLocalServiceEJBImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SCLicenseLocalServiceEJBImpl implements SCLicenseLocalService, SessionBean {

    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer) throws com.liferay.portal.SystemException {
        return SCLicenseLocalServiceFactory.getTxImpl().dynamicQuery(queryInitializer);
    }

    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer, int begin, int end) throws com.liferay.portal.SystemException {
        return SCLicenseLocalServiceFactory.getTxImpl().dynamicQuery(queryInitializer, begin, end);
    }

    public com.liferay.portlet.softwarecatalog.model.SCLicense addLicense(java.lang.String name, java.lang.String url, boolean openSource, boolean active, boolean recommended) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return SCLicenseLocalServiceFactory.getTxImpl().addLicense(name, url, openSource, active, recommended);
    }

    public void deleteLicense(long licenseId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        SCLicenseLocalServiceFactory.getTxImpl().deleteLicense(licenseId);
    }

    public com.liferay.portlet.softwarecatalog.model.SCLicense getLicense(long licenseId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return SCLicenseLocalServiceFactory.getTxImpl().getLicense(licenseId);
    }

    public java.util.List getLicenses() throws com.liferay.portal.SystemException {
        return SCLicenseLocalServiceFactory.getTxImpl().getLicenses();
    }

    public java.util.List getLicenses(int begin, int end) throws com.liferay.portal.SystemException {
        return SCLicenseLocalServiceFactory.getTxImpl().getLicenses(begin, end);
    }

    public java.util.List getLicenses(boolean active, boolean recommended) throws com.liferay.portal.SystemException {
        return SCLicenseLocalServiceFactory.getTxImpl().getLicenses(active, recommended);
    }

    public java.util.List getLicenses(boolean active, boolean recommended, int begin, int end) throws com.liferay.portal.SystemException {
        return SCLicenseLocalServiceFactory.getTxImpl().getLicenses(active, recommended, begin, end);
    }

    public int getLicensesCount() throws com.liferay.portal.SystemException {
        return SCLicenseLocalServiceFactory.getTxImpl().getLicensesCount();
    }

    public int getLicensesCount(boolean active, boolean recommended) throws com.liferay.portal.SystemException {
        return SCLicenseLocalServiceFactory.getTxImpl().getLicensesCount(active, recommended);
    }

    public java.util.List getProductEntryLicenses(long productEntryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return SCLicenseLocalServiceFactory.getTxImpl().getProductEntryLicenses(productEntryId);
    }

    public com.liferay.portlet.softwarecatalog.model.SCLicense updateLicense(long licenseId, java.lang.String name, java.lang.String url, boolean openSource, boolean active, boolean recommended) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return SCLicenseLocalServiceFactory.getTxImpl().updateLicense(licenseId, name, url, openSource, active, recommended);
    }

    public void ejbCreate() throws CreateException {
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public SessionContext getSessionContext() {
        return _sc;
    }

    public void setSessionContext(SessionContext sc) {
        _sc = sc;
    }

    private SessionContext _sc;
}
