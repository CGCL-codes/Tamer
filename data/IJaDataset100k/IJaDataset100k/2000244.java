package org.qtitools.qti.value;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.qtitools.qti.exception.QTIParseException;

/**
 * Tests <code>IntegerValue</code> implementation of parsing value from <code>String</code>.
 * <p>
 * This test contains only invalid <code>String</code> representations.
 *
 * @see org.qtitools.qti.value.IntegerValue
 */
@RunWith(Parameterized.class)
public class IntegerValueRefuseTest {

    /**
	 * Creates test data for this test.
	 *
	 * @return test data for this test
	 */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { null }, { "" }, { " " }, { "+" }, { "-" }, { "2147483648" }, { "+2147483648" }, { "-2147483649" }, { "1.0" }, { "++1" }, { "--1" }, { "+-1" }, { "-+1" } });
    }

    private String string;

    /**
	 * Constructs this test.
	 *
	 * @param string parsed <code>String</code>
	 */
    public IntegerValueRefuseTest(String string) {
        this.string = string;
    }

    /**
	 * Tests parsing value from <code>String</code> representation.
	 *
	 * @throws QTIParseException if test was successful
	 */
    @Test(expected = QTIParseException.class)
    public void testParseInteger() throws QTIParseException {
        IntegerValue.parseInteger(string);
    }
}
