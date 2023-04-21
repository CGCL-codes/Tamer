package edu.uga.galileo.slash.reports;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * <p>
 * Title: AbstractReport
 * </p>
 * <p>
 * Description: The abstract class from which all reports are descended.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: University of Georgia, Main Library/GALILEO
 * </p>
 * 
 * @author <a href="mdurant@uga.edu">Mark Durant </a>
 * @version 1.0
 */
public abstract class AbstractReport {

    /**
	 * The personal properties bundle.
	 */
    protected ResourceBundle rb = ResourceBundle.getBundle("personal");

    /**
	 * Flag to indicate whether aggregation is currently occurring for this
	 * report.
	 */
    protected boolean aggregating = false;

    /**
	 * The date threshold beyond which data should be aggregated.
	 */
    protected Calendar threshold;

    /**
	 * Instantiate basic information for <code>AbstractReport</code>
	 * subclasses.
	 */
    public AbstractReport() {
        int thresholdValue = Integer.parseInt(rb.getString("stats.aggregation.threshold"));
        threshold = Calendar.getInstance();
        threshold.add(Calendar.DATE, (-1 * thresholdValue));
    }

    /**
	 * Reports requiring daily (as opposed to monthly) aggregation don't need to
	 * send a date, so this method's provided for convenience. The report itself
	 * must still implement the
	 * <code>aggregateMyData(StatsConnection conn, String date)</code> method,
	 * but should expect a <code>null</code> value for the date.
	 * 
	 * @param conn
	 *            The <code>Connection</code> to be used.
	 * @return <code>true</code> if data aggregation was successful;
	 *         <code>false</code> if not.
	 */
    public boolean aggregateData(Connection conn) {
        return aggregateData(conn, null);
    }

    /**
	 * Whenever a report is listed in the properties file as requiring
	 * aggregation, this method will be called on it at whatever day/time is
	 * specified there. Calls the <code>aggregateMyData</code> abstract
	 * method. Aggregation itself is handled by
	 * <code>StatsAggregationControllerServlet</code>, so this method should
	 * only be called by it or supporting services.
	 * 
	 * @param conn
	 *            The <code>Connection</code> to be used.
	 * @param date
	 *            mm-yyyy format for month and year to aggregate.
	 * @return <code>true</code> if data aggregation was successful;
	 *         <code>false</code> if not.
	 */
    public boolean aggregateData(Connection conn, String date) {
        aggregating = true;
        boolean result = aggregateMyData(conn, date);
        aggregating = false;
        return result;
    }

    /**
	 * The method that must be implemented by subclasses in order for data
	 * aggregation to occur. This method is called by <code>aggregateData</code>,
	 * and shouldn't be called directly by outside applications.
	 * 
	 * @param conn
	 *            The <code>Connection</code> to be used.
	 * @param date
	 *            mm-yyyy format for month and year to aggregate.
	 * @return <code>true</code> if data aggregation was successful;
	 *         <code>false</code> if not.
	 */
    protected abstract boolean aggregateMyData(Connection conn, String date);

    /**
	 * Convert an mm-yyyy formatted <code>String</code> to a more useful
	 * <code>Calendar</code> object.
	 * 
	 * @param date
	 *            The mm-yyyy formatted date representing the month to be
	 *            aggregated.
	 * @return A <code>Calendar</code> object set to the specified month and
	 *         year.
	 */
    protected Calendar getTargetMonth(String date) {
        String month = date.substring(0, date.indexOf('-'));
        String year = date.substring(date.indexOf('-') + 1);
        Calendar workingCal = Calendar.getInstance();
        workingCal.set(Calendar.MONTH, (Integer.parseInt(month) - 1));
        workingCal.set(Calendar.YEAR, Integer.parseInt(year));
        return workingCal;
    }

    /**
	 * Subclass-specific implementation of data retrieval.
	 * 
	 * @return An <code>ArrayList</code> of results.
	 */
    protected abstract ArrayList getData();

    /**
	 * Check to see if the report is in the process of aggregating data.
	 * 
	 * @return <code>true</code> if data is being aggregated.
	 */
    protected boolean isAggregating() {
        return aggregating;
    }

    /**
	 * By reflection, instantiate a report from the
	 * <code>com.uga.statistics.reports</code> package.
	 * 
	 * @param reportName
	 *            The name of the <code>AbstractReport</code> subclass in
	 *            <code>com.uga.statistics.reports</code> to instantiate by
	 *            reflection.
	 * @return The <code>AbstractReport</code> requested.
	 */
    public static AbstractReport instantiateReport(String reportName) {
        Class c;
        Constructor constructor;
        AbstractReport report;
        try {
            c = Class.forName("com.uga.statistics.reports." + reportName);
            constructor = c.getConstructor((Class[]) null);
            report = (AbstractReport) constructor.newInstance((Object[]) null);
            return report;
        } catch (Exception e1) {
            System.out.println("AbstractReport instantiateReport(...) exception: " + e1);
            e1.printStackTrace();
        }
        return null;
    }
}
