package org.jmage.tags.filter.spatial;

/**
 * SharpenTagHandlerTests
 */
public class SharpenTagHandlerUnsharpMaskTest extends SharpenTagHandlerSharpTest {

    protected void setUp() throws Exception {
        super.setUp();
        ((SharpenTagHandler) jmageTagHandler).setStrategy("mask");
        jmageTagHandler.setId("1");
    }

    protected String getTagResult() {
        return "<img width=\"100\" height=\"200\" " + "src=\"http://host.org/context/jmage?image=image.gif&encode=jpg&chain=org.jmage.filter.spatial.UnsharpMaskFilter\"" + " id=\"1\"/>";
    }
}
