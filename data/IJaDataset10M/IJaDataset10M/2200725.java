package org.apache.lucene.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import java.io.IOException;

/**
 * Tests {@link MultiSearcher} class.
 *
 * @version $Id: TestMultiSearcher.java 505038 2007-02-08 21:12:49Z dnaber $
 */
public class TestMultiSearcherWithWrapper extends TestCase {

    public TestMultiSearcherWithWrapper(String name) {
        super(name);
    }

    public static void main(String[] argv) {
        TestRunner.run(suite());
        System.exit(0);
    }

    public static Test suite() {
        return new TestSuite(TestMultiSearcherWithWrapper.class);
    }

    /**
	 * ReturnS a new instance of the concrete MultiSearcher class
	 * used in this test.
	 */
    protected Searcher getMultiSearcherInstance(Searcher[] searchers) throws IOException {
        return new SearcherHCSourceWrapper(new MultiSearcher(searchers));
    }

    public void testEmptyIndex() throws Exception {
        Directory indexStoreA = new RAMDirectory();
        Directory indexStoreB = new RAMDirectory();
        Document lDoc = new Document();
        lDoc.add(new Field("fulltext", "Once upon a time.....", Field.Store.YES, Field.Index.TOKENIZED));
        lDoc.add(new Field("id", "doc1", Field.Store.YES, Field.Index.UN_TOKENIZED));
        lDoc.add(new Field("handle", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
        Document lDoc2 = new Document();
        lDoc2.add(new Field("fulltext", "in a galaxy far far away.....", Field.Store.YES, Field.Index.TOKENIZED));
        lDoc2.add(new Field("id", "doc2", Field.Store.YES, Field.Index.UN_TOKENIZED));
        lDoc2.add(new Field("handle", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
        Document lDoc3 = new Document();
        lDoc3.add(new Field("fulltext", "a bizarre bug manifested itself....", Field.Store.YES, Field.Index.TOKENIZED));
        lDoc3.add(new Field("id", "doc3", Field.Store.YES, Field.Index.UN_TOKENIZED));
        lDoc3.add(new Field("handle", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
        IndexWriter writerA = new IndexWriter(indexStoreA, new StandardAnalyzer(), true);
        IndexWriter writerB = new IndexWriter(indexStoreB, new StandardAnalyzer(), true);
        writerA.addDocument(lDoc);
        writerA.addDocument(lDoc2);
        writerA.addDocument(lDoc3);
        writerA.optimize();
        writerA.close();
        writerB.close();
        QueryParser parser = new QueryParser("fulltext", new StandardAnalyzer());
        Query query = parser.parse("handle:1");
        Searcher[] searchers = new Searcher[2];
        searchers[0] = new SearcherHCSourceWrapper(new IndexSearcher(indexStoreB));
        searchers[1] = new SearcherHCSourceWrapper(new IndexSearcher(indexStoreA));
        Searcher mSearcher = getMultiSearcherInstance(searchers);
        Hits hits = mSearcher.search(query);
        assertEquals(3, hits.length());
        for (int i = 0; i < hits.length(); i++) {
            Document d = hits.doc(i);
        }
        mSearcher.close();
        writerB = new IndexWriter(indexStoreB, new StandardAnalyzer(), false);
        writerB.addDocument(lDoc);
        writerB.optimize();
        writerB.close();
        Searcher[] searchers2 = new Searcher[2];
        searchers2[0] = new SearcherHCSourceWrapper(new IndexSearcher(indexStoreB));
        searchers2[1] = new SearcherHCSourceWrapper(new IndexSearcher(indexStoreA));
        Searcher mSearcher2 = getMultiSearcherInstance(searchers2);
        Hits hits2 = mSearcher2.search(query);
        assertEquals(4, hits2.length());
        for (int i = 0; i < hits2.length(); i++) {
            Document d = hits2.doc(i);
        }
        mSearcher2.close();
        Query subSearcherQuery = parser.parse("id:doc1");
        hits2 = mSearcher2.search(subSearcherQuery);
        assertEquals(2, hits2.length());
        subSearcherQuery = parser.parse("id:doc2");
        hits2 = mSearcher2.search(subSearcherQuery);
        assertEquals(1, hits2.length());
        Term term = new Term("id", "doc1");
        IndexReader readerB = IndexReader.open(indexStoreB);
        readerB.deleteDocuments(term);
        readerB.close();
        writerB = new IndexWriter(indexStoreB, new StandardAnalyzer(), false);
        writerB.optimize();
        writerB.close();
        Searcher[] searchers3 = new Searcher[2];
        searchers3[0] = new SearcherHCSourceWrapper(new IndexSearcher(indexStoreB));
        searchers3[1] = new SearcherHCSourceWrapper(new IndexSearcher(indexStoreA));
        Searcher mSearcher3 = getMultiSearcherInstance(searchers3);
        Hits hits3 = mSearcher3.search(query);
        assertEquals(3, hits3.length());
        for (int i = 0; i < hits3.length(); i++) {
            Document d = hits3.doc(i);
        }
        mSearcher3.close();
    }

    private static Document createDocument(String contents1, String contents2) {
        Document document = new Document();
        document.add(new Field("contents", contents1, Field.Store.YES, Field.Index.UN_TOKENIZED));
        if (contents2 != null) {
            document.add(new Field("contents", contents2, Field.Store.YES, Field.Index.UN_TOKENIZED));
        }
        return document;
    }

    private static void initIndex(Directory directory, int nDocs, boolean create, String contents2) throws IOException {
        IndexWriter indexWriter = null;
        try {
            indexWriter = new IndexWriter(directory, new KeywordAnalyzer(), create);
            for (int i = 0; i < nDocs; i++) {
                indexWriter.addDocument(createDocument("doc" + i, contents2));
            }
        } finally {
            if (indexWriter != null) {
                indexWriter.close();
            }
        }
    }

    public void testNormalization10() throws IOException {
        testNormalization(10, "Using 10 documents per index:");
    }

    private void testNormalization(int nDocs, String message) throws IOException {
        Query query = new TermQuery(new Term("contents", "doc0"));
        RAMDirectory ramDirectory1;
        Searcher indexSearcher1;
        Hits hits;
        ramDirectory1 = new RAMDirectory();
        initIndex(ramDirectory1, nDocs, true, null);
        initIndex(ramDirectory1, nDocs, false, "x");
        indexSearcher1 = new SearcherHCSourceWrapper(new IndexSearcher(ramDirectory1));
        hits = indexSearcher1.search(query);
        assertEquals(message, 2, hits.length());
        assertEquals(message, 1, hits.score(0), 1e-6);
        float[] scores = { hits.score(0), hits.score(1) };
        assertTrue(message, scores[0] > scores[1]);
        indexSearcher1.close();
        ramDirectory1.close();
        hits = null;
        RAMDirectory ramDirectory2;
        Searcher indexSearcher2;
        ramDirectory1 = new RAMDirectory();
        ramDirectory2 = new RAMDirectory();
        initIndex(ramDirectory1, nDocs, true, null);
        initIndex(ramDirectory2, nDocs, true, "x");
        indexSearcher1 = new SearcherHCSourceWrapper(new IndexSearcher(ramDirectory1));
        indexSearcher2 = new SearcherHCSourceWrapper(new IndexSearcher(ramDirectory2));
        Searcher searcher = getMultiSearcherInstance(new Searcher[] { indexSearcher1, indexSearcher2 });
        hits = searcher.search(query);
        assertEquals(message, 2, hits.length());
        assertEquals(message, scores[0], hits.score(0), 1e-6);
        assertEquals(message, scores[1], hits.score(1), 1e-6);
        hits = searcher.search(query, Sort.RELEVANCE);
        assertEquals(message, 2, hits.length());
        assertEquals(message, scores[0], hits.score(0), 1e-6);
        assertEquals(message, scores[1], hits.score(1), 1e-6);
        searcher.close();
        ramDirectory1.close();
        ramDirectory2.close();
    }
}
