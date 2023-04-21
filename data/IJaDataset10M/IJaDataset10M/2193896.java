package plugin.pretokens.test;

import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteTest;
import pcgen.util.PropertyFactory;
import java.util.List;

/**
 * @author blithwyn
 *
 */
public class PreSpellDescriptorTester extends AbstractPrerequisiteTest implements PrerequisiteTest {

    public int passes(final Prerequisite prereq, final PlayerCharacter character) {
        final String descriptor = prereq.getKey();
        final int requiredLevel = Integer.parseInt(prereq.getSubKey());
        final int requiredNumber = Integer.parseInt(prereq.getOperand());
        final List aArrayList = character.aggregateSpellList("Any", "No-Match", "A", descriptor, requiredLevel, 20);
        final int runningTotal = prereq.getOperator().compare(aArrayList.size(), requiredNumber);
        return countedTotal(prereq, runningTotal);
    }

    public String kindHandled() {
        return "SPELL.DESCRIPTOR";
    }

    public String toHtmlString(final Prerequisite prereq) {
        final Object[] args = new Object[] { prereq.getOperator().toDisplayString(), prereq.getOperand(), prereq.getSubKey(), prereq.getKey() };
        return PropertyFactory.getFormattedString("PreSpellDescriptor.toHtml", args);
    }
}
