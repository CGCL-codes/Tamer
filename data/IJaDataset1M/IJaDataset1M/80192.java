package net.sourceforge.strategema.common;

import net.sourceforge.strategema.games.ApplicationInterface;
import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * General utility functions for Strategema.
 * 
 * @author Lizzy
 * 
 */
public class Utils {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    /** Cannot construct instances. */
    private Utils() {
        throw new UnsupportedOperationException();
    }

    /**
	 * Finds all classes present on a search path that are subclasses of a given class and creates instances using their
	 * no-args constructors.
	 * @param <T> The class to construct instances of, one for each subclass.
	 * @param superclass The class to construct instances of.
	 * @param paths The search path to load subclasses from.
	 * @return A collection of objects, one for each subclass.
	 */
    public static <T> Collection<T> scanForClasses(final Class<T> superclass, final Set<File> paths) {
        final ClassLoader loader = ProgramConfig.CONFIG.getClassLoader();
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        final ArrayList<T> objects = new ArrayList<T>();
        boolean errorOccurred = false;
        for (final File path : paths) {
            final File[] classFiles = path.listFiles(Globals.CLASS_FILE_FILTER);
            for (final File classFile : classFiles) {
                try {
                    final String filename = classFile.getName();
                    final Class<?> cl = loader.loadClass(filename.substring(0, filename.length() - 6));
                    if (!classes.contains(cl) && superclass.isAssignableFrom(cl) & !cl.isInterface() && !Modifier.isAbstract(cl.getModifiers())) {
                        try {
                            @SuppressWarnings("unchecked") final T instance = (T) cl.newInstance();
                            objects.add(instance);
                            classes.add(cl);
                        } catch (final Exception e) {
                            LOG.severe(e.toString());
                            if (!errorOccurred) {
                                ApplicationInterface.getAPI().getInteraction().error("Failed to load " + classFile.toString() + ".\nCheck that it has a public no-args constructor.");
                                errorOccurred = true;
                            }
                        }
                    }
                } catch (final ClassNotFoundException e) {
                    LOG.severe(e.toString());
                    if (!errorOccurred) {
                        ApplicationInterface.getAPI().getInteraction().error("Failed to load " + classFile.toString() + ".\nUnable to find a needed class " + e.getLocalizedMessage());
                        errorOccurred = true;
                    }
                } catch (final LinkageError e) {
                    LOG.severe(e.toString());
                    if (!errorOccurred) {
                        ApplicationInterface.getAPI().getInteraction().error("Failed to load " + classFile.toString() + ".\n" + e.toString());
                        errorOccurred = true;
                    }
                }
            }
        }
        return objects;
    }
}
