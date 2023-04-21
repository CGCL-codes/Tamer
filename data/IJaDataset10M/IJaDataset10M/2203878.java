package org.vikamine.kernel.subgroup.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.vikamine.kernel.data.Attribute;
import org.vikamine.kernel.data.DataRecord;
import org.vikamine.kernel.data.DataRecordIteration;
import org.vikamine.kernel.data.FilteringDataRecordIterator;
import org.vikamine.kernel.data.IncludingDataRecordFilter;
import org.vikamine.kernel.data.Value;
import org.vikamine.kernel.subgroup.KBestSGSet;
import org.vikamine.kernel.subgroup.Options;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.SGDescription;
import org.vikamine.kernel.subgroup.SGSet;
import org.vikamine.kernel.subgroup.SGSets;
import org.vikamine.kernel.subgroup.SGStatistics;
import org.vikamine.kernel.subgroup.SGStatisticsBinary;
import org.vikamine.kernel.subgroup.SGStatisticsBuilder;
import org.vikamine.kernel.subgroup.SGStatisticsNumeric;
import org.vikamine.kernel.subgroup.quality.EstimatetableQFSimpleStatisticsBased;
import org.vikamine.kernel.subgroup.search.FPNodeNumeric.FPTreeNodeNumeric;
import org.vikamine.kernel.subgroup.search.FPNodeNumeric.FPTreePathNumeric;
import org.vikamine.kernel.subgroup.selectors.DefaultSGSelector;
import org.vikamine.kernel.subgroup.selectors.FastSelector;
import org.vikamine.kernel.subgroup.selectors.NegatedSGSelector;
import org.vikamine.kernel.subgroup.selectors.SGNominalSelector;
import org.vikamine.kernel.subgroup.selectors.SGSelector;
import org.vikamine.kernel.subgroup.target.BooleanTarget;
import org.vikamine.kernel.subgroup.target.NumericTarget;
import org.vikamine.kernel.subgroup.target.SGTarget;

/**
 * SDMAP - Subgroup Discovery Method using an Adapted Frequent Pattern Growth
 * Algorithm
 * 
 * @author atzmueller
 * 
 */
public class SDMapNumeric extends SDMethod {

    private static class MissingAdjustmentNumeric {

        public int sumUndefined;

        public int countUndefined;

        public static MissingAdjustmentNumeric EMPTY_MISSING_ADJUSTMENT = new MissingAdjustmentNumeric();
    }

    private static class SDMapMissingSelector extends DefaultSGSelector {

        private int cachedHashCode = 0;

        public SDMapMissingSelector(Attribute a, Set values) {
            super(a, values);
        }

        public SDMapMissingSelector(Attribute a, Value v) {
            super(a, v);
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (other.getClass() != this.getClass()) return false;
            SDMapMissingSelector miss = (SDMapMissingSelector) other;
            return this.getAttribute() == miss.getAttribute();
        }

        @Override
        public int hashCode() {
            if (cachedHashCode == 0) {
                cachedHashCode = super.hashCode();
            }
            return cachedHashCode;
        }
    }

    private static final String NAME = "SDMAP";

    private static List<? extends List<SGSelector>> computeCombinations(ArrayList<SGSelector> prefix, ArrayList<SGSelector> selectors, int maxSize, ArrayList<ArrayList<SGSelector>> result, List<SGSelector> conditionedSelectors) {
        result.add(prefix);
        if (prefix.size() == maxSize) {
            return result;
        }
        ArrayList<SGSelector> newRemainingSelectors = (ArrayList<SGSelector>) selectors.clone();
        for (int i = 0; i < selectors.size(); i++) {
            ArrayList<SGSelector> newSelectors = (ArrayList<SGSelector>) prefix.clone();
            SGSelector curSel = selectors.get(i);
            if ((curSel instanceof NegatedSGSelector) || ((!isAttributeContainedInSelectorSet(curSel.getAttribute(), prefix) && (!isAttributeContainedInSelectorSet(curSel.getAttribute(), conditionedSelectors))))) {
                newSelectors.add(curSel);
                newRemainingSelectors.remove(curSel);
                computeCombinations(newSelectors, newRemainingSelectors, maxSize, result, conditionedSelectors);
            }
        }
        return result;
    }

    private static List<? extends List<SGSelector>> computeCombinations(ArrayList<SGSelector> selectors, int maxSize, List<SGSelector> conditionedSelectors) {
        return computeCombinations(new ArrayList<SGSelector>(), selectors, maxSize, new ArrayList<ArrayList<SGSelector>>(), conditionedSelectors);
    }

    private static boolean isAttributeContainedInSelectorSet(Attribute attribute, List<SGSelector> selectors) {
        for (SGSelector sel : selectors) {
            if (sel.getAttribute() == attribute) {
                return true;
            }
        }
        return false;
    }

    protected static void sortFrequentNodesByOptimisticEstimate(List frequentNodes, final EstimatetableQFSimpleStatisticsBased qf, final double populationSize, final double p_0) {
        Collections.sort(frequentNodes, new Comparator<FPNode>() {

            @Override
            public int compare(FPNode n1, FPNode n2) {
                double estimate1 = qf.estimateQuality(n1.n, n1.tp, populationSize, p_0);
                double estimate2 = qf.estimateQuality(n2.n, n2.tp, populationSize, p_0);
                return -(Double.compare(estimate1, estimate2));
            }
        });
    }

    protected SGTarget target;

    double totalPopulationSize;

    double definedPositives;

    double definedNegatives;

    double populationMean;

    boolean pruningEnabled = true;

    KBestSGSet result;

    public SDMapNumeric() {
        super();
    }

    private MissingAdjustmentNumeric accumulateMissingAdjustment(FPTreeNumeric missingTree, Collection<SGSelector> missingSelectors) {
        MissingAdjustmentNumeric f = new MissingAdjustmentNumeric();
        for (SGSelector s : missingSelectors) {
            FPNodeNumeric node = missingTree.getFPNodeWithSameAttributeAsSelector(s);
            if (node != null) {
                f.sumUndefined += node.sum;
                f.countUndefined += node.n;
            }
        }
        List missingSelectorsToTestAsStart = new ArrayList(missingSelectors);
        FPNodeNumeric start = missingTree.findMostUnfrequentFPNode(missingSelectors);
        if (start != null) {
            missingSelectorsToTestAsStart.remove(start.sel);
            subtractPrefixPathsRecursively(missingTree, missingSelectorsToTestAsStart, missingSelectors, start, f);
        }
        return f;
    }

    private MissingAdjustmentNumeric calculateMissingAdjustment(FPTreeNumeric missingTree, List refSelectors) {
        if (missingTree.root.children.isEmpty()) {
            return MissingAdjustmentNumeric.EMPTY_MISSING_ADJUSTMENT;
        }
        Set attributes = new HashSet();
        for (Iterator iter = refSelectors.iterator(); iter.hasNext(); ) {
            SGSelector sel = (SGSelector) iter.next();
            attributes.add(sel.getAttribute());
        }
        if (!missingTree.containsAnyAttribute(attributes)) {
            return MissingAdjustmentNumeric.EMPTY_MISSING_ADJUSTMENT;
        }
        Collection<SGSelector> missingSelectors = createMissingSelectorSet(attributes);
        return accumulateMissingAdjustment(missingTree, missingSelectors);
    }

    protected void convertSGDescription(SG sg) {
        SGDescription sgd = sg.getSGDescription();
        List<SGSelector> selectors = new ArrayList();
        for (Iterator iter = sgd.iterator(); iter.hasNext(); ) {
            SGSelector sel = (SGSelector) iter.next();
            if (sel instanceof FastSelector) {
                selectors.add(new DefaultSGSelector(sel.getAttribute(), ((FastSelector) sel).getValues()));
            } else {
                selectors.add(sel);
            }
        }
        SGDescription converted = new SGDescription();
        converted.addAll(selectors);
        sg.setSGDescription(converted);
    }

    public final void convertSGDescriptions(SGSet result) {
        for (Iterator iter = result.iterator(); iter.hasNext(); ) {
            SG sg = (SG) iter.next();
            convertSGDescription(sg);
        }
    }

    private Collection<SGSelector> createMissingSelectorSet(Collection attributes) {
        Set selectors = new HashSet();
        for (Iterator iter = attributes.iterator(); iter.hasNext(); ) {
            Attribute att = (Attribute) iter.next();
            Boolean b = getOptions() == null ? null : getOptions().getBooleanAttributeOption(att, Options.TREAT_MISSING_NOT_AS_DEFINED_VALUE);
            if ((b != null) && (b.booleanValue())) {
                SGNominalSelector sel = new SDMapMissingSelector(att, Value.missingValue(att));
                selectors.add(sel);
            }
        }
        return selectors;
    }

    @Override
    public String getName() {
        return NAME;
    }

    private void growAndTestPatterns(FPTreeNumeric tree, FPTreeNumeric missingTree, SGDescription initialSGD) {
        if (getMethodStats() != null) {
            getMethodStats().increaseNodeCounter();
        }
        if (aborted) {
            return;
        }
        if (tree.root.children.isEmpty()) return;
        if (tree.treeHasSinglePath()) {
            testPatternsForSinglePath(tree, missingTree, initialSGD, Collections.EMPTY_LIST);
        } else {
            Map<FPNodeNumeric, SG> levelOneSubgroups = new HashMap<FPNodeNumeric, SG>();
            List<FPNodeNumeric> frequentNodes = new ArrayList(tree.frequentHeaderNodes);
            FPTreeNumeric.sortFrequentNodesAscending(frequentNodes);
            for (FPNodeNumeric headerNode : frequentNodes) {
                SGSelector conditioningSelector = headerNode.sel;
                List<SGSelector> newSelectors = new ArrayList();
                newSelectors.add(conditioningSelector);
                SG sg = new SG(getPopulation(), target);
                double sum = headerNode.sum;
                double n = headerNode.n;
                MissingAdjustmentNumeric f = calculateMissingAdjustment(missingTree, newSelectors);
                SGStatisticsBuilder.createSGStatisticsNumeric(sg, sum - f.sumUndefined, populationMean, n - f.countUndefined, totalPopulationSize);
                sg.updateQuality(task.getQualityFunction());
                levelOneSubgroups.put(headerNode, sg);
                if (result.isInKBestQualityRange(sg.getQuality())) {
                    SGDescription newSGD = (SGDescription) initialSGD.clone();
                    newSGD.addAll(newSelectors);
                    sg.setSGDescription(newSGD);
                    if ((!task.isSuppressStrictlyIrrelevantSubgroups()) || (!SGSets.isSGStrictlyIrrelevant(sg, result))) {
                        addSubgroupToSGSet(result, sg);
                    }
                }
            }
            for (Map.Entry<FPNodeNumeric, SG> entry : levelOneSubgroups.entrySet()) {
                FPNodeNumeric headerNode = entry.getKey();
                if (!pruningEnabled || result.isInKBestQualityRange(headerNode.estimate)) {
                    FPTreeNumeric conditionalFPTree = tree.buildConditionalFPTree(headerNode);
                    if (conditionalFPTree != null) growAndTestPatternsRecursive(conditionalFPTree, missingTree, initialSGD, Arrays.asList(new SGSelector[] { headerNode.sel }), 1);
                }
            }
        }
    }

    private void growAndTestPatternsRecursive(FPTreeNumeric tree, FPTreeNumeric missingTree, SGDescription initialSGdescription, List<SGSelector> conditionedSelectors, int depth) {
        if (getMethodStats() != null) {
            getMethodStats().increaseNodeCounter();
        }
        if (aborted) {
            return;
        }
        if (tree.root.children.isEmpty() || (depth >= task.getMaxSGDSize())) return;
        if (tree.treeHasSinglePath()) {
            testPatternsForSinglePath(tree, missingTree, initialSGdescription, conditionedSelectors);
        } else {
            List<FPNodeNumeric> frequentNodes = new ArrayList<FPNodeNumeric>(tree.frequentHeaderNodes);
            FPTreeNumeric.sortFrequentNodesAscending(frequentNodes);
            for (FPNodeNumeric headerNode : frequentNodes) {
                SGSelector conditioningSelector = headerNode.sel;
                if (isAttributeContainedInSelectorSet(conditioningSelector.getAttribute(), conditionedSelectors)) {
                    continue;
                }
                List<SGSelector> newSelectors = new ArrayList<SGSelector>();
                newSelectors.addAll(conditionedSelectors);
                newSelectors.add(conditioningSelector);
                SG sg = new SG(getPopulation(), target);
                double sum = headerNode.sum;
                double n = headerNode.n;
                MissingAdjustmentNumeric f = calculateMissingAdjustment(missingTree, newSelectors);
                SGStatisticsBuilder.createSGStatisticsNumeric(sg, sum - f.sumUndefined, populationMean, n - f.countUndefined, totalPopulationSize);
                sg.updateQuality(task.getQualityFunction());
                if (result.isInKBestQualityRange(sg.getQuality())) {
                    SGDescription newSGD = (SGDescription) initialSGdescription.clone();
                    newSGD.addAll(newSelectors);
                    sg.setSGDescription(newSGD);
                    if ((!task.isSuppressStrictlyIrrelevantSubgroups()) || (!SGSets.isSGStrictlyIrrelevant(sg, result))) {
                        addSubgroupToSGSet(result, sg);
                    }
                }
                if (!pruningEnabled || result.isInKBestQualityRange(headerNode.estimate)) {
                    FPTreeNumeric conditionalFPTree = tree.buildConditionalFPTree(headerNode);
                    if (conditionalFPTree != null) growAndTestPatternsRecursive(conditionalFPTree, missingTree, initialSGdescription, newSelectors, depth + 1);
                }
            }
        }
    }

    private void initDefaultTargetShareAndPopulationSize(SG initialSubgroup) {
        SGStatistics stats = initialSubgroup.getStatistics();
        if (target instanceof BooleanTarget) {
            SGStatisticsBinary statsBin = (SGStatisticsBinary) stats;
            definedPositives = statsBin.getPositives();
            definedNegatives = statsBin.getNegatives();
            populationMean = statsBin.getP0();
        } else if (target instanceof NumericTarget) {
            SGStatisticsNumeric statsNumeric = (SGStatisticsNumeric) stats;
            definedPositives = statsNumeric.getPopulationMean() * statsNumeric.getDefinedPopulationCount();
            definedNegatives = statsNumeric.getDefinedPopulationCount() - definedPositives;
            populationMean = statsNumeric.getPopulationMean();
        }
        totalPopulationSize = stats.getDefinedPopulationCount();
    }

    public boolean isPruningEnabled() {
        return pruningEnabled;
    }

    @Override
    public boolean isTreatMissingAsUndefinedSupported() {
        return true;
    }

    @Override
    protected SGSet search(final SG initialSubgroup) {
        this.target = initialSubgroup.getTarget();
        initDefaultTargetShareAndPopulationSize(initialSubgroup);
        boolean allowPrune = true;
        result = SGSets.createKBestSGSet(task.getMaxSGCount(), task.getMinQualityLimit());
        FPTreeNumeric tree = new FPTreeNumeric(getSelectorSet(initialSubgroup, target), new DataRecordIteration(initialSubgroup.subgroupInstanceIterator()), allowPrune, this);
        DataRecordIteration missingInstancesIteration = new DataRecordIteration(new FilteringDataRecordIterator(getPopulation().instanceIterator(), new IncludingDataRecordFilter() {

            @Override
            public boolean isIncluded(DataRecord instance) {
                return initialSubgroup.getStatistics().isInstanceDefinedForSubgroupVars(instance);
            }
        }));
        FPTreeNumeric missingTree = new FPTreeNumeric(createMissingSelectorSet(tree.extractFrequentAttributes()), missingInstancesIteration, false, this);
        SGDescription initialSGDescription = initialSubgroup.getSGDescription();
        growAndTestPatterns(tree, missingTree, initialSGDescription);
        convertSGDescriptions(result);
        if (target instanceof NumericTarget) {
            for (SG sg : result) {
                sg.createStatistics(getOptions());
            }
        }
        return result;
    }

    public void setPruningEnabled(boolean pruningEnabled) {
        this.pruningEnabled = pruningEnabled;
    }

    private void subtractPrefixPathsRecursively(FPTreeNumeric missingTree, Collection missingSelectorsToTestAsStart, Collection missingSelectors, FPNodeNumeric start, MissingAdjustmentNumeric f) {
        List<? extends FPTreePathNumeric> paths = start.getAllPrefixPaths();
        for (FPTreePathNumeric path : paths) {
            if (path.containsAnySelectors(missingSelectors)) {
                f.sumUndefined -= path.sum;
                f.countUndefined -= path.n;
            }
        }
        start = missingTree.findMostUnfrequentFPNode(missingSelectorsToTestAsStart);
        if (start != null) {
            missingSelectorsToTestAsStart.remove(start.sel);
            subtractPrefixPathsRecursively(missingTree, missingSelectorsToTestAsStart, missingSelectors, start, f);
        }
    }

    private void testPatternsForSinglePath(FPTreeNumeric tree, FPTreeNumeric missingTree, SGDescription sgd, List conditionedSelectors) {
        ArrayList selectors = new ArrayList();
        FPTreeNodeNumeric node = tree.root;
        while (!node.children.isEmpty()) {
            node = node.children.get(0);
            selectors.add(node.sel);
        }
        List<? extends List<SGSelector>> powerSetOfSelectors = computeCombinations(selectors, task.getMaxSGDSize() - conditionedSelectors.size(), conditionedSelectors);
        for (List combinationOfSelectors : powerSetOfSelectors) {
            if (getMethodStats() != null) {
                getMethodStats().increaseNodeCounter();
            }
            if (!combinationOfSelectors.isEmpty()) {
                List newSelectors = new ArrayList();
                newSelectors.addAll(conditionedSelectors);
                newSelectors.addAll(combinationOfSelectors);
                if (newSelectors.size() > task.getMaxSGDSize()) {
                    continue;
                }
                SG sg = new SG(getPopulation(), target);
                FPNodeNumeric mostUnfrequentNodeInCombination = tree.findMostUnfrequentFPNode(combinationOfSelectors);
                double sum = mostUnfrequentNodeInCombination.sum;
                double n = mostUnfrequentNodeInCombination.n;
                MissingAdjustmentNumeric f = calculateMissingAdjustment(missingTree, newSelectors);
                SGStatisticsBuilder.createSGStatisticsNumeric(sg, sum - f.sumUndefined, populationMean, n - f.countUndefined, totalPopulationSize);
                sg.updateQuality(task.getQualityFunction());
                if (result.isInKBestQualityRange(sg.getQuality())) {
                    SGDescription newSGD = (SGDescription) sgd.clone();
                    newSGD.addAll(newSelectors);
                    sg.setSGDescription(newSGD);
                    if ((!task.isSuppressStrictlyIrrelevantSubgroups()) || (!SGSets.isSGStrictlyIrrelevant(sg, result))) {
                        addSubgroupToSGSet(result, sg);
                    }
                }
            }
        }
    }
}
