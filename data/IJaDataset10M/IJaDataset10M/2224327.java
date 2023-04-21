package com.topnaukri.search.solr;

import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

public class SolrUtil {

    public static final String module = SolrUtil.class.getName();

    public static final String JOBSEEKER_SCHEMA = "JOBSEEKER";

    public static final String JOBPOST_SCHEMA = "JOBPOST";

    private static final String JOBSEEKER_URL = "http://localhost:8983/solr/jobseeker";

    private static final String JOBPOST_URL = "http://localhost:8983/solr/jobpost";

    public SolrUtil() {
    }

    public static SolrServer getSolrServer(String schema) throws MalformedURLException {
        SolrServer server = null;
        if (JOBSEEKER_SCHEMA.equals(schema)) {
            server = new CommonsHttpSolrServer(JOBSEEKER_URL);
        } else if (JOBPOST_SCHEMA.equals(schema)) {
            server = new CommonsHttpSolrServer(JOBPOST_URL);
        }
        System.out.println("schema -> " + schema);
        System.out.println("server -> " + server);
        return server;
    }

    public static UpdateResponse add(String schema, SolrInputDocument doc) throws MalformedURLException, IOException, SolrServerException {
        SolrServer server = getSolrServer(schema);
        UpdateResponse response = server.add(doc);
        System.out.println("response -> " + response);
        System.out.println("response -> " + response.getResponse());
        System.out.println("status -> " + response.getStatus());
        response = server.commit(true, true);
        System.out.println("response (commit) -> " + response);
        System.out.println("response -> " + response.getResponse());
        System.out.println("status -> " + response.getStatus());
        return response;
    }
}
