package abc.aspectj.ast;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import java.util.*;

/**
 * @author Julian Tibble
 */
public interface SymbolDecl extends Node {

    String name();

    Pointcut getPointcut();

    String kind();

    public SymbolKind getSymbolKind();

    Collection binds();

    public Block body(AJNodeFactory nf, String debug_msg, TypeNode ret_type);

    AdviceDecl generateSymbolAdvice(AJNodeFactory nf, List formals, TypeNode voidn, String tm_id, Position tm_pos);

    Pointcut generateClosedPointcut(AJNodeFactory nf, List formals);

    List aroundVars();
}
