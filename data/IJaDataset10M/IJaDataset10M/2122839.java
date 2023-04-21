package com.siegre.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import com.siegre.util.Format;

@Name("edadOfertaempleoValidator")
@Scope(ScopeType.CONVERSATION)
@org.jboss.seam.annotations.faces.Validator
@BypassInterceptors
public class EdadOfertaempleoValidator implements javax.faces.validator.Validator, java.io.Serializable {

    private static final long serialVersionUID = -1947945354034840246L;

    @Logger
    Log log;

    private String componentId;

    private String clientId;

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value != null) {
            componentId = component.getId();
            log.info("validate(): componentId = " + componentId);
            clientId = component.getClientId(context);
            log.info("validate(): clientId = " + clientId);
            UIInput componente2 = (UIInput) context.getViewRoot().findComponent("ofertaempleo:ofemEdadmaximaField:ofemEdadmaxima");
            String edadMaxima = (String) componente2.getSubmittedValue();
            Byte edadMinima = (Byte) value;
            if (edadMinima < 0) throw new ValidatorException(new FacesMessage("la cantidad debe ser mayor o igual a 0"));
            if (Format.esNumeroEntero(edadMaxima) && Format.parseInt(edadMaxima) >= 0 && edadMinima > Format.parseInt(edadMaxima)) throw new ValidatorException(new FacesMessage("la edad mínima es mayor que la edad máxima"));
        }
    }
}
