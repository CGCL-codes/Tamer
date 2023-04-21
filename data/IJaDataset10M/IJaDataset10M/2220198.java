package padrmi;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * Object stream that resolves unknown classes using given class loader. Used
 * for deserialization of objects from remote servers.
 */
public class ObjectInputStreamWithLoader extends ObjectInputStream {

    /**
	 * Class loader to be used.
	 */
    private final ClassLoader classLoader;

    /**
	 * Creates object using given input stream and class loader.
	 * 
	 * @param in
	 *            input stream
	 * @param classLoader
	 *            class loader
	 * @throws IOException
	 *             by super class constructor
	 */
    public ObjectInputStreamWithLoader(InputStream in, ClassLoader classLoader) throws IOException {
        super(in);
        this.classLoader = classLoader;
    }

    /**
	 * First tries to load class using the given classloader and if it fail it
	 * calls method from super class.
	 * 
	 * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
	 */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        try {
            if (Server.debug) {
                System.out.println("Loading class: " + desc.getName());
            }
            return Class.forName(desc.getName(), false, classLoader);
        } catch (ClassNotFoundException ex) {
            return super.resolveClass(desc);
        }
    }
}
