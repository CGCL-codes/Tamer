package org.hip.kernel.bom.impl.test;

import static org.junit.Assert.*;
import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.DomainObjectIterator;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.impl.EmptyQueryResult;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * Created on 10.08.2003
 * @author Luthiger
 */
public class EmptyQueryResultTest {

    private DomainObjectHome lHome;

    private static DataHouseKeeper data;

    @BeforeClass
    public static void init() {
        data = DataHouseKeeper.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        lHome = data.getSimpleHome();
    }

    @After
    public void tearDown() throws Exception {
        data.deleteAllFromSimple();
    }

    @Test
    public void testClose() {
        try {
            QueryResult lResult = new EmptyQueryResult(lHome);
            lResult.close();
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testGetCurrent() {
        try {
            QueryResult lResult = new EmptyQueryResult(lHome);
            assertNull(lResult.getCurrent());
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testGetCurrentPage() {
        try {
            QueryResult lResult = new EmptyQueryResult(lHome);
            assertNull(lResult.getCurrentPage());
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testGetKey() {
        try {
            QueryResult lResult = new EmptyQueryResult(lHome);
            assertNotNull(lResult.getKey());
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testHasMoreElements() {
        try {
            QueryResult lResult = new EmptyQueryResult(lHome);
            assertFalse(lResult.hasMoreElements());
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testNext() {
        try {
            QueryResult lResult = new EmptyQueryResult(lHome);
            assertNull(lResult.next());
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testNextAsXMLString() {
        try {
            QueryResult lResult = new EmptyQueryResult(lHome);
            assertNull(lResult.nextAsXMLString());
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testNextn() {
        try {
            QueryResult lResult = new EmptyQueryResult(lHome);
            assertNotNull(lResult.nextn(10));
            for (DomainObjectIterator lIterator = lResult.nextn(10).elements(); lIterator.hasMoreElements(); ) {
                lIterator.nextElement();
                fail("Should'nt get here.");
            }
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    @Test
    public void testNextnAsXMLString() {
        try {
            String lNL = System.getProperty("line.separator");
            String lExpected = lNL + "<ObjectList>" + lNL + "</ObjectList>";
            QueryResult lResult = new EmptyQueryResult(lHome);
            assertEquals(lExpected, lResult.nextnAsXMLString(10));
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }
}
