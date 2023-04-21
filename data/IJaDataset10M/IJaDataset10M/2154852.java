package tigerunit.util.filter;

import tigerunit.util.Filter;
import java.lang.reflect.AnnotatedElement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Annotation;

public class AnnotationFilter implements Filter<AnnotatedElement> {

    private Class<? extends Annotation> _type;

    public AnnotationFilter(Class<? extends Annotation> type) {
        _type = type;
    }

    public boolean accept(AnnotatedElement element, Class c, ElementType elementType, String elementName) {
        return element.isAnnotationPresent(_type);
    }
}
