package com.riversoforion.acheron.patterns;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import com.riversoforion.acheron.junit.category.FastTest;

public class MappedEnumHelperTest {

    @Test
    @Category(FastTest.class)
    public void testStringEnum() {
        assertThat(StringMappedEnum.fromStringValue("One"), is(StringMappedEnum.VAL_ONE));
        assertThat(StringMappedEnum.fromStringValue("Two"), is(StringMappedEnum.VAL_TWO));
        assertThat(StringMappedEnum.fromStringValue("Three"), is(StringMappedEnum.VAL_THREE));
        assertNull(StringMappedEnum.fromStringValue("Four"));
        assertThat(StringMappedEnum.VAL_ONE.toStringValue(), equalTo("One"));
        assertThat(StringMappedEnum.VAL_TWO.toStringValue(), equalTo("Two"));
        assertThat(StringMappedEnum.VAL_THREE.toStringValue(), equalTo("Three"));
    }

    @Test
    @Category(FastTest.class)
    public void testIntEnum() {
        assertThat(IntegerMappedEnum.fromIntValue(1), is(IntegerMappedEnum.VAL_ONE));
        assertThat(IntegerMappedEnum.fromIntValue(2), is(IntegerMappedEnum.VAL_TWO));
        assertThat(IntegerMappedEnum.fromIntValue(3), is(IntegerMappedEnum.VAL_THREE));
        assertNull(IntegerMappedEnum.fromIntValue(4));
        assertThat(IntegerMappedEnum.VAL_ONE.toIntValue(), is(1));
        assertThat(IntegerMappedEnum.VAL_TWO.toIntValue(), is(2));
        assertThat(IntegerMappedEnum.VAL_THREE.toIntValue(), is(3));
    }

    @Test(expected = IllegalArgumentException.class)
    @Category(FastTest.class)
    public void testNotAMappedEnum() {
        MappedEnumHelper.forEnum(NotAMappedEnum.class);
    }

    private static enum StringMappedEnum implements MappedEnum<String> {

        VAL_ONE("One"), VAL_TWO("Two"), VAL_THREE("Three");

        private static MappedEnumHelper<String, StringMappedEnum> helper = MappedEnumHelper.forEnum(StringMappedEnum.class);

        String strVal;

        private StringMappedEnum(String val) {
            this.strVal = val;
        }

        @Override
        public String getMappedValue() {
            return this.strVal;
        }

        public String toStringValue() {
            return helper.toMappedValue(this);
        }

        public static StringMappedEnum fromStringValue(String strVal) {
            return helper.fromMappedValue(strVal);
        }
    }

    private static enum IntegerMappedEnum implements MappedEnum<Integer> {

        VAL_ONE(1), VAL_TWO(2), VAL_THREE(3);

        private static MappedEnumHelper<Integer, IntegerMappedEnum> helper = MappedEnumHelper.forEnum(IntegerMappedEnum.class);

        Integer intVal;

        private IntegerMappedEnum(int val) {
            this.intVal = val;
        }

        @Override
        public Integer getMappedValue() {
            return this.intVal;
        }

        public Integer toIntValue() {
            return helper.toMappedValue(this);
        }

        public static IntegerMappedEnum fromIntValue(int intVal) {
            return helper.fromMappedValue(intVal);
        }
    }

    private static enum NotAMappedEnum {

        VAL_ONE, VAL_TWO, VAL_THREE
    }
}
