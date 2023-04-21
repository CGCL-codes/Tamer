package pl.wcislo.sbql4j.lang.optimiser.coderewrite.db4oindex;

import java.util.List;
import pl.wcislo.sbql4j.java.model.compiletime.BindResult;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.StaticEVNSType;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class Db4oIndexInvocationExpression extends NameExpression {

    private final NameExpression toReplace;

    private final Expression paramExpression;

    private final String indexedFieldName;

    public Db4oIndexInvocationExpression(int pos, NameExpression toReplace, Expression paramExpression, String indexedFieldName) {
        super(pos, toReplace.l);
        this.toReplace = toReplace;
        this.fullName = toReplace.fullName;
        this.boundGetterMethodAsIdentifier = toReplace.boundGetterMethodAsIdentifier;
        this.paramExpression = paramExpression;
        this.indexedFieldName = indexedFieldName;
    }

    public Expression getParamExpression() {
        return paramExpression;
    }

    public String getIndexedFieldName() {
        return indexedFieldName;
    }

    public Signature getSignature() {
        return toReplace.getSignature();
    }

    public void addBindResult(BindResult res) {
        toReplace.addBindResult(res);
    }

    public boolean equals(Object arg0) {
        return toReplace.equals(arg0);
    }

    public StaticEVNSType getBoundSignature() {
        return toReplace.getBoundSignature();
    }

    public List<BindResult> getBindResults() {
        return toReplace.getBindResults();
    }

    public int hashCode() {
        return toReplace.hashCode();
    }

    public String toString() {
        return toReplace.toString();
    }

    public void setSignature(Signature signature) {
        toReplace.setSignature(signature);
    }

    public void setBoundSignature(StaticEVNSType b) {
        toReplace.setBoundSignature(b);
    }

    @Override
    public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
    }

    @Override
    public Object accept(TreeVisitor visitor, Object object) {
        return visitor.visitNameExpression(this, object);
    }
}
