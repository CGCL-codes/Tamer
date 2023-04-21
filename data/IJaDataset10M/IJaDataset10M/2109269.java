package net.sourceforge.mandalore.jsf;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import net.sourceforge.mandalore.model.enums.StatusTypeEnum;

/**
 * @author Catalin Kormos Oct 8, 2006
 *
 */
public class StatusConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent c, String id) throws ConverterException {
        int ordinal = 0;
        try {
            ordinal = Integer.parseInt(id);
        } catch (NumberFormatException exc) {
            return null;
        }
        return StatusTypeEnum.values()[ordinal];
    }

    public String getAsString(FacesContext facesContext, UIComponent c, Object obj) throws ConverterException {
        if (obj instanceof StatusTypeEnum) return String.valueOf(((StatusTypeEnum) obj).getOrdinal());
        return null;
    }
}
