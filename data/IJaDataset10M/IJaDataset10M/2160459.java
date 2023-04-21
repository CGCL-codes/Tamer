package org.jmlspecs.jml4.esc.gc.lang.expr;

import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.jmlspecs.jml4.esc.gc.CfgExpressionVisitor;
import org.jmlspecs.jml4.esc.vc.WlpVisitor;
import org.jmlspecs.jml4.esc.vc.lang.VC;

public class CfgFieldReference extends CfgAssignable {

    public final CfgExpression receiver;

    public final String field;

    public CfgFieldReference(CfgExpression receiver, String field, int incarnation, TypeBinding type, int sourceStart, int sourceEnd) {
        super(incarnation, type, sourceStart, sourceEnd);
        this.receiver = receiver;
        this.field = field;
    }

    public VC accept(WlpVisitor visitor) {
        return visitor.visit(this);
    }

    public CfgExpression accept(CfgExpressionVisitor visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return "[" + this.receiver + "." + this.field + "$" + this.incarnation() + "]";
    }

    public String getName() {
        return this.field;
    }

    public boolean isField() {
        return true;
    }

    public CfgAssignable withIncarnation(int newIncarnation) {
        return new CfgFieldReference(this.receiver, this.field, newIncarnation, this.type, sourceStart, sourceEnd);
    }
}
