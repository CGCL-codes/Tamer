package com.ail.openquote.motorplus;

import com.ail.core.CoreProxy;
import com.ail.core.XMLString;
import com.ail.coretest.CoreUserTestCase;
import com.ail.openquote.ui.Label;
import com.ail.openquote.ui.PageFlow;
import com.ail.openquote.ui.SectionScroller;
import com.ail.openquote.ui.util.Binding;

public class TestPageFlow extends CoreUserTestCase {

    private static final long serialVersionUID = 0L;

    public TestPageFlow(String arg0) {
        super(arg0);
    }

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    protected void setUp() throws Exception {
        super.setupSystemProperties();
    }

    /**
	 * Test to ensure that the principal types used by the motor plus product can 
	 * be instantiated without throwing exceptions.
	 * @throws Exception
	 */
    public void testInstantiateTypes() throws Exception {
        CoreProxy cp = new CoreProxy();
        cp.newProductType("AIL.Demo.MotorPlus", "Quotation");
        cp.newProductType("AIL.Demo.MotorPlus", "VehicleAsset");
        cp.newProductType("AIL.Demo.MotorPlus", "DriverAsset");
        cp.newProductType("AIL.Demo.MotorPlus", "DriverHistoryAsset");
        cp.newProductType("AIL.Demo.MotorPlus", "QuotationPageFlow");
    }

    public void testPageFlowStructure() throws Exception {
        CoreProxy cp = new CoreProxy();
        PageFlow pf = (PageFlow) cp.newProductType("AIL.Demo.MotorPlus", "QuotationPageFlow");
        System.out.println(cp.toXML(pf));
    }

    public void testDataElementsOnLabel() throws Exception {
        CoreProxy cp = new CoreProxy();
        SectionScroller qs = new SectionScroller();
        Label l = new Label();
        l.setFormat("Hello %s");
        l.getParameter().add(new Binding("attribute[id='a']/value"));
        l.getParameter().add(new Binding("attribute[id='b']/value"));
        qs.setSectionTitle(l);
        System.out.println("" + cp.toXML(qs));
        String s = "<sectionScroller minRows='0' maxRows='-1' addAndDeleteEnabled='true' xsi:type='java:com.ail.openquote.ui.SectionScroller' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>" + "<sectionTitle format=\"Details for %s %s %s\">" + "<parameter xpath=\"attribute[id='make']/value\"/>" + "<parameter xpath=\"attribute[id='model']/value\"/>" + "<parameter xpath=\"attribute[id='registration']/value\"/>" + "</sectionTitle>" + "</sectionScroller>";
        qs = (SectionScroller) cp.fromXML(SectionScroller.class, new XMLString(s));
        assertEquals(3, qs.getSectionTitle().getParameter().size());
        assertEquals("attribute[id='make']/value", qs.getSectionTitle().getParameter().get(0).getXpath());
    }
}
