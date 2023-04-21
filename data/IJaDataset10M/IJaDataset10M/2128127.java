package org.herasaf.xacml.core.function.impl.nonNumericComparisonFunction;

import static org.testng.Assert.assertEquals;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.StringGreaterThanFunction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestStringGreaterThan {

    private Function ia;

    @DataProvider(name = "args")
    public Object[][] createArgs() {
        return new Object[][] { new Object[] { "1", "1", false }, new Object[] { "Hallo", "Hello", false }, new Object[] { "Ha", "Hello", false }, new Object[] { "He", "Hallo", true }, new Object[] { "Test", "Auto", true }, new Object[] { "Hello", "Ha", true }, new Object[] { "He", "Hel", false } };
    }

    @BeforeMethod
    public void init() {
        ia = new StringGreaterThanFunction();
    }

    @Test(dataProvider = "args")
    public void testArgs(String i1, String i2, Boolean result) throws Exception {
        assertEquals(ia.handle(i1, i2), result);
    }

    @Test
    public void testID() {
        assertEquals(ia.toString(), "urn:oasis:names:tc:xacml:1.0:function:string-greater-than");
    }
}
