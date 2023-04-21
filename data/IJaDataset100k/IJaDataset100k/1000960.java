package be.lassi.domain;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;
import be.lassi.base.DirtyStub;
import be.lassi.support.DirtyTest;
import be.lassi.support.ObjectBuilder;
import be.lassi.support.ObjectTest;

/**
 * Tests class <code>PresetValueDefinition</code>.
 */
public class PresetValueDefinitionTestCase {

    @Test
    public void testGetValues() {
        doTestGetValues("10", 10);
        doTestGetValues("10,20", 10, 20);
        doTestGetValues("10", 0, 10);
        doTestGetValues("255", 0, 255);
        doTestGetValues("256", 1, 0);
        doTestGetValues("257", 1, 1);
        doTestGetValues("257", 1, 1);
        doTestGetValues("32768", 128, 0);
        doTestGetValues("65535", 255, 255);
    }

    @Test
    public void dirty() {
        DirtyTest test = new DirtyTest();
        AttributeDefinition attributeDefinition = new AttributeDefinition();
        PresetValueDefinition value = new PresetValueDefinition(test.getDirty(), attributeDefinition, "1");
        test.notDirty();
        value.setValue("10");
        test.dirty();
    }

    @Test
    public void equals() {
        PresetValueDefinition value1 = newPresetValue("name", "1");
        PresetValueDefinition value2 = newPresetValue("name", "1");
        assertTrue(value1.equals(value2));
        value1.setValue("2");
        assertFalse(value1.equals(value2));
        value2.setValue("2");
        assertTrue(value1.equals(value2));
    }

    @Test
    public void object() {
        ObjectBuilder b = new ObjectBuilder() {

            public Object getObject1() {
                return newPresetValue("name1", "1");
            }

            public Object getObject2() {
                return newPresetValue("name2", "1");
            }
        };
        ObjectTest.test(b);
    }

    private void doTestGetValues(final String presetValues, final int... expectedValues) {
        AttributeDefinition attributeDefinition = new AttributeDefinition("name", "");
        PresetValueDefinition definition = new PresetValueDefinition(attributeDefinition, presetValues);
        int[] values = definition.getValues(expectedValues.length);
        assertEquals(values.length, expectedValues.length);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], expectedValues[i], "values[" + i + "]");
        }
    }

    private PresetValueDefinition newPresetValue(final String name, final String value) {
        AttributeDefinition attributeDefinition = new AttributeDefinition(name, "");
        PresetValueDefinition presetValue = new PresetValueDefinition(new DirtyStub(), attributeDefinition, value);
        return presetValue;
    }
}
