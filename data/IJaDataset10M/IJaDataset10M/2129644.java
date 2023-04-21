package org.shapeoforion;

import org.shapeoforion.ui.AbstractAnimationFrame;
import org.shapeoforion.log.MyLogger;
import org.apache.commons.logging.Log;
import javax.swing.*;
import java.awt.*;

/**
 * This class kicks off the game.
 *
 * @author jps
 * @version 0.1
 */
public final class ShapeOfOrion extends AbstractAnimationFrame {

    private static final Log LOG = MyLogger.getInstance(ShapeOfOrion.class);

    /**
     * construct the game
     *
     * @param args the command line arguments
     */
    public ShapeOfOrion(String[] args) {
        if (!DEVELOPMENT_MODE) {
            enterFullscreen();
        } else {
            LOG.debug("running in dev mode, FSE disabled");
        }
        setVisible(true);
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getBufferStrategy();
        width = getWidth();
        height = getHeight();
    }

    /**
     * The main() method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOG.info("starting Shape Of Orion");
        AbstractAnimationFrame soo = new ShapeOfOrion(args);
        soo.start();
    }

    protected void render(Graphics2D g) {
        LOG.debug("render");
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
    }

    /**
     * Test if the game is running - if so, the gameloop should continue.
     * @return true if the game is running, else false (duh)
     */
    protected boolean isGameRunning() {
        return true;
    }

    public static final boolean DEVELOPMENT_MODE = true;

    private int width, height;
}
