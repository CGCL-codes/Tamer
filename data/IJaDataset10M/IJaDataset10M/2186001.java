package plugin.pretokens.test;

import pcgen.core.Equipment;
import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.PreEquippedTester;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteException;
import pcgen.core.prereq.PrerequisiteTest;

/**
 * @author wardc
 *
 */
public class PreEquipPrimaryTester extends PreEquippedTester implements PrerequisiteTest {

    public int passes(final Prerequisite prereq, final PlayerCharacter character) throws PrerequisiteException {
        return passesPreEquipHandleTokens(prereq, character, Equipment.EQUIPPED_PRIMARY);
    }

    public String kindHandled() {
        return "EQUIPPRIMARY";
    }
}
