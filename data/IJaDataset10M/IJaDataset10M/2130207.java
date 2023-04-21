package ar.edu.unicen.exa.server.ia.variable;

import java.util.Vector;

/**
 * 
 * @author tio, chuki , maikel
 */
public class IABinnerFunction extends IAFunction {

    public IABinnerFunction(IAVariable variable1) {
        super(variable1, null);
    }

    Vector<Comparable> limits = new Vector<Comparable>();

    public void addLimit(Comparable l) {
        limits.add(l);
    }

    @Override
    public Object calculate(String user) {
        int i = 0;
        Comparable v = (Comparable) this.getVariable1().calculate(user);
        while (i < limits.size()) {
            Comparable limit = limits.get(i);
            if (v.compareTo(limit) == -1) return i;
        }
        return limits.size();
    }
}
