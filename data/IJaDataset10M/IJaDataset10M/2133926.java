package net.sourceforge.mtc.mixins;

import org.apache.tapestry.ioc.annotations.Inject;
import org.apache.tapestry.annotations.Path;
import org.apache.tapestry.annotations.Environmental;
import org.apache.tapestry.Asset;
import org.apache.tapestry.PageRenderSupport;

public class DojoBase {

    @Environmental
    private PageRenderSupport _support;

    @Inject
    @Path("${tapestry.dojo_toolkit}/dojo/dojo.js")
    private Asset _dojoBase;

    @Inject
    @Path("${tapestry.dojo_toolkit}/dojo/resources/dojo.css")
    private Asset _dojoStyle;

    void setupRender() {
        _support.addStylesheetLink(_dojoStyle, null);
        _support.addScriptLink(_dojoBase);
    }
}
