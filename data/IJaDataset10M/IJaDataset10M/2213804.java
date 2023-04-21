package org.thymeleaf.spring3.expression;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.thymeleaf.context.VariablesMap;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
class VariablesMapPropertyAccessor extends ReflectivePropertyAccessor {

    private static final Class<?>[] TARGET_CLASSES = new Class<?>[] { VariablesMap.class };

    public static final VariablesMapPropertyAccessor INSTANCE = new VariablesMapPropertyAccessor();

    public VariablesMapPropertyAccessor() {
        super();
    }

    @Override
    public Class<?>[] getSpecificTargetClasses() {
        return TARGET_CLASSES;
    }

    @Override
    public boolean canRead(final EvaluationContext context, final Object target, final String name) throws AccessException {
        if (target == null) {
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TypedValue read(final EvaluationContext context, final Object target, final String name) throws AccessException {
        if (target == null) {
            throw new AccessException("Cannot read property of null target");
        }
        if (!(target instanceof VariablesMap)) {
            throw new AccessException("Cannot read target of class " + target.getClass().getName());
        }
        return new TypedValue(((VariablesMap<String, ?>) target).get(name));
    }

    @Override
    public boolean canWrite(final EvaluationContext context, final Object target, final String name) throws AccessException {
        if (target == null) {
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(final EvaluationContext context, final Object target, final String name, final Object newValue) throws AccessException {
        if (target == null) {
            throw new AccessException("Cannot write property of null target");
        }
        if (!(target instanceof VariablesMap)) {
            throw new AccessException("Cannot write target of class " + target.getClass().getName());
        }
        ((VariablesMap<String, Object>) target).put(name, newValue);
    }
}
