package org.databene.dbsanity.parser;

import java.sql.Connection;
import org.databene.commons.SyntaxError;
import org.databene.jdbacl.DBUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the ValuesParser.<br/><br/>
 * Created: 29.10.2011 07:11:33
 * @since 0.9.0
 * @author Volker Bergmann
 */
public class ValuesParserTest extends AbstractDefectQueryParserTest {

    protected static Connection connection;

    protected static DbSanityParseContext context;

    public ValuesParserTest() {
        super(new ValuesParser());
    }

    @BeforeClass
    public static void prepare() throws Exception {
        connection = connectTestDatabase();
        DBUtil.executeUpdate("create table ValuesParserTest (id int, s varchar(8))", connection);
        DBUtil.executeUpdate("insert into ValuesParserTest values ( 1, 'alpha')", connection);
        DBUtil.executeUpdate("insert into ValuesParserTest values ( 2, 'beta')", connection);
        DBUtil.executeUpdate("insert into ValuesParserTest values ( 3, 'gamma')", connection);
        context = createContext(connection);
    }

    @AfterClass
    public static void tearDownDatabase() throws Exception {
        DBUtil.executeUpdate("drop table ValuesParserTest", connection);
    }

    @Test(expected = SyntaxError.class)
    public void testMissingSpec() throws Exception {
        checkQueryAndResults("<values expression=\"s\"/>", null);
    }

    @Test
    public void testStringSuccess() throws Exception {
        checkQueryAndResults("<values expression=\"s\" list=\"'alpha', 'beta', 'gamma'\"/>", "select id, s from ValuesParserTest where s not in ('alpha', 'beta', 'gamma')");
    }

    @Test
    public void testIntSuccess() throws Exception {
        checkQueryAndResults("<values expression=\"id\" list=\"1, 2, 3\"/>", "select id, s from ValuesParserTest where id not in (1, 2, 3)");
    }

    @Test
    public void testSingleString() throws Exception {
        checkQueryAndResults("<values expression=\"s\" list=\"'beta'\"/>", "select id, s from ValuesParserTest where s not in ('beta')", new Object[] { 1, "alpha" }, new Object[] { 3, "gamma" });
    }

    @Test
    public void testStrings() throws Exception {
        checkQueryAndResults("<values expression=\"s\" list=\"'alpha','beta'\"/>", "select id, s from ValuesParserTest where s not in ('alpha', 'beta')", new Object[] { 3, "gamma" });
    }

    @Test
    public void testStringValues() throws Exception {
        checkQueryAndResults("<values expression=\"s\"><value>'alpha'</value><value>'beta'</value></values>", "select id, s from ValuesParserTest where s not in ('alpha', 'beta')", new Object[] { 3, "gamma" });
    }

    @Test
    public void testInts() throws Exception {
        checkQueryAndResults("<values expression=\"id\" list=\"2,3\"/>", "select id, s from ValuesParserTest where id not in (2, 3)", new Object[] { 1, "alpha" });
    }

    @Test
    public void testMatchingCondition() throws Exception {
        checkQueryAndResults("<values expression=\"id\" list=\"2,3\" condition=\"1=1\"/>", "select id, s from ValuesParserTest where id not in (2, 3) and 1=1", new Object[] { 1, "alpha" });
    }

    @Test
    public void testMissedCondition() throws Exception {
        checkQueryAndResults("<values expression=\"id\" list=\"2,3\" condition=\"1=2\"/>", "select id, s from ValuesParserTest where id not in (2, 3) and 1=2");
    }

    @Test
    public void testReportColumns() throws Exception {
        checkQueryAndResults("<values expression=\"s\" list=\"'alpha','gamma'\" reportColumns=\"s\"/>", "select s from ValuesParserTest where s not in ('alpha', 'gamma')", new Object[] { "beta" });
    }

    @Override
    protected Connection getConnection() {
        return connection;
    }

    @Override
    protected DbSanityParseContext getContext() {
        return context;
    }
}
