package at.ofai.gate.japeutils.ops;

import gate.Annotation;
import gate.AnnotationSet;
import gate.FeatureMap;
import gate.util.GateRuntimeException;
import gate.jape.constraint.*;
import gate.jape.*;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A ConstraintPredicate that makes it possible to use value back-references
 * in JAPE rules.
 * <p>
 * The ConstraintPredicate "valueref" must be used with one of the following
 * operators:
 * <ul>
 * <li>!=~ This operator should be used once in its own phase to reset 
 * the data structures between documents
 * <li>!~ This operator should be used as the first operator in a  sequence
 * where identical values should be matched and will initialize a reference
 * with the value.
 * <li>== This operator should be used in all subsequent locations where the
 * value that has been assigned to a reference already should be matched.
 * </ul>
 * <p>
 * Here is an example for a phase to initialize the datastructures. Note that
 * the reference does not matter and only one reference name needs to be used,
 * even if the rest of the phases uses several different reference names.
 * <pre>
 * Phase: reset
 * Input: Token
 * Options: control = once
 * Rule: reset1
 * ({Token valueref {Token.string !=~ "ref"}}) --> {}
 * </pre>
 * <p>
 * <p>
 * Here is an example for a rule that makes shure that three tokens with the
 * same values for feature pos occur after one another:
 * <pre>
 * ({Token valueref {Token.pos !~ "ref"}} 
 *  {Token valueref {Token.pos == "ref"}}
 *  {Token valueref {Token.pos == "ref"}}):label
 * -->:label.ThreeTimes = {}
 * </pre>
 * @author Johann Petrak
 */
public class ValueRef implements ConstraintPredicate {

    private static final long serialVersionUID = -2369138804874616879L;

    protected AnnotationAccessor accessor;

    protected Object value;

    static Map<String, Set<Object>> valueRefsCur = new HashMap<String, Set<Object>>();

    static Map<String, Set<Object>> valueRefsLast = new HashMap<String, Set<Object>>();

    static Map<String, Integer> curOffsets = new HashMap<String, Integer>();

    protected String referenceName = "";

    protected String featureName = "";

    private static final int OPERATOR_EQUALS = 1;

    private static final int OPERATOR_NOTEQUALS = 2;

    private static final int OPERATOR_RESET = 0;

    private static final int OPERATOR_GREATER = 3;

    private static final int OPERATOR_GREATEREQUALS = 4;

    private static final int OPERATOR_LESS = 5;

    private static final int OPERATOR_LESSEQUALS = 6;

    private static final int OPERATOR_CLEAR = 7;

    protected int operator = OPERATOR_EQUALS;

    public ValueRef() {
    }

    public ValueRef(AnnotationAccessor accessor, Object value) {
    }

    public boolean matches(Annotation annot, AnnotationSet context) throws GateRuntimeException {
        if (operator == OPERATOR_CLEAR) {
            curOffsets.clear();
            valueRefsCur.clear();
            valueRefsLast.clear();
            return true;
        }
        Object featureValue = getFeature(annot.getFeatures(), featureName);
        int offset = annot.getStartNode().getOffset().intValue();
        Integer curOffset = curOffsets.get(referenceName);
        if (curOffset == null) {
            curOffset = -1;
        }
        if (operator == OPERATOR_RESET) {
            if (curOffset == offset) {
                valueRefsCur.get(referenceName).add(featureValue);
            } else {
                curOffsets.put(referenceName, offset);
                Set<Object> newset = new HashSet<Object>();
                newset.add(featureValue);
                valueRefsCur.put(referenceName, newset);
                valueRefsLast.remove(referenceName);
            }
            return true;
        }
        if (curOffset != offset) {
            curOffsets.put(referenceName, offset);
            valueRefsLast.put(referenceName, valueRefsCur.get(referenceName));
            valueRefsCur.put(referenceName, new HashSet<Object>());
        }
        if (operator == OPERATOR_EQUALS) {
            boolean ret = equal(valueRefsLast.get(referenceName), featureValue);
            if (ret) {
                valueRefsCur.get(referenceName).add(featureValue);
            }
            return ret;
        } else {
            throw new GateRuntimeException("Operator not (yet) supported");
        }
    }

    public void setAccessor(AnnotationAccessor accessor) {
        this.accessor = accessor;
    }

    public AnnotationAccessor getAccessor() {
        return this.accessor;
    }

    public void setValue(Object value) {
        this.value = value;
        if (!(value instanceof Constraint)) {
            throw new GateRuntimeException("Not a constraint: " + value);
        }
        Constraint constraint = (Constraint) value;
        if (constraint.isNegated()) {
            throw new GateRuntimeException("Constraint must not be negated: " + constraint);
        }
        if (constraint.getAttributeSeq().size() != 1) {
            throw new GateRuntimeException("Constraint must have one predicate: " + constraint);
        }
        String annType = constraint.getAnnotType();
        ConstraintPredicate pred = constraint.getAttributeSeq().get(0);
        String op = pred.getOperator();
        if (op.equals("!~")) {
            operator = OPERATOR_RESET;
        } else if (op.equals("!=~")) {
            operator = OPERATOR_CLEAR;
        } else if (op.equals("==")) {
            operator = OPERATOR_EQUALS;
        } else {
            throw new GateRuntimeException("Constraint operator must be !~ (reset),  !=~ (clear), or == (equal to ref)");
        }
        Object predObj = pred.getValue();
        String predValue = predObj.toString();
        referenceName = predValue;
        AnnotationAccessor aa = pred.getAccessor();
        String key = aa.getKey().toString();
        featureName = key;
    }

    public Object getValue() {
        return this.value;
    }

    public String getOperator() {
        return "valueref";
    }

    protected boolean equal(Set<Object> set, Object b) {
        if (b == null) {
            return false;
        }
        for (Object a : set) {
            if (a.equals(b)) {
                return true;
            }
        }
        return false;
    }

    protected int compare(Object a, Object b) {
        if (a != null && b != null) {
            if (a.getClass().equals(b.getClass())) {
                if (a instanceof Comparable) {
                    return ((Comparable) a).compareTo((Comparable) b);
                } else {
                    throw new GateRuntimeException("less than or equal comparison but not both are comparable: a=" + a + ",b=" + b);
                }
            } else {
                throw new GateRuntimeException("less than or equal comparison but not both of the same class: a=" + a + ",b=" + b);
            }
        } else {
            throw new GateRuntimeException("less than or equal comparison but not both different from null: a=" + a + ",b=" + b);
        }
    }

    private Object getFeature(FeatureMap fm, String name) {
        Object ret = fm.get(name);
        if (ret == null) {
            warnOnce("Null value in valueRef for feature " + name);
        }
        return ret;
    }

    private void warnOnce(String message) {
        if (!alreadyWarned.contains(message)) {
            alreadyWarned.add(message);
            System.out.println(message);
        }
    }

    private Set<String> alreadyWarned = new HashSet<String>();
}
