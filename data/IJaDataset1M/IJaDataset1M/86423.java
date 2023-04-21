package main.utils.Converters;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import main.rim.entity.OrganizationBean;

public class ServicioConverter implements Converter {

    List<OrganizationBean> orgs;

    public ServicioConverter(List<OrganizationBean> parts) {
        this.orgs = parts;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object obj) {
        if (obj == null) return null;
        OrganizationBean serv = (OrganizationBean) obj;
        String val = serv.getName().toString();
        return val;
    }

    public Object getAsObject(FacesContext facesContext, UIComponent component, String str) throws ConverterException {
        if (str == null || str.length() == 0) {
            return null;
        }
        for (OrganizationBean serv : orgs) {
            if (str.trim().compareTo(serv.getName().toString().trim()) == 0) {
                return serv;
            }
        }
        return null;
    }
}
