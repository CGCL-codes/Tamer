package org.python.antlr.ast;

import org.python.antlr.PythonTree;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import java.io.DataOutputStream;
import java.io.IOException;

public class comprehensionType extends PythonTree {

    public exprType target;

    public exprType iter;

    public exprType[] ifs;

    public static final String[] _fields = new String[] { "target", "iter", "ifs" };

    public comprehensionType(Token token, exprType target, exprType iter, exprType[] ifs) {
        super(token);
        this.target = target;
        addChild(target);
        this.iter = iter;
        addChild(iter);
        this.ifs = ifs;
        if (ifs != null) {
            for (int iifs = 0; iifs < ifs.length; iifs++) {
                addChild(ifs[iifs]);
            }
        }
    }

    public comprehensionType(int ttype, Token token, exprType target, exprType iter, exprType[] ifs) {
        super(ttype, token);
        this.target = target;
        addChild(target);
        this.iter = iter;
        addChild(iter);
        this.ifs = ifs;
        if (ifs != null) {
            for (int iifs = 0; iifs < ifs.length; iifs++) {
                addChild(ifs[iifs]);
            }
        }
    }

    public comprehensionType(PythonTree tree, exprType target, exprType iter, exprType[] ifs) {
        super(tree);
        this.target = target;
        addChild(target);
        this.iter = iter;
        addChild(iter);
        this.ifs = ifs;
        if (ifs != null) {
            for (int iifs = 0; iifs < ifs.length; iifs++) {
                addChild(ifs[iifs]);
            }
        }
    }

    public String toString() {
        return "comprehension";
    }

    public String toStringTree() {
        StringBuffer sb = new StringBuffer("comprehension(");
        sb.append("target=");
        sb.append(dumpThis(target));
        sb.append(",");
        sb.append("iter=");
        sb.append(dumpThis(iter));
        sb.append(",");
        sb.append("ifs=");
        sb.append(dumpThis(ifs));
        sb.append(",");
        sb.append(")");
        return sb.toString();
    }

    public <R> R accept(VisitorIF<R> visitor) throws Exception {
        traverse(visitor);
        return null;
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (target != null) target.accept(visitor);
        if (iter != null) iter.accept(visitor);
        if (ifs != null) {
            for (int i = 0; i < ifs.length; i++) {
                if (ifs[i] != null) ifs[i].accept(visitor);
            }
        }
    }
}
