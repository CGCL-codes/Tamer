package org.nakedobjects.xat;

import org.nakedobjects.NakedObjects;
import org.nakedobjects.object.NakedObjectRuntimeException;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObjectSpecificationLoader;
import org.nakedobjects.object.ReflectorFactory;
import org.nakedobjects.object.defaults.InternalNakedObject;
import org.nakedobjects.object.defaults.NakedObjectSpecificationImpl;
import org.nakedobjects.object.reflect.Reflector;
import org.nakedobjects.object.reflect.internal.InternalReflector;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.log4j.Logger;

public class StaticNakedObjectSpecificationLoader implements NakedObjectSpecificationLoader {

    private static final Logger LOG = Logger.getLogger(StaticNakedObjectSpecificationLoader.class);

    private static Hashtable classes;

    static {
        classes = new Hashtable();
    }

    /**
     * Expose as a .NET property
     * 
     * @property
     * @deprecated
     */
    public void set_ReflectorFactory(ReflectorFactory reflectorFactory) {
        throw new NakedObjectRuntimeException("Changed configuration; setup in NakedObjects instead");
    }

    public NakedObjectSpecification loadSpecification(Class cls) {
        return loadSpecification(cls.getName());
    }

    public NakedObjectSpecification loadSpecification(String className) {
        if (className == null) {
            throw new NullPointerException("No class name specified");
        }
        if (classes.containsKey(className)) {
            return (NakedObjectSpecification) classes.get(className);
        } else {
            Reflector reflector;
            try {
                Class cls = Class.forName(className);
                if (InternalNakedObject.class.isAssignableFrom(cls) || cls.getName().startsWith("java.")) {
                    reflector = new InternalReflector(className);
                } else {
                    ReflectorFactory reflectorFactory = NakedObjects.getReflectorFactory();
                    if (reflectorFactory == null) {
                        throw new NakedObjectRuntimeException("No reflector factory has be set up");
                    }
                    reflector = reflectorFactory.createReflector(className);
                }
                LOG.info("initialising specification for " + className);
                NakedObjectSpecificationImpl spec = new NakedObjectSpecificationImpl();
                classes.put(className, spec);
                spec.reflect(className, reflector);
                return spec;
            } catch (ClassNotFoundException e) {
                LOG.debug("non class " + className);
                NakedObjectSpecificationImpl spec = new NakedObjectSpecificationImpl();
                spec.nonReflect(className);
                classes.put(className, spec);
                return spec;
            }
        }
    }

    public NakedObjectSpecification[] getAllSpecifications() {
        int size = classes.size();
        NakedObjectSpecification[] cls = new NakedObjectSpecification[size];
        Enumeration e = classes.elements();
        int i = 0;
        while (e.hasMoreElements()) {
            cls[i++] = (NakedObjectSpecification) e.nextElement();
        }
        return cls;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        LOG.info("finalizing specification loader " + this);
    }

    public void shutdown() {
    }

    public void init() {
    }
}
