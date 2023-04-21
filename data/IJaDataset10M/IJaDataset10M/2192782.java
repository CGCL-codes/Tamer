package org.dyn4j.testbed.test;

import org.dyn4j.collision.Bounds;
import org.dyn4j.collision.RectangularBounds;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Segment;
import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.testbed.ContactCounter;
import org.dyn4j.testbed.Entity;
import org.dyn4j.testbed.Test;

/**
 * Tests circle and polygon shapes in collision deteciton and resolution.
 * @author William Bittle
 * @version 2.0.0
 * @since 1.0.0
 */
public class Shapes extends Test {

    @Override
    public String getName() {
        return "Shapes";
    }

    @Override
    public String getDescription() {
        return "Tests the various shapes supported.  This test ensures that all " + "shapes supported are caught by collision detection and resolved accordingly.";
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
        this.world.add(floor);
        Triangle triShape = new Triangle(new Vector2(0.0, 0.5), new Vector2(-0.5, -0.5), new Vector2(0.5, -0.5));
        Entity triangle = new Entity();
        triangle.addFixture(new BodyFixture(triShape));
        triangle.setMass();
        triangle.translate(-1.0, 2.0);
        triangle.getVelocity().set(5.0, 0.0);
        this.world.add(triangle);
        Circle cirShape = new Circle(0.5);
        Entity circle = new Entity();
        circle.addFixture(new BodyFixture(cirShape));
        circle.setMass();
        circle.translate(2.0, 2.0);
        circle.apply(new Vector2(-100.0, 0.0));
        circle.setLinearDamping(0.05);
        this.world.add(circle);
        Segment segShape = new Segment(new Vector2(0.5, 0.5), new Vector2(-0.5, -0.5));
        Entity segment1 = new Entity();
        segment1.addFixture(new BodyFixture(segShape));
        segment1.setMass();
        segment1.translate(1.0, 6.0);
        this.world.add(segment1);
        Entity segment2 = new Entity();
        segment2.addFixture(new BodyFixture(segShape));
        segment2.setMass();
        segment2.rotateAboutCenter(Math.toRadians(-45.0));
        segment2.translate(-4.5, 1.0);
        this.world.add(segment2);
        Rectangle rectShape = new Rectangle(1.0, 1.0);
        Entity rectangle = new Entity();
        rectangle.addFixture(new BodyFixture(rectShape));
        rectangle.setMass();
        rectangle.translate(0.0, 2.0);
        rectangle.getVelocity().set(-5.0, 0.0);
        this.world.add(rectangle);
        Polygon polyShape = Geometry.createUnitCirclePolygon(10, 1.0);
        Entity polygon = new Entity();
        polygon.addFixture(new BodyFixture(polyShape));
        polygon.setMass();
        polygon.translate(-2.5, 2.0);
        polygon.setAngularVelocity(Math.toRadians(-20.0));
        this.world.add(polygon);
        Circle c1 = new Circle(0.5);
        BodyFixture c1Fixture = new BodyFixture(c1);
        c1Fixture.setDensity(0.5);
        Circle c2 = new Circle(0.5);
        BodyFixture c2Fixture = new BodyFixture(c2);
        c2Fixture.setDensity(0.5);
        Rectangle rm = new Rectangle(2.0, 1.0);
        c1.translate(-1.0, 0.0);
        c2.translate(1.0, 0.0);
        Entity capsule = new Entity();
        capsule.addFixture(c1Fixture);
        capsule.addFixture(c2Fixture);
        capsule.addFixture(new BodyFixture(rm));
        capsule.setMass();
        capsule.translate(0.0, 4.0);
        this.world.add(capsule);
        Entity issTri = new Entity();
        issTri.addFixture(Geometry.createIsoscelesTriangle(1.0, 3.0));
        issTri.setMass();
        issTri.translate(2.0, 3.0);
        this.world.add(issTri);
        Entity equTri = new Entity();
        equTri.addFixture(Geometry.createEquilateralTriangle(2.0));
        equTri.setMass();
        equTri.translate(3.0, 3.0);
        this.world.add(equTri);
        Entity rightTri = new Entity();
        rightTri.addFixture(Geometry.createRightTriangle(2.0, 1.0));
        rightTri.setMass();
        rightTri.translate(4.0, 3.0);
        this.world.add(rightTri);
    }

    @Override
    public void home() {
        this.scale = 64.0;
        this.offset.set(0.0, -2.0);
    }
}
