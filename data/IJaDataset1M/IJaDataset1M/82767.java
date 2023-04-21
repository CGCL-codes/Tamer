package pcgen.core.prereq;

import pcgen.core.PlayerCharacter;
import pcgen.util.PropertyFactory;

/**
 * @author wardc
 *
 */
public class PreHands extends AbstractPrerequisiteTest implements PrerequisiteTest {

    public int passes(final Prerequisite prereq, final PlayerCharacter character) throws PrerequisiteException {
        int runningTotal;
        try {
            final int targetHands = Integer.parseInt(prereq.getOperand());
            runningTotal = prereq.getOperator().compare(character.getRace().getHands(), targetHands);
        } catch (NumberFormatException nfe) {
            throw new PrerequisiteException(PropertyFactory.getFormattedString("PreHands.error.badly_formed", prereq.getOperand()));
        }
        return countedTotal(prereq, runningTotal);
    }

    public String kindHandled() {
        return "HANDS";
    }
}
