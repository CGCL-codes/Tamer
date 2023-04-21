package ch.epfl.lsr.adhoc.tools;

/**
 * This class loader can be used to reference classes by their names.
 * <p>
 * This class returns references to objects for which the classes are already
 * known at compile time. It is thus possible to not use reflection (performance,
 * limitations on small devices).
 * <p>
 * @author Urs Hunkeler
 * @version 1.0
 */
public final class StaticClassLoader {

    /**
   * The StaticClassLoader should not be instantiated.
   */
    private StaticClassLoader() {
        throw new RuntimeException("The StaticClassLoader should not be instantiated");
    }

    /**
   * Returns an instance for the given class.
   * <p>
   * Note that the full class name must be specified (i.e. with its package).
   * <p>
   * @param name The full class name of the class
   * @return An instance of the named class
   */
    public static Object getInstanceFor(String name) {
        Object obj = null;
        return obj;
    }
}
