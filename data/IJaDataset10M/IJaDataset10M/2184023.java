package pcgen.core.analysis;

import org.junit.Test;
import pcgen.AbstractCharacterTestCase;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.SkillArmorCheck;
import pcgen.cdom.enumeration.StringKey;
import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.core.Globals;
import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.core.Race;
import pcgen.core.Skill;
import pcgen.core.bonus.Bonus;
import pcgen.core.bonus.BonusObj;
import pcgen.util.TestHelper;

/**
 * The Class <code>SkillModifierTest</code> is responsible for checking that the 
 * SkillModifier class is operating correctly.
 * 
 * Last Editor: $Author: $
 * Last Edited: $Date:  $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision:  $
 */
public class SkillModifierTest extends AbstractCharacterTestCase {

    PCClass pcClass;

    Race emptyRace = new Race();

    boolean firstTime = true;

    Ability skillFocus = new Ability();

    Ability persuasive = new Ability();

    Skill bluff;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if (firstTime) {
            firstTime = false;
            pcClass = new PCClass();
            TestHelper.makeSkill("Bluff", "Charisma", "CHA", true, SkillArmorCheck.NONE);
            TestHelper.makeSkill("Listen", "Wisdom", "WIS", true, SkillArmorCheck.NONE);
            skillFocus = TestHelper.makeAbility("Skill Focus", AbilityCategory.FEAT, "General");
            BonusObj aBonus = Bonus.newBonus("SKILL|LIST|3");
            if (aBonus != null) {
                aBonus.setCreatorObject(skillFocus);
                skillFocus.addToListFor(ListKey.BONUS, aBonus);
            }
            skillFocus.put(ObjectKey.MULTIPLE_ALLOWED, true);
            skillFocus.put(StringKey.CHOICE_STRING, "SKILLSNAMED|TYPE.Strength|TYPE.Dexterity|TYPE.Constitution|TYPE.Intelligence|TYPE.Wisdom|TYPE.Charisma");
            persuasive = TestHelper.makeAbility("Persuasive", AbilityCategory.FEAT, "General");
            aBonus = Bonus.newBonus("SKILL|KEY_Bluff,KEY_Listen|2");
            if (aBonus != null) {
                aBonus.setCreatorObject(persuasive);
                persuasive.addToListFor(ListKey.BONUS, aBonus);
            }
            persuasive.put(ObjectKey.MULTIPLE_ALLOWED, false);
        }
        final PlayerCharacter character = getCharacter();
        character.incrementClassLevel(1, pcClass);
    }

    @Override
    protected void tearDown() throws Exception {
        pcClass = null;
        super.tearDown();
    }

    /**
	 * Test getModifierExplanation for both lists and multiple 
	 * bonus feats.
	 */
    @Test
    public void testGetModifierExplanation() {
        bluff = Globals.getContext().ref.silentlyGetConstructedCDOMObject(Skill.class, "KEY_bluff");
        PlayerCharacter pc = getCharacter();
        assertEquals("Initial state", "", SkillModifier.getModifierExplanation(bluff, pc, false));
        pc.addAssociation(skillFocus, "KEY_Bluff");
        pc.addAbility(AbilityCategory.FEAT, skillFocus, null);
        assertEquals("Bonus after skill focus", "+3[Skill Focus]", SkillModifier.getModifierExplanation(bluff, pc, false));
        pc.addAbility(AbilityCategory.FEAT, persuasive, null);
        assertEquals("Bonus after persuasive", "+2[Persuasive] +3[Skill Focus]", SkillModifier.getModifierExplanation(bluff, pc, false));
    }
}
