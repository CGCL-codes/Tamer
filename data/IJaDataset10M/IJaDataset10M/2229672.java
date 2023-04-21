package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.CONCRETE;
import java.lang.reflect.InvocationTargetException;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.ITerm;

/**
 */
public class IsNotNCNameBuiltinTest extends AbstractBooleanBuiltinTest {

    public IsNotNCNameBuiltinTest(String name) {
        super(name);
    }

    public void testBuiltin() throws SecurityException, IllegalArgumentException, EvaluationException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String iri = "http://www.w3.org/2001/XMLSchema#NCName";
        String builtinName = IsNotNCNameBuiltin.class.getName();
        ITerm term = CONCRETE.createNCName("NC name");
        checkBuiltin(iri, term, builtinName, IsNotNormalizedStringBuiltin.class.getName(), IsNotStringBuiltin.class.getName(), IsNotTokenBuiltin.class.getName(), IsNotNameBuiltin.class.getName());
    }
}
