package MScheme.exceptions;

import MScheme.values.Symbol;

public class UnexpectedSyntax extends CompileError {

    public static final String id = "$Id: UnexpectedSyntax.java 309 2001-09-06 11:50:20Z sielenk $";

    public UnexpectedSyntax(Symbol cause) {
        super(cause);
    }
}
