package org.apache.myfaces.trinidadinternal.application;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.apache.myfaces.trinidad.render.InternalView;

public class TestInternalView extends InternalView {

    public TestInternalView() {
    }

    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {
        ViewHandlerImplTest.__internalViewCalled = "create";
        return null;
    }

    /**
   * Restores the UIViewRoot;  return null if no view should be returned.
   */
    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        ViewHandlerImplTest.__internalViewCalled = "restore";
        return null;
    }

    /**
   * Renders the view.
   */
    @Override
    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
        ViewHandlerImplTest.__internalViewCalled = "render";
    }
}
