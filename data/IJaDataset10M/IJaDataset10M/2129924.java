package com.simpledata.bc.reports.tarification;

import junit.framework.TestCase;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import com.simpledata.bc.reports.scriptlets.ValueGetterMockup;

/**
 * Unit Test for Table class and attached helpers 
 * TableFields, TableRow. 
 */
public class TestTableOfContentsScriptlet extends TestCase {

    protected TableOfContentsScriptlet m_scriptlet;

    protected ValueMockup m_mockup;

    public TestTableOfContentsScriptlet(String name) {
        super(name);
    }

    protected void setUp() {
        m_scriptlet = new TableOfContentsScriptlet();
        m_mockup = new ValueMockup();
        m_scriptlet.setUnitTestMockup(m_mockup);
    }

    public void testBasicDatasource() {
        JRRewindableDataSource ds = m_scriptlet.getDataSource();
        try {
            assertFalse(ds.next());
        } catch (Exception e) {
            assertTrue("Exception caught", false);
        }
    }

    public void testDatasourceFill() {
        boolean testOk = true;
        try {
            m_scriptlet.beforeGroupInit("Title");
            m_scriptlet.beforeGroupInit("SubTitle");
            m_scriptlet.beforeGroupInit("SubSubTitle");
        } catch (Exception e) {
            testOk = false;
            e.printStackTrace();
        }
        assertTrue(testOk);
    }

    /**
	 * Run the tests from the command line.  
	 */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestTableOfContentsScriptlet.class);
    }

    /**
	 * Internal class that permits simulation of variable
	 * and field content. 
	 */
    class ValueMockup implements ValueGetterMockup {

        public Object getVariableValue(String variableName) {
            return new Integer(30);
        }

        public Object getFieldValue(String fieldName) {
            return fieldName + " mockup";
        }

        public void setVariableValue(String variableName, Object value) {
        }
    }
}
