package com.liferay.portal.service.ejb;

import com.liferay.portal.service.spring.CompanyLocalService;
import com.liferay.portal.spring.util.SpringUtil;
import org.springframework.context.ApplicationContext;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * <a href="CompanyLocalServiceEJBImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class CompanyLocalServiceEJBImpl implements CompanyLocalService, SessionBean {

    public static final String CLASS_NAME = CompanyLocalService.class.getName() + ".transaction";

    public static CompanyLocalService getService() {
        ApplicationContext ctx = SpringUtil.getContext();
        return (CompanyLocalService) ctx.getBean(CLASS_NAME);
    }

    public void checkCompany(java.lang.String companyId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().checkCompany(companyId);
    }

    public void checkCompanyKey(java.lang.String companyId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().checkCompanyKey(companyId);
    }

    public java.util.List getCompanies() throws com.liferay.portal.SystemException {
        return getService().getCompanies();
    }

    public com.liferay.portal.model.Company getCompany(java.lang.String companyId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getCompany(companyId);
    }

    public com.liferay.util.lucene.Hits search(java.lang.String companyId, java.lang.String keywords) throws com.liferay.portal.SystemException {
        return getService().search(companyId, keywords);
    }

    public com.liferay.util.lucene.Hits search(java.lang.String companyId, java.lang.String portletId, java.lang.String groupId, java.lang.String keywords) throws com.liferay.portal.SystemException {
        return getService().search(companyId, portletId, groupId, keywords);
    }

    public com.liferay.portal.model.Company updateCompany(java.lang.String companyId, java.lang.String portalURL, java.lang.String homeURL, java.lang.String mx, java.lang.String name, java.lang.String legalName, java.lang.String legalId, java.lang.String legalType, java.lang.String sicCode, java.lang.String tickerSymbol, java.lang.String industry, java.lang.String type, java.lang.String size) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().updateCompany(companyId, portalURL, homeURL, mx, name, legalName, legalId, legalType, sicCode, tickerSymbol, industry, type, size);
    }

    public void updateDisplay(java.lang.String companyId, java.lang.String languageId, java.lang.String timeZoneId, java.lang.String resolution) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().updateDisplay(companyId, languageId, timeZoneId, resolution);
    }

    public void updateLogo(java.lang.String companyId, java.io.File file) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().updateLogo(companyId, file);
    }

    public void updateSecurity(java.lang.String companyId, java.lang.String authType, boolean autoLogin, boolean strangers) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().updateSecurity(companyId, authType, autoLogin, strangers);
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
