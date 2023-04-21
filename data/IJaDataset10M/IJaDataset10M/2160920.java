package org.redwood.business.usermanagement.reportsummary;

import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * The EJBHome interface.
 * This interface is for extending the EJBHome.
 *
 * @author  Gerrit Franke
 * @version 1.0
 */
public interface ReportSummaryHome extends EJBHome {

    public static final String COMP_NAME = "ReportSummary";

    public static final String JNDI_NAME = "ReportSummary";

    /**
   * Creates the ReportSummary.
   *
   * @return    the EJBObject.
   * @exception RemoteException, CreateException.
   */
    public ReportSummaryObject create(String id, String reportID, int periodLength, int periodUnit, int lastDate, int measureIntervalLength, int measureIntervalUnit, int position, boolean compare, boolean sum, boolean average, boolean max, boolean min) throws RemoteException, CreateException;

    /**
   * Finds the ReportSummary by its primary key.
   *
   * @return    the EJBObject.
   * @exception RemoteException, FinderException.
   */
    public ReportSummaryObject findByPrimaryKey(ReportSummaryPK pk) throws RemoteException, FinderException;

    /**
   *  Find the ReportSummaries by their report id.
   *
   *  @param rw_reportID - The report id.
   *  @return    The Collection of objects.
   *  @exception RemoteException, FinderException.
   */
    public Collection findByRw_reportID(String rw_reportID) throws RemoteException, FinderException;
}
