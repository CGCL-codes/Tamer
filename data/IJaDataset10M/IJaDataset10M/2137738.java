package kinky.parsers.idl.ast;

import kinky.parsers.idl.ast.visitor.GenericVisitor;
import kinky.parsers.idl.ast.visitor.VoidVisitor;

/**
 * @author pfigueiredo
 * 
 */
public class ConstantDeclaration extends Declaration {

    /**
	 * @param line
	 * @param column
	 */
    public ConstantDeclaration(int line, int column) {
        super(line, column);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }
}
