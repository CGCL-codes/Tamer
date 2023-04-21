package net.sf.katta.integrationTest.lib.lucene;

import java.io.File;
import java.util.List;
import java.util.Set;
import net.sf.katta.client.DeployClient;
import net.sf.katta.client.IDeployClient;
import net.sf.katta.client.IndexState;
import net.sf.katta.integrationTest.support.AbstractIntegrationTest;
import net.sf.katta.lib.lucene.DocumentFrequencyWritable;
import net.sf.katta.lib.lucene.Hit;
import net.sf.katta.lib.lucene.Hits;
import net.sf.katta.lib.lucene.ILuceneClient;
import net.sf.katta.lib.lucene.LuceneClient;
import net.sf.katta.node.Node;
import net.sf.katta.testutil.TestResources;
import net.sf.katta.testutil.TestUtil;
import net.sf.katta.util.KattaException;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Test for {@link LuceneClient}.
 */
public class LuceneClientTest extends AbstractIntegrationTest {

    private static Logger LOG = Logger.getLogger(LuceneClientTest.class);

    private static final String INDEX1 = "index1";

    private static final String INDEX2 = "index2";

    private static final String INDEX3 = "index3";

    public LuceneClientTest() {
        super(2);
    }

    @Test
    public void testAddRemoveIndices() throws Exception {
        ILuceneClient client = new LuceneClient(_protocol);
        IDeployClient deployClient = new DeployClient(_protocol);
        int listenerCountBeforeDeploys = _protocol.getRegisteredListenerCount();
        deployClient.addIndex("newIndex1", INDEX_FILE.getAbsolutePath(), 1).joinDeployment();
        deployClient.addIndex("newIndex2", INDEX_FILE.getAbsolutePath(), 1).joinDeployment();
        deployClient.addIndex("newIndex3", INDEX_FILE.getAbsolutePath(), 1).joinDeployment();
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("content: the");
        client.search(query, new String[] { "newIndex1" }, 10);
        deployClient.removeIndex("newIndex1");
        deployClient.removeIndex("newIndex2");
        deployClient.removeIndex("newIndex3");
        Thread.sleep(2000);
        assertEquals(listenerCountBeforeDeploys, _protocol.getRegisteredListenerCount());
    }

    @Test
    public void testInstantiateClientBeforeIndex() throws Exception {
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        deployTestIndices(1, getNodeCount());
        List<Node> nodes = _miniCluster.getNodes();
        for (Node node : nodes) {
            TestUtil.waitUntilNodeServesShards(_protocol, node.getName(), SHARD_COUNT);
        }
        Thread.sleep(2000);
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("content: the");
        client.count(query, new String[] { INDEX_NAME });
        client.close();
    }

    @Test
    public void testCount() throws Exception {
        deployTestIndices(1, 1);
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("content: the");
        final int count = client.count(query, new String[] { INDEX_NAME });
        assertEquals(937, count);
        client.close();
    }

    @Test
    public void testGetDetails() throws Exception {
        deployTestIndices(1, 1);
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("content: the");
        final Hits hits = client.search(query, new String[] { INDEX_NAME }, 10);
        assertNotNull(hits);
        assertEquals(10, hits.getHits().size());
        for (final Hit hit : hits.getHits()) {
            final MapWritable details = client.getDetails(hit);
            final Set<Writable> keySet = details.keySet();
            assertFalse(keySet.isEmpty());
            assertNotNull(details.get(new Text("path")));
            assertNotNull(details.get(new Text("category")));
        }
        client.close();
    }

    @Test
    public void testGetDetailsWithFieldNames() throws Exception {
        deployTestIndices(1, 1);
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("content: the");
        final Hits hits = client.search(query, new String[] { INDEX_NAME }, 10);
        assertNotNull(hits);
        assertEquals(10, hits.getHits().size());
        for (final Hit hit : hits.getHits()) {
            final MapWritable details = client.getDetails(hit, new String[] { "path" });
            final Set<Writable> keySet = details.keySet();
            assertFalse(keySet.isEmpty());
            assertNotNull(details.get(new Text("path")));
            assertNull(details.get(new Text("category")));
        }
        client.close();
    }

    @Test
    public void testGetBinaryDetails() throws Exception {
        File index = _temporaryFolder.newFolder("indexWithBinaryData");
        String textFieldName = "textField";
        String binaryFieldName = "binaryField";
        String textFieldContent = "sample text";
        byte[] bytesFieldContent = new byte[] { 1, 2, 3 };
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(index), new StandardAnalyzer(Version.LUCENE_30), true, MaxFieldLength.UNLIMITED);
        Document document = new Document();
        document.add(new Field(binaryFieldName, bytesFieldContent, Store.YES));
        document.add(new Field(textFieldName, textFieldContent, Store.NO, Index.ANALYZED));
        indexWriter.addDocument(document);
        indexWriter.optimize();
        indexWriter.close();
        DeployClient deployClient = new DeployClient(_miniCluster.getProtocol());
        IndexState indexState = deployClient.addIndex(index.getName(), index.getParentFile().getAbsolutePath(), 1).joinDeployment();
        assertEquals(IndexState.DEPLOYED, indexState);
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse(textFieldName + ": " + textFieldContent);
        final Hits hits = client.search(query, new String[] { index.getName() }, 10);
        assertNotNull(hits);
        assertEquals(1, hits.getHits().size());
        final Hit hit = hits.getHits().get(0);
        final MapWritable details = client.getDetails(hit);
        final Set<Writable> keySet = details.keySet();
        assertEquals(1, keySet.size());
        final Writable writable = details.get(new Text(binaryFieldName));
        assertNotNull(writable);
        assertThat(writable, instanceOf(BytesWritable.class));
        BytesWritable bytesWritable = (BytesWritable) writable;
        bytesWritable.setCapacity(bytesWritable.getLength());
        assertArrayEquals(bytesFieldContent, bytesWritable.getBytes());
        client.close();
    }

    @Test
    public void testGetDetailsConcurrently() throws KattaException, ParseException, InterruptedException {
        deployTestIndices(1, 1);
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("content: the");
        final Hits hits = client.search(query, new String[] { INDEX_NAME }, 10);
        assertNotNull(hits);
        assertEquals(10, hits.getHits().size());
        List<MapWritable> detailList = client.getDetails(hits.getHits());
        assertEquals(hits.getHits().size(), detailList.size());
        for (int i = 0; i < detailList.size(); i++) {
            final MapWritable details1 = client.getDetails(hits.getHits().get(i));
            final MapWritable details2 = detailList.get(i);
            assertEquals(details1.entrySet(), details2.entrySet());
            final Set<Writable> keySet = details2.keySet();
            assertFalse(keySet.isEmpty());
            final Writable writable = details2.get(new Text("path"));
            assertNotNull(writable);
        }
        client.close();
    }

    @Test
    public void testSearch() throws Exception {
        deploy3Indices();
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("foo: bar");
        final Hits hits = client.search(query, new String[] { INDEX3, INDEX2 });
        assertNotNull(hits);
        for (final Hit hit : hits.getHits()) {
            writeToLog(hit);
        }
        assertEquals(8, hits.size());
        assertEquals(8, hits.getHits().size());
        client.close();
    }

    @Test
    public void testFieldSortWithNoResultShard() throws Exception {
        File sortIndex1 = _temporaryFolder.newFolder("sortIndex1");
        File sortIndex2 = _temporaryFolder.newFolder("sortIndex2");
        IndexWriter indexWriter1 = new IndexWriter(FSDirectory.open(sortIndex1), new StandardAnalyzer(Version.LUCENE_30), true, MaxFieldLength.UNLIMITED);
        IndexWriter indexWriter2 = new IndexWriter(FSDirectory.open(sortIndex2), new StandardAnalyzer(Version.LUCENE_30), true, MaxFieldLength.UNLIMITED);
        Document document = new Document();
        document.add(new Field("text", "abc", Field.Store.YES, Index.NOT_ANALYZED));
        document.add(new NumericField("timesort", Field.Store.YES, false).setLongValue(1234567890123l));
        indexWriter1.addDocument(document);
        indexWriter1.close();
        document = new Document();
        document.add(new Field("text", "abc2", Field.Store.YES, Index.NOT_ANALYZED));
        document.add(new NumericField("timesort", Field.Store.YES, false).setLongValue(1234567890123l));
        indexWriter2.addDocument(document);
        indexWriter2.close();
        DeployClient deployClient = new DeployClient(_miniCluster.getProtocol());
        String indexName = "sortIndex";
        IndexState indexState = deployClient.addIndex(indexName, sortIndex1.getParentFile().getAbsolutePath(), 1).joinDeployment();
        assertEquals(IndexState.DEPLOYED, indexState);
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        Sort sort = new Sort(new SortField[] { new SortField("timesort", SortField.LONG) });
        Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("text:ab*");
        Hits hits = client.search(query, new String[] { indexName }, 20, sort);
        assertEquals(2, hits.size());
        query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("text:abc2");
        hits = client.search(query, new String[] { indexName }, 20, sort);
        assertEquals(1, hits.size());
        _miniCluster.shutdownNode(0);
        TestUtil.waitUntilIndexBalanced(_protocol, indexName);
        query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("text:abc2");
        hits = client.search(query, new String[] { indexName }, 20, sort);
        assertEquals(1, hits.size());
        client.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSortedSearch() throws Exception {
        File sortIndex = _temporaryFolder.newFolder("sortIndex2");
        String queryTerm = "2";
        String sortFieldName = "sortField";
        String textFieldName = "textField";
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(sortIndex), new StandardAnalyzer(Version.LUCENE_30), true, MaxFieldLength.UNLIMITED);
        for (int i = 0; i < 20; i++) {
            Document document = new Document();
            document.add(new Field(sortFieldName, "" + i, Store.NO, Index.NOT_ANALYZED));
            String textField = "sample text";
            if (i % 2 == 0) {
                for (int j = 0; j < i; j++) {
                    textField += " " + queryTerm;
                }
            }
            document.add(new Field(textFieldName, textField, Store.NO, Index.ANALYZED));
            indexWriter.addDocument(document);
        }
        indexWriter.optimize();
        indexWriter.close();
        DeployClient deployClient = new DeployClient(_miniCluster.getProtocol());
        IndexState indexState = deployClient.addIndex(sortIndex.getName(), sortIndex.getParentFile().getAbsolutePath(), 1).joinDeployment();
        assertEquals(IndexState.DEPLOYED, indexState);
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse(textFieldName + ": " + queryTerm);
        Sort sort = new Sort(new SortField[] { new SortField("sortField", SortField.INT) });
        final Hits hits = client.search(query, new String[] { sortIndex.getName() }, 20, sort);
        assertNotNull(hits);
        List<Hit> hitsList = hits.getHits();
        for (final Hit hit : hitsList) {
            writeToLog(hit);
        }
        assertEquals(9, hits.size());
        assertEquals(9, hitsList.size());
        assertEquals(1, hitsList.get(0).getSortFields().length);
        for (int i = 0; i < hitsList.size() - 1; i++) {
            int compareTo = hitsList.get(i).getSortFields()[0].compareTo(hitsList.get(i + 1).getSortFields()[0]);
            assertTrue("results not after field", compareTo == 0 || compareTo == -1);
        }
        client.close();
    }

    @Test
    public void testSearchLimit() throws Exception {
        deploy3Indices();
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("foo: bar");
        final Hits hits = client.search(query, new String[] { INDEX3, INDEX2 }, 1);
        assertNotNull(hits);
        for (final Hit hit : hits.getHits()) {
            writeToLog(hit);
        }
        assertEquals(8, hits.size());
        assertEquals(1, hits.getHits().size());
        for (final Hit hit : hits.getHits()) {
            LOG.info(hit.getNode() + " -- " + hit.getScore() + " -- " + hit.getDocId());
        }
        client.close();
    }

    @Test
    public void testKatta20SearchLimitMaxNumberOfHits() throws Exception {
        deployTestIndices(1, getNodeCount());
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("foo: bar");
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        final Hits expectedHits = client.search(query, new String[] { INDEX_NAME }, 4);
        assertNotNull(expectedHits);
        LOG.info("Expected hits:");
        for (final Hit hit : expectedHits.getHits()) {
            writeToLog(hit);
        }
        assertEquals(4, expectedHits.getHits().size());
        for (int i = 0; i < 100; i++) {
            for (int maxHits = 1; maxHits < expectedHits.size() + 1; maxHits++) {
                final Hits hits = client.search(query, new String[] { INDEX_NAME }, maxHits);
                assertNotNull(hits);
                assertEquals(maxHits, hits.getHits().size());
                for (int j = 0; j < hits.getHits().size(); j++) {
                    assertEquals(expectedHits.getHits().get(j).getScore(), hits.getHits().get(j).getScore(), 0.0);
                }
            }
        }
        client.close();
    }

    @Test
    public void testSearchSimiliarity() throws Exception {
        deploy3Indices();
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("foo: bar");
        final Hits hits = client.search(query, new String[] { INDEX2 });
        assertNotNull(hits);
        assertEquals(4, hits.getHits().size());
        for (final Hit hit : hits.getHits()) {
            LOG.info(hit.getNode() + " -- " + hit.getScore() + " -- " + hit.getDocId());
        }
        client.close();
    }

    @Test
    public void testNonExistentShard() throws Exception {
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("foo: bar");
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        try {
            client.search(query, new String[] { "doesNotExist" });
            fail("Should have failed.");
        } catch (KattaException e) {
            assertEquals("Index [pattern(s)] '[doesNotExist]' do not match to any deployed index: []", e.getMessage());
        }
        client.close();
    }

    @Test
    public void testIndexPattern() throws Exception {
        deploy3Indices();
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("foo: bar");
        final Hits hits = client.search(query, new String[] { "index[2-3]+" });
        assertNotNull(hits);
        for (final Hit hit : hits.getHits()) {
            writeToLog(hit);
        }
        assertEquals(8, hits.size());
        assertEquals(8, hits.getHits().size());
        client.close();
    }

    @Test
    public void testNumDocGreaterMaxInteger_KATTA_140() throws Exception {
        deployTestIndices(1, 1);
        LuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration()) {

            @Override
            protected DocumentFrequencyWritable getDocFrequencies(Query q, String[] indexNames) throws KattaException {
                DocumentFrequencyWritable docFreq = new DocumentFrequencyWritable();
                docFreq.put("foo", "bar", 23);
                docFreq.addNumDocs(Integer.MAX_VALUE);
                docFreq.addNumDocs(23);
                return docFreq;
            }
        };
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse("foo: bar");
        client.search(query, new String[] { INDEX_NAME }, 10, null);
        client.close();
    }

    @Test
    public void testFilteredSearch() throws Exception {
        File filterIndex = _temporaryFolder.newFolder("filterIndex1");
        String textFieldName = "textField";
        String filterFieldName = "filterField";
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(filterIndex), new StandardAnalyzer(Version.LUCENE_30), true, MaxFieldLength.UNLIMITED);
        for (int i = 0; i < 100; i++) {
            Document document = new Document();
            document.add(new Field(textFieldName, "sample " + i, Store.YES, Index.NOT_ANALYZED));
            document.add(new Field(filterFieldName, "" + (i % 10), Store.YES, Index.NOT_ANALYZED));
            indexWriter.addDocument(document);
        }
        indexWriter.optimize();
        indexWriter.close();
        DeployClient deployClient = new DeployClient(_miniCluster.getProtocol());
        IndexState indexState = deployClient.addIndex(filterIndex.getName(), filterIndex.getParentFile().getAbsolutePath(), 1).joinDeployment();
        assertEquals(IndexState.DEPLOYED, indexState);
        ILuceneClient client = new LuceneClient(_miniCluster.getZkConfiguration());
        TermQuery filterQuery = new TermQuery(new Term(filterFieldName, "3"));
        QueryWrapperFilter filter = new QueryWrapperFilter(filterQuery);
        final Query query = new QueryParser(Version.LUCENE_30, "", new KeywordAnalyzer()).parse(textFieldName + ":" + "sample*3");
        final Hits hits = client.search(query, new String[] { filterIndex.getName() }, 100, null, filter);
        assertNotNull(hits);
        List<Hit> hitsList = hits.getHits();
        for (final Hit hit : hitsList) {
            writeToLog(hit);
        }
        assertEquals(10, hits.size());
        assertEquals(10, hitsList.size());
        for (final Hit hit : hitsList) {
            MapWritable mw = client.getDetails(hit);
            Text text = (Text) mw.get(new Text("textField"));
            assertNotNull(text);
            String[] parts = text.toString().split(" ");
            assertTrue(parts.length == 2);
            int num = Integer.valueOf(parts[1]).intValue();
            assertTrue((num % 10) == 3);
        }
        client.close();
    }

    private void writeToLog(Hit hit) {
        LOG.info(hit.getNode() + " -- " + hit.getShard() + " -- " + hit.getScore() + " -- " + hit.getDocId());
    }

    private void deploy3Indices() throws Exception {
        DeployClient deployClient = new DeployClient(_miniCluster.getProtocol());
        deployClient.addIndex(INDEX1, TestResources.INDEX1.getAbsolutePath(), 1).joinDeployment();
        deployClient.addIndex(INDEX2, TestResources.INDEX1.getAbsolutePath(), 1).joinDeployment();
        deployClient.addIndex(INDEX3, TestResources.INDEX1.getAbsolutePath(), 1).joinDeployment();
    }
}
