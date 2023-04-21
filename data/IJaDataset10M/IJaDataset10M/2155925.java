package client.spells;

import client.BattleManager;
import client.MagicColor;
import client.ManaCost;
import client.ManaPool;

public class HealingSalve extends Spell {

    public HealingSalve() {
        manaCost = new ManaPool();
        manaCost.setColor(MagicColor.WHITE, 1);
        description = "Heals for 3 HP";
    }

    @Override
    public boolean use(BattleManager battle) {
        boolean used = false;
        if (battle.getPlayer().getManaPool().comparePool(manaCost)) {
            battle.getPlayer().getActiveCreature().addCurrentHP(3);
            battle.getPlayer().getManaPool().subtractPool(manaCost);
            battle.incrementSpellsUsed();
            used = true;
        }
        return used;
    }
}
