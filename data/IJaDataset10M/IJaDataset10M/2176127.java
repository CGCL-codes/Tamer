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
 * Test of <code>Member</code> expression.
 *
 * @see org.qtitools.qti.node.expression.operator.Member
 */
@RunWith(Parameterized.class)
public class MemberAcceptTest extends ExpressionAcceptTest {

    /**
	 * Creates test data for this test.
	 *
	 * @return test data for this test
	 */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { "<member>" + "<baseValue baseType='identifier'>identifier</baseValue>" + "<multiple>" + "<baseValue baseType='identifier'>identifier</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='identifier'>identifier</baseValue>" + "<ordered>" + "<baseValue baseType='identifier'>identifier</baseValue>" + "</ordered>" + "</member>", true }, { "<member>" + "<baseValue baseType='boolean'>true</baseValue>" + "<multiple>" + "<baseValue baseType='boolean'>true</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='boolean'>false</baseValue>" + "<multiple>" + "<baseValue baseType='boolean'>false</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='boolean'>true</baseValue>" + "<ordered>" + "<baseValue baseType='boolean'>true</baseValue>" + "</ordered>" + "</member>", true }, { "<member>" + "<baseValue baseType='boolean'>false</baseValue>" + "<ordered>" + "<baseValue baseType='boolean'>false</baseValue>" + "</ordered>" + "</member>", true }, { "<member>" + "<baseValue baseType='integer'>0</baseValue>" + "<multiple>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='integer'>3</baseValue>" + "</multiple>" + "</member>", false }, { "<member>" + "<baseValue baseType='integer'>1</baseValue>" + "<multiple>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='integer'>3</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='integer'>2</baseValue>" + "<multiple>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='integer'>3</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='integer'>3</baseValue>" + "<multiple>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='integer'>3</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='integer'>4</baseValue>" + "<multiple>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='integer'>3</baseValue>" + "</multiple>" + "</member>", false }, { "<member>" + "<baseValue baseType='integer'>0</baseValue>" + "<ordered>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='integer'>3</baseValue>" + "</ordered>" + "</member>", false }, { "<member>" + "<baseValue baseType='integer'>1</baseValue>" + "<ordered>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='integer'>3</baseValue>" + "</ordered>" + "</member>", true }, { "<member>" + "<baseValue baseType='integer'>2</baseValue>" + "<ordered>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='integer'>3</baseValue>" + "</ordered>" + "</member>", true }, { "<member>" + "<baseValue baseType='integer'>3</baseValue>" + "<ordered>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='integer'>3</baseValue>" + "</ordered>" + "</member>", true }, { "<member>" + "<baseValue baseType='integer'>4</baseValue>" + "<ordered>" + "<baseValue baseType='integer'>1</baseValue>" + "<baseValue baseType='integer'>2</baseValue>" + "<baseValue baseType='integer'>3</baseValue>" + "</ordered>" + "</member>", false }, { "<member>" + "<baseValue baseType='float'>1.2</baseValue>" + "<multiple>" + "<baseValue baseType='float'>1.2</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='float'>1.2</baseValue>" + "<ordered>" + "<baseValue baseType='float'>1.2</baseValue>" + "</ordered>" + "</member>", true }, { "<member>" + "<baseValue baseType='string'>string</baseValue>" + "<multiple>" + "<baseValue baseType='string'>string</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='string'>string</baseValue>" + "<ordered>" + "<baseValue baseType='string'>string</baseValue>" + "</ordered>" + "</member>", true }, { "<member>" + "<baseValue baseType='point'>1 1</baseValue>" + "<multiple>" + "<baseValue baseType='point'>1 1</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='point'>1 1</baseValue>" + "<ordered>" + "<baseValue baseType='point'>1 1</baseValue>" + "</ordered>" + "</member>", true }, { "<member>" + "<baseValue baseType='pair'>identifier_1 identifier_2</baseValue>" + "<multiple>" + "<baseValue baseType='pair'>identifier_1 identifier_2</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='pair'>identifier_1 identifier_2</baseValue>" + "<ordered>" + "<baseValue baseType='pair'>identifier_1 identifier_2</baseValue>" + "</ordered>" + "</member>", true }, { "<member>" + "<baseValue baseType='directedPair'>identifier_1 identifier_2</baseValue>" + "<multiple>" + "<baseValue baseType='directedPair'>identifier_1 identifier_2</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='directedPair'>identifier_1 identifier_2</baseValue>" + "<ordered>" + "<baseValue baseType='directedPair'>identifier_1 identifier_2</baseValue>" + "</ordered>" + "</member>", true }, { "<member>" + "<baseValue baseType='file'>file</baseValue>" + "<multiple>" + "<baseValue baseType='file'>file</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='file'>file</baseValue>" + "<ordered>" + "<baseValue baseType='file'>file</baseValue>" + "</ordered>" + "</member>", true }, { "<member>" + "<baseValue baseType='uri'>uri</baseValue>" + "<multiple>" + "<baseValue baseType='uri'>uri</baseValue>" + "</multiple>" + "</member>", true }, { "<member>" + "<baseValue baseType='uri'>uri</baseValue>" + "<ordered>" + "<baseValue baseType='uri'>uri</baseValue>" + "</ordered>" + "</member>", true } });
    }

    /**
	 * Constructs <code>Member</code> expression test.
	 *
	 * @param xml xml data used for creation tested expression
	 * @param expectedValue expected evaluated value
	 */
    public MemberAcceptTest(String xml, Boolean expectedValue) {
        super(xml, (expectedValue != null) ? new BooleanValue(expectedValue) : new NullValue());
    }
}
