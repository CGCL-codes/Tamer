package gov.lanl.Web;

import org.omg.DsObservationAccess.ObservationDataStruct;

public abstract class SyndromeReportChart {

    /**
   * Set the bin size
   */
    public abstract void setInterval(String interval);

    /** set the plot type
   * @param plotType String with the plot type
   */
    public abstract void setPlotType(String plotType);

    /**
   *  Define the property sheet to control the chart
   */
    public abstract void setChart(String chartProps);

    /** Compute the time history chart
   * @param q contains the data to be plotted
   * @param syndromeName Syndrome to be looked up
   * @param start begining of interval
   * @param stop end of interval
   * @return plot file name
   *
   */
    public abstract String getChart(ObservationDataStruct[] q, String syndromeName, String start, String stop);
}
