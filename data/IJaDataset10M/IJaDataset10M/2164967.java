package org.eclipse.babel.build.core;

import java.io.File;

public class ResourceProxy {

    private File fileResource;

    /** From translation catalogue */
    private String relativePath;

    /** From eclipse target */
    private String canonicalPath;

    public ResourceProxy(File fileResource) {
        this.fileResource = fileResource;
    }

    public ResourceProxy(String relativePath) {
        this.relativePath = relativePath;
        this.relativePath = this.relativePath.replace('/', File.separatorChar);
        this.canonicalPath = relativePath.replace(File.separatorChar, '/');
    }

    public ResourceProxy(File fileResource, String relativePath) {
        this.fileResource = fileResource;
        this.relativePath = relativePath.replace('/', File.separatorChar);
        this.canonicalPath = relativePath.replace(File.separatorChar, '/');
    }

    public File getFileResource() {
        return fileResource;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getCanonicalPath() {
        return canonicalPath;
    }
}
