package org.databene.benerator.distribution.sequence;

import org.databene.benerator.distribution.sequence.BitReverseLongGenerator;
import org.databene.benerator.test.GeneratorClassTest;
import org.junit.Test;

/**
 * Tests the BitReverseLongGenerator.<br/>
 * <br/>
 * Created: 13.11.2007 15:51:32
 * @author Volker Bergmann
 */
public class BitReverseLongGeneratorTest extends GeneratorClassTest {

    public BitReverseLongGeneratorTest() {
        super(BitReverseLongGenerator.class);
    }

    @Test
    public void testInstantiation() throws Exception {
        createAndInit(0, 10);
    }

    @Test
    public void testBasic() throws Exception {
        expectGeneratedSequence(createAndInit(0, 3), 0L, 2L, 1L, 3L).withCeasedAvailability();
        expectGeneratedSequence(createAndInit(0, 4), 0L, 4L, 2L, 1L, 3L).withCeasedAvailability();
        expectGeneratedSequence(createAndInit(0, 7), 0L, 4L, 2L, 6L, 1L, 5L, 3L, 7L).withCeasedAvailability();
    }

    @Test
    public void testShifted() throws Exception {
        expectGeneratedSequence(createAndInit(1, 4), 1L, 3L, 2L, 4L).withCeasedAvailability();
        expectGeneratedSequence(createAndInit(1, 5), 1L, 5L, 3L, 2L, 4L).withCeasedAvailability();
        expectGeneratedSequence(createAndInit(-1, 6), -1L, 3L, 1L, 5L, 0L, 4L, 2L, 6L).withCeasedAvailability();
        expectGeneratedSequence(createAndInit(-4, -1), -4L, -2L, -3L, -1L).withCeasedAvailability();
    }

    @Test
    public void testScaled() throws Exception {
        expectGeneratedSequence(createAndInit(2, 8, 2), 2L, 6L, 4L, 8L).withCeasedAvailability();
    }

    @Test
    public void testScaledAndShifted() throws Exception {
        expectGeneratedSequence(createAndInit(1, 7, 2), 1L, 5L, 3L, 7L).withCeasedAvailability();
    }

    @Test
    public void testReset() throws Exception {
        expectGeneratedSequence(createAndInit(1, 4), 1L, 3L, 2L).withContinuedAvailability();
    }

    private BitReverseLongGenerator createAndInit(long min, long max) {
        BitReverseLongGenerator gen = new BitReverseLongGenerator(min, max);
        gen.init(context);
        return gen;
    }

    private BitReverseLongGenerator createAndInit(long min, long max, long granularity) {
        BitReverseLongGenerator gen = new BitReverseLongGenerator(min, max, granularity);
        gen.init(context);
        return gen;
    }
}
