package analysis;

import junit.framework.TestCase;
import nlp.MockNaturalLanguageEngine;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LemmaAnalyzerTest extends TestCase {

    private RAMDirectory directory;

    private IndexSearcher searcher;

    private static LemmaAnalyzer lemmaAnalyzer;

    @Before
    public void setUp() throws Exception {
        lemmaAnalyzer = new LemmaAnalyzer(new MockNaturalLanguageEngine());
        directory = new RAMDirectory();
        IndexWriter writer = new IndexWriter(directory, lemmaAnalyzer, true, new MaxFieldLength(500));
        Document doc = new Document();
        doc.add(new Field("content", "The quick brown fox jumps over the lazy dogs", Store.NO, Index.ANALYZED));
        writer.addDocument(doc);
        writer.close();
        searcher = new IndexSearcher(directory);
    }

    @After
    public void tearDown() throws Exception {
        searcher.close();
    }

    @Test
    public void testSearchByAPITermQuery() throws Exception {
        TermQuery tq = new TermQuery(new Term("content", "jumps"));
        TopDocs hits = searcher.search(tq, 5);
        assertEquals(1, hits.totalHits);
    }

    @Test
    public void testSearchByAPIPhraseQuery() throws Exception {
        PhraseQuery pq = new PhraseQuery();
        pq.add(new Term("content", "fox"));
        pq.add(new Term("content", "jumps"));
        TopDocs hits = searcher.search(pq, 5);
        assertEquals(1, hits.totalHits);
    }

    @Test
    public void testWithQueryParserAndSynonymAnalyzerQ() throws Exception {
        QueryParser QP = new QueryParser("content", lemmaAnalyzer);
        Query query = QP.parse("\"fox jumps\"");
        TopDocs hits = searcher.search(query, 5);
        assertEquals("support exact phrase", 1, hits.totalHits);
    }

    @Test
    public void testQueryParserMorphologySupport() throws Exception {
        QueryParser QP = new QueryParser("content", lemmaAnalyzer);
        Query query = QP.parse("\"fox jumps\"");
        TopDocs hits = searcher.search(query, 5);
        assertEquals("support exact phrase", 1, hits.totalHits);
    }

    @Test
    public void testStandardAnalyzerWithoutMorphology() throws Exception {
        QueryParser QP1 = new QueryParser("content", new StandardAnalyzer());
        Query query = QP1.parse("\"fox jumps\"");
        TopDocs hits = searcher.search(query, 5);
        assertEquals("support morphology", 1, hits.totalHits);
    }
}
