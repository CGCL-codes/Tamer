package com.ohioedge.j2ee.api.org.proc.ejb;

import java.rmi.*;
import javax.ejb.*;

/**
 * @(#)ActivityTypeHierarchyManagerHome.java	1.3.1 10/15/2002
 * @version 1.3.1
 * @since Ohioedge Component API 1.2
 */
public interface ActivityTypeHierarchyManagerHome extends EJBHome {

    ActivityTypeHierarchyManager create() throws CreateException, RemoteException;
}
