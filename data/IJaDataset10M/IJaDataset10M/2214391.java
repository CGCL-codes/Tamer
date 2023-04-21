package com.threerings.antidote.property;

import org.junit.Test;
import com.threerings.antidote.RequiresValidationException;
import com.threerings.antidote.TestEnum;
import com.threerings.antidote.field.TestBaseField;
import com.threerings.antidote.property.EnumProperty;
import com.threerings.antidote.property.InvalidEnumViolation;
import static com.threerings.antidote.ValidationTestHelper.assertNoViolations;
import static com.threerings.antidote.ValidationTestHelper.assertOneViolation;
import static org.junit.Assert.assertEquals;

public class EnumPropertyTest {

    @Test
    public void testEnumProperty() {
        EnumProperty<TestEnum> property = new EnumProperty<TestEnum>("testprop", new TestBaseField(), TestEnum.class);
        property.setValue("TESTFIELD");
        assertNoViolations(property);
        assertEquals(TestEnum.TESTFIELD, property.getValue());
        property = new EnumProperty<TestEnum>("testprop", new TestBaseField(), TestEnum.class);
        property.setValue("testfield");
        assertNoViolations(property);
        assertEquals(TestEnum.TESTFIELD, property.getValue());
        property = new EnumProperty<TestEnum>("testprop", new TestBaseField(), TestEnum.class);
        property.setValue("TestField");
        assertNoViolations(property);
        assertEquals(TestEnum.TESTFIELD, property.getValue());
        property = new EnumProperty<TestEnum>("testprop", new TestBaseField(), TestEnum.class);
        property.setValue("bogus-field");
        assertOneViolation(property, InvalidEnumViolation.class);
    }

    @Test(expected = RequiresValidationException.class)
    public void testRequiresValidationInvalidValue() {
        final EnumProperty<TestEnum> property = new EnumProperty<TestEnum>("testprop", new TestBaseField(), TestEnum.class);
        property.setValue("bogus-field");
        property.getValue();
    }
}
