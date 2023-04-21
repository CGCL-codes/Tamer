package org.redwood.business.report.measureaggregation.summeasureaggregation;

import org.redwood.business.report.measureaggregation.exceptions.*;
import java.rmi.RemoteException;
import java.util.*;

/**
 *
 * @author  Gerrit Franke
 * @version 1.0
 */
public interface SumMeasureAggregation {

    public double getValue(String measureID, Date beginDate, Date endDate, Vector webSiteIDs) throws java.rmi.RemoteException, NoDataFoundException;
}
