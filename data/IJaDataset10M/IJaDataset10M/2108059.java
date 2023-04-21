package com.alisoft.xplatform.asf.cache.memcached.client;

import java.io.*;

public class ContextObjectInputStream extends ObjectInputStream {

    ClassLoader mLoader;

    public ContextObjectInputStream(InputStream in, ClassLoader loader) throws IOException, SecurityException {
        super(in);
        mLoader = loader;
    }

    @SuppressWarnings("unchecked")
    protected Class resolveClass(ObjectStreamClass v) throws IOException, ClassNotFoundException {
        if (mLoader == null) return super.resolveClass(v); else return Class.forName(v.getName(), true, mLoader);
    }
}
