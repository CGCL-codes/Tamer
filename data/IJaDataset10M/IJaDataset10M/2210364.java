package boolvarpb.utility;

import boolvarpb.input.Variable;
import boolvarpb.output.PBconst;
import boolvarpb.output.PBformula;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * This class provides resources for testing pseudo-Boolean
 * constraint encoding by assigning some variables and restoring
 * generalized arc consistency.
 */
public class PropagatePB {

    private PBformula formula;

    private ArrayList<Variable> observable;

    private Hashtable<Variable, Boolean> value;

    private boolean unsat;

    private boolean modified;

    /**
     * Creates a testing environment.
     */
    public PropagatePB() {
        formula = new PBformula(1);
        observable = new ArrayList<Variable>();
        value = new Hashtable<Variable, Boolean>();
        unsat = false;
    }

    /**
     * Adds a new formula, i.e., a list of pseudo Boolean
     * constraints, to the current formula.
     * @param f the formula to add.
     */
    public void addFormula(PBformula f) {
        formula.addFormula(f);
    }

    /**
     * Adds a new pseudo Boolean constraint to the current formula.
     * @param t the constraint to add.
     */
    public void addPBterm(PBconst t) {
        formula.addPBconst(t);
    }

    /**
     * Adds a new observable variable.
     * @param v the variable to add.
     */
    public void addVariable(Variable v) {
        observable.add(v);
    }

    /**
     * Adds several new observable variables.
     * @param tab Array of Variables to add.
     */
    public void addVariables(Variable[] tab) {
        observable.addAll(new ArrayList<Variable>(Arrays.asList(tab)));
    }

    /**
     * Adds several new observable variables.
     * @param list ArrayList of Variables to add.
     */
    public void addVariables(ArrayList<Variable> list) {
        observable.addAll(list);
    }

    /**
     * Assigns a value to an observable variable.
     * @param v the variable to be assigned.
     * @param x the value to assignEncoder.
     */
    public void assignVariable(Variable v, boolean x) {
        if (!observable.contains(v)) observable.add(v);
        value.put(v, new Boolean(x));
    }

    /**
     * Assigns a value to each observable variable, according
     * to its rank.
     * @param tab array containing the values that will be assigned
     * to the variables: 0 for true, 1 for false. Other values
     * make the variable unassigned.
     */
    public void assignVariables(int[] tab) {
        if (tab.length > observable.size()) throw (new RuntimeException("To much variables to assign\n"));
        for (int i = 0; i < tab.length; i++) {
            Variable v = observable.get(i);
            if (tab[i] == 0) value.put(v, false); else if (tab[i] == 1) value.put(v, true); else value.remove(v);
        }
    }

    /**
     * Tests if an inconsistency was detected.
     * @return true if and only if an inconsistency was detected.
     */
    public boolean isUnsat() {
        return unsat;
    }

    /**
     * Verifies whether the formula is satisfied by the
     * current assignation.
     * @return true if and only if the formula is empty.
     */
    public boolean isTrue() {
        return formula.size() == 0;
    }

    /**
     * Gives the value assigned to a variable.
     * @param v the variable to test.
     * @return the value assigned to v: 0 for false, 1 for true, 2 if
     * v is not assigned.
     */
    public int getValue(Variable v) {
        if (!value.containsKey(v)) return 2;
        if (value.get(v)) return 1; else return 0;
    }

    /**
     * Tests whether the values assigned to the observable variables
     * match with the values provided as a mask. The mask is an Array
     * of integers: 0 for false, 1 for true, other values will be ignored.
     * @param tab the mask.
     * @return true if the values assigned to the observable variables
     * match with the mask.
     */
    public boolean testValues(int[] tab) {
        if (tab.length > observable.size()) throw (new RuntimeException("To much variables to assign\n"));
        for (int i = 0; i < tab.length; i++) {
            if ((tab[i] == 0) || (tab[i] == 1)) {
                if (!value.containsKey(observable.get(i))) return false;
                Variable v = observable.get(i);
                if (tab[i] == 0) {
                    if (value.get(v)) return false;
                } else if (!value.get(v)) return false;
            }
        }
        return true;
    }

    /**
     * Splits a constraint with operator "=" into two constraints
     * with operator ">=".
     * @param t the constraint to split.
     * @return the resulting constraints in an ArrayList.
     */
    private PBformula splitBP(PBconst t) {
        if (t.getOperator() != 1) throw (new RuntimeException("PBterm with opetaor = expected\n"));
        PBformula res = new PBformula(2);
        PBconst t1 = new PBconst();
        PBconst t2 = new PBconst();
        for (int i = 0; i < t.size(); i++) {
            Variable var = t.getVariable(i);
            BigInteger coeff = t.getCoeff(i);
            t1.addMonome(var, coeff);
            t2.addMonome(var, coeff.negate());
        }
        t1.setOperator(2);
        t1.setBound(t.getBound());
        t2.setOperator(2);
        t2.setBound(t.getBound().negate());
        res.addPBconst(t1);
        res.addPBconst(t2);
        return res;
    }

    /**
     * Simplifies a constraint by removing the variables
     * that are assigned.
     * @param t the constraint to simplify.
     * @return true if and only if the resulting constraint is false.
     */
    private boolean simplifyPB(PBconst t) {
        int n = t.size();
        int i = 0;
        while (i < n) {
            Variable var = t.getVariable(i);
            BigInteger coeff = t.getCoeff(i);
            if (value.containsKey(var)) {
                if (value.get(var)) t.setBound(t.getBound().subtract(coeff));
                t.removeMonome(i);
                n--;
                modified = true;
            } else i++;
        }
        return (t.size() == 0) && (t.getBound().compareTo(BigInteger.ZERO) > 0);
    }

    /**
     * Checks a constraint with operator ">=" for inconsistency.
     * @param t the constraint to test.
     * @return true if the constraint cannot be satisfied, whatever
     * the values of its variables.
     */
    private boolean testInconsistency(PBconst t) {
        BigInteger max = BigInteger.ZERO;
        for (int i = 0; i < t.size(); i++) if (t.getCoeff(i).compareTo(BigInteger.ZERO) > 0) max = max.add(t.getCoeff(i));
        return max.compareTo(t.getBound()) < 0;
    }

    /**
     * Assigns any variable of a given constraint when one of this
     * two possible values makes the constraint false. Do not modify
     * the constraint, but only (if applicable) the values assigned to
     * its variables.
     * @param t the input constraint.
     * @return true if and only if a new assignation is inconsistent
     * with an existing one.
     */
    private boolean filteringPB(PBconst t) {
        if (testInconsistency(t)) return true;
        for (int i = 0; i < t.size(); i++) {
            BigInteger coeff = t.getCoeff(i);
            Variable var = t.getVariable(i);
            PBconst t1 = new PBconst(t);
            t1.removeMonome(i);
            if (coeff.compareTo(BigInteger.ZERO) < 0) {
                t1.setBound(t1.getBound().subtract(coeff));
                if (testInconsistency(t1)) {
                    if (value.containsKey(var)) if (value.get(var)) return true;
                    value.put(var, false);
                    modified = true;
                }
            } else {
                if (testInconsistency(t1)) {
                    if (value.containsKey(var)) if (!value.get(var)) return true;
                    value.put(var, true);
                    modified = true;
                }
            }
        }
        return false;
    }

    /**
     * Removes all the constraints with operators "=" and
     * replaces them by constraints with operator ">=".
     */
    private void normalizePBterms() {
        PBformula newFormula = new PBformula();
        for (int i = 0; i < formula.size(); i++) {
            PBconst t = (PBconst) formula.get(i);
            if (t.getOperator() == 2) newFormula.addPBconst(t); else {
                PBformula newTerms = splitBP(t);
                newFormula.addFormula(newTerms);
            }
        }
        formula = newFormula;
    }

    /**
     * Achieves all possible propagations, filtering and simplifications
     * until one of the following facts hold: the formula is satisfied,
     * an inconsistency is detected, or no more propagation can be done.
     */
    public void propagate() {
        normalizePBterms();
        do {
            modified = false;
            int i = 0;
            int n = formula.size();
            while (i < n) {
                PBconst t = (PBconst) formula.get(i);
                if (simplifyPB(t)) {
                    unsat = true;
                    return;
                } else if (t.size() == 0) {
                    formula.remove(i);
                    n--;
                } else i++;
            }
            if (isTrue()) return;
            n = formula.size();
            for (i = 0; i < n; i++) {
                if (filteringPB((PBconst) formula.get(i))) {
                    unsat = true;
                    return;
                }
            }
        } while (modified);
    }

    /**
     * Gives a string representation of the current formula.
     * @return the resulting string.
     */
    @Override
    public String toString() {
        String output = "";
        output += "Formula :\n";
        for (int i = 0; i < formula.size(); i++) output += (formula.get(i).toString() + "\n");
        output += "Variables :\n";
        for (int i = 0; i < observable.size(); i++) {
            Variable var = observable.get(i);
            output += (var.toString() + " ");
            if (!value.containsKey(var)) output += "[X]\n"; else if (value.get(var)) output += "[1]\n"; else output += "[0]\n";
        }
        output += "Status : ";
        if (isUnsat()) output += "UNSAT\n"; else if (isTrue()) output += "TRUE\n"; else output += "UNKNOW\n";
        return output;
    }

    public static void test() {
        PropagatePB prop = new PropagatePB();
        Variable[] vars = new Variable[5];
        for (int i = 0; i < vars.length; i++) vars[i] = new Variable();
        PBformula f = new PBformula();
        PBconst t = new PBconst();
        t.addMonome(vars[0], 1);
        t.addMonome(vars[1], -2);
        t.addMonome(vars[2], 1);
        t.setOperator(1);
        t.setBound(1);
        f.addPBconst(t);
        t = new PBconst();
        t.addMonome(vars[1], 1);
        t.addMonome(vars[3], 2);
        t.addMonome(vars[4], -1);
        t.setOperator(2);
        t.setBound(2);
        f.addPBconst(t);
        t = new PBconst();
        t.addMonome(vars[0], 2);
        t.addMonome(vars[2], -1);
        t.setOperator(2);
        t.setBound(1);
        f.addPBconst(t);
        prop.addFormula(f);
        prop.addVariables(vars);
        System.out.println(prop.toString());
        prop.propagate();
        System.out.println(prop.toString());
    }
}
