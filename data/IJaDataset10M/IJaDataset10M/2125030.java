package org.redwood.business.report.charts.piechart;

import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * The EJBHome interface.
 *
 * @author  Gerrit Franke
 * @version 1.0
 */
public interface PieChartHome extends EJBHome {

    public static final String COMP_NAME = "PieChart";

    public static final String JNDI_NAME = "PieChart";

    /**
   * Creates the .
   *
   * @return    the  EJBObject.
   * @exception RemoteException.
   */
    public PieChartObject create(String chartTitle) throws RemoteException, CreateException;
}
