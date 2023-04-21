package com.plugtree.solrmeter.mock;

import java.net.MalformedURLException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import com.plugtree.solrmeter.model.executor.UpdateExecutorRandomImpl;

public class UpdateExecutorMock extends UpdateExecutorRandomImpl {

    private SolrServerMock server;

    public synchronized CommonsHttpSolrServer getSolrServer() {
        if (server == null) {
            try {
                server = new SolrServerMock();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return server;
    }

    public SolrInputDocument getNextDocument() {
        return new SolrInputDocument();
    }

    @Override
    public void notifyAddedDocument(UpdateResponse response) {
    }

    @Override
    public void notifyCommitSuccessfull(UpdateResponse response) {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        super.stop();
    }
}
