package org.redwood.business.report.reportgeneration.specificationsgroups.valuesmeasurespecificationsgroup;

import org.redwood.business.report.reportgeneration.specificationsgroups.specificationsgroup.SpecificationsGroup;
import org.redwood.business.report.reportgeneration.specificationsgroups.exceptions.*;
import org.redwood.business.report.util.TimePeriod;
import java.util.Vector;
import java.rmi.RemoteException;

/**
 *
 * @author  Gerrit Franke
 * @version 1.0
 */
public interface ValuesMeasureSpecificationsGroup extends SpecificationsGroup {

    public void addValuesMeasureSeries(String seriesName, String webSiteGroupID, TimePeriod seriesTimePeriod) throws RemoteException, DuplicateSeriesNameException;

    public void pushbackSpecificationString(String specificationString) throws RemoteException;

    public void pushbackSeriesSpecification(String seriesName, TimePeriod seriesSpecificationTimePeriod) throws RemoteException, SeriesNotFoundException;

    public void pushbackSeriesValue(String seriesName, double seriesValue) throws RemoteException, SeriesNotFoundException;
}
