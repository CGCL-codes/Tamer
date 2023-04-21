package de.powerstaff.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.commons.lang.NotImplementedException;
import de.powerstaff.business.entity.HistoryType;
import de.powerstaff.web.backingbean.MessageConstants;

public class HistoryTypeConverter implements Converter, MessageConstants {

    public Object getAsObject(FacesContext aContext, UIComponent aComponent, String aValue) {
        throw new NotImplementedException();
    }

    public String getAsString(FacesContext aContext, UIComponent aComponent, Object aValue) {
        HistoryType theType = (HistoryType) aValue;
        if (theType == null) {
            return "";
        }
        return theType.getDescription();
    }
}
