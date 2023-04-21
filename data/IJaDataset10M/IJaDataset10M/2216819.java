package org.apache.solr.client.solrj.embedded;

import org.apache.solr.client.solrj.LargeVolumeTestBase;
import org.apache.solr.client.solrj.SolrServer;

/**
 * @version $Id: LargeVolumeEmbeddedTest.java 686780 2008-08-18 15:08:28Z yonik $
 * @since solr 1.3
 */
public class LargeVolumeEmbeddedTest extends LargeVolumeTestBase {

    SolrServer server;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        server = createNewSolrServer();
    }

    @Override
    protected SolrServer getSolrServer() {
        return server;
    }

    @Override
    protected SolrServer createNewSolrServer() {
        return new EmbeddedSolrServer(h.getCoreContainer(), "");
    }
}
