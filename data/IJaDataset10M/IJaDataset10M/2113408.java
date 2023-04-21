package net.sf.lightbound.tests.junit;

import org.junit.Test;

public class HtmlAnnotationTest extends BasicTest {

    @Test
    public void doTest() throws Exception {
        test(null, "tests/htmlAnnotationTest.html", null);
    }
}
