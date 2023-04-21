package com.izforge.izpack.compiler;

/**
 * Factory class for handling the packager classes
 *
 * @author Dennis Reil, <Dennis.Reil@reddot.de>
 */
public class PackagerFactory {

    /**
     * Returns a new instantiation of the specified packager
     *
     * @param classname the class name
     * @return a new packager instance
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static IPackager getPackager(String classname) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (IPackager) Class.forName(classname).newInstance();
    }
}
