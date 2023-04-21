package com.ssg.tools.jsonxml.json.schema;

import com.ssg.tools.jsonxml.common.PRIMITIVE_TYPE;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author ssg
 */
public class JSchemaTest extends TestCase {

    boolean IGNORE_NOT_IMPLEMENTED = true;

    public JSchemaTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getName method, of class JSchema.
     */
    public void testGetName() {
        System.out.println("getName");
        JSchema instance = new JSchema();
        assertNull(instance.getName());
        String expResult = "aaa";
        instance.setName(expResult);
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class JSchema.
     */
    public void testSetName() {
        System.out.println("setName");
        JSchema instance = new JSchema();
        String expResult = "aaa";
        instance.setName(expResult);
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getType method, of class JSchema.
     */
    public void testGetType() throws Exception {
        System.out.println("getType");
        JSchema instance = new JSchema();
        instance.setType(JSONValueType.ARRAY);
        List expResult = Collections.singletonList(JSONValueType.ARRAY);
        assertEquals(expResult, instance.getType());
        instance = new JSchema();
        instance.setType(new JSONValueType[] { JSONValueType.ARRAY });
        assertEquals(expResult, instance.getType());
    }

    /**
     * Test of setType method, of class JSchema.
     */
    public void testSetType_JSONValueType() throws Exception {
        System.out.println("setType");
        JSchema instance = new JSchema();
        instance.setType(JSONValueType.ARRAY);
        List expResult = Collections.singletonList(JSONValueType.ARRAY);
        assertEquals(expResult, instance.getType());
    }

    /**
     * Test of setType method, of class JSchema.
     */
    public void testSetType_JSONValueTypeArr() throws Exception {
        System.out.println("setType");
        JSchema instance = new JSchema();
        List expResult = Collections.singletonList(JSONValueType.ARRAY);
        instance = new JSchema();
        instance.setType(new JSONValueType[] { JSONValueType.ARRAY });
        assertEquals(expResult, instance.getType());
    }

    /**
     * Test of setType method, of class JSchema.
     */
    public void testSetType_List() throws Exception {
        System.out.println("setType");
        JSchema instance = new JSchema();
        List expResult = Collections.singletonList(JSONValueType.ARRAY);
        instance = new JSchema();
    }

    /**
     * Test of getProperties method, of class JSchema.
     */
    public void testGetProperties() throws Exception {
        System.out.println("getProperties");
        JSchema instance = new JSchema();
        assertNull(instance.getProperties());
        instance.setProperty("test", new JSchema());
        assertNotNull(instance.getProperties());
        assertEquals(1, instance.getProperties().size());
    }

    /**
     * Test of getProperty method, of class JSchema.
     */
    public void testGetProperty() throws Exception {
        System.out.println("getProperty");
        JSchema instance = new JSchema();
        assertNull(instance.getProperties());
        instance.setProperty("test", new JSchema());
        assertNotNull(instance.getProperties());
        assertEquals(1, instance.getProperties().size());
        assertEquals(new JSchema(), instance.getProperty("test"));
    }

    /**
     * Test of hasProperties method, of class JSchema.
     */
    public void testHasProperties() throws Exception {
        System.out.println("hasProperties");
        JSchema instance = new JSchema();
        assertFalse(instance.hasProperties());
        instance.setProperty("test", new JSchema());
        assertNotNull(instance.getProperties());
        assertTrue(instance.hasProperties());
    }

    /**
     * Test of setProperties method, of class JSchema.
     */
    public void testSetProperties() throws Exception {
        System.out.println("setProperties");
        Map<String, JSchema> properties = new LinkedHashMap<String, JSchema>();
        JSchema instance = new JSchema();
        instance.setProperties(properties);
        assertEquals(properties, instance.getProperties());
    }

    /**
     * Test of setProperty method, of class JSchema.
     */
    public void testSetProperty() throws Exception {
        System.out.println("setProperty");
        JSchema instance = new JSchema();
        assertNull(instance.getProperties());
        instance.setProperty("test", new JSchema());
        assertNotNull(instance.getProperties());
        assertEquals(1, instance.getProperties().size());
        assertEquals(new JSchema(), instance.getProperty("test"));
    }

    /**
     * Test of getPatternProperties method, of class JSchema.
     */
    public void testGetPatternProperties() throws Exception {
        System.out.println("getPatternProperties");
        JSchema instance = new JSchema();
        assertNull(instance.getPatternProperties());
        instance.setPatternProperty("test", new JSchema());
        assertNotNull(instance.getPatternProperties());
        assertEquals(1, instance.getPatternProperties().size());
        assertEquals(new JSchema(), instance.getPatternProperty("test"));
    }

    /**
     * Test of getPatternProperty method, of class JSchema.
     */
    public void testGetPatternProperty() throws Exception {
        System.out.println("getPatternProperty");
        JSchema instance = new JSchema();
        assertNull(instance.getPatternProperties());
        instance.setPatternProperty("test", new JSchema());
        assertNotNull(instance.getPatternProperties());
        assertEquals(new JSchema(), instance.getPatternProperty("test"));
    }

    /**
     * Test of setPatternProperty method, of class JSchema.
     */
    public void testSetPatternProperties() throws Exception {
        System.out.println("setPatternProperties");
        Map<String, JSchema> properties = new LinkedHashMap<String, JSchema>();
        JSchema instance = new JSchema();
        assertNull(instance.getPatternProperties());
        instance.setPatternProperties(properties);
        assertEquals(properties, instance.getPatternProperties());
    }

    /**
     * Test of setPatternProperty method, of class JSchema.
     */
    public void testSetPatternProperty() throws Exception {
        System.out.println("setPatternProperty");
        JSchema instance = new JSchema();
        assertNull(instance.getPatternProperties());
        instance.setPatternProperty("test", new JSchema());
        assertNotNull(instance.getPatternProperties());
        assertEquals(1, instance.getPatternProperties().size());
        assertEquals(new JSchema(), instance.getPatternProperty("test"));
    }

    /**
     * Test of getAdditionalProperties method, of class JSchema.
     */
    public void testGetAdditionalProperties() throws Exception {
        System.out.println("getAdditionalProperties");
        JSchema instance = new JSchema();
        assertNull(instance.getAdditionalProperties());
        instance.setAdditionalProperties(new JSchema());
        assertEquals(new JSchema(), instance.getAdditionalProperties());
        assertTrue(instance.isAdditionalProperties());
    }

    /**
     * Test of isAdditionalProperties method, of class JSchema.
     */
    public void testIsAdditionalProperties() throws Exception {
        System.out.println("isAdditionalProperties");
        JSchema instance = new JSchema();
        assertTrue(instance.isAdditionalProperties());
        instance.setAdditionalProperties(false);
        assertFalse(instance.isAdditionalProperties());
        instance.setAdditionalProperties(new JSchema());
        assertTrue(instance.isAdditionalProperties());
    }

    /**
     * Test of setAdditionalProperties method, of class JSchema.
     */
    public void testSetAdditionalProperties_JSchema() throws Exception {
        System.out.println("setAdditionalProperties");
        JSchema instance = new JSchema();
        assertNull(instance.getAdditionalProperties());
        instance.setAdditionalProperties(new JSchema());
        assertEquals(new JSchema(), instance.getAdditionalProperties());
        assertTrue(instance.isAdditionalProperties());
    }

    /**
     * Test of setAdditionalProperties method, of class JSchema.
     */
    public void testSetAdditionalProperties_Boolean() throws Exception {
        System.out.println("setAdditionalProperties");
        JSchema instance = new JSchema();
        assertTrue(instance.isAdditionalProperties());
        instance.setAdditionalProperties(false);
        assertFalse(instance.isAdditionalProperties());
        instance.setAdditionalProperties(new JSchema());
        assertTrue(instance.isAdditionalProperties());
    }

    /**
     * Test of getItems method, of class JSchema.
     */
    public void testGetItems() throws Exception {
        System.out.println("getItems");
        JSchema instance = new JSchema();
        JSchema expResult = new JSchema();
        assertNull(instance.getItems());
        instance.setItems(expResult);
        assertEquals(expResult, instance.getItems());
    }

    /**
     * Test of setItems method, of class JSchema.
     */
    public void testSetItems() throws Exception {
        System.out.println("setItems");
        JSchema instance = new JSchema();
        JSchema expResult = new JSchema();
        assertNull(instance.getItems());
        instance.setItems(expResult);
        assertEquals(expResult, instance.getItems());
    }

    /**
     * Test of getAdditionalItems method, of class JSchema.
     */
    public void testGetAdditionalItems() throws Exception {
        System.out.println("getAdditionalItems");
        JSchema instance = new JSchema();
        JSchema expResult = new JSchema();
        assertNull(instance.getAdditionalItems());
        instance.setAdditionalItems(expResult);
        assertEquals(expResult, instance.getAdditionalItems());
    }

    /**
     * Test of isAdditionalItems method, of class JSchema.
     */
    public void testIsAdditionalItems() throws Exception {
        System.out.println("isAdditionalItems");
        JSchema instance = new JSchema();
        JSchema expResult = new JSchema();
        assertNull(instance.getAdditionalItems());
        assertTrue(instance.isAdditionalItems());
        instance.setAdditionalItems(expResult);
        assertTrue(instance.isAdditionalItems());
        instance.setAdditionalItems(false);
        assertFalse(instance.isAdditionalItems());
        instance.setAdditionalItems(true);
        assertTrue(instance.isAdditionalItems());
    }

    /**
     * Test of setAdditionalItems method, of class JSchema.
     */
    public void testSetAdditionalItems_JSchema() throws Exception {
        System.out.println("setAdditionalItems");
        JSchema instance = new JSchema();
        JSchema expResult = new JSchema();
        assertNull(instance.getAdditionalItems());
        assertTrue(instance.isAdditionalItems());
        instance.setAdditionalItems(expResult);
        assertTrue(instance.isAdditionalItems());
    }

    /**
     * Test of setAdditionalItems method, of class JSchema.
     */
    public void testSetAdditionalItems_Boolean() throws Exception {
        System.out.println("setAdditionalItems");
        JSchema instance = new JSchema();
        JSchema expResult = new JSchema();
        assertNull(instance.getAdditionalItems());
        assertTrue(instance.isAdditionalItems());
        instance.setAdditionalItems(false);
        assertFalse(instance.isAdditionalItems());
        instance.setAdditionalItems(true);
        assertTrue(instance.isAdditionalItems());
    }

    /**
     * Test of isRequired method, of class JSchema.
     */
    public void testIsRequired() throws Exception {
        System.out.println("isRequired");
        JSchema instance = new JSchema();
        assertFalse(instance.isRequired());
        instance.setRequired(true);
        assertTrue(instance.isRequired());
        instance.setRequired(null);
        assertFalse(instance.isRequired());
        instance.setRequired(false);
        assertFalse(instance.isRequired());
    }

    /**
     * Test of setRequired method, of class JSchema.
     */
    public void testSetRequired() throws Exception {
        System.out.println("setRequired");
        JSchema instance = new JSchema();
        assertFalse(instance.isRequired());
        instance.setRequired(true);
        assertTrue(instance.isRequired());
        instance.setRequired(null);
        assertFalse(instance.isRequired());
        instance.setRequired(false);
        assertFalse(instance.isRequired());
    }

    /**
     * Test of getDependencies method, of class JSchema.
     */
    public void testGetDependencies() throws Exception {
        System.out.println("getDependencies");
        JSchema instance = new JSchema();
        Map<String, Object> deps = new LinkedHashMap<String, Object>();
        assertNull(instance.getDependencies());
        instance.setDependencies(deps);
        assertEquals(deps, instance.getDependencies());
        instance.setDependencies(null);
        assertNull(instance.getDependencies());
    }

    /**
     * Test of setDependencies method, of class JSchema.
     */
    public void testSetDependencies() throws Exception {
        System.out.println("setDependencies");
        JSchema instance = new JSchema();
        Map<String, Object> deps = new LinkedHashMap<String, Object>();
        assertNull(instance.getDependencies());
        instance.setDependencies(deps);
        assertEquals(deps, instance.getDependencies());
        instance.setDependencies(null);
        assertNull(instance.getDependencies());
    }

    /**
     * Test of getMinimum method, of class JSchema.
     */
    public void testGetMinimum() throws Exception {
        System.out.println("getMinimum");
        JSchema instance = new JSchema();
        assertNull(instance.getMinimum());
        instance.setMinimum(1);
        assertEquals(1, instance.getMinimum());
        instance.setMinimum(-121.2);
        assertEquals(-121.2, instance.getMinimum());
    }

    /**
     * Test of setMinimum method, of class JSchema.
     */
    public void testSetMinimum() throws Exception {
        System.out.println("setMinimum");
        JSchema instance = new JSchema();
        assertNull(instance.getMinimum());
        instance.setMinimum(1);
        assertEquals(1, instance.getMinimum());
        instance.setMinimum(-121.2);
        assertEquals(-121.2, instance.getMinimum());
    }

    /**
     * Test of getMaximum method, of class JSchema.
     */
    public void testGetMaximum() throws Exception {
        System.out.println("getMaximum");
        JSchema instance = new JSchema();
        assertNull(instance.getMaximum());
        instance.setMaximum(1);
        assertEquals(1, instance.getMaximum());
        instance.setMaximum(-121.2);
        assertEquals(-121.2, instance.getMaximum());
    }

    /**
     * Test of setMaximum method, of class JSchema.
     */
    public void testSetMaximum() throws Exception {
        System.out.println("setMaximum");
        JSchema instance = new JSchema();
        assertNull(instance.getMaximum());
        instance.setMaximum(1);
        assertEquals(1, instance.getMaximum());
        instance.setMaximum(-121.2);
        assertEquals(-121.2, instance.getMaximum());
    }

    /**
     * Test of isExclusiveMinimum method, of class JSchema.
     */
    public void testIsExclusiveMinimum() throws Exception {
        System.out.println("isExclusiveMinimum");
        JSchema instance = new JSchema();
        assertFalse(instance.isExclusiveMinimum());
        instance.setExclusiveMinimum(true);
        assertTrue(instance.isExclusiveMinimum());
        instance.setExclusiveMinimum(null);
        assertFalse(instance.isExclusiveMinimum());
        instance.setExclusiveMinimum(false);
        assertFalse(instance.isExclusiveMinimum());
    }

    /**
     * Test of setExclusiveMinimum method, of class JSchema.
     */
    public void testSetExclusiveMinimum() throws Exception {
        System.out.println("setExclusiveMinimum");
        JSchema instance = new JSchema();
        assertFalse(instance.isExclusiveMinimum());
        instance.setExclusiveMinimum(true);
        assertTrue(instance.isExclusiveMinimum());
        instance.setExclusiveMinimum(null);
        assertFalse(instance.isExclusiveMinimum());
        instance.setExclusiveMinimum(false);
        assertFalse(instance.isExclusiveMinimum());
    }

    /**
     * Test of isExclusiveMaximum method, of class JSchema.
     */
    public void testIsExclusiveMaximum() throws Exception {
        System.out.println("isExclusiveMaximum");
        JSchema instance = new JSchema();
        assertFalse(instance.isExclusiveMaximum());
        instance.setExclusiveMaximum(true);
        assertTrue(instance.isExclusiveMaximum());
        instance.setExclusiveMaximum(null);
        assertFalse(instance.isExclusiveMaximum());
        instance.setExclusiveMaximum(false);
        assertFalse(instance.isExclusiveMaximum());
    }

    /**
     * Test of setExclusiveMaximum method, of class JSchema.
     */
    public void testSetExclusiveMaximum() throws Exception {
        System.out.println("setExclusiveMaximum");
        JSchema instance = new JSchema();
        assertFalse(instance.isExclusiveMaximum());
        instance.setExclusiveMaximum(true);
        assertTrue(instance.isExclusiveMaximum());
        instance.setExclusiveMaximum(null);
        assertFalse(instance.isExclusiveMaximum());
        instance.setExclusiveMaximum(false);
        assertFalse(instance.isExclusiveMaximum());
    }

    /**
     * Test of getMinItems method, of class JSchema.
     */
    public void testGetMinItems() throws Exception {
        System.out.println("getMinItems");
        JSchema instance = new JSchema();
        assertNull(instance.getMinItems());
        instance.setMinItems(20);
        assertEquals((Integer) 20, instance.getMinItems());
    }

    /**
     * Test of setMinItems method, of class JSchema.
     */
    public void testSetMinItems() throws Exception {
        System.out.println("setMinItems");
        JSchema instance = new JSchema();
        assertNull(instance.getMinItems());
        instance.setMinItems(20);
        assertEquals((Integer) 20, instance.getMinItems());
        instance.setMinItems(null);
        assertNull(instance.getMinItems());
        try {
            instance.setMinItems(-20);
            fail("MUST throw exception if setting min length to negative value.");
        } catch (JSONSchemaException jsex) {
        }
    }

    /**
     * Test of getMaxItems method, of class JSchema.
     */
    public void testGetMaxItems() throws Exception {
        System.out.println("getMaxItems");
        JSchema instance = new JSchema();
        assertNull(instance.getMaxItems());
        instance.setMaxItems(20);
        assertEquals((Integer) 20, instance.getMaxItems());
    }

    /**
     * Test of setMaxItems method, of class JSchema.
     */
    public void testSetMaxItems() throws Exception {
        System.out.println("setMaxItems");
        JSchema instance = new JSchema();
        assertNull(instance.getMaxItems());
        instance.setMaxItems(20);
        assertEquals((Integer) 20, instance.getMaxItems());
        instance.setMaxItems(null);
        assertNull(instance.getMaxItems());
        try {
            instance.setMaxItems(-20);
            fail("MUST throw exception if setting min length to negative value.");
        } catch (JSONSchemaException jsex) {
        }
    }

    /**
     * Test of isUniqueItems method, of class JSchema.
     */
    public void testIsUniqueItems() throws Exception {
        System.out.println("isUniqueItems");
        JSchema instance = new JSchema();
        assertFalse(instance.isUniqueItems());
        instance.setUniqueItems(true);
        assertTrue(instance.isUniqueItems());
        instance.setUniqueItems(null);
        assertFalse(instance.isUniqueItems());
        instance.setUniqueItems(false);
        assertFalse(instance.isUniqueItems());
    }

    /**
     * Test of setUniqueItems method, of class JSchema.
     */
    public void testSetUniqueItems() throws Exception {
        System.out.println("setUniqueItems");
        JSchema instance = new JSchema();
        assertFalse(instance.isUniqueItems());
        instance.setUniqueItems(true);
        assertTrue(instance.isUniqueItems());
        instance.setUniqueItems(null);
        assertFalse(instance.isUniqueItems());
        instance.setUniqueItems(false);
        assertFalse(instance.isUniqueItems());
    }

    /**
     * Test of getPattern method, of class JSchema.
     */
    public void testGetPattern() throws Exception {
        System.out.println("getPattern");
        JSchema instance = new JSchema();
        assertNull(instance.getPattern());
        instance.setPattern("a");
        assertEquals("a", instance.getPattern());
        instance.setPattern(null);
        assertEquals(null, instance.getPattern());
        try {
            instance.setPattern("{.+}");
            fail("MUST throw exception when setting invalid pattern.");
        } catch (JSONSchemaException jsex) {
        }
    }

    /**
     * Test of setPattern method, of class JSchema.
     */
    public void testSetPattern() throws Exception {
        System.out.println("setPattern");
        JSchema instance = new JSchema();
        assertNull(instance.getPattern());
        instance.setPattern("a");
        assertEquals("a", instance.getPattern());
        instance.setPattern(null);
        assertEquals(null, instance.getPattern());
        try {
            instance.setPattern("{.+}");
            fail("MUST throw exception when setting invalid pattern.");
        } catch (JSONSchemaException jsex) {
        }
    }

    /**
     * Test of getMinLength method, of class JSchema.
     */
    public void testGetMinLength() throws Exception {
        System.out.println("getMinLength");
        JSchema instance = new JSchema();
        assertNull(instance.getMinLength());
        instance.setMinLength(20);
        assertEquals((Integer) 20, instance.getMinLength());
    }

    /**
     * Test of setMinLength method, of class JSchema.
     */
    public void testSetMinLength() throws Exception {
        System.out.println("setMinLength");
        JSchema instance = new JSchema();
        assertNull(instance.getMinLength());
        instance.setMinLength(20);
        assertEquals((Integer) 20, instance.getMinLength());
        instance.setMinLength(null);
        assertNull(instance.getMinLength());
        try {
            instance.setMinLength(-20);
            fail("MUST throw exception if setting min length to negative value.");
        } catch (JSONSchemaException jsex) {
        }
    }

    /**
     * Test of getMaxLength method, of class JSchema.
     */
    public void testGetMaxLength() throws Exception {
        System.out.println("getMaxLength");
        JSchema instance = new JSchema();
        assertNull(instance.getMaxLength());
        instance.setMaxLength(20);
        assertEquals((Integer) 20, instance.getMaxLength());
    }

    /**
     * Test of setMaxLength method, of class JSchema.
     */
    public void testSetMaxLength() throws Exception {
        System.out.println("setMaxLength");
        JSchema instance = new JSchema();
        assertNull(instance.getMaxLength());
        instance.setMaxLength(20);
        assertEquals((Integer) 20, instance.getMaxLength());
        instance.setMaxLength(null);
        assertNull(instance.getMaxLength());
        try {
            instance.setMaxLength(-20);
            fail("MUST throw exception if setting max length to negative value.");
        } catch (JSONSchemaException jsex) {
        }
    }

    /**
     * Test of getEnum method, of class JSchema.
     */
    public void testGetEnum() throws Exception {
        System.out.println("getEnum");
        JSchema instance = new JSchema();
        assertNull(instance.getEnum());
        List expResult = new ArrayList();
        expResult.add("1");
        expResult.add(1.1);
        expResult.add(new Date());
        instance.setEnum(expResult);
        assertEquals(expResult, instance.getEnum());
    }

    /**
     * Test of setEnum method, of class JSchema.
     */
    public void testSetEnum_List() throws Exception {
        System.out.println("setEnum");
        JSchema instance = new JSchema();
        assertNull(instance.getEnum());
        List expResult = new ArrayList();
        expResult.add("1");
        expResult.add(1.1);
        expResult.add(new Date());
        instance.setEnum(expResult);
        assertEquals(expResult, instance.getEnum());
        instance.setEnum((List) null);
        assertNull(instance.getEnum());
        TestIterable iterable = new TestIterable();
        instance.setEnum(iterable);
        assertEquals(iterable.list, instance.getEnum());
        expResult.add(expResult.get(0));
        try {
            instance.setEnum(expResult);
            fail("MUST throw exception if enumeration list contains duplicates.");
        } catch (JSONSchemaException jsex) {
        }
    }

    /**
     * Test of setEnum method, of class JSchema.
     */
    public void testSetEnum_ObjectArr() throws Exception {
        System.out.println("setEnum");
        JSchema instance = new JSchema();
        assertNull(instance.getEnum());
        Object[] expResult = new Object[] { "1", 1.1, new Date() };
        instance.setEnum(expResult);
        assertEquals(Arrays.asList(expResult), instance.getEnum());
        instance.setEnum((Object[]) null);
        assertNull(instance.getEnum());
        expResult[expResult.length - 1] = expResult[0];
        try {
            instance.setEnum(expResult);
            fail("MUST throw exception if enumeration array contains duplicates.");
        } catch (JSONSchemaException jsex) {
        }
    }

    /**
     * Test of setEnum method, of class JSchema.
     */
    public void testSetEnum_Object() throws Exception {
        System.out.println("setEnum");
        JSchema instance = new JSchema();
        assertNull(instance.getEnum());
        Object[] eA = new Object[] { "1", 1.1, new Date() };
        List eL = Arrays.asList(eA);
        Object eO = eA[0];
        Map eM = new LinkedHashMap();
        for (Object o : eA) {
            eM.put(o, o);
        }
        instance.setEnum((Object) eA);
        assertEquals(eL, instance.getEnum());
        instance.setEnum((Object) null);
        assertNull(instance.getEnum());
        instance.setEnum((Object) eL);
        assertEquals(eL, instance.getEnum());
        instance.setEnum((Object) null);
        assertNull(instance.getEnum());
        instance.setEnum(eO);
        assertEquals(Collections.singletonList(eO), instance.getEnum());
        instance.setEnum(eM.keySet());
        assertEquals(eL, instance.getEnum());
        instance.setEnum(eM.keySet().iterator());
        assertEquals(eL, instance.getEnum());
        instance.setEnum(Collections.enumeration(eM.keySet()));
        assertEquals(eL, instance.getEnum());
        instance.setEnum(PRIMITIVE_TYPE.class);
        assertEquals(Arrays.asList(PRIMITIVE_TYPE.values()), instance.getEnum());
        eA[eA.length - 1] = eA[0];
        eL.set(eL.size() - 1, eL.get(0));
        try {
            instance.setEnum(eA);
            fail("MUST throw exception if enumeration list contains duplicates.");
        } catch (JSONSchemaException jsex) {
        }
        try {
            instance.setEnum(eL);
            fail("MUST throw exception if enumeration list contains duplicates.");
        } catch (JSONSchemaException jsex) {
        }
    }

    /**
     * Test of getDefault method, of class JSchema.
     */
    public void testGetDefault() throws Exception {
        System.out.println("getDefault");
        JSchema instance = new JSchema();
        assertNull(instance.getDefault());
        instance.setDefault("");
        assertEquals("", instance.getDefault());
        instance.setDefault(null);
        assertNull(instance.getDefault());
        Object o = new TestClone("aaa");
        instance.setDefault(o);
        assertEquals(o, instance.getDefault());
        assertEquals(instance.getDefault(), instance.getDefault());
        assertNotSame(instance.getDefault(), instance.getDefault());
    }

    /**
     * Test of hasDefault method, of class JSchema.
     */
    public void testHasDefault() throws Exception {
        System.out.println("hasDefault");
        JSchema instance = new JSchema();
        assertFalse(instance.hasDefault());
        instance.setDefault("");
        assertTrue(instance.hasDefault());
        instance.setDefault(null);
        assertFalse(instance.hasDefault());
        Object o = new TestClone();
        instance.setDefault(o);
        assertTrue(instance.hasDefault());
    }

    /**
     * Test of setDefault method, of class JSchema.
     */
    public void testSetDefault() throws Exception {
        System.out.println("setDefault");
        JSchema instance = new JSchema();
        assertFalse(instance.hasDefault());
        instance.setDefault("");
        assertTrue(instance.hasDefault());
        instance.setDefault(null);
        assertFalse(instance.hasDefault());
        Object o = 1;
        instance.setDefault(o);
        assertEquals(o, instance.getDefault());
        o = 1.1;
        instance.setDefault(o);
        assertEquals(o, instance.getDefault());
        o = new Date();
        instance.setDefault(o);
        assertEquals(o, instance.getDefault());
        o = true;
        instance.setDefault(o);
        assertEquals(o, instance.getDefault());
        o = null;
        instance.setDefault(o);
        assertEquals(o, instance.getDefault());
        o = new TestClone("aaa");
        instance.setDefault(o);
        assertEquals(o, instance.getDefault());
        assertEquals(instance.getDefault(), instance.getDefault());
        assertNotSame(instance.getDefault(), instance.getDefault());
        o = new LinkedHashMap() {

            {
                put("a", "b");
            }
        };
        instance.setDefault(o);
        assertEquals(o, instance.getDefault());
        assertEquals(instance.getDefault(), instance.getDefault());
        assertNotSame(instance.getDefault(), instance.getDefault());
        o = new ArrayList() {

            {
                add("a");
                add("b");
            }
        };
        instance.setDefault(o);
        assertEquals(o, instance.getDefault());
        assertEquals(instance.getDefault(), instance.getDefault());
        assertNotSame(instance.getDefault(), instance.getDefault());
        o = new File("");
        try {
            instance.setDefault(o);
            fail("MUST throw exception if non-cloneable non-immutable default value is used.");
        } catch (JSONSchemaException jsex) {
        }
        o = new URL("http://aaa.bbb.com");
        try {
            instance.setDefault(o);
            fail("MUST throw exception if non-cloneable non-immutable default value is used.");
        } catch (JSONSchemaException jsex) {
        }
        o = new TestCloneWrong("aaa");
        try {
            instance.setDefault(o);
            fail("MUST throw exception if clone error.");
        } catch (JSONSchemaException jsex) {
        }
    }

    /**
     * Test of getTitle method, of class JSchema.
     */
    public void testGetTitle() throws Exception {
        System.out.println("getTitle");
        JSchema instance = new JSchema();
        assertNull(instance.getTitle());
        instance.setTitle("aaa");
        assertEquals("aaa", instance.getTitle());
        instance.setTitle(null);
        assertNull(instance.getTitle());
    }

    /**
     * Test of setTitle method, of class JSchema.
     */
    public void testSetTitle() throws Exception {
        System.out.println("setTitle");
        JSchema instance = new JSchema();
        assertNull(instance.getTitle());
        instance.setTitle("aaa");
        assertEquals("aaa", instance.getTitle());
        instance.setTitle(null);
        assertNull(instance.getTitle());
    }

    /**
     * Test of getDescription method, of class JSchema.
     */
    public void testGetDescription() throws Exception {
        System.out.println("getDescription");
        JSchema instance = new JSchema();
        assertNull(instance.getDescription());
        instance.setDescription("aaa");
        assertEquals("aaa", instance.getDescription());
        instance.setDescription(null);
        assertNull(instance.getDescription());
    }

    /**
     * Test of setDescription method, of class JSchema.
     */
    public void testSetDescription() throws Exception {
        System.out.println("setDescription");
        JSchema instance = new JSchema();
        assertNull(instance.getDescription());
        instance.setDescription("aaa");
        assertEquals("aaa", instance.getDescription());
        instance.setDescription(null);
        assertNull(instance.getDescription());
    }

    /**
     * Test of getFormat method, of class JSchema.
     */
    public void testGetFormat() throws Exception {
        System.out.println("getFormat");
        JSchema instance = new JSchema();
        assertNull(instance.getFormat());
        instance.setFormat(JSONValueFormat.DATE);
        assertEquals(JSONValueFormat.DATE, instance.getFormat());
        instance.setFormat(new JSONValueFormat("date"));
        assertEquals(JSONValueFormat.DATE, instance.getFormat());
        instance.setFormat(null);
        assertNull(instance.getFormat());
    }

    /**
     * Test of setFormat method, of class JSchema.
     */
    public void testSetFormat() throws Exception {
        System.out.println("setFormat");
        JSchema instance = new JSchema();
        assertNull(instance.getFormat());
        instance.setFormat(JSONValueFormat.DATE);
        assertEquals(JSONValueFormat.DATE, instance.getFormat());
        instance.setFormat(new JSONValueFormat("date"));
        assertEquals(JSONValueFormat.DATE, instance.getFormat());
        instance.setFormat(null);
        assertNull(instance.getFormat());
    }

    /**
     * Test of getDivisibleBy method, of class JSchema.
     */
    public void testGetDivisibleBy() throws Exception {
        System.out.println("getDivisibleBy");
        JSchema instance = new JSchema();
        assertNull(instance.getDivisibleBy());
        instance.setDivisibleBy(3);
        assertEquals(3, instance.getDivisibleBy());
        instance.setDivisibleBy(3.3);
        assertEquals(3.3, instance.getDivisibleBy());
        instance.setDivisibleBy(null);
        assertNull(instance.getDivisibleBy());
    }

    /**
     * Test of setDivisibleBy method, of class JSchema.
     */
    public void testSetDivisibleBy() throws Exception {
        System.out.println("setDivisibleBy");
        JSchema instance = new JSchema();
        assertNull(instance.getDivisibleBy());
        instance.setDivisibleBy(3);
        assertEquals(3, instance.getDivisibleBy());
        instance.setDivisibleBy(3.3);
        assertEquals(3.3, instance.getDivisibleBy());
        instance.setDivisibleBy(null);
        assertNull(instance.getDivisibleBy());
    }

    /**
     * Test of getDisallow method, of class JSchema.
     */
    public void testGetDisallow() throws Exception {
        System.out.println("getDisallow");
        JSchema instance = new JSchema();
        instance.setDisallow(JSONValueType.ARRAY);
        List expResult = Collections.singletonList(JSONValueType.ARRAY);
        assertEquals(expResult, instance.getDisallow());
        instance = new JSchema();
        instance.setDisallow(new JSONValueType[] { JSONValueType.ARRAY });
        assertEquals(expResult, instance.getDisallow());
    }

    /**
     * Test of setDisallow method, of class JSchema.
     */
    public void testSetDisallow_JSONValueType() throws Exception {
        System.out.println("setDisallow");
        JSchema instance = new JSchema();
        instance.setDisallow(JSONValueType.ARRAY);
        List expResult = Collections.singletonList(JSONValueType.ARRAY);
        assertEquals(expResult, instance.getDisallow());
    }

    /**
     * Test of setDisallow method, of class JSchema.
     */
    public void testSetDisallow_JSONValueTypeArr() throws Exception {
        System.out.println("setDisallow");
        JSchema instance = new JSchema();
        List expResult = Collections.singletonList(JSONValueType.ARRAY);
        instance = new JSchema();
        instance.setDisallow(new JSONValueType[] { JSONValueType.ARRAY });
        assertEquals(expResult, instance.getDisallow());
    }

    /**
     * Test of getExtends method, of class JSchema.
     */
    public void testGetExtends() throws Exception {
        System.out.println("getExtends");
        JSchema instance = new JSchema();
        assertNull(instance.getExtends());
        JSONValueExtends extend = new JSONValueExtends("number");
        List el = Collections.singletonList(extend);
        instance.setExtends(extend);
        assertEquals(el, instance.getExtends());
        instance.setExtends((JSONValueExtends[]) el.toArray(new JSONValueExtends[el.size()]));
        assertEquals(el, instance.getExtends());
    }

    /**
     * Test of setExtends method, of class JSchema.
     */
    public void testSetExtends_JSONValueExtends() throws Exception {
        System.out.println("setExtends");
        JSchema instance = new JSchema();
        assertNull(instance.getExtends());
        JSONValueExtends extend = new JSONValueExtends("number");
        List el = Collections.singletonList(extend);
        instance.setExtends(extend);
        assertEquals(el, instance.getExtends());
    }

    /**
     * Test of setExtends method, of class JSchema.
     */
    public void testSetExtends_JSONValueExtendsArr() throws Exception {
        System.out.println("setExtends");
        JSchema instance = new JSchema();
        assertNull(instance.getExtends());
        JSONValueExtends extend = new JSONValueExtends("number");
        List el = Collections.singletonList(extend);
        instance.setExtends((JSONValueExtends[]) el.toArray(new JSONValueExtends[el.size()]));
        assertEquals(el, instance.getExtends());
    }

    /**
     * Test of getId method, of class JSchema.
     */
    public void testGetId() throws Exception {
        System.out.println("getId");
        JSchema instance = new JSchema();
        URI uri = new URI("#");
        assertNull(instance.getId());
        instance.setId(uri);
        assertEquals(uri, instance.getId());
        instance.setId(null);
        assertNull(instance.getId());
    }

    /**
     * Test of setId method, of class JSchema.
     */
    public void testSetId() throws Exception {
        System.out.println("setId");
        JSchema instance = new JSchema();
        URI uri = new URI("#");
        assertNull(instance.getId());
        instance.setId(uri);
        assertEquals(uri, instance.getId());
        instance.setId(null);
        assertNull(instance.getId());
    }

    /**
     * Test of get$ref method, of class JSchema.
     */
    public void testGet$ref() throws Exception {
        System.out.println("get$ref");
        JSchema instance = new JSchema();
        URI uri = new URI("#");
        assertNull(instance.get$ref());
        instance.set$ref(uri);
        assertEquals(uri, instance.get$ref());
        instance.set$ref(null);
        assertNull(instance.get$ref());
    }

    /**
     * Test of set$ref method, of class JSchema.
     */
    public void testSet$ref() throws Exception {
        System.out.println("set$ref");
        JSchema instance = new JSchema();
        URI uri = new URI("#");
        assertNull(instance.get$ref());
        instance.set$ref(uri);
        assertEquals(uri, instance.get$ref());
        instance.set$ref(null);
        assertNull(instance.get$ref());
    }

    /**
     * Test of get$schema method, of class JSchema.
     */
    public void testGet$schema() throws Exception {
        System.out.println("get$schema");
        JSchema instance = new JSchema();
        URI uri = new URI("#");
        assertNull(instance.get$schema());
        instance.set$schema(uri);
        assertEquals(uri, instance.get$schema());
        instance.set$schema(null);
        assertNull(instance.get$schema());
    }

    /**
     * Test of set$schema method, of class JSchema.
     */
    public void testSet$schema() throws Exception {
        System.out.println("set$schema");
        JSchema instance = new JSchema();
        URI uri = new URI("#");
        assertNull(instance.get$schema());
        instance.set$schema(uri);
        assertEquals(uri, instance.get$schema());
        instance.set$schema(null);
        assertNull(instance.get$schema());
    }

    /**
     * Test of getLinks method, of class JSchema.
     */
    public void testGetLinks() throws Exception {
        System.out.println("getLinks");
        JSchema instance = new JSchema();
        List links = new ArrayList();
        assertNull(instance.getLinks());
        instance.setLinks(links);
        assertEquals(links, instance.getLinks());
    }

    /**
     * Test of setLinks method, of class JSchema.
     */
    public void testSetLinks_List() throws Exception {
        System.out.println("setLinks");
        JSchema instance = new JSchema();
        List links = new ArrayList();
        assertNull(instance.getLinks());
        instance.setLinks(links);
        assertEquals(links, instance.getLinks());
        instance.setLinks((List) null);
        assertNull(instance.getLinks());
    }

    /**
     * Test of setLinks method, of class JSchema.
     */
    public void testSetLinks_JLinkArr() throws Exception {
        System.out.println("setLinks");
        JSchema instance = new JSchema();
        JLink[] links = new JLink[0];
        assertNull(instance.getLinks());
        instance.setLinks(links);
        assertEquals(Arrays.asList(links), instance.getLinks());
        instance.setLinks((JLink[]) null);
        assertNull(instance.getLinks());
    }

    /**
     * Test of getFragmentResolution method, of class JSchema.
     */
    public void testGetFragmentResolution() throws Exception {
        System.out.println("getFragmentResolution");
        JSchema instance = new JSchema();
        assertNull(instance.getFragmentResolution());
        for (FRAGMENT_PROTOCOL fp : FRAGMENT_PROTOCOL.values()) {
            instance.setFragmentResolution(fp);
            assertEquals(fp, instance.getFragmentResolution());
        }
        instance.setFragmentResolution(null);
        assertNull(instance.getFragmentResolution());
    }

    /**
     * Test of setFragmentResolution method, of class JSchema.
     */
    public void testSetFragmentResolution() throws Exception {
        System.out.println("setFragmentResolution");
        JSchema instance = new JSchema();
        assertNull(instance.getFragmentResolution());
        for (FRAGMENT_PROTOCOL fp : FRAGMENT_PROTOCOL.values()) {
            instance.setFragmentResolution(fp);
            assertEquals(fp, instance.getFragmentResolution());
        }
        instance.setFragmentResolution(null);
        assertNull(instance.getFragmentResolution());
    }

    /**
     * Test of isReadonly method, of class JSchema.
     */
    public void testIsReadonly() {
        System.out.println("isReadonly");
        JSchema instance = new JSchema();
        assertFalse(instance.isReadonly());
        instance.setReadonly(true);
        assertTrue(instance.isReadonly());
        instance.setReadonly(null);
        assertFalse(instance.isReadonly());
        instance.setReadonly(false);
        assertFalse(instance.isReadonly());
    }

    /**
     * Test of setReadonly method, of class JSchema.
     */
    public void testSetReadonly() {
        System.out.println("setReadonly");
        JSchema instance = new JSchema();
        assertFalse(instance.isReadonly());
        instance.setReadonly(true);
        assertTrue(instance.isReadonly());
        instance.setReadonly(null);
        assertFalse(instance.isReadonly());
        instance.setReadonly(false);
        assertFalse(instance.isReadonly());
    }

    /**
     * Test of getContentEncoding method, of class JSchema.
     */
    public void testGetContentEncoding() throws Exception {
        System.out.println("getContentEncoding");
        JSchema instance = new JSchema();
        assertNull(instance.getContentEncoding());
        instance.setContentEncoding("UTF-8");
        assertEquals("UTF-8", instance.getContentEncoding());
    }

    /**
     * Test of setContentEncoding method, of class JSchema.
     */
    public void testSetContentEncoding() throws Exception {
        System.out.println("setContentEncoding");
        JSchema instance = new JSchema();
        assertNull(instance.getContentEncoding());
        instance.setContentEncoding("UTF-8");
        assertEquals("UTF-8", instance.getContentEncoding());
        instance.setContentEncoding("UTF-16");
        assertEquals("UTF-16", instance.getContentEncoding());
        instance.setContentEncoding(null);
        assertNull(instance.getContentEncoding());
        try {
            instance.setContentEncoding("dtjhsrykstrlkdtultdulkdtldtu");
            fail("MUST throw exception if unsupported (by Java!) encoding is set.");
        } catch (JSONSchemaException jsex) {
        }
    }

    /**
     * Test of getPathStart method, of class JSchema.
     */
    public void testGetPathStrart() throws Exception {
        System.out.println("getPathStrart");
        JSchema instance = new JSchema();
        URI uri = new URI("#");
        assertNull(instance.getPathStart());
        instance.setPathStart(uri);
        assertEquals(uri, instance.getPathStart());
        instance.setPathStart(null);
        assertNull(instance.getPathStart());
    }

    /**
     * Test of setPathStart method, of class JSchema.
     */
    public void testSetPathStart() throws Exception {
        System.out.println("setPathStart");
        JSchema instance = new JSchema();
        URI uri = new URI("#");
        assertNull(instance.getPathStart());
        instance.setPathStart(uri);
        assertEquals(uri, instance.getPathStart());
        instance.setPathStart(null);
        assertNull(instance.getPathStart());
    }

    /**
     * Test of getMediaType method, of class JSchema.
     */
    public void testGetMediaType() throws Exception {
        System.out.println("getMediaType");
        JSchema instance = new JSchema();
        assertNull(instance.getMediaType());
        instance.setMediaType("application/json");
        assertEquals("application/json", instance.getMediaType());
    }

    /**
     * Test of setMediaType method, of class JSchema.
     */
    public void testSetMediaType() throws Exception {
        System.out.println("setMediaType");
        JSchema instance = new JSchema();
        assertNull(instance.getMediaType());
        instance.setMediaType("application/json");
        assertEquals("application/json", instance.getMediaType());
        instance.setMediaType(null);
        assertNull(instance.getMediaType());
    }

    /**
     * Test of validate method, of class JSchema.
     */
    public void testValidate() throws Exception {
        System.out.println("validate");
        JSchema instance = new JSchema();
        JSONValidationContext ctx = new JSONValidationContext();
        Object data = null;
        assertTrue(instance.validate(ctx, data, true));
    }

    /**
     * Test of equals method, of class JSchema.
     */
    public void testEquals() throws Exception {
        System.out.println("equals");
        JSchema i1 = new JSchema();
        JSchema i2 = new JSchema();
        assertTrue(i1.equals(i2));
        i1.setName("a");
        assertFalse(i1.equals(i2));
        i2.setName("b");
        assertFalse(i1.equals(i2));
        i2.setName("a");
        assertTrue(i1.equals(i2));
        i1.setType(JSONValueType.NULL);
        assertFalse(i1.equals(i2));
        i2.setType(JSONValueType.NULL);
        assertTrue(i1.equals(i2));
    }

    /**
     * Test of hashCode method, of class JSchema.
     */
    public void testHashCode() {
        System.out.println("hashCode");
    }

    public static class TestClone implements Cloneable {

        String name;

        public TestClone() {
        }

        public TestClone(String name) {
            this.name = name;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            TestClone clone = (TestClone) super.clone();
            clone.name = name;
            return clone;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TestClone other = (TestClone) obj;
            if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0);
            return hash;
        }
    }

    public static class TestCloneWrong implements Cloneable {

        String name;

        public TestCloneWrong() {
        }

        public TestCloneWrong(String name) {
            this.name = name;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Object clone = super.clone();
            if (1 == 1) {
                throw new CloneNotSupportedException("special cloneable with error on clone.");
            }
            return clone;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TestCloneWrong other = (TestCloneWrong) obj;
            if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
            return hash;
        }
    }

    public static class TestIterable implements Iterable {

        protected List list = new ArrayList() {

            {
                add(1);
                add("sd");
            }
        };

        public Iterator iterator() {
            return list.iterator();
        }
    }
}
