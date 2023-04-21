package com.apelon.apelonserver.server;

import com.apelon.apelonserver.ApelonHeader;
import com.apelon.apelonserver.client.ApelonException;
import com.apelon.apelonserver.util.ObjPooler;
import com.apelon.common.log4j.Categories;
import com.apelon.common.sql.ConnectionParams;
import com.apelon.common.sql.SQL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class maintains a pool of query server. A query server represents
 * a database connection and execution unit. Whenever Apelon OntylogDTS
 * server receives a query, a query is assinged to one of query servers in
 * the pool and the query server processes a query and returns the result to
 * a client.
 * The class also maintains a cache for the faster search. The cache is
 * shared by query servers in the pool.
 *
 * @author Apelon, Inc
 * @version 2.0.0
 *          Copyright (c) 1997-2001 Apelon, Inc. All rights reserved.
 */
class QueryServerPool extends ObjPooler {

    protected Map query_cache;

    protected int sizeOfCache = 0;

    private String[] app_args;

    private Class query_server = null;

    private ConnectionParams connParams;

    /**
     * This constructor builds a cache. The size of cache is 100
     */
    public QueryServerPool() {
        query_cache = Collections.synchronizedMap(new LinkedHashMap(16, (float) 0.75, true) {

            private static final int MAX_ENTRIES = 100;

            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAX_ENTRIES;
            }
        });
    }

    /**
     * This method instanitates a query server and creates database connection
     * and intializes the query server. In SocketServer, when multiple database
     * connections are created, all the connections are passed to upperlayer's
     * setConnections().
     * If one of connection is failed to instantiate,
     * null is set for the connecton to upperlayer.
     *
     * @return query server instance from query server pool
     * @throws Exception thrown if there is an error in creating or initializing
     *                   query servers.
     */
    public Object create() throws Exception {
        QueryServer qs = (QueryServer) query_server.newInstance();
        qs.setCache(query_cache, sizeOfCache);
        try {
            if (!ApelonHeader.DISABLE_DB) {
                Connection c = getConnection(connParams);
                qs.setConnection(c);
            }
            qs.setApplicationArgs(app_args);
            qs.init();
            Categories.dataServer().info("Query Server " + qs.getClass() + " is started.");
        } catch (Exception e) {
            shutDown(qs);
            throw e;
        }
        return qs;
    }

    /**
     * This method get database connection. If connection cannot be obtained
     * null is retured.
     *
     * @param params database connection parameters
     * @return database connection
     * @throws ApelonException on db connection error
     */
    private Connection getConnection(ConnectionParams params) throws ApelonException {
        String conParams = "user=" + params.getUserName() + ",hostname=" + params.getHostName() + ",port=" + params.getPort() + ",instance=" + params.getInstance() + ",type=" + params.getType() + ",driver=" + params.getJDBCDriver();
        try {
            Connection c = SQL.getConnection(params);
            return c;
        } catch (SQLException sqle) {
            String errorMsg = "Server could not create database connection -> " + conParams;
            Categories.dataServer().error(errorMsg, sqle);
            throw new ApelonException(errorMsg, sqle);
        }
    }

    /**
     * This method gets one of query servers in the pool and assingns
     * the query to the query server. Once the query server process the query,
     * the result is returned to a client. Then it returns the query server
     * to the pool for later use.
     *
     * @param query XML formatted query string
     * @return xml formatted string of the query result
     * @throws Exception thrown if there is an error in processig query.
     */
    public String executeQuery(String query, Permissions permit) throws Throwable {
        QueryServer qs = (QueryServer) checkOut();
        if (qs != null) {
            try {
                String s = qs.executeQuery(query, permit);
                checkIn(qs);
                return s;
            } catch (Error e) {
                e.printStackTrace();
                Categories.dataServer().fatal("FATAL ERROR: " + e.getMessage());
                throw new Exception("FATAL ERROR: " + e.getMessage());
            } finally {
                checkIn(qs);
            }
        } else {
            throw new Exception("no resource to be had");
        }
    }

    /**
     * This method returns the number of the current
     * active database connection in a pool.
     *
     * @return the number of active database connection.
     */
    public int getActiveDbConnCount() {
        return this.getActiveObjectsCount();
    }

    /**
     * This method sets the multiple database connection parameters to the
     * pool. Each element in the connParams represents connection parameters
     * and connection type. When a query server instance is created,
     * connections to databases also will be created using connection
     * parameters.
     *
     * @param connParams multiple database connection parameters
     */
    public void setConnectionArgs(ConnectionParams connParams) {
        this.connParams = connParams;
    }

    /**
     * This method saves query server parameters in the pool.
     * These parameters are used by query server. This method is
     * reserved for the future.
     *
     * @param args upper layer specific arguments
     */
    public void setApplicationArgs(String[] args) {
        app_args = args;
    }

    /**
     * This method sets the maximum size of cache. and creates new cache.
     *
     * @param n the maximum size of cache
     */
    public void setMaxCacheSize(final int n) {
        sizeOfCache = n;
        if (query_server != null) {
            Categories.dataServer().info("Size of the cache in " + query_server.getName() + " is " + n);
        } else {
            Categories.dataServer().info("Size of the cache in " + this.getClass().getName() + " is " + n);
        }
        query_cache = Collections.synchronizedMap(new LinkedHashMap(16, (float) 0.75, true) {

            private final int MAX_ENTRIES = n;

            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAX_ENTRIES;
            }
        });
    }

    /**
     * This method flushes the cache.
     */
    protected void flushCache() {
        query_cache.clear();
    }

    /**
     * This method sets the maxmimum number of query servers in the pool
     *
     * @param size the maximum size of pool.
     */
    public void setMaxPoolSize(int size) {
        max_no_objs = size;
    }

    /**
     * This method sets the type of this pool. The pool is homogenuous query
     * server pool. It means that the pool cannot contain a different query server.
     *
     * @param qs server class
     */
    public void setQueryServer(Class qs) {
        Categories.dataServer().info("Setting class for Query Server " + qs);
        query_server = qs;
    }

    /**
     * This method releases any resource used by a query server when a server
     * shuts down.
     *
     * @param obj query server
     */
    public void shutDown(Object obj) {
        QueryServer qs = (QueryServer) obj;
        qs.shutdown();
        Categories.dataServer().info("Query Server " + qs.getClass() + " is shutdown.");
    }

    /**
     * This method checks if query server is in a good state. Since query
     * server contains database connection, if there is an error in the database,
     * it is considered that query server is in bad state.
     * <p/>
     * TODO: add additional call to queryServerIsValid
     *
     * @param obj QueryServer instance
     * @return true if queryserver is in good state otherwise false.
     */
    public boolean validate(Object obj) {
        QueryServer qs = (QueryServer) obj;
        if (qs.isValid()) {
            return qs.queryServerIsValid();
        }
        return false;
    }
}
