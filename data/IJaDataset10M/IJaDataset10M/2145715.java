package uk.ac.ebi.rhea.webapp.pub.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.util.List;
import java.util.Map.Entry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.rhea.util.index.Field;

/**
 *
 * @author rafa
 */
public class SearchResultTest {

    private SearchResult instance;

    public SearchResultTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new SearchResult();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addMatchingField method, of class SearchResult.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddMatchingField() {
        System.out.println("addMatchingField");
        assertNull(instance.getMatchingFields());
        instance.addMatchingField(Field.PARTICIPANT, "CHEBI:12345");
        assertNotNull(instance.getMatchingFields());
        assertEquals(1, instance.getMatchingFields().length);
        assertEquals(1, ((Entry<String, List<String>>) instance.getMatchingFields()[0]).getValue().size());
        instance.addMatchingField(Field.PARTICIPANT, "CHEBI:54321");
        assertEquals(1, instance.getMatchingFields().length);
        instance.addMatchingField(Field.XREF, "R98765");
        assertEquals(2, instance.getMatchingFields().length);
    }
}
