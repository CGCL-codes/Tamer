package org.iqual.infodrome;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Enhancer;
import org.objectweb.asm.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * <p/>
 * Created by IntelliJ IDEA.
 * User: zslajchrt
 * Date: May 14, 2010
 * Time: 10:29:08 PM
 */
public abstract class AbstractParticle extends Particle {

    protected AbstractParticle() {
    }

    protected AbstractParticle(String name) {
        super(name);
    }

    public Collection<Class> getTargetTypes() {
        return null;
    }

    public <X> Page<X> getConfigPage(Class<X> targetClass) throws Exception {
        Starters startersAnnot = findAnnotation(getClass().getAnnotations(), Starters.class);
        if (startersAnnot == null) {
            throw new IllegalArgumentException("No starter class defined");
        }
        Class[] starterClasses = startersAnnot.value();
        Class<X> selectedStarterClass = null;
        for (Class starterClass : starterClasses) {
            Starts starterAnnot = findAnnotation(starterClass.getAnnotations(), Starts.class);
            if (starterAnnot == null) {
                throw new IllegalArgumentException("@Starts annotation missing on " + starterClass + " class");
            }
            if (targetClass.isAssignableFrom(starterAnnot.value())) {
                selectedStarterClass = starterClass;
                break;
            }
        }
        if (selectedStarterClass == null) {
            throw new IllegalArgumentException("No starter found for " + targetClass + " target type");
        }
        String starterClassFile = selectedStarterClass.getName().replace('.', '/') + ".class";
        URL starterClassResource = selectedStarterClass.getClassLoader().getResource(starterClassFile);
        ParticlePageInterceptor<X> interceptor = new ParticlePageInterceptor<X>(this, targetClass, starterClassResource);
        Enhancer e = new Enhancer();
        e.setSuperclass(selectedStarterClass);
        e.setCallback(interceptor);
        Starter starter;
        if (selectedStarterClass.getConstructors()[0].getParameterTypes().length == 0) {
            starter = (Starter) e.create();
        } else {
            starter = (Starter) e.create(new Class[] { getClass() }, new Object[] { this });
        }
        interceptor.setWrappedStarterInstance(starter);
        starter.getInstance();
        return interceptor;
    }

    private static <A extends Annotation> A findAnnotation(Annotation[] paramAnnotations, Class<A> annotClass) {
        return Utils.findAnnotation(paramAnnotations, annotClass);
    }
}
