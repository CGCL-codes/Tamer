package polyglot.ext.jl5.ast;

import polyglot.ast.Block;
import polyglot.ast.Formal;
import polyglot.ast.Node;
import polyglot.ast.Catch_c;
import polyglot.ext.jl5.types.ParameterizedType;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;

public class JL5Catch_c extends Catch_c implements JL5Catch {

    public JL5Catch_c(Position pos, Formal formal, Block body) {
        super(pos, formal, body);
    }

    public Node typeCheck(ContextVisitor tc) throws SemanticException {
        Type t = formal.type().type();
        if (t instanceof ParameterizedType) {
            throw new SemanticException("Cannot have a parameterized type for a catch formal", formal.position());
        } else if (t instanceof TypeVariable) {
            throw new SemanticException("Cannot have a type variable for a catch formal", formal.position());
        }
        return super.typeCheck(tc);
    }
}
