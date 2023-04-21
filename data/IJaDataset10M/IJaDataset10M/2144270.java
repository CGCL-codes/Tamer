package net.sourceforge.mtc.base.toolkit;

import org.apache.tapestry.annotations.SupportsInformalParameters;
import org.apache.tapestry.annotations.Path;
import org.apache.tapestry.annotations.SetupRender;
import org.apache.tapestry.annotations.Environmental;
import org.apache.tapestry.Asset;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: mlake
 * Date: Jan 14, 2008
 * Time: 10:33:38 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CoreBase {

    @Environmental
    private PageRenderSupport _support;

    @Inject
    @Path("${tapestry.dojo_toolkit}/dojo/dojo.js")
    private Asset _dojoBase;

    @Inject
    @Path("${tapestry.dojo_toolkit}/dojo/resources/dojo.css")
    private Asset _dojoStyle;

    @SetupRender
    void setupRender() {
        _support.addStylesheetLink(_dojoStyle, null);
        _support.addScriptLink(_dojoBase);
    }
}
