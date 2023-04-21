package gov.lanl.Web;

import gov.lanl.Utility.ISODate;
import gov.lanl.Utility.ObsData2Dom;
import gov.lanl.Utility.Utilities;
import gov.lanl.Utility.XML;
import gov.lanl.ObsDataTools.CoasDataAccess;
import gov.lanl.ObsDataTools.ObsDataAccessException;
import gov.lanl.Analysis.category.GetFits;
import org.omg.DsObservationAccess.ObservationDataStruct;
import org.omg.DsObservationAccess.ObservationId;
import nu.xom.Document;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.Serializable;

/**
 *
 * @author  $Author: dwforslund $
 * @version $Revision: 3349 $ $Date: 2006-03-27 20:23:07 -0500 (Mon, 27 Mar 2006) $
 */
public class SyndromeReport {

    private static org.apache.log4j.Logger cat = org.apache.log4j.Logger.getLogger(SyndromeReport.class.getName());

    private COAS coas;

    private SimpleDateFormat longDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    private static final String DATE_TIME = XML.ObservationDate_Time;

    private Compare c = new Compare();

    private Vector fields;

    private ObservationDataStruct[] q = null;

    private String SEP = ",";

    /** Creates new SyndromeReport with default chart (JFreeChart) */
    public SyndromeReport() {
        fields = new Vector();
        fields.addElement(DATE_TIME);
    }

    /**
     * set the Properties
     * @param props Properties
     */
    public void setProperties(Properties props) {
    }

    /** set the COAS server
     * @param coas COAS server
     */
    public void setCoas(COAS coas) {
        this.coas = coas;
    }

    /**
     * add field for display
     */
    public void addField(String field) {
        fields.addElement(field);
    }

    /**
     * return the fields labels
     */
    public Vector getFields() {
        return fields;
    }

    /**
     *  return the number of observations that were found
     */
    public int getNumber() {
        if (q != null) return q.length; else return 0;
    }

    /**
     *   replace the fields with these (but retain Date_Time as first field no matter what)
     *   @param inFields
     */
    public void setFields(String[] inFields) {
        fields = new Vector();
        fields.addElement(DATE_TIME);
        for (int i = 0; i < inFields.length; i++) {
            fields.addElement(inFields[i]);
        }
    }

    /**
     *  get the observations from the server
     */
    public ObservationDataStruct[] getObservations(String facility, String syndromeName, Calendar start, Calendar stop, boolean fullData) {
        cat.debug("getObservations(" + facility + "," + syndromeName + ",*,*," + fullData + ")");
        String startString = "";
        String stopString = "";
        if (start != null) startString = ISODate.Date2ISO(start.getTime());
        if (stop != null) {
            Calendar stopClone = (Calendar) stop.clone();
            stopClone.set(Calendar.HOUR_OF_DAY, 23);
            stopClone.set(Calendar.MINUTE, 59);
            stopClone.set(Calendar.SECOND, 59);
            stopString = ISODate.Date2ISO(stopClone.getTime());
        }
        coas.setDuplicateRemoval(true);
        coas.setQualifiersOnly(!fullData);
        coas.setReturnObsId(!fullData);
        q = coas.query(facility, syndromeName, startString, stopString, coas.getCredentials());
        cat.debug("getObservations: returning " + q.length + " observations");
        return q;
    }

    /**
     * Get observations from the server, merging results from multiple
     * facilities.
     * @param facilities List of facilities to get data for, or null to get
     *   all facilities.
     * @param syndromeName Syndrome name to get observations for.
     * @param start Start of interval.
     * @param stop End of interval.
     * @param fullData Whether to return full observation data (otherwise
     *   return qualifiers only).
     * @return List of ObservationDataStruct.
     */
    public ArrayList getObservations(String[] facilities, String syndromeName, Calendar start, Calendar stop, boolean fullData) {
        cat.debug("getObservations(*," + syndromeName + ",*,*," + fullData + ")");
        if (facilities == null) facilities = new String[] { "OTHER:/" };
        ArrayList results = new ArrayList();
        for (int i = 0; i < facilities.length; i++) {
            ObservationDataStruct[] observations = getObservations(facilities[i], syndromeName, start, stop, fullData);
            for (int j = 0; j < observations.length; j++) results.add(observations[j]);
        }
        cat.debug("getObservations: returning " + results.size() + " results");
        return results;
    }

    /**
     * Compute the time history chart
     * @param facilities Facilities to get data for.
     * @param syndromeNames Syndromes to be looked up
     * @param start begining of interval
     * @param stop end of interval
     * @param fullData Whether to query full observation data (not just
     *   qualifiers)
     * @return Graph data sources
     */
    public GraphDataSource[] getStackedChartData(String[] facilities, String[] syndromeNames, Calendar start, Calendar stop, boolean fullData) {
        cat.debug("getStackedChartData(*,*,*,*," + fullData + ")");
        ArrayList graphDataSources = new ArrayList();
        ArrayList allResults = new ArrayList();
        for (int i = 0; i < syndromeNames.length; i++) {
            ArrayList results = getObservations(facilities, syndromeNames[i], start, stop, fullData);
            if (results.size() > 0) {
                allResults.addAll(results);
                ObservationDataStruct[] observations = new ObservationDataStruct[results.size()];
                results.toArray(observations);
                graphDataSources.add(new ObservationDataSource(syndromeNames[i], observations));
            }
        }
        GraphDataSource[] sources = new GraphDataSource[graphDataSources.size()];
        graphDataSources.toArray(sources);
        ObservationDataStruct[] allObservations = new ObservationDataStruct[allResults.size()];
        allResults.toArray(allObservations);
        q = allObservations;
        cat.debug("getStackedChartData: returning " + sources.length + " sources");
        return sources;
    }

    /**
     * Get a chart with control chart information superimposed.
     * @param facilities Facilities to get data for.
     * @param syndromeName Syndrome name to plot
     * @param start Beginning of interval
     * @param stop End of interval
     * @param fullData Whether to query full observation data (not just
     *   qualifiers)
     * @return Plot file name
     */
    public GraphDataSource[] getRangedChartData(String[] facilities, String syndromeName, Calendar start, Calendar stop, boolean fullData) {
        cat.debug("getRangedChartData(*," + syndromeName + ",*,*," + fullData + ")");
        ArrayList results = getObservations(facilities, syndromeName, start, stop, fullData);
        ObservationDataStruct[] observations = new ObservationDataStruct[results.size()];
        results.toArray(observations);
        this.q = observations;
        String shortSyndromeName = syndromeName.substring(syndromeName.lastIndexOf('/') + 1);
        GraphDataSource[] sources = new GraphDataSource[3];
        sources[0] = new UCLDataSource(shortSyndromeName);
        sources[1] = new BaselineDataSource(shortSyndromeName);
        sources[2] = new ObservationDataSource(syndromeName, this.q);
        cat.debug("getRAngedChartData() -> exit");
        return sources;
    }

    /**
     * Set up to graph a chart of Page's Statistics
     * @param pagesStats Collection of PagesStat objects
     * @param start Start of chart range
     * @param stop End of chart range
     * @return GraphDataSource[]
     */
    public GraphDataSource[] getPagesChartData(Collection pagesStats, Calendar start, Calendar stop) {
        GraphDataSource[] sources = new GraphDataSource[1];
        sources[0] = new PagesDataSource(pagesStats);
        return sources;
    }

    /**
     * return a Collection of Events organized by row with date/syndrome/facility/clinician in the row
     */
    public Vector getRecords() {
        cat.debug("getRecords()");
        Vector records = new Vector();
        if (q != null) {
            Vector rows = new Vector();
            for (int i = 0; i < q.length; i++) {
                Vector row = new Vector();
                XQLQuery xqlQuery = new XQLQuery(q[i]);
                row.addElement(Integer.toString(i));
                Vector v;
                for (int j = 0; j < fields.size(); j++) {
                    String fld = (String) fields.elementAt(j);
                    v = new Vector();
                    String f = "";
                    if ("_Code".equals(fld)) f = q[i].code; else {
                        xqlQuery.match(fld, "ObsValue", v);
                        for (int k = 0; k < v.size(); k++) {
                            f += (String) v.elementAt(k);
                            if (k < v.size() - 1) f += SEP;
                        }
                    }
                    row.addElement(f);
                }
                rows.addElement(row);
            }
            Collections.sort(rows, c);
            for (int i = 0; i < rows.size(); i++) {
                Vector v = (Vector) rows.elementAt(i);
                if (v.elementAt(1) != null) {
                    Date d = ISODate.HL72Date((String) v.elementAt(1));
                    v.setElementAt(longDateFormat.format(d, new StringBuffer(), new java.text.FieldPosition(0)).toString(), 1);
                }
                records.addElement(v);
            }
        }
        cat.debug("getRecords() -> exit");
        return records;
    }

    /**
     *  return XML string of the specified record
     *  @param no record number to be retrieved
     */
    public String getRecord(int no) {
        ObsData2Dom obs = new ObsData2Dom(q[no]);
        return obs.toString();
    }

    /**
     * return Dom representation of specified record
     * @param no of record to be returned
     */
    public Document getDom(int no) {
        ObsData2Dom obs = new ObsData2Dom(q[no]);
        return obs.toDom();
    }

    /**
     * get the ObservationId associated with a data element
     * @param no
     * @return Document
     */
    public Document getObsById(int no) {
        CoasDataAccess dataAccess = new CoasDataAccess(q[no]);
        ObservationId obsId = null;
        try {
            obsId = dataAccess.getObservationId();
        } catch (ObsDataAccessException e) {
            cat.error(e);
        }
        Document doc = null;
        if (obsId != null) {
            ObservationDataStruct obsData = coas.getObservation(obsId);
            if (obsData != null) {
                cat.debug("getObsById: found " + obsData.code);
                ObsData2Dom obsDataDom = new ObsData2Dom();
                doc = obsDataDom.toDom(obsData);
            }
            if (doc == null) cat.error("Failed to find requested ObsId");
        } else {
            cat.error("getObsById: obsId was null");
            return getDom(no);
        }
        return doc;
    }

    /**
     * Get an ordered mapping of calendars to Integers containing the number of
     * counts found on that date in the given observation data
     */
    public TreeMap getSortedCountsByDay() {
        return getSortedCountsByDay(this.q);
    }

    /**
     * Get an ordered mapping of calendars to Integers containing the number of
     * counts found on that date in the given observation data
     */
    public static TreeMap getSortedCountsByDay(ObservationDataStruct[] observations) {
        cat.debug("getSortedCountsByDay([" + observations.length + "])");
        XQLQuery xqlQuery = new XQLQuery(observations);
        Vector v = new Vector();
        xqlQuery.match(DATE_TIME, "ObsValue", v);
        TreeMap counts = new TreeMap(new CalendarComparator());
        Iterator i = v.iterator();
        while (i.hasNext()) {
            Date d = ISODate.HL72Date((String) i.next());
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Integer previousValue = (Integer) counts.get(cal);
            if (previousValue == null) counts.put(cal, new Integer(1)); else counts.put(cal, new Integer(previousValue.intValue() + 1));
        }
        cat.debug("getSortedCountsByDay: returning " + counts.size() + " counts");
        return counts;
    }

    /**
     * Fill in the missing slots in a Calendar-to-Integer mapping with zeros.
     */
    public static void expandCountsMap(TreeMap calendars) {
        cat.debug("expandCountsMap(*)");
        if (calendars == null || calendars.size() == 0) return;
        Calendar startDate = (Calendar) calendars.firstKey();
        Calendar endDate = (Calendar) calendars.lastKey();
        Calendar currentDate = (Calendar) startDate.clone();
        while (currentDate.before(endDate)) {
            if (!calendars.containsKey(currentDate)) calendars.put(currentDate.clone(), new Integer(0));
            currentDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        cat.debug("expandCountsMap() -> exit");
    }

    /**
     * Get an ordered set of page statistics for the current observations
     * (which are obtained from CountsDaemon)
     * @param syndrome Syndrome name to get Page's statistics for
     * @param t0Date Date to start Page calculations at
     * @param endDate Date to stop Page calculations at
     */
    public TreeSet getPageStatisticsByDay(String syndrome, Calendar t0Date, Calendar endDate) {
        cat.debug("getPageStatisticsByDay(" + syndrome + ",*,*)");
        GetFits fits = GetFits.createInstance(syndrome);
        if (t0Date == null || t0Date.before(fits.getT0Date())) t0Date = fits.getT0Date();
        if (endDate == null) endDate = Calendar.getInstance();
        int days = Utilities.daysBetween(t0Date, endDate);
        double[] counts = new double[days + 1];
        double[] errors = new double[days + 1];
        Calendar cal = (Calendar) t0Date.clone();
        int i = 0;
        while (!cal.after(endDate)) {
            counts[i] = CountsDaemon.getInstance().getCounts(cal, "WALDO", syndrome);
            errors[i] = (Math.log(counts[i] + 1) - Math.log(fits.calcFits(cal) + 1)) / Math.log(10);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            i++;
        }
        cal = (Calendar) t0Date.clone();
        i = 0;
        double[] pageStats = fits.getPage(errors);
        TreeSet sortedStats = new TreeSet();
        while (!cal.after(endDate)) {
            PagesStats ps = new PagesStats((Calendar) cal.clone(), pageStats[i], (int) counts[i]);
            sortedStats.add(ps);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            i++;
        }
        cat.debug("getPageStatisticsByDay() -> exit");
        return sortedStats;
    }

    public static class CalendarComparator implements Comparator, Serializable {

        public int compare(Object o1, Object o2) {
            Calendar c1 = (Calendar) o1;
            Calendar c2 = (Calendar) o2;
            long t1 = c1.getTime().getTime();
            long t2 = c2.getTime().getTime();
            if (t1 < t2) return -1; else if (t1 > t2) return 1; else return 0;
        }
    }

    /**
     * return XMl string of all contained records
     */
    public String getAllRecords() {
        ObsData2Dom obs = new ObsData2Dom(q);
        return obs.toString();
    }

    /**
     * Special comparator for two vectors which contain a set of strings and comparing on the 2nd
     *  element
     */
    class Compare implements java.util.Comparator {

        public Compare() {
        }

        public int compare(java.lang.Object obj, java.lang.Object obj1) {
            return ((String) ((Vector) obj).elementAt(1)).compareTo((String) ((Vector) obj1).elementAt(1));
        }

        public boolean equals(java.lang.Object obj) {
            return obj.equals(this);
        }
    }
}
