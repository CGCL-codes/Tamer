package net.sourceforge.mtc.integration.app1.pages;

import org.apache.tapestry.Block;
import org.apache.tapestry.PageRenderSupport;
import org.apache.tapestry.annotations.SetupRender;
import org.apache.tapestry.annotations.Environmental;
import org.apache.tapestry.annotations.IncludeStylesheet;
import org.apache.tapestry.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry.ioc.annotations.Inject;
import org.slf4j.Logger;

public class DropZoneDemo {

    @Inject
    private Logger _logger;

    private String _name;

    private static final String[] NAMES = { "Fred & Wilma", "Mr. <Roboto>", "Grim Fandango" };

    public String[] getNames() {
        return NAMES;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    Object onActionFromSelect(String name) {
        _name = name;
        return null;
    }
}
