package shellkk.qiq.jdm.algorithm.svm;

import java.util.HashMap;
import java.util.Map;
import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.VerificationReport;
import javax.datamining.algorithm.svm.KernelFunction;
import javax.datamining.algorithm.svm.regression.SVMRegressionSettings;
import shellkk.qiq.jdm.base.AlgorithmSettingsImpl;

public class SVMRegressionSettingsImpl extends AlgorithmSettingsImpl implements SVMRegressionSettings {

    protected String kernelName;

    protected Map<String, String> kernelProperties = new HashMap();

    protected double eps = 1.0E-2;

    protected double CStrategy = 1.0E+10;

    protected double complexityFactor;

    protected double tolerance = 1.0E-3;

    protected double epsilon = 1.0E-1;

    protected int kernelCacheSize;

    protected String kernelFunctionName;

    protected int polynomialDegree = 3;

    protected double standardDeviation = 1;

    protected String epsilonAttribute;

    protected double gapEps = 1.0E-3;

    public SVMRegressionSettingsImpl() {
        kernelFunctionName = KernelFunction.systemDetermined.name();
    }

    @Override
    protected SVMRegressionSettingsImpl create() {
        return new SVMRegressionSettingsImpl();
    }

    public SVMRegressionSettingsImpl getCopy() {
        SVMRegressionSettingsImpl copy = (SVMRegressionSettingsImpl) super.getCopy();
        copy.setEps(eps);
        copy.setCStrategy(CStrategy);
        copy.setComplexityFactor(complexityFactor);
        copy.setTolerance(tolerance);
        copy.setEpsilon(epsilon);
        copy.setKernelCacheSize(kernelCacheSize);
        copy.setKernelFunctionName(kernelFunctionName);
        copy.setPolynomialDegree(polynomialDegree);
        copy.setStandardDeviation(standardDeviation);
        copy.setEpsilonAttribute(epsilonAttribute);
        copy.setGapEps(gapEps);
        copy.setKernelName(kernelName);
        copy.getKernelProperties().putAll(kernelProperties);
        return copy;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public double getCStrategy() {
        return CStrategy;
    }

    public void setCStrategy(double strategy) {
        CStrategy = strategy;
    }

    public double getComplexityFactor() {
        return complexityFactor;
    }

    public void setComplexityFactor(double complexityFactor) {
        this.complexityFactor = complexityFactor;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public int getKernelCacheSize() {
        return kernelCacheSize;
    }

    public void setKernelCacheSize(int kernelCacheSize) {
        this.kernelCacheSize = kernelCacheSize;
    }

    public String getKernelFunctionName() {
        return kernelFunctionName;
    }

    public void setKernelFunctionName(String kernelFunctionName) {
        this.kernelFunctionName = kernelFunctionName;
    }

    public int getPolynomialDegree() {
        return polynomialDegree;
    }

    public void setPolynomialDegree(int polynomialDegree) {
        this.polynomialDegree = polynomialDegree;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public String getEpsilonAttribute() {
        return epsilonAttribute;
    }

    public void setEpsilonAttribute(String epsilonAttribute) {
        this.epsilonAttribute = epsilonAttribute;
    }

    public double getGapEps() {
        return gapEps;
    }

    public void setGapEps(double gapEps) {
        this.gapEps = gapEps;
    }

    public KernelFunction getKernelFunction() {
        try {
            return KernelFunction.valueOf(kernelFunctionName);
        } catch (JDMException e) {
            throw new RuntimeException(e);
        }
    }

    public void setKernelFunction(KernelFunction kernelFunction) {
        kernelFunctionName = kernelFunction.name();
    }

    public String getKernelName() {
        return kernelName;
    }

    public void setKernelName(String kernelName) {
        this.kernelName = kernelName;
    }

    public Map<String, String> getKernelProperties() {
        return kernelProperties;
    }

    public void setKernelProperties(Map<String, String> kernelProperties) {
        this.kernelProperties = kernelProperties;
    }

    public MiningAlgorithm getMiningAlgorithm() {
        return MiningAlgorithm.svmRegression;
    }

    public VerificationReport verify() {
        return null;
    }
}
