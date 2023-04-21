/**
 * Title:        Redwood
 * Description:  Workflow Object
 * Copyright:    Copyright (c) Gerrit Franke
 * Company:      University of Muenster
 * @author       Gerrit Franke
 * @version 1.0
 */

package org.redwood.business.report.reportgeneration.charthandler;

import org.redwood.business.report.charts.chart.*;
import org.redwood.business.report.charts.chartfactory.*;
import org.redwood.business.report.reportgeneration.reportsummarygeneration.*;
import org.redwood.business.report.reportgeneration.reportgenerationmeasures.reportgenerationmeasure.*;
import org.redwood.business.report.reportgeneration.specificationsgroups.specificationsgroup.*;
import org.redwood.business.report.reportgeneration.measuresseries.measureseries.*;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;
import javax.transaction.UserTransaction;

/**
 *  The bean class. This class creates charts for reportSummaries and
 *  reportMeasures which can be added to the report documents. The charts
 *  will be returned as byte array representations of a jpeg image.
 *
 *  @author  Gerrit Franke
 *  @version 1.0
 */
public class ChartHandlerBean implements SessionBean, ChartHandler {

  // Keep the reference on the SessionContext
  protected SessionContext sessionContext = null;
  
  /*========================= ejbCreate methods ============================*/

  /**
   * There must be one ejbCreate() method per create() method on the Home interface,
   * and with the same signature.
   *
   * @exception RemoteException If the instance could not perform the function
   *            requested by the container
   */
  public void ejbCreate() throws RemoteException {
  }

  /*====================== javax.ejb.SessionBean implementation =================*/

  /**
   * This method is called when the instance is activated from its "passive" state.
   * The instance should acquire any resource that it has released earlier in the
   * ejbPassivate() method.
   * This method is called with no transaction context.
   *
   * @exception RemoteException - Thrown if the instance could not perform the function
   *            requested by the container
   */
  public void ejbActivate() throws RemoteException {
    // Nothing to be done here.
  }

  /**
   * This method is called before the instance enters the "passive" state.
   * The instance should release any resources that it can re-acquire later in the
   * ejbActivate() method.
   * After the passivate method completes, the instance must be in a state that
   * allows the container to use the Java Serialization protocol to externalize
   * and store away the instance's state.
   * This method is called with no transaction context.
   *
   * @exception RemoteException - Thrown if the instance could not perform the function
   *            requested by the container
   */
  public void ejbPassivate() throws RemoteException {
    // Nothing to be done here.
  }

  /**
   * A container invokes this method before it ends the life of the session object.
   * This happens as a result of a client's invoking a remove operation, or when a
   * container decides to terminate the session object after a timeout.
   * This method is called with no transaction context.
   *
   * @exception RemoteException - Thrown if the instance could not perform the function
   *            requested by the container
   */
  public void ejbRemove() throws RemoteException {
    // Nothing to be done here, in container persistance.
  }

  /**
   * Sets the associated session context. The container calls this method after the instance
   * creation.
   * The enterprise Bean instance should store the reference to the context object
   * in an instance variable.
   * This method is called with no transaction context.
   *
   * @param sessionContext - A SessionContext interface for the instance.
   * @exception RemoteException - Thrown if the instance could not perform the function
   *            requested by the container because of a system-level error.
   */
  public void setSessionContext(SessionContext sessionContext) throws RemoteException {
    // Keep the session context in object
    this.sessionContext = sessionContext;
  }

  
  /*========================= ChartHandler Workflow Objects implementation ============================*/
  

  /** Creates summary charts for a report summary and returns them in a vector
   *  represented as byte arrays of a jpeg image.
   *
   *  @param chartType  The type of the chart
   *  @param ReportSummaryGen - The reportSummaryGeneration currently processed
   *
   *  @return Vector  The created charts as jpeg images of the chart in form of
   *                   a byte array.
   * @exception RemoteException  Thrown if the instance could not perform the 
   *                              function requested by the container
   */
  public Vector createSummaryCharts(String chartType,
                                    ReportSummaryGeneration reportSummaryGen)
  throws RemoteException{
    // create a vector for the charts
    Vector charts = new Vector();
    if (reportSummaryGen.isCompareWSGs()){
      // get web site group ids and names
      Vector webSiteGroupIDs = reportSummaryGen.getWebSiteGroupIDs();
      Vector webSiteGroupNames = reportSummaryGen.getWebSiteGroupNames(webSiteGroupIDs);
      // create charts
      charts = createSummaryCharts(chartType, reportSummaryGen, webSiteGroupIDs,
                                  webSiteGroupNames);
    }
    else{ // different charts for each web site group
      Vector id;
      // for each web site group id
      Vector webSiteGroupIDs = reportSummaryGen.getWebSiteGroupIDs();
      Enumeration enumWSGIDs = webSiteGroupIDs.elements();
      while (enumWSGIDs.hasMoreElements()){
        // web site group id has to be passed as vector
        id = new Vector();
        id.addElement(enumWSGIDs.nextElement());
        // find web site group name
        Vector webSiteGroupNames = reportSummaryGen.getWebSiteGroupNames(id);
        // create table
        charts.addAll(createSummaryCharts(chartType, reportSummaryGen, id,
                                         webSiteGroupNames));
      } // end while web site group ids
    } // end if
    
    // return created charts
    return charts;
  }

  /** Creates measure charts for a report mesures and returns them in a vector
   *  represented as byte arrays of a jpeg image.
   *
   *  @param chartType  The type of the chart
   *  @param ReportGenMeasure  The reportGenMeasure currently processed
   *
   *  @return Vector  The created charts as jpeg images of the chart in form of
   *                   a byte array.
   *
   * @exception RemoteException  Thrown if the instance could not perform the 
   *                              function requested by the container
   */
  public Vector createMeasureCharts(String chartType,
                                    ReportGenerationMeasure reportGenMeasure)
  throws RemoteException{
    // create new Vector for the charts
    Vector charts = new Vector();
    byte[] b;
    // get specifications group of current reportgenerationmeasure
    SpecificationsGroup specsGroup =
      (SpecificationsGroup) reportGenMeasure.getSpecificationsGroup();
    // helper variables
    Vector measureSeries;
    MeasureSeries ms;
    // if PieChart then create one chart for each measure series
    if (chartType.equalsIgnoreCase("PieChart")){
      measureSeries = specsGroup.getMeasureSeries();
      // for each measure series
      Enumeration enumMS = measureSeries.elements();
      Vector currentMS = new Vector();
      while (enumMS.hasMoreElements()){
        // get next measureSeries
        ms = (MeasureSeries) enumMS.nextElement();
        // save ms in a vector to pass it to the createMeasureChart method
        currentMS.clear();
        currentMS.addElement(ms);
        b = createMeasureChart(chartType, reportGenMeasure, currentMS);
        charts.addElement(b);
      }
    }
    else{ // other than pie chart
      // if all wsgs should be displayed in one chart
      if (reportGenMeasure.isCompareWSGs()){
        // get measure series
        measureSeries = specsGroup.getMeasureSeries();
        // create chart
        b = createMeasureChart(chartType, reportGenMeasure, measureSeries);
        charts.addElement(b);        
      }
      else{ // different charts for each web site group
        // for each web site group
        Vector webSiteGroupIDs = reportGenMeasure.getWebSiteGroupIDs();
        Enumeration enum = webSiteGroupIDs.elements();
        while(enum.hasMoreElements()){
          String webSiteGroupID = (String) enum.nextElement();
          // get appropriate measure series
          measureSeries = 
            specsGroup.getMeasureSeriesByWebSiteGroupID(webSiteGroupID);
          // create chart
          b = createMeasureChart(chartType, reportGenMeasure, measureSeries);
          charts.addElement(b);          
        } // end while
      }
    } // end if pie chart
    
    // return created charts
    return charts;
  }
  

  /** Creates summary charts for a number of web site groups.
   *
   *  @param chartType  The type of the chart
   *  @param ReportSummaryGen  The reportSummaryGeneration currently processed
   *  @param webSiteGroupIDs  The ids of the web site groups to display in the
   *                           chart
   *  @param webSiteGroupNames  The names of the web site groups to display in 
   *                             the chart
   *
   *  @return Vector  The created charts as jpeg images of the chart in form of
   *                   a byte array 
   *
   * @exception RemoteException  Thrown if the instance could not perform the 
   *                              function requested by the container
   */
  protected Vector createSummaryCharts(String chartType, 
                                       ReportSummaryGeneration reportSummaryGen,
                                       Vector webSiteGroupIDs,
                                       Vector webSiteGroupNames)
  throws RemoteException{
    byte[] b;
    Vector charts = new Vector();
    // get ChartFactory 
    ChartFactory chartFactory = createChartFactory();
    // create appropriate chart
    Chart chart = 
      chartFactory.createChart(chartType, "");
    
    // get measure series and add them to the chart(s)
    // lots of helper variables
    boolean first = true;
    String measureID, measureName, webSiteGroupID, webSiteGroupName;
    Vector specStrings = null;
    SpecificationsGroup specsGroup;
    Vector measuresSeries;
    MeasureSeries measureSeries;
    Vector msValues;
    // get measureIDs
    Vector measureIDs = reportSummaryGen.getMeasureIDs();    
    // for each measureID
    Enumeration enumMeasureIDs = measureIDs.elements();
    while(enumMeasureIDs.hasMoreElements()){
      measureID = (String) enumMeasureIDs.nextElement();
      measureName = reportSummaryGen.getMeasureName(measureID);
      Enumeration enumWSGs = webSiteGroupNames.elements();
      Enumeration enumWSGIDs = webSiteGroupIDs.elements();
      // for each web site group
      while(enumWSGs.hasMoreElements()){
        webSiteGroupName = (String)enumWSGs.nextElement();
        webSiteGroupID = (String) enumWSGIDs.nextElement();
        specsGroup =
          reportSummaryGen.getSpecificationsGroupByMeasureID(measureID);
        measuresSeries = 
          specsGroup.getMeasureSeriesByWebSiteGroupID(webSiteGroupID);
        // if first "round" then add specification Strings to chart
        if (first){
          // get specification Strings
          specStrings = specsGroup.getSpecificationsStrings();
          // add specifications to chart
          chart.setSpecifications(specStrings);
          first = false;
        } // end if
        // for each measure series
        Enumeration enumSeries = measuresSeries.elements();
        while(enumSeries.hasMoreElements()){
          measureSeries = (MeasureSeries) enumSeries.nextElement();
          // Get values of this measureSeries
          Vector mSValues = measureSeries.getSeriesValues();
          // Add series to chart
          chart.addSeries(measureName + " " +  webSiteGroupName, mSValues);
          // one pie chart for each measure series
          if (chartType.equalsIgnoreCase("PieChart")){
            chart.setTitle(measureName + " " + webSiteGroupName);
            // paint chart and get its jpeg image
            chart.paint();
            //b = chart.getJpegImage();
            b = chart.getPngImage();
            // add chart to charts Vector
            charts.addElement(b);
            // Delete "old" pie chart, because its no longer needed
            chart.deleteChart();
            // create new pie chart
            chart = chartFactory.createChart(chartType, "");
            // add specifications to chart
            chart.setSpecifications(specStrings);
          } // end if pie chart
          
        } // end while enumSeries
      } // end while enumWSGs
    } // end wihle measureIDs
    
    // if not pie chart
    if (!chartType.equalsIgnoreCase("PieChart")){
      // paint chart and get its jpeg image
      chart.paint();
      //b = chart.getJpegImage();
      b = chart.getPngImage();
      // add chart to charts Vector
      charts.addElement(b);
      // Delete "old" chart, because its no longer needed
      chart.deleteChart();
    } // end if not pie chart      

    return charts;
  }

  
  /** Creates a measure chart for a number of measure series.
   *
   *  @param chartType  The type of the chart
   *  @param ReportGenMeasure  The reportGenerationMeasure currently processed
   *  @param measureSeries  The measureSeries to display in the chart
   *
   *  @return byte[]  A jpeg image of the chart as byte array 
   *
   * @exception RemoteException  Thrown if the instance could not perform the 
   *                              function requested by the container
   */
  protected byte[] createMeasureChart(String chartType, 
                                      ReportGenerationMeasure reportGenMeasure,
                                      Vector measureSeries)
  throws RemoteException{
    // get ChartFactory 
    ChartFactory chartFactory = createChartFactory();
    // create appropriate chart
    Chart chart = 
      chartFactory.createChart(chartType, reportGenMeasure.getMeasureName());

    // get specificationsGroup of the ReportGenerationMeasure
    SpecificationsGroup specsGroup =
      (SpecificationsGroup) reportGenMeasure.getSpecificationsGroup();
    // Get specification Strings of the specificationsgroup
    Vector specStrings = specsGroup.getSpecificationsStrings();
    // add specifications to chart
    chart.setSpecifications(specStrings);
    
    // for each maesureSeries of this chart
    MeasureSeries ms;
    Enumeration enumMS = measureSeries.elements();
    while (enumMS.hasMoreElements()){
      // get next measureSeries
      ms = (MeasureSeries) enumMS.nextElement();
      // Get values of this measureSeries
      Vector mSValues = ms.getSeriesValues();
      // Add series to chart
      chart.addSeries(ms.getSeriesName(), mSValues);
      // if pie chart, change title
      if (chartType.equals("PieChart")){
        chart.setTitle(reportGenMeasure.getMeasureName() + ": " + ms.getSeriesName());
      } // end if 
    } // end while measure series
    
    // paint chart
    chart.paint();
    // Get Image in Jpeg format in form of an OutputStream
    byte[] b;
    //b = chart.getJpegImage();
    b = chart.getPngImage();
    // Delete "old" pie chart, because its no longer needed
    chart.deleteChart();
    return b;
  }

  /** Returns a newly created ChartFactoryObject.
   *
   *  @return ChartFactory  Returns the newly created ChartFactory
   *
   * @exception RemoteException  Thrown if the instance could not perform the 
   *                              function requested by the container
   */
  protected ChartFactory createChartFactory() throws RemoteException{
    try{
      Context initialContext = new InitialContext();
      // get home
      ChartFactoryHome chartFactoryHome = (ChartFactoryHome)
        PortableRemoteObject.narrow(
          initialContext.lookup(ChartFactoryHome.COMP_NAME),
                                ChartFactoryHome.class);
      // get chartFactory
      ChartFactory chartFactory = chartFactoryHome.create();
      return chartFactory;
    }
    catch(Exception e){
      throw new RemoteException();
    }
  }
  
}
