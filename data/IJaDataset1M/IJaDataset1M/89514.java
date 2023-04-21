package engine.test;

import engine.World;
import engine.joint.BasicJoint;
import engine.shapes.Body;
import engine.shapes.Box;
import engine.shapes.StaticBody;
import engine.vector.Vector;

/**
 * Dominos demo
 * 
 * @author Jeffery D. Ahern
 * @author Keith Kowalski
 * @author Matt DePorter
 * @author Kevin Mcomber
 */
public class Demo06 extends AbstractDemo {

    /** The block to move */
    private Body ball;

    /**
	 * Create a simple demo
	 */
    public Demo06() {
        super("Demo 6 - Stuff! - hit space");
    }

    /**
	 * @see engine.test.AbstractDemo#keyHit(char)
	 */
    protected void keyHit(char c) {
        if (c == ' ') {
            if (ball.getVelocity().length() == 0) {
                ball.addForce(new Vector(-2000000, 0));
            }
        }
    }

    /**
	 * @see engine.test.AbstractDemo#init(engine.World)
	 */
    protected void init(World world) {
        this.world = world;
        Body body;
        BasicJoint j;
        Body base = new StaticBody("Ground1", new Box(500.0f, 20.0f));
        base.setPosition(250.0f, 400);
        world.add(base);
        body = new StaticBody("Ground2", new Box(250.0f, 20.0f));
        body.setPosition(225.0f, 200);
        body.setFriction(3.0f);
        world.add(body);
        body = new StaticBody("Pen1", new Box(20.0f, 20.0f));
        body.setPosition(70.0f, 100);
        world.add(body);
        ball = new Body("Ball", new Box(10.0f, 10.0f), 1000);
        ball.setPosition(70.0f, 170);
        world.add(ball);
        j = new BasicJoint(body, ball, new Vector(70, 110));
        world.add(j);
        for (int i = 0; i < 8; i++) {
            body = new Body("Domino " + i, new Box(10.0f, 40.0f), 10 - i);
            body.setPosition(120.0f + (i * 30), 170);
            world.add(body);
        }
        body = new StaticBody("Ground2", new Box(200.0f, 10.0f));
        body.setPosition(345.0f, 270);
        body.setRotation(-0.6f);
        body.setFriction(0);
        world.add(body);
        body = new Body("Teete", new Box(250.0f, 5.0f), 10);
        body.setPosition(250.0f, 360);
        world.add(body);
        j = new BasicJoint(body, base, new Vector(250, 360));
        world.add(j);
        body = new Body("Turner", new Box(40.0f, 40.0f), 0.1f);
        body.setPosition(390.0f, 330);
        body.setFriction(0f);
        world.add(body);
        j = new BasicJoint(base, body, new Vector(390, 335));
        world.add(j);
        Body top = new Body("Top", new Box(40.0f, 5.0f), 0.01f);
        top.setPosition(390.0f, 307.5f);
        top.setFriction(0f);
        world.add(top);
        j = new BasicJoint(top, body, new Vector(410, 310));
        world.add(j);
    }

    /**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
    public static void main(String[] argv) {
        Demo06 demo = new Demo06();
        demo.start();
    }
}
