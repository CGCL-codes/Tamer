package pl.rzarajczyk.utils.system.paths;

public abstract class PathsFactory {

    private static PathsWithSupportedMarker[] instances = new PathsWithSupportedMarker[] { new WindowsPaths(), new UnixPaths() };

    /**
     * Gets the best implementation of {@link Paths} for the current platform.
     * This method will always return some implementation. If no system-specific
     * implementation can be found, the {@link DefaultPaths} will be used.
     * @return the best {@link Paths} implementation
     */
    public static Paths getInstance() {
        for (PathsWithSupportedMarker instance : instances) {
            if (instance.isSupported()) {
                return instance;
            }
        }
        return new DefaultPaths();
    }
}
