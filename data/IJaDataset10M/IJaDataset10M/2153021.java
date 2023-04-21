package com.megawaretech.dbconnurl.dbutils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Think, How Terrible it's to distribute a 30.2kb dependancy (DbUtils.jar) with
 * a library of 9.2kb (DBConnection.jar) Only (that too with examples and templates.)
 * That's why this class here.
 * 
 * From DbUtils library only QueryLoader.class and some of it's methods is requiered for us.
 * So made them repeat here.
 *
 * @author K.R.Arun (kra3@in.com)
 * @version 1.4
 */
public class QueryLoader {

    private static final QueryLoader instance = new QueryLoader();

    private final Map queries = new HashMap();

    public static QueryLoader instance() {
        return instance;
    }

    protected QueryLoader() {
        super();
    }

    @SuppressWarnings("unchecked")
    public synchronized Map load(String path) throws IOException {
        Map queryMap = (Map) this.queries.get(path);
        if (queryMap == null) {
            queryMap = this.loadQueries(path);
            this.queries.put(path, queryMap);
        }
        return queryMap;
    }

    @SuppressWarnings("unchecked")
    protected Map loadQueries(String path) throws IOException {
        InputStream in = getClass().getResourceAsStream(path);
        if (in == null) {
            throw new IllegalArgumentException(path + " not found.");
        }
        Properties props = new Properties();
        props.load(in);
        return new HashMap(props);
    }
}
