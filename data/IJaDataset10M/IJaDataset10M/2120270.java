package net.sourceforge.squirrel_sql.plugins.oracle.types;

import net.sourceforge.squirrel_sql.fw.datasetviewer.cellcomponent.AbstractDataTypeComponentTest;

public class OracleXmlTypeDataTypeComponentTest extends AbstractDataTypeComponentTest {

    /**
	 * @see net.sourceforge.squirrel_sql.fw.datasetviewer.cellcomponent.AbstractDataTypeComponentTest#setUp()
	 */
    @Override
    public void setUp() throws Exception {
        classUnderTest = new OracleXmlTypeDataTypeComponent();
        super.setUp();
        super.defaultValueIsNull = true;
    }

    @Override
    protected Object getEqualsTestObject() {
        return "testString";
    }
}
