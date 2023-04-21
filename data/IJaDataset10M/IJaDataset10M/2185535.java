package net.sf.julie.library;

import org.junit.Assert;
import org.junit.Test;

public class AssvTest extends AbstractLibraryTest {

    @Test
    public void simple() {
        Assert.assertEquals("(5 7)", evalToString("(assv 5 '((2 3) (5 7) (11 13)))"));
    }
}
