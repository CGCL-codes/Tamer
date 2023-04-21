package ise.foosball;

import ise.math.Vector2D;
import ise.objects.Ball;
import ise.utilities.Timer;
import processing.core.PApplet;

public class FoosBall extends Ball {

    /**
	   * Creates a new Ball object.
	   *
	   * @param x The x value of the center.
	   * @param y The y value of the center.
	   * @param radius The radius.
	   * @param density The density.
	   */
    public FoosBall(PApplet p, Timer timer, float x, float y) {
        super(p, timer, x, y, 30, 1);
        color = p.color(0xf8, 0xab, 0x21);
    }
}
