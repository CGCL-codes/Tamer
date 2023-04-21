package com.ham.mud.commands.spells.heal;

/**
 * Created by hlucas on Jul 6, 2011 at 10:51:39 AM
 */
public class CureSeriousSpell extends HealingSpell {

    @Override
    protected double getHealAmount() {
        return 60;
    }

    @Override
    public int getManaCost() {
        return 15;
    }

    @Override
    public String getName() {
        return "Cure Serious";
    }
}
