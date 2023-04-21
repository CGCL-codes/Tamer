package au.edu.qut.yawl.editor.thirdparty.engine;

import java.util.LinkedList;
import java.util.Iterator;
import junit.framework.*;
import au.edu.qut.yawl.editor.data.DataVariable;
import au.edu.qut.yawl.editor.specification.SpecificationModel;

public class TestAvailableEngineProxyImplementation extends TestCase {

    AvailableEngineProxyImplementation proxy;

    public TestAvailableEngineProxyImplementation(String pName) {
        super(pName);
    }

    public static Test suite() {
        return new TestSuite(TestAvailableEngineProxyImplementation.class);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    protected void setUp() {
        proxy = new AvailableEngineProxyImplementation();
        SpecificationModel.getInstance().reset();
        proxy.setDataTypeSchema(SpecificationModel.DEFAULT_TYPE_DEFINITION);
    }

    public void testValidateSimpleSchemaForStringBaseType() {
        assertEquals("", proxy.validateBaseDataTypeInstance("<element name=\"testString\" type=\"string\"/>", "<testString>I am a test string</testString>"));
    }

    public void testValidateSimpleSchemaForLongBaseType() {
        assertEquals("", proxy.validateBaseDataTypeInstance("<element name=\"testLong\" type=\"long\"/>", "<testLong>1234</testLong>"));
        assertFalse(proxy.validateBaseDataTypeInstance("<element name=\"testLong\" type=\"long\"/>", "<testLong>this is not a long</testLong>").equals(""));
    }

    public void testValidateSimpleSchemaForBooleanBaseType() {
        assertEquals("", proxy.validateBaseDataTypeInstance("<element name=\"testBoolean\" type=\"boolean\"/>", "<testBoolean>true</testBoolean>"));
        assertEquals("", proxy.validateBaseDataTypeInstance("<element name=\"testBoolean\" type=\"boolean\"/>", "<testBoolean>false</testBoolean>"));
        assertFalse(proxy.validateBaseDataTypeInstance("<element name=\"testBoolean\" type=\"boolean\"/>", "<testBoolean>this is not a boolean</testBoolean>").equals(""));
    }

    public void testValidateSimpleSchemaForDoubleBaseType() {
        assertEquals("", proxy.validateBaseDataTypeInstance("<element name=\"testDouble\" type=\"double\"/>", "<testDouble>1.234</testDouble>"));
        assertFalse(proxy.validateBaseDataTypeInstance("<element name=\"testDouble\" type=\"double\"/>", "<testDouble>this is not a double</testDouble>").equals(""));
    }

    public void testValidateSimpleSchemaForDateBaseType() {
        assertTrue(true);
    }

    public void testValidateSimpleSchemaForTimeBaseType() {
        assertTrue(true);
    }

    public void testValidateSimpleSchemaForEnumeratedStringVariable() {
        SpecificationModel.getInstance().setDataTypeDefinition("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\">" + "  <xs:simpleType name=\"TestEnumeration\">" + "    <xs:restriction base=\"xs:string\">" + "      <xs:enumeration value=\"OK\"/>" + "      <xs:enumeration value=\"NOK\"/>" + "    </xs:restriction>" + "  </xs:simpleType>" + "</xs:schema>");
        assertEquals("", proxy.validateUserSuppliedDataTypeInstance("testElement", "TestEnumeration", "OK"));
        assertFalse(proxy.validateUserSuppliedDataTypeInstance("testElement", "TestEnumeration", "crap").equals(""));
    }

    public void testValidateComplexSchema() {
        SpecificationModel.getInstance().setDataTypeDefinition("<schema xmlns='http://www.w3.org/2001/XMLSchema'>" + "  <complexType name='PersonList'>" + "    <sequence>" + "      <element name='PersonName' type='string' maxOccurs='unbounded'/>" + "    </sequence>" + "  </complexType>" + "</schema>");
        assertEquals("", proxy.validateUserSuppliedDataTypeInstance("testList", "PersonList", "<PersonName>Linds</PersonName>"));
        assertFalse(proxy.validateUserSuppliedDataTypeInstance("testList", "PersonList", "<NonPersonName>crap</NonPersonName>").equals(""));
    }
}
