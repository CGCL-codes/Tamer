package com.liferay.portlet.ratings.service.ejb;

import com.liferay.portal.service.impl.PrincipalSessionBean;
import com.liferay.portlet.ratings.service.RatingsEntryService;
import com.liferay.portlet.ratings.service.RatingsEntryServiceFactory;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * <a href="RatingsEntryServiceEJBImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class RatingsEntryServiceEJBImpl implements RatingsEntryService, SessionBean {

    public com.liferay.portlet.ratings.model.RatingsEntry updateEntry(java.lang.String className, java.lang.String classPK, double score) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        PrincipalSessionBean.setThreadValues(_sc);
        return RatingsEntryServiceFactory.getTxImpl().updateEntry(className, classPK, score);
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
