package org.databene.dbsanity.parser;

import static org.junit.Assert.*;
import java.io.File;
import java.sql.Connection;
import java.util.Locale;
import java.util.concurrent.Callable;
import org.databene.commons.ConfigurationError;
import org.databene.commons.LocaleUtil;
import org.databene.commons.SyntaxError;
import org.databene.commons.xml.XMLUtil;
import org.databene.dbsanity.model.CheckContext;
import org.databene.dbsanity.model.DefectIterator;
import org.databene.dbsanity.model.StrategyBasedCheck;
import org.databene.dbsanity.model.data.DataStrategy;
import org.databene.jdbacl.DBUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;

/**
 * Tests the {@link DataParser}.<br/><br/>
 * Created: 25.01.2012 15:35:56
 * @since 0.9.3
 * @author Volker Bergmann
 */
public class DataParserTest extends AbstractParserTest {

    protected static Connection connection;

    protected static DbSanityParseContext context;

    public DataParserTest() {
        super(new DataParser());
    }

    @BeforeClass
    public static void prepare() throws Exception {
        connection = connectTestDatabase();
        DBUtil.executeUpdate("create table DataParserTest (id int, name varchar(8), age int, primary key (id))", connection);
        DBUtil.executeUpdate("insert into DataParserTest values ( 1, 'Alice', 23)", connection);
        DBUtil.executeUpdate("insert into DataParserTest values ( 2, 'Bob', 34)", connection);
        DBUtil.executeUpdate("insert into DataParserTest values ( 3, 'Charly', 45)", connection);
        context = createContext(connection);
    }

    @AfterClass
    public static void tearDownDatabase() throws Exception {
        DBUtil.executeUpdate("drop table DataParserTest", connection);
    }

    @Test(expected = SyntaxError.class)
    public void testMissingSpec() throws Exception {
        check("<data>" + "	<testee/>" + "	<reference/>" + "</data>", null);
    }

    @Test(expected = SyntaxError.class)
    public void testMissingTestee() throws Exception {
        check("<data referenceIs='subset'>" + "	<reference/>" + "</data>", null);
    }

    @Test(expected = SyntaxError.class)
    public void testMissingReference() throws Exception {
        check("<data referenceIs='subset'>" + "	<testee/>" + "</data>", null);
    }

    @Test(expected = SyntaxError.class)
    public void testIrregularTestee() throws Exception {
        check("<data referenceIs='subset'>" + "	<testee/>" + "	<reference file='DataParserTest-ABC.csv'/>" + "</data>", null);
    }

    @Test(expected = SyntaxError.class)
    public void testIrregularReference() throws Exception {
        check("<data referenceIs='subset'>" + "	<testee columns='name'/>" + "	<reference/>" + "</data>", null);
    }

    @Test
    public void testCsvSetSuccess() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='name'/>" + "	<reference file='DataParserTest-ABC.csv'/>" + "</data>", "SELECT ID, NAME FROM DataParserTest");
    }

    @Test
    public void testCsvSetSuccessWithOffset() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='name'/>" + "	<reference file='DataParserTest-ABC-ofs.csv' headerOffset='2'/>" + "</data>", "SELECT ID, NAME FROM DataParserTest");
    }

    @Test
    public void testCsvSetFailure() throws Exception {
        LocaleUtil.callInLocale(Locale.US, new Callable<Void>() {

            public Void call() throws Exception {
                check("<data referenceIs='set'>" + "	<testee columns='name'/>" + "	<reference file='DataParserTest-AB.csv'/>" + "</data>", "SELECT ID, NAME FROM DataParserTest", new Object[] { 3, "Charly", "Not an allowed entry" });
                return null;
            }
        });
    }

    @Test
    public void testCsvSupersetIsSetSuccess() throws Exception {
        check("<data referenceIs='superset'>" + "	<testee columns='name'/>" + "	<reference file='DataParserTest-ABCD.csv'/>" + "</data>", "SELECT ID, NAME FROM DataParserTest");
    }

    @Test
    public void testCsvSupersetSuccess() throws Exception {
        check("<data referenceIs='superset'>" + "	<testee columns='name'/>" + "	<reference file='DataParserTest-ABCD.csv'/>" + "</data>", "SELECT ID, NAME FROM DataParserTest");
    }

    @Test
    public void testCsvSupersetFailure() throws Exception {
        LocaleUtil.callInLocale(Locale.US, new Callable<Void>() {

            public Void call() throws Exception {
                check("<data referenceIs='superset'>" + "	<testee columns='name'/>" + "	<reference file='DataParserTest-AB.csv'/>" + "</data>", "SELECT ID, NAME FROM DataParserTest", new Object[] { 3, "Charly", "Not an allowed entry" });
                return null;
            }
        });
    }

    @Test
    public void testCsvSubsetIsSetSuccess() throws Exception {
        check("<data referenceIs='subset'>" + "	<testee columns='name'/>" + "	<reference file='DataParserTest-ABC.csv'/>" + "</data>", "SELECT ID, NAME FROM DataParserTest");
    }

    @Test
    public void testCsvSubsetSuccess() throws Exception {
        check("<data referenceIs='subset'>" + "	<testee columns='name'/>" + "	<reference file='DataParserTest-AB.csv'/>" + "</data>", "SELECT ID, NAME FROM DataParserTest");
    }

    @Test
    public void testCsvSubsetFailure() throws Exception {
        LocaleUtil.callInLocale(Locale.US, new Callable<Void>() {

            public Void call() throws Exception {
                check("<data referenceIs='subset'>" + "	<testee columns='name'/>" + "	<reference file='DataParserTest-ABCD.csv'/>" + "</data>", "SELECT ID, NAME FROM DataParserTest", new Object[] { null, "Doris", "Required entry is missing" });
                return null;
            }
        });
    }

    @Test
    public void testXlsSetSuccess() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='name'/>" + "	<reference file='DataParserTest-ABC.xls'/>" + "</data>", "SELECT ID, NAME FROM DataParserTest");
    }

    @Test
    public void testCondition() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='name' condition='id &lt; 3'/>" + "	<reference file='DataParserTest-AB.csv'/>" + "</data>", "SELECT ID, NAME FROM DataParserTest where id < 3");
    }

    @Test
    public void testReportColumns() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='name' reportColumns='id, name, age'/>" + "	<reference file='DataParserTest-ABC.csv'/>" + "</data>", "SELECT ID, NAME, AGE FROM DataParserTest");
    }

    @Test
    public void testCompleteMultiColumnSet() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='id, name, age' />" + "	<reference file='DataParserTest-mc-ABC.csv'/>" + "</data>", "SELECT ID, NAME, AGE FROM DataParserTest");
    }

    @Test
    public void testMultiColumnSetWithGap() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='id, age' />" + "	<reference file='DataParserTest-mc-ABC.csv' columns='id, age' />" + "</data>", "SELECT ID, AGE FROM DataParserTest");
    }

    @Test
    public void testLeftPartOfMultiColumnSet() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='id, name' />" + "	<reference file='DataParserTest-mc-ABC.csv' columns='id, name' />" + "</data>", "SELECT ID, NAME FROM DataParserTest");
    }

    @Test
    public void testCenterPartOfMultiColumnSet() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='name, age' />" + "	<reference file='DataParserTest-mc-ABC.csv' columns='name, age' />" + "</data>", "SELECT ID, NAME, AGE FROM DataParserTest");
    }

    @Test
    public void testRightPartOfMultiColumnSet() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='name, age' />" + "	<reference file='DataParserTest-mc-ABC.csv' columns='name, age' />" + "</data>", "SELECT ID, NAME, AGE FROM DataParserTest");
    }

    @Test
    public void testSetWithFileColumnSelection() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='name' />" + "	<reference file='DataParserTest-mc-ABC.csv' columns='NAME'/>" + "</data>", "SELECT ID, NAME FROM DataParserTest");
    }

    @Test(expected = ConfigurationError.class)
    public void testSetWithIdentityColumns_bad_setup() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='name,age' identityColumns='age' />" + "	<reference file='DataParserTest-mc-ABC.csv' columns='NAME'/>" + "</data>", null);
    }

    @Test
    public void testSetWithIdentityColumns_consistent() throws Exception {
        check("<data referenceIs='set'>" + "	<testee columns='name,age' identityColumns='age' />" + "	<reference file='DataParserTest-mc-ABC.csv' columns='NAME,AGE'/>" + "</data>", "SELECT ID, NAME, AGE FROM DataParserTest");
    }

    @Test
    public void testSetWithIdentityColumns_inconsistent() throws Exception {
        LocaleUtil.callInLocale(Locale.US, new Callable<Void>() {

            public Void call() throws Exception {
                check("<data referenceIs='set'>" + "	<testee columns='id,name,age' identityColumns='age' />" + "	<reference file='DataParserTest-mc-inco-ABC.csv' columns='ID,NAME,AGE'/>" + "</data>", "SELECT ID, NAME, AGE FROM DataParserTest", new Object[] { 2, "Bob", 34, "Not an allowed entry" }, new Object[] { "3 [4]", "Charly", 45, "Misconfigured entry" }, new Object[] { "2", "Bob", "33", "Required entry is missing" });
                return null;
            }
        });
    }

    @Override
    protected Connection getConnection() {
        return connection;
    }

    @Override
    protected DbSanityParseContext getContext() {
        return context;
    }

    protected void check(String checkDef, String expectedQuery, Object[]... expectedRows) throws Exception {
        Element element = XMLUtil.parseStringAsElement(checkDef);
        StrategyBasedCheck check = createStrategyBasedCheck("DataParserTest", new File("src/test/resources/org/databene/dbsanity/parser/DataParserTest.dbs.xml"));
        Object[] parentPath = new Object[] { check };
        DataStrategy strategy = (DataStrategy) parser.parse(element, parentPath, getContext());
        Connection connection = getConnection();
        DefectIterator iterator = check.perform(new CheckContext(connection, importDatabaseMetaData(connection), 1));
        for (Object[] expectedRow : expectedRows) {
            assertTrue(iterator.hasNext());
            assertEqualArrays(expectedRow, iterator.next().getCells());
        }
        assertFalse("More defects than expected", iterator.hasNext());
        assertEquals(expectedRows.length, strategy.getDefectCount());
        if (expectedQuery != null) assertEqualsIgnoreCase(expectedQuery, strategy.getQuery());
    }
}
