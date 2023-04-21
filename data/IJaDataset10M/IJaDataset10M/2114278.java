package dp.lib.dto.geda.assembler.extension.impl;

/**
 * Byte array class loader, load classes directly from bytes of generated classes.
 * 
 * @author denispavlov
 *
 */
public class ByteClassLoader extends ClassLoader {

    /**
	 * @param parent parent class loader
	 */
    public ByteClassLoader(final ClassLoader parent) {
        super(parent);
    }

    /**
	 * @param name full name of class
	 * @param clazz class as byte array
	 * @return class object
	 */
    public Class<?> loadClass(final String name, final byte[] clazz) {
        return defineClass(name, clazz, 0, clazz.length);
    }
}
