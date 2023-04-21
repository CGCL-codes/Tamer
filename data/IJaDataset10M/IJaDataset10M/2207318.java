package shellkk.qiq.jdm.modeldetail.tree;

import java.util.ArrayList;
import java.util.List;
import javax.datamining.JDMException;
import javax.datamining.modeldetail.tree.PredictionType;
import javax.datamining.rule.Predicate;
import javax.datamining.rule.Rule;
import javax.datamining.statistics.AttributeStatisticsSet;
import shellkk.qiq.jdm.rule.BooleanPredicateImpl;
import shellkk.qiq.jdm.rule.RuleFactory;
import shellkk.qiq.jdm.rule.RuleImpl;
import shellkk.qiq.jdm.rule.SimplePredicateImpl;

public class C45ClassificationNode extends DecisionTreeNode {

    protected List<ClassificationNodeItem> targetItems = new ArrayList();

    protected String topCategory;

    @Override
    protected C45ClassificationNode create() {
        return new C45ClassificationNode();
    }

    public C45ClassificationNode getCopy() {
        C45ClassificationNode copy = (C45ClassificationNode) super.getCopy();
        copy.setTopCategory(topCategory);
        for (ClassificationNodeItem item : targetItems) {
            copy.targetItems.add(item.getCopy());
        }
        return copy;
    }

    public List<ClassificationNodeItem> getTargetItems() {
        return targetItems;
    }

    public void setTargetItems(List<ClassificationNodeItem> targetItems) {
        this.targetItems = targetItems;
    }

    public String getTopCategory() {
        return topCategory;
    }

    public void setTopCategory(String topCategory) {
        this.topCategory = topCategory;
    }

    public AttributeStatisticsSet getNodeStatistics() throws JDMException {
        return null;
    }

    public Object getPrediction() {
        return topCategory;
    }

    public PredictionType getPredictionType() {
        return PredictionType.category;
    }

    public Rule getRule() throws JDMException {
        Predicate antecedent = getPredicate();
        if (antecedent == null) {
            antecedent = BooleanPredicateImpl.TRUE;
        }
        SimplePredicateImpl consequent = SimplePredicateImpl.createEqual(targetAttrName, topCategory);
        RuleImpl rule = RuleFactory.getInstance().createRule(antecedent, consequent);
        rule.setAbsoluteSupport(getCaseCount());
        rule.setConfidence(getTargetWeight(topCategory) / weight);
        rule.setRuleIdentifier(getIdentifier());
        rule.setSupport(support);
        return rule;
    }

    protected boolean equalAsString(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        } else if (a != null && b != null) {
            return a.toString().equals(b.toString());
        } else {
            return false;
        }
    }

    public long getTargetCount(Object target) throws JDMException {
        for (ClassificationNodeItem item : targetItems) {
            if (equalAsString(target, item.getCategory())) {
                return Math.round(item.getWeight());
            }
        }
        return 0;
    }

    public double getTargetWeight(Object target) throws JDMException {
        for (ClassificationNodeItem item : targetItems) {
            if (equalAsString(target, item.getCategory())) {
                return item.getWeight();
            }
        }
        return 0;
    }

    public double getTargetProbability(Object target) throws JDMException {
        for (ClassificationNodeItem item : targetItems) {
            if (equalAsString(target, item.getCategory())) {
                return item.getProbability();
            }
        }
        return 0;
    }

    public long[] getTargetCounts() {
        long[] all = new long[targetItems.size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = Math.round(targetItems.get(i).getWeight());
        }
        return all;
    }

    public double[] getTargetWeights() {
        double[] all = new double[targetItems.size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = targetItems.get(i).getWeight();
        }
        return all;
    }

    public double[] getTargetProbabilities() {
        double[] all = new double[targetItems.size()];
        for (int i = 0; i < all.length; i++) {
            all[i] = targetItems.get(i).getProbability();
        }
        return all;
    }
}
