package com.liferay.portal.service.ejb;

import com.liferay.portal.service.spring.ContactService;
import com.liferay.portal.spring.util.SpringUtil;
import org.springframework.context.ApplicationContext;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * <a href="ContactServiceEJBImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ContactServiceEJBImpl implements ContactService, SessionBean {

    public static final String CLASS_NAME = ContactService.class.getName() + ".transaction";

    public static ContactService getService() {
        ApplicationContext ctx = SpringUtil.getContext();
        return (ContactService) ctx.getBean(CLASS_NAME);
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
