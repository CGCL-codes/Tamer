package com.servengine.formprocessor;

import com.servengine.user.UserSessionSBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.naming.NamingException;

@FacesConverter(forClass = Form.class)
public class FormProcessorFormConverter extends com.servengine.jsf.Converter {

    protected static String USERSESSIONSBEAN_ATTRIBUTEID = "userSession";

    /**
	 * converts the String representation of the key back to the Object
	 */
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        UserSessionSBean userSession = (UserSessionSBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(USERSESSIONSBEAN_ATTRIBUTEID);
        try {
            FormProcessorManagerLocal manager = (FormProcessorManagerLocal) getManager("FormProcessorManager");
            return manager.getForm(userSession.getPortal(), new Integer(value));
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
	 * converts the Key object into its String representation.
	 */
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return "";
        if (value instanceof Form) {
            return ((Form) value).getId().toString();
        } else {
            throw new IllegalArgumentException("Cannot convert non-key object in FilePublisherCategoryConverter");
        }
    }
}
