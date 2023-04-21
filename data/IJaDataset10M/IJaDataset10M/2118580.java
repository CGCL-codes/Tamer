package org.jmlspecs.jml4.ast;

import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;

public class JmlArrayIndexRangeExpression extends Expression {

    public final Expression low;

    public final Expression high;

    public JmlArrayIndexRangeExpression(Expression lo, Expression hi) {
        this.low = lo;
        this.high = hi;
    }

    public TypeBinding resolveType(BlockScope scope) {
        if (this.low != null) {
            if (this.low.resolveTypeExpecting(scope, TypeBinding.INT) == null) return null;
        }
        if (this.high != null) {
            if (this.high.resolveTypeExpecting(scope, TypeBinding.INT) == null) return null;
        }
        return TypeBinding.INT;
    }

    public StringBuffer printExpression(int indent, StringBuffer output) {
        output.append(this.low);
        output.append("..");
        output.append(this.high);
        return output;
    }
}
