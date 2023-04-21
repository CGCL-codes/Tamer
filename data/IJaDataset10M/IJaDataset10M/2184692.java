package net.sf.tacos.seam.components;

import org.apache.tapestry.Asset;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.annotations.Environmental;
import org.apache.tapestry.annotations.Path;
import org.apache.tapestry.ioc.annotations.Inject;

public class Layout {

    @Environmental
    private PageRenderSupport pageRenderSupport;

    @Inject
    @Path("context:css/main.css")
    private Asset mainCss;

    @Inject
    @Path("context:css/dark/skin.css")
    private Asset skinCss;

    void setupRender() {
        pageRenderSupport.addStylesheetLink(mainCss, null);
        pageRenderSupport.addStylesheetLink(skinCss, null);
    }
}
