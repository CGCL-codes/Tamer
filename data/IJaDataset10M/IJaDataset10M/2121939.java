package jp.dodododo.reloadable.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class ObjectInputStreamEx extends ObjectInputStream {

    private ClassLoader loader;

    public ObjectInputStreamEx(ClassLoader loader) throws IOException, SecurityException {
        super();
        this.loader = loader;
    }

    public ObjectInputStreamEx(InputStream in, ClassLoader loader) throws IOException {
        super(in);
        this.loader = loader;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        return Class.forName(desc.getName(), true, loader);
    }
}
