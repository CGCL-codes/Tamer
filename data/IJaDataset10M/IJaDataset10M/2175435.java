package at.ac.univie.mminf.luceneSKOS.analysis;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import at.ac.univie.mminf.luceneSKOS.analysis.SKOSAnalyzer.ExpansionType;
import at.ac.univie.mminf.luceneSKOS.util.AnalyzerUtils;
import at.ac.univie.mminf.luceneSKOS.util.TestUtil;

/**
 * Testing the SKOS Label Filter
 * 
 * @author Bernhard Haslhofer <bernhard.haslhofer@univie.ac.at>
 * @author Martin Kysel <martin.kysel@univie.ac.at>
 * 
 */
public class SKOSLabelFilterTest extends AbstractFilterTest {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        skosAnalyzer = new SKOSAnalyzer(skosEngine, ExpansionType.LABEL);
        writer = new IndexWriter(directory, skosAnalyzer, IndexWriter.MaxFieldLength.UNLIMITED);
    }

    @Test
    public void termQuerySearch() throws Exception {
        Document doc = new Document();
        doc.add(new Field("content", "The quick brown fox jumps over the lazy dog", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);
        searcher = new IndexSearcher(writer.getReader());
        TermQuery tq = new TermQuery(new Term("content", "hops"));
        Assert.assertEquals(1, TestUtil.hitCount(searcher, tq));
    }

    @Test
    public void phraseQuerySearch() throws Exception {
        Document doc = new Document();
        doc.add(new Field("content", "The quick brown fox jumps over the lazy dog", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);
        searcher = new IndexSearcher(writer.getReader());
        PhraseQuery pq = new PhraseQuery();
        pq.add(new Term("content", "fox"));
        pq.add(new Term("content", "hops"));
        Assert.assertEquals(1, TestUtil.hitCount(searcher, pq));
    }

    @Test
    public void queryParserSearch() throws Exception {
        Document doc = new Document();
        doc.add(new Field("content", "The quick brown fox jumps over the lazy dog", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);
        searcher = new IndexSearcher(writer.getReader());
        Query query = new QueryParser(Version.LUCENE_30, "content", skosAnalyzer).parse("\"fox jumps\"");
        Assert.assertEquals(1, TestUtil.hitCount(searcher, query));
        Assert.assertEquals("content:\"fox (jumps hops leaps)\"", query.toString());
        Assert.assertEquals("org.apache.lucene.search.MultiPhraseQuery", query.getClass().getName());
        query = new QueryParser(Version.LUCENE_30, "content", new StandardAnalyzer(Version.LUCENE_30)).parse("\"fox jumps\"");
        Assert.assertEquals(1, TestUtil.hitCount(searcher, query));
        Assert.assertEquals("content:\"fox jumps\"", query.toString());
        Assert.assertEquals("org.apache.lucene.search.PhraseQuery", query.getClass().getName());
    }

    @Test
    public void testTermQuery() throws Exception {
        Document doc = new Document();
        doc.add(new Field("content", "I work for the united nations", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);
        searcher = new IndexSearcher(writer.getReader());
        QueryParser parser = new QueryParser(Version.LUCENE_30, "content", new SimpleAnalyzer());
        Query query = parser.parse("united nations");
        Assert.assertEquals(1, TestUtil.hitCount(searcher, query));
    }

    public void displayTokensWithLabelExpansion() throws Exception {
        String text = "The quick brown fox jumps over the lazy dog";
        AnalyzerUtils.displayTokensWithFullDetails(skosAnalyzer, text);
    }
}
