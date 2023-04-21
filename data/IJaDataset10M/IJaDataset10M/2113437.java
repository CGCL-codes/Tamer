package equation;

import java.util.List;
import Jama.Matrix;
import Jama.QRDecomposition;

public class Newton {

    public int n;

    public List<Term> functions;

    public List<Variable> variables;

    public Newton(List<Term> functions, List<Variable> variables, double epsilon) {
        if (functions.size() != variables.size()) {
            throw new DeterminationException(functions.size(), variables.size());
        }
        n = variables.size();
        this.functions = functions;
        this.variables = variables;
        cStep();
        int i = 0;
        while ((error() > epsilon) && (i < 100)) {
            cStep();
            i++;
        }
        if (i == 100) {
            throw new ConvergenceException();
        }
    }

    public void cStep() {
        try {
            step();
        } catch (RuntimeException r) {
            if (r.getMessage().equals("Matrix is singular.")) {
                for (Variable v : variables) {
                    v.value = +v.value + v.value * 0.001 * Math.random();
                }
            } else {
                throw r;
            }
        }
    }

    public double error() {
        double d = 0;
        for (Term f : functions) {
            double e = f.getValue();
            d += e * e;
        }
        return Math.sqrt(d) / n;
    }

    private void step() {
        double[] x0 = new double[n];
        int i = 0;
        for (Variable v : variables) {
            x0[i] = v.getValue();
            i++;
        }
        Matrix mx0 = new Matrix(x0, n);
        double[] f0 = new double[n];
        i = 0;
        for (Term f : functions) {
            f0[i] = f.getValue();
            i++;
        }
        Matrix mf0 = new Matrix(f0, n);
        Jacobian j = new Jacobian(functions, variables);
        Matrix mj = new Matrix(j.evaluate());
        Matrix ms = mj.solve(mf0);
        mx0 = mx0.minus(ms);
        i = 0;
        for (Variable v : variables) {
            v.setValue(mx0.get(i, 0));
            i++;
        }
    }
}
