package net.sourceforge.align.filter.aligner.align.hmm.util.calculator;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class CompositeCalculatorTest {

    @Test
    public void calculate() {
        List<Calculator> calculatorList = Arrays.asList(new Calculator[] { new CalculatorMock(0.5f), new CalculatorMock(0.25f) });
        Calculator calculator = new CompositeCalculator(calculatorList);
        assertEquals(0.75f, calculator.calculateScore(null, null), 0.75f);
    }
}
