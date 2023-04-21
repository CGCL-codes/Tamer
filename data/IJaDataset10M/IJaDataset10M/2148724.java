package org.qtitools.qti.node.expression.operator;

import java.util.Arrays;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.qtitools.qti.node.expression.ExpressionAcceptTest;
import org.qtitools.qti.value.BooleanValue;
import org.qtitools.qti.value.NullValue;

/**
 * Test of <code>Lt</code> expression.
 *
 * @see org.qtitools.qti.node.expression.operator.Lt
 */
@RunWith(Parameterized.class)
public class LtAcceptTest extends ExpressionAcceptTest {

    /**
	 * Creates test data for this test.
	 *
	 * @return test data for this test
	 */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { "<lt>" + "<null/>" + "<null/>" + "</lt>", null }, { "<lt>" + "<baseValue baseType='integer'>1</baseValue>" + "<null/>" + "</lt>", null }, { "<lt>" + "<null/>" + "<baseValue baseType='integer'>1</baseValue>" + "</lt>", null }, { "<lt>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "</lt>", true }, { "<lt>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='integer'>1</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='integer'>1</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>-7</baseValue>" + "<baseValue baseType='integer'>-5</baseValue>" + "</lt>", true }, { "<lt>" + "<baseValue baseType='integer'>-3</baseValue>" + "<baseValue baseType='integer'>-3</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>-4</baseValue>" + "<baseValue baseType='integer'>-5</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>-4</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "</lt>", true }, { "<lt>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='integer'>-4</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>0</baseValue>" + "<baseValue baseType='integer'>0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>+0</baseValue>" + "<baseValue baseType='integer'>+0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>+0</baseValue>" + "<baseValue baseType='integer'>-0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>-0</baseValue>" + "<baseValue baseType='integer'>+0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>-0</baseValue>" + "<baseValue baseType='integer'>-0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>1.5</baseValue>" + "<baseValue baseType='float'>2.3</baseValue>" + "</lt>", true }, { "<lt>" + "<baseValue baseType='float'>1.4</baseValue>" + "<baseValue baseType='float'>1.4</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>2.8</baseValue>" + "<baseValue baseType='float'>1.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>-7.4</baseValue>" + "<baseValue baseType='float'>-5.6</baseValue>" + "</lt>", true }, { "<lt>" + "<baseValue baseType='float'>-3.3</baseValue>" + "<baseValue baseType='float'>-3.3</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>-4.1</baseValue>" + "<baseValue baseType='float'>-5.9</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>-4.6</baseValue>" + "<baseValue baseType='float'>2.7</baseValue>" + "</lt>", true }, { "<lt>" + "<baseValue baseType='float'>2.6</baseValue>" + "<baseValue baseType='float'>-4.3</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>0.0</baseValue>" + "<baseValue baseType='float'>0.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>+0.0</baseValue>" + "<baseValue baseType='float'>+0.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>+0.0</baseValue>" + "<baseValue baseType='float'>-0.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>-0.0</baseValue>" + "<baseValue baseType='float'>+0.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>-0.0</baseValue>" + "<baseValue baseType='float'>-0.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='float'>2.3</baseValue>" + "</lt>", true }, { "<lt>" + "<baseValue baseType='float'>1.5</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "</lt>", true }, { "<lt>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='float'>1.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='float'>1.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>2.8</baseValue>" + "<baseValue baseType='integer'>1</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>-7</baseValue>" + "<baseValue baseType='float'>-5.6</baseValue>" + "</lt>", true }, { "<lt>" + "<baseValue baseType='float'>-7.4</baseValue>" + "<baseValue baseType='integer'>-5</baseValue>" + "</lt>", true }, { "<lt>" + "<baseValue baseType='integer'>-3</baseValue>" + "<baseValue baseType='float'>-3.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>-4</baseValue>" + "<baseValue baseType='float'>-5.9</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>-4.1</baseValue>" + "<baseValue baseType='integer'>-5</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>-4</baseValue>" + "<baseValue baseType='float'>2.7</baseValue>" + "</lt>", true }, { "<lt>" + "<baseValue baseType='float'>-4.6</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "</lt>", true }, { "<lt>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='float'>-4.3</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>2.6</baseValue>" + "<baseValue baseType='integer'>-4</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>0</baseValue>" + "<baseValue baseType='float'>0.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>+0</baseValue>" + "<baseValue baseType='float'>+0.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>+0</baseValue>" + "<baseValue baseType='float'>-0.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>+0.0</baseValue>" + "<baseValue baseType='integer'>-0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>-0</baseValue>" + "<baseValue baseType='float'>+0.0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='float'>-0.0</baseValue>" + "<baseValue baseType='integer'>+0</baseValue>" + "</lt>", false }, { "<lt>" + "<baseValue baseType='integer'>-0</baseValue>" + "<baseValue baseType='float'>-0.0</baseValue>" + "</lt>", false } });
    }

    /**
	 * Constructs <code>Lt</code> expression test.
	 *
	 * @param xml xml data used for creation tested expression
	 * @param expectedValue expected evaluated value
	 */
    public LtAcceptTest(String xml, Boolean expectedValue) {
        super(xml, (expectedValue != null) ? new BooleanValue(expectedValue) : new NullValue());
    }
}
