package br.ufmg.lcc.arangi.view.converter;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class EnumTypeConverter implements Converter {

    @SuppressWarnings("unchecked")
    public Object getAsObject(FacesContext context, UIComponent comp, String value) {
        ELContext elContext = context.getELContext();
        ValueExpression valueExpression = comp.getValueExpression("value");
        Class enumType = valueExpression.getType(elContext);
        return Enum.valueOf(enumType, value);
    }

    @SuppressWarnings("unchecked")
    public String getAsString(FacesContext context, UIComponent comp, Object object) {
        if (object == null) {
            return "";
        }
        return object.toString();
    }
}
