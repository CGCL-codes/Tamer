package com.rapidminer.operator.learner.sequence.crf.tools;

import java.util.ArrayList;
import java.util.Collections;
import riso.numerical.LBFGS;
import cern.colt.function.DoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.Functions;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.tools.LogService;

/**
 * This class uses LBFGS (quasi-newton method) to optimize the lambda-values for
 * a CRF.
 * 
 * @author Felix Jungermann
 * @version $Id: LBFGSLogLikelihood.java,v 1.7 2009-03-12 13:30:29 jungerma Exp $
 */
public class LBFGSLogLikelihood {

    ArrayList<Integer> features2ProcessFor, features2ProcessBack;

    int fIndex, bIndex;

    ArrayList<Attribute> attributes;

    DenseDoubleMatrix1D alphaValues, tempAlphaValues;

    DenseDoubleMatrix1D[] betaValues;

    double[] scaling;

    protected DoubleMatrix1D calculatingMatrixValues;

    MultSingle scalingMultiplier = new MultSingle();

    ForwardBackward.mult m = new ForwardBackward.mult();

    DenseDoubleMatrix2D transMatrix;

    DenseDoubleMatrix1D stateMatrix;

    double[] gradient, lambda, startLambda, diag;

    int[] iprint, iflag;

    int counter = 0;

    double tolerance = 1.0e-16;

    double f;

    FeatureSet fSet;

    public FeatureSet getFSet() {
        return fSet;
    }

    public void setFSet(FeatureSet set) {
        fSet = set;
    }

    public double calculateZ(ArrayList<DenseDoubleMatrix2D> matrices, double[] scale) {
        double result = 0;
        if (matrices.size() > 0) {
            for (int i = 0; i < matrices.size(); i++) {
                double valueMatrix = matrices.get(i).aggregate(Functions.plus, Functions.log);
                result += valueMatrix;
            }
        }
        return result;
    }

    public double getFitness() {
        return norm(gradient) / norm(lambda);
    }

    public boolean optimizeStep(double epsForConvergence, boolean ArrayMethod) throws Exception {
        if (!ArrayMethod) {
            f = getLogLikelihood(gradient, fSet);
        } else {
            f = getLogLikelihoodNEW(gradient, fSet, counter);
        }
        f *= (-1);
        for (int i = 0; i < gradient.length; i++) {
            gradient[i] *= (-1);
        }
        LBFGS.lbfgs(lambda.length, 7, lambda, f, gradient, false, diag, iprint, epsForConvergence, tolerance, iflag);
        LogService.getGlobal().logError("Optimization round no. " + counter);
        fSet.setLambda(lambda);
        if (getFitness() <= epsForConvergence) return true;
        counter++;
        return false;
    }

    public void init() throws Exception {
        diag = new double[fSet.size()];
        iprint = new int[] { -1, 0 };
        iflag = new int[] { 0 };
        fSet.reset();
        ArrayList<Feature> listOfFeatures = new ArrayList<Feature>();
        while (fSet.hasNext()) {
            listOfFeatures.add(fSet.next());
        }
        gradient = new double[fSet.size()];
        lambda = fSet.getLambda();
        startLambda = fSet.getLambda();
    }

    public FeatureSet optimization(FeatureSet fSet, double epsForConvergence, boolean ArrayMethod) throws Exception {
        init();
        boolean optimized = false;
        try {
            do {
                optimized = optimizeStep(epsForConvergence, ArrayMethod);
            } while (!optimized && counter < 100);
        } catch (LBFGS.ExceptionWithIflag e) {
            System.err.println("CRF: lbfgs failed.\n" + e);
            if (e.iflag == -1) {
                System.err.println("Possible reasons could be: \n \t 1. Bug in the feature generation or data handling code\n\t 2. Not enough features to make observed feature value==expected value\n");
            }
            return null;
        }
        fSet.setLambda(lambda);
        return fSet;
    }

    void initMatrices(int numY) {
        if (transMatrix == null || transMatrix.size() != numY) {
            transMatrix = new DenseDoubleMatrix2D(numY, numY);
        }
        if (stateMatrix == null || stateMatrix.size() != numY) {
            stateMatrix = new DenseDoubleMatrix1D(numY);
        }
        if (alphaValues == null || alphaValues.size() != numY) {
            alphaValues = new DenseDoubleMatrix1D(numY);
        }
        if (tempAlphaValues == null || tempAlphaValues.size() != numY) {
            tempAlphaValues = new DenseDoubleMatrix1D(numY);
        }
        if (calculatingMatrixValues == null || calculatingMatrixValues.size() != numY) {
            calculatingMatrixValues = new DenseDoubleMatrix1D(numY);
        }
    }

    protected double addPrior(double lambda[], double grad[], double logli) {
        for (int f = 0; f < lambda.length; f++) {
            grad[f] = -1 * lambda[f] * 0.01;
            logli -= ((lambda[f] * lambda[f]) * 0.01) / 2;
        }
        return logli;
    }

    private double getLogLikelihood(double[] grad, FeatureSet fSet) {
        double logli = 0.0;
        logli = addPrior(lambda, grad, logli);
        double[] expectationF = new double[fSet.size()];
        Label2IntegerMapping mapping = fSet.getMapping();
        int numY = fSet.getMapping().size();
        initMatrices(numY);
        fSet.reset();
        SequenceIterator sIter = fSet.getSIterator();
        int sCounter = 0;
        while (sIter.hasNext()) {
            LogService.getGlobal().logError("optimizing Sequence no. " + sCounter++);
            Sequence s = sIter.next();
            alphaValues.assign(1);
            for (int f = 0; f < lambda.length; f++) expectationF[f] = 0;
            if ((betaValues == null) || (betaValues.length < s.size())) {
                betaValues = new DenseDoubleMatrix1D[s.size()];
                for (int i = 0; i < betaValues.length; i++) betaValues[i] = new DenseDoubleMatrix1D(numY);
                scaling = new double[s.size()];
            }
            scaling[s.size() - 1] = numY;
            betaValues[s.size() - 1].assign(1.0 / scaling[s.size() - 1]);
            long t = System.currentTimeMillis();
            for (int i = s.size() - 1; i > 0; i--) {
                computeMatrices(fSet, s, i, lambda, transMatrix, stateMatrix);
                calculatingMatrixValues.assign(betaValues[i]);
                calculatingMatrixValues.assign(stateMatrix, m);
                ForwardBackward.Mult(transMatrix, calculatingMatrixValues, betaValues[i - 1], 1, 0, false);
                scaling[i - 1] = betaValues[i - 1].zSum();
                if ((scaling[i - 1] < 1) && (scaling[i - 1] > -1)) scaling[i - 1] = 1;
                scalingMultiplier.mult = 1.0 / scaling[i - 1];
                betaValues[i - 1].assign(scalingMultiplier);
            }
            LogService.getGlobal().logError((System.currentTimeMillis() - t) + " time for backward-Scan");
            t = System.currentTimeMillis();
            double thisSeqLogli = 0;
            for (int i = 0; i < s.size(); i++) {
                computeMatrices(fSet, s, i, lambda, transMatrix, stateMatrix);
                if (i > 0) {
                    calculatingMatrixValues.assign(alphaValues);
                    ForwardBackward.Mult(transMatrix, calculatingMatrixValues, tempAlphaValues, 1, 0, true);
                    tempAlphaValues.assign(stateMatrix, m);
                } else {
                    tempAlphaValues.assign(stateMatrix);
                }
                if (grad != null) {
                    ArrayList<Feature> featuresForToken;
                    if (i == 0) featuresForToken = getFeatures(s.getExample(i), null, fSet, s); else featuresForToken = getFeatures(s.getExample(i), s.getExample(i - 1), fSet, s);
                    fSet.reset();
                    for (int c = 0; c < featuresForToken.size(); c++) {
                        Feature feature = featuresForToken.get(c);
                        int f = fSet.getIndex(feature);
                        String currentLabel = feature.getLabel();
                        String previousLabel = feature.getPrevLabel();
                        if (currentLabel != null) {
                            double val = 1.0;
                            if ((grad != null) && (s.getY()[i].equals(currentLabel)) && ((((previousLabel != null) && (i - 1 >= 0)) && (previousLabel.equals(s.getY()[i - 1]))) || previousLabel == null)) {
                                grad[f] += val;
                                thisSeqLogli += val * lambda[f];
                            }
                            if (previousLabel == null) {
                                expectationF[f] += tempAlphaValues.get(mapping.getLabelInt(currentLabel)) * val * betaValues[i].get(mapping.getLabelInt(currentLabel));
                            } else {
                                expectationF[f] += alphaValues.get(mapping.getLabelInt(previousLabel)) * stateMatrix.get(mapping.getLabelInt(currentLabel)) * transMatrix.get(mapping.getLabelInt(previousLabel), mapping.getLabelInt(currentLabel)) * val * betaValues[i].get(mapping.getLabelInt(currentLabel));
                            }
                        }
                    }
                }
                LogService.getGlobal().logError((System.currentTimeMillis() - t) + " time for forward-Scan");
                t = System.currentTimeMillis();
                alphaValues.assign(tempAlphaValues);
                scalingMultiplier.mult = 1.0 / scaling[i];
                alphaValues.assign(scalingMultiplier);
            }
            double Zx = alphaValues.zSum();
            thisSeqLogli -= Math.log(Zx);
            for (int i = 0; i < s.size(); i++) {
                thisSeqLogli -= Math.log(scaling[i]);
            }
            if (grad != null) {
                for (int f = 0; f < grad.length; f++) grad[f] -= expectationF[f] / Zx;
            }
            logli += thisSeqLogli;
        }
        return logli;
    }

    private double getLogLikelihoodNEW(double[] grad, FeatureSet fSet, int round) {
        double logli = 0.0;
        logli = addPrior(lambda, grad, logli);
        double[] expectationF = new double[fSet.size()];
        Label2IntegerMapping mapping = fSet.getMapping();
        int numY = fSet.getMapping().size();
        initMatrices(numY);
        fSet.reset();
        SequenceIterator sIter = fSet.getSIterator();
        int sCounter = 0;
        fIndex = 0;
        bIndex = 0;
        if (round == 0) {
            if (features2ProcessFor == null) features2ProcessFor = new ArrayList<Integer>();
        }
        if (round == 0) {
            if (features2ProcessBack == null) features2ProcessBack = new ArrayList<Integer>();
        }
        while (sIter.hasNext()) {
            LogService.getGlobal().logError("optimizing Sequence no. " + sCounter++);
            Sequence s = sIter.next();
            alphaValues.assign(1);
            for (int f = 0; f < lambda.length; f++) expectationF[f] = 0;
            if ((betaValues == null) || (betaValues.length < s.size())) {
                betaValues = new DenseDoubleMatrix1D[s.size()];
                for (int i = 0; i < betaValues.length; i++) betaValues[i] = new DenseDoubleMatrix1D(numY);
                scaling = new double[s.size()];
            }
            scaling[s.size() - 1] = numY;
            betaValues[s.size() - 1].assign(1.0 / scaling[s.size() - 1]);
            long t = System.currentTimeMillis();
            for (int i = s.size() - 1; i > 0; i--) {
                computeMatricesNEW(fSet, s, i, lambda, transMatrix, stateMatrix, features2ProcessBack, round, false);
                calculatingMatrixValues.assign(betaValues[i]);
                calculatingMatrixValues.assign(stateMatrix, m);
                ForwardBackward.Mult(transMatrix, calculatingMatrixValues, betaValues[i - 1], 1, 0, false);
                scaling[i - 1] = betaValues[i - 1].zSum();
                if ((scaling[i - 1] < 1) && (scaling[i - 1] > -1)) scaling[i - 1] = 1;
                scalingMultiplier.mult = 1.0 / scaling[i - 1];
                betaValues[i - 1].assign(scalingMultiplier);
            }
            LogService.getGlobal().logError((System.currentTimeMillis() - t) + " time for backward-Scan");
            t = System.currentTimeMillis();
            double thisSeqLogli = 0;
            for (int i = 0; i < s.size(); i++) {
                computeMatricesNEW(fSet, s, i, lambda, transMatrix, stateMatrix, features2ProcessFor, round, true);
                if (i > 0) {
                    calculatingMatrixValues.assign(alphaValues);
                    ForwardBackward.Mult(transMatrix, calculatingMatrixValues, tempAlphaValues, 1, 0, true);
                    tempAlphaValues.assign(stateMatrix, m);
                } else {
                    tempAlphaValues.assign(stateMatrix);
                }
                if (grad != null) {
                    ArrayList<Feature> featuresForToken;
                    if (i == 0) featuresForToken = getFeatures(s.getExample(i), null, fSet, s); else featuresForToken = getFeatures(s.getExample(i), s.getExample(i - 1), fSet, s);
                    fSet.reset();
                    for (int c = 0; c < featuresForToken.size(); c++) {
                        Feature feature = featuresForToken.get(c);
                        int f = fSet.getIndex(feature);
                        String currentLabel = feature.getLabel();
                        String previousLabel = feature.getPrevLabel();
                        if (currentLabel != null) {
                            double val = 1.0;
                            if ((grad != null) && (s.getY()[i].equals(currentLabel)) && ((((previousLabel != null) && (i - 1 >= 0)) && (previousLabel.equals(s.getY()[i - 1]))) || previousLabel == null)) {
                                grad[f] += val;
                                thisSeqLogli += val * lambda[f];
                            }
                            if (previousLabel == null) {
                                expectationF[f] += tempAlphaValues.get(mapping.getLabelInt(currentLabel)) * val * betaValues[i].get(mapping.getLabelInt(currentLabel));
                            } else {
                                expectationF[f] += alphaValues.get(mapping.getLabelInt(previousLabel)) * stateMatrix.get(mapping.getLabelInt(currentLabel)) * transMatrix.get(mapping.getLabelInt(previousLabel), mapping.getLabelInt(currentLabel)) * val * betaValues[i].get(mapping.getLabelInt(currentLabel));
                            }
                        }
                    }
                }
                LogService.getGlobal().logError((System.currentTimeMillis() - t) + " time for forward-Scan");
                t = System.currentTimeMillis();
                alphaValues.assign(tempAlphaValues);
                scalingMultiplier.mult = 1.0 / scaling[i];
                alphaValues.assign(scalingMultiplier);
            }
            double Zx = alphaValues.zSum();
            thisSeqLogli -= Math.log(Zx);
            for (int i = 0; i < s.size(); i++) {
                thisSeqLogli -= Math.log(scaling[i]);
            }
            if (grad != null) {
                for (int f = 0; f < grad.length; f++) grad[f] -= expectationF[f] / Zx;
            }
            logli += thisSeqLogli;
        }
        if (round == 0) {
            for (int i = 0; i < features2ProcessFor.size(); i++) {
                LogService.getGlobal().logError("for " + i + ".)" + features2ProcessFor.get(i).toString());
            }
            for (int i = 0; i < features2ProcessBack.size(); i++) {
                LogService.getGlobal().logError("back " + i + ".)" + features2ProcessBack.get(i).toString());
            }
        }
        return logli;
    }

    protected double norm(double ar[]) {
        double v = 0;
        for (int f = 0; f < ar.length; f++) v += ar[f] * ar[f];
        return Math.sqrt(v);
    }

    public boolean computeMatricesNEW(FeatureSet featureGen, Sequence s, int i, double lambda[], DoubleMatrix2D transitions, DoubleMatrix1D states, ArrayList<Integer> features2Process, int round, boolean front) {
        boolean mSet = false;
        transitions.assign(0);
        states.assign(0);
        featureGen.reset();
        ArrayList<Feature> l;
        if (i > 0) l = getFeaturesNEW(s.getExample(i), s.getExample(i - 1), featureGen, s, features2Process, round, front); else l = getFeaturesNEW(s.getExample(i), null, featureGen, s, features2Process, round, front);
        featureGen.reset();
        Label2IntegerMapping mapping = featureGen.getMapping();
        for (int a = 0; a < l.size(); a++) {
            Feature feature = l.get(a);
            int f = featureGen.getIndex(feature);
            String yp = feature.getLabel();
            String yprev = feature.getPrevLabel();
            if (yp != null) {
                double val = 1.0;
                if (yprev == null) {
                    try {
                        double oldVal = states.get(mapping.getLabelInt(yp));
                        states.set(mapping.getLabelInt(yp), oldVal + lambda[f] * val);
                    } catch (Exception e) {
                        System.out.println("exp2: " + yp);
                        System.out.println(mapping.toString());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (transitions != null) {
                            double oldVal = transitions.get(mapping.getLabelInt(yprev), mapping.getLabelInt(yp));
                            transitions.set(mapping.getLabelInt(yprev), mapping.getLabelInt(yp), oldVal + lambda[f] * val);
                            mSet = true;
                        }
                    } catch (Exception e) {
                        System.out.println("exp2: " + yp + " " + yprev);
                        System.out.println(mapping.toString());
                        e.printStackTrace();
                    }
                }
            }
        }
        for (int r = states.size() - 1; r >= 0; r--) {
            states.setQuick(r, Math.exp(states.getQuick(r)));
            if (transitions != null) for (int c = transitions.columns() - 1; c >= 0; c--) {
                transitions.setQuick(r, c, Math.exp(transitions.getQuick(r, c)));
            }
        }
        return mSet;
    }

    public boolean computeMatrices(FeatureSet featureGen, Sequence s, int i, double lambda[], DoubleMatrix2D transitions, DoubleMatrix1D states) {
        boolean mSet = false;
        transitions.assign(0);
        states.assign(0);
        featureGen.reset();
        ArrayList<Feature> l;
        if (i > 0) l = getFeatures(s.getExample(i), s.getExample(i - 1), featureGen, s); else l = getFeatures(s.getExample(i), null, featureGen, s);
        featureGen.reset();
        Label2IntegerMapping mapping = featureGen.getMapping();
        for (int a = 0; a < l.size(); a++) {
            Feature feature = l.get(a);
            int f = featureGen.getIndex(feature);
            String yp = feature.getLabel();
            String yprev = feature.getPrevLabel();
            if (yp != null) {
                double val = 1.0;
                if (yprev == null) {
                    try {
                        double oldVal = states.get(mapping.getLabelInt(yp));
                        states.set(mapping.getLabelInt(yp), oldVal + lambda[f] * val);
                    } catch (Exception e) {
                        System.out.println("exp2: " + yp);
                        System.out.println(mapping.toString());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (transitions != null) {
                            double oldVal = transitions.get(mapping.getLabelInt(yprev), mapping.getLabelInt(yp));
                            transitions.set(mapping.getLabelInt(yprev), mapping.getLabelInt(yp), oldVal + lambda[f] * val);
                            mSet = true;
                        }
                    } catch (Exception e) {
                        System.out.println("exp2: " + yp + " " + yprev);
                        System.out.println(mapping.toString());
                        e.printStackTrace();
                    }
                }
            }
        }
        for (int r = states.size() - 1; r >= 0; r--) {
            states.setQuick(r, Math.exp(states.getQuick(r)));
            if (transitions != null) for (int c = transitions.columns() - 1; c >= 0; c--) {
                transitions.setQuick(r, c, Math.exp(transitions.getQuick(r, c)));
            }
        }
        return mSet;
    }

    private ArrayList<Feature> getFeaturesNEW(Example ex, Example prevEx, FeatureSet fSet, Sequence s, ArrayList<Integer> features2Process, int round, boolean front) {
        ArrayList<Feature> result = new ArrayList<Feature>();
        String label = null;
        try {
            if (ex != null) {
                label = ex.getValueAsString(fSet.getExampleSet().getAttributes().getLabel());
                label = fSet.eraseIOB(label);
            }
        } catch (Exception e) {
        }
        if (attributes == null) {
            attributes = new ArrayList<Attribute>(fSet.getAttributes());
        }
        for (int i = 0; i < attributes.size(); i++) {
            ArrayList<Feature> list = null;
            FeatureStub fs = FeatureStub.createFeatureStub(attributes.get(i), ex);
            if (round == 0) {
                list = fSet.getFeatures(attributes.get(i), ex);
            } else {
                boolean otherStub = false;
                while (!otherStub) {
                    Feature f;
                    if (front) {
                        if (features2Process.size() > fIndex) {
                            f = fSet.getFeature(features2Process.get(fIndex++));
                        } else f = null;
                    } else {
                        if (features2Process.size() > bIndex) {
                            f = fSet.getFeature(features2Process.get(bIndex++));
                        } else f = null;
                    }
                    if (f == null || !f.getStub().equals(fs)) {
                        otherStub = true;
                        if (front) fIndex--; else bIndex--;
                    } else {
                        if (list == null) {
                            list = new ArrayList<Feature>();
                        }
                        list.add(f);
                    }
                }
            }
            if (list != null) {
                for (int b = 0; b < list.size(); b++) {
                    Feature f = list.get(b);
                    if (!result.contains(f)) {
                        result.add(f);
                    }
                }
            }
        }
        if (round == 0) {
            for (int i = 0; i < result.size(); i++) {
                features2Process.add(fSet.getIndex(result.get(i)));
                if (front) System.out.println("FRONT IN LIST: " + features2Process.size() + " " + result.get(i).toString()); else System.out.println("BACK IN LIST: " + features2Process.size() + " " + result.get(i).toString());
            }
        }
        return result;
    }

    private ArrayList<Feature> getFeatures(Example ex, Example prevEx, FeatureSet fSet, Sequence s) {
        ArrayList<Feature> result = new ArrayList<Feature>();
        String label = null;
        try {
            if (ex != null) {
                label = ex.getValueAsString(fSet.getExampleSet().getAttributes().getLabel());
                label = fSet.eraseIOB(label);
            }
        } catch (Exception e) {
        }
        long t = 0;
        if (attributes == null) {
            attributes = new ArrayList<Attribute>(fSet.getAttributes());
        }
        for (int i = 0; i < attributes.size(); i++) {
            ArrayList<Feature> list = null;
            long t2 = System.currentTimeMillis();
            list = fSet.getFeatures(attributes.get(i), ex);
            t += System.currentTimeMillis() - t2;
            if (list != null) {
                for (int b = 0; b < list.size(); b++) {
                    Feature f = list.get(b);
                    if (!result.contains(f)) {
                        result.add(f);
                    }
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    public static class MultSingle implements DoubleFunction {

        public double mult = 1.0;

        @Override
        public double apply(double multWith) {
            return multWith * mult;
        }
    }

    ;
}
