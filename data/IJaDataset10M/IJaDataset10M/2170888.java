package siac.com.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import siac.com.controller.AppModuloController;
import siac.com.entity.AppModulo;

/**
 * @author razao
 *
 */
@FacesConverter(forClass = AppModulo.class)
public class AppModuloConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        AppModuloController controller = (AppModuloController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "appModuloController");
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
        if (object instanceof AppModulo) {
            AppModulo o = (AppModulo) object;
            return getStringKey(o.getId());
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + AppModulo.class.getName());
        }
    }
}
