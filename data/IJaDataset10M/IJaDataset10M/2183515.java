package org.activision.io.scripts.items;

import org.activision.model.player.Player;
import org.activision.model.player.Skills;
import org.activision.io.scripts.itemScript;

public class i147 extends itemScript {

    @Override
    public void option1(Player p, int itemId, int interfaceId, int slot) {
        if (p.getInventory().getContainer().get(slot) == null) return;
        if (p.getInventory().getContainer().get(slot).getId() != itemId) return;
        if (interfaceId != 149) return;
        if (System.currentTimeMillis() - p.getCombatDefinitions().getLastPot() < 0) return;
        p.getInventory().deleteItem(147, 1);
        p.getInventory().addItem(149, 1);
        p.getSkills().set(Skills.ATTACK, p.getSkills().getLevelForXp(Skills.ATTACK) + 5 + Math.round(p.getSkills().getLevelForXp(Skills.ATTACK) * 15 / 100));
        p.animate(829);
        p.getCombatDefinitions().setLastPot(System.currentTimeMillis() + 1800);
        p.getCombatDefinitions().setLastFood(System.currentTimeMillis() + 1800);
        p.getCombat().delay += 3;
    }
}
