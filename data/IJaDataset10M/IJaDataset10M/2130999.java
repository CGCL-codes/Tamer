package com.liferay.portal.service.spring;

/**
 * <a href="CompanyService.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public interface CompanyService {

    public com.liferay.portal.model.Company updateCompany(java.lang.String companyId, java.lang.String portalURL, java.lang.String homeURL, java.lang.String mx, java.lang.String name, java.lang.String legalName, java.lang.String legalId, java.lang.String legalType, java.lang.String sicCode, java.lang.String tickerSymbol, java.lang.String industry, java.lang.String type, java.lang.String size) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void updateDisplay(java.lang.String companyId, java.lang.String languageId, java.lang.String timeZoneId, java.lang.String resolution) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void updateLogo(java.lang.String companyId, java.io.File file) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;

    public void updateSecurity(java.lang.String companyId, java.lang.String authType, boolean autoLogin, boolean strangers) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException;
}
