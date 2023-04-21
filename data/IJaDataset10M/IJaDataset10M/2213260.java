package plugin.pretokens.test;

import pcgen.core.PlayerCharacter;
import pcgen.core.Skill;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteTest;
import pcgen.util.PropertyFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author frugal@purplewombat.co.uk
 *
 */
public class PreSkillMultTester extends AbstractPrerequisiteTest implements PrerequisiteTest {

    public int passes(final Prerequisite prereq, final PlayerCharacter character) {
        int runningTotal = 0;
        final int requiredRanks = Integer.parseInt(prereq.getOperand());
        String requiredSkillName = prereq.getKey().toUpperCase();
        final boolean isType = (requiredSkillName.startsWith("TYPE.") || requiredSkillName.startsWith("TYPE="));
        if (isType) {
            requiredSkillName = requiredSkillName.substring(5).toUpperCase();
        }
        final String skillName = requiredSkillName.toUpperCase();
        final int percentageSignPosition = skillName.lastIndexOf('%');
        boolean foundMatch = false;
        final List sList = (ArrayList) character.getSkillList().clone();
        for (Iterator e1 = sList.iterator(); e1.hasNext() && !foundMatch; ) {
            final Skill aSkill = (Skill) e1.next();
            final String aSkillName = aSkill.getName().toUpperCase();
            if (isType) {
                if (percentageSignPosition >= 0) {
                    final int maxCount = aSkill.getMyTypeCount();
                    for (int k = 0; k < maxCount && !foundMatch; k++) {
                        if (aSkill.getMyType(k).startsWith(skillName.substring(0, percentageSignPosition))) {
                            foundMatch = true;
                        }
                    }
                } else if (aSkill.isType(skillName)) {
                    foundMatch = true;
                }
                if (foundMatch) {
                    final int result = prereq.getOperator().compare(aSkill.getTotalRank(character).intValue(), requiredRanks);
                    if (result == 0) {
                        foundMatch = false;
                    } else {
                        runningTotal = result;
                    }
                }
            } else if (aSkillName.equals(skillName) || ((percentageSignPosition >= 0) && aSkillName.startsWith(skillName.substring(0, percentageSignPosition)))) {
                final int result = prereq.getOperator().compare(aSkill.getTotalRank(character).intValue(), requiredRanks);
                if (result > 0) {
                    foundMatch = true;
                    runningTotal = result;
                }
            }
        }
        return countedTotal(prereq, runningTotal);
    }

    public String kindHandled() {
        return "SKILLMULT";
    }

    public String toHtmlString(final Prerequisite prereq) {
        String skillName = prereq.getKey();
        if (prereq.getSubKey() != null && !prereq.getSubKey().equals("")) {
            skillName += " (" + prereq.getSubKey() + ")";
        }
        final String foo = PropertyFactory.getFormattedString("PreSkillMult.toHtml", new Object[] { prereq.getOperator().toDisplayString(), prereq.getOperand(), skillName });
        return foo;
    }
}
