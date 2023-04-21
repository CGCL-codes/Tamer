package org.dllearner.algorithms.refexamples;

import java.util.Set;
import java.util.TreeSet;
import org.dllearner.core.owl.Description;
import org.dllearner.core.owl.Individual;
import org.dllearner.utilities.ConceptComparator;

/**
 * 
 * Represents a node in the search tree. A node consists of
 * the following parts:
 * 
 * ... (see paper) ... 
 * 
 * @author Jens Lehmann
 *
 */
public class ExampleBasedNode {

    private Set<Individual> coveredPositives;

    private Set<Individual> coveredNegatives;

    public enum QualityEvaluationMethod {

        TOP, REASONER, TOO_WEAK_LIST, OVERLY_GENERAL_LIST
    }

    ;

    private QualityEvaluationMethod qualityEvaluationMethod = QualityEvaluationMethod.TOP;

    private Description concept;

    private int horizontalExpansion;

    private int coveredNegativeExamples;

    private boolean isTooWeak;

    private boolean isQualityEvaluated;

    private boolean isRedundant;

    private static ConceptComparator conceptComparator = new ConceptComparator();

    private static NodeComparatorStable nodeComparator = new NodeComparatorStable();

    private ExampleBasedNode parent = null;

    private Set<ExampleBasedNode> children = new TreeSet<ExampleBasedNode>(nodeComparator);

    private Set<Description> childConcepts = new TreeSet<Description>(conceptComparator);

    public ExampleBasedNode(Description concept) {
        this.concept = concept;
        horizontalExpansion = 0;
        isQualityEvaluated = false;
    }

    public void setCoveredNegativeExamples(int coveredNegativeExamples) {
        if (isQualityEvaluated) throw new RuntimeException("Cannot set quality of a node more than once.");
        this.coveredNegativeExamples = coveredNegativeExamples;
        isQualityEvaluated = true;
    }

    public void setHorizontalExpansion(int horizontalExpansion) {
        this.horizontalExpansion = horizontalExpansion;
    }

    public void setRedundant(boolean isRedundant) {
        this.isRedundant = isRedundant;
    }

    public void setTooWeak(boolean isTooWeak) {
        if (isQualityEvaluated) throw new RuntimeException("Cannot set quality of a node more than once.");
        this.isTooWeak = isTooWeak;
        isQualityEvaluated = true;
    }

    public boolean addChild(ExampleBasedNode child) {
        child.parent = this;
        childConcepts.add(child.concept);
        return children.add(child);
    }

    public Description getConcept() {
        return concept;
    }

    public int getCoveredNegativeExamples() {
        return coveredNegativeExamples;
    }

    public int getHorizontalExpansion() {
        return horizontalExpansion;
    }

    public boolean isQualityEvaluated() {
        return isQualityEvaluated;
    }

    public boolean isRedundant() {
        return isRedundant;
    }

    public boolean isTooWeak() {
        return isTooWeak;
    }

    @Override
    public String toString() {
        String ret = concept.toString() + " [q:";
        if (isTooWeak) ret += "tw"; else ret += coveredNegativeExamples;
        ret += ", he:" + horizontalExpansion + ", children:" + children.size() + "]";
        return ret;
    }

    public String getRefinementChainString() {
        if (parent != null) {
            String ret = parent.getRefinementChainString();
            ret += " => " + concept.toString();
            return ret;
        } else {
            return concept.toString();
        }
    }

    public String getTreeString() {
        return getTreeString(0).toString();
    }

    private StringBuilder getTreeString(int depth) {
        StringBuilder treeString = new StringBuilder();
        for (int i = 0; i < depth - 1; i++) treeString.append("  ");
        if (depth != 0) treeString.append("|--> ");
        treeString.append(getShortDescription() + "\n");
        for (ExampleBasedNode child : children) {
            treeString.append(child.getTreeString(depth + 1));
        }
        return treeString;
    }

    private String getShortDescription() {
        String ret = concept.toString() + " [q:";
        if (isTooWeak) ret += "tw"; else ret += coveredNegativeExamples;
        ret += " (" + qualityEvaluationMethod + "), he:" + horizontalExpansion + "]";
        return ret;
    }

    public Set<ExampleBasedNode> getChildren() {
        return children;
    }

    public Set<Description> getChildConcepts() {
        return childConcepts;
    }

    public QualityEvaluationMethod getQualityEvaluationMethod() {
        return qualityEvaluationMethod;
    }

    public void setQualityEvaluationMethod(QualityEvaluationMethod qualityEvaluationMethod) {
        this.qualityEvaluationMethod = qualityEvaluationMethod;
    }

    public Set<Individual> getCoveredPositives() {
        return coveredPositives;
    }

    public void setCoveredPositives(Set<Individual> coveredPositives) {
        this.coveredPositives = coveredPositives;
    }

    public Set<Individual> getCoveredNegatives() {
        return coveredNegatives;
    }

    public void setCoveredNegatives(Set<Individual> coveredNegatives) {
        this.coveredNegatives = coveredNegatives;
    }
}
