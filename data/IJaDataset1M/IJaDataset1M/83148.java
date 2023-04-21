package org.codecompany.jeha.example.ejb;

import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import org.codecompany.jeha.ExceptionHandler;
import org.codecompany.jeha.example.ejb.handler.EjbHandler;

public class JehaTestBean implements SessionBean {

    private static final long serialVersionUID = 7798123302101540446L;

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }

    public void ejbCreate() throws EJBException {
    }

    public void setSessionContext(SessionContext context) throws EJBException, RemoteException {
    }

    @ExceptionHandler(handler = EjbHandler.class)
    public void execute(String param) {
        if ("generalEJB".equals(param)) {
            throw new RuntimeException("Runtime exception");
        } else if ("specificEJB".equals(param)) {
            throw new ArrayIndexOutOfBoundsException("Array index out of bound exception");
        }
    }
}
