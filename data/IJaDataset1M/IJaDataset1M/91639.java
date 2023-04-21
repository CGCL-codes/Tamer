package org.databene.benerator.wrapper;

import org.databene.benerator.ConstantTestGenerator;
import org.databene.benerator.test.GeneratorTest;
import org.databene.benerator.util.GeneratorUtil;
import org.databene.commons.ConversionException;
import org.databene.commons.converter.ThreadSafeConverter;
import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link ConvertingGenerator}.<br/><br/>
 * Created: 11.10.2006 23:12:21
 * @since 0.1
 * @author Volker Bergmann
 */
public class ConvertingGeneratorTest extends GeneratorTest {

    @Test
    public void test() {
        ConstantTestGenerator<Integer> source = new ConstantTestGenerator<Integer>(1);
        TestConverter converter = new TestConverter();
        ConvertingGenerator<Integer, String> generator = new ConvertingGenerator<Integer, String>(source, converter);
        assertEquals("constructor", source.getLastMethodCall());
        assertEquals("1", GeneratorUtil.generateNonNull(generator));
        assertEquals("1", GeneratorUtil.generateNonNull(generator));
        assertTrue(generator.getSource() == source);
        generator.reset();
        assertEquals("reset", source.getLastMethodCall());
        generator.close();
        assertEquals("close", source.getLastMethodCall());
    }

    private static class TestConverter extends ThreadSafeConverter<Integer, String> {

        public TestConverter() {
            super(Integer.class, String.class);
        }

        public String convert(Integer sourceValue) throws ConversionException {
            return String.valueOf(sourceValue);
        }
    }
}
