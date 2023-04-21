package org.incava.java;

import java.io.*;
import java.util.*;

public interface JavaParser {

    public Token getToken(int n);

    /**
     * Returns t.kind, essentially, except that it accounts for the difference
     * between the 1.3 and 1.4 grammars and lists of constants. That is, for
     * 1.3, the "assert" constant is accounted for.
     */
    public int getTokenKind(Token t);

    /**
     * Returns the compilation unit.
     */
    public ASTCompilationUnit CompilationUnit() throws ParseException;

    /**
     * Resets the parser.
     */
    public void ReInit(InputStream stream);
}
