package streamcruncher.innards.core.partition.correlation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DelayedMatcher extends ImmediateMatcher {

    protected final Set<Object> waitAndWatchCorrIdList;

    protected final List<Object> emptyList;

    public DelayedMatcher(String[] presentAliases, String[] notPresentAliases) {
        super(presentAliases, notPresentAliases);
        this.waitAndWatchCorrIdList = new HashSet<Object>();
        this.emptyList = new ArrayList<Object>();
    }

    @Override
    public void eventExpelled(String alias, Object id) {
        int bitPositionForAlias = aliasAndBitPatterns.get(alias);
        Integer pattern = workingPatternSet.get(id);
        if (pattern == null) {
            pattern = stalePatternSet.get(id);
            if (pattern == null) {
            } else {
                pattern = pattern & ~bitPositionForAlias;
                if (pattern == 0) {
                    stalePatternSet.remove(id);
                    waitAndWatchCorrIdList.remove(id);
                } else {
                    stalePatternSet.put(id, pattern);
                }
            }
        } else {
            Integer oldPattern = pattern;
            pattern = pattern & ~bitPositionForAlias;
            if (oldPattern == targetBitPattern && waitAndWatchCorrIdList.contains(id) == true) {
                corrIdSessionHitList.add(id);
            }
            if (pattern == 0) {
                workingPatternSet.remove(id);
                waitAndWatchCorrIdList.remove(id);
            } else {
                workingPatternSet.put(id, pattern);
            }
        }
    }

    @Override
    public List<Object> endExpulsions() {
        ArrayList<Object> results = new ArrayList<Object>(corrIdSessionHitList);
        for (Iterator iter = corrIdSessionHitList.iterator(); iter.hasNext(); ) {
            Object staleId = iter.next();
            iter.remove();
            Integer pattern = workingPatternSet.remove(staleId);
            stalePatternSet.put(staleId, pattern);
            waitAndWatchCorrIdList.remove(staleId);
        }
        return results;
    }

    @Override
    protected void handleWorkingSetArrival(Object id, int bitPositionForAlias, Integer pattern) {
        Integer updatedPattern = pattern | bitPositionForAlias;
        if (updatedPattern == targetBitPattern) {
            corrIdSessionHitList.add(id);
        } else {
            corrIdSessionHitList.remove(id);
            waitAndWatchCorrIdList.remove(id);
        }
        workingPatternSet.put(id, updatedPattern);
    }

    @Override
    public List<Object> endArrivals() {
        waitAndWatchCorrIdList.addAll(corrIdSessionHitList);
        corrIdSessionHitList.clear();
        return emptyList;
    }
}
