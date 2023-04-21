package br.usp.iterador.universeiterator;

import org.apache.log4j.Logger;
import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.ImageInfo;

/**
 * Executes an iteration for each point on the view space.
 * 
 * @author Guilherme Silveira
 */
public class UniverseIteratorExecutor {

    private static final Logger logger = Logger.getLogger(UniverseIteratorExecutor.class);

    private boolean[][] done;

    private int width, height, pointsDone = 0;

    private GUIHelper helper;

    private Application app;

    private UniverseIteratorExecutorListener listener;

    public UniverseIteratorExecutor(Application app, UniverseIteratorExecutorListener listener) {
        this.listener = listener;
        this.helper = new GUIHelper(width, height);
        this.app = app;
        ImageInfo info = app.getImage();
        this.width = info.getWidth();
        this.height = info.getHeight();
        this.done = new boolean[height + 5][width + 5];
    }

    public void doSquare() {
        int curX = 0, curY = 0, delX = width, delY = height;
        while (true) {
            int x = curX + delX / 2;
            int y = curY + delY / 2;
            if (x < width && y < height && !done[y][x]) {
                double leftX = helper.mudaEscala(x, 0, width, app.getXScale());
                double leftY = helper.mudaEscala(y, 0, height, app.getYScale());
                double rightX = helper.mudaEscala(x + 1, 0, width, app.getXScale());
                double rightY = helper.mudaEscala(y + 1, 0, height, app.getYScale());
                listener.doPoint(leftX, leftY, rightX, rightY, curY, curX, delY, delX, x, y);
                pointsDone++;
                done[y][x] = true;
            }
            if (x < width) {
                curX += delX;
            } else if (y < height) {
                curY += delY;
                curX = 0;
            } else {
                if (delX == 1 && delY == 1) break;
                if (delX != 1) delX /= 2;
                if (delY != 1) delY /= 2;
                curX = curY = 0;
                logger.debug(String.format("del changed to (%d,%d)", delX, delY));
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!done[y][x]) {
                    logger.error("found point not done: " + y + ", " + x);
                    double leftX = helper.mudaEscala(x, 0, width, app.getXScale());
                    double leftY = helper.mudaEscala(y, 0, height, app.getYScale());
                    double rightX = helper.mudaEscala(x + 1, 0, width, app.getXScale());
                    double rightY = helper.mudaEscala(y + 1, 0, height, app.getYScale());
                    listener.doPoint(leftX, leftY, rightX, rightY, y, x, 0, 0, x, y);
                    pointsDone++;
                    done[y][x] = true;
                }
            }
        }
    }

    public void init() {
        this.listener.init();
    }

    public void finish() {
        this.listener.finish();
    }

    public double getPercentageComplete() {
        return helper.mudaEscala(pointsDone, 0, width * height, 0, 100.0);
    }
}
