package pcgen.core.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pcgen.cdom.enumeration.AssociationKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.core.SubstitutionClass;
import pcgen.core.prereq.PrereqHandler;
import pcgen.util.chooser.ChooserFactory;
import pcgen.util.chooser.ChooserInterface;

public class SubstitutionClassApplication {

    public static void checkForSubstitutionClass(PCClass cl, final int aLevel, final PlayerCharacter aPC) {
        List<SubstitutionClass> substitutionClassList = cl.getListFor(ListKey.SUBSTITUTION_CLASS);
        if (substitutionClassList == null || substitutionClassList.isEmpty()) {
            return;
        }
        List<String> columnNames = new ArrayList<String>(1);
        columnNames.add("Name");
        List<PCClass> choiceList = new ArrayList<PCClass>();
        buildSubstitutionClassChoiceList(cl, choiceList, aPC.getLevel(cl), aPC);
        if (choiceList.size() <= 1) {
            return;
        }
        final ChooserInterface c = ChooserFactory.getChooserInstance();
        c.setTitle("Substitution Levels");
        c.setMessageText("Choose one of the listed substitution levels " + "or the base class(top entry).  " + "Pressing Close will take the standard class level.");
        c.setTotalChoicesAvail(1);
        c.setPoolFlag(false);
        c.setAvailableColumnNames(columnNames);
        c.setAvailableList(choiceList);
        c.setVisible(true);
        List<PCClass> selectedList = c.getSelectedList();
        PCClass selected = null;
        if (!selectedList.isEmpty()) {
            selected = selectedList.get(0);
        }
        if ((!selectedList.isEmpty()) && selected instanceof SubstitutionClass) {
            SubstitutionClass sc = (SubstitutionClass) selected;
            SubstitutionLevelSupport.applyLevelArrayModsToLevel(sc, cl, aLevel, aPC);
            aPC.setAssoc(aPC.getActiveClassLevel(cl, aLevel), AssociationKey.SUBSTITUTIONCLASS_KEY, sc.getKeyName());
            return;
        } else {
            aPC.removeAssoc(aPC.getActiveClassLevel(cl, aLevel), AssociationKey.SUBSTITUTIONCLASS_KEY);
            return;
        }
    }

    /**
	 * Build a list of Substitution Classes for the user to choose from. The
	 * list passed in will be populated.
	 * 
	 * @param choiceNames
	 *            The list of substitution classes to choose from.
	 * @param level
	 *            The class level to determine the choices for
	 * @param aPC
	 */
    private static void buildSubstitutionClassChoiceList(PCClass cl, final List<PCClass> choiceList, final int level, final PlayerCharacter aPC) {
        for (SubstitutionClass sc : cl.getListFor(ListKey.SUBSTITUTION_CLASS)) {
            if (!PrereqHandler.passesAll(sc.getPrerequisiteList(), aPC, cl)) {
                continue;
            }
            if (!sc.hasOriginalClassLevel(level)) {
                continue;
            }
            if (!SubstitutionLevelSupport.qualifiesForSubstitutionLevel(cl, sc, aPC, level)) {
                continue;
            }
            choiceList.add(sc);
        }
        Collections.sort(choiceList);
        choiceList.add(0, cl);
    }
}
