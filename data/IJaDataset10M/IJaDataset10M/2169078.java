package shellkk.qiq.jdm.supervised.classification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.datamining.JDMException;
import javax.datamining.data.CategorySet;
import javax.datamining.supervised.classification.ConfusionMatrix;
import shellkk.qiq.jdm.JDMExceptionUtil;
import shellkk.qiq.jdm.data.CategorySetImpl;

public class ConfusionMatrixImpl implements ConfusionMatrix {

    protected Long id;

    protected Integer hversion;

    protected double accuracy;

    protected double error;

    protected CategorySetImpl targetCategorySet;

    protected List<ConfusionMatrixElement> elements = new ArrayList();

    public ConfusionMatrixImpl getCopy() {
        ConfusionMatrixImpl copy = new ConfusionMatrixImpl();
        copy.setAccuracy(accuracy);
        copy.setError(error);
        copy.setTargetCategorySet(targetCategorySet.getCopy());
        for (ConfusionMatrixElement ele : elements) {
            copy.getElements().add(ele.getCopy());
        }
        return copy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHversion() {
        return hversion;
    }

    public void setHversion(Integer hversion) {
        this.hversion = hversion;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public CategorySetImpl getTargetCategorySet() {
        return targetCategorySet;
    }

    public void setTargetCategorySet(CategorySetImpl targetCategorySet) {
        this.targetCategorySet = targetCategorySet;
    }

    public List<ConfusionMatrixElement> getElements() {
        return elements;
    }

    public void setElements(List<ConfusionMatrixElement> elements) {
        this.elements = elements;
    }

    protected boolean equalAsString(Object a, Object b) {
        String stra = a == null ? null : a.toString();
        String strb = b == null ? null : b.toString();
        return stra == null ? strb == null : stra.equals(strb);
    }

    protected ConfusionMatrixElement getElement(Object actualTarget, Object predictedTarget) {
        for (ConfusionMatrixElement ele : elements) {
            if (equalAsString(actualTarget, ele.getActualCategory()) && equalAsString(predictedTarget, ele.getPredictedCategory())) {
                return ele;
            }
        }
        return null;
    }

    public long getNumberOfPredictions(Object actualCategoryValue, Object predictedCategoryValue) throws JDMException {
        ConfusionMatrixElement ele = getElement(actualCategoryValue, predictedCategoryValue);
        if (ele == null) {
            JDMExceptionUtil.throwException(JDMException.JDM_GENERIC_ERROR, "unknown actualCategory[" + actualCategoryValue + "] predictedCategory[" + predictedCategoryValue + "]");
            return 0;
        } else {
            return ele.getNumberOfPredictions();
        }
    }

    public Collection getCategories() {
        return targetCategorySet.getCategories();
    }

    public CategorySet getCategorySet() {
        return targetCategorySet;
    }

    public Double getValue(Object rowCategoryValue, Object columnCategoryValue) throws JDMException {
        return Double.valueOf((double) getNumberOfPredictions(rowCategoryValue, columnCategoryValue));
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("accuracy=" + accuracy + "\n");
        buf.append("error=" + error + "\n");
        for (ConfusionMatrixElement ele : elements) {
            buf.append("actual=" + ele.getActualCategory() + " predict=" + ele.getPredictedCategory() + " count=" + ele.getNumberOfPredictions() + "\n");
        }
        return buf.toString();
    }
}
