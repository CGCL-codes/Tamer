package net.sourceforge.sparql4j.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;
import junit.framework.TestCase;

/**
 * @author ssaarela
 */
public class SelectAskResultSetTest extends TestCase {

    public static final String ASK = "net/sourceforge/sparql4j/jdbc/ask.xml";

    public static final String SELECT = "net/sourceforge/sparql4j/jdbc/select.xml";

    public void testAsk() throws IOException, SQLException {
        InputStream inputStream = getClass().getClassLoader().getResource(ASK).openStream();
        ResultStream resultStream = new ResultStream(inputStream, "UTF-8", "application/sparql-results+xml");
        SelectAskResultSet rs = new SelectAskResultSet(resultStream);
        Map row = rs.nextRow();
        assertNotNull(row);
        assertEquals(row.get(SelectAskResultSet.ASK_COLUMN_NAME), "true");
    }

    public void testSelect() throws IOException, SQLException {
        InputStream inputStream = getClass().getClassLoader().getResource(SELECT).openStream();
        ResultStream resultStream = new ResultStream(inputStream, "UTF-8", "application/sparql-results+xml");
        SelectAskResultSet rs = new SelectAskResultSet(resultStream);
        Map row = rs.nextRow();
        assertNotNull(row);
        assertEquals("r2", row.get("x"));
        assertEquals(Integer.toString(SPARQL.BLANK_NODE), row.get("x$type"));
        assertNull(row.get("x$lang"));
        assertNull(row.get("x$datatype"));
        assertEquals("http://work.example.org/bob/", row.get("hpage"));
        assertEquals(Integer.toString(SPARQL.URI), row.get("hpage$type"));
        assertNull(row.get("hpage$lang"));
        assertNull(row.get("hpage$datatype"));
        assertEquals("Bob", row.get("name"));
        assertEquals(Integer.toString(SPARQL.PLAIN_LITERAL), row.get("name$type"));
        assertEquals("en", row.get("name$lang"));
        assertNull(row.get("name$datatype"));
        assertEquals("30", row.get("age"));
        assertEquals(Integer.toString(SPARQL.TYPED_LITERAL), row.get("age$type"));
        assertNull(row.get("age$lang"));
        assertEquals("http://www.w3.org/2001/XMLSchema#integer", row.get("age$datatype"));
        assertEquals("mailto:bob@work.example.org", row.get("mbox"));
        assertEquals(Integer.toString(SPARQL.URI), row.get("mbox$type"));
        assertNull(row.get("mbox$lang"));
        assertNull(row.get("mbox$datatype"));
        row = rs.nextRow();
        assertNotNull(row);
        assertEquals("http://work.example.org/bob/", row.get("x"));
        assertEquals(Integer.toString(SPARQL.URI), row.get("x$type"));
        assertNull(row.get("x$lang"));
        assertNull(row.get("x$datatype"));
        assertNull(row.get("hpage"));
        assertNull(row.get("hpage$type"));
        assertNull(row.get("hpage$lang"));
        assertNull(row.get("hpage$datatype"));
        assertEquals("Bob", row.get("name"));
        assertEquals(Integer.toString(SPARQL.PLAIN_LITERAL), row.get("name$type"));
        assertNull(row.get("name$lang"));
        assertNull(row.get("name$datatype"));
        assertNull(row.get("age"));
        assertNull(row.get("age$type"));
        assertNull(row.get("age$lang"));
        assertNull(row.get("age$datatype"));
        assertNull(row.get("friend"));
        assertNull(row.get("friend$type"));
        assertNull(row.get("friend$lang"));
        assertNull(row.get("friend$datatype"));
        assertNull(row.get("mbox"));
        assertNull(row.get("mbox$type"));
        assertNull(row.get("mbox$lang"));
        assertNull(row.get("mbox$datatype"));
        try {
            row = rs.nextRow();
            fail("Expected error.resultset.format");
        } catch (SQLException e) {
        }
    }

    public static void main(String args[]) throws Exception {
        junit.textui.TestRunner.run(SelectAskResultSetTest.class);
    }
}
