package net.sourceforge.visages.renderkit.renderer;

import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 * Base classe for XUL Renderers
 * 
 */
public class BaseXulRenderer extends Renderer {

    public String convertClientId(FacesContext context, String clientId) {
        return clientId;
    }
}
