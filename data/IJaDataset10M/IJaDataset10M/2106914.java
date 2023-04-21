package fr.fg.server.events.impl;

import fr.fg.server.data.Fleet;
import fr.fg.server.events.GameEvent;

public class DodgeEvent extends GameEvent {

    private Fleet attacker;

    private Fleet defender;

    private int attackerSlot;

    private int defenderSlot;

    public DodgeEvent(Fleet attacker, Fleet defender, int attackerSlot, int defenderSlot) {
        super();
        this.attacker = attacker;
        this.defender = defender;
        this.attackerSlot = attackerSlot;
        this.defenderSlot = defenderSlot;
    }

    public Fleet getAttacker() {
        return attacker;
    }

    public Fleet getDefender() {
        return defender;
    }

    public int getAttackerSlot() {
        return attackerSlot;
    }

    public int getDefenderSlot() {
        return defenderSlot;
    }
}
