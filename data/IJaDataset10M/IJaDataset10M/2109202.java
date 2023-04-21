package org.nakedobjects.nof.reflect.javax;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.nof.reflect.java.AbstractJavaObjectLoaderFactory;
import org.nakedobjects.nof.reflect.java.JavaDomainObjectContainer;

/**
 * Sets up a {@link JavaxDomainObjectContainer} as the implementation of 
 * {@link DomainObjectContainer}, providing CgLib-based proxy services.
 * 
 * @author dkhaywood
 */
public class JavaxObjectLoaderFactory extends AbstractJavaObjectLoaderFactory {

    private static final Logger LOGGER = Logger.getLogger(JavaxObjectLoaderFactory.class);

    protected Logger getLOGGER() {
        return LOGGER;
    }

    public JavaxObjectLoaderFactory() {
        super("javax");
    }

    protected JavaDomainObjectContainer createDomainObjectFactory() {
        return new JavaxDomainObjectContainer();
    }
}
