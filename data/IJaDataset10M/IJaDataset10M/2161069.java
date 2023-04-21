package net.godcode.t5c.components;

import org.apache.tapestry.ClientElement;
import org.apache.tapestry.ComponentResources;
import org.apache.tapestry.MarkupWriter;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.annotations.AfterRender;
import org.apache.tapestry.annotations.BeginRender;
import org.apache.tapestry.annotations.Environmental;
import org.apache.tapestry.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry.annotations.Mixin;
import org.apache.tapestry.annotations.SupportsInformalParameters;
import org.apache.tapestry.corelib.mixins.RenderInformals;
import org.apache.tapestry.ioc.annotations.Inject;

@SupportsInformalParameters
@IncludeJavaScriptLibrary("clicksubmit.js")
public class LinkSubmit implements ClientElement {

    @SuppressWarnings("unused")
    @Mixin
    private RenderInformals renderInformals;

    @Inject
    private ComponentResources resources;

    @Environmental
    private PageRenderSupport renderSupport;

    /** The client-side id. */
    private String clientId;

    @BeginRender
    void beginRender(final MarkupWriter writer) {
        clientId = renderSupport.allocateClientId(resources.getId());
        renderSupport.addScript("new ClickSubmit('%s');", getClientId());
        writer.element("a", "id", getClientId(), "href", "#");
    }

    @AfterRender
    void afterRender(final MarkupWriter writer) {
        writer.end();
    }

    public String getClientId() {
        return clientId;
    }
}
