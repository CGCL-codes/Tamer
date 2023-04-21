package edge.dataaccess.test.unit;

import edge.dataaccess.metamodel.EntityType;
import edge.dataaccess.metamodel.Property;
import edge.dataaccess.request.DataRequestFactory;
import edge.dataaccess.request.Selection;
import edge.dataaccess.sql.DataRequest2SQLTransformer;
import edge.dataaccess.sql.SelectionQuery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class SelectionQueryTest {

    public static EntityType getTestEntityType() {
        final String TEST_MAPPING_PATH = "edge/dataaccess/test/config/edge-dataaccess-SelectiontQueryTest-config.xml";
        return DataAccessUnitTestHelper.getTestEntityType(TEST_MAPPING_PATH);
    }

    public static Property getTestProperty() {
        final String TEST_MAPPING_PATH = "edge/dataaccess/test/config/edge-dataaccess-SelectiontQueryTest-config.xml";
        return DataAccessUnitTestHelper.getTestEntityType(TEST_MAPPING_PATH).getProperty("name");
    }

    @Test
    public void creation_valid() {
        final Selection EXPECTED_SELECTION = DataRequestFactory.selection("testSelection", getTestEntityType());
        SelectionQuery selectionQuery = DataRequest2SQLTransformer.selectionQuery(EXPECTED_SELECTION, null);
        assertEquals("Selection is not equal to provided one", EXPECTED_SELECTION, selectionQuery.getSelection());
    }

    @Test
    public void creation_nullSelection() {
        try {
            SelectionQuery selectionQuery = DataRequest2SQLTransformer.selectionQuery(null, null);
            fail("Selection must not be null - exception must have been thrown");
        } catch (Exception e) {
        }
    }

    @Test
    public void getQuery_noPropertiesAdded() {
        Selection selection = DataRequestFactory.selection("testSelection", getTestEntityType());
        SelectionQuery selectionQuery = DataRequest2SQLTransformer.selectionQuery(selection, null);
        try {
            selectionQuery.getQuery();
            fail("Properties to add must not be empty (empty select list and no conditions) - exception must have been thrown");
        } catch (Exception e) {
        }
    }

    @Test
    public void getQuery_selectProperty() {
        Selection selection = DataRequestFactory.selection("testSelection", getTestEntityType());
        List<Property> selectList = new ArrayList<Property>();
        selectList.add(getTestProperty());
        SelectionQuery selectionQuery = DataRequest2SQLTransformer.selectionQuery(selection, selectList);
        System.out.println(selectionQuery.getQuery().getQuery());
    }
}
