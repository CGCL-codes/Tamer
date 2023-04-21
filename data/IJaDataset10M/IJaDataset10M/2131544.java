package coat.ui.jogl;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import coat.DemoFactory;
import coat.Graph;
import coat.GraphEventQueue;
import coat.elements.Axis;

/**
 * Listens for user interaction, here: arrow keys.
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since Oct 19, 2007
 */
public class JoglController {

    /** The JOGL pane to operate on. */
    private JoglPane pane;

    /** The current key pressed. */
    private int currentKeyPressed = 0;

    /** The x (horizontal) user coordinate. */
    private int iX = 0;

    /** The y (vertical) user coordinate. */
    private int iY = 0;

    /** The z (distance) user coordinate. */
    private int iZ = 0;

    /**
   * A new JOGL (key, mouse) controller.
   * 
   * @param pane The JOGL pane.
   */
    public JoglController(JoglPane pane) {
        this.pane = pane;
        this.pane.setController(this);
        Component awtComponent = pane.getAWTComponent();
        awtComponent.addKeyListener(new KeyController());
    }

    /**
   * Controls the key strokes by the user.
   * 
   * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
   * @since Mar 22, 2008
   */
    private class KeyController implements KeyListener {

        /**
     * Key pressed.
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
        public void keyPressed(KeyEvent evt) {
            if (0 != currentKeyPressed) {
                return;
            }
            currentKeyPressed = evt.getKeyCode();
        }

        /**
     * Key released.
     * 
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
        public void keyReleased(KeyEvent evt) {
            if (0 == currentKeyPressed) {
                return;
            }
            if (currentKeyPressed == evt.getKeyCode()) {
                currentKeyPressed = 0;
            }
        }

        /**
     * Not supported.
     * 
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
        public void keyTyped(KeyEvent evt) {
        }
    }

    /**
   * Advances one tick.
   */
    public void tick() {
        switch(currentKeyPressed) {
            case KeyEvent.VK_LEFT:
                iX--;
                break;
            case KeyEvent.VK_RIGHT:
                iX++;
                break;
            case KeyEvent.VK_UP:
                iY--;
                break;
            case KeyEvent.VK_DOWN:
                iY++;
                break;
            case KeyEvent.VK_PAGE_UP:
                iZ--;
                break;
            case KeyEvent.VK_PAGE_DOWN:
                iZ++;
                break;
            case KeyEvent.VK_ENTER:
                reset();
                break;
            case KeyEvent.VK_ESCAPE:
                DemoFactory.isDemoRunning = false;
                break;
            case KeyEvent.VK_HOME:
                toggleGraphLayout();
                currentKeyPressed = 0;
                break;
            case KeyEvent.VK_END:
                toggleAxisLabels();
                currentKeyPressed = 0;
                break;
            case KeyEvent.VK_SPACE:
                pane.addGraphEvent(GraphEventQueue.EVENT_FULL_RENDER);
                break;
        }
    }

    /**
   * @return The current x coordinate.
   */
    public int getX() {
        return iX;
    }

    /**
   * @return The current y coordinate.
   */
    public int getY() {
        return iY;
    }

    /**
   * @return The current z coordinate.
   */
    public int getZ() {
        return iZ;
    }

    /**
   * @param iX The current x coordinate.
   */
    public void setX(int iX) {
        this.iX = iX;
    }

    /**
   * @param iY The current y coordinate.
   */
    public void setY(int iY) {
        this.iY = iY;
    }

    /**
   * @param iZ The current z coordinate.
   */
    public void setZ(int iZ) {
        this.iZ = iZ;
    }

    /**
   * Resets the controller.
   */
    public void reset() {
        iX = 0;
        iY = 0;
        iZ = 0;
    }

    /**
   * Changes the graph layout.
   */
    public void toggleGraphLayout() {
        int layerMode = pane.getLayerModus();
        layerMode = (layerMode + 1) % Graph.LAYER_MODE_COUNT;
        pane.setLayerModus(layerMode);
        pane.addGraphEvent(GraphEventQueue.EVENT_FULL_RENDER);
    }

    /**
   * Changes the axis label orientation (on the y axis).
   */
    public void toggleAxisLabels() {
        final int graphCount = pane.getGraphCount();
        Object data;
        Axis axis;
        int labelStyleY;
        for (int pos = 0; pos < graphCount; pos++) {
            for (Iterator dataIter = pane.getGraphAt(pos).getPersistentData().iterator(); dataIter.hasNext(); ) {
                data = dataIter.next();
                if (data instanceof Axis) {
                    axis = (Axis) data;
                    labelStyleY = axis.getYLabelStyle();
                    axis.setYLabelStyle((labelStyleY + 1) % 2);
                }
            }
        }
        pane.addGraphEvent(GraphEventQueue.EVENT_FULL_RENDER);
    }
}
