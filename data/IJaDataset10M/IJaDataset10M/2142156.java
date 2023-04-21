package org.jmlspecs.jir.jdt.dom.jc.test.source;

import org.jmlspecs.javacontract.JC;

public class RequiresONENull {

    public static void m(final Object o) {
        JC.spec(JC.requires(o != null));
    }
}
