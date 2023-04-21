package net.cloneshop.apexcost.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import net.cloneshop.apexcost.entity.Contact;

public class ConverterContact implements Converter {

    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        if (!arg2.equals("---")) {
            MainUtil uBean = (MainUtil) arg0.getExternalContext().getApplicationMap().get("uBean");
            return ObjectUtil.loadObject(uBean.getCompanies(), Long.valueOf(arg2));
        }
        return null;
    }

    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        if (null != arg2) return ((Contact) arg2).getID().toString();
        return null;
    }
}
