package org.jcrpg.world.ai.abs.state.effect;

import org.jcrpg.game.logic.ImpactUnit;
import org.jcrpg.world.ai.abs.attribute.Attributes;
import org.jcrpg.world.ai.abs.attribute.FantasyResistances;
import org.jcrpg.world.ai.abs.attribute.Resistances;
import org.jcrpg.world.ai.abs.skill.SkillActForm;
import org.jcrpg.world.ai.abs.state.StateEffect;

public class ElementalResistance extends StateEffect {

    public ElementalResistance() {
    }

    @Override
    public boolean updateBeingAttacked() {
        return false;
    }

    @Override
    public boolean canDoActForm(SkillActForm form) {
        return true;
    }

    @Override
    public ImpactUnit impactForTime() {
        return null;
    }

    @Override
    public ImpactUnit impactForTurn() {
        return null;
    }

    @Override
    public String getIcon() {
        return "elemental_resistance.png";
    }

    @Override
    public Attributes getBaseAttributes() {
        return null;
    }

    static Resistances resis = new FantasyResistances();

    static {
        resis.setResistance(FantasyResistances.RESIST_COLD, 10);
        resis.setResistance(FantasyResistances.RESIST_HEAT, 10);
        resis.setResistance(FantasyResistances.RESIST_CHEMICAL, 10);
    }

    @Override
    public Resistances getBaseResistances() {
        return resis;
    }

    @Override
    public boolean canDoUse() {
        return true;
    }
}
