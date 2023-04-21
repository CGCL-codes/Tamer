package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.lang.codegen.noqres.QueryCodeGenNoQres;
import pl.wcislo.sbql4j.lang.codegen.nostacks.QueryCodeGenNoStacks;
import pl.wcislo.sbql4j.lang.codegen.simple.QueryCodeGenSimple;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.terminals.Operator;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.lang.tree.visitors.OperatorVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TypeChecker;
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorUnion extends Operator {

    public OperatorUnion(OperatorType type) {
        super(type);
    }

    @Override
    public <T, V extends TreeVisitor> T accept(OperatorVisitor<T, V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
        return opVisitor.visitUnion(this, treeVisitor, opExpr, subExprs);
    }

    ;

    @Override
    public void eval(Interpreter interpreter, Expression... args) {
        Bag eres = new Bag();
        CollectionResult eRight = Utils.objectToCollection(interpreter.getQres().pop());
        CollectionResult eLeft = Utils.objectToCollection(interpreter.getQres().pop());
        eres.addAll(eLeft);
        eres.addAll(eRight);
        interpreter.getQres().push(eres);
    }

    @Override
    public void evalStatic(TypeChecker checker, Expression... args) {
        Expression leftExpr = args[0];
        Expression rightExpr = args[1];
        Expression opExpr = args[2];
        ClassTypes cTypes = ClassTypes.getInstance();
        Type leftType = ((ValueSignature) leftExpr.getSignature()).type;
        Type rightType = ((ValueSignature) rightExpr.getSignature()).type;
        Type returnType = cTypes.getSharedAncestor(leftType, rightType);
        ValueSignature vsig = JavaSignatureAbstractFactory.getJavaSignatureFactory().createJavaSignature(returnType);
        SCollectionType cType;
        SCollectionType leftCType = leftExpr.getSignature().sColType;
        SCollectionType rightCType = rightExpr.getSignature().sColType;
        cType = SCollectionType.getUnionSCType(leftCType, rightCType);
        vsig.sColType = cType;
        opExpr.setSignature(vsig);
    }
}
