package com.liferay.portlet.polls.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * <a href="PollsChoiceManagerHome.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.9 $
 *
 */
public interface PollsChoiceManagerHome extends EJBHome {

    public PollsChoiceManagerEJB create() throws CreateException, RemoteException;
}
