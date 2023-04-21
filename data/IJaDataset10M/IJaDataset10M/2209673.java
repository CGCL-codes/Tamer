package org.apache.solr.client.solrj.embedded;

import org.apache.solr.client.solrj.MultiCoreExampleTestBase;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;

/**
 * TODO? perhaps use:
 *  http://docs.codehaus.org/display/JETTY/ServletTester
 * rather then open a real connection?
 * 
 * @version $Id: MultiCoreExampleJettyTest.java 819804 2009-09-29 02:55:49Z yonik $
 * @since solr 1.3
 */
public class MultiCoreExampleJettyTest extends MultiCoreExampleTestBase {

    JettySolrRunner jetty;

    int port = 0;

    static final String context = "/example";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        jetty = new JettySolrRunner(context, 0);
        jetty.start(false);
        port = jetty.getLocalPort();
        h.getCoreContainer().setPersistent(false);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        jetty.stop();
    }

    @Override
    protected SolrServer getSolrCore(String name) {
        return createServer(name);
    }

    @Override
    protected SolrServer getSolrCore0() {
        return createServer("core0");
    }

    @Override
    protected SolrServer getSolrCore1() {
        return createServer("core1");
    }

    @Override
    protected SolrServer getSolrAdmin() {
        return createServer("");
    }

    private SolrServer createServer(String name) {
        try {
            String url = "http://localhost:" + port + context + "/" + name;
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
