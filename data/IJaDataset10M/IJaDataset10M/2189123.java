package net.sf.yui4jsf.component.button;

import java.io.IOException;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;

/**
 * @author Latest modification by $Author: cagatay_civici $
 * @version $Revision: 921 $ $Date: 2007-07-10 03:52:06 -0400 (Tue, 10 Jul 2007) $
 */
public class MenuButtonItemRenderer extends Renderer {

    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
        if (component instanceof UICommand) {
            renderMenuButtonItem(facesContext, component);
        } else if (component instanceof UIOutput) {
        } else {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }

    private String getComponentId(FacesContext facesContext, UIComponent component) {
        MenuButton menuButton = (MenuButton) component.getParent();
        MenuButtonItem menuButtonItem = (MenuButtonItem) component;
        return menuButton.getClientId(facesContext) + ":" + menuButtonItem.getId();
    }

    protected void renderMenuButtonItem(FacesContext facesContext, UIComponent component) throws IOException {
        MenuButtonItem menuButtonItem = (MenuButtonItem) component;
        String id = getComponentId(facesContext, component);
        String value = (String) menuButtonItem.getValue();
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<option value=\"" + id + "\">" + value + " </option>\n");
    }

    public void decode(FacesContext facesContext, UIComponent component) {
        if (component instanceof UICommand) {
            MenuButtonItem menuItemButton = (MenuButtonItem) component;
            if (isSubmitted(facesContext, menuItemButton)) menuItemButton.queueEvent(new ActionEvent(menuItemButton));
        } else if (component instanceof UIOutput) {
        } else {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }

    public boolean isSubmitted(FacesContext context, MenuButtonItem menuButtonItem) {
        MenuButton menuButton = (MenuButton) menuButtonItem.getParent();
        String key = menuButton.getClientId(context);
        if (key == null) return false;
        String value = (String) context.getExternalContext().getRequestParameterMap().get(key);
        if (value == null || value.equals("") || !value.equals(getComponentId(context, menuButtonItem))) return false;
        return true;
    }
}
