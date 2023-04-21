package polyglot.ext.jl5.ast;

import java.util.List;
import polyglot.ast.AmbQualifierNode;

public interface JL5AmbQualifierNode extends AmbQualifierNode {

    List typeArguments();

    JL5AmbQualifierNode typeArguments(List args);
}
