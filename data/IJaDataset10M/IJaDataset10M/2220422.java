package com.ham.mud.commands.spells.heal;

/**
 * Created by hlucas on Jul 6, 2011 at 10:51:39 AM
 */
public class CureCriticalSpell extends HealingSpell {

    @Override
    protected double getHealAmount() {
        return 180;
    }

    @Override
    public int getManaCost() {
        return 60;
    }

    @Override
    public String getName() {
        return "Cure Critical";
    }
}
