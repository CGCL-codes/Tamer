package polyglot.parse;

import polyglot.ast.*;
import polyglot.util.*;

/**
 * Encapsulates some of the data in a variable declaration.  Used only by the parser.
 */
public class VarDeclarator {

    public Position pos;

    public Id name;

    public int dims;

    public Expr init;

    public VarDeclarator(Position pos, Id name) {
        this.pos = pos;
        this.name = name;
        this.dims = 0;
        this.init = null;
    }

    public Position position() {
        return pos;
    }
}
