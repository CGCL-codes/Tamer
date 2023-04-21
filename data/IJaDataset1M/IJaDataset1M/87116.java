package org.dyn4j.testbed.test;

import org.dyn4j.collision.Bounds;
import org.dyn4j.collision.RectangularBounds;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.PrismaticJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.testbed.ContactCounter;
import org.dyn4j.testbed.Entity;
import org.dyn4j.testbed.Test;

/**
 * Tests the prismatic joint.
 * @author William Bittle
 * @version 2.2.2
 * @since 1.0.0
 */
public class Prismatic extends Test {

    @Override
    public String getName() {
        return "Prismatic";
    }

    @Override
    public String getDescription() {
        return "Tests a prismatic joint.";
    }

    @Override
    public void initialize() {
        super.initialize();
        this.home();
        Bounds bounds = new RectangularBounds(Geometry.createRectangle(16.0, 15.0));
        this.world = new World(bounds);
        ContactCounter cc = new ContactCounter();
        this.world.addListener(cc);
        this.setup();
    }

    @Override
    protected void setup() {
        Rectangle floorRect = new Rectangle(15.0, 1.0);
        Entity floor = new Entity();
        floor.addFixture(new BodyFixture(floorRect));
        floor.setMass(Mass.Type.INFINITE);
        floor.translate(0.0, -4.0);
        this.world.add(floor);
        Rectangle r = new Rectangle(0.5, 1.0);
        Entity top = new Entity();
        top.addFixture(new BodyFixture(r));
        top.setMass();
        top.translate(0.0, -1.5);
        top.getVelocity().set(2.0, 0.0);
        Entity bot = new Entity();
        bot.addFixture(new BodyFixture(r));
        bot.setMass();
        bot.translate(0.0, -0.5);
        this.world.add(top);
        this.world.add(bot);
        PrismaticJoint joint = new PrismaticJoint(bot, top, new Vector2(0.0, 2.0), new Vector2(0.0, 1.0));
        joint.setLimitsEnabled(0.5, 1.5);
        joint.setCollisionAllowed(true);
        this.world.add(joint);
    }

    @Override
    public void home() {
        this.scale = 64.0;
        this.offset.set(0.0, 2.0);
    }
}
