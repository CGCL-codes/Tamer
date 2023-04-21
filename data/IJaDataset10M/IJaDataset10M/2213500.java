package net.phys2d.raw.test;

import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Line;

/**
 * Lines terrain testing extensive
 * 
 * @author Kevin Glass
 */
public class Demo18 extends AbstractDemo {

    /** The box falling into the simulation */
    private Body box;

    /**
	 * Create the demo
	 */
    public Demo18() {
        super("Phys2D Demo 18 - Lines Testing");
    }

    /**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
    protected void init(World world) {
        Body land = new StaticBody("Line1", new Line(100, 50));
        land.setPosition(150, 150);
        world.add(land);
        land = new StaticBody("Line2", new Line(150, -75));
        land.setPosition(250, 300);
        world.add(land);
        land = new StaticBody("Line2", new Line(150, 75));
        land.setPosition(100, 350);
        world.add(land);
        land = new StaticBody("Line2", new Line(150, 0));
        land.setPosition(300, 450);
        world.add(land);
        box = new Body("Faller", new Box(50, 50), 1);
        box.setPosition(200, 50);
        world.add(box);
    }

    /**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
    public static void main(String[] argv) {
        Demo18 demo = new Demo18();
        demo.start();
    }
}
