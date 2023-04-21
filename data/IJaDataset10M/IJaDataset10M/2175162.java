package de.xirp.testerbot;

import java.util.Random;
import org.eclipse.swt.graphics.Point;

/**
 * Creates an array with values of a thermopile sensor.
 * 
 * @author Matthias Gernand
 */
public class TesterBotThermopile extends AbstractValueCreator {

    /**
	 * Maximum temperature
	 */
    private static final double MAX_CELL_VALUE = 100.0;

    /**
	 * Minimum temperature
	 */
    @SuppressWarnings("unused")
    private static final double MIN_CELL_VALUE = 4.0;

    /**
	 * Temperature of a hot spot
	 */
    private static final double HOT_TEMP = 50.0;

    /**
	 * The main environment temperature.
	 */
    private static final double ENV_TEMP = 20.0;

    /**
	 * Number of columns of the value array.
	 */
    private static final int CELL_COLUMN = 32;

    /**
	 * Number of rows of the value array.
	 */
    private static final int CELL_ROW = 8;

    /**
	 * The random number generator
	 */
    private final Random random;

    /**
	 * The random hotspot temperature to use
	 */
    private double randomTemp;

    /**
	 * The position of the hotspot
	 */
    private Point position;

    /**
	 * Counts how many times values were created since a new hot spot
	 * temperature was created.
	 */
    private int cnt = 0;

    /**
	 * Creator which returns an array with {@value #CELL_ROW} rows and
	 * {@value #CELL_COLUMN} columns. Each array element is a
	 * temperature ranging from {@value #MIN_CELL_VALUE} to
	 * {@value #MAX_CELL_VALUE} with an environment temperature of
	 * {@value #ENV_TEMP} and one hot spot ranging from
	 * {@value #HOT_TEMP} to {@value #MAX_CELL_VALUE}.
	 * 
	 * @param runTime
	 *            this time span is ignored.
	 */
    public TesterBotThermopile(long runTime) {
        super(runTime);
        random = new Random();
        position = new Point(random.nextInt(CELL_COLUMN), random.nextInt(CELL_ROW));
        randomTemp = random.nextInt((int) (MAX_CELL_VALUE - HOT_TEMP)) + HOT_TEMP;
    }

    /**
	 * Creates the array with temperatures.<br>
	 * There's one hotspot in the array which wanders around. The
	 * temperature of the hotspot is recalculated every so often. The
	 * hotspot also warms up the surrounding cells to some extent.
	 * 
	 * @param elapsedTime
	 *            this time is ignored. The values are created
	 *            continously.
	 * @see de.xirp.testerbot.AbstractValueCreator#calculate(long)
	 */
    @Override
    protected double[][] calculate(@SuppressWarnings("unused") long elapsedTime) {
        if (cnt % 10 == 0) {
            randomTemp = random.nextInt((int) (MAX_CELL_VALUE - HOT_TEMP)) + HOT_TEMP;
        }
        double[][] values = new double[CELL_ROW][CELL_COLUMN];
        for (int y = 0; y < CELL_ROW; y++) {
            for (int x = 0; x < CELL_COLUMN; x++) {
                values[y][x] = ENV_TEMP + random.nextInt(7) - 3;
            }
        }
        int deltaX = random.nextInt(3) - 1;
        int deltaY = random.nextInt(3) - 1;
        position.x += deltaX;
        position.y += deltaY;
        if (position.x >= CELL_COLUMN) {
            position.x = CELL_COLUMN - 1;
        } else if (position.x < 0) {
            position.x = 0;
        }
        if (position.y >= CELL_ROW) {
            position.y = CELL_ROW - 1;
        } else if (position.y < 0) {
            position.y = 0;
        }
        values[position.y][position.x] = randomTemp;
        for (int y = -2; y <= 2; y++) {
            for (int x = -2; x <= 2; x++) {
                if (!(x == 0 && y == 0)) {
                    int tempX = position.x + x;
                    int tempY = position.y + y;
                    double factor = 0.9;
                    if (Math.abs(x) == 2 || Math.abs(y) == 2) {
                        factor = 0.7;
                    }
                    if (tempX >= 0 && tempX < CELL_COLUMN && tempY >= 0 && tempY < CELL_ROW) {
                        values[tempY][tempX] = randomTemp * factor + (random.nextInt(7) - 3);
                    }
                }
            }
        }
        cnt++;
        return values;
    }
}
