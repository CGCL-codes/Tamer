package kpython.backend.ir.tree;

import java.util.LinkedList;
import java.util.List;
import kpython.ASTAndExpr;
import kpython.ASTArgumentList;
import kpython.ASTAssignmentStmt;
import kpython.ASTAtom;
import kpython.ASTBreakStmt;
import kpython.ASTCallFunction;
import kpython.ASTCallList;
import kpython.ASTCompOper;
import kpython.ASTComparison;
import kpython.ASTCompoundStmt;
import kpython.ASTCondExpr;
import kpython.ASTContinueStmt;
import kpython.ASTDefFunction;
import kpython.ASTElif;
import kpython.ASTElse;
import kpython.ASTExpr;
import kpython.ASTFactorOper;
import kpython.ASTFor;
import kpython.ASTForStmt;
import kpython.ASTIdentifier;
import kpython.ASTIf;
import kpython.ASTIfStmt;
import kpython.ASTList;
import kpython.ASTListDisplay;
import kpython.ASTLiteral;
import kpython.ASTLiteralFloat;
import kpython.ASTLiteralInteger;
import kpython.ASTLiteralString;
import kpython.ASTNotExpr;
import kpython.ASTOrExpr;
import kpython.ASTParameterList;
import kpython.ASTPrimary;
import kpython.ASTPrintStmt;
import kpython.ASTReturnStmt;
import kpython.ASTSimpleStmt;
import kpython.ASTStart;
import kpython.ASTStmt;
import kpython.ASTSuite;
import kpython.ASTTermOper;
import kpython.ASTWhile;
import kpython.ASTWhileStmt;
import kpython.ParserVisitor;
import kpython.SimpleNode;

public class IRTreeBuilder implements ParserVisitor {

    private IRNode root = null;

    private int counter = 1;

    @Override
    public IRNode visit(SimpleNode node, Object data) {
        return null;
    }

    /**
	 * List Display ?
	 */
    @Override
    public AtomList visit(ASTListDisplay node, Object data) {
        return (AtomList) node.jjtGetChild(0).jjtAccept(this, data);
    }

    /**
	 * Literal
	 */
    @Override
    public Atom visit(ASTLiteral node, Object data) {
        return (Atom) node.jjtGetChild(0).jjtAccept(this, data);
    }

    /**
	 * Identificador
	 */
    @Override
    public Object visit(ASTIdentifier node, Object data) {
        System.out.println("Identifier: " + node.getSymbol());
        if ((node.jjtGetParent() instanceof ASTDefFunction)) {
            Label label = new Label();
            label.label = node.getSymbol();
            return label;
        } else if ((node.jjtGetParent() instanceof ASTCallFunction)) {
            return node.getSymbol();
        } else {
            Atom atom = new Atom();
            atom.type = Atom.var;
            atom.atom = node.getSymbol();
            return atom;
        }
    }

    /**
	 * Literal float
	 */
    @Override
    public Atom visit(ASTLiteralFloat node, Object data) {
        System.out.println("Float :" + node.getValue());
        Atom atom = new Atom();
        atom.type = Atom.nfloat;
        atom.atom = node.getValue();
        return atom;
    }

    /**
	 * Literal Integer
	 */
    @Override
    public Atom visit(ASTLiteralInteger node, Object data) {
        System.out.println("Integer: " + node.getValue());
        Atom atom = new Atom();
        atom.type = Atom.ninteger;
        atom.atom = node.getValue();
        return atom;
    }

    /**
	 * Literal String
	 */
    @Override
    public Atom visit(ASTLiteralString node, Object data) {
        System.out.println("String: " + node.getValue());
        Atom atom = new Atom();
        atom.type = Atom.string;
        atom.atom = node.getValue();
        return atom;
    }

    /**
	 * Atom - meio abstracto
	 */
    @Override
    public Expr visit(ASTAtom node, Object data) {
        System.out.println("ATOM");
        return (Expr) node.jjtGetChild(0).jjtAccept(this, data);
    }

    /**
	 * A lista de argumentos de uma função
	 */
    @Override
    public List<Expr> visit(ASTArgumentList node, Object data) {
        System.out.println("ARGUMENT LIST");
        List<Expr> args = new LinkedList<Expr>();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) args.add((Expr) node.jjtGetChild(i).jjtAccept(this, data));
        return args;
    }

    /**
	 * Chamada de uma função
	 */
    @Override
    public CallBlock visit(ASTCallFunction node, Object data) {
        System.out.println("CALL FUNCTION");
        CallBlock block = new CallBlock();
        Goto go = new Goto();
        go.label = (String) node.jjtGetChild(0).jjtAccept(this, data);
        block.gotoStmt = go;
        if (node.jjtGetNumChildren() == 2) block.params = (List<Expr>) node.jjtGetChild(1).jjtAccept(this, data);
        return block;
    }

    /**
	 * Chamada de uma lista
	 */
    @Override
    public IRNode visit(ASTCallList node, Object data) {
        System.out.println("CALL LIST");
        ListElement element = new ListElement();
        element.atom = (Atom) node.jjtGetChild(0).jjtAccept(this, data);
        if (node.jjtGetNumChildren() == 2) {
            element.expr = (Atom) node.jjtGetChild(1).jjtAccept(this, data);
        }
        return element;
    }

    /**
	 * Primary
	 */
    @Override
    public Expr visit(ASTPrimary node, Object data) {
        System.out.println("PRIMARY");
        return (Expr) node.jjtGetChild(0).jjtAccept(this, data);
    }

    /**
	 * Assignment
	 */
    @Override
    public Assignment visit(ASTAssignmentStmt node, Object data) {
        System.out.println("ASSIGNMENT");
        Assignment assign = new Assignment();
        assign.atom = (Atom) node.jjtGetChild(0).jjtAccept(this, data);
        assign.expr = (Expr) node.jjtGetChild(1).jjtAccept(this, data);
        return assign;
    }

    /**
	 * Imprimir
	 */
    @Override
    public PrintBlock visit(ASTPrintStmt node, Object data) {
        System.out.println("PRINT");
        PrintBlock block = new PrintBlock();
        System.out.println("");
        block.expr = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
        return block;
    }

    /**
	 * Operator
	 */
    @Override
    public Expr visit(ASTTermOper node, Object data) {
        Expr expr = null;
        if (node.jjtGetNumChildren() == 1) {
            expr = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
        } else {
            Operation oper = new Operation();
            oper.type = node.getOperator();
            oper.left = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
            oper.right = (Expr) node.jjtGetChild(1).jjtAccept(this, data);
            expr = oper;
        }
        return expr;
    }

    /**
	 * Operator
	 */
    @Override
    public Expr visit(ASTFactorOper node, Object data) {
        System.out.println("FACTOR");
        Expr expr = null;
        if (node.jjtGetNumChildren() == 1) {
            expr = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
        } else {
            Operation oper = new Operation();
            oper.type = node.getOperator();
            oper.left = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
            oper.right = (Expr) node.jjtGetChild(1).jjtAccept(this, data);
            expr = oper;
        }
        return expr;
    }

    /**
	 * Comparison
	 */
    @Override
    public Expr visit(ASTComparison node, Object data) {
        System.out.println("COMPARISON");
        Expr expr = null;
        if (node.jjtGetNumChildren() == 1) {
            expr = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
        } else {
            Comparison comp = new Comparison();
            comp.type = ((ASTCompOper) node.jjtGetChild(1)).getOperator();
            comp.left = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
            comp.right = (Expr) node.jjtGetChild(2).jjtAccept(this, data);
            expr = comp;
        }
        return expr;
    }

    /**
	 * Comp operator
	 */
    @Override
    public Object visit(ASTCompOper node, Object data) {
        return null;
    }

    /**
	 * Not
	 */
    @Override
    public Expr visit(ASTNotExpr node, Object data) {
        System.out.println("NOT");
        Not not = new Not();
        if (node.isNot()) {
            not.expr = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
            return not;
        } else return (Expr) node.jjtGetChild(0).jjtAccept(this, data);
    }

    /**
	 * And
	 */
    @Override
    public Expr visit(ASTAndExpr node, Object data) {
        System.out.println("AND");
        Expr expr = null;
        if (node.jjtGetNumChildren() == 1) {
            expr = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
        } else {
            Relational relat = new Relational();
            relat.type = Relational.and;
            relat.left = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
            relat.right = (Expr) node.jjtGetChild(1).jjtAccept(this, data);
            expr = relat;
        }
        return expr;
    }

    /**
	 * Or
	 */
    @Override
    public IRNode visit(ASTOrExpr node, Object data) {
        System.out.println("OR");
        Expr expr = null;
        if (node.jjtGetNumChildren() == 1) {
            expr = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
        } else {
            Relational relat = new Relational();
            relat.type = Relational.or;
            relat.left = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
            relat.right = (Expr) node.jjtGetChild(1).jjtAccept(this, data);
            expr = relat;
        }
        return expr;
    }

    /**
	 * Conditional - meio abstracto
	 */
    @Override
    public Expr visit(ASTCondExpr node, Object data) {
        System.out.println("CONDITIONAL");
        return (Expr) node.jjtGetChild(0).jjtAccept(this, data);
    }

    /**
	 * Expressão - meio abstracto
	 */
    @Override
    public Expr visit(ASTExpr node, Object data) {
        System.out.println("EXPR");
        return (Expr) node.jjtGetChild(0).jjtAccept(this, data);
    }

    /**
	 * Abstracto
	 */
    @Override
    public IRNode visit(ASTSimpleStmt node, Object data) {
        return (IRNode) node.jjtGetChild(0).jjtAccept(this, data);
    }

    /**
	 * Continue
	 */
    @Override
    public Goto visit(ASTContinueStmt node, Object data) {
        System.out.println("CONTINUE");
        Goto go = new Goto();
        go.label = "continue";
        return go;
    }

    /**
	 * Break
	 */
    @Override
    public Goto visit(ASTBreakStmt node, Object data) {
        System.out.println("BREAK");
        Goto go = new Goto();
        go.label = "break";
        return go;
    }

    /**
	 * return
	 */
    @Override
    public Return visit(ASTReturnStmt node, Object data) {
        System.out.println("RETURN");
        Return retBlock = new Return();
        retBlock.expr = (Expr) node.jjtGetChild(0).jjtAccept(this, data);
        return retBlock;
    }

    /**
	 * Parameter List
	 */
    @Override
    public List<Atom> visit(ASTParameterList node, Object data) {
        System.out.println("PARAMETERLIST");
        List<Atom> params = new LinkedList<Atom>();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) params.add((Atom) node.jjtGetChild(i).jjtAccept(this, data));
        return params;
    }

    /**
	 * Else if
	 */
    @Override
    public IFStmt visit(ASTElif node, Object data) {
        counter++;
        System.out.println("IF");
        IFStmt temp = new IFStmt();
        temp.expr = (Expr) node.jjtGetChild(0).jjtAccept(this, node);
        Goto go = new Goto();
        go.label = "if" + counter;
        temp.isTrueGoto = go;
        temp.isTrueLabel = new Label();
        temp.isTrueLabel.label = "if" + counter;
        temp.block = (SimpleBlock) node.jjtGetChild(1).jjtAccept(this, node);
        return temp;
    }

    /**
	 * Else
	 */
    @Override
    public SimpleBlock visit(ASTElse node, Object data) {
        System.out.println("ELSE");
        SimpleBlock elseBlock = new SimpleBlock();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) elseBlock.addContent((Stmt) node.jjtGetChild(i).jjtAccept(this, data));
        return elseBlock;
    }

    /**
	 * IF 
	 * expressão, suite
	 */
    @Override
    public IFStmt visit(ASTIf node, Object data) {
        counter++;
        System.out.println("IF");
        IFStmt temp = new IFStmt();
        temp.expr = (Expr) node.jjtGetChild(0).jjtAccept(this, node);
        Goto go = new Goto();
        go.label = "if" + counter;
        temp.isTrueGoto = go;
        temp.isTrueLabel = new Label();
        temp.isTrueLabel.label = "if" + counter;
        temp.block = (SimpleBlock) node.jjtGetChild(1).jjtAccept(this, node);
        return temp;
    }

    /**
	 * IF (ELIF *) ELSE
	 */
    @Override
    public IFBlock visit(ASTIfStmt node, Object data) {
        System.out.println("IFSTMT");
        IFBlock block = new IFBlock();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            if (!(node.jjtGetChild(i) instanceof ASTElse)) {
                block.addStmt((IFStmt) node.jjtGetChild(i).jjtAccept(this, data));
            } else {
                block.elseBlock = (SimpleBlock) node.jjtGetChild(i).jjtAccept(this, data);
                break;
            }
        }
        return block;
    }

    /**
	 * Ciclo For
	 */
    @Override
    public FORBlock visit(ASTFor node, Object data) {
        System.out.println("for");
        return null;
    }

    /**
	 * Ciclo For
	 */
    @Override
    public FORBlock visit(ASTForStmt node, Object data) {
        System.out.println("FOR");
        return null;
    }

    /**
	 * Ciclo While
	 */
    @Override
    public IRNode visit(ASTWhile node, Object data) {
        System.out.println("WHILE");
        return null;
    }

    /**
	 * Ciclo While
	 */
    @Override
    public IRNode visit(ASTWhileStmt node, Object data) {
        System.out.println("WHILESTMT");
        return null;
    }

    /**
	 * Definição de uma funcao
	 * Vou ter de puxar um stackframe, meter-lhe lá os argumentos e as variáveis
	 * Criar um Label com o nome do identificador
	 */
    @Override
    public FunctionBlock visit(ASTDefFunction node, Object data) {
        System.out.println("DEFFUNCTION");
        FunctionBlock function = new FunctionBlock();
        function.label = (Label) node.jjtGetChild(0).jjtAccept(this, data);
        function.skip = new Goto();
        function.skip.label = function.label.label + "_skip";
        if (node.jjtGetNumChildren() == 3) {
            function.args = (List<Atom>) node.jjtGetChild(1).jjtAccept(this, data);
            function.block = (SimpleBlock) node.jjtGetChild(2).jjtAccept(this, data);
        } else function.block = (SimpleBlock) node.jjtGetChild(1).jjtAccept(this, data);
        return function;
    }

    /**
	 * Nó abstracto
	 */
    @Override
    public IRNode visit(ASTCompoundStmt node, Object data) {
        System.out.println("COMPOUND");
        return (IRNode) node.jjtGetChild(0).jjtAccept(this, data);
    }

    /**
	 * Nó abstracto
	 */
    @Override
    public IRNode visit(ASTStmt node, Object data) {
        System.out.println("STMT");
        return (IRNode) node.jjtGetChild(0).jjtAccept(this, data);
    }

    /**
	 * Um bloco de código de uma função, ou ciclo
	 */
    @Override
    public SimpleBlock visit(ASTSuite node, Object data) {
        System.out.println("SUITE");
        SimpleBlock suite = new SimpleBlock();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            suite.addContent((IRNode) node.jjtGetChild(i).jjtAccept(this, data));
        }
        return suite;
    }

    /**
	 * Nó inicial
	 */
    @Override
    public IRNode visit(ASTStart node, Object data) {
        System.out.println("START");
        this.root = new SimpleBlock();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            ((SimpleBlock) this.root).addContent((IRNode) node.jjtGetChild(i).jjtAccept(this, data));
        }
        return this.root;
    }

    @Override
    public AtomList visit(ASTList node, Object data) {
        AtomList atomList = new AtomList();
        for (int i = 0; i < node.jjtGetNumChildren(); i++) atomList.addItem((IRNode) node.jjtGetChild(i).jjtAccept(this, data));
        return atomList;
    }
}
