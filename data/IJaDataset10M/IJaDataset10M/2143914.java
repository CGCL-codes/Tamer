package de.grogra.pf.registry;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;
import de.grogra.util.CompoundEnumeration;

class ImportsClassLoader extends PluginClassLoader {

    private final LibraryClassLoader[] imports;

    ImportsClassLoader(ClassLoader parent, LibraryClassLoader[] imports, PluginDescriptor descriptor) {
        super(new URL[0], parent, descriptor);
        this.imports = imports;
    }

    @Override
    protected Class findClassNull(String name) {
        return loadClassParentsSelf(name);
    }

    @Override
    Class loadClassParentsSelf(String name) {
        for (int i = imports.length - 1; i >= 0; i--) {
            Class c;
            if ((c = imports[i].loadClassParentsSelf(name)) != null) {
                return c;
            }
        }
        return null;
    }

    @Override
    public URL findResource(String name) {
        URL u = super.findResource(name);
        if (u != null) {
            return u;
        }
        for (int i = imports.length - 1; i >= 0; i--) {
            if ((u = imports[i].getResource(name)) != null) {
                return u;
            }
        }
        return null;
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        CompoundEnumeration<URL> ce = new CompoundEnumeration<URL>(true);
        ce.add(super.findResources(name));
        for (int i = imports.length - 1; i >= 0; i--) {
            ce.add(imports[i].findResources(name));
        }
        return ce;
    }

    protected boolean shouldLookForClass(String name) {
        return false;
    }

    @Override
    public String toString() {
        return "Import " + Arrays.toString(imports);
    }
}
