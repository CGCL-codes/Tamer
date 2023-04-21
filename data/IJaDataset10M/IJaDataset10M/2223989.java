package org.kumenya.ide.annotator;

import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.kumenya.ide.psi.CQLFileBase;
import org.kumenya.ide.psi.api.*;
import org.kumenya.ide.psi.CQLPsiElement;
import org.kumenya.ide.psi.CQLElementVisitor;
import org.kumenya.ide.psi.impl.*;
import static org.kumenya.ide.lang.lexer.CQLTokenTypes.*;
import org.kumenya.ide.lang.lexer.CQLTokenTypes;
import org.kumenya.api.type.Type;
import org.kumenya.api.type.Types;
import static org.kumenya.api.type.TypeTag.*;
import static org.kumenya.api.type.TypeTag.LONG;
import static org.kumenya.api.type.Type.type;
import java.util.*;

/**
 * todo: code from this class is almost the same than org.kumenya.compiler.phases.TypeChecker
 *       see if we could reuse it.
 *
 * @author Jean Morissette
 */
public class CQLAnnotator implements Annotator {

    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof CQLFileBase) {
            new TypeChecker().checkType((CQLPsiElement) element, holder);
        }
    }

    private static class Context {

        @NotNull
        AnnotationHolder holder;

        Map<String, CQLPsiElement> vars = new HashMap();

        Context(AnnotationHolder holder) {
            this.holder = holder;
        }

        Context copy() {
            Context ctx = new Context(holder);
            ctx.vars = new HashMap(vars);
            return ctx;
        }
    }

    private static final Type ERROR = type(ILLEGAL);

    /**
     * If the type of an operand is illegal with respect to its operator,
     * an error annotation is created locally and an error type is propagated up the tree.
     *
     */
    private static class TypeChecker extends CQLElementVisitor<Type, Context> {

        void checkType(CQLPsiElement element, AnnotationHolder holder) {
            Context ctx = new Context(holder);
            checkType(element, ctx);
        }

        Type checkType(CQLPsiElement element, Context ctx) {
            if (element == null) {
                return ERROR;
            }
            Type type = element.accept(this, ctx);
            element.setType(type);
            return type;
        }

        public Type visitStatement(CQLStatement element, Context ctx) {
            return checkType(element.getChild(), ctx);
        }

        public Type visitLiteral(CQLLiteral element, Context ctx) {
            IElementType et = element.getNode().getElementType();
            if (et.equals(INTEGER_LITERAL)) {
                return type(INTEGER);
            } else if (et.equals(LONG_LITERAL)) {
                return type(LONG);
            } else if (et.equals(LONG_LITERAL)) {
                return type(LONG);
            } else if (et.equals(FLOAT_LITERAL)) {
                return type(FLOAT);
            } else if (et.equals(DOUBLE_LITERAL)) {
                return type(DOUBLE);
            } else if (et.equals(CHARACTER_LITERAL)) {
                return type(CHAR);
            } else if (et.equals(STRING_LITERAL)) {
                return type(STRING);
            } else if (et.equals(TRUE_KEYWORD) || et.equals(FALSE_KEYWORD)) {
                return type(BOOLEAN);
            } else if (et.equals(NULL_KEYWORD)) {
                return type(NULL);
            }
            return ERROR;
        }

        public Type visitSelectStatement(CQLSelectStatement element, Context ctx) {
            List<CQLRangeVarImpl> rangeVars = element.getRangeVars();
            for (CQLRangeVarImpl rangeVar : rangeVars) {
                String var = rangeVar.getVar();
                checkType(rangeVar, ctx.copy());
                ctx.vars.put(var, rangeVar);
            }
            ASTNode where = element.getNode().findChildByType(CQLTokenTypes.WHERE_KEYWORD);
            if (where != null) {
                CQLExpression whereOperand = ((CQLWhereCondition) where.getPsi()).getOperand();
                Type t = checkType(whereOperand, ctx);
                if (!t.isErroneous() && t.tag != BOOLEAN && whereOperand != null) {
                    ctx.holder.createErrorAnnotation(whereOperand, "boolean expression expected");
                }
            }
            PsiElement groupBy = (PsiElement) element.getNode().findChildByType(CQLTokenTypes.GROUP_KEYWORD);
            if (groupBy != null) {
                for (PsiElement e : groupBy.getChildren()) {
                    CQLLabeledExpressionImpl le = (CQLLabeledExpressionImpl) e;
                    checkType(le, ctx);
                }
            }
            List<String> labels = new ArrayList();
            List<Type> members = new ArrayList();
            List<CQLLabeledExpressionImpl> projection = element.getProjection();
            for (CQLLabeledExpressionImpl le : projection) {
                CQLPsiElement expr = le.getExpression();
                if (expr == null) {
                    continue;
                }
                checkType(expr, ctx.copy());
                String label = le.getLabel();
                ctx.vars.put(label, expr);
                labels.add(label);
                members.add(expr.getType());
            }
            Type projectionType = type(STRUCT).of(labels, members);
            Type s = type(BAG).of(projectionType);
            return s;
        }

        public Type visitRelationalExpression(CQLRelationalExpression element, Context ctx) {
            Type recoveringType = ERROR;
            Type t = checkType(element.getLeftOperand(), ctx);
            Type s = checkType(element.getRightOperand(), ctx);
            if (t.isErroneous()) return recoveringType;
            if (s.isErroneous()) return recoveringType;
            if (!t.tag.isDescendantOf(ORDERABLE) || !s.tag.isDescendantOf(ORDERABLE)) {
                String op = element.getOperator().getText();
                error(element, ctx.holder, op, t, s);
                return recoveringType;
            }
            if (!Types.isSubtype(t, s) && !Types.isSubtype(s, t)) {
                String op = element.getOperator().getText();
                error(element, ctx.holder, op, t, s);
                return recoveringType;
            }
            boolean nullable = (t.isNullable() || s.isNullable());
            return type(BOOLEAN, nullable);
        }

        public Type visitUnaryArithmeticExpression(CQLUnaryArithmeticExpression element, Context ctx) {
            Type t = checkType(element.getOperand(), ctx);
            if (t.tag.isDescendantOf(NUMBER)) {
                return t;
            }
            return type(NUMBER);
        }

        public Type visitBinaryLogicalExpression(CQLBinaryLogicalExpression element, Context ctx) {
            Type recoveringType = ERROR;
            Type t1 = checkType(element.getLeftOperand(), ctx);
            Type t2 = checkType(element.getRightOperand(), ctx);
            if (t1.isErroneous()) return recoveringType;
            if (t2.isErroneous()) return recoveringType;
            if (!isBoolean(t1) || !isBoolean(t2)) {
                String op = element.getOperator().getText();
                error(element, ctx.holder, op, t1, t2);
                return recoveringType;
            }
            return t1.isNullable() ? t1 : t2;
        }

        public Type visitUnaryLogicalExpression(CQLUnaryLogicalExpressionImpl element, Context ctx) {
            Type t = checkType(element.getOperand(), ctx);
            if (t.isErroneous()) return ERROR;
            if (!isBoolean(t)) {
                String op = element.getOperator().getText();
                error(element, ctx.holder, op, t);
                return ERROR;
            }
            return t;
        }

        public Type visitEqualityExpression(CQLEqualityExpression element, Context ctx) {
            Type recoveringType = ERROR;
            Type t1 = checkType(element.getLeftOperand(), ctx);
            Type t2 = checkType(element.getRightOperand(), ctx);
            if (t1.isErroneous()) return recoveringType;
            if (t2.isErroneous()) return recoveringType;
            if (!Types.isSubtype(t1, t2) && !Types.isSubtype(t2, t1)) {
                String op = element.getOperator().getText();
                error(element, ctx.holder, op, t1, t2);
                return recoveringType;
            }
            return type(BOOLEAN);
        }

        public Type visitBinaryArithmetic(CQLBinaryArithmeticExpressionImpl element, Context ctx) {
            Type recoveringType = ERROR;
            Type t1 = checkType(element.getLeftOperand(), ctx);
            Type t2 = checkType(element.getRightOperand(), ctx);
            if (t1.isErroneous()) return recoveringType;
            if (t2.isErroneous()) return recoveringType;
            Type t3 = binaryNumericPromotion(t1, t2);
            if (t3.isErroneous()) {
                return recoveringType;
            }
            if (t1.isNullable() || t2.isNullable()) {
                t3 = toNullable(t3);
            }
            return t3;
        }

        public Type visitIdentifier(CQLIdentifierImpl element, Context ctx) {
            Type t;
            CQLPsiElement var = ctx.vars.get(element.getId());
            if (var == null) {
                t = ERROR;
                ctx.holder.createErrorAnnotation(element, "Cannot resolve identifer '" + element.getId() + "'");
            } else {
                element.setTarget(var);
                t = var.getType();
                if (t != null) {
                    t = t.getComponentType();
                } else {
                    t = ERROR;
                }
            }
            return t;
        }

        public Type visitFieldAccess(CQLFieldAccessImpl element, Context ctx) {
            Type t = checkType(element.getTarget(), ctx);
            if (t.isErroneous()) return t;
            Type s = t.getMemberType(element.getField());
            if (s == null) {
                ctx.holder.createErrorAnnotation(element, "'" + element.getField() + "' is not a column in tuple '" + element.getTarget() + "'");
                return type(ANY);
            }
            return s;
        }

        public Type visitFile(CQLFileBase element, Context ctx) {
            CQLStatement[] statements = element.getStatements();
            for (int i = 0; i < statements.length; i++) {
                CQLStatement stmt = statements[i];
                Type t = checkType(stmt, ctx);
                if (t.isErroneous()) return ERROR;
            }
            return type(VOID);
        }

        public Type visitOuputStreamDefinition(CQLOutputStreamDefinition element, Context ctx) {
            return checkType(element.getSelect(), ctx);
        }

        public Type visitInputStreamDefinition(CQLInputStreamDefinition element, Context ctx) {
            return Type.type(STRUCT).of(Arrays.asList("id", "a"), Arrays.asList(Type.type(INTEGER), Type.type(INTEGER)));
        }

        public Type visitIntermediateStreamDefinition(CQLIntermediateStreamDefinition element, Context ctx) {
            return checkType(element.getSelect(), ctx);
        }

        public Type visitRangeVar(CQLRangeVarImpl element, Context ctx) {
            System.out.println("CQLAnnotator$TypeChecker.visitRangeVar");
            Type t = checkType(element.getExpression(), ctx);
            Type s = t.getComponentType();
            System.out.println("s = " + s);
            return s;
        }
    }

    private static void error(PsiElement psiElement, AnnotationHolder holder, String opText, Type lhs, Type rhs) {
        holder.createErrorAnnotation(psiElement, "operator '" + opText + "' cannot be applied to '" + lhs + "', '" + rhs + "'");
    }

    private static void error(PsiElement psiElement, AnnotationHolder holder, String opText, Type operand) {
        holder.createErrorAnnotation(psiElement, "operator '" + opText + "' cannot be applied to '" + operand + "'");
    }

    private static boolean isBoolean(Type type) {
        return type.tag == BOOLEAN;
    }

    /**
     * If either operand is not a numeric type, an error type is returned.
     * Otherwise, if either operand is of type double, double is returned.
     * Otherwise, if either operand is of type float, float is returned.
     * Otherwise, if either operand is of type long, long is returned.
     * Otherwise, int is returned.
     * todo: code duplicate from org.kumenya.compiler.phases.TypeChecker
     */
    private static Type binaryNumericPromotion(Type t, Type s) {
        if (!t.tag.isDescendantOf(NUMBER) || !s.tag.isDescendantOf(NUMBER)) {
            return ERROR;
        }
        return t.tag == DOUBLE ? t : s.tag == DOUBLE ? s : t.tag == FLOAT ? t : s.tag == FLOAT ? s : t.tag == LONG ? t : s.tag == LONG ? s : type(INTEGER);
    }

    private static Type toNullable(Type t) {
        if (!t.isNullable()) {
            t = t.clone();
            t.setNullable(true);
        }
        return t;
    }
}
