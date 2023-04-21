package org.tm4j.tologx.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.tm4j.net.Locator;
import org.tm4j.tologx.TologResultsSet;
import org.tm4j.tologx.QueryEvaluator;
import org.tm4j.tologx.QueryEvaluatorFactory;
import org.tm4j.tologx.TologProcessingException;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;
import org.tm4j.topicmap.TopicMapProvider;
import org.tm4j.topicmap.TopicMapProviderException;
import org.tm4j.topicmap.TopicMapProviderFactory;
import org.tm4j.topicmap.source.SerializedTopicMapSource;

/**
 * @author Kal
 * 
 * Describe MemoryQueryEvaluatorImplTest here.
 */
public class MemoryQueryEvaluatorImplTest extends TestCase {

    private File m_tologTestDir;

    private TopicMapProvider m_provider;

    private TestQuery[] TEST_QUERIES = new TestQuery[] { new TestQuery("blocks1", "blocks.ltm", "select * from restson($A:upper, $B:lower)?", 2, 3, new Object[][] { new Object[] { new TopicRef("redblock"), new TopicRef("blueblock") } }), new TestQuery("blocks2", "blocks.ltm", "select $INST from direct-instance-of($INST, block)?", 1, 4, new Object[][] { new Object[] { new TopicRef("redblock") }, new Object[] { new TopicRef("blueblock") }, new Object[] { new TopicRef("greenblock") }, new Object[] { new TopicRef("yellowblock") } }), new TestQuery("blocks3", "blocks.ltm", "select $BLOCK, $OTHER, $SHAPE  from restson($OTHER:lower, $BLOCK:upper), has-shape($OTHER:block, $SHAPE:shape)?", 3, 3, new Object[][] { new Object[] { new TopicRef("redblock"), new TopicRef("blueblock"), new TopicRef("cylinder") }, new Object[] { new TopicRef("redblock"), new TopicRef("greenblock"), new TopicRef("rectangle") }, new Object[] { new TopicRef("yellowblock"), new TopicRef("redblock"), new TopicRef("rectangle") } }) };

    /**
	 * Constructor for MemoryQueryEvaluatorImplTest.
	 * @param arg0
	 */
    public MemoryQueryEvaluatorImplTest(String arg0) {
        super(arg0);
        m_tologTestDir = getTologTestDirectory();
        try {
            m_provider = initialiseProvider();
        } catch (TopicMapProviderException e) {
            throw new RuntimeException("Unable to initialise topic map provider.", e);
        }
    }

    /**
	 * Locates the directory containing tologx package test
	 * resources. The directory is located by looking for
	 * a subdirectory 'tologx' in the test directory specified
	 * by the System property 'testdir'
	 * @return the File that represents the tologx test resource directory.
	 * @throws RuntimeException if the System property 'testdir' is not specified, 
	 *         or if the test directory or the tologx subdirectory could not be found.
	 */
    public File getTologTestDirectory() {
        String testdirName = System.getProperty("testdir");
        if (testdirName == null) {
            throw new RuntimeException("No value for system property 'testdir'");
        }
        File f = new File(testdirName);
        if (!(f.exists() && f.isDirectory())) {
            throw new RuntimeException("File '" + testdirName + "' does not exist or is not a directory.");
        }
        File ret = new File(f, "tologx");
        if (!(ret.exists() && ret.isDirectory())) {
            throw new RuntimeException("Could not find subdirectory 'tologx' in test directory '" + f.getAbsolutePath() + "'.");
        }
        return ret;
    }

    public TopicMapProvider initialiseProvider() throws TopicMapProviderException {
        TopicMapProviderFactory tmpf = TopicMapProviderFactory.newInstance();
        return tmpf.newTopicMapProvider(System.getProperties());
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MemoryQueryEvaluatorImplTest.class);
    }

    public TopicMap getTopicMap(File tmFile, boolean forceReload) throws Exception {
        Locator tmLoc = m_provider.getLocatorFactory().createLocator("URI", tmFile.toURL().toString());
        TopicMap tm = m_provider.getTopicMap(tmLoc);
        if (tm != null) {
            if (forceReload) {
                tm.destroy();
            } else {
                return tm;
            }
        }
        SerializedTopicMapSource src = new SerializedTopicMapSource(tmFile);
        return m_provider.addTopicMap(src);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testSimpleQueryWithProjection() throws Exception {
        TopicMap tm = getTopicMap(new File(m_tologTestDir, "blocks.ltm"), false);
        assertNotNull(tm);
        QueryEvaluator qe = QueryEvaluatorFactory.newQueryEvaluator(tm);
        TologResultsSet results = qe.execute("select $A, $B from restson($A:upper, $B:lower)?");
        assertNotNull(results);
        assertEquals(2, results.getNumCols());
        assertEquals(3, results.getNumRows());
        Object[][] expectedResults = new Object[][] { new Object[] { tm.getObjectByID("redblock"), tm.getObjectByID("blueblock") } };
        checkResultsSet("simpleQuery", results, 2, 3, expectedResults);
    }

    public void testQueries() throws Exception {
        for (int i = 0; i < TEST_QUERIES.length; i++) {
            TopicMap tm = getTopicMap(new File(m_tologTestDir, TEST_QUERIES[i].tm), false);
            for (int j = 0; j < TEST_QUERIES[i].expectedRows.length; j++) {
                Object[] expectedRow = TEST_QUERIES[i].expectedRows[j];
                for (int k = 0; k < expectedRow.length; k++) {
                    if (expectedRow[k] instanceof TopicRef) {
                        expectedRow[k] = tm.getObjectByID(((TopicRef) expectedRow[k]).id);
                    }
                }
            }
            QueryEvaluator qe = QueryEvaluatorFactory.newQueryEvaluator(tm);
            try {
                TologResultsSet rs = qe.execute(TEST_QUERIES[i].queryString);
                if (TEST_QUERIES[i].parseErrorExpected) {
                    fail(TEST_QUERIES[i].name + ": Expected a TologParserException");
                }
                if (TEST_QUERIES[i].processingErrorExpected) {
                    fail(TEST_QUERIES[i].name + ": Expected a TologProcessingException");
                }
                checkResultsSet(TEST_QUERIES[i].name, rs, TEST_QUERIES[i].resultsCols, TEST_QUERIES[i].resultsRows, TEST_QUERIES[i].expectedRows);
            } catch (TologProcessingException ex) {
                if (TEST_QUERIES[i].processingErrorExpected) return;
            }
        }
    }

    public void testInlineModule() throws Exception {
        TopicMap tm = getTopicMap(new File(m_tologTestDir, "blocks.ltm"), false);
        Topic greenblock = tm.getTopicByID("greenblock");
        Topic blueblock = tm.getTopicByID("blueblock");
        assertNotNull(tm);
        QueryEvaluator qe = QueryEvaluatorFactory.newQueryEvaluator(tm);
        qe.addRule("rests-on-both($A, $B, $C) :- restson($A:upper, $B:lower), restson($A:upper, $C:lower), $C /= $B .");
        TologResultsSet results = qe.execute("rests-on-both(redblock, $BLOCK1, $BLOCK2)?");
        assertEquals(2, results.getNumRows());
        List row = results.getRow(0);
        assertTrue((row.get(0).equals(blueblock) && row.get(1).equals(greenblock)) || (row.get(0).equals(greenblock) && row.get(1).equals(blueblock)));
        row = results.getRow(1);
        assertTrue((row.get(0).equals(blueblock) && row.get(1).equals(greenblock)) || (row.get(0).equals(greenblock) && row.get(1).equals(blueblock)));
    }

    public void testExternalModule() throws Exception {
        TopicMap tm = getTopicMap(new File(m_tologTestDir, "blocks.ltm"), false);
        Topic greenblock = tm.getTopicByID("greenblock");
        Topic blueblock = tm.getTopicByID("blueblock");
        QueryEvaluator qe = QueryEvaluatorFactory.newQueryEvaluator(tm);
        qe.addRulesModule(new FileInputStream(new File(m_tologTestDir, "blocks.tl")), "blocks");
        TologResultsSet results = qe.execute("select $BLOCK1, $BLOCK2 from blocks::rests-on-both(redblock, $BLOCK1, $BLOCK2)?");
        assertEquals(2, results.getNumRows());
        List row = results.getRow(0);
        assertTrue((row.get(0).equals(blueblock) && row.get(1).equals(greenblock)) || (row.get(0).equals(greenblock) && row.get(1).equals(blueblock)));
        row = results.getRow(1);
        assertTrue((row.get(0).equals(blueblock) && row.get(1).equals(greenblock)) || (row.get(0).equals(greenblock) && row.get(1).equals(blueblock)));
    }

    public void testRecursiveRules() throws Exception {
        TopicMap tm = getTopicMap(new File(m_tologTestDir, "family.ltm"), false);
        QueryEvaluator qe = QueryEvaluatorFactory.newQueryEvaluator(tm);
        qe.addRule("descendant-of($A, $B) :- { parent-of($B:parent, $A:child) | parent-of($A:child, $C:parent), descendant-of($C, $B) }.");
        TologResultsSet rs = qe.execute("descendant-of(bart, $WHO) ?");
        assertEquals(3, rs.getNumRows());
        Topic homer = tm.getTopicByID("homer");
        Topic marge = tm.getTopicByID("marge");
        Topic granpa = tm.getTopicByID("granpa");
        for (int i = 0; i < 3; i++) {
            List row = rs.getRow(i);
            Topic t = (Topic) row.get(0);
            System.out.println(t.getID());
            assertTrue(row.contains(homer) || row.contains(marge) || row.contains(granpa));
        }
    }

    public void testNameAndOccurrence() throws Exception {
        TopicMap tm = getTopicMap(new File(m_tologTestDir, "blocks.ltm"), false);
        QueryEvaluator qe = QueryEvaluatorFactory.newQueryEvaluator(tm);
        TologResultsSet rs = qe.execute("select $A, $D, $E from topic-name($A, $B), occurrence($A, $C), value($B, $D), value($C, $E)?");
        for (int i = 0; i < rs.getNumRows(); i++) {
            System.out.println(dumpRow(rs.getRow(i)));
        }
    }

    public void testReplacement() throws Exception {
        TopicMap tm = getTopicMap(new File(m_tologTestDir, "blocks.ltm"), false);
        QueryEvaluator qe = QueryEvaluatorFactory.newQueryEvaluator(tm);
        TologResultsSet rst = qe.execute("select $A from restson($A:upper, %1:lower)?", new Object[] { tm.getTopicByID("redblock") });
        assertEquals(1, rst.getNumRows());
        assertEquals(tm.getTopicByID("yellowblock"), rst.getRow(0).get(0));
    }

    private void checkResultsSet(String testName, TologResultsSet results, int expectedColSize, int expectedRowSize, Object[][] expectedRows) {
        assertEquals(testName + ": Unexpected results set width.", expectedColSize, results.getNumCols());
        assertEquals(testName + ": Unexpected results set length.", expectedRowSize, results.getNumRows());
        for (int i = 0; i < expectedRows.length; i++) {
            List expectedRow = Arrays.asList(expectedRows[i]);
            boolean gotMatch = false;
            for (int j = 0; (!gotMatch) && (j < results.getNumRows()); j++) {
                gotMatch = expectedRow.equals(results.getRow(j));
            }
            assertTrue(testName + ": Expected to find a match for the row " + dumpRow(expectedRow), gotMatch);
        }
    }

    private String dumpRow(List row) {
        StringBuffer ret = new StringBuffer();
        ret.append("[");
        Iterator it = row.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof TopicMapObject) {
                ret.append(((TopicMapObject) o).getID());
            } else {
                ret.append(o.toString());
            }
            if (it.hasNext()) ret.append(", ");
        }
        ret.append("]");
        return ret.toString();
    }

    class TestQuery {

        String name;

        String tm;

        String queryString;

        boolean parseErrorExpected;

        boolean processingErrorExpected;

        int resultsCols;

        int resultsRows;

        Object[][] expectedRows;

        public TestQuery(String name, String tm, String queryString, boolean parseErrorExpected, boolean processingErrorExpected) {
            this.name = name;
            this.tm = tm;
            this.queryString = queryString;
            this.parseErrorExpected = parseErrorExpected;
            this.processingErrorExpected = processingErrorExpected;
        }

        public TestQuery(String name, String tm, String queryString, int resultsCols, int resultsRows, Object[][] expectedRows) {
            this.name = name;
            this.tm = tm;
            this.queryString = queryString;
            this.resultsCols = resultsCols;
            this.resultsRows = resultsRows;
            this.expectedRows = expectedRows;
        }
    }

    class TopicRef {

        String id;

        TopicRef(String id) {
            this.id = id;
        }
    }
}
