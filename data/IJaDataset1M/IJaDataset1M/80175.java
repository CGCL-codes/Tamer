package org.wuhsin.application.commandline;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.wuhsin.canon.commandline.Argument;

/**
 * @author jmochel
 *
 */
public class ArgumentTestCase {

    /**
     * Test method for {@link org.wuhsin.canon.commandline.Argument#Argument(java.lang.String, java.lang.String)}.
     * This is a very basic test case but it could become more complicated over time depending on I18N
     * changes.
     */
    @Test
    public void testArgument() {
        final String nomen = "filename";
        final String detail = "shmoo.txt";
        final Argument arg = new Argument(nomen, detail);
        Assert.assertTrue(arg.hasDetail());
        Assert.assertEquals(arg.getNomen(), nomen);
        Assert.assertEquals(arg.getDetail(), detail);
    }
}
