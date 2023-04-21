package org.infoeng.aki.test.ofbiz;

import org.ofbiz.base.start.*;

public class OFBizActivatorTest extends TestCase {

    public OFBizActivatorTest() {
    }

    public void testOne() {
        String[] args = { "-startup" };
        Start st = new Start(args, false);
    }
}
