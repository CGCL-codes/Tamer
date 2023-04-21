/**
 * Title:        Redwood
 * Description:  Workflow Object
 * Copyright:    Copyright (c) Gerrit Franke
 * Company:      University of Muenster
 * @author       Gerrit Franke
 * @version 1.0
 */
package org.redwood.business.report.reportgeneration.reportgenerationmeasures.reportgenerationlistmeasures;

import org.redwood.business.report.reportgeneration.reportgenerationmeasures.reportgenerationmeasure.*;
import org.redwood.business.report.reportgeneration.reportgenerationmeasures.exceptions.*;
import org.redwood.business.report.reportgeneration.specificationsgroups.listmeasurespecificationsgroup.*;
import org.redwood.business.report.reportgeneration.specificationsgroups.specificationsgroup.*;
import org.redwood.business.report.reportgeneration.specificationsgroups.exceptions.*;
import org.redwood.business.report.measureaggregation.listmeasureaggregation.*;
import org.redwood.business.report.measureaggregation.exceptions.*;
import org.redwood.business.usermanagement.reportmeasures.reportlistmeasure.*;
import org.redwood.business.report.util.*;

import java.util.*;
import java.rmi.RemoteException;
import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;
import javax.transaction.UserTransaction;


/**
 * The  bean class.
 * This class extends the ReportGenerationBean.
 * It handles the retrieval and the temporary storage of data of SumMeasures.
 * Therefor it makes use of the ListMeasureAggregationBean and stores the
 * retrieved data in a ListMeasureSpecificationsGroup and its
 * ListMeasureSeries.
 * @author  Gerrit Franke
 * @version 1.0
 */
public class ReportGenerationListMeasureBean extends ReportGenerationMeasureBean{

  /** The maximal number of the positions of this ListMeasure.
    */
  protected int maxPositions = 0;
  
  /** Contains the keys of the HashMap returned by the AggregationBean.
   */
  protected String[] keys;
  
  /** Contains the values of the HashMap returned by the AggregationBean.
   */
  protected double[] values;
  
  /*========================= ReportGenerationListMeasure Workflow Objects implementation ============================*/

  /** This methodes retrieves the necessary data out of the database. 
   *  Therefore the different time periods are calculated and the data is 
   *  queried with the help of the ListMeasureAggregationBeans.
   *  @exception NoDataException - Thrown if no data at all was found for
   *                               the given time period, measure and web sites
   *  @exception RemoteException - Thrown if the instance could not perform the 
   *                              function requested.
   */
  public void loadData() throws RemoteException, NoDataException {
    // Convert general reportMeasure to ReportValuesMeasure
    ReportListMeasureObject reportMeasureObject = 
      (ReportListMeasureObject) reportMeasure;
    // retrieve measureID
    measureID = reportMeasureObject.getRw_measureID();
    // retrieve maxPositions of the list
    maxPositions = reportMeasureObject.getRw_nbPositions();

    // Create SpecificationsGroup to save retrieved values in
    ListMeasureSpecificationsGroupObject specsGroup = null;
    try {
      // get context
      Context initialContext = new InitialContext();
      // get home
      ListMeasureSpecificationsGroupHome specsGroupHome = (ListMeasureSpecificationsGroupHome)
        PortableRemoteObject.narrow(
          initialContext.lookup(ListMeasureSpecificationsGroupHome.COMP_NAME),
          ListMeasureSpecificationsGroupHome.class);
      // Create new stateful session bean to work with
      specsGroup = specsGroupHome.create();
    }
    catch(Exception e){
      e.printStackTrace();
    }
 
    // calculate seriesTimePeriods
    Vector seriesTimePeriods = calculateSeriesTimePeriods(reportMeasureObject);    

    // create new dateParser to set seriesNames
    DateParser dateParser = new DateParser(seriesPeriodUnit, periodUnit);    

    // create ListMeasureAggregationBean
    ListMeasureAggregationObject aggregationObject = null;
    try {
      // get context
      Context initialContext = new InitialContext();
      // get home
      ListMeasureAggregationHome aggregationHome = (ListMeasureAggregationHome)
        PortableRemoteObject.narrow(
          initialContext.lookup(ListMeasureAggregationHome.COMP_NAME),
          ListMeasureAggregationHome.class);
      // Create new stateful session bean to work with
      aggregationObject = aggregationHome.create();
    }
    catch(Exception e){
      e.printStackTrace();
    }    
    
    // create measure series of the specificationsgroups
    // and add the specifications to the specificationsgroup and the 
    // measure series values to the measure series
    boolean dataFound = false;
    TimePeriod timePeriod;
    String seriesNamePart1, seriesName;
    String webSiteGroupName;
    HashMap specificationsAndValues;
    Vector sortedSpecifications;
    try{
      Enumeration enumTPs = seriesTimePeriods.elements();
      // For each series time period
      while (enumTPs.hasMoreElements()){
        // get next series' time period
        timePeriod = (TimePeriod)enumTPs.nextElement();
        // set 1. part of series' name
        seriesNamePart1 = 
          dateParser.parseDate(timePeriod.getBeginDate().getTime()) + " - " +
          dateParser.parseDate(timePeriod.getEndDate().getTime());
        // for each webSiteGroup
        // the two while statements result in: for each measure series
        Enumeration enumWSGs = webSiteGroupNames.elements();
        while (enumWSGs.hasMoreElements()){
          webSiteGroupName = (String)enumWSGs.nextElement();
          // append to seriesName (2. part)
          seriesName = seriesNamePart1 + webSiteGroupName;
          // add measureSeries to SpecificatonsGroup
          specsGroup.addListMeasureSeries(seriesName,
            (String)webSiteGroupIDs.get(webSiteGroupNames.indexOf(webSiteGroupName)),
            timePeriod);
//          System.out.println("RGLM seriesName " +  seriesName);
          // get the HashMap representing the specifcations and Values of the
          // measure series
          try{
            specificationsAndValues =
            aggregationObject.getSpecificationsAndValues(measureID,
              timePeriod.getBeginDate().getTime(),
              timePeriod.getEndDate().getTime(),
              maxPositions, // correct it ??
              getWebSiteIDs((String)webSiteGroupIDs.get(
              webSiteGroupNames.indexOf(webSiteGroupName))));
            
            dataFound = true;
          }
          catch(NoDataFoundException dfe){
            // use empty Hash Map to continue
            specificationsAndValues = new HashMap();
          }
          // sort the specifications by their values and store sorted results
          // in the vectors keys and values of this bean
          sortedSpecifications = sort(specificationsAndValues);
          // Add specifications and values to the specifications group
          // for each key/specification in the vector
//          System.out.println("RGLM Specification and Values: " + specificationsAndValues.size());
          String specification;
          Enumeration enum = sortedSpecifications.elements();
          while(enum.hasMoreElements()){
            specification = (String) enum.nextElement();
            try{
              // if list not full yet
              if (maxPositions > 0){
                specsGroup.addSpecification(specification);
                // one more list entry
                maxPositions--;
              }
            // try to set specificationValue if specification exists already
              specsGroup.setSeriesValue(specification, seriesName, 
                ((Double)specificationsAndValues.get(specification)).doubleValue());

            }catch(DuplicateSpecificationStringException e){
              // nothing to do
            }
            catch(SpecificationStringNotFoundException se){
              // nothing to do
            }
            
//            System.out.println("RGLM Specification: " + specification);
            
          } // end while
          
        } // end while webSiteGroupIDs
        
      } // end while series

      // save specification in attribute
      specsGroupObject = specsGroup;
    }
    catch(Exception e){
      e.printStackTrace();
    }    

    // retrieve maxPositions again, because it is changed while loading the data
    // and needed by getAnnotations
    maxPositions = reportMeasureObject.getRw_nbPositions();
    
    if (!dataFound)
      throw new NoDataException();
    
  }  
  
  
  /** Sorts the specifications by their values and saves them in the vetors
   *  keys and values of this bean.
   *  
   *  @param HashMap - The specifications and values for this list measure
   *
   *  @return A vector containing the specifications sorted by their values
   */
  protected Vector sort(HashMap specificationsAndValues){
    // copy HashMap entries into arrays to sort them
    int index = 0;
    Object key;
    Set keySet = specificationsAndValues.keySet();
    keys= new String[keySet.size()];
    values = new double[keySet.size()];
    Iterator iter = keySet.iterator();
    while (iter.hasNext()){
      key = iter.next();
      keys[index] = (String)key;
      values[index] = ((Double)specificationsAndValues.get(key)).doubleValue();
      index++;
    }
    
    // sort the values and keys arrays simultaneously by values in
    // ascending order
    index = 1;
    int helpIndex;
    double helpValue;
    String helpKey;
    // bubble sort
    while (index < values.length){
      helpIndex = index;
      while ((helpIndex > 0) && (values[helpIndex] > values[helpIndex-1])){
        // switch values
        helpValue = values[helpIndex];
        values[helpIndex] = values[helpIndex-1];
        values[helpIndex-1] = helpValue;
        // switch keys
        helpKey = keys[helpIndex];
        keys[helpIndex] = keys[helpIndex-1];
        keys[helpIndex-1] = helpKey;
        // decrement helpIndex
        helpIndex--;
      }
      index++;
    }
 
    // copy keys array into vector
    Vector keysVector = new Vector();
    for (int i = 0; i < keys.length; i++)
      keysVector.addElement(keys[i]);
    
    // return sorted keys vector
    return keysVector;
  }  // end sort

  /** Returns the annotations of this ReportGenerationMeasure.
   *  @return The annotations as String.
   *  @exception RemoteException - Thrown if the instance could not perform the 
   *                              function requested.
   */
  public String getAnnotations() throws RemoteException{
    // construct annotations out of configuration parameters
    String annotations = "List of the " + maxPositions + " most common " + getMeasureName() + " per ";
    // construct timeInterval String representation
    if (seriesPeriodLength > 1)
      annotations+= seriesPeriodLength + " ";
    annotations += getTimeUnitStringRepresentation(seriesPeriodUnit);
    if (seriesPeriodLength > 1)
      annotations += "s";
    annotations += ".";    
    return annotations;

  } 
}
