package ee.webAppToolkit.core.expert;

import java.lang.annotation.Annotation;
import java.util.Set;
import com.google.inject.ImplementedBy;
import ee.webAppToolkit.core.RequestMethod;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.expert.impl.ActionImpl;

@ImplementedBy(ActionImpl.class)
public interface Action {

    public String getName();

    public Set<RequestMethod> getRequestMethods();

    public Class<?> getReturnType();

    public Result invoke(Object instance) throws Throwable;

    public Annotation[] getAnnotations();

    boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass);
}
