package org.owasp.orizon.mirage.java.parser;

public class ExplicitConstructorInvocation extends BaseNode {

    public ExplicitConstructorInvocation(int id) {
        super(id);
    }

    public ExplicitConstructorInvocation() {
        super(JavaConstants.EXPLICITCONSTRUCTORINVOCATION);
    }
}
