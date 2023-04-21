package jgrx.iface.impl.terms;

import jgrx.iface.Term;
import java.util.Map;
import java.util.Set;
import jgrx.iface.impl.util.Command;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import jgrx.iface.impl.util.FunctionUtil;

public abstract class AbstractTerm implements Term {

    public static final HashSet<String> X;

    public static final String x = "x";

    static DecimalFormat decimalFormat;

    private static int sigfigs;

    static {
        HashSet<String> set = new HashSet<String>();
        set.add(x);
        X = set;
        decimalFormat = new DecimalFormat();
        sigfigs = 3;
    }

    public static String formatNumber(double d) {
        return decimalFormat.format(d);
    }

    public static void setSignificant(int digit) {
        String s = "#.";
        for (int i = 0; i < digit; i++) s += "#";
        decimalFormat = new DecimalFormat(s);
        sigfigs = digit;
    }

    public static int getSignificant() {
        return sigfigs;
    }

    public static boolean checkVar(char c) {
        return (Character.isLetter((int) c) && c != 'e' && c != 'π');
    }

    public static String[] getSortedVars(Set<String> h) {
        String[] c = new String[h.size()];
        Iterator<String> it = h.iterator();
        int i = 0;
        while (it.hasNext()) {
            c[i] = it.next();
            i++;
        }
        Arrays.sort(c);
        return c;
    }

    double coefficient, power;

    private HashSet<String> vars;

    String[] sortedVars;

    boolean sortUptoDate, hasMultipleVars;

    public AbstractTerm() {
        this(1, 1);
    }

    public AbstractTerm(double c, double p) {
        coefficient = c;
        power = p;
        setVars(new HashSet<String>());
    }

    public double solve(double d) {
        return coefficient * Math.pow(d, power);
    }

    public double[] solve_inverse(double y) {
        return FunctionUtil.solveFor(this, y);
    }

    public double find_inverse(double x) {
        return FunctionUtil.getRoot(this, x);
    }

    public Term getIntegral() {
        throw new UnsupportedOperationException("Whee! Integration not implemented");
    }

    public Term add(double d) {
        return TermUtil.add(copy(), d);
    }

    public Term add(Term t) {
        return TermUtil.add(copy(), t);
    }

    public Term sbt(double d) {
        return TermUtil.sbt(copy(), d);
    }

    public Term sbt(Term t) {
        return TermUtil.sbt(copy(), t);
    }

    public Term mul(double p) {
        Term t = copy();
        t.setCoefficient(coefficient * p);
        return t;
    }

    public Term mul(Term t) {
        if (t.isConstant()) return mul(t.solve(0));
        return TermUtil.mul(copy(), t);
    }

    public Term div(double p) {
        Term t = copy();
        t.setCoefficient(coefficient / p);
        return t;
    }

    public Term div(Term t) {
        if (t.isConstant()) return div(t.solve(0));
        return TermUtil.div(copy(), t);
    }

    public Term pow(double p) {
        Term t = copy();
        t.setCoefficient(Math.pow(t.getCoefficient(), p));
        t.setPower(t.getPower() * p);
        return t;
    }

    public Term pow(Term t) {
        if (t.isConstant()) return pow(t.solve(0));
        return TermUtil.pow(copy(), t);
    }

    public boolean isConstant() {
        if (coefficient == 0) return true;
        return (power == 0);
    }

    public double constantValue() {
        if (coefficient == 0) {
            return 0;
        }
        if (power == 0) {
            return coefficient;
        }
        throw new UnsupportedOperationException("Term is not constant!");
    }

    public boolean isArray() {
        return false;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double d) {
        coefficient = d;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double d) {
        power = d;
    }

    public boolean equals(Term t) {
        return (power == t.getPower() && coefficient == t.getCoefficient());
    }

    public int compareTo(Term t) {
        double dif = power - t.getPower();
        if (dif > 0) return 1;
        if (dif < 0) return -1;
        return 0;
    }

    public String varsToString() {
        if (sortedVars == null) {
            sortedVars = getSortedVars(getVars());
        }
        String s = "(";
        for (int i = 0; i < sortedVars.length; i++) {
            s += sortedVars[i];
            if (i < sortedVars.length - 1) s += ",";
        }
        s += ")";
        return s;
    }

    public String toString() {
        if (coefficient == 0) return "0";
        if (power == 0) return decimalFormat.format(coefficient);
        String s;
        if (coefficient == -1) s = "-"; else if (coefficient == 1) s = ""; else s = decimalFormat.format(coefficient);
        s += termToString();
        if (power != 1) s += decimalFormat.format(power);
        return s;
    }

    public HashSet<String> getVars() {
        return vars;
    }

    public void setVars(HashSet<String> vars) {
        this.vars = vars;
    }

    public void setVariables(Map<String, String> cs) {
        Iterator<String> iter = cs.keySet().iterator();
        while (iter.hasNext()) {
            String c = iter.next();
            if (vars.contains(c)) {
                vars.remove(c);
                vars.add(cs.get(c));
            }
        }
    }
}
