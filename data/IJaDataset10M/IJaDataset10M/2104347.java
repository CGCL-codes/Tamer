package shellkk.qiq.jdm.modeldetail.svm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import javax.datamining.modeldetail.svm.SVMRegressionModelDetail;
import shellkk.qiq.jdm.JDMExceptionUtil;
import shellkk.qiq.jdm.base.BuildSettingsImpl;
import shellkk.qiq.jdm.base.ModelImpl;
import shellkk.qiq.jdm.common.FormerMetaProperty;
import shellkk.qiq.jdm.common.Vector;
import shellkk.qiq.jdm.common.kernel.KernelFormer;
import shellkk.qiq.jdm.common.kernel.KernelFormerFactor;
import shellkk.qiq.jdm.common.kernel.LinearKernel;
import shellkk.qiq.jdm.data.LogicalDataRecord;
import shellkk.qiq.jdm.supervised.regression.RegressionApplyResult;
import shellkk.qiq.jdm.supervised.regression.RegressionModelDetail;
import shellkk.qiq.jdm.supervised.regression.RegressionModelImpl;
import shellkk.qiq.math.kernel.Kernel;
import shellkk.qiq.math.matrix.Statistics;
import shellkk.qiq.math.ml.svm.CrossKernel;

public class SVMRegressionModelDetailImpl extends KernelFormer implements RegressionModelDetail, SVMRegressionModelDetail {

    protected double epsilon;

    protected String epsilonAttribute;

    protected double CStrategy;

    protected int kernelCacheSize;

    protected double tolerance;

    protected double eps;

    protected double gapEps;

    protected int numberOfSupportVectors;

    protected int numberOfBoundedVectors;

    protected int numberOfUnboundedVectors;

    protected double supportSquareRange;

    protected ModelImpl model;

    protected Kernel kernel;

    @Override
    public SVMRegressionElement createElement() {
        return new SVMRegressionElement();
    }

    @Override
    public SVMRegressionModelDetailImpl create() {
        return new SVMRegressionModelDetailImpl();
    }

    public SVMRegressionModelDetailImpl getCopy() {
        SVMRegressionModelDetailImpl copy = (SVMRegressionModelDetailImpl) super.getCopy();
        copy.setEpsilon(epsilon);
        copy.setEpsilonAttribute(epsilonAttribute);
        copy.setCStrategy(CStrategy);
        copy.setKernelCacheSize(kernelCacheSize);
        copy.setTolerance(tolerance);
        copy.setEps(eps);
        copy.setGapEps(gapEps);
        copy.setNumberOfBoundedVectors(numberOfBoundedVectors);
        copy.setNumberOfSupportVectors(numberOfSupportVectors);
        copy.setNumberOfUnboundedVectors(numberOfUnboundedVectors);
        copy.setSupportSquareRange(supportSquareRange);
        copy.setKernel(kernel);
        return copy;
    }

    public List<String> getInputAttributeNames() {
        List<String> attrNames = new ArrayList();
        for (FormerMetaProperty meta : categoryMetaProps) {
            attrNames.add(meta.getAttributeName());
        }
        for (FormerMetaProperty meta : numberMetaProps) {
            attrNames.add(meta.getAttributeName());
        }
        for (FormerMetaProperty meta : textMetaProps) {
            attrNames.add(meta.getAttributeName());
        }
        for (FormerMetaProperty meta : otherMetaProps) {
            attrNames.add(meta.getAttributeName());
        }
        return attrNames;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public String getEpsilonAttribute() {
        return epsilonAttribute;
    }

    public void setEpsilonAttribute(String epsilonAttribute) {
        this.epsilonAttribute = epsilonAttribute;
    }

    public double getCStrategy() {
        return CStrategy;
    }

    public void setCStrategy(double strategy) {
        CStrategy = strategy;
    }

    public int getKernelCacheSize() {
        return kernelCacheSize;
    }

    public void setKernelCacheSize(int kernelCacheSize) {
        this.kernelCacheSize = kernelCacheSize;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public double getGapEps() {
        return gapEps;
    }

    public void setGapEps(double gapEps) {
        this.gapEps = gapEps;
    }

    public int getNumberOfSupportVectors() {
        return numberOfSupportVectors;
    }

    public void setNumberOfSupportVectors(int numberOfSupportVectors) {
        this.numberOfSupportVectors = numberOfSupportVectors;
    }

    public int getNumberOfBoundedVectors() {
        return numberOfBoundedVectors;
    }

    public void setNumberOfBoundedVectors(int numberOfBoundedVectors) {
        this.numberOfBoundedVectors = numberOfBoundedVectors;
    }

    public int getNumberOfUnboundedVectors() {
        return numberOfUnboundedVectors;
    }

    public void setNumberOfUnboundedVectors(int numberOfUnboundedVectors) {
        this.numberOfUnboundedVectors = numberOfUnboundedVectors;
    }

    public double getSupportSquareRange() {
        return supportSquareRange;
    }

    public void setSupportSquareRange(double supportSquareRange) {
        this.supportSquareRange = supportSquareRange;
    }

    public ModelImpl getModel() {
        return model;
    }

    public void setModel(ModelImpl model) {
        this.model = model;
    }

    public Kernel getKernel() {
        return kernel;
    }

    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    public double getBias() throws JDMException {
        return mbias;
    }

    public double getCoefficient(String numericalAttrName) throws JDMException {
        JDMExceptionUtil.throwException(JDMException.JDMR_UNSUPPORTED_FEATURE);
        return 0;
    }

    public double getCoefficient(String categoricalAttrName, Object categoryValue) throws JDMException {
        JDMExceptionUtil.throwException(JDMException.JDMR_UNSUPPORTED_FEATURE);
        return 0;
    }

    public Map getCoefficients(String attrName) throws JDMException {
        JDMExceptionUtil.throwException(JDMException.JDMR_UNSUPPORTED_FEATURE);
        return null;
    }

    public boolean isLinearSVMModel() {
        return kernel instanceof LinearKernel;
    }

    public RegressionApplyResult getRegressionApplyResult(LogicalDataRecord logicRecord, double confidenceLevel) {
        double predict = getPredictedValue(logicRecord);
        RegressionApplyResult result = new RegressionApplyResult();
        result.setPredictedValue(predict);
        if (confidenceLevel > 0 && confidenceLevel <= 100) {
            double var = ((RegressionModelImpl) model).getSquaredError();
            double err = Math.sqrt(var) * Statistics.normalInverse(0.5 + 0.005 * confidenceLevel);
            result.setPredictedLowerBound(predict - err);
            result.setPredictedUpperBound(predict + err);
        }
        return result;
    }

    public double getPredictedValue(LogicalDataRecord logicRecord) {
        boolean cross = kernel instanceof CrossKernel;
        Vector x = toVector(logicRecord);
        double product = 0;
        for (int i = 0; i < factors.size(); i++) {
            KernelFormerFactor factor = factors.get(i);
            Vector xi = vectors[i];
            double alpha = factor.getCoefficient();
            if (cross) {
                product += alpha * ((CrossKernel) kernel).crossProduct(xi, x);
            } else {
                product += alpha * kernel.product(xi, x);
            }
        }
        return product + mbias;
    }

    public BuildSettingsImpl getEffectiveBuildSettings(BuildSettingsImpl buildSettings) {
        return buildSettings;
    }

    public MiningAlgorithm getMiningAlgorithm() {
        return MiningAlgorithm.svmRegression;
    }
}
