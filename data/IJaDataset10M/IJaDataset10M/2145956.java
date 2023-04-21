package com.liferay.portlet.polls.ejb;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import com.liferay.portal.auth.PrincipalException;
import com.liferay.portal.ejb.PrincipalSessionBean;

/**
 * <a href="PollsQuestionManagerEJBImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.2 $
 *
 */
public class PollsQuestionManagerEJBImpl extends PollsQuestionManagerImpl implements SessionBean {

    public String getUserId() throws PrincipalException {
        return PrincipalSessionBean.getUserId(_sc);
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
