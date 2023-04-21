package com.ham.mud.commands.spells.damage;

import com.ham.mud.characters.MudCharacter;
import com.ham.mud.characters.player.Player;

/**
 * Created by hlucas on Jul 6, 2011 at 10:51:39 AM
 */
public class HarmSpell extends DamagingSpell {

    @Override
    protected String getPlayerText(MudCharacter target, double damage) {
        return "Your God smites " + target.getName() + " for " + damage + " damage.";
    }

    @Override
    protected String getOtherPlayerText(String name) {
        return name + " prays to their God, casting a damaging spell.";
    }

    @Override
    protected double getDamageAmount(Player player) {
        return 350;
    }

    @Override
    public int getManaCost() {
        return 100;
    }

    @Override
    public String getName() {
        return "Harm";
    }
}
