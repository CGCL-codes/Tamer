package r2q2.processing.expression.operators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.jdom.Element;
import r2q2.processing.expression.Expression;
import r2q2.processing.expression.ExpressionFactory;
import r2q2.processing.expression.InvalidArgumentsException;
import r2q2.variable.ItemVariable;
import r2q2.variable.TransitoryVariable;
import r2q2.variable.ItemVariable.BaseType;
import r2q2.variable.ItemVariable.Cardinality;

/**
 * 
 * 'equal' operator expression
 * 
 * constraints: 
 *   takes a two sub expressions of the same numeric base type
 *   expressions must have single cardinality
 *   returns Boolean 'true' if the two expressions are numerically equal
 *   if either sub expression is NULL, NULL is returned
 */
public class Equal implements Expression {

    public enum toleranceMode {

        exact, absolute, relative
    }

    private Expression subpart[];

    private toleranceMode toleranceMode;

    private double[] tolerances;

    public Equal(Element _e) throws InvalidArgumentsException {
        subpart = new Expression[2];
        ExpressionFactory of = ExpressionFactory.getInstance();
        Iterator children = _e.getChildren().iterator();
        String tolModString = _e.getAttributeValue("toleranceMode");
        if (tolModString.equals("exact")) toleranceMode = toleranceMode.exact; else if (tolModString.equals("absolute")) toleranceMode = toleranceMode.absolute; else if (tolModString.equals("relative")) toleranceMode = toleranceMode.relative; else throw new InvalidArgumentsException("Error: unknown tolerance mode attribute : " + tolModString + ", aborting");
        if (toleranceMode == toleranceMode.absolute || toleranceMode == toleranceMode.relative) {
            String tolString = _e.getAttributeValue("tolerance");
            StringTokenizer st = new StringTokenizer(tolString);
            int tolCount = st.countTokens();
            if (tolCount > 2 || tolCount < 1) throw new InvalidArgumentsException("Error: incorrect number of tolerances surplied to the 'equal' method, aborting");
            tolerances = new double[tolCount];
            int i = 0;
            while (st.hasMoreTokens()) {
                tolerances[i] = Double.parseDouble(st.nextToken());
                if (tolerances[i] < 0) throw new InvalidArgumentsException("Error: cannot have a negative value for a tolerance, aborting");
                i++;
            }
        }
        int i = 0;
        try {
            while (children.hasNext()) {
                if (i < 2) {
                    subpart[i] = of.makeExpression((Element) children.next());
                }
                i++;
            }
        } catch (InvalidArgumentsException iae) {
            throw new InvalidArgumentsException("Error occured in building childrens expression: ", iae);
        }
        if (i < 2 || i > 2) throw new InvalidArgumentsException("Error: usage violation, Member must be called with exactly 2 argument rather than the " + i + " arguments specified");
    }

    public ItemVariable eval(HashMap<String, ItemVariable> vars) throws InvalidArgumentsException {
        try {
            ItemVariable test1 = subpart[0].eval(vars);
            ItemVariable test2 = subpart[1].eval(vars);
            if (test1 == null || test2 == null) return null;
            if (test1.value == null || test2.value == null) return null;
            if (test1.card != Cardinality.single || test2.card != Cardinality.single) throw new InvalidArgumentsException("Error: the 'equal' operation can only be called on two operands each of single cardinality, aborting");
            if (test1.varType == BaseType.integer && test2.varType == BaseType.integer) {
                Integer i1 = (Integer) test1.value;
                Integer i2 = (Integer) test2.value;
                if (toleranceMode == toleranceMode.exact) {
                    if (i1.intValue() == i2.intValue()) return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(true)); else return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(false));
                } else {
                    double t0 = 0;
                    double t1 = 0;
                    if (tolerances.length == 1) {
                        t0 = t1 = tolerances[0];
                    } else {
                        t0 = tolerances[0];
                        t1 = tolerances[1];
                    }
                    if (toleranceMode == toleranceMode.absolute) {
                        if (i2 > i1 - t0 && i2 < i1 + t1) return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(true)); else return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(false));
                    } else {
                        if (i2 > (i1 * (1 - (t0 / 100))) && i2 < (i1 * (1 + (t1 / 100)))) return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(true)); else return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(false));
                    }
                }
            } else {
                Double f1;
                Double f2;
                if (test1.varType == BaseType.integer) f1 = new Double(((Integer) test1.value).intValue()); else if (test1.varType == BaseType.afloat) f1 = (Double) test1.value; else throw new InvalidArgumentsException("Error: first sub expression is not of a supported numeric type, aborting");
                if (test2.varType == BaseType.integer) f2 = new Double(((Integer) test2.value).intValue()); else if (test2.varType == BaseType.afloat) f2 = (Double) test2.value; else throw new InvalidArgumentsException("Error: second sub expression is not of a supported numeric type, aborting");
                if (toleranceMode == toleranceMode.exact) {
                    if (f1.floatValue() == f2.floatValue()) return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(true)); else return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(false));
                } else {
                    double t0 = 0;
                    double t1 = 0;
                    if (tolerances.length == 1) {
                        t0 = t1 = tolerances[0];
                    } else {
                        t0 = tolerances[0];
                        t1 = tolerances[1];
                    }
                    if (toleranceMode == toleranceMode.absolute) {
                        if (f2 > f1 - t0 && f2 < f1 + t1) return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(true)); else return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(false));
                    } else {
                        if (f2 > (f1 * (1 - (t0 / 100))) && f2 < (f1 * (1 + (t1 / 100)))) return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(true)); else return new TransitoryVariable(BaseType.bool, Cardinality.single, new Boolean(false));
                    }
                }
            }
        } catch (InvalidArgumentsException iae) {
            throw new InvalidArgumentsException("Error: evaluation of container yielded unexpected exception", iae);
        }
    }
}
