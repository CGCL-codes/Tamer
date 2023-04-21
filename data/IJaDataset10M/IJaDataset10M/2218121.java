package boolvarpb.input;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * A PseudoBooleanLeqConstraint is an input constraint of the form
 * a1 l1 + ... + an ln <= b.
 * @author Olivier Bailleux
 */
public class PseudoBooleanLeqConstraint extends GenericConstraint {

    ArrayList<BigInteger> coeffs;

    ArrayList<Literal> lits;

    BigInteger bound;

    /**
     * Creates an empty constraint.
     */
    public PseudoBooleanLeqConstraint() {
        coeffs = new ArrayList<BigInteger>();
        lits = new ArrayList<Literal>();
        bound = null;
    }

    /**
     * Creates a constraint.
     * @param c coefficients.
     * @param l literals.
     * @param b bound.
     */
    public PseudoBooleanLeqConstraint(int[] c, Literal[] l, int b) {
        if ((c.length == 0) || (l.length == 0) || (c.length != l.length)) throw new RuntimeException("Bad arguments");
        coeffs = new ArrayList<BigInteger>(c.length);
        lits = new ArrayList<Literal>(c.length);
        bound = new BigInteger("" + b);
        for (int i = 0; i < c.length; i++) {
            coeffs.add(new BigInteger(c[i] + ""));
            lits.add(l[i]);
        }
    }

    /**
     * Creates a constraint.
     * @param c coefficients.
     * @param l literals.
     * @param b bound.
     */
    public PseudoBooleanLeqConstraint(BigInteger[] c, Literal[] l, BigInteger b) {
        if ((c.length == 0) || (l.length == 0) || (c.length != l.length)) throw new RuntimeException("Bad arguments");
        coeffs = new ArrayList<BigInteger>(c.length);
        lits = new ArrayList<Literal>(c.length);
        bound = b;
        for (int i = 0; i < c.length; i++) {
            coeffs.add(c[i]);
            lits.add(l[i]);
        }
    }

    /**
     * Creates a constraint.
     * @param c coefficients.
     * @param l literals.
     * @param b bound.
     */
    public PseudoBooleanLeqConstraint(ArrayList<BigInteger> c, ArrayList<Literal> l, BigInteger b) {
        if ((c.size() == 0) || (l.size() == 0) || (c.size() != l.size())) throw new RuntimeException("Bad arguments");
        coeffs = c;
        lits = l;
        bound = b;
    }

    /**
     * Sets the bound.
     * @param b bound.
     */
    public void setBound(BigInteger b) {
        bound = b;
    }

    /**
     * Sets the bound.
     * @param b bound.
     */
    public void setBound(int b) {
        bound = new BigInteger(b + "");
    }

    /**
     * Adds a new term.
     * @param c coefficient.
     * @param l literal.
     */
    public void addTerm(int c, Literal l) {
        coeffs.add(new BigInteger(c + ""));
        lits.add(l);
    }

    /**
     * Adds a new term.
     * @param c coefficient.
     * @param l literal.
     */
    public void addTerm(BigInteger c, Literal l) {
        coeffs.add(c);
        lits.add(l);
    }

    /**
     * Adds a new term.
     * @param c coefficient.
     * @param v variable.
     */
    public void addTerm(int c, Variable v) {
        coeffs.add(new BigInteger(c + ""));
        lits.add(v.posLit);
    }

    /**
     * Adds a new term.
     * @param c coefficient.
     * @param v variable.
     */
    public void addTerm(BigInteger c, Variable v) {
        coeffs.add(c);
        lits.add(v.posLit);
    }

    /**
     * Translates the current constraint into a raw constraint.
     * @return the resulting raw constraint.
     */
    public RawConstraint[] getCanonicalConstraints() {
        RawConstraint q = new RawConstraint();
        RawConstraint[] out = { q };
        q.setBound(bound);
        q.setParams(coeffs.toArray(new BigInteger[0]));
        q.setLits(lits.toArray(new Literal[0]));
        q.setType(RawConstraint.LEQ);
        return out;
    }

    @Override
    public String toString() {
        String output = coeffs.get(0).toString() + "." + lits.get(0).toString();
        for (int i = 1; i < coeffs.size(); i++) {
            output += (" + " + coeffs.get(i).toString() + "." + lits.get(i).toString());
        }
        output += (" <= " + bound.toString());
        return output;
    }
}
