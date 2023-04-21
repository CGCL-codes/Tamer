package br.gov.frameworkdemoiselle.internal.processor;

import java.lang.reflect.InvocationTargetException;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.BeanManager;
import br.gov.frameworkdemoiselle.exception.ApplicationException;
import br.gov.frameworkdemoiselle.exception.SeverityType;

/**
 * Represents an annotated method to be processed;
 * 
 * @param <DC>
 *            declaring class owner of the method
 */
public class AnnotatedMethodProcessor<DC> extends AbstractProcessor<DC> {

    public AnnotatedMethodProcessor(final AnnotatedMethod<DC> annotatedMethod, final BeanManager beanManager) {
        super(annotatedMethod, beanManager);
    }

    protected AnnotatedMethod<DC> getAnnotatedMethod() {
        return (AnnotatedMethod<DC>) getAnnotatedCallable();
    }

    public boolean process(Object... args) throws Throwable {
        getLogger().info(getBundle().getString("processing", getAnnotatedMethod().getJavaMember().toGenericString()));
        try {
            getAnnotatedMethod().getJavaMember().invoke(getReferencedBean(), args);
        } catch (InvocationTargetException cause) {
            handleException(cause.getCause());
        }
        return true;
    }

    private void handleException(Throwable cause) throws Throwable {
        ApplicationException ann = cause.getClass().getAnnotation(ApplicationException.class);
        if (ann != null && SeverityType.FATAL == ann.severity()) {
            throw cause;
        } else {
            switch(ann.severity()) {
                case INFO:
                    getLogger().info(cause.getMessage());
                    break;
                case WARN:
                    getLogger().warn(cause.getMessage());
                    break;
                default:
                    getLogger().error(getBundle().getString("processing-fail"), cause);
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return getBundle().getString("for", getClass().getSimpleName(), getAnnotatedMethod().getJavaMember().toGenericString());
    }
}
