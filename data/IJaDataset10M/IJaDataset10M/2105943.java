package org.jplate.util.version;

/**
 *
 * This class satisfies the VersionFactoryIfc interface and adheres to the
 * Singleton pattern.
 *
 */
public final class VersionFactory implements VersionFactoryIfc {

    /**
     *
     * This class manages the singleton.
     *
     */
    private static final class VersionFactorySingleton {

        /**
         *
         * The actual singleton.
         *
         */
        static final VersionFactoryIfc _singleton = new VersionFactory();
    }

    /**
     *
     * Default constructor not allowed.
     *
     */
    private VersionFactory() {
    }

    /**
     *
     * This method will return the singleton.
     *
     * @return the singleton.
     *
     */
    public VersionFactoryIfc getSingleton() {
        return VersionFactorySingleton._singleton;
    }

    /**
     *
     * This method will create a version object whose contents will be stored
     * in file name.
     *
     * @param fileName represents the name of the file to store the date of the
     *        version as well as number.
     *
     * @param versionNumber represents the version number.
     *
     * @return a version object whose date and number are contained in fileName.
     *
     * @throws VersionException if any problems arise create the version object.
     *
     */
    public VersionIfc createVersion(final String fileName, final String versionNumber) throws VersionException {
        return new Version(fileName, versionNumber);
    }

    /**
     *
     * This method will create a version object whose contents are stored in
     * resourceName.
     *
     * @param resourceName represents the name of the resource who contains the
     *        contents of the version object to be returned.
     *
     * @return a version object whose contents are contained in resourceName.
     *
     * @throws VersionException if any problems arise create the version object.
     *
     */
    public VersionIfc createVersion(final String resourceName) throws VersionException {
        return new Version(resourceName);
    }
}
