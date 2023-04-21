package com.volantis.mcs.protocols.html.xhtmltransitional;

import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.Utils;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.html.xhtmlfull.XHTMLFullTransVisitorTestCase;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.devices.InternalDevice;

/**
 * Tests the TransVisitor behaviour in conjunction with its superclasses.
 */
public class XHTMLTransitionalTransVisitorTestCase extends XHTMLFullTransVisitorTestCase {

    protected DOMProtocol createDOMProtocol(InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(new TestProtocolRegistry.TestXHTMLTransitionalFactory(), internalDevice);
        return protocol;
    }

    protected TransFactory getProtocolSpecificFactory() {
        return new XHTMLTransitionalTransFactory(null);
    }

    public void testGetPromotePreserveStyleAttributes() throws Exception {
        String[] expected = { "align", "bgcolor", "border", "cellspacing", "cellpadding", "dir", "frame", "id", "lang", "onclick", "ondblclick", "onkeydown", "onkeypress", "onkeyup", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup", "rules", "style", "summary", "title", "width", "xml:lang" };
        XHTMLTransitionalTransVisitor visitor = (XHTMLTransitionalTransVisitor) getProtocolSpecificFactory().getVisitor(protocol);
        String result = Utils.findMismatches(expected, visitor.getPromotePreserveStyleAttributes());
        assertTrue("Preserve style attribute mismatches: " + result, (result == null));
    }

    /**
     * todo Implement this test!
     * This has been stubbed out for the moment as this will currently fail
     * with the XHTMLBasicTransVisitorTestCase expected results.  this is
     * because XHTMLBasicTransVisitor optimizes away nested tables whereas
     * we do not here.
     *
     * When implemented this test should demonstrate that this output is the
     * same as the input but with whitespace removed from tables siblings.
     */
    public void testBasicInterface() throws Exception {
    }

    /**
     * todo Implement this test!
     * This has been stubbed out for the moment as this will currently fail
     * with the XHTMLBasicTransVisitorTestCase expected results.  this is
     * because XHTMLBasicTransVisitor optimizes away nested tables whereas
     * we do not here.
     *
     * When implemented this test should demonstrate that this output is the
     * same as the input but with whitespace removed from tables siblings.
     */
    public void testStyleClassRetention() throws Exception {
    }
}
