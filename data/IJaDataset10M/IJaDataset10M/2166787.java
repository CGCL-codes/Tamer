package org.jmlspecs.jml6.core.bytecode.jirrresolver;

import org.eclipse.jdt.core.dom.Expression;

/**
 * Handles expressions for which a resolver is not specified
 * @author e_hotes
 *
 */
public class DefaultResolver implements IJirResolver {

    /**
	 * @see IJirResolver.resolve
	 */
    @Override
    public Expression resolve(Expression e) {
        Expression e2 = (Expression) e.getAST().createInstance(e.getNodeType());
        return (Expression) Expression.copySubtree(e2.getAST(), e);
    }
}
