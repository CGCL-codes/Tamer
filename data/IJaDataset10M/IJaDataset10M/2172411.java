package net.sf.jasperreports.jsf.validation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.jsf.component.UIReport;

public abstract class ReportValidator implements Validator {

    public void validate(final FacesContext context, final UIComponent component) throws ValidationException {
        if (!(component instanceof UIReport)) {
            throw new IllegalArgumentException("");
        }
        doValidate(context, (UIReport) component);
    }

    protected abstract void doValidate(FacesContext context, UIReport report) throws ValidationException;
}
