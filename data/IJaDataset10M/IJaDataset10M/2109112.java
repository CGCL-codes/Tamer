package plugin.pretokens.test;

import pcgen.core.Equipment;
import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteException;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.core.prereq.PrerequisiteTest;
import pcgen.util.PropertyFactory;
import java.util.Iterator;
import java.util.List;

/**
 * @author frugal@purplewombat.co.uk
 *
 */
public class PreTypeTester extends AbstractPrerequisiteTest implements PrerequisiteTest {

    public String kindHandled() {
        return "TYPE";
    }

    public int passes(final Prerequisite prereq, final Equipment equipment, PlayerCharacter aPC) throws PrerequisiteException {
        final String requiredType = prereq.getKey();
        int runningTotal = 0;
        if (prereq.getOperator().equals(PrerequisiteOperator.EQ)) {
            if (equipment.isPreType(requiredType)) {
                runningTotal++;
            }
        } else if (prereq.getOperator().equals(PrerequisiteOperator.NEQ)) {
            if (!equipment.isPreType(requiredType)) {
                runningTotal++;
            }
        } else {
            throw new PrerequisiteException(PropertyFactory.getFormattedString("PreType.error.invalidComparison", prereq.getOperator().toString(), prereq.toString()));
        }
        runningTotal = countedTotal(prereq, runningTotal);
        return runningTotal;
    }

    public int passes(final Prerequisite prereq, final PlayerCharacter aPC) {
        if (aPC == null) {
            return 0;
        }
        final String requiredType = prereq.getKey();
        final int numRequired = Integer.parseInt(prereq.getOperand());
        int runningTotal = 0;
        final List typeList = aPC.getTypes();
        for (Iterator iter = typeList.iterator(); iter.hasNext(); ) {
            final String element = (String) iter.next();
            if (element.equalsIgnoreCase(requiredType)) {
                runningTotal++;
            }
        }
        runningTotal = prereq.getOperator().compare(runningTotal, numRequired);
        return countedTotal(prereq, runningTotal);
    }

    public String toHtmlString(final Prerequisite prereq) {
        return PropertyFactory.getFormattedString("PreType.toHtml", prereq.getOperator().toDisplayString(), prereq.getKey());
    }
}
