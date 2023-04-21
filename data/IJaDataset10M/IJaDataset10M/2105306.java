package org.jmlspecs.jir.bytecode;

public interface IClassLoaderProvider {

    ClassLoader getClassLoaderFor(String className);
}
