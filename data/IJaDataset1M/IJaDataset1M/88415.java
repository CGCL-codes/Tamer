package org.springframework.binding.valuemodel;

import org.springframework.binding.Undefined;
import org.springframework.binding.convert.AbstractRichConverter;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.valuemodel.DefaultValueChangeDetector;
import org.springframework.binding.valuemodel.TypeConverter;
import org.springframework.binding.valuemodel.ValueHolder;
import org.springframework.binding.valuemodel.ValueModel;
import org.springframework.richclient.test.SpringRichTestCase;

/**
 * Tests class {@link TypeConverter}.
 * 
 * @author Oliver Hutchison
 */
public class TypeConverterTests extends SpringRichTestCase {

    private ValueModel vm = new ValueHolder(new Integer(0));

    private TypeConverter tc;

    public void doSetUp() {
        tc = new TypeConverter(vm, new ConversionExecutor(new TestConverter(), String.class), new ConversionExecutor(new TestConverter(), Integer.class));
        tc.setValueChangeDetector(new DefaultValueChangeDetector());
    }

    public void testConvertsTo() {
        vm.setValue(new Integer(1));
        assertEquals("1", tc.getValue());
        assertEquals(new Integer(1), vm.getValue());
    }

    public void testConvertsFrom() {
        tc.setValue("1");
        assertEquals("1", tc.getValue());
        assertEquals(new Integer(1), vm.getValue());
    }

    public void testNoValuePassesThrough() {
        vm.setValue(Undefined.VALUE);
        assertEquals(Undefined.VALUE, tc.getValue());
        assertEquals(Undefined.VALUE, vm.getValue());
    }

    private class TestConverter extends AbstractRichConverter {

        protected Object doConvert(Object value, Class targetClass) throws Exception {
            if (targetClass == String.class) {
                return ((Integer) value).toString();
            } else {
                return Integer.valueOf((String) value);
            }
        }

        public Class[] getSourceClasses() {
            return new Class[] { String.class };
        }

        public Class[] getTargetClasses() {
            return new Class[] { Integer.class };
        }
    }
}
