package neon.core.handlers;

import neon.entities.creatures.Creature;

public interface CombatListener {

    public void fight(Creature attacker, Creature defender);
}
