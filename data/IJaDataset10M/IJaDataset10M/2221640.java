package uk.ac.ebi.intact.application.search3.searchEngine.test.large;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.collections.IterableMap;
import org.apache.commons.collections.MapIterator;
import uk.ac.ebi.intact.application.search3.searchEngine.business.SearchEngineImpl;
import uk.ac.ebi.intact.application.search3.searchEngine.business.dao.SearchDAO;
import uk.ac.ebi.intact.application.search3.searchEngine.business.dao.SearchDAOImpl;
import uk.ac.ebi.intact.application.search3.searchEngine.lucene.IntactAnalyzer;
import uk.ac.ebi.intact.business.IntactException;
import uk.ac.ebi.intact.model.AnnotatedObject;
import uk.ac.ebi.intact.model.CvObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * TODO comment that ...
 *
 * @author Anja Friedrichsen
 * @version $Id: SearchCvObjectTest.java 5014 2006-06-09 13:43:19Z baranda $
 */
public class SearchCvObjectTest extends TestCase {

    /**
     * Constructs a NewtServerProxyTest instance with the specified name.
     *
     * @param name the name of the test.
     */
    public SearchCvObjectTest(final String name) {
        super(name);
    }

    /**
     * Returns this test suite. Reflection is used here to add all
     * the testXXX() methods to the suite.
     */
    public static Test suite() {
        return new TestSuite(SearchCvObjectTest.class);
    }

    SearchEngineImpl engine;

    IterableMap result;

    /**
     * Sets up the test fixture. Called before every test case method.
     */
    protected void setUp() throws IntactException {
        SearchDAO dao = new SearchDAOImpl();
        engine = new SearchEngineImpl(new IntactAnalyzer(), new File("indexLarge"), dao, null);
    }

    /**
     * check if the cv "caution", which is in the index, is found
     */
    public void testCvShortlabel() {
        try {
            result = (IterableMap) engine.getResult(engine.findObjectByLucene("shortlabel:caution AND objclass:uk.*Cv*"));
        } catch (IntactException e) {
            assertTrue(false);
            e.printStackTrace();
        }
        assertNotNull(result);
        MapIterator it = result.mapIterator();
        Object key = null;
        Collection value = null;
        while (it.hasNext()) {
            key = it.next();
            value = (ArrayList) it.getValue();
            if (key.equals("cvobject")) {
                assertNotNull(key);
                assertNotNull(value);
                assertTrue(!value.isEmpty());
                assertEquals("cvobject", key);
                AnnotatedObject anObject = (AnnotatedObject) value.iterator().next();
                assertEquals("caution", anObject.getShortLabel());
            }
        }
    }

    /**
     * check if all Cvs shortlabel starting with 'com' are found
     */
    public void testMultiCvShortlabel() {
        try {
            result = (IterableMap) engine.getResult(engine.findObjectByLucene("shortlabel:com* AND objclass:uk.*Cv*"));
            assertNotNull(result);
            MapIterator it = result.mapIterator();
            Object key = null;
            Collection value = null;
            while (it.hasNext()) {
                key = it.next();
                value = (ArrayList) it.getValue();
                if (key.equals("cvobject")) {
                    assertNotNull(key);
                    assertNotNull(value);
                    assertTrue(!value.isEmpty());
                    assertEquals("cvobject", key);
                    int size = 6;
                    assertEquals(size, value.size());
                    for (Iterator iterator = value.iterator(); iterator.hasNext(); ) {
                        CvObject prot = (CvObject) iterator.next();
                        assertNotNull(prot);
                        assertTrue(prot.getShortLabel().matches(".*com.*"));
                    }
                }
            }
        } catch (IntactException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }

    /**
     * tests if a specific AC is found
     */
    public void testCvAc() {
        try {
            result = (IterableMap) engine.getResult(engine.findObjectByLucene("ac:EBI-316 AND objclass:uk.*Cv*"));
            assertNotNull(result);
            MapIterator it = result.mapIterator();
            Object key = null;
            Collection value = null;
            while (it.hasNext()) {
                key = it.next();
                value = (ArrayList) it.getValue();
                if (key.equals("cvobject")) {
                    assertNotNull(key);
                    assertNotNull(value);
                    assertTrue(!value.isEmpty());
                    assertEquals("cvobject", key);
                    AnnotatedObject anObject = (AnnotatedObject) value.iterator().next();
                    assertEquals("EBI-316", anObject.getAc());
                }
            }
        } catch (IntactException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }

    /**
     * tests if a AC starting with 'EBI-11' are found
     */
    public void testMultiCvAc() {
        try {
            result = (IterableMap) engine.getResult(engine.findObjectByLucene("ac:EBI-111* AND objclass:uk.*Cv*"));
            assertNotNull(result);
            MapIterator it = result.mapIterator();
            Object key = null;
            Collection value = null;
            while (it.hasNext()) {
                key = it.next();
                value = (ArrayList) it.getValue();
                if (key.equals("cvobject")) {
                    assertNotNull(key);
                    assertNotNull(value);
                    assertTrue(!value.isEmpty());
                    assertEquals("cvobject", key);
                    int size = 2;
                    assertEquals(size, value.size());
                    for (Iterator iterator = value.iterator(); iterator.hasNext(); ) {
                        CvObject prot = (CvObject) iterator.next();
                        assertNotNull(prot);
                        assertTrue(prot.getAc().startsWith("EBI-111"));
                    }
                }
            }
        } catch (IntactException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }

    /**
     * tests if a specific fullname is found
     */
    public void testCvFullname() {
        try {
            result = (IterableMap) engine.getResult(engine.findObjectByLucene("fullname:example AND objclass:uk.*Cv*"));
            assertNotNull(result);
            MapIterator it = result.mapIterator();
            Object key = null;
            Collection value = null;
            while (it.hasNext()) {
                key = it.next();
                value = (ArrayList) it.getValue();
                if (key.equals("cvobjects")) {
                    assertNotNull(key);
                    assertNotNull(value);
                    assertTrue(!value.isEmpty());
                    assertEquals("cvobject", key);
                    AnnotatedObject anObject = (AnnotatedObject) value.iterator().next();
                    assertEquals("Example", anObject.getFullName());
                }
            }
        } catch (IntactException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }

    /**
     * tests if all fullname containing 'comple*' are found
     */
    public void testMultiCvFullname() {
        try {
            result = (IterableMap) engine.getResult(engine.findObjectByLucene("fullname:comple* AND objclass:uk.*Cv*"));
            assertNotNull(result);
            MapIterator it = result.mapIterator();
            Object key = null;
            Collection value = null;
            while (it.hasNext()) {
                key = it.next();
                value = (ArrayList) it.getValue();
                if (key.equals("cvobject")) {
                    assertNotNull(key);
                    assertNotNull(value);
                    assertTrue(!value.isEmpty());
                    assertEquals("cvobject", key);
                    int size = 8;
                    assertEquals(size, value.size());
                    for (Iterator iterator = value.iterator(); iterator.hasNext(); ) {
                        CvObject prot = (CvObject) iterator.next();
                        assertNotNull(prot);
                        assertTrue(prot.getFullName().matches(".*comple.*"));
                    }
                }
            }
        } catch (IntactException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }
}
