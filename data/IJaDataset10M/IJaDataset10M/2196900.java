package org.akrogen.tkui.core.gui;

import org.akrogen.tkui.core.loader.ITkuiLoader;
import org.akrogen.tkui.core.resources.ITkuilURIResolver;

/**
 * GUI context to store TkUI loader, GUI builder ID and URI resolver.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class GuiContext {

    private ITkuiLoader tkuiLoader;

    private String guiBuilderId;

    private ITkuilURIResolver uriResolver;

    public ITkuiLoader getTkuiLoader() {
        return tkuiLoader;
    }

    public void setTkuiLoader(ITkuiLoader tkuiLoader) {
        this.tkuiLoader = tkuiLoader;
    }

    public String getGuiBuilderId() {
        return guiBuilderId;
    }

    public void setGuiBuilderId(String guiBuilderId) {
        this.guiBuilderId = guiBuilderId;
    }

    public ITkuilURIResolver getURIResolver() {
        return uriResolver;
    }

    public void setURIResolver(ITkuilURIResolver uriResolver) {
        this.uriResolver = uriResolver;
    }
}
