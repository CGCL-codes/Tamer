package org.scicm.search;

import java.io.*;
import java.util.*;
import java.sql.*;
import org.apache.lucene.index.*;
import org.apache.lucene.document.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;
import org.apache.lucene.analysis.*;
import org.apache.log4j.Logger;
import org.apache.oro.text.perl.Perl5Util;
import org.scicm.content.Collection;
import org.scicm.content.Community;
import org.scicm.core.ConfigurationManager;
import org.scicm.core.Constants;
import org.scicm.core.Context;
import org.scicm.core.LogManager;

public class DSQuery {

    static final String ALL = "999";

    static final String ITEM = "" + Constants.ITEM;

    static final String COLLECTION = "" + Constants.COLLECTION;

    static final String COMMUNITY = "" + Constants.COMMUNITY;

    private static Searcher searcher;

    private static long lastModified;

    /** log4j logger */
    private static Logger log = Logger.getLogger(DSQuery.class);

    /** Do a query, returning a List of SciCM Handles to objects matching the query.
     *  @param query string in Lucene query syntax
     *
     *  @return HashMap with lists for items, communities, and collections
     *        (keys are strings from Constants.ITEM, Constants.COLLECTION, etc.
     */
    public static QueryResults doQuery(Context c, QueryArgs args) throws IOException {
        String querystring = args.getQuery();
        QueryResults qr = new QueryResults();
        List hitHandles = new ArrayList();
        List hitTypes = new ArrayList();
        qr.setHitHandles(hitHandles);
        qr.setHitTypes(hitTypes);
        qr.setStart(args.getStart());
        qr.setPageSize(args.getPageSize());
        querystring = checkEmptyQuery(querystring);
        querystring = workAroundLuceneBug(querystring);
        querystring = stripHandles(querystring);
        querystring = stripAsterisk(querystring);
        try {
            Searcher searcher = getSearcher(ConfigurationManager.getProperty("search.dir"));
            QueryParser qp = new QueryParser("default", new DSAnalyzer());
            Query myquery = qp.parse(querystring);
            Hits hits = searcher.search(myquery);
            qr.setHitCount(hits.length());
            if (args.getStart() < hits.length()) {
                int hitsRemaining = hits.length() - args.getStart();
                int hitsToProcess = (hitsRemaining < args.getPageSize()) ? hitsRemaining : args.getPageSize();
                for (int i = args.getStart(); i < args.getStart() + hitsToProcess; i++) {
                    Document d = hits.doc(i);
                    String handleText = d.get("handle");
                    String handletype = d.get("type");
                    hitHandles.add(handleText);
                    if (handletype.equals("" + Constants.ITEM)) {
                        hitTypes.add(new Integer(Constants.ITEM));
                    } else if (handletype.equals("" + Constants.COLLECTION)) {
                        hitTypes.add(new Integer(Constants.COLLECTION));
                    } else if (handletype.equals("" + Constants.COMMUNITY)) {
                        hitTypes.add(new Integer(Constants.COMMUNITY));
                    } else {
                    }
                }
            }
        } catch (NumberFormatException e) {
            log.warn(LogManager.getHeader(c, "Number format exception", "" + e));
            qr.setErrorMsg("Number format exception");
        } catch (ParseException e) {
            log.warn(LogManager.getHeader(c, "Invalid search string", "" + e));
            qr.setErrorMsg("Invalid search string");
        } catch (TokenMgrError tme) {
            log.warn(LogManager.getHeader(c, "Invalid search string", "" + tme));
            qr.setErrorMsg("Invalid search string");
        }
        return qr;
    }

    static String checkEmptyQuery(String myquery) {
        if (myquery.equals("")) {
            myquery = "empty_query_string";
        }
        return myquery;
    }

    static String workAroundLuceneBug(String myquery) {
        Perl5Util util = new Perl5Util();
        myquery = util.substitute("s/ AND / && /g", myquery);
        myquery = util.substitute("s/ OR / || /g", myquery);
        myquery = util.substitute("s/ NOT / ! /g", myquery);
        myquery = myquery.toLowerCase();
        return myquery;
    }

    static String stripHandles(String myquery) {
        Perl5Util util = new Perl5Util();
        myquery = util.substitute("s|^(\\s+)?http://hdl\\.handle\\.net/||", myquery);
        myquery = util.substitute("s|^(\\s+)?hdl:||", myquery);
        return myquery;
    }

    static String stripAsterisk(String myquery) {
        Perl5Util util = new Perl5Util();
        myquery = util.substitute("s/^\\*//", myquery);
        myquery = util.substitute("s| \\*| |", myquery);
        myquery = util.substitute("s|\\(\\*|\\(|", myquery);
        myquery = util.substitute("s|:\\*|:|", myquery);
        return myquery;
    }

    /** Do a query, restricted to a collection
     * @param query
     * @param collection
     *
     * @return QueryResults same results as doQuery, restricted to a collection
     */
    public static QueryResults doQuery(Context c, QueryArgs args, Collection coll) throws IOException {
        String querystring = args.getQuery();
        querystring = checkEmptyQuery(querystring);
        String location = "l" + (coll.getID());
        String newquery = new String("+(" + querystring + ") +location:\"" + location + "\"");
        args.setQuery(newquery);
        return doQuery(c, args);
    }

    /** Do a query, restricted to a community
     * @param querystring
     * @param community
     *
     * @return HashMap results, same as full doQuery, only hits in a Community
     */
    public static QueryResults doQuery(Context c, QueryArgs args, Community comm) throws IOException {
        String querystring = args.getQuery();
        querystring = checkEmptyQuery(querystring);
        String location = "m" + (comm.getID());
        String newquery = new String("+(" + querystring + ") +location:\"" + location + "\"");
        args.setQuery(newquery);
        return doQuery(c, args);
    }

    /** return everything from a query
     * @param results hashmap from doQuery
     *
     * @return List of all objects returned by search
     */
    public static List getResults(HashMap results) {
        return ((List) results.get(ALL));
    }

    /** return just the items from a query
     * @param results hashmap from doQuery
     *
     * @return List of items found by query
     */
    public static List getItemResults(HashMap results) {
        return ((List) results.get(ITEM));
    }

    /** return just the collections from a query
     * @param results hashmap from doQuery
     *
     * @return List of collections found by query
     */
    public static List getCollectionResults(HashMap results) {
        return ((List) results.get(COLLECTION));
    }

    /** return just the communities from a query
     * @param results hashmap from doQuery
     *
     * @return list of Communities found by query
     */
    public static List getCommunityResults(HashMap results) {
        return ((List) results.get(COMMUNITY));
    }

    /** returns true if anything found
     * @param results hashmap from doQuery
     *
     * @return true if anything found, false if nothing
     */
    public static boolean resultsFound(HashMap results) {
        List thislist = getResults(results);
        return (!thislist.isEmpty());
    }

    /** returns true if items found
     * @param results hashmap from doQuery
     *
     * @return true if items found, false if none found
     */
    public static boolean itemsFound(HashMap results) {
        List thislist = getItemResults(results);
        return (!thislist.isEmpty());
    }

    /** returns true if collections found
     * @param results hashmap from doQuery
     *
     * @return true if collections found, false if none
     */
    public static boolean collectionsFound(HashMap results) {
        List thislist = getCollectionResults(results);
        return (!thislist.isEmpty());
    }

    /** returns true if communities found
     * @param results hashmap from doQuery
     *
     * @return true if communities found, false if none
     */
    public static boolean communitiesFound(HashMap results) {
        List thislist = getCommunityResults(results);
        return (!thislist.isEmpty());
    }

    /** Do a query, printing results to stdout
     *  largely for testing, but it is useful
     */
    public static void doCMDLineQuery(String query) {
        System.out.println("Command line query: " + query);
        System.out.println("Only reporting default-sized results list");
        try {
            Context c = new Context();
            QueryArgs args = new QueryArgs();
            args.setQuery(query);
            QueryResults results = doQuery(c, args);
            Iterator i = results.getHitHandles().iterator();
            Iterator j = results.getHitTypes().iterator();
            while (i.hasNext()) {
                String thisHandle = (String) i.next();
                Integer thisType = (Integer) j.next();
                String type = Constants.typeText[thisType.intValue()];
                System.out.println(type + "\t" + thisHandle);
            }
        } catch (Exception e) {
            System.out.println("Exception caught: " + e);
        }
    }

    public static void main(String[] args) {
        DSQuery q = new DSQuery();
        if (args.length > 0) {
            q.doCMDLineQuery(args[0]);
        }
    }

    /**
     * get an IndexSearcher, hopefully a cached one
     *  (gives much better performance.) checks to see
     *  if the index has been modified - if so, it
     *  creates a new IndexSearcher
     */
    private static synchronized Searcher getSearcher(String indexDir) throws IOException {
        if (lastModified != IndexReader.lastModified(indexDir)) {
            lastModified = IndexReader.lastModified(indexDir);
            searcher = new IndexSearcher(indexDir);
        }
        return searcher;
    }
}
