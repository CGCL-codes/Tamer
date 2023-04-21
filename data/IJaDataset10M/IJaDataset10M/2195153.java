package polyglot.ext.jl5.ast;

import java.util.List;
import polyglot.ast.ConstructorCall;

public interface JL5ConstructorCall extends ConstructorCall {

    List typeArguments();

    JL5ConstructorCall typeArguments(List args);
}
