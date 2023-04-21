package DAOs;

import beans.EmpConfidential;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Ioana C
 */
public class EmpConfidentialConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        EmpConfidentialJpaController controller = (EmpConfidentialJpaController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "empConfidentialJpa");
        return controller.findEmpConfidential(id);
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof EmpConfidential) {
            EmpConfidential o = (EmpConfidential) object;
            return o.getPersonID() == null ? "" : o.getPersonID().toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: beans.EmpConfidential");
        }
    }
}
