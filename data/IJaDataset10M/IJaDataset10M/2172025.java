package gov.lanl.adore.solr;

import org.apache.solr.servlet.DirectSolrConnection;

public class AdoreSolrIndexer {

    private DirectSolrConnection solr = null;

    private StringBuilder buf = null;

    private int count = 0;

    public static final int BATCH_SIZE = 1000;

    public AdoreSolrIndexer() {
        System.out.println("Writing documents to: " + System.getProperty("solr.solr.home"));
        this.solr = new DirectSolrConnection();
        this.buf = new StringBuilder();
    }

    public void optimize() throws Exception {
        solr.request("/update", "<optimize/>");
    }

    public void addSolrDocument(String solrDoc) throws Exception {
        if (solrDoc != null && solrDoc.length() > 0) {
            buf.append(solrDoc).append('\n');
            count++;
            if (count % BATCH_SIZE == 0) {
                buf.insert(0, "<add>\n");
                buf.append("</add>\n");
                solr.request("/update", buf.toString());
                buf.setLength(0);
            }
        }
    }

    public int getCount() {
        return this.count;
    }

    public int close() throws Exception {
        if (buf.length() > 0) {
            buf.insert(0, "<add>\n");
            buf.append("</add>\n");
            solr.request("/update", buf.toString());
        }
        solr.request("/update", "<commit/>");
        solr = null;
        return getCount();
    }
}
