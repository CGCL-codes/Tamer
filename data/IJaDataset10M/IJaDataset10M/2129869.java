package ru.dreamjteam.session;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 *
 * @author диман
 */
public interface PutToLogLocalHome extends EJBLocalHome {

    ru.dreamjteam.session.PutToLogLocal create() throws CreateException;
}
