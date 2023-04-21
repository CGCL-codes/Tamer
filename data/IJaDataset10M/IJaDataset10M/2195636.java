package jorg.indexing;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jorg.gui.config.Configurator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneSearcher {

    private static final int getNumberFromConfigurator() {
        int standard = 60;
        try {
            standard = Integer.parseInt(Configurator.getSetupProprerty("number.results"));
        } catch (NumberFormatException numberFormatException) {
            System.out.println("LOG -- Assuming the standard (" + standard + ")");
        }
        return standard;
    }

    private static final void initialize(int[] documents) {
        for (int i = 0; i < documents.length; i++) {
            documents[i] = -1;
        }
    }

    private static IndexReader reader;

    private static Searcher searcher;

    private static Set<String> stopWords;

    private static Analyzer analyzer;

    private static QueryParser parser;

    private static Query query;

    public static final void resetReader() {
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception ex) {
            }
        }
        reader = null;
        searcher = null;
        analyzer = null;
        parser = null;
    }

    public static final int[] search(String value) throws CorruptIndexException, IOException, ParseException {
        if (value == null) {
            return new int[0];
        }
        if (value.trim().equals("")) {
            return new int[0];
        }
        value = value.replace(java.io.File.separator, " ").replace("_", " ") + " " + value.replace(".", " ");
        int[] documentId = null;
        final int hitsPerPage = getNumberFromConfigurator();
        if (reader == null) {
            reader = IndexReader.open(FSDirectory.open(LuceneIndexer.INDEX_DIR), true);
        }
        if (searcher == null) {
            searcher = new IndexSearcher(reader);
        }
        if (stopWords == null) {
            stopWords = new HashSet<String>();
            LuceneIndexer.fillUp(stopWords);
        }
        if (analyzer == null) {
            analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT, stopWords);
        }
        if (parser == null) {
            parser = new QueryParser(Version.LUCENE_CURRENT, LuceneIndexer.CONTENTS_FIELD, analyzer);
        }
        query = parser.parse(value);
        documentId = doPagingSearch(searcher, query, hitsPerPage, false, true);
        return documentId;
    }

    public static void closeReader() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(LuceneSearcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static final int[] doPagingSearch(final Searcher searcher, final Query query, final int hitsPerPage, final boolean raw, final boolean interactive) throws IOException {
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, false);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        int numTotalHits = collector.getTotalHits();
        int[] documents = new int[numTotalHits];
        initialize(documents);
        int index = 0;
        log(numTotalHits + " total matching documents");
        for (ScoreDoc sDoc : hits) {
            Document doc = searcher.doc(sDoc.doc);
            int id = Integer.parseInt(doc.get(LuceneIndexer.ID_FIELD));
            documents[index++] = id;
        }
        return documents;
    }

    public static final void log(final Object o) {
        System.out.println(new Date().toLocaleString() + "--LOG :" + o);
    }
}
