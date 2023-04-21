package com.raidan.dclog.core.render.queries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.raidan.dclog.core.Config;

public class QueryLoaderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testGetInstance() throws IOException {
        QueryLoader instance = QueryLoader.getInstance();
        assertTrue(instance == QueryLoader.getInstance());
        assertTrue(instance == QueryLoader.getInstance());
    }

    @Test
    public final void testLoadQueries() throws IOException {
        QueryLoader.getInstance().loadQueries();
        Map<String, Query> q = QueryLoader.getInstance().getQueries();
        Map<String, Query> m = QueryLoader.getInstance().getCycleTemplates();
        assertNotNull(q);
        assertNotNull(m);
        System.out.println(q);
        System.out.println(m);
    }

    @Test
    public final void testGetQueries() throws IOException {
        Map<String, Query> q = QueryLoader.getInstance().getQueries();
        assertNotNull(q);
        assertFalse(q == QueryLoader.getInstance().getQueries());
    }

    @Test
    public final void testGetCycleTemplates() throws IOException {
        Map<String, Query> m = QueryLoader.getInstance().getCycleTemplates();
        assertNotNull(m);
        assertFalse(m == QueryLoader.getInstance().getCycleTemplates());
    }

    @Test
    public void testLoadQueries4Correct() throws Exception {
        Config.getInstance().setConfigName("config-test.properties");
        QueryLoader.getInstance().loadQueries("com/raidan/dclog/core/render/queries/test/");
        Config.getInstance().resetConfigName();
        Map<String, Query> queries = QueryLoader.getInstance().getQueries();
        Map<String, Query> templates = QueryLoader.getInstance().getCycleTemplates();
        Set<String> expected = new LinkedHashSet<String>();
        expected.add("report-common-test");
        expected.add("report-1-main");
        expected.add("report-4-main-non-skipped");
        expected.add("report-4-main-cycle-skipped");
        assertEquals(expected, queries.keySet());
        expected = new HashSet<String>();
        expected.add("report-1-cycle");
        expected.add("report-4-cycle");
        assertEquals(expected, templates.keySet());
        Query q = queries.get("report-common-test");
        assertEquals("report-common-test", q.getName());
        assertEquals("SAMPLE REPORT. Sample 1.", q.getDescription());
        assertEquals("select row1, row2 from table_X1 order by row1", q.getSql());
        assertTrue(q.isSystem());
        assertFalse(q.isCycle());
        assertNull(q.getCycleColumn());
        assertNull(q.getCycleTemplate());
        q = queries.get("report-1-main");
        assertEquals("report-1-main", q.getName());
        assertEquals("SAMPLE REPORT. Sample report 1 (master).", q.getDescription());
        assertEquals("select row1, row2, 123, 'Alex' from table_X1 order by row1", q.getSql());
        assertTrue(q.isSystem());
        assertTrue(q.isCycle());
        assertEquals("2", q.getCycleColumn());
        assertEquals("report-1-cycle", q.getCycleTemplate());
        q = queries.get("report-4-main-non-skipped");
        assertTrue(q.isSystem());
        assertTrue(q.isCycle());
        assertEquals("1", q.getCycleColumn());
        assertEquals("report-1-cycle", q.getCycleTemplate());
        q = queries.get("report-4-main-cycle-skipped");
        assertTrue(q.isSystem());
        assertFalse(q.isCycle());
        assertNull(q.getCycleColumn());
        assertNull(q.getCycleTemplate());
        q = templates.get("report-1-cycle");
        assertEquals("report-1-cycle", q.getName());
        assertEquals("select row1, row2, row3, 'for-sample' as macros from table_X1 where row1 = %cycle% order by row1", q.getSql());
        assertTrue(q.isSystem());
        assertFalse(q.isCycle());
        assertNull(q.getCycleColumn());
        assertNull(q.getCycleTemplate());
        q = templates.get("report-4-cycle");
        assertEquals("report-4-cycle", q.getName());
        assertTrue(q.isSystem());
        assertFalse(q.isCycle());
        assertNull(q.getCycleColumn());
        assertNull(q.getCycleTemplate());
    }

    @Test
    public void testLoadQueries4InCorrect2() throws IOException {
        try {
            QueryLoader.getInstance().loadQueries("com/raidan/dclog/core/render/queries/test2/");
        } catch (InvalidCycleDependentException icoc) {
            System.out.println("Received expected exception: " + icoc);
        }
    }

    @Test
    public void testLoadQueries4InCorrect3() throws IOException {
        try {
            QueryLoader.getInstance().loadQueries("com/raidan/dclog/core/render/queries/test3/");
        } catch (InvalidCycleOverCycleException icoc) {
            System.out.println("Received expected exception: " + icoc);
        }
    }
}
