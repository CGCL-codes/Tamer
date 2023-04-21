package edu.gsbme.MMLParser2.MathML.MEE;

import java.util.ArrayList;
import edu.gsbme.MMLParser2.MathML.MEE.Error.SyntaxError;
import edu.gsbme.MMLParser2.MathML.MEE.Evaluate.VariableTables;
import edu.gsbme.MMLParser2.MathML.MEE.MathAST.AST;
import edu.gsbme.MMLParser2.MathML.MEE.MathAST.BinaryExpr;
import edu.gsbme.MMLParser2.MathML.MEE.MathAST.CallFunc;
import edu.gsbme.MMLParser2.MathML.MEE.MathAST.EmptyAST;
import edu.gsbme.MMLParser2.MathML.MEE.MathAST.Expr;
import edu.gsbme.MMLParser2.MathML.MEE.MathAST.ExprStmt;
import edu.gsbme.MMLParser2.MathML.MEE.MathAST.IntExpr;
import edu.gsbme.MMLParser2.MathML.MEE.MathAST.IntLiteral;
import edu.gsbme.MMLParser2.MathML.MEE.MathAST.Operator;
import edu.gsbme.MMLParser2.MathML.MEE.MathAST.UnaryExpr;
import edu.gsbme.MMLParser2.MathML.MEE.MathAST.VarExpr;
import edu.gsbme.MMLParser2.MathML.MEE.StringPrinter.ExpressionPrinterVisitor;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.InsertVariablePrefix;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.NoramlizeEq;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.SearchReplace;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.cloneASTTree;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.evaluateFunctionCoefficient;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.searchDiff2FunctionVisitor;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.searchFunctionVisitor;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.searchVariable;

/**
 * A simplified central point of access to access MEE utilities found in other MEE packages.
 * @author David
 *
 */
public class MEEUtility {

    public static void main(String[] arg) throws SyntaxError {
        String testeq = " (5*diff(diff(x,y),y)-4)+3*2*diff(x,y)/3-2=2-3*(1-2*diff(z,y))";
        MEEParser parser = new MEEParser(testeq, true);
        ExprStmt root = (ExprStmt) parser.getTreeRoot();
        System.out.println((String) root.visit(new ExpressionPrinterVisitor(), null));
        double r = evaluateFunctionCoefficient.getFunctionCoefficient(root, "diff", new String[] { "x", "y" }, new VariableTables());
        System.out.println(r);
        String testeq2 = "y= 5 > 3";
        parser = new MEEParser(testeq2, true);
        System.out.println(parser);
    }

    /**
	 * Get the dampening coffient. The right hand side of the equation is considered to be positive. I.e. if the DA is found on the left side, it will be negated to the right side (i.e. *-1)
	 * 
	 * FIXME BUG <- Doesn't Consider left or righthand side
	 * 
	 * @param rootTree
	 * @param arg {dependent variable, independent variable} to be searched for.
	 * @return
	 */
    public static String getDaCoefficient(AST rootTree, String[] arg) {
        return "" + evaluateFunctionCoefficient.getFunctionCoefficient(rootTree, "diff", arg, new VariableTables());
    }

    /**
	 * Get ddx/dyy etc
	 * 
	 * FIXME need to update this using the same type of algorithm as Da extraction
	 * Update evaluateFunctionAlgorithm to support embeded functions i.e. diff(diff(x,t),t)
	 * 
	 * @param rootTree
	 * @param arg
	 * @return
	 */
    public static String getEaCoefficient(AST rootTree, String[] arg) {
        searchDiff2FunctionVisitor search = new searchDiff2FunctionVisitor(arg);
        ExpressionPrinterVisitor printer = new ExpressionPrinterVisitor();
        AST result = (AST) rootTree.visit(search, null);
        return "" + evaluateFunctionCoefficient.getFunctionCoefficient(result, new VariableTables());
    }

    /**
	 * Get the coefficient of a variable declaration from a equation 
	 * Assuming variable only declared ONCE
	 * Assume variable is only declared on the right hand side
	 * @param rootTree
	 * @param variable
	 * @return
	 */
    public static String getVariableCoefficient(AST rootTree, String variable) {
        ExpressionPrinterVisitor printer = new ExpressionPrinterVisitor();
        searchVariable searchVar = new searchVariable(variable);
        rootTree.visit(searchVar, null);
        if (searchVar.result.size() == 0) return "0"; else {
            VarExpr var = (VarExpr) searchVar.result.get(0).parent;
            if (!(var.parent instanceof BinaryExpr)) {
                return "1";
            } else {
                BinaryExpr BE = (BinaryExpr) var.parent;
                if (BE.O.spelling.equals("*")) {
                    if (BE.E1.equals(var)) {
                        return (String) multipleByNegativeOne(BE.E2).visit(printer, null);
                    } else {
                        return (String) multipleByNegativeOne(BE.E1).visit(printer, null);
                    }
                } else if (BE.O.spelling.equals("/")) {
                    if (BE.E1 instanceof CallFunc) {
                        return "-1/" + ((String) BE.E2.visit(printer, null));
                    } else {
                        System.out.println("ERROR: Equation is not in supported format");
                        return (String) BE.E1.visit(printer, null);
                    }
                } else if (BE.O.spelling.equals("-")) {
                    if (BE.E1.equals(var)) return "1"; else return "-1";
                } else {
                    return "1";
                }
            }
        }
    }

    public static String getCoefficient(AST rootTree, String function, String[] arg) {
        searchFunctionVisitor search = new searchFunctionVisitor(function, arg);
        ExpressionPrinterVisitor printer = new ExpressionPrinterVisitor();
        AST result = (AST) rootTree.visit(search, null);
        if (result instanceof EmptyAST) return "0"; else if (result instanceof CallFunc) {
            if (!(result.parent instanceof BinaryExpr)) return "1"; else {
                BinaryExpr BE = (BinaryExpr) result.parent;
                if (BE.O.spelling.equals("*")) {
                    if (BE.E1.equals(result)) {
                        if (search.side == 0) return (String) BE.E2.visit(printer, null); else return (String) multipleByNegativeOne(BE.E2).visit(printer, null);
                    } else {
                        if (search.side == 0) return (String) BE.E1.visit(printer, null); else return (String) multipleByNegativeOne(BE.E1).visit(printer, null);
                    }
                } else if (BE.O.spelling.equals("/")) {
                    if (BE.E1.equals(result)) {
                        if (search.side == 0) return "1/" + ((String) BE.E2.visit(printer, null)); else return "-1/" + ((String) BE.E2.visit(printer, null));
                    } else {
                        System.out.println("ERROR: Equation is not in supported format");
                        return (String) BE.E1.visit(printer, null);
                    }
                } else if (BE.O.spelling.equals("-")) {
                    if (BE.E1.equals(result)) return "1"; else return "-1";
                } else {
                    return "1";
                }
            }
        }
        return "0";
    }

    /**
	 * Get the source term of non diff terms
	 * @param rootTree
	 * @return
	 */
    public static String getSourceTerm(AST rootTree) throws SyntaxError {
        AST clone = Clone(rootTree);
        MEEParser cloneP = new MEEParser(clone, false);
        clone.visit(new NoramlizeEq(), null);
        return cloneP.toString().split("=")[1];
    }

    public static AST multipleByNegativeOne(Expr ast) {
        UnaryExpr negOne = new UnaryExpr(new Operator("-"), new IntExpr(new IntLiteral("1")));
        BinaryExpr negative = new BinaryExpr(negOne, new Operator("*"), ast);
        return negative;
    }

    /**
	 * 
	 * Replace algorithm with cloneASTTree instead
	 * 
	 * @param rootTree
	 * @return
	 * @throws SyntaxError
	 */
    public static AST Clone(AST rootTree) throws SyntaxError {
        return cloneASTTree.clone(rootTree);
    }

    public static AST appendPrefix(AST rootTree, String prefix, ArrayList<String> ignore) {
        InsertVariablePrefix appendPrefix = new InsertVariablePrefix(prefix, ignore);
        rootTree.visit(appendPrefix, null);
        return rootTree;
    }

    public static AST replaceVar(AST rootTree, String originalVar, String newVar) {
        SearchReplace search = new SearchReplace(originalVar, newVar);
        rootTree.visit(search, null);
        return rootTree;
    }
}
