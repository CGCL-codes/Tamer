package wrm.saferJava.oval.exception;

import wrm.saferJava.oval.ConstraintViolation;
import wrm.saferJava.oval.context.ConstructorParameterContext;
import wrm.saferJava.oval.context.FieldContext;
import wrm.saferJava.oval.context.MethodEntryContext;
import wrm.saferJava.oval.context.MethodParameterContext;
import wrm.saferJava.oval.context.MethodReturnValueContext;
import wrm.saferJava.oval.context.OValContext;
import wrm.saferJava.oval.internal.Log;

/**
 * Translates OVal specific exceptions to standard exceptions part of the JRE:
 * <ul>
 * <li><code>ConstraintsViolatedException</code> for constructor/method parameter translated to <code>IllegalArgumentException</code>
 * <li><code>ConstraintsViolatedException</code> for class field translated to <code>IllegalStateException</code>
 * <li><code>ConstraintsViolatedException</code> for method return values translated to <code>IllegalStateException</code>
 * <li>Other exceptions based on <code>OValException</code> translated to <code>RuntimeException</code>
 * </ul>
 * @author Sebastian Thomschke
 */
public class ExceptionTranslatorJDKExceptionsImpl implements ExceptionTranslator {

    private static final Log LOG = Log.getLog(ExceptionTranslatorJDKExceptionsImpl.class);

    /**
	 * {@inheritDoc}
	 */
    public RuntimeException translateException(final OValException ex) {
        if (ex instanceof ConstraintsViolatedException) {
            final ConstraintsViolatedException cex = (ConstraintsViolatedException) ex;
            final ConstraintViolation cv = cex.getConstraintViolations()[0];
            final OValContext ctx = cv.getContext();
            if (ctx instanceof MethodParameterContext || ctx instanceof ConstructorParameterContext || ctx instanceof MethodEntryContext) {
                final IllegalArgumentException iaex = new IllegalArgumentException(cv.getMessage(), ex.getCause());
                iaex.setStackTrace(ex.getStackTrace());
                LOG.debug("Translated Exception {1} to {2}", ex, iaex);
                return iaex;
            }
            if (ctx instanceof FieldContext || ctx instanceof MethodReturnValueContext) {
                final IllegalStateException ise = new IllegalStateException(cv.getMessage(), ex.getCause());
                ise.setStackTrace(ex.getStackTrace());
                LOG.debug("Translated Exception {1} to {2}", ex, ise);
                return ise;
            }
        }
        {
            final RuntimeException rex = new RuntimeException(ex.getMessage(), ex.getCause());
            rex.setStackTrace(ex.getStackTrace());
            LOG.debug("Translated Exception {1} to {2}", ex, rex);
            return rex;
        }
    }
}
