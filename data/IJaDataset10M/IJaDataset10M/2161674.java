package net.sf.joafip.performance.items.service;

import java.awt.Color;
import net.sf.joafip.logger.JoafipLogger;

/**
 * 
 * @author luc peuvrier
 * 
 */
public final class MainDrawGraphTest {

    private static final JoafipLogger LOGGER = JoafipLogger.getLogger(MainDrawGraphTest.class);

    public static void main(final String[] args) {
        try {
            final MainDrawGraphTest mainDrawGraphTest = new MainDrawGraphTest();
            mainDrawGraphTest.run();
        } catch (Exception exception) {
            LOGGER.error("error", exception);
        }
    }

    private MainDrawGraphTest() {
        super();
    }

    private void run() {
        final GraphDrawer graphDrawer = new GraphDrawer();
        final int[] values1 = new int[] { 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89 };
        graphDrawer.addData(values1, Color.BLUE, "mS min", "mS max");
        final int[] values2 = new int[] { 100, 200, 700, 600, 300, 400, 500, 800, 900, 100, 200, 300, 400, 500, 600, 700, 800, 900, 100, 200, 300, 400, 500, 600, 700, 800, 900, 100, 800, 200, 400, 500, 300, 600, 700, 900, 100, 600, 200, 300, 900, 400, 500, 700, 800 };
        graphDrawer.addData(values2, Color.RED, "aa", "bbbb");
        graphDrawer.draw("runtime/image.png", 200, 200, "10", "100");
    }
}
