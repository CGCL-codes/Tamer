package pcgen.core.prereq;

import pcgen.AbstractCharacterTestCase;
import pcgen.core.Equipment;
import pcgen.core.PlayerCharacter;

/**
 */
public class PreEquipTest extends AbstractCharacterTestCase {

    public void testPassesPrerequisitePlayerCharacter() {
        final PlayerCharacter character = getCharacter();
        final Equipment longsword = new Equipment();
        longsword.setName("Longsword");
        character.addEquipment(longsword);
        longsword.setIsEquipped(true, character);
        final Prerequisite prereq = new Prerequisite();
        prereq.setKind("equip");
        prereq.setKey("LONGSWORD%");
        prereq.setOperand("1");
        prereq.setOperator(PrerequisiteOperator.EQ);
        final boolean passes = PrereqHandler.passes(prereq, character, null);
        assertTrue(passes);
    }
}
