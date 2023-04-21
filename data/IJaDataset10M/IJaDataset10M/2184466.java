package lelouet.datacenter.thermal.impacts;

import static choco.Choco.minus;
import static choco.Choco.mult;
import static choco.Choco.plus;
import lelouet.datacenter.ArrayModel;
import lelouet.datamap.DataMap;
import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.variables.real.RealExpressionVariable;
import choco.kernel.model.variables.real.RealVariable;

/**
 * specifies and solve a problem of linear regression of energy to heat.<br />
 * This must be given enough data to be solved, and if there are too many data
 * to have an exact solution, it tries to approximate the values to get the
 * lowest error.<br />
 * The heat can either be the one entering or exiting the servers, but once
 * selected, it should always be from the same position.
 * 
 * @author lelouet
 * 
 */
public class ChocoImpactInterpoler extends ArrayModel {

    CPSolver solver = new CPSolver();

    CPModel model = new CPModel();

    @SuppressWarnings("deprecation")
    public ChocoImpactInterpoler(ArrayModel model) {
        super(model);
        addConstantLine();
        addImpactsVariables();
        solver.setDoMaximize(false);
    }

    /**
	 * computes a simili-distance between two vectors of RealsExpressionVariable
	 * or one fo {@link RealExpressionVariable} and one of double.
	 */
    public static interface Distance {

        public RealExpressionVariable evaluate(RealExpressionVariable[] vect1, double[] vect2);
    }

    public static class dist2 implements Distance {

        @Override
        public RealExpressionVariable evaluate(RealExpressionVariable[] vect1, double[] vect2) {
            RealExpressionVariable sumsqr = Choco.power(minus(vect1[0], vect2[0]), 2);
            for (int i = 1; i < vect1.length; i++) {
                sumsqr = plus(sumsqr, Choco.power(minus(vect1[i], vect2[i]), 2));
            }
            return sumsqr;
        }
    }

    private Distance dist;

    public void setDist(Distance dist) {
        this.dist = dist;
    }

    protected RealVariable[] constantLine;

    protected void addConstantLine() {
        int size = getCoding().size();
        constantLine = new RealVariable[size];
        for (int i = 0; i < size; i++) {
            RealVariable c = new RealVariable("c" + i, 0, Double.POSITIVE_INFINITY);
            constantLine[i] = c;
            model.addVariable(c);
        }
    }

    protected RealVariable[][] impactMatrix;

    protected void addImpactsVariables() {
        int size = getCoding().size();
        impactMatrix = new RealVariable[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                RealVariable imp = new RealVariable(impactName(i, j), 0.0, Double.POSITIVE_INFINITY);
                impactMatrix[i][j] = imp;
                model.addVariable(imp);
            }
        }
    }

    protected String impactName(int serverFrom, int serverTo) {
        return "impact" + serverFrom + "to" + serverTo;
    }

    /**
	 * try to convert the datamaps to double[] then return
	 * {@link #addPoint(double[], double[])}
	 */
    public boolean addPoint(DataMap energy, DataMap thermal) {
        return addPoint(convertMap(energy), convertMap(thermal));
    }

    protected RealExpressionVariable sumdiffs = null;

    /**
	 * handles a conversion from energy to heat at a moment. Indices are those
	 * used for the {@link #getCoding()}
	 * 
	 * @param energy
	 *            the array of consumed energies for each server at index i.
	 * @param thermal
	 *            the array of incoming or outgoing air temperatures for each
	 *            server at index i.
	 */
    public boolean addPoint(double[] energy, double[] thermal) {
        if (energy.length != thermal.length || energy.length != getCoding().size()) {
            return false;
        }
        int size = energy.length;
        RealExpressionVariable[] variableHeatVector = new RealExpressionVariable[size];
        for (int j = 0; j < size; j++) {
            RealExpressionVariable ServerIncommingHeat = constantLine[j];
            for (int i = 0; i < size; i++) {
                ServerIncommingHeat = plus(ServerIncommingHeat, mult(energy[i], impactMatrix[i][j]));
            }
            variableHeatVector[j] = ServerIncommingHeat;
        }
        RealExpressionVariable diff = dist.evaluate(variableHeatVector, thermal);
        if (sumdiffs == null) {
            sumdiffs = diff;
        } else {
            sumdiffs = plus(sumdiffs, diff);
        }
        return true;
    }

    public double sumError = Double.POSITIVE_INFINITY;

    public LinearModel solve() {
        model.addVariable(sumdiffs);
        solver.read(model);
        throw new RuntimeException();
    }
}
