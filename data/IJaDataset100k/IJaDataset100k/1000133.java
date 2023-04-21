package org.sigoa.refimpl.go.base;

import org.sfc.refimpl.naming.ID;
import org.sfc.spec.math.random.IRandomizer;
import org.sfc.spec.naming.IID;
import org.sigoa.spec.jobsystem.IHost;

/**
 * The base class of all sigoa objects
 * 
 * @author Thomas Weise
 */
public class SigoaObject extends ID {

    /**
   * The serial version id.
   */
    private static final long serialVersionUID = 1L;

    /**
   * Create a new id in the given parent context
   * 
   * @param parent
   *          the parent context
   * @param simpleName
   *          the simple name of this id
   * @param resolve
   *          the id to resolve to
   */
    public SigoaObject(final SigoaObjectGroup parent, final String simpleName, final IID resolve) {
        super((parent != null) ? parent : SigoaObjectGroup.OPTIMIZATION_ROOT, simpleName, resolve);
    }

    /**
   * Create a new id in the given parent context
   * 
   * @param parent
   *          the parent context
   * @param simpleName
   *          the simple name of this id
   */
    public SigoaObject(final SigoaObjectGroup parent, final String simpleName) {
        this(parent, simpleName, null);
    }

    /**
   * Check the given simple name for validity
   * 
   * @param name
   *          the name
   * @param clazz
   *          the class to be constructed
   * @return the preprocessed name
   */
    protected static final String processName(final String name, final Class<?> clazz) {
        return SigoaObjectGroup.processName(name, clazz);
    }

    /**
   * Obtain the current host
   * 
   * @return the current host
   */
    protected static final IHost currentHost() {
        final Thread t;
        t = Thread.currentThread();
        if (t instanceof IHost) {
            return ((IHost) t);
        }
        return DummyHost.INSTANCE;
    }

    /**
   * Obtain the current randomizer
   * 
   * @return the current randomizer
   */
    protected static final IRandomizer currentRandomizer() {
        return currentHost().getRandomizer();
    }
}
