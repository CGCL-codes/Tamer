package plugin.pretokens.test;

import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteException;
import pcgen.core.prereq.PrerequisiteTest;
import pcgen.util.PropertyFactory;

/**
 * @author wardc
 *
 */
public class PreHPTester extends AbstractPrerequisiteTest implements PrerequisiteTest {

    public int passes(final Prerequisite prereq, final PlayerCharacter character) throws PrerequisiteException {
        int runningTotal;
        try {
            final int targetHP = Integer.parseInt(prereq.getOperand());
            runningTotal = prereq.getOperator().compare(character.hitPoints(), targetHP);
        } catch (NumberFormatException nfe) {
            throw new PrerequisiteException(PropertyFactory.getFormattedString("PreHP.error.bad_operand", prereq.getOperand()));
        }
        return countedTotal(prereq, runningTotal);
    }

    public String kindHandled() {
        return "HP";
    }
}
