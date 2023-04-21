package plugin.pretokens.test;

import pcgen.core.GameMode;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.SettingsHandler;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteTest;
import pcgen.util.PropertyFactory;

/**
 * @author wardc
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PreDeityAlignTester extends AbstractPrerequisiteTest implements PrerequisiteTest {

    public int passes(final Prerequisite prereq, final PlayerCharacter character) {
        int runningTotal = 0;
        if (Globals.getGameModeAlignmentText().length() == 0) {
            runningTotal = 1;
        } else {
            final GameMode gm = SettingsHandler.getGame();
            final String[] aligns = gm.getAlignmentListStrings(false);
            String deityAlign = "";
            if (character.getDeity() != null) {
                try {
                    final int align = Integer.parseInt(character.getDeity().getAlignment());
                    deityAlign = aligns[align];
                } catch (NumberFormatException e) {
                    deityAlign = character.getDeity().getAlignment();
                }
            }
            String desiredAlign = prereq.getOperand();
            try {
                final int align = Integer.parseInt(prereq.getOperand());
                desiredAlign = aligns[align];
            } catch (NumberFormatException e) {
            }
            if (desiredAlign.equalsIgnoreCase(deityAlign)) {
                runningTotal = 1;
            }
        }
        return countedTotal(prereq, runningTotal);
    }

    public String kindHandled() {
        return "DEITYALIGN";
    }

    public String toHtmlString(final Prerequisite prereq) {
        return PropertyFactory.getFormattedString("PreDeityAlign.toHtml", prereq.getOperator().toDisplayString(), SettingsHandler.getGame().getShortAlignmentAtIndex(Integer.parseInt(prereq.getKey())));
    }
}
