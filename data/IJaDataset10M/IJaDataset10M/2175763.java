package shellkk.qiq.jdm.supervised;

import javax.datamining.MiningFunction;
import javax.datamining.MiningTask;
import javax.datamining.supervised.TestMetricsTask;
import shellkk.qiq.jdm.base.TaskImpl;

public abstract class TestMetricsTaskImpl extends TaskImpl implements TestMetricsTask {

    protected String actualTargetAttrName;

    protected String applyOutputDataName;

    protected String predictedTargetAttrName;

    protected String predictionRankingAttrName;

    protected String testMetricsDescription;

    protected String testMetricsName;

    public TestMetricsTaskImpl getCopy() {
        TestMetricsTaskImpl copy = (TestMetricsTaskImpl) super.getCopy();
        copy.setActualTargetAttrName(actualTargetAttrName);
        copy.setApplyOutputDataName(applyOutputDataName);
        copy.setPredictedTargetAttrName(predictedTargetAttrName);
        copy.setPredictionRankingAttrName(predictionRankingAttrName);
        copy.setTestMetricsDescription(testMetricsDescription);
        copy.setTestMetricsName(testMetricsName);
        return copy;
    }

    public String getActualTargetAttrName() {
        return actualTargetAttrName;
    }

    public void setActualTargetAttrName(String actualTargetAttrName) {
        this.actualTargetAttrName = actualTargetAttrName;
    }

    public String getApplyOutputDataName() {
        return applyOutputDataName;
    }

    public void setApplyOutputDataName(String applyOutputDataName) {
        this.applyOutputDataName = applyOutputDataName;
    }

    public String getPredictedTargetAttrName() {
        return predictedTargetAttrName;
    }

    public void setPredictedTargetAttrName(String predictedTargetAttrName) {
        this.predictedTargetAttrName = predictedTargetAttrName;
    }

    public String getPredictionRankingAttrName() {
        return predictionRankingAttrName;
    }

    public void setPredictionRankingAttrName(String predictionRankingAttrName) {
        this.predictionRankingAttrName = predictionRankingAttrName;
    }

    public String getTestMetricsDescription() {
        return testMetricsDescription;
    }

    public void setTestMetricsDescription(String testMetricsDescription) {
        this.testMetricsDescription = testMetricsDescription;
    }

    public String getTestMetricsName() {
        return testMetricsName;
    }

    public void setTestMetricsName(String testMetricsName) {
        this.testMetricsName = testMetricsName;
    }

    @Override
    public MiningTask getMiningTask() {
        return MiningTask.testTask;
    }

    public abstract MiningFunction getMiningFunction();
}
