package net.sf.brightside.mobilestock.view.web.tapestry.core;

import org.apache.tapestry.ioc.AnnotationProvider;
import org.apache.tapestry.ioc.ObjectLocator;
import org.apache.tapestry.ioc.ObjectProvider;
import org.apache.tapestry.ioc.annotations.InjectService;
import org.springframework.context.ApplicationContext;

public class SpringObjectProvider implements ObjectProvider {

    private final ApplicationContext _context;

    public SpringObjectProvider(@InjectService("ApplicationContext") ApplicationContext context) {
        _context = context;
    }

    public <T> T provide(Class<T> objectType, AnnotationProvider annotationProvider, ObjectLocator locator) {
        Bean annotation = annotationProvider.getAnnotation(Bean.class);
        if (annotation == null) return null;
        String beanName = annotation.value();
        try {
            Object raw = _context.getBean(beanName, objectType);
            return objectType.cast(raw);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
