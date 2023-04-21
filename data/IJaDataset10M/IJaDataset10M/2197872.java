package normal.engine.implementation.jni;

/**
 * Exception thrown when a shared library to be used with JNI could not
 * be loaded.
 */
public class JNILibraryException extends Exception {

    /**
     * Create a new exception because a shared library could not be loaded.
     *
     * @param libName the library that could not be loaded.
     */
    public JNILibraryException(String libName) {
        super("Could not load shared library [" + libName + "].");
    }

    /**
     * Returns the current library search path.
     *
     * @return the current library search path, or <tt>null</tt> if this
     * could not be determined.
     */
    public String getLibraryPath() {
        try {
            return System.getProperty("java.library.path");
        } catch (Throwable th) {
        }
        return null;
    }
}
