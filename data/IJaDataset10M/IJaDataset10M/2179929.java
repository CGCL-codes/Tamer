package org.openaion.gameserver.skill;

import org.openaion.gameserver.dataholders.DataManager;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.templates.item.ItemTemplate;
import org.openaion.gameserver.skill.model.ActivationAttribute;
import org.openaion.gameserver.skill.model.Skill;
import org.openaion.gameserver.skill.model.SkillTemplate;

/**
 * @author ATracer
 *
 */
public class SkillEngine {

    public static final SkillEngine skillEngine = new SkillEngine();

    /**
	 * should not be instantiated directly
	 */
    private SkillEngine() {
    }

    /**
	 *  This method is used for skills that were learned by player
	 *  
	 * @param player
	 * @param skillId
	 * @return Skill
	 */
    public Skill getSkillFor(Player player, int skillId, VisibleObject firstTarget) {
        SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);
        if (template == null) return null;
        if (template.getActivationAttribute() != ActivationAttribute.PROVOKED) {
            if (!player.getSkillList().isSkillPresent(skillId)) return null;
        }
        Creature target = null;
        if (firstTarget instanceof Creature) target = (Creature) firstTarget;
        return new Skill(template, player, target);
    }

    /**
	 *  This method is used for not learned skills (item skills etc)
	 *  
	 * @param creature
	 * @param skillId
	 * @param skillLevel
	 * @return Skill
	 */
    public Skill getSkill(Creature creature, int skillId, int skillLevel, VisibleObject firstTarget) {
        return this.getSkill(creature, skillId, skillLevel, firstTarget, null);
    }

    public Skill getSkill(Creature creature, int skillId, int skillLevel, VisibleObject firstTarget, ItemTemplate itemTemplate) {
        SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(skillId);
        if (template == null) return null;
        Creature target = null;
        if (firstTarget instanceof Creature) target = (Creature) firstTarget;
        return new Skill(template, creature, skillLevel, target, itemTemplate);
    }

    public static SkillEngine getInstance() {
        return skillEngine;
    }
}
