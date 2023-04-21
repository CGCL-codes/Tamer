package org.jzonic.yawiki.search;

import java.util.List;
import java.util.Vector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.jconfig.Configuration;
import org.jconfig.ConfigurationManager;
import org.jzonic.jlo.LogManager;
import org.jzonic.jlo.Logger;

public class WikiSearcher {

    private static final Configuration cfg = ConfigurationManager.getConfiguration("jZonic");

    private static final Logger logger = LogManager.getLogger("org.jzonic.yawiki.search");

    public WikiSearcher() {
    }

    public List search(String text) {
        List allItems = new Vector();
        try {
            String indexPath = cfg.getProperty("index_path", "/tmp", "wiki");
            Searcher searcher = new IndexSearcher(indexPath);
            Analyzer analyzer = new StandardAnalyzer();
            Query query = QueryParser.parse(text, "contents", analyzer);
            logger.info("Searching for: " + query.toString("contents"));
            Hits hits = searcher.search(query);
            logger.info(hits.length() + " total matching documents");
            for (int i = 0; i < hits.length(); i++) {
                Document doc = hits.doc(i);
                allItems.add(doc.get("domain") + ":" + doc.get("name"));
            }
            searcher.close();
        } catch (Exception e) {
            logger.fatal("search", e);
        }
        return allItems;
    }

    public List searchMeta(String text) {
        List allItems = new Vector();
        try {
            String indexPath = cfg.getProperty("index_path", "/tmp", "wiki");
            Searcher searcher = new IndexSearcher(indexPath);
            Analyzer analyzer = new StandardAnalyzer();
            Query query = QueryParser.parse(text, "meta", analyzer);
            logger.info("Searching for: " + query.toString("contents"));
            Hits hits = searcher.search(query);
            logger.info(hits.length() + " total matching documents");
            for (int i = 0; i < hits.length(); i++) {
                Document doc = hits.doc(i);
                allItems.add(doc.get("domain") + ":" + doc.get("name"));
            }
            searcher.close();
        } catch (Exception e) {
            logger.fatal("search", e);
        }
        return allItems;
    }
}
