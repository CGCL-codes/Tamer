package com.cdbaby.lucene.handlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import com.cdbaby.lucene.IndexManager;
import com.cdbaby.lucene.LuceneWebService;
import com.cdbaby.lucene.ServletUtils;
import com.cdbaby.lucene.WebServiceException;

public abstract class AbstractSearch implements Handler {

    protected final int numMaxHits = 100;

    protected final int defaultMaxHits = 25;

    public void execute(LuceneWebService ws, HttpServletRequest req, HttpServletResponse res, String index, String method) throws IOException, WebServiceException {
        int maxHits = ServletUtils.getPositiveIntParameter(req, "maxhits", defaultMaxHits);
        int offset = ServletUtils.getPositiveIntParameter(req, "offset", 0);
        IndexManager indexManager = ws.getIndexManager();
        IndexSearcher indexSearcher = indexManager.getSearcher(index);
        if (indexSearcher == null) throw new WebServiceException(indexManager.getError(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Analyzer analyzer = indexManager.createAnalyzer(index);
        Query query = createQuery(req, res, analyzer);
        Hits hits = indexSearcher.search(query);
        ws.debug("<!-- " + query + " -->");
        printResults(res, index, method, hits, offset, maxHits);
    }

    protected abstract Query createQuery(HttpServletRequest req, HttpServletResponse res, Analyzer analyzer) throws WebServiceException;

    /**
	 * Write an XML snippet that contains search results.
	 * 
	 * @param res
	 * @param index name of the index used
	 * @param method
	 * @param hits
	 * @param offset offset to start with. zero-based
	 * @param maxHits don't print more than maxHits results
	 * @throws IOException 
	 */
    protected void printResults(HttpServletResponse res, String index, String method, Hits hits, int offset, int maxHits) throws IOException {
        res.setContentType("text/xml");
        PrintWriter out = res.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        maxHits = Math.min(maxHits, numMaxHits);
        int start = offset;
        int stop = Math.min(start + maxHits, Math.max(start, hits.length()));
        out.println("<searchresults>");
        out.println("  <index>" + index + "</index>");
        out.println("  <hits total=\"" + hits.length() + "\">" + (stop - start) + "</hits>");
        out.println("  <searchresult>");
        for (int i = start; i < stop; i++) {
            try {
                Document doc = hits.doc(i);
                out.println("    <document hit=\"" + i + "\">");
                out.println("      <score>" + (int) (hits.score(i) * 100) + "</score>");
                for (Enumeration e = doc.fields(); e.hasMoreElements(); ) {
                    Field field = (Field) e.nextElement();
                    out.println("      <field name=\"" + field.name() + "\">" + escape(field.stringValue()) + "</field>");
                }
                out.println("    </document>");
            } catch (IOException e) {
                continue;
            }
        }
        out.println("  </searchresult>");
        out.println("</searchresults>");
    }

    private String escape(String text) {
        text = text.replaceAll("&", "&amp;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        return text;
    }
}
