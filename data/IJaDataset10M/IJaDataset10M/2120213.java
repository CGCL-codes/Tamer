package com.od.jtimeseries.ui.visualizer.download.panel;

import com.od.jtimeseries.net.httpd.AttributeName;
import com.od.jtimeseries.net.httpd.ElementName;
import com.od.jtimeseries.context.ContextProperties;
import com.od.jtimeseries.ui.net.AbstractRemoteQuery;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 12-Jan-2009
 * Time: 12:03:26
 */
public class FindRemoteTimeSeriesQuery extends AbstractRemoteQuery {

    private List<RemoteTimeSeries> result;

    public FindRemoteTimeSeriesQuery(URL url) {
        super(url);
    }

    public ContentHandler getContentHandler() {
        return new DefaultHandler() {

            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (localName.equals(ElementName.series.name())) {
                    parseSeries(attributes);
                }
            }

            private void parseSeries(Attributes attributes) {
                String url = attributes.getValue(AttributeName.seriesUrl.name());
                URL seriesUrl = null;
                try {
                    seriesUrl = new URL(getQueryUrl(), url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String id = attributes.getValue(AttributeName.id.name());
                String description = attributes.getValue(AttributeName.description.name());
                String parentPath = attributes.getValue(AttributeName.parentPath.name());
                String summaryStats = attributes.getValue(AttributeName.summaryStats.name());
                summaryStats = summaryStats == null ? "" : summaryStats;
                RemoteTimeSeries series = new RemoteTimeSeries(parentPath, id, description, seriesUrl, summaryStats);
                result.add(series);
            }
        };
    }

    public List<RemoteTimeSeries> getResult() {
        return result;
    }

    @Override
    protected void doBeforeRun() {
        result = new ArrayList<RemoteTimeSeries>();
    }

    public String getQueryDescription() {
        return "FindRemoteTimeSeriesQuery";
    }

    public static class RemoteTimeSeries {

        private String parentPath;

        private String id;

        private String description;

        private URL seriesURL;

        private Properties summaryStats;

        public RemoteTimeSeries(String parentPath, String id, String description, URL seriesURL, String summaryStats) {
            this.parentPath = parentPath;
            this.id = id;
            this.description = description;
            this.seriesURL = seriesURL;
            this.summaryStats = ContextProperties.getSummaryStatsProperties(summaryStats);
        }

        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public URL getSeriesURL() {
            return seriesURL;
        }

        public String getParentPath() {
            return parentPath;
        }

        public Properties getSummaryStatsProperties() {
            return summaryStats;
        }
    }
}
