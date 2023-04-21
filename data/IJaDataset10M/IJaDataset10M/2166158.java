package com.caucho.hessian.io;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Output stream for Hessian requests.
 *
 * <p>HessianOutput is unbuffered, so any client needs to provide
 * its own buffering.
 *
 * <h3>Serialization</h3>
 *
 * <pre>
 * OutputStream os = new FileOutputStream("test.xml");
 * HessianOutput out = new HessianSerializerOutput(os);
 *
 * out.writeObject(obj);
 * os.close();
 * </pre>
 *
 * <h3>Writing an RPC Call</h3>
 *
 * <pre>
 * OutputStream os = ...; // from http connection
 * HessianOutput out = new HessianSerializerOutput(os);
 * String value;
 *
 * out.startCall("hello");  // start hello call
 * out.writeString("arg1"); // write a string argument
 * out.completeCall();      // complete the call
 * </pre>
 */
public class HessianSerializerOutput extends Hessian2Output {

    /**
     * Creates a new Hessian output stream, initialized with an
     * underlying output stream.
     *
     * @param os the underlying output stream.
     */
    public HessianSerializerOutput(OutputStream os) {
        super(os);
    }

    /**
     * Creates an uninitialized Hessian output stream.
     */
    public HessianSerializerOutput() {
        super(null);
    }

    /**
     * Applications which override this can do custom serialization.
     *
     * @param object the object to write.
     */
    public void writeObjectImpl(Object obj) throws IOException {
        Class cl = obj.getClass();
        try {
            Method method = cl.getMethod("writeReplace", new Class[0]);
            Object repl = method.invoke(obj, new Object[0]);
            writeObject(repl);
            return;
        } catch (Exception e) {
        }
        try {
            writeMapBegin(cl.getName());
            for (; cl != null; cl = cl.getSuperclass()) {
                Field[] fields = cl.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    field.setAccessible(true);
                    writeString(field.getName());
                    writeObject(field.get(obj));
                }
            }
            writeMapEnd();
        } catch (IllegalAccessException e) {
            throw new IOExceptionWrapper(e);
        }
    }
}
