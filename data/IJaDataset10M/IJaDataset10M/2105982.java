package org.jmage.tags.encoder;

import org.jmage.tags.GenericTagHandlerTests;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * EncoderTagHandlerTests
 */
public class EncoderTagHandlerPNGTestsNoIEWithEventHandlers extends GenericTagHandlerTests {

    protected void setUp() throws Exception {
        super.setUp();
        jmageTagHandler = new EncoderTagHandler();
        jmageTagHandler.setPageContext((PageContext) mockPageContext.proxy());
        jmageTagHandler.setImage("image.gif");
        jmageTagHandler.setWidth("200px");
        jmageTagHandler.setHeight("100px");
        jmageTagHandler.setEncode("png");
        jmageTagHandler.setOnclick(";");
        jmageTagHandler.setOndblclick(";");
        jmageTagHandler.setOnmouseover(";");
        jmageTagHandler.setOnmouseout(";");
        jmageTagHandler.setOnmousemove(";");
        jmageTagHandler.setOnmouseup(";");
        jmageTagHandler.setOnmousedown(";");
        jmageTagHandler.setOnkeydown(";");
        jmageTagHandler.setOnkeyup(";");
        jmageTagHandler.setId("1");
    }

    protected void setExpectations() {
        mockPageContext.expects(once()).method("getRequest").will(returnValue((HttpServletRequest) mockHttpServletRequest.proxy()));
        mockPageContext.expects(once()).method("getOut").will(returnValue((JspWriter) mockJspWriter.proxy()));
        mockHttpServletRequest.expects(once()).method("getContextPath").will(returnValue("http://host.org/context"));
        mockHttpServletRequest.expects(atLeastOnce()).method("getHeader").with(eq("user-agent")).will(returnValue("mocky browser 12.3"));
        mockHttpServletRequest.expects(atLeastOnce()).method("getHeader").with(eq("User-Agent")).will(returnValue("mocky browser 12.3"));
        mockJspWriter.expects(once()).method("print").with(eq(this.getTagResult())).isVoid();
    }

    protected String getTagResult() {
        return "<img width=\"200px\" height=\"100px\" " + "src=\"http://host.org/context/jmage?image=image.gif&encode=png&chain=org.jmage.filter.NoOpFilter\"" + " id=\"1\"" + " onclick=\";\"" + " ondblclick=\";\"" + " onmousedown=\";\"" + " onmouseup=\";\"" + " onmouseover=\";\"" + " onmousemove=\";\"" + " onmouseout=\";\"" + " onkeydown=\";\"" + " onkeyup=\";\"" + "/>";
    }
}
