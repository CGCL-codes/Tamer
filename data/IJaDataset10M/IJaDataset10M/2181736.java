package org.qtitools.qti.node.expression.operator;

import java.util.Arrays;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.qtitools.qti.exception.QTIBaseTypeException;
import org.qtitools.qti.exception.QTICardinalityException;
import org.qtitools.qti.exception.QTIRuntimeException;
import org.qtitools.qti.node.expression.ExpressionRefuseTest;

/**
 * Test of <code>Truncate</code> expression.
 *
 * @see org.qtitools.qti.node.expression.operator.Truncate
 */
@RunWith(Parameterized.class)
public class TruncateRefuseTest extends ExpressionRefuseTest {

    /**
	 * Creates test data for this test.
	 *
	 * @return test data for this test
	 */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { "<truncate>" + "<multiple>" + "<baseValue baseType='float'>1</baseValue>" + "</multiple>" + "</truncate>", QTICardinalityException.class }, { "<truncate>" + "<ordered>" + "<baseValue baseType='float'>1</baseValue>" + "</ordered>" + "</truncate>", QTICardinalityException.class }, { "<truncate>" + "<recordEx identifiers='key_1'>" + "<baseValue baseType='float'>1</baseValue>" + "</recordEx>" + "</truncate>", QTICardinalityException.class }, { "<truncate>" + "<baseValue baseType='identifier'>identifier</baseValue>" + "</truncate>", QTIBaseTypeException.class }, { "<truncate>" + "<baseValue baseType='boolean'>1</baseValue>" + "</truncate>", QTIBaseTypeException.class }, { "<truncate>" + "<baseValue baseType='integer'>1</baseValue>" + "</truncate>", QTIBaseTypeException.class }, { "<truncate>" + "<baseValue baseType='string'>1</baseValue>" + "</truncate>", QTIBaseTypeException.class }, { "<truncate>" + "<baseValue baseType='point'>1 1</baseValue>" + "</truncate>", QTIBaseTypeException.class }, { "<truncate>" + "<baseValue baseType='pair'>identifier_1 identifier_2</baseValue>" + "</truncate>", QTIBaseTypeException.class }, { "<truncate>" + "<baseValue baseType='directedPair'>identifier_1 identifier_2</baseValue>" + "</truncate>", QTIBaseTypeException.class }, { "<truncate>" + "<baseValue baseType='duration'>1</baseValue>" + "</truncate>", QTIBaseTypeException.class }, { "<truncate>" + "<baseValue baseType='file'>file</baseValue>" + "</truncate>", QTIBaseTypeException.class }, { "<truncate>" + "<baseValue baseType='uri'>uri</baseValue>" + "</truncate>", QTIBaseTypeException.class } });
    }

    /**
	 * Constructs <code>Truncate</code> expression test.
	 *
	 * @param xml xml data used for creation tested expression
	 * @param expectedException expected exception during evaluation of tested expression
	 */
    public TruncateRefuseTest(String xml, Class<? extends QTIRuntimeException> expectedException) {
        super(xml, expectedException);
    }
}
