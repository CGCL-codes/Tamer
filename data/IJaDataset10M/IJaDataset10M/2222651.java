package net.sf.tacos.seam.pages;

import org.apache.tapestry.Asset;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.annotations.Environmental;
import org.apache.tapestry.annotations.Path;
import org.apache.tapestry.ioc.annotations.Inject;

public class RemotingExample {

    @Environmental
    private PageRenderSupport pageRenderSupport;

    @Inject
    @Path("context:js/autocomplete.js")
    private Asset library;

    void setupRender() {
        pageRenderSupport.addScriptLink(library);
    }
}
