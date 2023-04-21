package org.osee.indexer.indexProcessor;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocCollector;
import org.osee.indexer.indexBuilder.IndexDir;

public class FinanceIndexProcessor {

    private String url, content, title;

    public String getURL() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public FinanceResult[] get(String keyword) {
        String field1 = "content";
        String[] fields = { field1 };
        String query1 = keyword;
        String[] queries = { query1 };
        BooleanClause.Occur[] clauses = { BooleanClause.Occur.MUST };
        try {
            Query query = MultiFieldQueryParser.parse(queries, fields, clauses, new WhitespaceAnalyzer());
            IndexSearcher searcher = new IndexSearcher(IndexDir.getDir());
            TopDocCollector collector = new TopDocCollector(IndexProcessorParameter.searchResultNumber);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            FinanceResult[] fr = new FinanceResult[hits.length];
            for (int i = 0; i < hits.length; i++) {
                int docID = hits[i].doc;
                Document doc = searcher.doc(docID);
                fr[i] = new FinanceResult(doc.getField("time").toString(), doc.getField("url").toString(), doc.getField("title").toString());
                fr[i].print();
            }
            return fr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
