package org.insight.report.useragent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import org.insight.common.util.Util;
import org.insight.model.browser.BrowserManager;
import org.insight.model.graphs.GraphManager;
import org.insight.model.graphs.GraphType;
import org.insight.model.graphs.ImageType;
import org.insight.model.graphs.PieChart;
import org.insight.model.log.LogEntry;
import org.insight.model.report.EntryInfo;
import org.insight.model.report.Report;

/**
 * Provides a breakdown of page views per user agent,
 * ignoring all robot traffic.
 *
 * <pre>
 * Version History:
 * 
 * $Log: UserAgentReport.java,v $
 * Revision 1.2  2006/02/03 13:20:29  cjn
 * Fixed checkstyle violations.
 *
 * Revision 1.1  2003/03/19 13:35:59  cjn
 * Refactored packages
 *
 * Revision 1.7  2003/03/19 09:59:25  cjn
 * Refactored packages
 *
 * Revision 1.6  2003/01/24 14:48:21  cjn
 * Added handling of empty user agent fields
 *
 * Revision 1.5  2002/10/03 14:42:07  cjn
 * Added mapping user agent strings to browser names
 *
 * Revision 1.4  2002/09/23 11:31:24  cjn
 * Changed default 5 items to 10
 *
 * Revision 1.3  2002/09/20 15:16:51  cjn
 * Updated to produce a PieChart
 *
 * Revision 1.2  2002/07/05 09:10:40  cjn
 * Added CVS keywords
 * </pre>
 *
 * @author Chris Nappin
 * @version $Revision: 1.2 $
 */
public class UserAgentReport extends Report {

    /** number of unique user agents to list. */
    private static final int MAX_UAS = 10;

    /** resource strings used by this report. */
    private static final String TITLE = "title";

    private static final String DESCRIPTION = "desc";

    /**
     * map of user agent totals, key is user agent string,
     * value is number of visits.
     */
    private Map userAgents;

    /** total page views. */
    private int totalPages;

    /** used to map user agent strings to browser names. */
    private BrowserManager browserManager;

    /**
     * Flag indicating that at least one valid user agent string has been found.
     * For some log file formats, we may not get any, in which
     * case this report should not be shown
     */
    private boolean userAgentFound;

    /**
     * Constructor.
     */
    public UserAgentReport() {
        this.browserManager = BrowserManager.getManager();
        this.userAgents = new HashMap();
    }

    /**
     * Process the specified log file entry.
     * @param entry The log file entry
     * @param info Extra information about the entry
     */
    public void processEntry(LogEntry entry, EntryInfo info) {
        if (!info.isRobot() && info.isPage()) {
            String userAgent = resolveUserAgent(entry.getUserAgent());
            int visits = 0;
            Integer intVisits = (Integer) userAgents.get(userAgent);
            if (intVisits != null) {
                visits = intVisits.intValue();
            }
            userAgents.put(userAgent, new Integer(++visits));
            totalPages++;
        }
    }

    /** 
     * Resolves the names of user agents, returns "Unknown" if not available.
     * @param userAgent The user agent string
     * @return The resolved name
     */
    private String resolveUserAgent(String userAgent) {
        if (userAgent == null || userAgent.trim().length() == 0) {
            return "Unknown";
        } else {
            userAgentFound = true;
            String name = browserManager.matchUserAgent(userAgent);
            if (name != null) {
                return name;
            } else {
                Util.debug("Unknown Browser: " + userAgent);
                return "Unknown";
            }
        }
    }

    /**
     * Provides a textual summary of the report,
     * in HTML format.
     * @return The HTML report
     */
    public String reportHTML() {
        if (!userAgentFound) {
            return "";
        }
        SortedMap sortedUserAgents = sortByValues(userAgents);
        ReportData data = flattenSortedMap(sortedUserAgents, MAX_UAS);
        StringBuffer report = new StringBuffer();
        report.append("<h3>").append(getResourceString(TITLE)).append("</h3>").append("<p>").append(getResourceString(DESCRIPTION)).append("</p>");
        try {
            GraphManager graphManager = GraphManager.getManager();
            PieChart chart = (PieChart) graphManager.getGraph(GraphType.PIE_CHART, GRAPH_WIDTH, GRAPH_HEIGHT);
            chart.setLabels(data.getLabels());
            chart.setValues(data.getValues());
            chart.setTitle(getResourceString(TITLE));
            graphManager.writeGraph(chart, ImageType.JPEG, getGraphDirectory(), getGraphFilename());
            report.append(getReportImageHTML());
        } catch (IOException ioex) {
            Util.debug("Error creating graph");
        }
        report.append("<p>&nbsp;</p>");
        return report.toString();
    }
}
