package plugin.pretokens.test;

import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteException;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.core.prereq.PrerequisiteTest;
import pcgen.util.PropertyFactory;

/**
 * @author wardc
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PreDeityTester extends AbstractPrerequisiteTest implements PrerequisiteTest {

    public int passes(final Prerequisite prereq, final PlayerCharacter character) throws PrerequisiteException {
        int runningTotal;
        final String charDeity = character.getDeity() != null ? character.getDeity().getName() : "";
        if (prereq.getOperator().equals(PrerequisiteOperator.EQ)) {
            runningTotal = (charDeity.equalsIgnoreCase(prereq.getOperand())) ? 1 : 0;
        } else if (prereq.getOperator().equals(PrerequisiteOperator.NEQ)) {
            runningTotal = (charDeity.equalsIgnoreCase(prereq.getOperand())) ? 0 : 1;
        } else {
            throw new PrerequisiteException(PropertyFactory.getFormattedString("PreDeity.error.bad_coparator", prereq.toString()));
        }
        return countedTotal(prereq, runningTotal);
    }

    public String kindHandled() {
        return "DEITY";
    }
}
