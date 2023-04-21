package model.sources;

import model.sources.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.ListIterator;
import model.retrieval.Field;
import model.retrieval.ParentSourceTest;
import model.retrieval.SourceConnectionException;
import model.retrieval.Query.queryType;
import model.storage.Artifact;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * JUnit4 test class for CiteSeerSource. Several test cases are inhered
 * from ParentSourceTest and OnlineSourceTest since CiteSeerSource inheres them.
 * 
 * Note: Internet connection is required to correctly run these test cases.
 * 		 Data are fetched from the Internet directly for testing, the running
 * 		 speed of these test cases greatly depend on the speed of the connection.
 * 		 Some test cases may take up to 3 minutes to complete.
 * 
 * @author Tao Zhou
 *
 */
public class CiteSeerSourceTest extends ParentSourceTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        simpleSource = new CiteSeerSource();
        connectingSource = new CiteSeerSource();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testBasicSearch() {
        testQuery.setQueryType(queryType.Basic);
        connectingSource.setQuery(testQuery);
        connectingSource.executeQuery();
        ArrayList<Artifact> results = connectingSource.getSearchResults();
        for (Artifact result : results) {
            result.setTitle(result.getTitle().toLowerCase());
            assertTrue("CiteSeerSource: basicSearch failed.", result.getTitle().indexOf("mathematics") != -1);
        }
        assertTrue("CiteSeerSource: no result was returned.", results.size() != 0);
    }

    @Test
    public void testUpdateSearch() {
        connectingSource.setQuery(testQuery);
        testQuery.setQueryType(queryType.Basic);
        Artifact originalArtifact = new Artifact();
        originalArtifact.setTitle("mathematics");
        testQuery.setArtifact(originalArtifact);
        try {
            connectingSource.connect();
        } catch (SourceConnectionException e1) {
            fail();
        }
        connectingSource.executeQuery();
        ArrayList<Artifact> results = connectingSource.getSearchResults();
        for (int i = 0; i < results.size(); i++) {
            assertTrue("search result " + i + " returned was incorrect.", results.get(i).getTitle().indexOf("mathematics") != -1 || results.get(i).getTitle().indexOf("Mathematics") != -1);
        }
        assertTrue("No search result was returned.", results.size() != 0);
    }

    @Test
    public void testCiteSeerSource() {
        CiteSeerSource citeSeer = new CiteSeerSource();
        assertTrue("CiteSeerSource sourceName incorrect", citeSeer.getSourceName().equalsIgnoreCase("Cite Seer"));
        assertTrue("CiteSeerSource failed to setup libraryProxy", citeSeer.getLibraryProxy() != null);
        assertTrue("CiteSeerSource plugin type incorrect", citeSeer.getType().toString().equalsIgnoreCase("OnlineSource"));
    }

    @Test
    public void testGenerateFields() {
        ListIterator<Field> fields = connectingSource.generateFields().listIterator();
        while (fields.hasNext()) {
            Field field = fields.next();
            assertTrue("CiteSeerSource: Field label incorrect.", field.getLabel() != null);
            assertTrue("CiteSeerSource: Field Key was not set.", field.getKey().length() != 0);
            assertTrue("CiteSeerSource: Field Type was not set.", field.getType() != null);
            assertTrue("CiteSeerSource: Field Type was set incorrectly.", (field.getType() == Field.fieldType.TEXT) || (field.getType() == Field.fieldType.CHECKBOXES) || (field.getType() == Field.fieldType.DROPDOWN));
        }
    }
}
