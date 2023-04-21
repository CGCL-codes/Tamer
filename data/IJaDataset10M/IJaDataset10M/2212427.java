package org.apache.myfaces.trinidadinternal.renderkit;

import javax.faces.context.FacesContext;
import javax.faces.application.StateManager;
import javax.faces.component.UIViewRoot;

public class MStateManager extends StateManager {

    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId, String renderKitId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
