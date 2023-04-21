package plugin.exporttokens;

import junit.framework.Test;
import junit.framework.TestSuite;
import pcgen.AbstractCharacterTestCase;
import pcgen.core.Equipment;
import pcgen.core.EquipmentList;
import pcgen.core.EquipmentModifier;
import pcgen.core.GameMode;
import pcgen.core.Globals;
import pcgen.core.PCClass;
import pcgen.core.PCStat;
import pcgen.core.PlayerCharacter;
import pcgen.core.SettingsHandler;
import pcgen.core.bonus.Bonus;
import pcgen.core.bonus.BonusObj;
import pcgen.core.character.EquipSet;
import pcgen.io.exporttoken.AttackToken;

/**
 * <code>ACTokenTest</code> tests the function of the AC token and 
 * thus the calculations of armor class.  
 *
 * Last Editor: $Author: jdempsey $
 * Last Edited: $Date: 2007-03-29 18:27:19 -0400 (Thu, 29 Mar 2007) $
 *
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision: 2611 $
 */
public class AttackTokenTest extends AbstractCharacterTestCase {

    PCClass myClass = new PCClass();

    /**
	 * Quick test suite creation - adds all methods beginning with "test"
	 * @return The Test suite
	 */
    public static Test suite() {
        return new TestSuite(AttackTokenTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        PlayerCharacter character = getCharacter();
        setPCStat(character, "STR", 14);
        PCStat stat = character.getStatList().getStatAt(SettingsHandler.getGame().getStatFromAbbrev("STR"));
        stat.getBonusList().clear();
        stat.addBonusList("COMBAT|TOHIT.Melee|STR|TYPE=Ability");
        EquipSet def = new EquipSet("0.1", "Default");
        character.addEquipSet(def);
        character.setCalcEquipmentList();
        character.calcActiveBonuses();
        myClass.setName("My Class");
        myClass.setAbbrev("Myc");
        myClass.setSkillPointFormula("3");
        final BonusObj babClassBonus = Bonus.newBonus("1|COMBAT|BAB|CL+5");
        myClass.addBonusList(babClassBonus);
        Globals.getClassList().add(myClass);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test the character's attack calcs with no bonus.
	 * @throws Exception
	 */
    public void testBase() throws Exception {
        assertEquals("Total melee attack no bonus", "+2", new AttackToken().getToken("ATTACK.MELEE.TOTAL", getCharacter(), null));
        assertEquals("Total melee attack no bonus short", "+2", new AttackToken().getToken("ATTACK.MELEE.TOTAL.SHORT", getCharacter(), null));
    }

    /**
	 * Test the character's attack calcs with a bonus.
	 * @throws Exception
	 */
    public void testIterative() throws Exception {
        getCharacter().incrementClassLevel(1, myClass, true);
        getCharacter().calcActiveBonuses();
        assertEquals("Total melee attack no bonus", "+8/+3", new AttackToken().getToken("ATTACK.MELEE.TOTAL", getCharacter(), null));
        assertEquals("Total melee attack no bonus short", "+8", new AttackToken().getToken("ATTACK.MELEE.TOTAL.SHORT", getCharacter(), null));
    }
}
