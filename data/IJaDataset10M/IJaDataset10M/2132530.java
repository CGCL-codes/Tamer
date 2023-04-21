package de.kumpe.hadooptimizer.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Test;
import de.kumpe.hadooptimizer.EsIndividual;
import de.kumpe.hadooptimizer.Evaluator;
import de.kumpe.hadooptimizer.NeedsRandomGenerator;

public class EsEvaluatorWrapperTest {

    @Test
    @SuppressWarnings("unchecked")
    public final void testEsEvaluatorWrapper() {
        try {
            new EsEvaluatorWrapper(null);
            fail("expected NullPointerException not thrown");
        } catch (final NullPointerException e) {
        }
        new EsEvaluatorWrapper(mock(Evaluator.class));
    }

    @Test
    public final void testSetRandomGenerator() {
        @SuppressWarnings("unchecked") final Evaluator<double[]> evaluator = mock(Evaluator.class, withSettings().extraInterfaces(NeedsRandomGenerator.class));
        final EsEvaluatorWrapper wrapper = new EsEvaluatorWrapper(evaluator);
        final RandomGenerator randomGenerator = mock(RandomGenerator.class);
        wrapper.setRandomGenerator(randomGenerator);
        ((NeedsRandomGenerator) verify(evaluator)).setRandomGenerator(same(randomGenerator));
    }

    @Test
    public final void testEvaluate() {
        @SuppressWarnings("unchecked") final Evaluator<double[]> evaluator = mock(Evaluator.class, withSettings().extraInterfaces(NeedsRandomGenerator.class));
        when(evaluator.evaluate((double[]) any())).thenReturn(123d);
        final EsEvaluatorWrapper wrapper = new EsEvaluatorWrapper(evaluator);
        final EsIndividual individual = new EsIndividual(new double[1]);
        assertEquals(123d, wrapper.evaluate(individual), 0);
    }
}
