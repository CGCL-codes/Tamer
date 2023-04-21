package com.papyrus.alf2java.translator;

import java.util.ArrayList;
import com.papyrus.alf2java.parser.alfparser.ASTBaseExpression;
import com.papyrus.alf2java.parser.alfparser.ASTDocumentedStatement;
import com.papyrus.alf2java.parser.alfparser.ASTExpression;
import com.papyrus.alf2java.parser.alfparser.ASTInitializationExpression;
import com.papyrus.alf2java.parser.alfparser.ASTLocalNameDeclarationStatement;
import com.papyrus.alf2java.parser.alfparser.ASTLocalNameDeclarationStatementCompletion;
import com.papyrus.alf2java.parser.alfparser.ASTName;
import com.papyrus.alf2java.parser.alfparser.ASTNameBinding;
import com.papyrus.alf2java.parser.alfparser.ASTNonNamePostfixOrCastExpression;
import com.papyrus.alf2java.parser.alfparser.ASTNumber;
import com.papyrus.alf2java.parser.alfparser.ASTPostfixOrCastExpression;
import com.papyrus.alf2java.parser.alfparser.ASTQualifiedName;
import com.papyrus.alf2java.parser.alfparser.ASTStatement;
import com.papyrus.alf2java.parser.alfparser.ASTStatementSequence;
import com.papyrus.alf2java.parser.alfparser.ASTTypeName;
import com.papyrus.alf2java.parser.alfparser.ASTUnaryExpression;
import com.papyrus.alf2java.parser.alfparser.ASTUnqualifiedName;
import com.papyrus.alf2java.parser.alfparser.ASTeval;
import com.papyrus.alf2java.parser.alfparser.ASTexnil;
import com.papyrus.alf2java.parser.alfparser.GrammaireVisitor;
import com.papyrus.alf2java.parser.alfparser.Node;
import com.papyrus.alf2java.parser.alfparser.SimpleNode;

public class Translator implements GrammaireVisitor {

    private static Translator instance;

    private String tabInstructionJava;

    private SimpleNode rootNode;

    private String nomVarEnCour = "";

    public static Translator getInstance() {
        if (instance == null) return instance = new Translator(); else return instance;
    }

    public void translate(SimpleNode node) {
        this.tabInstructionJava = "";
        node.jjtAccept(this, null);
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        this.rootNode = node;
        return null;
    }

    @Override
    public Object visit(ASTeval node, Object data) {
        System.out.println("nbr de statement : " + node.jjtGetNumChildren());
        for (int i = 0; i < node.jjtGetNumChildren() - 1; i++) node.jjtGetChild(i).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTStatementSequence node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTDocumentedStatement node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTStatement node, Object data) {
        System.out.println("bipp" + node);
        node.jjtGetChild(0).jjtAccept(this, data);
        node.jjtGetChild(1).jjtAccept(this, data);
        this.tabInstructionJava += ";\n";
        System.out.println("et hop : " + this.tabInstructionJava);
        return null;
    }

    @Override
    public Object visit(ASTLocalNameDeclarationStatement node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        node.jjtGetChild(1).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTLocalNameDeclarationStatementCompletion node, Object data) {
        System.out.println("bipp" + node);
        this.tabInstructionJava += " = ";
        System.out.println("et hop : " + this.tabInstructionJava);
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTInitializationExpression node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTExpression node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTUnaryExpression node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTPostfixOrCastExpression node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTNonNamePostfixOrCastExpression node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTBaseExpression node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTNumber node, Object data) {
        System.out.println("bipp" + node);
        this.tabInstructionJava += Integer.toString(node.getValue());
        System.out.println("et hop : " + this.tabInstructionJava);
        return null;
    }

    @Override
    public Object visit(ASTTypeName node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTQualifiedName node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTUnqualifiedName node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTNameBinding node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, data);
        return null;
    }

    @Override
    public Object visit(ASTName node, Object data) {
        System.out.println("bipp" + node);
        String name = node.getValue();
        if (name.equals("Integer")) {
            this.tabInstructionJava += "int " + this.nomVarEnCour;
            System.out.println("et hop : " + this.tabInstructionJava);
            this.nomVarEnCour = "";
            return null;
        } else if (name.equals("Boolean")) {
            this.tabInstructionJava += "boolean " + this.nomVarEnCour;
            System.out.println("et hop : " + this.tabInstructionJava);
            this.nomVarEnCour = "";
            return null;
        } else {
            this.nomVarEnCour = name;
            return null;
        }
    }

    @Override
    public Object visit(ASTexnil node, Object data) {
        return null;
    }

    public String getProgJava() {
        System.out.println("et hop la sortie : " + this.tabInstructionJava);
        return this.tabInstructionJava;
    }
}
