package jm.lib.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import jm.lib.util.ConfUtil;

/**
 * @author JimingLiu
 *
 */
public class BooleanConverter implements Converter {

    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) throws ConverterException {
        return ConfUtil.getBoolean(arg2, false) ? Boolean.TRUE : Boolean.FALSE;
    }

    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) throws ConverterException {
        if (arg2 instanceof Boolean) {
            return ((Boolean) arg2).booleanValue() ? "1" : "0";
        } else {
            return "0";
        }
    }
}
