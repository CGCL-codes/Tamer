package java2opSem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

/** main class to visit a Java program (version <= 1.5)
 * and to generate a program in the syntax of opsem.ml
 * 
 * uses JDT for parsing Java
 * see http://help.eclipse.org/help31/index.jsp?topic=/org.eclipse.jdt.doc.isv/reference/api/index.html
 * for description of the API and AST nodes
 * 
 * Java files are in javaFiles/ directory and generated files
 * are in opsemFiles/
 *
 * See README.txt for current restrictions and details
 * about translation.
 * 
 * @version August 2008
 * @author Hélène Collavizza
 * from an original student work by Eric Le Duff and Sébastien Derrien
 * Polytech'Nice Sophia Antipolis
 */
public class Java2opSemVisitor extends ASTVisitor {

    private FileWriter wFile;

    private BufferedWriter wBuffer;

    private String fileName;

    private String outName;

    private String baseName;

    private int expression = 0;

    private Stack<Integer> statement;

    private Stack<Integer> sequence;

    private int indentation;

    Jml2opSem jml;

    private boolean isArrayDeclaration;

    private boolean minus;

    private ArrayList<String> parameters;

    private ArrayList<String> intVar;

    private ArrayList<String> arrVar;

    String methodName;

    public Java2opSemVisitor(String fileName) throws ExceptionJava2opSem {
        super();
        this.fileName = fileName;
        sequence = new Stack<Integer>();
        isArrayDeclaration = false;
        sequence = new Stack<Integer>();
        statement = new Stack<Integer>();
        parameters = new ArrayList<String>();
        intVar = new ArrayList<String>();
        arrVar = new ArrayList<String>();
        indentation = 0;
        int fst = fileName.indexOf("javaFiles");
        int last = fileName.lastIndexOf('/');
        int dot = fileName.indexOf('.');
        String pref = fst == 0 ? "" : fileName.substring(0, fst) + "/";
        baseName = fileName.substring(last + 1, dot);
        outName = pref + "opsemFiles/" + baseName + "Script" + ".sml";
        try {
            wFile = new FileWriter(outName);
            wBuffer = new BufferedWriter(wFile);
        } catch (Exception e) {
            throw new ExceptionJava2opSem(e.getMessage());
        }
        try {
            jml = new Jml2opSem(new File(fileName));
        } catch (ExceptionJava2opSem e) {
            throw e;
        }
    }

    public void close() {
        try {
            wBuffer.close();
            wFile.close();
            System.out.println("Result has been written in " + outName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseJML() throws ExceptionJava2opSem {
        jml.parse(parameters);
    }

    private void newSequence(int n) {
        sequence.push(new Integer(0));
        statement.push(new Integer(n));
    }

    private void decStatement() {
        int val = statement.pop().intValue();
        statement.push(new Integer(val - 1));
    }

    private void addSequence() {
        int val = sequence.pop().intValue();
        sequence.push(new Integer(val + 1));
    }

    private boolean isLastStatement() {
        return statement.peek().intValue() == 1;
    }

    private void endSequence() {
        statement.pop();
    }

    private int getSequence() {
        return sequence.pop().intValue();
    }

    private void printOpenSequence() {
        if (!isLastStatement()) {
            indentation++;
            println("(Seq");
            addSequence();
        }
        decStatement();
    }

    private void printCloseSequence() {
        int nbSeq = getSequence();
        for (int i = 0; i < nbSeq; i++) {
            println(")");
            indentation--;
        }
        endSequence();
    }

    private boolean expression() {
        return expression > 0;
    }

    /** returns the string which contains the number
	 *  of spaces that correspond to indentation
	 */
    private String indent() {
        String result = "";
        for (int i = 0; i < indentation; i++) {
            result += "  ";
        }
        return result;
    }

    /**
	 * 	print str with the current indentation
	 * 	add a new line
	 */
    private void println(String str) {
        String indent = indent();
        try {
            wBuffer.write(indent + str + "\n");
        } catch (IOException e) {
            System.err.println("Cannot write in the file");
        }
    }

    /**
	 * 	print str with the current indentation
	 */
    private void printIndent(String str) {
        String indent = indent();
        try {
            wBuffer.write(indent + str);
        } catch (IOException e) {
            System.err.println("Cannot write in the file");
        }
    }

    private void print(String str) {
        try {
            wBuffer.write(str);
        } catch (IOException e) {
            System.err.println("Cannot write in the file");
        }
    }

    private void printHeader() {
        println("(* This file has been generated by java2opSem from " + fileName + "*)\n\n");
        println("open HolKernel Parse boolLib");
        println("stringLib IndDefLib IndDefRules");
        println("finite_mapTheory relationTheory");
        println("newOpsemTheory");
        println("computeLib bossLib;\n");
        println("val _ = new_theory \"" + baseName + "\";\n");
    }

    public boolean visit(MethodDeclaration node) {
        printHeader();
        println("(* Method " + node.getName() + "*)");
        println("val MAIN_def =");
        indentation++;
        println("Define `MAIN =");
        List param = node.parameters();
        for (int i = 0; i < param.size(); i++) {
            SingleVariableDeclaration tmp = (SingleVariableDeclaration) param.get(i);
            String name = tmp.getName().toString();
            parameters.add(name);
            if (tmp.getType().isArrayType()) {
                arrVar.add(name);
            } else intVar.add(name);
        }
        try {
            parseJML();
        } catch (ExceptionJava2opSem e) {
            System.err.println("Error found during JML parsing." + " Not able to build JML specification.");
            e.printStackTrace();
        }
        methodName = node.getName().toString();
        indentation++;
        println("RSPEC");
        println("(\\state.");
        indentation++;
        println(jml.getRequires(methodName) + ")");
        indentation--;
        return true;
    }

    private void printVars(ArrayList<String> v) {
        int s = v.size();
        if (s != 0) {
            print("[");
            for (int i = 0; i < s - 1; i++) print("\"" + v.get(i) + "\";");
            print("\"" + v.get(s - 1) + "\"");
            print("]");
        } else print("[]: string list");
    }

    public void endVisit(MethodDeclaration node) {
        println("(\\state1 state2.");
        indentation++;
        println(jml.getEnsures(methodName) + ")");
        indentation--;
        println("`\n");
        indentation--;
        Type rt = node.getReturnType2();
        if (rt.isPrimitiveType() && !rt.toString().equals("void")) intVar.add("Result");
        println("  val intVar_def =");
        printIndent("	     Define `intVar =");
        printVars(intVar);
        println("`\n");
        println("  val arrVar_def =");
        printIndent("	     Define `arrVar =");
        printVars(arrVar);
        println("`\n");
        println("val _ = export_theory();");
    }

    public boolean visit(Javadoc node) {
        print("(*");
        print(node.toString());
        print("*)");
        return false;
    }

    /**
	 * Visit a block delimited with '{' and '}'
	 */
    public boolean visit(Block node) {
        newSequence(node.statements().size());
        return true;
    }

    public void endVisit(Block node) {
        printCloseSequence();
    }

    public boolean visit(VariableDeclarationStatement node) {
        if (node.getType().isArrayType()) {
            isArrayDeclaration = true;
        }
        return true;
    }

    public void endVisit(VariableDeclarationStatement node) {
        isArrayDeclaration = false;
    }

    public boolean visit(VariableDeclarationFragment node) {
        if (!isArrayDeclaration) {
            intVar.add(node.getName().toString());
            printOpenSequence();
            indentation++;
            if (node.getInitializer() == null) println("(Assign \"" + node.getName() + "\" (Const 0))"); else {
                println("(Assign \"" + node.getName() + "\"");
                expression++;
                node.getInitializer().accept(this);
                expression--;
                return false;
            }
        } else {
            println("(* Array declaration of " + node.getName() + "*)");
        }
        return true;
    }

    public void endVisit(VariableDeclarationFragment node) {
        if (node.getInitializer() != null) {
            println(")");
        }
        indentation--;
    }

    /**
	 * Binary expressions
	 */
    public boolean visit(InfixExpression node) {
        if (node.getOperator().equals(InfixExpression.Operator.GREATER)) {
            indentation++;
            println("(Less ");
            expression++;
            node.getRightOperand().accept(this);
            node.getLeftOperand().accept(this);
            println(")");
            indentation--;
            return false;
        } else if (node.getOperator().equals(InfixExpression.Operator.GREATER_EQUALS)) {
            indentation++;
            println("(Not (Less ");
            expression++;
            node.getLeftOperand().accept(this);
            node.getRightOperand().accept(this);
            println("))");
            indentation--;
            return false;
        } else {
            indentation++;
            if (node.getOperator().equals(InfixExpression.Operator.PLUS)) {
                println("(Plus ");
            } else if (node.getOperator().equals(InfixExpression.Operator.MINUS)) {
                println("(Sub ");
            } else if (node.getOperator().equals(InfixExpression.Operator.TIMES)) {
                println("(Times ");
            } else if (node.getOperator().equals(InfixExpression.Operator.DIVIDE)) {
                println("(Div ");
            } else if (node.getOperator().equals(InfixExpression.Operator.EQUALS)) {
                println("(Equal ");
            } else if (node.getOperator().equals(InfixExpression.Operator.LESS)) {
                println("(Less ");
            } else if (node.getOperator().equals(InfixExpression.Operator.LESS_EQUALS)) {
                println("(LessEq ");
            } else if (node.getOperator().equals(InfixExpression.Operator.CONDITIONAL_OR)) {
                println("(Or ");
            } else if (node.getOperator().equals(InfixExpression.Operator.CONDITIONAL_AND)) {
                println("(And ");
            } else if (node.getOperator().equals(InfixExpression.Operator.NOT_EQUALS)) {
                println("(Not (Equal ");
            }
        }
        expression++;
        return true;
    }

    /**
	 * Close infix operators
	 */
    public void endVisit(InfixExpression node) {
        if (node.getOperator().equals(InfixExpression.Operator.NOT_EQUALS)) {
            println("))");
            indentation--;
        } else if (!(node.getOperator().equals(InfixExpression.Operator.GREATER_EQUALS) || node.getOperator().equals(InfixExpression.Operator.GREATER))) {
            println(")");
            indentation--;
        }
        expression--;
    }

    /**
	 * prefix expression not
	 */
    public boolean visit(PrefixExpression node) {
        if (node.getNodeType() == PrefixExpression.PREFIX_EXPRESSION) {
            minus = true;
        } else {
            indentation++;
            println("(Not ");
        }
        expression++;
        return true;
    }

    public void endVisit(PrefixExpression node) {
        if (minus) minus = false; else {
            println(")");
            indentation--;
        }
        expression--;
    }

    /**
	 * Numeric nodes
	 */
    public boolean visit(NumberLiteral node) {
        indentation++;
        println("(Const " + (minus ? "~" : "") + node.toString() + ")");
        return false;
    }

    public void endVisit(NumberLiteral node) {
        indentation--;
    }

    /**
	 * Variables
	 * Variable name must be printed only inside an expression 
	 */
    public boolean visit(SimpleName node) {
        if (expression()) {
            indentation++;
            println("(Var \"" + node + "\")");
        }
        return false;
    }

    public void endVisit(SimpleName node) {
        if (expression()) indentation--;
    }

    public boolean visit(ParenthesizedExpression node) {
        return true;
    }

    public void endVisit(ParenthesizedExpression node) {
    }

    /**
	 * Return a term "(Cond <b> <if> <then>)" where
	 * b is the term representing the condition
	 * if is the term representing the "if" block
	 * then is the term representing the "else" block, 
	 *      it is equals to "Skip" if there is not "else" block 
	 */
    public boolean visit(IfStatement node) {
        printOpenSequence();
        indentation++;
        println("(Cond ");
        node.getExpression().accept(this);
        node.getThenStatement().accept(this);
        if (node.getElseStatement() != null) {
            node.getElseStatement().accept(this);
        } else {
            indentation++;
            println("Skip");
            indentation--;
        }
        return false;
    }

    public void endVisit(IfStatement node) {
        println(")");
        indentation--;
    }

    /**
	 * Return a term "(While <b> <body>)" where
	 * b is the term representing the condition
	 * body is the term representing the while block
	 */
    public boolean visit(WhileStatement node) {
        printOpenSequence();
        indentation++;
        println("(While ");
        return true;
    }

    public void endVisit(WhileStatement node) {
        println(")");
        indentation--;
    }

    public boolean visit(Assignment node) {
        printOpenSequence();
        indentation++;
        if (node.getLeftHandSide().getNodeType() != Assignment.ARRAY_ACCESS) {
            println("(Assign \"" + node.getLeftHandSide() + "\"");
            expression++;
            node.getRightHandSide().accept(this);
        } else {
            ArrayAccess var = (ArrayAccess) node.getLeftHandSide();
            println("(ArrayAssign \"" + var.getArray() + "\"");
            expression++;
            var.getIndex().accept(this);
            node.getRightHandSide().accept(this);
        }
        return false;
    }

    public void endVisit(Assignment node) {
        println(")");
        indentation--;
        expression--;
    }

    public boolean visit(ReturnStatement node) {
        if (node.getExpression() != null) {
            indentation++;
            println("(Assign \"Result\"");
            expression++;
        }
        return true;
    }

    public void endVisit(ReturnStatement node) {
        if (node.getExpression() != null) {
            expression--;
            println(")");
            indentation--;
        }
    }

    public boolean visit(LineComment node) {
        return true;
    }

    public boolean visit(ArrayAccess node) {
        indentation++;
        println("(Arr \"" + node.getArray() + "\"");
        node.getIndex().accept(this);
        return false;
    }

    public void endVisit(ArrayAccess node) {
        println(")");
        indentation--;
    }

    public boolean visit(QualifiedName node) {
        indentation++;
        if (node.getName().toString().equals("length")) println("(Var \"" + node.getQualifier() + "Length\")"); else System.err.println("Object fields are not yet handled");
        return false;
    }

    public void endVisit(QualifiedName node) {
        indentation--;
    }
}
