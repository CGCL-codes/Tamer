package net.sourceforge.squirrel_sql.fw.datasetviewer.cellcomponent;

import net.sourceforge.squirrel_sql.fw.datasetviewer.ColumnDisplayDefinition;
import org.junit.Before;

/**
 * JUnit test for DataTypeFloat class.
 * 
 * @author manningr
 */
public class DataTypeFloatTest extends AbstractDataTypeComponentTest {

    @Before
    public void setUp() throws Exception {
        ColumnDisplayDefinition mockColumnDisplayDefinition = getMockColumnDisplayDefinition();
        mockHelper.replayAll();
        classUnderTest = new DataTypeFloat(null, mockColumnDisplayDefinition);
        mockHelper.resetAll();
        super.setUp();
    }

    @Override
    protected Object getEqualsTestObject() {
        return Float.valueOf(1);
    }
}
