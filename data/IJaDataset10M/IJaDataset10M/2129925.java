package gameserver.services;

import gameserver.configs.CustomConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.PlayerClass;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.SkillList;
import gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import gameserver.skill.model.learn.SkillLearnTemplate;
import gameserver.utils.PacketSendUtility;

public class SkillLearnService {

    /**
	 * 
	 * @param player
	 * @param isNewCharacter
	 */
    public static void addNewSkills(Player player, boolean isNewCharacter) {
        int level = player.getCommonData().getLevel();
        PlayerClass playerClass = player.getCommonData().getPlayerClass();
        Race playerRace = player.getCommonData().getRace();
        if (isNewCharacter) {
            player.setSkillList(new SkillList());
        }
        addSkills(player, level, playerClass, playerRace, isNewCharacter);
    }

    /**
	 *  Recursively check missing skills and add them to player
	 *  
	 * @param player
	 */
    public static void addMissingSkills(Player player) {
        int level = player.getCommonData().getLevel();
        PlayerClass playerClass = player.getCommonData().getPlayerClass();
        Race playerRace = player.getCommonData().getRace();
        for (int i = 0; i <= level; i++) {
            addSkills(player, i, playerClass, playerRace, false);
        }
        if (!playerClass.isStartingClass()) {
            PlayerClass startinClass = PlayerClass.getStartingClassFor(playerClass);
            for (int i = 1; i < 10; i++) {
                addSkills(player, i, startinClass, playerRace, false);
            }
            if (player.getSkillList().getSkillEntry(30001) != null) {
                int skillLevel = player.getSkillList().getSkillLevel(30001);
                player.getSkillList().removeSkill(30001);
                PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player));
                player.getSkillList().addSkill(player, 30002, skillLevel, true);
                player.getSkillList().addSkill(player, 30003, 1, true);
                player.getSkillList().addSkill(player, 40009, 1, true);
                player.getRecipeList().autoLearnRecipe(player, 40009, 1);
            }
        }
    }

    /**
	 *  Adds skill to player according to the specified level, class and race
	 *  
	 * @param player
	 * @param level
	 * @param playerClass
	 * @param playerRace
	 * @param isNewCharacter
	 */
    private static void addSkills(Player player, int level, PlayerClass playerClass, Race playerRace, boolean isNewCharacter) {
        SkillLearnTemplate[] skillTemplates = DataManager.SKILL_TREE_DATA.getTemplatesFor(playerClass, level, playerRace);
        SkillList playerSkillList = player.getSkillList();
        for (SkillLearnTemplate template : skillTemplates) {
            if (!checkLearnIsPossible(playerSkillList, template)) continue;
            if (player.getCommonData().getLevel() <= 9 || template.getSkillId() != 30001 && player.getCommonData().getLevel() > 9) playerSkillList.addSkill(player, template.getSkillId(), template.getSkillLevel(), !isNewCharacter);
        }
    }

    /**
	 *  Check SKILL_AUTOLEARN property
	 *  Check skill already learned
	 *  Check skill template auto-learn attribute
	 *  
	 * @param playerSkillList
	 * @param template
	 * @return
	 */
    private static boolean checkLearnIsPossible(SkillList playerSkillList, SkillLearnTemplate template) {
        if (playerSkillList.isSkillPresent(template.getSkillId())) return true;
        if ((CustomConfig.SKILL_AUTOLEARN && !template.isStigma()) || (CustomConfig.STIGMA_AUTOLEARN && template.isStigma())) return true;
        if (template.isAutolearn()) return true;
        return false;
    }
}
