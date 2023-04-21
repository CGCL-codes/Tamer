package de.tum.in.botl.transformer.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import de.tum.in.botl.model.interfaces.BotlObjectInterface;
import de.tum.in.botl.ruleSet.interfaces.ObjectVariableInterface;

/**
 * @author Frank
 */
public class ObjectMatchFinder {

    private int[] actObjectMatch;

    private int[] maxIndex;

    private Map objectsByType;

    private ArrayList objectVariables;

    private boolean moreMatchesExist;

    private Match nextMatch;

    private int numOfObjectVars;

    protected ObjectMatchFinder(Map objectTable, ArrayList objectVariables) {
        this.objectsByType = objectTable;
        this.objectVariables = objectVariables;
        numOfObjectVars = objectVariables.size();
        maxIndex = new int[numOfObjectVars];
        for (int i = 0; i < numOfObjectVars; i++) {
            ArrayList objects = (ArrayList) objectsByType.get(((ObjectVariableInterface) objectVariables.get(i)).getType());
            maxIndex[i] = objects.size() - 1;
        }
        reInitialize();
    }

    protected void reInitialize() {
        actObjectMatch = new int[numOfObjectVars];
        Arrays.fill(actObjectMatch, 0);
        moreMatchesExist = true;
        createNextMatch();
        while (moreMatchesExist && nextMatch.getObjMatches().entrySet().size() != numOfObjectVars) createNextMatch();
    }

    protected boolean hasMoreMatches() {
        return (nextMatch != null);
    }

    protected Match nextMatch() {
        Match result = nextMatch;
        createNextMatch();
        while (moreMatchesExist && nextMatch.getObjMatches().entrySet().size() != numOfObjectVars) createNextMatch();
        return result;
    }

    /**
   * 
   */
    private void incrementActualMatch() {
        for (int i = 0; i < numOfObjectVars; i++) {
            if (actObjectMatch[i] < maxIndex[i]) {
                actObjectMatch[i]++;
                return;
            } else actObjectMatch[i] = 0;
        }
        moreMatchesExist = false;
    }

    /**
   * @return The actualMatch
   */
    private void createNextMatch() {
        if (!moreMatchesExist) {
            nextMatch = null;
            return;
        }
        incrementActualMatch();
        Match result = new Match();
        try {
            for (int i = 0; i < numOfObjectVars; i++) {
                ArrayList objects = (ArrayList) objectsByType.get(((ObjectVariableInterface) objectVariables.get(i)).getType());
                BotlObjectInterface bo = (BotlObjectInterface) objects.get(actObjectMatch[i]);
                result.addObjMatch((ObjectVariableInterface) objectVariables.get(i), bo);
            }
            nextMatch = result;
        } catch (IndexOutOfBoundsException e) {
            moreMatchesExist = false;
            nextMatch = null;
            return;
        }
    }
}
