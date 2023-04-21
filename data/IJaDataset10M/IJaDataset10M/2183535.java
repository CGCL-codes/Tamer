package org.apache.solr.client.solrj.embedded;

import org.apache.solr.client.solrj.LargeVolumeTestBase;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;

/**
 * @version $Id: LargeVolumeJettyTest.java 647048 2008-04-11 05:22:54Z hossman $
 * @since solr 1.3
 */
public class LargeVolumeJettyTest extends LargeVolumeTestBase {

    SolrServer server;

    JettySolrRunner jetty;

    int port = 0;

    static final String context = "/example";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        jetty = new JettySolrRunner(context, 0);
        jetty.start();
        port = jetty.getLocalPort();
        server = this.createNewSolrServer();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        jetty.stop();
    }

    @Override
    protected SolrServer getSolrServer() {
        return server;
    }

    @Override
    protected SolrServer createNewSolrServer() {
        try {
            String url = "http://localhost:" + port + context;
            CommonsHttpSolrServer s = new CommonsHttpSolrServer(url);
            s.setConnectionTimeout(100);
            s.setDefaultMaxConnectionsPerHost(100);
            s.setMaxTotalConnections(100);
            return s;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
