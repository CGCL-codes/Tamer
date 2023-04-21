package au.edu.ausstage.exchange.items;

import au.edu.ausstage.utils.*;
import au.edu.ausstage.exchange.types.*;
import au.edu.ausstage.exchange.builders.*;
import java.util.ArrayList;
import java.sql.ResultSet;

/**
 * The main driving class for the collation of event data using contributor ids
 */
public class ContentIndicatorData extends BaseData {

    /**
	 * Constructor for this class
	 *
	 * @param database    the DbManager class used to connect to the database
	 * @param ids         the array of unique contributor ids
	 * @param outputType  the output type
	 * @param recordLimit the record limit
	 * @param sortOrder   the order that records must be sorted in
	 *
	 * @throws IllegalArgumentException if any of the parameters are empty or do not pass validation
	 *
	 */
    public ContentIndicatorData(DbManager database, String[] ids, String outputType, String recordLimit, String sortOrder) {
        super(database, ids, outputType, recordLimit, sortOrder);
    }

    @Override
    public String getEventData() {
        String sql;
        DbObjects results;
        Event event;
        String venue;
        String firstDate;
        ArrayList<Event> eventList = new ArrayList<Event>();
        String[] ids = getIds();
        if (ids.length == 1) {
            sql = "SELECT e.eventid, e.event_name, e.yyyyfirst_date, e.mmfirst_date, e.ddfirst_date, " + "       v.venueid, v.venue_name, v.street, v.suburb, s.state, v.postcode, " + "       c.countryname " + "FROM events e, venue v, country c, states s " + "WHERE e.content_indicator = ? " + "AND e.venueid = v.venueid " + "AND v.countryid = c.countryid (+) " + "AND v.state = s.stateid (+) ";
        } else {
            sql = "SELECT e.eventid, e.event_name, e.yyyyfirst_date, e.mmfirst_date, e.ddfirst_date, " + "       v.venueid, v.venue_name, v.street, v.suburb, s.state, v.postcode, " + "       c.countryname " + "FROM events e, venue v, country c, states s " + "WHERE e.content_indicator = ANY (";
            for (int i = 0; i < ids.length; i++) {
                sql += "?,";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += ") " + "AND e.venueid = v.venueid " + "AND v.countryid = c.countryid (+) " + "AND v.state = s.stateid (+) ";
        }
        String sort = getSortOrder();
        if (sort.equals("firstdate") == true) {
            sql += "ORDER BY e.yyyyfirst_date DESC, e.mmfirst_date DESC, e.ddfirst_date DESC";
        } else if (sort.equals("createdate") == true) {
            sql += "ORDER BY e.entered_date DESC";
        } else if (sort.equals("updatedate") == true) {
            sql += "ORDER BY e.updated_date DESC";
        }
        results = getDatabase().executePreparedStatement(sql, ids);
        if (results == null) {
            throw new RuntimeException("unable to lookup event data");
        }
        ResultSet resultSet = results.getResultSet();
        try {
            while (resultSet.next()) {
                venue = buildShortVenueAddress(resultSet.getString(12), resultSet.getString(8), resultSet.getString(9), resultSet.getString(10));
                venue = resultSet.getString(7) + ", " + venue;
                firstDate = DateUtils.buildDisplayDate(resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
                event = new Event(resultSet.getString(1), resultSet.getString(2), venue, firstDate);
                eventList.add(event);
            }
        } catch (java.sql.SQLException ex) {
            throw new RuntimeException("unable to build list of events: " + ex.toString());
        }
        resultSet = null;
        results.tidyUp();
        results = null;
        if (getRecordLimit().equals("all") == false) {
            int limit = getRecordLimitAsInt();
            if (eventList.size() > limit) {
                ArrayList<Event> list = new ArrayList<Event>();
                for (int i = 0; i < limit; i++) {
                    list.add(eventList.get(i));
                }
                eventList = list;
                list = null;
            }
        }
        String data = null;
        if (getOutputType().equals("html") == true) {
            data = EventDataBuilder.buildHtml(eventList);
        } else if (getOutputType().equals("json")) {
            data = EventDataBuilder.buildJson(eventList);
        } else if (getOutputType().equals("xml")) {
            data = EventDataBuilder.buildXml(eventList);
        } else if (getOutputType().equals("rss") == true) {
            data = EventDataBuilder.buildRss(eventList);
        }
        return data;
    }

    @Override
    public String getResourceData() {
        String sql;
        DbObjects results;
        Resource resource;
        ArrayList<Resource> resourceList = new ArrayList<Resource>();
        String[] ids = getIds();
        if (ids.length == 1) {
            sql = "SELECT i.itemid, i.citation, COALESCE(i.title, 'Untitled') " + "FROM item i, itemcontentindlink icil " + "WHERE icil.itemid = i.itemid " + "AND icil.contentindicatorid = ?";
        } else {
            sql = "SELECT i.itemid, i.citation, COALESCE(i.title, 'Untitled') " + "FROM item i, itemcontentindlink icil " + "WHERE icil.itemid = i.itemid " + "AND icil.contentindicatorid = ANY (";
            for (int i = 0; i < ids.length; i++) {
                sql += "?,";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += ") ";
        }
        String sort = getSortOrder();
        if (sort.equals("createdate") == true) {
            sql += " ORDER BY i.entered_date DESC";
        } else if (sort.equals("updatedate") == true) {
            sql += "ORDER BY i.updated_date DESC";
        }
        results = getDatabase().executePreparedStatement(sql, ids);
        if (results == null) {
            throw new RuntimeException("unable to lookup resource data");
        }
        ResultSet resultSet = results.getResultSet();
        try {
            while (resultSet.next()) {
                resource = new Resource(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
                resourceList.add(resource);
            }
        } catch (java.sql.SQLException ex) {
            throw new RuntimeException("unable to build list of resources: " + ex.toString());
        }
        resultSet = null;
        results.tidyUp();
        results = null;
        if (getRecordLimit().equals("all") == false) {
            int limit = getRecordLimitAsInt();
            if (resourceList.size() > limit) {
                ArrayList<Resource> list = new ArrayList<Resource>();
                for (int i = 0; i < limit; i++) {
                    list.add(resourceList.get(i));
                }
                resourceList = list;
                list = null;
            }
        }
        String data = null;
        if (getOutputType().equals("html") == true) {
            data = ResourceDataBuilder.buildHtml(resourceList);
        } else if (getOutputType().equals("json")) {
            data = ResourceDataBuilder.buildJson(resourceList);
        } else if (getOutputType().equals("xml")) {
            data = ResourceDataBuilder.buildXml(resourceList);
        } else if (getOutputType().equals("rss") == true) {
            data = ResourceDataBuilder.buildRss(resourceList);
        }
        return data;
    }
}
