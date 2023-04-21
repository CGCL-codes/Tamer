package org.granite.messaging.amf.io.util.externalizer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import org.granite.messaging.amf.io.util.instantiator.BigIntegerInstantiator;

/**
 * @author Franck WOLFF
 */
public class BigIntegerExternalizer extends DefaultExternalizer {

    public static final int RADIX = 36;

    public BigIntegerExternalizer() {
    }

    @Override
    public int accept(Class<?> clazz) {
        return (clazz == BigInteger.class ? 1 : -1);
    }

    @Override
    public Object newInstance(String type, ObjectInput in) throws IOException, ClassNotFoundException, InstantiationException, InvocationTargetException, IllegalAccessException {
        return new BigIntegerInstantiator();
    }

    @Override
    public void writeExternal(Object o, ObjectOutput out) throws IOException, IllegalAccessException {
        out.writeObject(((BigInteger) o).toString(RADIX));
    }
}
