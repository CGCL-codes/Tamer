package com.liferay.portal.service.ejb;

import com.liferay.portal.service.spring.ContactLocalService;
import com.liferay.portal.spring.util.SpringUtil;
import org.springframework.context.ApplicationContext;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * <a href="ContactLocalServiceEJBImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ContactLocalServiceEJBImpl implements ContactLocalService, SessionBean {

    public static final String CLASS_NAME = ContactLocalService.class.getName() + ".transaction";

    public static ContactLocalService getService() {
        ApplicationContext ctx = SpringUtil.getContext();
        return (ContactLocalService) ctx.getBean(CLASS_NAME);
    }

    public com.liferay.portal.model.Contact getContact(java.lang.String contactId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getContact(contactId);
    }

    public void deleteContact(java.lang.String contactId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().deleteContact(contactId);
    }

    public void deleteContact(com.liferay.portal.model.Contact contact) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().deleteContact(contact);
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
