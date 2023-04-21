package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.CONCRETE;
import java.lang.reflect.InvocationTargetException;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.ITerm;

public class IsDateTimeStampBuiltinTest extends AbstractBooleanBuiltinTest {

    public IsDateTimeStampBuiltinTest(String name) {
        super(name);
    }

    public void testBuiltin() throws SecurityException, IllegalArgumentException, EvaluationException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String iri = "http://www.w3.org/2001/XMLSchema#DateTimeStamp";
        String builtinName = IsDateTimeStampBuiltin.class.getName();
        ITerm term = CONCRETE.createDateTimeStamp(2010, 10, 6, 3, 23, 0.0, 0, 0);
        checkBuiltin(iri, term, builtinName, IsDateTimeBuiltin.class.getName());
    }
}
