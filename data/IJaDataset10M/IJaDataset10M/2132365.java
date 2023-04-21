package fr.cnes.sitools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import fr.cnes.sitools.common.SitoolsSettings;
import fr.cnes.sitools.common.model.ResourceCollectionFilter;
import fr.cnes.sitools.dictionary.DictionaryStoreXML;
import fr.cnes.sitools.dictionary.model.Dictionary;
import fr.cnes.sitools.server.Consts;

/**
 * Test DictionaryStoreXML
 * 
 * @since UserStory : ADM Dictionaries - Sprint : 4
 * @author jp.boignard (AKKA Technologies)
 */
public class DictionaryStoreXMLTestCase extends AbstractSitoolsTestCase {

    /**
   * static xml store instance for the test
   */
    private static DictionaryStoreXML store = null;

    @Override
    protected String getBaseUrl() {
        return super.getBaseUrl() + SitoolsSettings.getInstance().getString(Consts.APP_DICTIONARIES_URL);
    }

    @Override
    protected String getTestRepository() {
        return super.getTestRepository() + SitoolsSettings.getInstance().getString(Consts.APP_DICTIONARIES_STORE_DIR);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        if (store == null) {
            File storeDirectory = new File(getTestRepository());
            cleanDirectory(storeDirectory);
            store = new DictionaryStoreXML(storeDirectory);
        }
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
   * Test CRUD scenario.
   */
    @Test
    public void testCRUD() {
        assertNone();
        create();
        retrieve();
        update();
        delete();
        assertNone();
    }

    /**
   * Invokes store.create and asserts result is conform.
   */
    public void create() {
        Dictionary element = new Dictionary();
        element.setId("testCreateDictionary");
        element.setName("test_Name");
        element.setDescription("test_Description");
        Dictionary result = store.create(element);
        assertNotNull(result);
        assertEquals("test_Name", result.getName());
        assertEquals("test_Description", result.getDescription());
    }

    /**
   * Invokes store.getArray and check result is an empty array
   */
    public void assertNone() {
        Dictionary[] list = store.getArray();
        assertNotNull(list);
        assertEquals(0, list.length);
    }

    /**
   * Invokes store.retrieve and asserts result is conform.
   */
    public void retrieve() {
        Dictionary result = store.retrieve("testCreateDictionary");
        assertNotNull(result);
        assertEquals("testCreateDictionary", result.getId());
    }

    /**
   * Invokes store.update and asserts result is conform.
   */
    public void update() {
        Dictionary element = new Dictionary();
        element.setId("testCreateDictionary");
        element.setName("test_Name_modified");
        element.setDescription("test_Description_modified");
        Dictionary result = store.update(element);
        assertNotNull(result);
        assertEquals("test_Name_modified", result.getName());
        assertEquals("test_Description_modified", result.getDescription());
    }

    /**
   * Test des requetes avec XQuery / XPath >> quand nous aurons une BD XML Test de creation/consultation/suppression
   * multiples
   */
    @Test
    public void testGetDictionaryByXQuery() {
        fillDatabase(10);
        Dictionary[] completeList = store.getArray();
        assertNotNull(completeList);
        ResourceCollectionFilter filter = new ResourceCollectionFilter(2, 5, "test");
        List<Dictionary> newList = store.getList(filter);
        assertNotNull(newList);
        assertEquals(10, newList.size());
        List<Dictionary> pageList = store.getPage(filter, newList);
        assertEquals(5, pageList.size());
        filter = new ResourceCollectionFilter(-1, 5, "test");
        newList = store.getList(filter);
        assertNotNull(newList);
        assertEquals(10, newList.size());
        pageList = store.getPage(filter, newList);
        assertEquals(5, pageList.size());
        filter = new ResourceCollectionFilter(0, 5, "TEST");
        newList = store.getList(filter);
        assertNotNull(newList);
        assertEquals(10, newList.size());
        pageList = store.getPage(filter, newList);
        assertEquals(5, pageList.size());
        filter = new ResourceCollectionFilter(0, 1, "test");
        newList = store.getList(filter);
        assertNotNull(newList);
        assertEquals(10, newList.size());
        pageList = store.getPage(filter, newList);
        assertEquals(1, pageList.size());
        filter = new ResourceCollectionFilter(1, 1, "test");
        newList = store.getList(filter);
        assertNotNull(newList);
        assertEquals(10, newList.size());
        pageList = store.getPage(filter, newList);
        assertEquals(1, pageList.size());
        filter = new ResourceCollectionFilter(8, 20, "test");
        newList = store.getList(filter);
        assertNotNull(newList);
        assertEquals(10, newList.size());
        pageList = store.getPage(filter, newList);
        assertEquals(2, pageList.size());
        filter = new ResourceCollectionFilter(9, 20, "test");
        newList = store.getList(filter);
        assertNotNull(newList);
        assertEquals(10, newList.size());
        pageList = store.getPage(filter, newList);
        assertEquals(1, pageList.size());
        filter = new ResourceCollectionFilter(10, 20, "test");
        newList = store.getList(filter);
        assertNotNull(newList);
        assertEquals(10, newList.size());
        pageList = store.getPage(filter, newList);
        assertEquals(0, pageList.size());
        clearDatabase(10);
    }

    /**
   * Invokes store.delete and asserts result is conform.
   */
    public void delete() {
        boolean result = store.delete("testCreateDictionary");
        assertTrue(result);
        Dictionary testDeletedDictionary = store.retrieve("testCreateDictionary");
        assertNull(testDeletedDictionary);
    }

    /**
   * Invokes store.create for numerous elements ( "test"+no for identifier)
   * 
   * @param nbElement
   *          the number of elements to be created
   */
    private void fillDatabase(int nbElement) {
        Dictionary element = null;
        for (int i = 0; i < nbElement; i++) {
            element = new Dictionary();
            element.setId("test" + i);
            element.setName("test_Name" + i);
            element.setDescription("test_Description" + i);
            Dictionary result = store.create(element);
            assertNotNull(result);
        }
    }

    /**
   * Invokes store.delete on the "test"+no elements with 0 <= no < nbElement
   * 
   * @param nbElement
   *          number of element to clear suppose elements are identifier by "test"+no
   */
    private void clearDatabase(int nbElement) {
        for (int i = 0; i < nbElement; i++) {
            store.delete("test" + i);
        }
    }
}
