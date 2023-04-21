package org.fao.geonet.kernel.search.log;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import jeeves.resources.dbms.Dbms;
import jeeves.utils.Log;
import jeeves.utils.SerialFactory;
import org.fao.geonet.constants.Geonet;

/**
 * A bean representing a query request.
 * Performs the mapping with the database.
 * @author nicolas
 *
 */
public class QueryRequest {

    /** the date format when inserting date into database */
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String QUERY_TYPE_MDS = "service";

    /** the constant for service MD query type */
    public static final String QUERY_TYPE_MDD = "dataset";

    /** the constant for geodata query type */
    public static final String QUERY_TYPE_GEO = "basicgeodata";

    /** the constant for geodata query type */
    public static final String QUERY_TYPE_ALL = "all";

    /** The db unique identifier for the stored request */
    int requestId;

    /** the QueryInfo objects generated during this query: search terms and search fields */
    private Vector<QueryInfo> queryInfos;

    /** The date of the Query */
    private Date date;

    /** the formatted date to insert into database (varchar field...) */
    private String formattedDate;

    /** The client IP making the search query */
    private String ip;

    /** the lucene query */
    private String luceneQuery;

    /** the number of hits for this query */
    private Integer hits;

    /** the query language */
    private String language;

    /** the query sort by clause */
    private String sortBy;

    /** the query spatial filter
	 * todo: store a true geotools spatialFilter ? 
	 */
    private String spatialFilter;

    /** the metadata searched type. see static constants for the list of types. */
    private String mdType;

    /** the service use to do the search */
    private String service;

    /** true if this query is simple (concerned only the What, type, Canton fields), false otherwise
	 * 
	 */
    private boolean simpleQuery;

    /**
	 * ctor taking the client ip and query date
	 * todo: should remove dependency from Jeeves services ?
	 */
    public QueryRequest(String ip, long queryDate) {
        this.ip = ip;
        this.date = new Date(queryDate);
        this.formattedDate = new SimpleDateFormat(QueryRequest.DATE_FORMAT).format(this.date);
        this.mdType = QueryRequest.QUERY_TYPE_ALL;
    }

    public Vector<QueryInfo> getQueryInfos() {
        return queryInfos;
    }

    /**
	 * sets the queryinfo object and also look into all queryInfos to see if a query type can be guessed
	 * @param queryInfos
	 */
    public void setQueryInfos(Vector<QueryInfo> queryInfos) {
        this.queryInfos = queryInfos;
        if (queryInfos != null) {
            for (QueryInfo qi : queryInfos) {
                String t = qi.getMdQueryType();
                if (t != null) {
                    this.setMdType(t);
                    break;
                }
            }
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public String getLuceneQuery() {
        return luceneQuery;
    }

    public void setLuceneQuery(String luceneQuery) {
        this.luceneQuery = luceneQuery;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSpatialFilter() {
        return spatialFilter;
    }

    public void setSpatialFilter(String spatialFilter) {
        this.spatialFilter = spatialFilter;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public String getMdType() {
        return mdType;
    }

    public void setMdType(String mdtype) {
        this.mdType = mdtype;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return service;
    }

    /**
	 * sets simpleQuery value and returns this value:
	 * true if this Query is simple:
	 * its QueryInfos elements concern only Lucene +any, or +type, or kantone elements 
	 * => if queryInfo has a field different from:
	 * (any, type, _isTemplate, _locale) then its an advanced query<br/>
	 * todo: move list of fields into a configuration; to avoid hard-coded values to guess if query is simple
	 * @return if this query is simple.
	 */
    public boolean isSimpleQuery() {
        simpleQuery = false;
        if (this.queryInfos != null) {
            simpleQuery = true;
            for (QueryInfo qi : this.queryInfos) {
                if (QueryInfo.MATCH_ALL_DOCS_QUERY.equals(qi.getLuceneQueryType()) || !"any".equals(qi.getField()) && !"type".equals(qi.getField()) && !"_owner".equals(qi.getField()) && !qi.getField().contains("_op") && !"_isTemplate".equals(qi.getField()) && !"_locale".equals(qi.getField())) {
                    simpleQuery = false;
                    break;
                }
            }
        }
        return simpleQuery;
    }

    /**
	 * Stores this object to 
	 * @param dbms
	 * @return
	 */
    public boolean storeToDb(Dbms dbms, SerialFactory sf) {
        if (dbms == null || dbms.isClosed()) {
            Log.debug(Geonet.SEARCH_LOGGER, "null or closed dbms object");
            return false;
        }
        try {
            this.requestId = sf.getSerial(dbms, "Requests");
            String query = "insert into requests(id,requestDate, ip, query, ";
            query += "hits, lang, sortBy, spatialFilter,type,simple,service) values(?,?,?,?,?,?,?,?,?,?,?)";
            int res = dbms.execute(query, this.requestId, this.getFormattedDate(), this.getIp(), this.getLuceneQuery(), this.getHits(), this.getLanguage(), this.getSortBy(), this.getSpatialFilter(), this.getMdType(), (this.simpleQuery ? 1 : 0), this.getService());
            Log.debug(Geonet.SEARCH_LOGGER, res + " queryRequest inserted (id: " + this.requestId + ")");
            if (this.queryInfos != null) {
                for (QueryInfo qi : this.queryInfos) {
                    if (qi != null) {
                        if (!qi.storeToDb(dbms, sf, this.requestId)) {
                            Log.warning(Geonet.SEARCH_LOGGER, "unable to log this QueryInfo: " + qi.toString());
                        }
                    } else {
                        Log.debug(Geonet.SEARCH_LOGGER, "null queryInfo object to store ??!!");
                    }
                }
            } else {
                Log.debug(Geonet.SEARCH_LOGGER, "No queryInfo objects to insert into database for this query: " + this.luceneQuery);
            }
            Log.debug(Geonet.SEARCH_LOGGER, "commiting query to db...");
            dbms.commit();
        } catch (SQLException sqle) {
            dbms.abort();
            Log.warning(Geonet.SEARCH_LOGGER, "an error occuring during QueryRequest database storage. Aborting :" + sqle.getMessage() + "\n" + "Is rollback occured ?");
            sqle.printStackTrace();
        } finally {
        }
        return true;
    }
}
