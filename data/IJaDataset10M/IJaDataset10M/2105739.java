package com.volantis.xml.pipeline.sax.drivers.web.rules;

import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.xml.pipeline.sax.drivers.web.Script;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;

/**
 * Test cases for {@link ScriptRule}.
 */
public class ScriptRuleTestCase extends DynamicRuleTestAbstract {

    /**
     * Ensure that rule detects missing attributes.
     */
    public void testMissing() throws Exception {
        DynamicElementRule rule = new ScriptRule();
        doStartFailure(rule, "Script tag should have the 'ref' parameter set. " + "Value is: ref='null'");
    }

    /**
     * Ensure that rule works properly.
     */
    public void testNormal() throws Exception {
        final String ref = "ref";
        contextMock.fuzzy.setProperty(Script.class, mockFactory.expectsAny(), Boolean.FALSE).does(new MethodAction() {

            public Object perform(MethodActionEvent event) throws Throwable {
                Script script = (Script) event.getArgument("value", Object.class);
                assertEquals(ref, script.getRef());
                return null;
            }
        });
        DynamicElementRule rule = new ScriptRule();
        addAttribute("ref", ref);
        rule.startElement(dynamicProcessMock, elementName, attributes);
    }
}
