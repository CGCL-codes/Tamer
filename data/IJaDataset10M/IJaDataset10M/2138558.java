package shellkk.qiq.jdm.supervised.classification;

import javax.datamining.MiningFunction;
import javax.datamining.supervised.classification.ClassificationModel;
import shellkk.qiq.jdm.base.ModelImpl;
import shellkk.qiq.jdm.data.CategorySetImpl;

public class ClassificationModelImpl extends ModelImpl implements ClassificationModel {

    protected double classificationError;

    protected CategorySetImpl targetCategorySet;

    protected boolean useCostMatrix;

    protected String targetAttributeName;

    @Override
    protected ClassificationModelImpl create() {
        return new ClassificationModelImpl();
    }

    public ClassificationModelImpl getCopy() {
        ClassificationModelImpl copy = (ClassificationModelImpl) super.getCopy();
        copy.setClassificationError(classificationError);
        copy.setTargetCategorySet(targetCategorySet == null ? null : targetCategorySet.getCopy());
        copy.setUseCostMatrix(useCostMatrix);
        copy.setTargetAttributeName(targetAttributeName);
        return copy;
    }

    public double getClassificationError() {
        return classificationError;
    }

    public void setClassificationError(double classificationError) {
        this.classificationError = classificationError;
    }

    public boolean isUseCostMatrix() {
        return useCostMatrix;
    }

    public void setUseCostMatrix(boolean useCostMatrix) {
        this.useCostMatrix = useCostMatrix;
    }

    public String getTargetAttributeName() {
        return targetAttributeName;
    }

    public void setTargetAttributeName(String targetAttributeName) {
        this.targetAttributeName = targetAttributeName;
    }

    public CategorySetImpl getTargetCategorySet() {
        return targetCategorySet;
    }

    public void setTargetCategorySet(CategorySetImpl targetCategorySet) {
        this.targetCategorySet = targetCategorySet;
    }

    public boolean wasCostMatrixUsed() {
        return isUseCostMatrix();
    }

    public MiningFunction getMiningFunction() {
        return MiningFunction.classification;
    }
}
