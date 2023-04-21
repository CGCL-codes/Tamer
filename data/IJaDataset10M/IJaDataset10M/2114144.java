package org.basegen.base.view.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;

/**
 * A JSF converter to CEP
 */
public class EncryptConverter implements Converter {

    /**
     * Encrypt the value
     * @param value value
     * @return string
     */
    public String encrypt(String value) {
        ShaPasswordEncoder encoder = new ShaPasswordEncoder();
        return encoder.encodePassword(value, null);
    }

    /**
     * Get as object
     * @param context context
     * @param component component
     * @param value value
     * @return object
     */
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return encrypt(value);
    }

    /**
     * Get as string
     * @param context context
     * @param component component
     * @param value value
     * @return string
     */
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return encrypt(value.toString());
        } else {
            return null;
        }
    }
}
