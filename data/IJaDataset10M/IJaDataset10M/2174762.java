package net.sf.openbugproject.jsf;

import net.sf.openbugproject.controller.StateTransitionJpaController;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import net.sf.openbugproject.persistence.StateTransition;

/**
 *
 * @author slavo
 */
public class StateTransitionConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        StateTransitionJpaController controller = (StateTransitionJpaController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "stateTransitionJpa");
        return controller.findStateTransition(id);
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof StateTransition) {
            StateTransition o = (StateTransition) object;
            return o.getIdStateTransition() == null ? "" : o.getIdStateTransition().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: net.sf.openbugproject.persistence.StateTransition");
        }
    }
}
