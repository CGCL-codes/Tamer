package net.sf.jasperreports.jsf.spi;

import java.util.Set;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.jsf.validation.Validator;

public final class ValidatorLoader {

    private static final Set<ValidatorFactory> validatorFactoryCache = Services.set(ValidatorFactory.class);

    public static Validator getValidator(final FacesContext context, final UIComponent component) {
        ValidatorFactory validatorFactory = null;
        for (final ValidatorFactory factory : validatorFactoryCache) {
            if (factory.acceptsComponent(component)) {
                validatorFactory = factory;
                break;
            }
        }
        if (validatorFactory == null) {
            throw new ValidatorFactoryNotFoundException("No factory found for component: " + component.getFamily());
        }
        return validatorFactory.createValidator(context, component);
    }

    private ValidatorLoader() {
    }
}
