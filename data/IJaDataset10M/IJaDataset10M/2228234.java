package org.springframework.webflow.builder;

import junit.framework.TestCase;
import org.springframework.webflow.Event;
import org.springframework.webflow.RequestContext;
import org.springframework.webflow.ViewSelector;
import org.springframework.webflow.support.ApplicationView;
import org.springframework.webflow.support.ConversationRedirect;
import org.springframework.webflow.support.ExternalRedirect;
import org.springframework.webflow.support.FlowRedirect;
import org.springframework.webflow.test.MockRequestContext;

/**
 * Test case for TextToViewDescriptorCreator.
 * 
 * @author Erwin Vervaet
 */
public class TextToViewSelectorTests extends TestCase {

    private TextToViewSelector converter;

    public void setUp() {
        DefaultFlowArtifactFactory flowArtifactFactory = new DefaultFlowArtifactFactory();
        converter = new TextToViewSelector(flowArtifactFactory);
        converter.setConversionService(flowArtifactFactory.getConversionService());
    }

    public void testApplicationView() {
        ViewSelector selector = (ViewSelector) converter.convert("myView");
        RequestContext context = getRequestContext();
        ApplicationView view = (ApplicationView) selector.makeSelection(context);
        assertEquals("myView", view.getViewName());
        assertEquals(5, view.getModel().size());
    }

    public void testConversationRedirect() {
        ViewSelector selector = (ViewSelector) converter.convert("redirect:myView");
        RequestContext context = getRequestContext();
        ConversationRedirect redirect = (ConversationRedirect) selector.makeSelection(context);
        assertEquals("myView", redirect.getApplicationView().getViewName());
        assertEquals(5, redirect.getApplicationView().getModel().size());
    }

    public void testFlowRedirect() {
        ViewSelector selector = (ViewSelector) converter.convert("flowRedirect:myFlow");
        RequestContext context = getRequestContext();
        FlowRedirect redirect = (FlowRedirect) selector.makeSelection(context);
        assertEquals("myFlow", redirect.getFlowId());
        assertEquals(0, redirect.getInput().size());
    }

    public void testFlowRedirectWithModel() {
        ViewSelector selector = (ViewSelector) converter.convert("flowRedirect:myFlow?foo=${flowScope.foo}&bar=${requestScope.oven}");
        RequestContext context = getRequestContext();
        FlowRedirect redirect = (FlowRedirect) selector.makeSelection(context);
        assertEquals("myFlow", redirect.getFlowId());
        assertEquals(2, redirect.getInput().size());
        assertEquals("bar", redirect.getInput().get("foo"));
        assertEquals("mit", redirect.getInput().get("bar"));
    }

    public void testExternalRedirect() {
        ViewSelector selector = (ViewSelector) converter.convert("externalRedirect:myUrl.htm?foo=${flowScope.foo}&bar=${requestScope.oven}");
        RequestContext context = getRequestContext();
        ExternalRedirect view = (ExternalRedirect) selector.makeSelection(context);
        assertEquals("myUrl.htm?foo=bar&bar=mit", view.getUrl());
    }

    private RequestContext getRequestContext() {
        MockRequestContext ctx = new MockRequestContext();
        ctx.getFlowScope().put("foo", "bar");
        ctx.getFlowScope().put("bar", "car");
        ctx.getRequestScope().put("oven", "mit");
        ctx.getRequestScope().put("cat", "woman");
        ctx.getFlowScope().put("boo", new Integer(3));
        ctx.setLastEvent(new Event(this, "sample"));
        return ctx;
    }
}
