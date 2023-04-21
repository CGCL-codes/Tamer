package se.liu.johfa428.game.objects.intro;

import java.util.Random;
import se.liu.oschi129.animation.Animation;
import se.liu.oschi129.world.model.World;
import se.liu.oschi129.world.objects.WorldMovableObject;

public class ObjectCloud extends WorldMovableObject {

    double yMin, yMax, direction, speed;

    /**
	 * Creates an Object that moves from left to right or right to left with a constant speed and randomly 
	 * generates a starting y cordinate and direction when moved out from the screen.  
	 * @param yMax
	 * Maximum value of the random y cordinate
	 * @param yMin
	 * Minimum value of the random y cordinate.
	 * @param xStart
	 * The starting X cordinate
	 * @param animation
	 * The animated object
	 * @param width
	 * @param height
	 * @param speed
	 * The speed for the object
	 */
    public ObjectCloud(double yMax, double yMin, double xStart, Animation animation, double width, double height, double speed) {
        super(Type.SOFT, animation, true, xStart, 0, width, height);
        this.yMin = yMin;
        this.yMax = yMax;
        this.speed = speed;
        this.direction = 0;
        randomizeY();
        randomizeDirectrion();
        if (xStart == 0) randomizeXStart();
    }

    public void randomizeY() {
        Random random = new Random();
        double range = this.yMax - this.yMin;
        double newY = yMin + (Math.abs(random.nextInt()) % range);
        this.setY(newY);
    }

    public void randomizeXStart() {
        Random random = new Random();
        double range = 121 * 2 + 320;
        double newX = (Math.abs(random.nextInt()) % range) - getWidth();
        this.setX(newX);
    }

    public void randomizeDirectrion() {
        Random random = new Random();
        boolean dir = random.nextBoolean();
        if (dir) this.direction = -this.speed; else this.direction = this.speed;
    }

    @Override
    public void performStepAction(World world) {
        this.setX(getX() + this.direction);
        if (this.getX() < -121 || this.getX() > 30 + 320) {
            randomizeDirectrion();
            randomizeY();
            if (this.direction < 0) this.setX(320 + 121); else this.setX(-121);
        }
    }
}
