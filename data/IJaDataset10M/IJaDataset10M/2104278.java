package org.opensih.comunes.Utils.Converters;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.opensih.comunes.Modelo.Sala;

public class SalasConverter implements Converter {

    List<Sala> salas;

    public SalasConverter(List<Sala> salas) {
        this.salas = salas;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object obj) {
        if (obj == null) return null;
        Sala sala = (Sala) obj;
        String val = sala.getNombre();
        return val;
    }

    public Object getAsObject(FacesContext facesContext, UIComponent component, String str) throws ConverterException {
        if (str == null || str.length() == 0) {
            return null;
        }
        for (Sala sala : salas) {
            if (str.trim().compareTo(sala.getNombre().trim()) == 0) {
                return sala;
            }
        }
        return null;
    }
}
