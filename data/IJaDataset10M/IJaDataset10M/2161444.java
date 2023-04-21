package com.mangobop.impl.query.construction;

import org.saxpath.SAXPathException;
import org.saxpath.XPathHandler;
import com.mangobop.impl.query.VariableQueryOperand;
import com.mangobop.query.BooleanExpression;
import com.mangobop.query.QueryOperand;
import com.mangobop.query.qom.Predicate;

/**
 * @author Stefan Meyer
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class MPathHandler implements XPathHandler {

    private java.util.Vector exprStack = new java.util.Vector();

    private java.util.Vector functionalStack = new java.util.Vector();

    private QueryOperand topQ;

    private boolean initialPhase = false;

    public MPathHandler() {
        reset();
    }

    public void reset() {
    }

    private Predicate evaluatePredicate(Object expr) {
        Predicate q = null;
        Predicate next = null;
        while (!expr.equals(exprStack.lastElement())) {
            Object last = exprStack.lastElement();
            ;
            if (last instanceof String) {
                exprStack.remove(exprStack.size() - 1);
            } else {
                if (last instanceof Predicate) {
                    exprStack.remove(exprStack.size() - 1);
                    q = (Predicate) last;
                }
            }
            next = q;
        }
        exprStack.remove(exprStack.size() - 1);
        return q;
    }

    private QueryOperand evaluate(Object expr) {
        QueryOperand q = null;
        QueryOperand next = null;
        while (!expr.equals(exprStack.lastElement())) {
            Object last = exprStack.lastElement();
            ;
            if (last instanceof String) {
                exprStack.remove(exprStack.size() - 1);
            } else {
                if (last instanceof QueryOperand) {
                    exprStack.remove(exprStack.size() - 1);
                    q = (QueryOperand) last;
                }
            }
            if (next != null && next != q) {
                extendPath(q, next);
            }
            next = q;
        }
        exprStack.remove(exprStack.size() - 1);
        return q;
    }

    /**
	 * @param q
	 * @param next
	 */
    protected abstract void extendPath(QueryOperand q, QueryOperand next);

    private java.util.List getOperand(Object expr) {
        Object o = null;
        java.util.List list = new java.util.ArrayList();
        while (!expr.equals(exprStack.lastElement())) {
            Object last = exprStack.lastElement();
            ;
            if (last instanceof String) {
                exprStack.remove(exprStack.size() - 1);
            } else {
                exprStack.remove(exprStack.size() - 1);
                if (o != null) {
                    System.out.println(" more then one AbstractOperand");
                }
                o = last;
                list.add(o);
            }
        }
        exprStack.remove(exprStack.size() - 1);
        return list;
    }

    public void startXPath() throws SAXPathException {
        initialPhase = true;
        exprStack.add("xpath");
    }

    public void endXPath() throws SAXPathException {
        java.util.List list = getOperand("xpath");
        topQ = ((QueryOperand) list.get(0));
    }

    public void startPathExpr() throws SAXPathException {
        exprStack.add("pathExpr");
    }

    public void endPathExpr() throws SAXPathException {
        QueryOperand op = evaluate("pathExpr");
        exprStack.add(op);
    }

    public void startAbsoluteLocationPath() throws SAXPathException {
        QueryOperand op = createRoot();
        exprStack.add(op);
        initialPhase = false;
    }

    public void endAbsoluteLocationPath() throws SAXPathException {
    }

    public void startRelativeLocationPath() throws SAXPathException {
        if (initialPhase) {
            QueryOperand op = createCurrent();
            exprStack.add(op);
        } else {
            int x = 0;
        }
        initialPhase = false;
    }

    public void endRelativeLocationPath() throws SAXPathException {
    }

    public void startNameStep(int axis, String prefix, String localName) throws SAXPathException {
        QueryOperand op = createPathPart(axis, prefix, localName);
        if (op != null) {
            exprStack.add(op);
        }
    }

    public void endNameStep() throws SAXPathException {
    }

    public void startTextNodeStep(int axis) throws SAXPathException {
    }

    public void endTextNodeStep() throws SAXPathException {
    }

    public void startCommentNodeStep(int axis) throws SAXPathException {
    }

    public void endCommentNodeStep() throws SAXPathException {
    }

    public void startAllNodeStep(int axis) throws SAXPathException {
    }

    public void endAllNodeStep() throws SAXPathException {
    }

    public void startProcessingInstructionNodeStep(int axis, String name) throws SAXPathException {
    }

    public void endProcessingInstructionNodeStep() throws SAXPathException {
    }

    public void startPredicate() throws SAXPathException {
        exprStack.add("predicate");
    }

    public void endPredicate() throws SAXPathException {
        java.util.List ops = getOperand("predicate");
        BooleanExpression exp1 = (BooleanExpression) ops.get(0);
        QueryOperand predicate = createPredicatePath(exp1);
        exprStack.add(predicate);
    }

    public void startFilterExpr() throws SAXPathException {
    }

    public void endFilterExpr() throws SAXPathException {
    }

    public void startOrExpr() throws SAXPathException {
        exprStack.add("or");
    }

    public void endOrExpr(boolean create) throws SAXPathException {
        if (create) {
        }
    }

    public void startAndExpr() throws SAXPathException {
        exprStack.add("and");
    }

    public void endAndExpr(boolean create) throws SAXPathException {
        if (create) {
            BooleanExpression right = (BooleanExpression) getOperand("and").get(0);
            BooleanExpression left = (BooleanExpression) getOperand("and").get(0);
            BooleanExpression p = createAndExpression(left, right);
            exprStack.add(p);
        }
    }

    public void startEqualityExpr() throws SAXPathException {
        exprStack.add("equalityExpr");
    }

    public void endEqualityExpr(int equalityOperator) throws SAXPathException {
        if (equalityOperator != 0) {
            QueryOperand right = (QueryOperand) getOperand("equalityExpr").get(0);
            QueryOperand left = (QueryOperand) getOperand("equalityExpr").get(0);
            BooleanExpression op = createEquality(left, right);
            exprStack.add(op);
        }
    }

    public void startRelationalExpr() throws SAXPathException {
    }

    public void endRelationalExpr(int relationalOperator) throws SAXPathException {
    }

    public void startAdditiveExpr() throws SAXPathException {
        exprStack.add("additive");
    }

    public void endAdditiveExpr(int additiveOperator) throws SAXPathException {
        if (additiveOperator != 0) {
            QueryOperand right = (QueryOperand) getOperand("additive").get(0);
            QueryOperand left = (QueryOperand) getOperand("additive").get(0);
            QueryOperand q = createAdditiveExpression(additiveOperator, left, right);
            exprStack.add(q);
        }
    }

    public void startMultiplicativeExpr() throws SAXPathException {
    }

    public void endMultiplicativeExpr(int multiplicativeOperator) throws SAXPathException {
    }

    public void startUnaryExpr() throws SAXPathException {
    }

    public void endUnaryExpr(int unaryOperator) throws SAXPathException {
    }

    public void startUnionExpr() throws SAXPathException {
    }

    public void endUnionExpr(boolean create) throws SAXPathException {
    }

    public void number(int number) throws SAXPathException {
        QueryOperand part = createNumber(number);
        exprStack.add(part);
    }

    public void number(double number) throws SAXPathException {
    }

    public void literal(String literal) throws SAXPathException {
        QueryOperand p = createLiteral(literal);
        exprStack.add(p);
    }

    public void variableReference(String prefix, String variableName) throws SAXPathException {
        exprStack.add(new VariableQueryOperand(prefix, variableName));
    }

    public void startFunction(String prefix, String functionName) throws SAXPathException {
        exprStack.add("function");
        functionalStack.add(prefix + ":::" + functionName);
    }

    public void endFunction() throws SAXPathException {
        java.util.List parameter = getOperand("function");
        String f = (String) functionalStack.get(functionalStack.size() - 1);
        functionalStack.remove(functionalStack.size() - 1);
        String[] names = f.split(":::");
        String prefix = names[0];
        String functionName = names[1];
        if ("current".equals(functionName)) {
            QueryOperand op = createCurrent();
            exprStack.add(op);
        } else {
            try {
                QueryOperand fu = createFunction(prefix, functionName, parameter);
                exprStack.add(fu);
            } catch (CreationException e) {
                throw new SAXPathException(e.getMessage());
            }
        }
    }

    protected abstract QueryOperand createPathPart(int axis, String prefix, String localName);

    protected abstract BooleanExpression createEquality(QueryOperand l, QueryOperand r);

    protected abstract BooleanExpression createAndExpression(BooleanExpression l, BooleanExpression r);

    protected abstract QueryOperand createAdditiveExpression(int additiveOperator, QueryOperand l, QueryOperand r);

    protected abstract QueryOperand createFunction(String prefix, String functionName, java.util.List parameter) throws CreationException;

    protected abstract QueryOperand createLiteral(String literal);

    protected abstract QueryOperand createNumber(int number);

    public QueryOperand getQuery() {
        return topQ;
    }

    public abstract QueryOperand createCurrent();

    public abstract QueryOperand createRoot();

    protected abstract Predicate createPredicatePath(BooleanExpression exp1);
}
