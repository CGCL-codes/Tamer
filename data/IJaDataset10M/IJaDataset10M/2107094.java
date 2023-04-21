package org.gap.jseed;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.Modifier;
import javassist.NotFoundException;

/**
 * This object is responsible for delegating the code injection based on 
 * class {@code Annotation}s, method {@code Annotation}s or field {@code Annotation}s.
 * 
 * @author gpelcha
 * @see ClassInjector
 * @see MethodInjector
 * @see FieldInjector
 */
class Injector implements Configuration {

    private static ClassPool POOL;

    private SubClassFactory factory;

    private ClassInjector classInjector;

    private MethodInjector methodInjector;

    private FieldInjector fieldInjector;

    static {
        try {
            POOL = ClassPool.getDefault();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Injector() {
        factory = new SubClassFactory(POOL);
        classInjector = new ClassInjector();
        methodInjector = new MethodInjector();
        fieldInjector = new FieldInjector();
    }

    /**
	 * Injects the abstraction with an implementation that can be instantiated.
	 * @param <T>
	 * @param type
	 * @return
	 * @throws InjectCodeException unable to inject code.
	 */
    public <T> Class<? extends T> inject(Class<T> type) {
        return inject(null, type);
    }

    /**
	 * 
	 * @param <T>
	 * @param annotation specifies the class annotation to be used.
	 * @param type
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> inject(Class<? extends Annotation> annotation, Class<T> type) {
        try {
            Class<? extends T> result = null;
            CtClass abstraction = null;
            try {
                abstraction = POOL.get(type.getName());
            } catch (NotFoundException e) {
                POOL.insertClassPath(new ClassClassPath(type));
                abstraction = POOL.get(type.getName());
            }
            CtClass implementation = factory.createSubClass(type.getName(), abstraction);
            methodInjector.injectBehavior(type, abstraction, implementation);
            if (annotation == null) {
                classInjector.injectBehavior(type, abstraction, implementation);
            } else {
                classInjector.injectBehavior(annotation, type, abstraction, implementation);
            }
            fieldInjector.injectBehavior(type, abstraction, implementation);
            implementation.setModifiers(Modifier.PUBLIC);
            result = (Class<? extends T>) implementation.toClass();
            implementation.prune();
            return result;
        } catch (Exception e) {
            throw new InjectCodeException(e);
        }
    }

    @Override
    public void registerClass(Class<? extends Annotation> annotation, Class<? extends InvocationHandler> handler) {
        classInjector.add(annotation, handler);
    }

    public void registerClass(Class<? extends Annotation> annotation, Class<? extends InvocationHandler> handler, Validator validator) {
        classInjector.add(annotation, handler);
        classInjector.validateWith(annotation, validator);
    }

    public void registerMethod(Class<? extends Annotation> annotation, Class<? extends InvocationHandler> handler) {
        methodInjector.add(annotation, handler);
    }

    public void registerField(Class<? extends Annotation> annotation, Class<? extends FieldHandler> handler) {
        fieldInjector.add(annotation, handler);
    }
}
