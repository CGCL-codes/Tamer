package cz.cuni.mff.ksi.jinfer.base.utils;

import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.Lookup;

/**
 * Logic for looking up service provider implementations based on their name.
 * 
 * @author vektor
 */
public final class ModuleSelectionHelper {

    private static final Logger LOG = Logger.getLogger(ModuleSelectionHelper.class);

    private static final Comparator<NamedModule> MODULE_NAME_CMP = new Comparator<NamedModule>() {

        @Override
        public int compare(final NamedModule o1, final NamedModule o2) {
            return o1.getDisplayName().compareTo(o2.getDisplayName());
        }
    };

    /** Library class. */
    private ModuleSelectionHelper() {
    }

    /**
   * Defines what should happen if none of the implementations has the name
   * we look for.
   */
    public enum Fallback {

        /** Throw an exception. */
        EXCEPTION, /** Return the first implementation. */
        FIRST
    }

    /**
   * Returns a list of all the implementations of requested interface registered via
   * {@link org.openide.util.lookup.ServiceProvider}.
   *
   * @param <T> Interface for which the implementations to be found.
   * Must extend NamedModule.
   * @param clazz Interface for which the implementations to be found.
   * Must extend NamedModule.
   *
   * @return List alphabetically sorted by {@link NamedModule#getDisplayName()} of all implementations of modules registered as
   * {@link org.openide.util.lookup.ServiceProvider} implementing the requested
   * interface.
   */
    public static <T extends NamedModule> List<T> lookupImpls(final Class<T> clazz) {
        @SuppressWarnings("unchecked") final List<T> implementations = new ArrayList<T>((Collection<T>) Lookup.getDefault().lookupAll(clazz));
        if (BaseUtils.isEmpty(implementations)) {
            throw new IllegalArgumentException("No implementations of " + clazz.getCanonicalName() + " found.");
        }
        Collections.sort(implementations, MODULE_NAME_CMP);
        return implementations;
    }

    /**
   * Looks up implementation of requested interface based on its name.
   * 
   * @param <T> Interface to be found. Must extend NamedModule.
   * @param clazz Interface to be found. Must extend NamedModule.
   * @param name Module name. This parameter will be compared to the names
   * ({@link cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule#getName()})
   * of all implementations of the requested interface.
   * 
   * @return Implementation with the correct name. If there is no implementation
   * of this interface <cite>at all</cite>, runtime exception. If there are
   * implementations but none has the correct name, first implementation (in
   * alphabetic order) is returned.
   */
    public static <T extends NamedModule> T lookupImpl(final Class<T> clazz, final String name) {
        return lookupImpl(clazz, name, Fallback.FIRST);
    }

    /**
   * Looks up implementation of requested interface based on its name.
   *
   * @param <T> Interface to be found. Must extend NamedModule.
   * @param clazz Interface to be found. Must extend NamedModule.
   * @param name Module name. This parameter will be compared to the names
   * ({@link cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule#getName()})
   * of all implementations of the requested interface.
   * @param fallback Defines what to do if no implementation with correct name
   * is found.
   *
   * @return Implementation with the correct name. If there is no implementation
   * of this interface <cite>at all</cite>, runtime exception. If there are
   * implementations but none has the correct name, the behaviour depends on the
   * fallback parameter. See {@link Fallback}.
   */
    public static <T extends NamedModule> T lookupImpl(final Class<T> clazz, final String name, final Fallback fallback) {
        @SuppressWarnings("unchecked") final List<T> implementations = lookupImpls(clazz);
        for (final T implementation : implementations) {
            if (implementation.getName().equals(name)) {
                return implementation;
            }
        }
        switch(fallback) {
            case EXCEPTION:
                throw new IllegalArgumentException("No implementation of " + clazz.getCanonicalName() + " with name " + name + " was found.");
            case FIRST:
                LOG.warn("No implementation of " + clazz.getCanonicalName() + " with name " + name + " was found, using the first one.");
                return implementations.get(0);
            default:
                throw new IllegalArgumentException("Unknown fallback type: " + fallback);
        }
    }
}
