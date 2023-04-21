package com.volantis.mcs.protocols.html;

import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.trans.ContainerActions;
import com.volantis.mcs.protocols.trans.ContainerValidator;
import com.volantis.mcs.protocols.trans.LCMImpl;
import com.volantis.mcs.protocols.trans.TransElement;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransVisitor;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This is the unit test for the XHTMLBasicTransFactory class and the
 * TransFactory interface.
 *
 * @todo make sure all methods are tested, specifically the visitor methods
 */
public class XHTMLBasicTransFactoryTestCase extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Protocol specific configuration.
     */
    protected XHTMLBasicConfiguration configuration = new XHTMLBasicConfiguration();

    /**
     * Test version of protocol.
     */
    protected XHTMLBasic protocol;

    private InternalDevice internalDevice;

    protected void setUp() throws Exception {
        super.setUp();
        internalDevice = InternalDeviceTestHelper.createTestDevice();
        protocol = (XHTMLBasic) new ProtocolBuilder().build(new TestProtocolRegistry.TestXHTMLBasicFactory(), internalDevice);
    }

    /**
     * Simple method test
     */
    public void testGetTable() {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);
        Element element = domFactory.createElement();
        TransElement trans = factory.getTable(element, protocol);
        assertTrue("getTable returned an instance of " + trans.getClass().getName(), trans instanceof XHTMLBasicTransTable);
    }

    /**
     * Simple method test
     */
    public void testGetCell() {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);
        Element element = domFactory.createElement();
        TransElement trans = factory.getCell(element, element, 0, 0, null);
        assertTrue("getCell returned an instance of " + trans.getClass().getName(), trans instanceof XHTMLBasicTransCell);
    }

    /**
     * Simple method test
     */
    public void testGetVisitor() throws Exception {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(new TestProtocolRegistry.TestDOMProtocolFactory(), internalDevice);
        TransVisitor visitor = factory.getVisitor(protocol);
        assertTrue("The visitor should be an XHTMLBasicTransVisitor", visitor instanceof XHTMLBasicTransVisitor);
    }

    /**
     * Simple method test
     */
    public void testGetLCM() {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);
        assertSame("expect the factory to return the default LCM singleton", factory.getLCM(), LCMImpl.getInstance());
    }

    /**
     * Simple method test
     */
    public void testGetMapper() {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);
        assertTrue("expect the factory to return a new XHTMLBasicTransMapper", factory.getMapper(protocol) instanceof XHTMLBasicTransMapper);
    }

    /**
     * Simple method test
     */
    public void testGetElementHelper() {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);
        assertSame("expect the factory to return the " + "XHTMLBasicElementHelper singleton", factory.getElementHelper(), XHTMLBasicElementHelper.getInstance());
    }

    /**
     * Simple method test.
     */
    public void testGetContainerValidator() {
        TransFactory factory = new XHTMLBasicTransFactory(configuration);
        String[] elementNames = { "table", "tr", "td", "span", "div", "form", "strong" };
        int[] expectedResult = { ContainerActions.PROMOTE, ContainerActions.PROMOTE, ContainerActions.PROMOTE, ContainerActions.PROMOTE, ContainerActions.PROMOTE, ContainerActions.INVERSE_REMAP, ContainerActions.PROMOTE };
        ContainerValidator cv = factory.getContainerValidator(protocol);
        Element element = domFactory.createElement();
        for (int i = 0; i < elementNames.length; i++) {
            element.setName(elementNames[i]);
            assertEquals(elementNames[i] + " action " + i + " not as expected", cv.getAction(element, null), expectedResult[i]);
        }
    }
}
