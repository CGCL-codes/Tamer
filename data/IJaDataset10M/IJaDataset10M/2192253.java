package org.omegahat.Simulation.MCMC.Proposals;

import org.omegahat.Simulation.MCMC.*;
import org.omegahat.Probability.Distributions.MVNormal;
import org.omegahat.Simulation.RandomGenerators.*;
import org.omegahat.GUtilities.ArrayTools;
import java.util.Arrays;
import java.lang.Math;

public class KernelDirectionSampler implements HastingsCoupledProposal {

    public boolean DEBUG = false;

    protected int numPoints = 100;

    protected double[][] proposalVar = null;

    protected MVNormal normal;

    protected PRNG prng;

    protected static double EPSILON = 1e-9;

    protected double[] X1;

    protected double[] X2;

    protected double[] Y;

    protected double[][] sample;

    protected double[] weightsWithX1;

    protected double[] weightsWithoutX1;

    protected double[] weightsWithY;

    protected int Yindex;

    double[] lowerEdge;

    double[] upperEdge;

    int getNumPoints() {
        return numPoints;
    }

    int setNumPoints(int num) {
        return numPoints = num;
    }

    double[][] getProposalVar() {
        return proposalVar;
    }

    double[][] getProposalVar(double[][] var) {
        return proposalVar = var;
    }

    protected void wipeFields() {
        X1 = null;
        X2 = null;
        Y = null;
        sample = null;
        weightsWithX1 = null;
        weightsWithoutX1 = null;
        weightsWithY = null;
        Yindex = -1;
    }

    public KernelDirectionSampler(int numPoints, double[][] var, PRNG prng) {
        this.numPoints = numPoints;
        this.proposalVar = var;
        this.prng = prng;
        this.normal = new MVNormal(var, prng);
    }

    public double conditionalPDF(Object state, Object conditions, int which, MultiState stateVector) {
        return transitionProbability(conditions, state, which, stateVector);
    }

    public double logConditionalPDF(Object state, Object conditions, int which, MultiState stateVector) {
        return logTransitionProbability(conditions, state, which, stateVector);
    }

    public double transitionProbability(Object from, Object to, int which, MultiState stateVector) {
        MultiDoubleState states = new MultiDoubleState(stateVector);
        double[] source = ArrayTools.Otod(from);
        double[] target = ArrayTools.Otod(to);
        if (Arrays.equals(target, Y) && Arrays.equals(source, X1)) return weightsWithX1[Yindex]; else if (Arrays.equals(target, X1) && Arrays.equals(source, Y)) {
            if (weightsWithY != null) return weightsWithY[Yindex]; else {
                weightsWithY = new double[weightsWithoutX1.length];
                double cumsumWithY = 0.0;
                normal.setMean(Y);
                for (int i = 0; i < weightsWithY.length; i++) {
                    weightsWithY[i] = weightsWithoutX1[i];
                    if (i != Yindex) weightsWithY[i] += normal.PDF(sample[i]); else weightsWithY[i] += normal.PDF(X1);
                    cumsumWithY += weightsWithY[i];
                }
                for (int i = 0; i < weightsWithY.length; i++) {
                    weightsWithY[i] /= cumsumWithY;
                }
                return weightsWithY[Yindex];
            }
        } else return 0.0;
    }

    public double logTransitionProbability(Object from, Object to, int which, MultiState stateVector) {
        return Math.log(transitionProbability(from, to, which, stateVector));
    }

    boolean isOnLine(double[] point, double[] X1, double[] X2) {
        double[] a = new double[point.length];
        for (int i = 0; i < point.length; i++) {
            a[i] = (point[i] - X1[i]) / (X2[i] - X1[i]);
            if (Math.abs(a[i] - a[0]) > EPSILON) return false;
        }
        return true;
    }

    public Object generate(Object currentComponent, int which, MultiState currentState) {
        wipeFields();
        MultiDoubleState newState = new MultiDoubleState(currentState);
        int dim = ((double[]) newState.get(1)).length;
        int size = newState.size();
        if (size <= 1) throw new RuntimeException("KernelDirectionSampler only operates" + "on MultiStates with at least two components");
        double[] min = newState.min();
        double[] max = newState.max();
        for (int i = 0; i < dim; i++) {
            min[i] = min[i] - 2 * proposalVar[i][i];
            max[i] = max[i] + 2 * proposalVar[i][i];
        }
        int other = which;
        while (other == which) {
            other = (int) (prng.nextDouble() * size);
        }
        X1 = ArrayTools.Otod(currentComponent);
        X2 = ArrayTools.Otod(currentState.get(other));
        sample = generateSample(X1, X2, min, max);
        computeWeights(sample, currentState, which);
        double randval = prng.nextDouble();
        Yindex = -1;
        double cumProb = 0.0;
        for (int i = 0; i < weightsWithX1.length; i++) {
            cumProb += weightsWithX1[i];
            if (randval < cumProb) ;
            {
                Yindex = i;
                break;
            }
        }
        if (Yindex < 0) throw new RuntimeException("Internal error, unable to resample.");
        Y = sample[Yindex];
        return (Y);
    }

    public void computeEdgePoints(double[] X1, double[] X2, double[] min, double[] max) {
        int dim = X1.length;
        double[] min_a = new double[dim];
        double[] max_a = new double[dim];
        double lower_a = Double.NEGATIVE_INFINITY;
        double upper_a = Double.POSITIVE_INFINITY;
        for (int i = 0; i < dim; i++) {
            min_a[i] = (min[i] - X1[i]) / (X2[i] - X1[i]);
            max_a[i] = (max[i] - X1[i]) / (X2[i] - X1[i]);
            if (min_a[i] < 0 && min_a[i] > lower_a) lower_a = min_a[i];
            if (max_a[i] < 0 && max_a[i] > lower_a) lower_a = max_a[i];
            if (min_a[i] > 1 && min_a[i] < upper_a) upper_a = min_a[i];
            if (max_a[i] > 1 && max_a[i] < upper_a) upper_a = max_a[i];
        }
        lowerEdge = new double[dim];
        upperEdge = new double[dim];
        for (int i = 0; i < dim; i++) {
            lowerEdge[i] = X1[i] + lower_a * (X2[i] - X1[i]);
            upperEdge[i] = X1[i] + upper_a * (X2[i] - X1[i]);
        }
        return;
    }

    public double[][] generateSample(double[] X1, double[] X2, double[] min, double[] max) {
        computeEdgePoints(X1, X2, min, max);
        int dim = X1.length;
        double rand;
        double[][] sample = new double[numPoints][dim];
        for (int i = 0; i < numPoints; i++) {
            rand = prng.nextDouble();
            for (int j = 0; j < dim; j++) {
                sample[i][j] = lowerEdge[j] + rand * (upperEdge[j] - lowerEdge[j]);
            }
        }
        return sample;
    }

    protected void computeWeights(double[][] sample, MultiState currentState, int which) {
        weightsWithX1 = new double[sample.length];
        weightsWithoutX1 = new double[sample.length];
        double cumsumWithX1 = 0.0;
        double cumsumWithoutX1 = 0.0;
        double tmp;
        for (int i = 0; i < sample.length; i++) {
            weightsWithX1[i] = 0.0;
            for (int j = 0; j < currentState.size(); j++) {
                normal.setMean(ArrayTools.Otod(currentState.get(j)));
                tmp = normal.PDF(sample[i]);
                weightsWithX1[i] += tmp;
                if (i != which) weightsWithoutX1[i] += tmp;
            }
            cumsumWithX1 += weightsWithX1[i];
        }
        for (int i = 0; i < sample.length; i++) {
            weightsWithX1[i] /= cumsumWithX1;
        }
    }
}
