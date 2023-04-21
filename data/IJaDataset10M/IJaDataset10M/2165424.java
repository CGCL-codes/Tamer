package wrm.saferJava.oval.constraint;

import wrm.saferJava.oval.Validator;
import wrm.saferJava.oval.configuration.annotation.AbstractAnnotationCheck;
import wrm.saferJava.oval.context.OValContext;

/**
 * @author Sebastian Thomschke
 */
public class AssertConstraintSetCheck extends AbstractAnnotationCheck<AssertConstraintSet> {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void configure(final AssertConstraintSet constraintAnnotation) {
        super.configure(constraintAnnotation);
        setId(constraintAnnotation.id());
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String getErrorCode() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public String getId() {
        return id;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String getMessage() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int getSeverity() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
	 *  <b>This method is not used.</b><br>
	 *  The validation of this special constraint is directly performed by the Validator class
	 *  @throws UnsupportedOperationException always thrown if this method is invoked
	 */
    public boolean isSatisfied(final Object validatedObject, final Object valueToValidate, final OValContext context, final Validator validator) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void setErrorCode(final String errorCode) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void setId(final String id) {
        this.id = id;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void setMessage(final String message) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void setSeverity(final int severity) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
