package wrm.saferJava.oval.constraint;

import wrm.saferJava.oval.Validator;
import wrm.saferJava.oval.configuration.annotation.AbstractAnnotationCheck;
import wrm.saferJava.oval.context.OValContext;

/**
 * @author Sebastian Thomschke
 */
public class NotNullCheck extends AbstractAnnotationCheck<NotNull> {

    private static final long serialVersionUID = 1L;

    /**
	 * {@inheritDoc}
	 */
    public boolean isSatisfied(final Object validatedObject, final Object valueToValidate, final OValContext context, final Validator validator) {
        return valueToValidate != null;
    }
}
