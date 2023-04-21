package com.volantis.xml.pipeline.sax.drivers.web.rules;

import com.volantis.shared.net.proxy.ProxyManager;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;

/**
 * Test cases for {@link ProxyRule}.
 */
public class ProxyRuleTestCase extends DynamicRuleTestAbstract {

    /**
     * Ensure that rule detects missing attributes.
     */
    public void testMissing() throws Exception {
        DynamicElementRule rule = new ProxyRule();
        doStartFailure(rule, "Proxy tag should have the 'ref' parameter set. " + "Value is: ref='null'");
    }

    /**
     * Ensure that rule works properly.
     */
    public void testNormal() throws Exception {
        final String ref = "ref";
        contextMock.expects.setProperty(ProxyManager.class, ref, false);
        DynamicElementRule rule = new ProxyRule();
        addAttribute("ref", ref);
        rule.startElement(dynamicProcessMock, elementName, attributes);
    }
}
