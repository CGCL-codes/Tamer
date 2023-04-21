package jsfSatsutaVA;

import entitySatsutaVA.SalesSatsutaVA;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import jpaSatsutaVA.SalesSatsutaVAJpaController;

/**
 *
 * @author dosER
 */
public class SalesSatsutaVAConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Integer id = new Integer(string);
        SalesSatsutaVAJpaController controller = (SalesSatsutaVAJpaController) facesContext.getApplication().getVariableResolver().resolveVariable(facesContext, "salesSatsutaVAJpa");
        return controller.findSalesSatsutaVA(id);
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof SalesSatsutaVA) {
            SalesSatsutaVA o = (SalesSatsutaVA) object;
            if (component instanceof HtmlSelectOneListbox || component instanceof HtmlSelectManyListbox || component instanceof HtmlSelectOneMenu || component == null) return o.getId() == null ? "" : o.getId().toString();
            return o.toString();
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: entitySatsutaVA.SalesSatsutaVA");
        }
    }
}
