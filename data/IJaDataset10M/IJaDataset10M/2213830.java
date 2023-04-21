package plugin.pretokens.test;

import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.RaceType;
import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteTest;

/**
 * @author	wardc
 * @author	byngl <byngl@hotmail.com>
 *
 */
public class PreRaceTypeTester extends AbstractPrerequisiteTest implements PrerequisiteTest {

    @Override
    public int passes(final Prerequisite prereq, final PlayerCharacter character) {
        final int reqnumber = Integer.parseInt(prereq.getOperand());
        final String requiredRaceType = prereq.getKey();
        int runningTotal = 0;
        try {
            RaceType preRaceType = RaceType.valueOf(requiredRaceType);
            if (preRaceType.equals(character.getRace().get(ObjectKey.RACETYPE))) {
                runningTotal++;
            }
        } catch (IllegalArgumentException e) {
        }
        if (character.getCritterType().indexOf(requiredRaceType) >= 0) {
            runningTotal++;
        }
        runningTotal = prereq.getOperator().compare(runningTotal, reqnumber);
        return countedTotal(prereq, runningTotal);
    }

    public String kindHandled() {
        return "RACETYPE";
    }
}
