package org.exist.xquery;

import java.util.Iterator;
import java.util.List;

/**
 * An {@link org.exist.xquery.ExpressionVisitor} which traverses the entire
 * expression tree. Methods may be overwritten by subclasses to filter out the
 * events they need.
 */
public class DefaultExpressionVisitor extends BasicExpressionVisitor {

    public void visitPathExpr(PathExpr expression) {
        for (int i = 0; i < expression.getLength(); i++) {
            Expression next = expression.getExpression(i);
            next.accept(this);
        }
    }

    public void visitUserFunction(UserDefinedFunction function) {
        if (function.getCaller().isRecursive()) return;
        function.getFunctionBody().accept(this);
    }

    public void visitBuiltinFunction(Function function) {
        for (int i = 0; i < function.getArgumentCount(); i++) {
            Expression arg = function.getArgument(i);
            arg.accept(this);
        }
    }

    public void visitForExpression(ForExpr forExpr) {
        forExpr.getInputSequence().accept(this);
        Expression where = forExpr.getWhereExpression();
        if (where != null) where.accept(this);
        forExpr.getReturnExpression().accept(this);
    }

    public void visitLetExpression(LetExpr letExpr) {
        letExpr.getInputSequence().accept(this);
        Expression where = letExpr.getWhereExpression();
        if (where != null) where.accept(this);
        letExpr.getReturnExpression().accept(this);
    }

    public void visitConditional(ConditionalExpression conditional) {
        conditional.getTestExpr().accept(this);
        conditional.getThenExpr().accept(this);
        conditional.getElseExpr().accept(this);
    }

    public void visitLocationStep(LocationStep locationStep) {
        List<Predicate> predicates = locationStep.getPredicates();
        for (Predicate pred : predicates) {
            pred.accept(this);
        }
    }

    public void visitPredicate(Predicate predicate) {
        predicate.getExpression(0).accept(this);
    }

    public void visitDocumentConstructor(DocumentConstructor constructor) {
        constructor.getContent().accept(this);
    }

    public void visitElementConstructor(ElementConstructor constructor) {
        constructor.getNameExpr().accept(this);
        if (constructor.getContent() != null) constructor.getContent().accept(this);
    }

    public void visitTextConstructor(DynamicTextConstructor constructor) {
        constructor.getContent().accept(this);
    }

    public void visitAttribConstructor(AttributeConstructor constructor) {
        for (Iterator<Object> i = constructor.contentIterator(); i.hasNext(); ) {
            Object next = i.next();
            if (next instanceof Expression) ((Expression) next).accept(this);
        }
    }

    public void visitAttribConstructor(DynamicAttributeConstructor constructor) {
        constructor.getNameExpr().accept(this);
        if (constructor.getContentExpr() != null) constructor.getContentExpr().accept(this);
    }

    public void visitUnionExpr(Union union) {
        union.left.accept(this);
        union.right.accept(this);
    }

    public void visitIntersectionExpr(Intersection intersect) {
        intersect.left.accept(this);
        intersect.right.accept(this);
    }

    @Override
    public void visitVariableDeclaration(VariableDeclaration decl) {
        decl.getExpression().accept(this);
    }
}
