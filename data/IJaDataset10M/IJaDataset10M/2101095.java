package net.sourceforge.signal.runtime.utils;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;
import net.sourceforge.signal.runtime.utils.ValidationUtils;
import org.testng.annotations.Test;

@Test
public class ValidationUtilsTest {

    public void testCheckForNull() {
        ValidationUtils.checkForNull("abc", "name");
        final String varname = "someVariable";
        try {
            ValidationUtils.checkForNull(null, varname);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().indexOf(varname) >= 0);
        }
    }

    public void testCheckMinLength() {
        ValidationUtils.checkMinLength("foo", "name", 3);
        final String varname = "aVariable";
        try {
            ValidationUtils.checkMinLength("data", varname, 5);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().indexOf(varname) >= 0);
        }
    }
}
