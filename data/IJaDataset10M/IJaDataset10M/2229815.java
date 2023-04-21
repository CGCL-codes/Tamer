package org.jquery4jsf.renderkit;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public interface AjaxBaseRenderer {

    public void encodePartially(FacesContext context, UIComponent component) throws IOException;

    String getActionURL(FacesContext context);
}
