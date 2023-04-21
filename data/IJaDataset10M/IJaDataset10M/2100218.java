package org.jmlspecs.jml4.esc.gc.lang.expr;

import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.jmlspecs.jml4.esc.gc.CfgExpressionVisitor;
import org.jmlspecs.jml4.esc.vc.WlpVisitor;
import org.jmlspecs.jml4.esc.vc.lang.VC;

public class CfgArrayAllocationExpression extends CfgExpression {

    public final int[] ids;

    public final CfgExpression[] dims;

    public CfgArrayAllocationExpression(int[] ids, CfgExpression[] dims, TypeBinding type, int sourceStart, int sourceEnd) {
        super(type, sourceStart, sourceEnd);
        this.ids = ids;
        this.dims = dims;
    }

    public VC accept(WlpVisitor visitor) {
        return visitor.visit(this);
    }

    public CfgExpression accept(CfgExpressionVisitor visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        String sType = this.type.debugName();
        String sDims = getDimsAsString();
        return "new " + sType + sDims;
    }

    private String getDimsAsString() {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < this.dims.length; i++) {
            result.append("[" + this.dims[i] + "]");
        }
        return result.toString();
    }
}
