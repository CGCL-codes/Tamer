package org.granite.messaging.amf.io.util.externalizer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.math.MathContext;
import org.granite.messaging.amf.io.util.instantiator.MathContextInstantiator;

/**
 * @author Franck WOLFF
 */
public class MathContextExternalizer extends DefaultExternalizer {

    public MathContextExternalizer() {
    }

    @Override
    public int accept(Class<?> clazz) {
        return (clazz == MathContext.class ? 1 : -1);
    }

    @Override
    public Object newInstance(String type, ObjectInput in) throws IOException, ClassNotFoundException, InstantiationException, InvocationTargetException, IllegalAccessException {
        return new MathContextInstantiator();
    }

    @Override
    public void writeExternal(Object o, ObjectOutput out) throws IOException, IllegalAccessException {
        MathContext mc = (MathContext) o;
        out.writeObject(mc.getPrecision());
        out.writeObject(mc.getRoundingMode());
    }
}
