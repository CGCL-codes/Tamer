package de.kumpe.hadooptimizer.impl;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.junit.Test;
import de.kumpe.hadooptimizer.EvaluationResult;

public class DuplicatingRecombinerTest {

    @Test
    public final void testDuplicatingPairingSelector() {
        try {
            new DuplicatingRecombiner<Object>(0);
            fail("expected IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException e) {
        }
        new DuplicatingRecombiner<Object>(1);
    }

    @Test
    public final void testRecombine() {
        final DuplicatingRecombiner<Object> pairingSelector = new DuplicatingRecombiner<Object>(10);
        pairingSelector.setRandomGenerator(new JDKRandomGenerator());
        final EvaluationResult<Object> evaluationResult = new EvaluationResult<Object>(new Object(), 0);
        @SuppressWarnings("unchecked") final Collection<EvaluationResult<Object>> input = Arrays.asList(evaluationResult, evaluationResult);
        final Collection<Object> output = pairingSelector.recombine(input);
        assertEquals(10, output.size());
    }
}
