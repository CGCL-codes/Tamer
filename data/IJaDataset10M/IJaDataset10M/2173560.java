package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.CONCRETE;
import java.lang.reflect.InvocationTargetException;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.ITerm;

/**
 */
public class IsNotUnsignedShortBuiltinTest extends AbstractBooleanBuiltinTest {

    public IsNotUnsignedShortBuiltinTest(String name) {
        super(name);
    }

    public void testBuiltin() throws SecurityException, IllegalArgumentException, EvaluationException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String iri = "http://www.w3.org/2001/XMLSchema#UnsignedShort";
        String builtinName = IsNotUnsignedShortBuiltin.class.getName();
        ITerm term = CONCRETE.createUnsignedShort((short) 4232);
        checkBuiltin(iri, term, builtinName, IsNotDecimalBuiltin.class.getName(), IsNotIntegerBuiltin.class.getName(), IsNotUnsignedIntBuiltin.class.getName(), IsNotUnsignedLongBuiltin.class.getName(), IsNotNonNegativeIntegerBuiltin.class.getName(), IsNotNumericBuiltin.class.getName());
    }
}
