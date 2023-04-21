package org.databene.benerator.distribution.sequence;

import org.databene.benerator.Generator;
import org.databene.benerator.SequenceTestGenerator;
import org.databene.benerator.distribution.Sequence;
import org.databene.benerator.test.GeneratorTest;
import org.junit.Test;

/**
 * Tests the {@link HeadSequence}.<br/><br/>
 * Created: 25.07.2010 11:19:57
 * @since 0.6.3
 * @author Volker Bergmann
 */
public class HeadSequenceTest extends GeneratorTest {

    @Test
    public void testLongGenerator() throws Exception {
        expectGeneratedSequence(longGenerator(1), 0L).withCeasedAvailability();
        expectGeneratedSequence(longGenerator(2), 0L, 1L).withCeasedAvailability();
    }

    @Test
    public void testDoubleGenerator() throws Exception {
        expectGeneratedSequence(doubleGenerator(1), 0.).withCeasedAvailability();
        expectGeneratedSequence(doubleGenerator(2), 0., 1.).withCeasedAvailability();
    }

    @Test
    public void testApply() throws Exception {
        expectGeneratedSequence(charGenerator(1), 'A').withCeasedAvailability();
        expectGeneratedSequence(charGenerator(2), 'A', 'B').withCeasedAvailability();
    }

    private Generator<Long> longGenerator(long n) {
        Sequence sequence = new HeadSequence(n);
        return initialize(sequence.createNumberGenerator(Long.class, 0L, 1000L, 1L, false));
    }

    private Generator<Double> doubleGenerator(long n) {
        Sequence sequence = new HeadSequence(n);
        return initialize(sequence.createNumberGenerator(Double.class, 0., 1000., 1., false));
    }

    private Generator<Character> charGenerator(long n) {
        Sequence sequence = new HeadSequence(n);
        Generator<Character> source = new SequenceTestGenerator<Character>('A', 'B', 'C', 'D');
        return initialize(sequence.applyTo(source, false));
    }
}
