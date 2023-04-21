package org.databene.benerator.primitive;

import org.databene.benerator.Generator;
import org.databene.benerator.test.GeneratorClassTest;
import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the HexUUIDGenerator.<br/>
 * <br/>
 * Created: 15.11.2007 11:00:42
 * @author Volker Bergmann
 */
public class HibUUIDGeneratorTest extends GeneratorClassTest {

    public HibUUIDGeneratorTest() {
        super(HibUUIDGenerator.class);
    }

    @Test
    public void testWithoutSeparator() {
        HibUUIDGenerator generator = new HibUUIDGenerator();
        generator.init(context);
        for (int i = 0; i < 5; i++) {
            String id = generator.generate();
            assertEquals(32, id.length());
        }
    }

    @Test
    public void testMinusSeparator() {
        HibUUIDGenerator generator = new HibUUIDGenerator("-");
        generator.init(context);
        for (int i = 0; i < 5; i++) {
            String id = generator.generate();
            assertEquals(36, id.length());
            assertEquals('-', id.charAt(8));
            assertEquals('-', id.charAt(17));
            assertEquals('-', id.charAt(22));
            assertEquals('-', id.charAt(31));
            logger.debug(id);
        }
    }

    @Test
    public void testUniqueness() {
        Generator<String> generator = new HibUUIDGenerator();
        generator.init(context);
        expectUniqueGenerations(generator, 100);
    }
}
