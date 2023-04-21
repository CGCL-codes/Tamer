package org.redwood.business.report.reportgeneration.specificationsgroups.classificationmeasurespecificationsgroup;

import org.redwood.business.report.reportgeneration.specificationsgroups.specificationsgroup.SpecificationsGroup;
import org.redwood.business.report.reportgeneration.specificationsgroups.exceptions.*;
import org.redwood.business.report.util.*;
import java.util.Vector;
import java.rmi.RemoteException;

/**
 *
 * @author  Gerrit Franke
 * @version 1.0
 */
public interface ClassificationMeasureSpecificationsGroup extends SpecificationsGroup {

    public void addClassificationMeasureSeries(String seriesName, String webSiteGroupID, TimePeriod seriesTimePeriod) throws RemoteException, DuplicateSeriesNameException;

    public void pushbackSpecification(String specificationString, ClassLimits classLimits) throws RemoteException;

    public void pushbackSeriesValue(String seriesName, double seriesValue) throws RemoteException, SeriesNotFoundException;
}
