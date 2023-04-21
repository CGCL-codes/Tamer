package org.vikamine.kernel.subgroup.search;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.vikamine.kernel.data.Attribute;
import org.vikamine.kernel.subgroup.KBestSGSet;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.SGDescription;
import org.vikamine.kernel.subgroup.SGSet;
import org.vikamine.kernel.subgroup.SGSets;
import org.vikamine.kernel.subgroup.SGStatisticsBinary;
import org.vikamine.kernel.subgroup.selectors.SGNominalSelector;
import org.vikamine.kernel.subgroup.selectors.SGSelector;
import org.vikamine.kernel.subgroup.target.SGTarget;

/**
 * @author Atzmueller
 */
public class SDBeamSearch extends SDMethod {

    private static final String NAME = "SDBEAMSearch";

    protected boolean hasImproved = false;

    private int initialSubgroupDescriptionLength = 0;

    private List iterationSubgroupResults;

    public SDBeamSearch() {
        super();
    }

    private void expandBeamUsingAttribute(SG sg, Attribute attr, KBestSGSet resultSGSet) {
        for (SGSelector sel : task.searchSpace) {
            if (sel.getAttribute().equals(attr)) {
                SG currentSG = (SG) sg.clone();
                SGDescription currentSGD = currentSG.getSGDescription();
                if (addSGSelector(currentSGD, sel)) {
                    if (!resultSGSet.contains(currentSG)) {
                        currentSG.createStatistics(getOptions());
                        currentSG.updateQuality(task.getQualityFunction());
                        if (doAddSubgroup(resultSGSet, currentSG)) {
                            addSGToSGSet(resultSGSet, currentSG);
                            hasImproved = true;
                        }
                    }
                }
            }
        }
    }

    protected boolean addSGSelector(SGDescription sgd, SGSelector sel) {
        if (!sgd.contains((SGNominalSelector) sel)) {
            sgd.add(sel);
            return true;
        }
        return false;
    }

    private KBestSGSet beam(KBestSGSet currentSGSet, SGTarget target) {
        hasImproved = false;
        KBestSGSet resultSGSet = (KBestSGSet) SGSets.copySGSet(currentSGSet);
        Iterator subgroupIter = currentSGSet.iterator();
        while (subgroupIter.hasNext()) {
            SG sg = (SG) subgroupIter.next();
            SGDescription sgd = sg.getSGDescription();
            if ((sgd.size() - initialSubgroupDescriptionLength) >= task.getMaxSGDSize()) {
                break;
            }
            for (Iterator iter = task.getAttributes().iterator(); iter.hasNext(); ) {
                Attribute attr = (Attribute) iter.next();
                if (isValidAttribute(attr, target, sgd)) {
                    expandBeamUsingAttribute(sg, attr, resultSGSet);
                }
            }
        }
        return resultSGSet;
    }

    private void addSGToSGSet(KBestSGSet resultSGSet, SG currentSG) {
        resultSGSet.addByReplacingWorstSG(currentSG);
        if (task.isSuppressStrictlyIrrelevantSubgroups()) {
            SGSets.removeIrrelevantSubgroupsFromSGSet(resultSGSet);
        }
    }

    protected boolean doAddSubgroup(KBestSGSet sgSet, SG currentSG) {
        SGStatisticsBinary statistics = (SGStatisticsBinary) currentSG.getStatistics();
        return (fullfillsMinSupport(statistics.getTp(), currentSG.getStatistics().getSubgroupSize()) && sgSet.isInKBestQualityRange(currentSG.getQuality())) && (!sgSet.contains(currentSG)) && ((!task.isSuppressStrictlyIrrelevantSubgroups()) || (!SGSets.isSGStrictlyIrrelevant(currentSG, sgSet)));
    }

    protected boolean isValidAttribute(Attribute attr, SGTarget target, SGDescription sgd) {
        return (attr.isNominal()) && (!sgd.containsAttributeAsSelector(attr)) && (!target.getAttributes().contains(attr) && ((task.getAttributes() == null) || (task.getAttributes().contains(attr))));
    }

    protected void searchInternal(SG initialSubgroup) {
        iterationSubgroupResults = new LinkedList();
        KBestSGSet currentSGSet = SGSets.createKBestSGSet(task.getMaxSGCount(), task.getMinQualityLimit());
        double initQual = task.getQualityFunction().evaluate(initialSubgroup);
        SG clonedInitial = (SG) initialSubgroup.clone();
        clonedInitial.setQuality(initQual);
        currentSGSet.add(clonedInitial);
        initialSubgroupDescriptionLength = initialSubgroup.getSGDescription().size();
        int iteration = 1;
        do {
            hasImproved = false;
            KBestSGSet resultSGSet = beam(currentSGSet, initialSubgroup.getTarget());
            if (hasImproved) {
                resultSGSet.setName(getName() + " i" + iteration);
                iterationSubgroupResults.add(resultSGSet);
                currentSGSet = resultSGSet;
                iteration++;
            }
        } while (hasImproved);
        SGSet allIterationResultsSGSet = SGSets.createKBestSGSet(KBestSGSet.DEFAULT_MAX_SG_COUNT, task.getMinQualityLimit());
        for (Iterator iter = iterationSubgroupResults.iterator(); iter.hasNext(); ) {
            SGSet sgSet = (SGSet) iter.next();
            for (Iterator iterator = sgSet.iterator(); iterator.hasNext(); ) {
                SG sg = (SG) iterator.next();
                if (!allIterationResultsSGSet.contains(sg)) {
                    allIterationResultsSGSet.add(sg);
                }
            }
        }
        allIterationResultsSGSet.setName(getName() + " All");
        iterationSubgroupResults.add(0, allIterationResultsSGSet);
    }

    @Override
    protected SGSet search(SG initialSubgroup) {
        searchInternal(initialSubgroup);
        return SGSets.mergeSGSetsToKBestSGSet(iterationSubgroupResults, task.getMaxSGCount(), task.getMinQualityLimit());
    }

    /**
     * Must not be called before perform() method!
     * 
     * @return
     */
    public List<SGSet> getIterationResults() {
        if (iterationSubgroupResults == null) {
            throw new IllegalStateException("Must perform subgroup search, before using Iteration results");
        }
        return iterationSubgroupResults;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isTreatMissingAsUndefinedSupported() {
        return true;
    }
}
