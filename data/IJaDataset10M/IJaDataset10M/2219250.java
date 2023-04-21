package drcube.views.graphics2D;

import drcube.model.Rotation;
import drcube.sys.Configuration;
import drcube.views.CanvasCubeView;

/**
 * Abstract Class to control animations on a Cube-Object.
 * MAX_ANGLE / STEPPING determines the frames rendered within
 * a second.
 * 
 * @author osaft
 *
 */
public abstract class AbstractRotator implements Runnable {

    /**
	 * The maximum angle a rotation can do.
	 **
	 * DO NOT CHANGE!
	 **
	 * In this implementation a change in this variable can cause
	 * serious problems due to the use of a pre-calculated
	 * divider-array. Kinda messy.
	 * TODO: Think of another method to get all dividers from
	 * 		 the given ANGLE or redesign the calculation of
	 * 	     steppings.
	 */
    public static final int ANGLE = 90;

    /**
	 * The stepping between frames in degrees.
	 */
    public static int STEPPING = 15;

    /**
	 * Desired FPS to use.
	 */
    private static int FPS = 500;

    protected Cube pCube;

    protected Rotation rotation;

    protected CanvasCubeView parent;

    private boolean benchmark = true;

    private static final int[] dividers = new int[] { 1, 2, 3, 5, 6, 9, 10, 15, 18, 30, 45 };

    /**
	 * Initiates a Rotator with the desired default Frames Per Second (FPS).
	 */
    public AbstractRotator() {
        int fps = getFPS();
        if (fps != -1) {
            FPS = fps;
        }
    }

    private int getFPS() {
        int fps = -1;
        if (Configuration.getInstance().getOption("all.animation").equals("highFPS")) {
            fps = 200;
        }
        if (Configuration.getInstance().getOption("all.animation").equals("standardFPS")) {
            fps = 400;
        }
        if (Configuration.getInstance().getOption("all.animation").equals("lowFPS")) {
            fps = 700;
        }
        return fps;
    }

    public boolean adjust() {
        if (benchmark) {
            try {
                long start = System.currentTimeMillis();
                run();
                long diff = System.currentTimeMillis() - start + 1;
                int cFPS = (int) (ANGLE / STEPPING * FPS / diff);
                STEPPING = binsearch(ANGLE / cFPS);
            } catch (ArithmeticException e) {
                STEPPING = 45;
            }
        }
        return benchmark;
    }

    public void run() {
    }

    ;

    /**
	 * Performs a binary search on the dividers array for the given number.
	 * 
	 * @param num the number to find.
	 * @return hopefully the number.
	 */
    private int binsearch(int num) {
        int left = 0;
        int right = dividers.length - 1;
        int index = right - left / 2;
        while (left < right) {
            index = (right + left) / 2;
            if (num == dividers[index]) {
                return num;
            } else if (num > dividers[index]) {
                left = index + 1;
            } else if (num < dividers[index]) {
                right = index - 1;
            }
        }
        return dividers[left];
    }
}
