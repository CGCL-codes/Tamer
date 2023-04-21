package test.net.rptools.chartool.model.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.math.BigDecimal;
import net.rptools.chartool.model.property.CompiledScript;
import net.rptools.chartool.model.property.DefaultScriptPropertyValue;
import net.rptools.chartool.model.property.PropertyDescriptor;
import net.rptools.chartool.model.property.PropertyDescriptorType;
import net.rptools.chartool.model.property.RPScript;
import net.rptools.chartool.model.property.ScriptPropertyValue;
import org.junit.Test;

/**
 * @author jgorrell
 * @version $Revision$ $Date$ $Author$
 */
public class PropertyDescriptorTest {

    /**
   * Test method for {@link net.rptools.chartool.model.property.PropertyDescriptor#PropertyDescriptor(java.lang.String)}.
   */
    @Test
    public void testPropertyDescriptorString() {
        PropertyDescriptor pd = new PropertyDescriptor("testProperty");
        assertEquals("testProperty", pd.getPropertyName());
        assertEquals("TEST_PROPERTY", pd.getColumnName());
        assertEquals("Test Property", pd.getDisplayName());
        assertEquals(PropertyDescriptorType.STRING, pd.getType());
        assertNull(pd.getDescription());
        assertNull(pd.getMapProperties());
        assertNull(pd.getScript());
        try {
            new PropertyDescriptor("bad.name");
            fail("No exception");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
   * Test method for {@link net.rptools.chartool.model.property.PropertyDescriptor#setType(PropertyDescriptorType)}.
   */
    @Test
    public void testSetType() {
        PropertyDescriptor pd = new PropertyDescriptor("testProperty");
        assertEquals(PropertyDescriptorType.STRING, pd.getType());
        pd.setType(PropertyDescriptorType.LIST);
        pd.setListValueDescriptor(pd);
        assertEquals(PropertyDescriptorType.LIST, pd.getType());
        pd.setType(PropertyDescriptorType.STRING);
        assertEquals(PropertyDescriptorType.STRING, pd.getType());
        assertNull(pd.getListValueDescriptor());
        pd.setType(PropertyDescriptorType.MAP);
        assertEquals(PropertyDescriptorType.MAP, pd.getType());
        assertNotNull(pd.getMapProperties());
        pd.setType(PropertyDescriptorType.BOOLEAN);
        assertEquals(PropertyDescriptorType.BOOLEAN, pd.getType());
        assertNull(pd.getMapProperties());
        pd.setType(PropertyDescriptorType.SCRIPT);
        assertEquals(PropertyDescriptorType.SCRIPT, pd.getType());
        pd.setScript("8 + 5");
        pd.setType(PropertyDescriptorType.NUMBER);
        assertEquals(PropertyDescriptorType.NUMBER, pd.getType());
        assertNull(pd.getScript());
    }

    /**
   * Test method for {@link net.rptools.chartool.model.property.PropertyDescriptor#createScriptValue(java.lang.String)}.
   */
    @Test
    public void testCreateCalculatedValue() {
        PropertyDescriptor pd = new PropertyDescriptor("testProperty", PropertyDescriptorType.SCRIPT);
        pd.setScript("oneValue + 5");
        ScriptPropertyValue spv = (ScriptPropertyValue) pd.createScriptValue(null);
        assertTrue(spv instanceof DefaultScriptPropertyValue);
        assertTrue(((DefaultScriptPropertyValue) spv).getScriptInstance() instanceof RPScript);
        assertEquals(false, spv.isData());
        assertEquals("oneValue + 5", spv.getScript());
        assertEquals(RPScript.RPTOOL_SCRIPT_TYPE, spv.getScriptType());
        spv = (ScriptPropertyValue) pd.createScriptValue("JavaScript::oneValue + 5");
        assertTrue(((DefaultScriptPropertyValue) spv).getScriptInstance() instanceof CompiledScript);
        assertEquals(true, spv.isData());
        assertEquals("oneValue + 5", spv.getScript());
        assertEquals("JavaScript", spv.getScriptType());
        pd = new PropertyDescriptor("testProperty", PropertyDescriptorType.SCRIPT);
        assertNull(pd.createScriptValue(null));
        assertEquals(Boolean.FALSE, pd.createScriptValue("false"));
        assertEquals(Boolean.TRUE, pd.createScriptValue("true"));
        assertEquals(new BigDecimal("1234567890"), pd.createScriptValue("1234567890"));
        assertEquals(new BigDecimal("-12345678.9"), pd.createScriptValue("-12345678.9"));
    }

    /**
   * Test method for {@link net.rptools.chartool.model.property.PropertyDescriptor#propertyToColumnName(java.lang.String)}.
   */
    @Test
    public void testPropertyToColumnName() {
        assertEquals("TEST_PROPERTY", PropertyDescriptor.propertyToColumnName("testProperty"));
        assertEquals("CAPS_FIRST", PropertyDescriptor.propertyToColumnName("CAPSFirst"));
        assertEquals("CAPS_LAST", PropertyDescriptor.propertyToColumnName("capsLAST"));
        assertEquals("CAPS_IN_MIDDLE", PropertyDescriptor.propertyToColumnName("capsINMiddle"));
    }

    /**
   * Test method for {@link net.rptools.chartool.model.property.PropertyDescriptor#propertyToDisplayName(java.lang.String)}.
   */
    @Test
    public void testPropertyToDisplayName() {
        assertEquals("Test Property", PropertyDescriptor.propertyToDisplayName("testProperty"));
        assertEquals("CAPS First", PropertyDescriptor.propertyToDisplayName("CAPSFirst"));
        assertEquals("Caps LAST", PropertyDescriptor.propertyToDisplayName("capsLAST"));
        assertEquals("Caps IN Middle", PropertyDescriptor.propertyToDisplayName("capsINMiddle"));
    }

    /**
   * Test method for {@link net.rptools.chartool.model.property.PropertyDescriptor#validatePropertyName(java.lang.String)}.
   */
    @Test
    public void testValidatePropertyName() {
        assertNotNull(PropertyDescriptor.validatePropertyName(" spaceBefore"));
        assertNotNull(PropertyDescriptor.validatePropertyName("space Middle"));
        assertNotNull(PropertyDescriptor.validatePropertyName("spaceAfter "));
        assertNotNull(PropertyDescriptor.validatePropertyName("7firstNumber"));
        assertNotNull(PropertyDescriptor.validatePropertyName("-firstDash"));
        assertNotNull(PropertyDescriptor.validatePropertyName("test:colon"));
        assertNotNull(PropertyDescriptor.validatePropertyName("test.dot"));
        assertNotNull(PropertyDescriptor.validatePropertyName("test#pound"));
        assertNotNull(PropertyDescriptor.validatePropertyName("test_underscore"));
        assertNotNull(PropertyDescriptor.validatePropertyName("test-dash"));
        assertNull(PropertyDescriptor.validatePropertyName("abcdefghijklmnopqrstuvwxyz"));
        assertNull(PropertyDescriptor.validatePropertyName("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertNull(PropertyDescriptor.validatePropertyName("a0123456789"));
    }
}
