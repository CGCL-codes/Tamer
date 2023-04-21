package com.googlecode.jchav.data;

import java.util.SortedSet;

/**
 * Interface to the data source provided from JMeter detailing the performance characteristics of a given request.
 * @author $LastChangedBy: dallaway $
 * @version $LastChangedDate: 2007-12-22 11:17:13 -0500 (Sat, 22 Dec 2007) $ $LastChangedRevision: 166 $
 */
public interface PageData {

    /** The identifier for the summary page id. */
    public static final String SUMMARY_PAGE_ID = "Summary";

    /** Get the ordered list of measurements for a given pageId.
     *
     * @param pageId The unique page name from the getPages iterator.
     * @return Ordered list of measurements. Ordered in X axis order (left to right).
     */
    SortedSet<Measurement> getMeasurements(String pageId);

    /**
	 * Get the human-readable name for this page.
	 *
	 * @param pageId the page ID to look up.
	 * @return the human readable page title for the give pageId.
	 */
    String getPageTitle(String pageId);

    /** Test to decide if any data has been found.
     *
     * @return true if data has been added; false otherwise.
     */
    boolean isEmpty();

    /** Get an iterator over the ordered set of page ids.
     * @return Iterator over the ordered set of page ids.
     */
    Iterable<String> getPageIds();

    /** Add a measurement to the structure.
     *
     * @param pageId the pageId to add for,
	 * @param pageTitle the human-readable page title.
     * @param measurement The measurement.
     */
    void addMeasurement(String pageId, String pageTitle, Measurement measurement);
}
