package com.unsins.test.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: odpsoft
 * Date: 2009-2-6
 * Time: 17:04:15
 */
public class LuceneQueryTest {

    /**
     * This class is used to demonstrate the
     * process of searching on an existing
     * Lucene index
     *
     */
    public static void main(String[] args) throws Exception {
        String queryStr = "保险";
        File indexDir = new File("C:\\Documents and Settings\\odpsoft\\searchengine\\unsins\\compass\\index\\product\\");
        FSDirectory directory = FSDirectory.getDirectory(indexDir);
        IndexSearcher searcher = new IndexSearcher(directory);
        if (!indexDir.exists()) {
            System.out.println("The Lucene index is not exist");
            return;
        }
        Term term = new Term("pdName");
        TermQuery luceneQuery = new TermQuery(term);
        Hits hits = searcher.search(luceneQuery);
        for (int i = 0; i < hits.length(); i++) {
            Document document = hits.doc(i);
            System.out.println("File: " + document.get("path"));
        }
    }
}
