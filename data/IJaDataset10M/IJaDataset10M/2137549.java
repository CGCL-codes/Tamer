package org.aspectme.cldc.reflect.model;

public interface ReflectClassLoader {

    /**
	 * Loads a class with the specified name. Arrays are denoted by a leading
	 * bracket, e.g. "[int". Array objects are surrounded by L and semi colon,
	 * e.g. "[Ljava.lang.String;".
	 * 
	 * @param name The class name.
	 * 
	 * @return The loaded class or null if the class doesn't exist or couldn't
	 * be loaded.
	 */
    public ReflectClass loadClass(String name);
}
