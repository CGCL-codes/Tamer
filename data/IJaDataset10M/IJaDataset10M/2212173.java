package mipt.math.sys.alt.solve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mipt.math.Number;
import mipt.math.sys.alt.AbstractDecisionSolution;
import mipt.math.sys.alt.AlternativesProblem;
import mipt.math.sys.alt.DecisionSolution;
import mipt.math.sys.alt.GradeProblem;

/**
 * Fills AbstractDecisionSolution (even subclass can't fill inconsistent solution type!),
 *  defines what alternatives are in groups (the default implementation is trivial, see below)
 *  and defines rules of groups ordering (if needed).
 * Do not use this creator if you don't need groups at all - it would be waste of time!
 *  (however you CAN use it not only for GradeProblems if you need e.g. sorting of alternatives
 *   - in this case DefaultDecisionSolution is adequate structure too).
 * In this default implementation, groupsCount in the solution is the number of different scores
 *  (so it's almost equivalent to a solution of linear ranking problem;
 *  but all the work is done here to support group ranking (grading) problems.
 * To implement grading, 1) override getGroupKey(int,Number,List) (return group index, not score value)
 *  and 2) either send non-null groups from GroupingAlgorithm to this.fillSolution()
 *  or override getGroupValue() (its default implementation does grouping by equal score values).
 * Note: does not use any problem except for GradeProblem (instanceof check is here)
 * @author Evdokimov
 */
public class DefaultDecisionSolutionCreator implements DecisionSolutionCreator {

    /**
	 * Used to identify group in Map (map collects alternative indices).
	 * If groups is null and sortGroups is not overridden, the result must be Comparable. 
	 * This implementation allows to treat linear grading problems as group-grading
	 *  - it returns the score (of alternative!) - 
	 *   but it can be overridden to return integer (group index in list), etc.
	 * @param groups - can be null
	 * @param groups - GradeProblem
	 */
    protected Object getGroupKey(int alternativeIndex, Number alternativeScore, List groups) {
        return alternativeScore;
    }

    /**
	 * By default groups should contain group scores.
	 */
    protected Object getGroupKey(int groupIndex, List groups) {
        return groups.get(groupIndex);
    }

    /**
	 * By default return group key. 
	 */
    protected Number getGroupValue(int groupIndex, Object groupKey) {
        return (Number) groupKey;
    }

    /**
	 * Used if groups was null and should be determined by alternatives' scores. 
	 * By default order is determined by values returned by groupKeys (they should be comparable!);
	 *  but overriders can use their own Comparator.
	 * @param groups - group keys
	 */
    protected void sortGroups(List groups) {
        Collections.sort(groups);
    }

    /**
	 * @see mipt.math.sys.alt.solve.DecisionSolutionCreator#fillSolution(mipt.math.sys.alt.DefaultDecisionSolution, mipt.math.Number[], java.util.List)
	 */
    public void fillSolution(DecisionSolution solution, Object allScores, List groups, AlternativesProblem problem) {
        if (!(solution instanceof AbstractDecisionSolution)) throw new IllegalArgumentException("DefaultDecisionSolutionCreator can fill AbstractDecisionSolution only");
        Map<Integer, Number> allScoresMap = allScores instanceof Map ? (Map<Integer, Number>) allScores : null;
        Number[] allScoresArray = allScoresMap == null ? (Number[]) allScores : null;
        int alternativesCount = allScoresMap == null ? allScoresArray.length : allScoresMap.size();
        int groupsCapacity;
        boolean groupsWasNull = false, isGradeProblem = problem instanceof GradeProblem;
        if (groups != null) groupsCapacity = groups.size(); else {
            groupsWasNull = true;
            groupsCapacity = getGroupsCountEstimate(alternativesCount, isGradeProblem);
            groups = new ArrayList(groupsCapacity);
        }
        HashMap<Object, List<Integer>> map = new HashMap<Object, List<Integer>>(groupsCapacity * 4 / 3 + 1);
        if (allScoresMap == null) for (int i = 0; i < alternativesCount; i++) {
            addAlternative(new Integer(i), i, allScoresArray[i], groupsWasNull, groups, map);
        } else for (Map.Entry<Integer, Number> entry : allScoresMap.entrySet()) {
            addAlternative(entry.getKey(), entry.getKey().intValue(), entry.getValue(), groupsWasNull, groups, map);
        }
        if (isGradeProblem) {
            GradeProblem prob = (GradeProblem) problem;
            for (int i = prob.getGradesCount() - 1; i >= 0; i--) {
                getAlternativesList(groupsWasNull, groups, map, prob.getGrade(i));
            }
        }
        if (groupsWasNull) sortGroups(groups);
        fillSolution(solution, allScores, groups, map);
    }

    private void addAlternative(Integer altKey, int altIndex, Number altValue, boolean groupsWasNull, List groups, HashMap<Object, List<Integer>> map) {
        getAlternativesList(groupsWasNull, groups, map, getGroupKey(altIndex, altValue, groupsWasNull ? null : groups)).add(altKey);
    }

    /**
	 * Can be overriden for performance only
	 */
    protected int getGroupsCountEstimate(int alternativesCount, boolean isGradeProblem) {
        return isGradeProblem ? 1 + alternativesCount / 4 : alternativesCount;
    }

    /**
	 * Gets list of alternatives of the given group from map
	 *  (and puts new list to map for the first time).
	 * @param groupsWasNull - if true, adds group to the list
	 * @param groups
	 * @param map
	 * @param groupKey
	 * @return
	 */
    protected final List<Integer> getAlternativesList(boolean groupsWasNull, List groups, HashMap<Object, List<Integer>> map, Object groupKey) {
        List<Integer> list = map.get(groupKey);
        if (list == null) {
            if (groupsWasNull) groups.add(groupKey);
            list = new LinkedList();
            map.put(groupKey, list);
        }
        return list;
    }

    /**
	 * Final step of the solution creation process.  
	 */
    protected void fillSolution(DecisionSolution solution, Object allScores, List groups, HashMap<Object, List<Integer>> map) {
        AbstractDecisionSolution sol = (AbstractDecisionSolution) solution;
        sol.clearGroups();
        for (int i = 0; i < groups.size(); i++) {
            Object groupKey = getGroupKey(i, groups);
            List<Integer> list = map.get(groupKey);
            int[] indices = new int[list.size()];
            for (int j = 0; j < indices.length; j++) {
                indices[j] = list.get(j).intValue();
            }
            sol.addGroup(i, groupKey, getGroupValue(i, groupKey), indices, allScores);
        }
    }
}
