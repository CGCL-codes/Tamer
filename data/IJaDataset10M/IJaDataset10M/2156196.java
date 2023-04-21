package org.thenesis.planetino2.ai.pattern;

import java.util.Enumeration;
import java.util.Vector;
import org.thenesis.planetino2.bsp2D.BSPTree;
import org.thenesis.planetino2.game.GameObject;
import org.thenesis.planetino2.math3D.Vector3D;
import org.thenesis.planetino2.util.MoreMath;

/**
    An "dodge" pattern that puts the bot in a random location
    in a half-circle around the player.
*/
public class DodgePatternRandom extends AIPattern {

    private float radiusSq;

    public DodgePatternRandom(BSPTree tree) {
        this(tree, 500);
    }

    public DodgePatternRandom(BSPTree tree, float radius) {
        super(tree);
        this.radiusSq = radius * radius;
    }

    public Enumeration find(GameObject bot, GameObject player) {
        Vector3D goal = getLocationFromPlayer(bot, player, radiusSq);
        float maxAngle = (float) Math.PI / 2;
        float angle = MoreMath.random(-maxAngle, maxAngle);
        goal.subtract(player.getLocation());
        goal.rotateY(angle);
        goal.add(player.getLocation());
        calcFloorHeight(goal, bot.getFloorHeight());
        Vector v = new Vector();
        v.addElement(goal);
        return v.elements();
    }
}
