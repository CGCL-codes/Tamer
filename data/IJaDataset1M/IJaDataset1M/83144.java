package shellkk.qiq.jdm.engine.algorithm.naivebayes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.datamining.MiningAlgorithm;
import javax.datamining.supervised.classification.ClassificationApplyCapability;
import javax.datamining.supervised.classification.ClassificationApplyContent;
import shellkk.qiq.jdm.engine.classification.IClassificationApplyObject;

public class NaiveBayesApply implements IClassificationApplyObject {

    protected List<ClassificationApplyCapability> capabilities = new ArrayList();

    protected List<ClassificationApplyContent> contents = new ArrayList();

    public NaiveBayesApply() {
        capabilities.add(ClassificationApplyCapability.allPredictions);
        capabilities.add(ClassificationApplyCapability.bottomSequentialRanks);
        capabilities.add(ClassificationApplyCapability.costMatrix);
        capabilities.add(ClassificationApplyCapability.individualCategories);
        capabilities.add(ClassificationApplyCapability.topPrediction);
        capabilities.add(ClassificationApplyCapability.topSequentialRanks);
        contents.add(ClassificationApplyContent.cost);
        contents.add(ClassificationApplyContent.predictedCategory);
        contents.add(ClassificationApplyContent.probability);
    }

    public MiningAlgorithm getMiningAlgorithm() {
        return MiningAlgorithm.naiveBayes;
    }

    public Collection<ClassificationApplyCapability> getSupportApplyCapabilities() {
        ArrayList<ClassificationApplyCapability> all = new ArrayList();
        all.addAll(capabilities);
        return all;
    }

    public Collection<ClassificationApplyContent> getSupportApplyContents() {
        ArrayList<ClassificationApplyContent> all = new ArrayList();
        all.addAll(contents);
        return all;
    }
}
