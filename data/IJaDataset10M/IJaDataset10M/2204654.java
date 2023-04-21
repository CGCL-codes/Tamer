package com.jesyre.collaboration.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import com.jesyre.collaboration.object.Proposta;

public class PropostaStatoConverter implements Converter {

    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object obj) {
        if ((obj == null)) {
            return "";
        }
        return ((Proposta.Stato) obj).toString();
    }

    public Object getAsObject(FacesContext facesContext, UIComponent uIComponent, String str) throws ConverterException {
        if (str == null) return null;
        if (str.equals("")) return null;
        return Proposta.Stato.toStato(str);
    }
}
