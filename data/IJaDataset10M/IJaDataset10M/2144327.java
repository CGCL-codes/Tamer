package org.force4spring;

import java.rmi.RemoteException;
import org.springframework.dao.DataAccessResourceFailureException;

public class WebServicesCallException extends DataAccessResourceFailureException {

    public WebServicesCallException(RemoteException re) {
        super("Web Services call failed", re);
    }
}
