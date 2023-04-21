package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.CONCRETE;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.ITerm;

/**
 */
public class IsNotNegativeIntegerBuiltinTest extends AbstractBooleanBuiltinTest {

    public IsNotNegativeIntegerBuiltinTest(String name) {
        super(name);
    }

    public void testBuiltin() throws SecurityException, IllegalArgumentException, EvaluationException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String iri = "http://www.w3.org/2001/XMLSchema#NegativeInteger";
        String builtinName = IsNotNegativeIntegerBuiltin.class.getName();
        ITerm term = CONCRETE.createNegativeInteger(BigInteger.valueOf((long) (-2435234)));
        checkBuiltin(iri, term, builtinName, IsNotDecimalBuiltin.class.getName(), IsNotIntegerBuiltin.class.getName(), IsNotNonPositiveIntegerBuiltin.class.getName(), IsNotNumericBuiltin.class.getName());
    }
}
