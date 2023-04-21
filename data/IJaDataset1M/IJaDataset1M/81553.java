package org.jcrpg.world.ai.profession.adventurer;

import org.jcrpg.world.ai.abs.attribute.FantasyAttributes;
import org.jcrpg.world.ai.abs.skill.mental.Languages;
import org.jcrpg.world.ai.abs.skill.social.Chatter;
import org.jcrpg.world.ai.profession.HumanoidProfessional;

public class Jester extends HumanoidProfessional {

    public Jester() {
        super();
        attrMinLevels.minimumLevels.put(FantasyAttributes.CHARISMA, 14);
        attrMinLevels.minimumLevels.put(FantasyAttributes.PIETY, 12);
        attrMinLevels.minimumLevels.put(FantasyAttributes.PSYCHE, 10);
        addMajorSkill(Chatter.class);
        addMajorSkill(Languages.class);
    }
}
