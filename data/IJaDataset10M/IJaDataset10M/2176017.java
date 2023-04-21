package neon.creatures.ai;

import neon.core.Engine;
import neon.creatures.Creature;
import java.awt.Point;

public class GuardAI extends AI {

    private int range;

    private Point home;

    public GuardAI(Creature creature, byte aggression, byte confidence, int range) {
        this.aggression = aggression;
        this.range = range;
        this.creature = creature;
        home = new Point(creature.getPosition());
    }

    public void act() {
        if (isHostile() && Engine.getPlayer().getPosition().distance(creature.getPosition()) > range) {
            if (creature.getHealth() / creature.getBaseHealth() < confidence) {
                if (Math.random() > 0.2 || !(cure() || heal())) {
                    flee(Engine.getPlayer());
                }
            } else {
                hunt(range, home, Engine.getPlayer());
            }
        } else {
            wander(range, home);
        }
    }
}
