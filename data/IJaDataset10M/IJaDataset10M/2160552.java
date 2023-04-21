package org.equanda.t5gui.services;

import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.InjectionProvider;
import org.apache.tapestry5.services.TransformConstants;

/**
 * Performs injection of translated messages
 *
 * @author <a href="mailto:vladimir.tkachenko@gmail.com">Vladimir Tkachenko</a>
 */
public class EquandaMessagesInjectionProvider implements InjectionProvider {

    @SuppressWarnings("unchecked")
    public boolean provideInjection(String fieldName, Class fieldType, ObjectLocator locator, ClassTransformation transformation, MutableComponentModel componentModel) {
        if (fieldType.equals(EquandaMessages.class)) {
            String msgSourceField = transformation.addInjectedField(EquandaMessagesSource.class, "equandaMessagesSource", locator.getService(EquandaMessagesSource.class));
            String body = String.format("%s = %s.%s( %s );", fieldName, msgSourceField, "getMessages", transformation.getResourcesFieldName());
            transformation.makeReadOnly(fieldName);
            transformation.extendMethod(TransformConstants.CONTAINING_PAGE_DID_LOAD_SIGNATURE, body);
            return true;
        }
        return false;
    }
}
