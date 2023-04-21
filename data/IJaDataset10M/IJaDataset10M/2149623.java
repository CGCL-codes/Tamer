package plugin.pretokens.test;

import pcgen.core.PCTemplate;
import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteException;
import pcgen.core.prereq.PrerequisiteTest;
import pcgen.util.PropertyFactory;
import java.util.Iterator;

/**
 * @author wardc
 *
 */
public class PreTemplateTester extends AbstractPrerequisiteTest implements PrerequisiteTest {

    public int passes(final Prerequisite prereq, final PlayerCharacter character) throws PrerequisiteException {
        int runningTotal = 0;
        final int number;
        try {
            number = Integer.parseInt(prereq.getOperand());
        } catch (NumberFormatException exceptn) {
            throw new PrerequisiteException(PropertyFactory.getFormattedString("PreTemplate.error", prereq.toString()));
        }
        if (!character.getTemplateList().isEmpty()) {
            String templateName = prereq.getKey().toUpperCase();
            final int wildCard = templateName.indexOf('%');
            if (wildCard >= 0) {
                templateName = templateName.substring(0, wildCard);
                for (Iterator ti = character.getTemplateList().iterator(); ti.hasNext(); ) {
                    final PCTemplate aTemplate = (PCTemplate) ti.next();
                    if (aTemplate.getName().toUpperCase().startsWith(templateName)) {
                        runningTotal++;
                    }
                }
            } else if (character.getTemplateNamed(templateName) != null) {
                runningTotal++;
            }
        }
        runningTotal = prereq.getOperator().compare(runningTotal, number);
        return countedTotal(prereq, runningTotal);
    }

    public String kindHandled() {
        return "TEMPLATE";
    }
}
