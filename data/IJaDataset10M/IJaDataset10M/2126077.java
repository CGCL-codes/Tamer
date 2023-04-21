package abc.aspectj.ast;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.ext.jl.ast.*;
import java.util.*;

/**
 * @author Julian Tibble
 */
public class BeforeSymbol_c extends Node_c implements SymbolKind {

    public BeforeSymbol_c(Position pos) {
        super(pos);
    }

    public String kind() {
        return BEFORE;
    }

    public Collection binds() {
        return new HashSet();
    }

    public AdviceSpec generateAdviceSpec(AJNodeFactory nf, List formals, TypeNode voidn) {
        return nf.Before(position(), formals, voidn);
    }

    public List aroundVars() {
        return null;
    }
}
