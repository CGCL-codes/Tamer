package net.sourceforge.purrpackage.test.demo.ideal;

import org.testng.Assert;
import org.testng.annotations.Test;

public class IdealExampleOneTest extends Assert {

    @Test
    public void testBeforeAndAfter() {
        IdealExample x = new IdealExample("boo");
        x.before();
        assertTrue(x.after().length() > 3);
    }

    @Test
    public void testBrancherLastFalse() {
        IdealExample.Brancher x = new IdealExample("foo").new Brancher();
        x.branch(true, false);
        x.branch(false, false);
    }
}
