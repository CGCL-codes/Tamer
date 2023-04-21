package org.jul.dcl.classpath;

import org.jul.dcl.fetcher.ClassFetcher;
import org.jul.dcl.fetcher.UnsupportedSourceException;

public class RawClasspath extends Classpath {

    public boolean add(byte[] data) throws UnsupportedSourceException {
        return add(ClassFetcher.getFetcher(null, data));
    }

    public RawClasspath(ClassLoader parent) {
        super(parent);
    }

    public RawClasspath(Classpath classpathParent) {
        super(classpathParent);
    }
}
