package com.volantis.mcs.ibm.websphere.mcsi;

/**
 * This class tests GeneratedResourcesElement
 *
 * todo: later factor together with similar portlet content child tests
 */
public class GeneratedResourcesElementTestCase extends PortletContextChildElementTestAbstract {

    /**
     * The base directory
     */
    private static String BASE_DIR = "/flobnobs";

    /**
     * Test the element
     */
    public void testElement() throws Exception {
        GeneratedResourcesElement element = new GeneratedResourcesElement();
        pageContextMock.expects.pushMCSIElement(element);
        pageContextMock.expects.popMCSIElement().returns(element);
        GeneratedResourcesAttributes attrs = new GeneratedResourcesAttributes();
        attrs.setBaseDir(BASE_DIR);
        int result;
        result = element.elementStart(requestContextMock, attrs);
        assertEquals("Unexpected result from elementStart.", MCSIConstants.PROCESS_ELEMENT_BODY, result);
        result = element.elementEnd(requestContextMock, null);
        assertEquals("Unexpected result from elementEnd.", MCSIConstants.CONTINUE_PROCESSING, result);
        String baseDir = parent.getGeneratedResourcesConfiguration().getBaseDir();
        assertEquals("BaseDir has unexpected value", baseDir, BASE_DIR);
    }
}
