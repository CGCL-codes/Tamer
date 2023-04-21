package org.springframework.webflow.executor.jsf;

import java.io.IOException;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

public class MockViewHandler extends ViewHandler {

    private UIViewRoot viewRoot;

    public Locale calculateLocale(FacesContext context) {
        return null;
    }

    public String calculateRenderKitId(FacesContext context) {
        return null;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        return viewRoot;
    }

    /**
	 * Set the view root that this mpck is supposed to create.
	 * @param viewRoot the view to set.
	 */
    public void setCreateView(UIViewRoot viewRoot) {
        this.viewRoot = viewRoot;
    }

    public String getActionURL(FacesContext context, String viewId) {
        return null;
    }

    public String getResourceURL(FacesContext context, String path) {
        return null;
    }

    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
    }

    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return null;
    }

    public void writeState(FacesContext context) throws IOException {
    }
}
