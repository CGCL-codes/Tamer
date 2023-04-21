package siac.com.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import siac.com.controller.LocMunicipioController;
import siac.com.entity.LocMunicipio;

/**
 * @author razao
 *
 */
@FacesConverter(forClass = LocMunicipio.class)
public class LocMunicipioConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        LocMunicipioController controller = (LocMunicipioController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "locMunicipioController");
        return controller.getDao().find(getKey(value));
    }

    java.lang.Long getKey(String value) {
        java.lang.Long key;
        key = Long.valueOf(value);
        return key;
    }

    String getStringKey(java.lang.Long value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof LocMunicipio) {
            LocMunicipio o = (LocMunicipio) object;
            return getStringKey(o.getId());
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + LocMunicipio.class.getName());
        }
    }
}
