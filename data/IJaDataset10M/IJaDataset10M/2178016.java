package net.sf.refactorit.refactorings.changesignature;

import net.sf.refactorit.classmodel.BinConstructor;
import net.sf.refactorit.classmodel.BinEnumConstant;
import net.sf.refactorit.classmodel.BinExpressionList;
import net.sf.refactorit.classmodel.BinItemVisitable;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinParameter;
import net.sf.refactorit.classmodel.expressions.BinConstructorInvocationExpression;
import net.sf.refactorit.classmodel.expressions.BinExpression;
import net.sf.refactorit.classmodel.expressions.BinNewExpression;
import net.sf.refactorit.classmodel.expressions.BinVariableUseExpression;
import net.sf.refactorit.classmodel.expressions.MethodOrConstructorInvocationExpression;
import net.sf.refactorit.classmodel.statements.BinStatementList;
import net.sf.refactorit.common.util.AppRegistry;
import net.sf.refactorit.parser.ASTImpl;
import net.sf.refactorit.parser.ASTUtil;
import net.sf.refactorit.parser.JavaTokenTypes;
import net.sf.refactorit.source.edit.Editor;
import net.sf.refactorit.source.edit.StringEraser;
import net.sf.refactorit.source.edit.StringInserter;
import net.sf.refactorit.source.format.FormatSettings;
import net.sf.refactorit.transformations.TransformationList;
import java.util.Iterator;
import java.util.List;

public class MethodInvocationEditor {

    private final MethodOrConstructorInvocationExpression x;

    private MethodSignatureChange change;

    private static int THIS_CALL = 1;

    private int SUPER_CALL = 2;

    public MethodInvocationEditor(MethodSignatureChange change, MethodOrConstructorInvocationExpression x) {
        this.x = x;
        this.change = change;
    }

    public void doEdit(final TransformationList transList) {
        int constructorCode = 0;
        MethodOrConstructorInvocationExpression invExpr = this.x;
        if (containsInSameMethodAsParameter(invExpr)) {
            if (ChangeMethodSignatureRefactoring.debug) {
                System.out.println("skipped editing of nested invocation " + invExpr);
            }
            return;
        }
        int startLine;
        int startColumn;
        int endLine;
        int endColumn;
        if (invExpr instanceof BinConstructorInvocationExpression && ((BinConstructorInvocationExpression) invExpr).isSynthetic()) {
            BinConstructorInvocationExpression ctExpr = (BinConstructorInvocationExpression) invExpr;
            invExpr = getNewExpressionForAnonymousCls(ctExpr);
        }
        if (invExpr != null) {
            BinExpressionList xList = invExpr.getExpressionList();
            if ((invExpr.getParent() instanceof BinEnumConstant) && (xList.getStartLine() < 0)) {
                startLine = endLine = invExpr.getEndLine();
                startColumn = endColumn = invExpr.getEndColumn() - 1;
            } else {
                BinExpression[] exprs = xList.getExpressions();
                startLine = xList.getStartLine();
                startColumn = xList.getStartColumn() - 1;
                endLine = xList.getEndLine();
                endColumn = xList.getEndColumn() - 2;
                if (exprs != null && exprs.length > 0) {
                    Editor eraseAllParameters = new StringEraser(invExpr.getCompilationUnit(), startLine, startColumn, endLine, endColumn);
                    transList.add(eraseAllParameters);
                }
                if ((invExpr.getParent() instanceof BinEnumConstant)) {
                    ASTImpl enumAst = ASTUtil.getFirstChildOfType(xList.getRootAst().getParent(), JavaTokenTypes.IDENT);
                    Editor eraseEnumConstantBrackets = new StringEraser(invExpr.getCompilationUnit(), enumAst.getEndLine(), enumAst.getEndColumn() - 1, xList.getEndLine(), xList.getEndColumn() - 1);
                    transList.add(eraseEnumConstantBrackets);
                }
            }
        } else {
            invExpr = x;
            BinConstructorInvocationExpression ctExpr = (BinConstructorInvocationExpression) x;
            if (ctExpr.getOwner().equals(change.getMethod().getOwner())) {
                constructorCode = THIS_CALL;
            } else {
                constructorCode = SUPER_CALL;
            }
            if (!(ctExpr.getParent() instanceof BinConstructor)) {
                AppRegistry.getLogger(this.getClass()).debug("constructor creating case not implemented");
                return;
            }
            BinConstructor ctrCalledFrom = (BinConstructor) ctExpr.getParent();
            BinStatementList bodySt = ctrCalledFrom.getBody();
            startLine = bodySt.getStartLine();
            startColumn = bodySt.getStartColumn();
            endLine = bodySt.getEndLine();
            String emptySpace = FormatSettings.LINEBREAK + FormatSettings.getIndentString(ctrCalledFrom.getIndent() + FormatSettings.getBlockIndent());
            String ctrName = emptySpace + (constructorCode == THIS_CALL ? "this" : "super") + '(';
            transList.add(new StringInserter(x.getCompilationUnit(), startLine, startColumn, ctrName));
        }
        if (ChangeMethodSignatureRefactoring.debug) {
            System.out.println("editing invocation: " + invExpr);
            System.out.println("lines:" + startLine + ", " + endLine);
        }
        String invContent = getChangedInvocationContent(invExpr);
        if ((invContent.length() > 0) && (invExpr.getParent() instanceof BinEnumConstant)) {
            invContent = '(' + invContent + ')';
        }
        if (constructorCode == SUPER_CALL || constructorCode == THIS_CALL) {
            invContent += ");";
        }
        Editor parameterInserter = new StringInserter(invExpr.getCompilationUnit(), startLine, startColumn, invContent);
        transList.add(parameterInserter);
    }

    /**
   * @param invExpr
   */
    private MethodOrConstructorInvocationExpression getNewExpressionForAnonymousCls(BinConstructorInvocationExpression invExpr) {
        BinItemVisitable parent = invExpr.getParent();
        while (parent != null && !(parent instanceof BinNewExpression)) {
            parent = parent.getParent();
        }
        return (BinNewExpression) parent;
    }

    /**
   * @param exprs
   * @return content between method call brackets
   */
    private String getChangedInvocationContent(final MethodOrConstructorInvocationExpression x) {
        BinExpressionList xList = x.getExpressionList();
        BinExpression[] exprs = xList.getExpressions();
        final List changedParsList = change.getParametersList(this.x.getMethod());
        Iterator parIterator = changedParsList.iterator();
        boolean firstParameter = true;
        String exprContent;
        StringBuffer buffer = new StringBuffer();
        final boolean isNestedCall = x.getParentMember() instanceof BinMethod && change.isHierarchyMethod((BinMethod) x.getParentMember());
        while (parIterator.hasNext()) {
            ParameterInfo parameter = (ParameterInfo) parIterator.next();
            if (parameter instanceof NewParameterInfo) {
                if (isNestedCall) {
                    exprContent = parameter.getName();
                } else {
                    exprContent = ((NewParameterInfo) parameter).getDefaultValue(x.getParentMember());
                }
            } else {
                int oldParIndex = ((ExistingParameterInfo) parameter).getOriginalParameter().getIndex();
                exprContent = null;
                if (isNestedCall) {
                    if (exprs[oldParIndex] instanceof BinVariableUseExpression) {
                        BinVariableUseExpression varUseExpr = (BinVariableUseExpression) exprs[oldParIndex];
                        ExistingParameterInfo newPar = null;
                        if (varUseExpr.getVariable() instanceof BinParameter) {
                            newPar = getExistingParameterFromList(changedParsList, (BinParameter) varUseExpr.getVariable());
                        }
                        if (newPar != null) {
                            exprContent = newPar.getName();
                        }
                    }
                }
                if (exprContent == null) {
                    exprContent = getParameterExpressionContent(exprs[oldParIndex]);
                }
            }
            if (!firstParameter) {
                exprContent = ", " + exprContent;
            } else {
                firstParameter = false;
            }
            buffer.append(exprContent);
        }
        return buffer.toString();
    }

    /**
   * @param expr
   */
    private String getParameterExpressionContent(final BinExpression expr) {
        String exprContent = expr.getText();
        if (!containsSameMethodInvocations(expr)) {
            return exprContent;
        }
        final int lbracketIndex = exprContent.indexOf('(');
        String beforeBracket = exprContent.substring(0, lbracketIndex);
        final MethodOrConstructorInvocationExpression invokExpr = (MethodOrConstructorInvocationExpression) expr;
        BinExpressionList list = invokExpr.getExpressionList();
        String afterBracket = exprContent.substring(lbracketIndex + list.getEndPosition() - list.getStartPosition() + 1);
        StringBuffer result = new StringBuffer(beforeBracket + "(");
        if (invokExpr.getMethod() == x.getMethod()) {
            result.append(getChangedInvocationContent(invokExpr));
        } else {
            BinExpression exprs[] = list.getExpressions();
            for (int i = 0; i < exprs.length; i++) {
                if (i > 0) {
                    result.append(", ");
                }
                result.append(getParameterExpressionContent(exprs[i]));
            }
        }
        result.append(")" + afterBracket);
        return result.toString();
    }

    private boolean containsSameMethodInvocations(BinExpression expr) {
        if (expr instanceof MethodOrConstructorInvocationExpression) {
            final MethodOrConstructorInvocationExpression mcX = ((MethodOrConstructorInvocationExpression) expr);
            if (mcX.getMethod() == null) {
                return false;
            }
            if (mcX.getMethod() == x.getMethod()) {
                return true;
            }
            BinExpression[] exprs = mcX.getExpressionList().getExpressions();
            for (int i = 0; i < exprs.length; i++) {
                if (containsSameMethodInvocations(exprs[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    private static ExistingParameterInfo getExistingParameterFromList(List changedParsList, BinParameter binParameter) {
        for (int i = 0; i < changedParsList.size(); i++) {
            if (!(changedParsList.get(i) instanceof ExistingParameterInfo)) {
                continue;
            }
            ExistingParameterInfo info = (ExistingParameterInfo) changedParsList.get(i);
            if (info.getOriginalParameter() == binParameter) {
                return info;
            }
        }
        return null;
    }

    /**
   *
   * @param x
   * @return true if x is parameter for invocation where method is same
   */
    private static boolean containsInSameMethodAsParameter(MethodOrConstructorInvocationExpression x) {
        BinItemVisitable parent = x;
        while (parent.getParent() instanceof BinExpressionList) {
            parent = parent.getParent().getParent();
            if (parent instanceof MethodOrConstructorInvocationExpression) {
                if (((MethodOrConstructorInvocationExpression) parent).getMethod() == x.getMethod()) {
                    return true;
                }
            }
        }
        return false;
    }
}
