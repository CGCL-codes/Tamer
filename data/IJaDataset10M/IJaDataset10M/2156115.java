package org.redwood.business.usermanagement.reportmeasures.reportlistmeasure;

import org.redwood.business.usermanagement.reportmeasures.reportmeasure.*;
import java.rmi.RemoteException;

/**
 *
 * @author  Gerrit Franke
 * @version 1.0
 */
public interface ReportListMeasure extends ReportMeasure {

    public int getRw_nbPositions() throws RemoteException;

    public void setRw_nbPositions(int nbPositions) throws RemoteException;
}
