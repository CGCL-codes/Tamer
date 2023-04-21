package net.sf.orcc.util;

import java.net.URL;

/**
 * This class defines a getter for multiple class loader.
 * 
 * @author Matthieu Wipliez
 * @author Jerome Gorin
 * 
 */
public class MultipleClassLoader extends ClassLoader {

    private ClassLoader[] loaders;

    public MultipleClassLoader(Class<?>... classes) {
        this.loaders = new ClassLoader[classes.length];
        int i = 0;
        for (Class<?> clasz : classes) {
            loaders[i++] = clasz.getClassLoader();
        }
    }

    @Override
    public URL getResource(String name) {
        for (ClassLoader loader : loaders) {
            URL url = loader.getResource(name);
            if (url != null) {
                return url;
            }
        }
        return null;
    }
}
