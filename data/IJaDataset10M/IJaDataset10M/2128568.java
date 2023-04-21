package studetns.uiGarbatsevichMO;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import studetns.controllersGarbatsevichMO.KafedrasGarbatsevichMOJpaController;
import studetns.entitiesGarbatsevichMO.KafedrasGarbatsevichMO;

/**
 *
 * @author dosER
 */
public class KafedrasGarbatsevichMOConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        KafedrasGarbatsevichMOJpaController controller = (KafedrasGarbatsevichMOJpaController) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "kafedrasGarbatsevichMOJpa");
        return controller.findKafedrasGarbatsevichMO(id);
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof KafedrasGarbatsevichMO) {
            KafedrasGarbatsevichMO o = (KafedrasGarbatsevichMO) object;
            if (component instanceof HtmlSelectOneListbox || component instanceof HtmlSelectManyListbox || component instanceof HtmlSelectOneMenu || component == null) return o.getId() == null ? "" : o.getId().toString();
            return o.toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: studetns.entitiesGarbatsevichMO.KafedrasGarbatsevichMO");
        }
    }
}
