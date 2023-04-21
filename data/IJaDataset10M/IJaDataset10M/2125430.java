package org.mandarax.compiler.defaultImpl.velocity.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.mandarax.compiler.CompilerException;
import org.mandarax.kernel.Fact;
import org.mandarax.kernel.Term;

public class CodeUtil {

    public static String getObjectRefCode(Class type, Object obj) throws CompilerException {
        if (type == String.class) {
            return "\"" + obj + "\"";
        } else {
            throw new CompilerException("Don't know how to reference this object: " + obj);
        }
    }

    public static boolean[] buildInputParams(Fact f, Map<Term, String> bindings, List<String> params) {
        return null;
    }
}
