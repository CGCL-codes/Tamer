package org.apache.hadoop.contrib.dlucene.writable;

import java.io.DataInput;
import java.io.IOException;
import junit.framework.TestCase;
import org.apache.hadoop.contrib.dlucene.TestConstants;
import org.apache.hadoop.contrib.dlucene.TestUtils;
import org.apache.hadoop.contrib.dlucene.LuceneIndex;
import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;

public class SearchResultsTest extends TestCase {

    private static SearchResults sr = new SearchResults();

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testSearchResults() {
        SearchResults q = new SearchResults();
        assertEquals(0, q.size());
    }

    private LuceneIndex makeLuceneIndex() throws Exception {
        LuceneIndex li = new LuceneIndex();
        li.indexArticle(TestConstants.NAME[0], "Life in bedrock", "Anthropology");
        li.indexArticle(TestConstants.NAME[1], "Dino's habits", "Anthropology");
        li.indexArticle(TestConstants.NAME[0], "Living with Wilma", "Bibliography");
        return li;
    }

    public void testSearchResultsHits() throws Exception {
        LuceneIndex li = makeLuceneIndex();
        Hits hits = li.search("author:Fred");
        sr = new SearchResults(hits);
        assertEquals(hits.length(), sr.size());
        for (int i = 0; i < hits.length(); i++) {
            assertEquals(hits.doc(i), sr.get(i));
        }
    }

    public void testReadNull() throws Exception {
        try {
            SearchResults.read((DataInput) null);
            fail("read() should have thrown an exception!");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testWriteNull() throws Exception {
        try {
            sr.write(null);
            fail("write() should have thrown an exception!");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testReadWrite() throws IOException {
        DataOutputBuffer out = new DataOutputBuffer();
        sr.write(out);
        DataInputBuffer in = new DataInputBuffer();
        in.reset(out.getData(), out.getLength());
        SearchResults qr = SearchResults.read(in);
        assertEquals(sr, qr);
    }

    public void testAddDocument() {
        int s = sr.size();
        Document doc = TestUtils.makeDocument("author", "William Shakespeare");
        sr.add(doc);
        assertEquals(s + 1, sr.size());
    }

    public void testAddSearchResults() {
        SearchResults orb = new SearchResults();
        orb.add(sr);
        assertEquals(sr.size(), orb.size());
    }

    public void testEquals() throws Exception {
        LuceneIndex li = makeLuceneIndex();
        SearchResults s1 = new SearchResults(li.search("author:Fred"));
        SearchResults s2 = new SearchResults(li.search("author:Fred"));
        SearchResults s3 = new SearchResults(li.search("author:Barney"));
        assertEquals(2, s1.size());
        assertEquals(2, s2.size());
        assertEquals(1, s3.size());
        assertEquals(s1, s2);
        assertTrue(!s1.equals(s3));
        assertTrue(!s2.equals(s3));
        assertNotNull(s1.toString());
        assertTrue(s1.hashCode() != 0);
    }
}
