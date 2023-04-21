package org.peaseplate.designator;

import org.peaseplate.util.*;
import org.testng.annotations.*;

@Test
public class IfDesignatorTest extends AbstractTemplateTestCase {

    @Test
    public void automatic() throws Exception {
        test("org/peaseplate/designator/ifDesignatorTest.xml", null);
    }
}
