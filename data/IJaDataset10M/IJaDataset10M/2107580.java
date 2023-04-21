package org.vardb.analytics;

import java.util.ArrayList;
import java.util.List;
import nl.gridshore.google.AnalyticsServiceWrapper;
import nl.gridshore.google.DataQueryBuilder;
import nl.gridshore.google.Profile;
import org.vardb.CVardbException;
import org.vardb.util.CStringHelper;
import com.google.gdata.client.analytics.DataQuery;
import com.google.gdata.data.analytics.Aggregates;
import com.google.gdata.data.analytics.DataEntry;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.data.analytics.DataSource;
import com.google.gdata.data.analytics.Dimension;
import com.google.gdata.data.analytics.Metric;
import com.google.gdata.data.analytics.Segment;

public final class CAnalyticsWrapper {

    private CAnalyticsWrapper() {
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Not enough arguments supplied:");
            System.out.println("arg 1 : username");
            System.out.println("arg 2 : password");
            return;
        }
        String username = args[0];
        String password = args[1];
        if (CStringHelper.isEmpty(username)) {
            System.out.println("Username is empty");
            return;
        }
        if (CStringHelper.isEmpty(password)) {
            System.out.println("Password is empty");
            return;
        }
        AnalyticsServiceWrapper serviceWrapper = new AnalyticsServiceWrapper(username, password);
        List<Profile> profiles = serviceWrapper.obtainProfiles();
        printProfileInformation(profiles);
        Profile profile = getProfile(profiles, "ga:6097495");
        DataQuery query = createQuery(profile);
        query.setMaxResults(10);
        DataFeed feed = serviceWrapper.obtainQueryResults(query);
        printFeedData(feed);
        printFeedDataSources(feed);
        printFeedAggregates(feed);
        printSegmentInfo(feed);
        printDataForOneEntry(feed);
        System.out.println(getEntriesAsTable(feed));
    }

    public static void showResults(DataFeed feed) {
        System.out.println("----------- Data Feed Results ----------");
        for (DataEntry entry : feed.getEntries()) {
            System.out.println("\nPage Title = " + entry.stringValueOf("ga:pageTitle") + "\nPage Path  = " + entry.stringValueOf("ga:pagePath") + "\nPageviews  = " + entry.stringValueOf("ga:pageviews"));
            System.out.println("title=" + entry.getTitle());
        }
    }

    public static DataQuery createQuery(Profile profile) {
        return DataQueryBuilder.newBuilder(profile).startDate("2010-01-01").endDate("2010-01-06").dimension(nl.gridshore.google.Dimension.day).metric(nl.gridshore.google.Metric.visits).filters("ga:pageviews>=1").create();
    }

    private static void printProfileInformation(List<Profile> profiles) {
        for (Profile profile : profiles) {
            System.out.println("Title : " + profile.getTitle() + " key : " + profile.getUniqueId());
        }
    }

    private static Profile getProfile(List<Profile> profiles, String key) {
        for (Profile profile : profiles) {
            if (profile.getUniqueId().equals(key)) return profile;
        }
        throw new CVardbException("can't find profile with key: " + key);
    }

    /**
     * Prints the important Google Analytics relates data in the Data Feed.
     */
    public static void printFeedData(DataFeed feed) {
        System.out.println("\n-------- Important Feed Information --------");
        System.out.println("\nFeed Title      = " + feed.getTitle().getPlainText() + "\nFeed ID         = " + feed.getId() + "\nTotal Results   = " + feed.getTotalResults() + "\nSart Index      = " + feed.getStartIndex() + "\nItems Per Page  = " + feed.getItemsPerPage() + "\nStart Date      = " + feed.getStartDate().getValue() + "\nEnd Date        = " + feed.getEndDate().getValue());
    }

    /**
     * Prints the important information about the data sources in the feed.
     * Note: the GA Export API currently has exactly one data source.
     */
    public static void printFeedDataSources(DataFeed feed) {
        DataSource gaDataSource = feed.getDataSources().get(0);
        System.out.println("\n-------- Data Source Information --------");
        System.out.println("\nTable Name      = " + gaDataSource.getTableName().getValue() + "\nTable ID        = " + gaDataSource.getTableId().getValue() + "\nWeb Property Id = " + gaDataSource.getProperty("ga:webPropertyId") + "\nProfile Id      = " + gaDataSource.getProperty("ga:profileId") + "\nAccount Name    = " + gaDataSource.getProperty("ga:accountName"));
    }

    /**
     * Prints all the metric names and values of the aggregate data. The
     * aggregate metrics represent the sum of the requested metrics across all
     * of the entries selected by the query and not just the rows returned.
     */
    public static void printFeedAggregates(DataFeed feed) {
        System.out.println("\n-------- Aggregate Metric Values --------");
        Aggregates aggregates = feed.getAggregates();
        for (Metric metric : aggregates.getMetrics()) {
            System.out.println("\nMetric Name  = " + metric.getName() + "\nMetric Value = " + metric.getValue() + "\nMetric Type  = " + metric.getType() + "\nMetric CI    = " + metric.getConfidenceInterval().toString());
        }
    }

    /**
     * Prints segment information if the query has an advanced segment defined.
     */
    public static void printSegmentInfo(DataFeed feed) {
        if (feed.hasSegments()) {
            System.out.println("\n-------- Advanced Segments Information --------");
            for (Segment segment : feed.getSegments()) {
                System.out.println("\nSegment Name       = " + segment.getName() + "\nSegment ID         = " + segment.getId() + "\nSegment Definition = " + segment.getDefinition().getValue());
            }
        }
    }

    /**
     * Prints all the important information from the first entry in the
     * data feed.
     */
    public static void printDataForOneEntry(DataFeed feed) {
        System.out.println("\n-------- Important Entry Information --------\n");
        if (feed.getEntries().isEmpty()) {
            System.out.println("No entries found");
        } else {
            DataEntry singleEntry = feed.getEntries().get(0);
            System.out.println("Entry ID    = " + singleEntry.getId());
            System.out.println("Entry Title = " + singleEntry.getTitle().getPlainText());
            for (Dimension dimension : singleEntry.getDimensions()) {
                System.out.println("Dimension Name  = " + dimension.getName());
                System.out.println("Dimension Value = " + dimension.getValue());
            }
            for (Metric metric : singleEntry.getMetrics()) {
                System.out.println("Metric Name  = " + metric.getName());
                System.out.println("Metric Value = " + metric.getValue());
                System.out.println("Metric Type  = " + metric.getType());
                System.out.println("Metric CI    = " + metric.getConfidenceInterval().toString());
            }
        }
    }

    /**
     * Get the data feed values in the feed as a string.
     * @return {String} This returns the contents of the feed.
     */
    public static String getEntriesAsTable(DataFeed feed) {
        if (feed.getEntries().isEmpty()) {
            return "No entries found";
        }
        DataEntry singleEntry = feed.getEntries().get(0);
        List<String> feedDataNames = new ArrayList<String>();
        StringBuffer feedDataValues = new StringBuffer("\n-------- All Entries In A Table --------\n");
        for (Dimension dimension : singleEntry.getDimensions()) {
            feedDataNames.add(dimension.getName());
        }
        for (Metric metric : singleEntry.getMetrics()) {
            feedDataNames.add(metric.getName());
        }
        for (DataEntry entry : feed.getEntries()) {
            for (String dataName : feedDataNames) {
                feedDataValues.append(String.format("\n%s \t= %s", dataName, entry.stringValueOf(dataName)));
            }
            feedDataValues.append('\n');
        }
        return feedDataValues.toString();
    }
}
