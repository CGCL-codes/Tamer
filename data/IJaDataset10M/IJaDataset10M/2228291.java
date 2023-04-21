package com.luxoft.fitpro.core.testcase;

public final class Location {

    private static FileNameUtils utils = new FileNameUtils();

    private String absolutePath;

    private String relativePath;

    private Location(String absolutePath, String relativePath) {
        this.absolutePath = utils.formatPath(absolutePath);
        this.relativePath = utils.formatPath(relativePath);
    }

    public static Location createLocation(String absolutePath) {
        assertIsAbsolute(absolutePath);
        String relativePath = utils.extractLastPathElement(absolutePath);
        return new Location(absolutePath, relativePath);
    }

    public static Location createLocationRelativeToFile(String baseFilePath, String absolutePath) {
        assertIsAbsolute(baseFilePath);
        assertIsAbsolute(absolutePath);
        String relativePath = utils.createRelativePath(baseFilePath, absolutePath);
        return new Location(absolutePath, relativePath);
    }

    public static Location createLocationRelativeToFolder(String baseFolderPath, String relativePath) {
        assertIsAbsolute(baseFolderPath);
        assertIsRelative(relativePath);
        String absolutePath = utils.mergePaths(baseFolderPath, relativePath);
        return new Location(absolutePath, relativePath);
    }

    private static void assertIsAbsolute(String path) {
        if (!utils.isAbsolute(path)) {
            throw new IllegalArgumentException(path + " is not an absolute path");
        }
    }

    private static void assertIsRelative(String path) {
        if (!utils.isRelative(path)) {
            throw new IllegalArgumentException(path + " is not a relative path");
        }
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getBaseDir() {
        return utils.extractBaseDir(getAbsolutePath());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((absolutePath == null) ? 0 : absolutePath.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (absolutePath == null) {
            if (other.absolutePath != null) {
                return false;
            }
        } else if (!absolutePath.equals(other.absolutePath)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return relativePath + " [" + absolutePath + "]";
    }
}
