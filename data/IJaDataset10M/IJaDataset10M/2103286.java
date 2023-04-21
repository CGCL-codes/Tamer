package gov.nasa.jpf.symbc.string;

import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import java.util.*;
import hampi.Hampi;
import hampi.constraints.Constraint;
import hampi.constraints.Regexp;
import hampi.constraints.Expression;
import hampi.Solution;

public class SymbolicStringConstraintsHAMPI {

    StringPathCondition pc = null;

    Hampi hampi;

    public boolean isSatisfiable(StringPathCondition pc) {
        hampi = new Hampi();
        boolean result = getExpression(pc.header);
        return result;
    }

    private Expression getStringExpression(StringExpression expr) {
        Expression result = null;
        if (expr instanceof StringConstant) {
            result = hampi.constExpr(expr.solution());
        } else if (expr instanceof StringSymbolic) {
            result = hampi.varExpr(expr.getName());
        } else if (expr instanceof DerivedStringExpression) {
            DerivedStringExpression dexpr = (DerivedStringExpression) expr;
            switch(dexpr.op) {
                case VALUEOF:
                    result = getStringExpression((StringExpression) dexpr.oprlist[0]);
                    break;
                case CONCAT:
                    Object left = getStringExpression(dexpr.left);
                    Object right = getStringExpression(dexpr.right);
                    result = hampi.concatExpr((Expression) left, (Expression) right);
                    break;
            }
        } else {
            System.out.println("Exiting after unhandled type " + expr);
            System.exit(0);
        }
        return result;
    }

    private boolean findSolution(Constraint c1) {
        Constraint all = hampi.andConstraint(listOfAllConstraints);
        all = hampi.andConstraint(all, c1);
        Solution s;
        int len = 0;
        while (len < 256) {
            s = hampi.solve(all, len);
            if (s.isSatisfiable()) {
                System.out.println("Found solution!");
                return true;
            }
            len++;
        }
        System.out.println("No solution for " + all.toJavaCode(""));
        return false;
    }

    List<Constraint> listOfAllConstraints = new LinkedList<Constraint>();

    private boolean evaluateStringConstraint(StringConstraint c) {
        Expression left = null;
        Expression right = null;
        switch(c.comp) {
            case EQUALS:
            case EQ:
                left = getStringExpression(c.left);
                right = getStringExpression(c.right);
                if (!(c.left instanceof StringConstant) && !(c.right instanceof StringConstant)) {
                    System.out.println("EQ: One side must be non symbolic for HAMPI to work!");
                    System.exit(0);
                }
                if ((c.left instanceof StringConstant) && (c.right instanceof StringConstant)) {
                    return c.left.solution().equals(c.right.solution());
                }
                if ((c.left instanceof StringConstant)) {
                    Regexp regLeft = hampi.constRegexp(c.left.solution());
                    Constraint c1 = hampi.regexpConstraint(right, true, regLeft);
                    if (findSolution(c1)) listOfAllConstraints.add(c1); else return false;
                } else if ((c.right instanceof StringConstant)) {
                    Regexp regRight = hampi.constRegexp(c.right.solution());
                    Constraint c1 = hampi.regexpConstraint(left, true, regRight);
                    if (findSolution(c1)) listOfAllConstraints.add(c1); else return false;
                }
                break;
            case NOTEQUALS:
            case NE:
                left = getStringExpression(c.left);
                right = getStringExpression(c.right);
                if (!(c.left instanceof StringConstant) && !(c.right instanceof StringConstant)) {
                    System.out.println("NE: One side must be non symbolic for HAMPI to work!");
                    System.exit(0);
                }
                if ((c.left instanceof StringConstant) && (c.right instanceof StringConstant)) {
                    return !c.left.solution().equals(c.right.solution());
                }
                if ((c.left instanceof StringConstant)) {
                    Regexp regLeft = hampi.constRegexp(c.left.solution());
                    Constraint c1 = hampi.regexpConstraint(right, false, regLeft);
                    if (findSolution(c1)) listOfAllConstraints.add(c1); else return false;
                } else if ((c.right instanceof StringConstant)) {
                    Regexp regRight = hampi.constRegexp(c.right.solution());
                    Constraint c1 = hampi.regexpConstraint(left, false, regRight);
                    if (findSolution(c1)) listOfAllConstraints.add(c1); else return false;
                }
                break;
        }
        return true;
    }

    private boolean getExpression(StringConstraint c) {
        listOfAllConstraints = new LinkedList<Constraint>();
        while (c != null) {
            boolean constraintResult = evaluateStringConstraint(c);
            if (constraintResult == false) return false;
            c = c.and;
        }
        return true;
    }

    public void solve(StringPathCondition pc) {
    }
}
