package org.jcrpg.world.ai.profession.adventurer;

import org.jcrpg.world.ai.abs.attribute.FantasyAttributes;
import org.jcrpg.world.ai.abs.skill.magical.Elementarism;
import org.jcrpg.world.ai.abs.skill.magical.Mentalism;
import org.jcrpg.world.ai.abs.skill.mental.MagicalLore;
import org.jcrpg.world.ai.profession.HumanoidProfessional;
import org.jcrpg.world.object.combat.blade.Dagger;

public class Psionic extends HumanoidProfessional {

    public Psionic() {
        super();
        generationNewInstanceObjects.add(Dagger.class);
        attrMinLevels.minimumLevels.put(FantasyAttributes.PSYCHE, 13);
        attrMinLevels.minimumLevels.put(FantasyAttributes.CONCENTRATION, 13);
        addMajorSkill(Mentalism.class);
        addMinorSkill(Elementarism.class);
        addMajorSkill(MagicalLore.class);
    }
}
