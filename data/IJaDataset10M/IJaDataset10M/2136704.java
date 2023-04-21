package com.judoscript.jamaica.parser;

import java.util.Set;
import java.util.HashSet;
import com.judoscript.jamaica.JavaClassCreator;
import com.judoscript.jamaica.JavaClassCreatorException;

public class JamaicaVerifyVisitor extends JamaicaVisitorBase {

    int errCnt = 0;

    StringBuffer sb = new StringBuffer();

    HashSet<String> fields = new HashSet<String>();

    HashSet<String> methods = new HashSet<String>();

    HashSet<String> locals = new HashSet<String>();

    HashSet<String> labels = new HashSet<String>();

    public Object visit(ASTClassDeclaration node, Object data) throws Exception {
        int len = node.jjtGetNumChildren();
        for (int i = 0; i < len; ++i) {
            SimpleNode n = (SimpleNode) node.jjtGetChild(i);
            if (n instanceof ASTVariableDeclarator) checkConflict("Field", ((ASTVariableDeclarator) n).getName(), fields, n);
        }
        node.childrenAccept(this, data);
        fields.clear();
        methods.clear();
        if (errCnt > 0) throw new JavaClassCreatorException((errCnt > 100 ? "More than " : "") + errCnt + " errors:\n" + sb.toString());
        return null;
    }

    public Object visit(ASTMethodDeclaration node, Object data) throws Exception {
        String s = node.getName() + node.getParameterString();
        checkConflict("Method", s, methods, node);
        int len = node.jjtGetNumChildren();
        for (int i = 0; i < len; ++i) {
            SimpleNode n = (SimpleNode) node.jjtGetChild(i);
            if (n instanceof ASTLabel) checkConflict("Label", ((ASTLabel) n).getName(), labels, n);
        }
        node.childrenAccept(this, data);
        locals.clear();
        labels.clear();
        return null;
    }

    public Object visit(ASTVariableDeclarator node, Object data) throws Exception {
        String fld = node.getName();
        Node parent = node.jjtGetParent();
        if (parent instanceof ASTFormalParameters || parent instanceof ASTMethodDeclaration) checkConflict("Variable", fld, locals, node);
        return null;
    }

    public Object visit(ASTCatchClause node, Object data) throws Exception {
        String exception = node.getException();
        checkLabel(node.getStartLabel(), node.getStartLabelLineNum());
        checkLabel(node.getEndLabel(), node.getEndLabelLineNum());
        checkLabel(node.getActionLabel(), node.getActionLabelLineNum());
        return null;
    }

    public Object visit(ASTLabel node, Object data) throws Exception {
        return null;
    }

    public Object visit(ASTCodeSimple node, Object data) throws Exception {
        return null;
    }

    public Object visit(ASTCodeWithText node, Object data) throws Exception {
        switch(node.getOpcode()) {
            case Constants.IFEQ:
            case Constants.IFNE:
            case Constants.IFLT:
            case Constants.IFGE:
            case Constants.IFGT:
            case Constants.IFLE:
            case Constants.IF_ICMPEQ:
            case Constants.IF_ICMPNE:
            case Constants.IF_ICMPLT:
            case Constants.IF_ICMPGE:
            case Constants.IF_ICMPGT:
            case Constants.IF_ICMPLE:
            case Constants.IF_ACMPEQ:
            case Constants.IF_ACMPNE:
            case Constants.GOTO:
            case Constants.JSR:
            case Constants.IFNULL:
            case Constants.IFNONNULL:
            case Constants.GOTO_W:
            case Constants.JSR_W:
                checkLabel(node.getText(), node.getLineNum());
                break;
            case Constants.ILOAD:
            case Constants.LLOAD:
            case Constants.FLOAD:
            case Constants.DLOAD:
            case Constants.ALOAD:
            case Constants.ISTORE:
            case Constants.LSTORE:
            case Constants.FSTORE:
            case Constants.DSTORE:
            case Constants.ASTORE:
            case Constants.RET:
                checkExistence("Variable", node.getText(), locals, node.getLineNum());
                break;
        }
        return null;
    }

    public Object visit(ASTCodeWithTextInt node, Object data) throws Exception {
        if (node.getOpcode() == 132) checkExistence("Field", node.getText(), fields, node.getLineNum());
        return null;
    }

    public Object visit(ASTCodeWithConstant node, Object data) throws Exception {
        return null;
    }

    public Object visit(ASTCodeMemberAccess node, Object data) throws Exception {
        if (node.isSelfAccess()) checkExistence("Field", node.getName(), fields, node.getLineNum());
        return null;
    }

    public Object visit(ASTCodeInvoke node, Object data) throws Exception {
        return null;
    }

    public Object visit(ASTCodeSwitch node, Object data) throws Exception {
        checkLabel(node.getDefault(), node.getLineNum());
        String[] caseLabels = (String[]) node.getCases()[1];
        for (int i = caseLabels.length - 1; i >= 0; --i) checkLabel(caseLabels[i], node.getCaseLabelLineNum(caseLabels[i]));
        return null;
    }

    public Object visit(ASTMacroSet node, Object data) throws Exception {
        checkVariableField(node);
        Object val = node.getValue();
        if (val != null && val instanceof ASTMacroBase) checkVariableField((ASTMacroBase) val);
        return null;
    }

    public Object visit(ASTMacroPrint node, Object data) throws Exception {
        checkVariableField(node);
        return null;
    }

    public Object visit(ASTMacroObject node, Object data) throws Exception {
        checkVariableField(node);
        return null;
    }

    public Object visit(ASTMacroArray node, Object data) throws Exception {
        checkVariableField(node);
        return null;
    }

    public Object visit(ASTMacroStringConcat node, Object data) throws Exception {
        checkVariableField(node);
        return null;
    }

    public Object visit(ASTMacroIterate node, Object data) throws Exception {
        if (!node.isEnd()) {
            checkVariableField(node.getCollection());
            String s = node.getIterateVar();
            if (s != null) checkVariableField(s, node.getIterateVarLine());
        }
        return null;
    }

    public Object visit(ASTMacroIf node, Object data) throws Exception {
        checkVariableField(node);
        return null;
    }

    final void checkVariableField(String name, int line) {
        if (!locals.contains(name) && !fields.contains(name)) addError("Variable/field '" + name + "' at line " + line + " is not defined.");
    }

    final void checkVariableField(ASTMacroBase macro) {
        for (JavaClassCreator.VarAccess var : macro.getAllVariables()) checkVariableField(var);
    }

    final void checkVariableField(JavaClassCreator.VarAccess var) {
        if (var == null) return;
        checkVariableField(var.name, var.line);
        if (var.index != null) {
            if (var.index instanceof JavaClassCreator.VarAccess) {
                checkVariableField((JavaClassCreator.VarAccess) var.index);
            } else if (var.index instanceof Object[]) {
                Object[] oa = (Object[]) var.index;
                for (int i = 0; i < oa.length; ++i) if (oa[i] instanceof JavaClassCreator.VarAccess) checkVariableField((JavaClassCreator.VarAccess) oa[i]);
            }
        }
    }

    final void checkConflict(String type, String value, Set<String> set, SimpleNode node) {
        if (set.contains(value)) addError(type + " '" + value + "' at line " + node.getLineNum() + " is already defined."); else set.add(value);
    }

    final void checkExistence(String type, String value, Set<String> set, int line) {
        if (!set.contains(value) && !value.equals("this")) addError(type + " '" + value + "' at line " + line + " is not defined.");
    }

    final void checkLabel(String label, int line) {
        checkExistence("Label", label, labels, line);
    }

    final void addError(String msg) {
        if (errCnt > 100) return;
        ++errCnt;
        sb.append("  ");
        sb.append(msg);
        sb.append('\n');
    }
}
