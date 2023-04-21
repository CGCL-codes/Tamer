package seevolution.trees;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;

/**
 * A TreePanel is a component used to display an associated Tree. It uses two private classes to record mouse events that are 
 * processed in a separate thread 30 times per second to give a smooth feeling without overloading the CPU.<br>
 * The tree is displayed in two overlapping images. The bottom one contains the unchanging tree structure, and the top one
 * is mostly transparent and contains the changing elements, such as the position markers or the highlighted nodes.
 * @author Andres Esteban Marcos
 * @version 1.0
 */
public class TreePanel extends JPanel implements Runnable, ComponentListener {

    private static final int FPS = 30;

    private int moveX, moveY, pressX, pressY, pressButton;

    private boolean newMove = false, newPress = false;

    private BufferedImage treeImage;

    private BufferedImage overImage;

    private Dimension size;

    private Graphics2D overGraphics;

    private Color transparent;

    private Color markerColor = Color.red;

    private Color positionColor = Color.blue;

    private Color originColor = Color.yellow;

    private Color destinationColor = Color.green;

    private Color highlightColor = Color.orange;

    private Tree tree;

    /**
	 * Creates a new TreePanel without an associated tree
	 */
    public TreePanel() {
        this(null);
    }

    /**
	 * Creates a new TreePanel with an associated tree
	 * @param tree The tree
	 */
    public TreePanel(Tree tree) {
        this.tree = tree;
        if (tree != null) tree.randomLeafOriginAndDestination();
        addComponentListener(this);
        transparent = new Color(0, 0, 0, 0);
        addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) mousePress(e.getX(), e.getY(), MouseEvent.BUTTON3); else mousePress(e.getX(), e.getY(), e.getButton());
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent e) {
                mouseMove(e.getX(), e.getY());
            }
        });
        createTreeImage();
        (new Thread(this)).start();
    }

    /**
	 * Changes the tree associated with this panel
	 * @param tree The new tree
	 */
    public void setTree(Tree tree) {
        this.tree = tree;
        if (tree != null) tree.randomLeafOriginAndDestination();
        createTreeImage();
    }

    /**
	 * Creates a new image the same size as this component and paints the tree and its accidents on it
	 */
    public void createTreeImage() {
        size = getSize();
        if (size.width <= 0 || size.height <= 0) return;
        treeImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = treeImage.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, size.width, size.height);
        if (tree != null) {
            g.setColor(Color.black);
            paintTree(g);
        }
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        overImage = gc.createCompatibleImage(size.width, size.height, Transparency.TRANSLUCENT);
        overGraphics = (Graphics2D) overImage.createGraphics();
        overGraphics.setComposite(AlphaComposite.Src);
        paintAccidents();
        repaint();
    }

    /**
	 * Used by the private classes to record when the mouse moves.
	 * @param x The x coordinate of the mouse
	 * @param y The y coordinate of the mouse
	 */
    public void mouseMove(int x, int y) {
        moveX = x;
        moveY = y;
        newMove = true;
    }

    /**
	 * Used by the private classes to record a mouse click
	 * @param x The x coordinate of the mouse
	 * @param y The y coordinate of the mouse
	 * @param button The button pressed
	 */
    public void mousePress(int x, int y, int button) {
        pressX = x;
        pressY = y;
        pressButton = button;
        newPress = true;
    }

    /**
	 * Paints this component. The unchanging tree image is painted first, then the transparent image with the accidents is painted on top of it.
	 * @param g The graphics object on which the images are painted.
	 */
    public void paint(Graphics g) {
        if (treeImage != null) {
            g.drawImage(treeImage, 0, 0, null);
        }
        if (overImage != null) {
            g.drawImage(overImage, 0, 0, null);
        }
    }

    /**
	 * Paints the tree accidents on the transparent image. The accidents include the origin and destination nodes, any highlighted nodes, 
	 * a marker that identifies the position on which the mouse is and a marker that identifies the current position on the tree.
	 */
    public void paintAccidents() {
        if (overGraphics != null && tree != null) {
            overGraphics.setColor(transparent);
            overGraphics.fill(new Rectangle(0, 0, size.width, size.height));
            overGraphics.setColor(originColor);
            tree.paintOrigin(overGraphics);
            overGraphics.setColor(destinationColor);
            tree.paintDestination(overGraphics);
            overGraphics.setColor(highlightColor);
            tree.highlightNode(overGraphics);
            overGraphics.setColor(markerColor);
            tree.paintMarker(overGraphics);
            overGraphics.setColor(positionColor);
            tree.paintPosition(overGraphics);
        }
    }

    /**
	 * Paints the tree associated with this panel
	 * @param g The Graphics object on which the tree is painted
	 * @return
	 */
    private void paintTree(Graphics g) {
        float length = tree.getLength();
        if (length == 0) length++;
        float scale = (float) (size.height - 40) / length;
        tree.paint(5, size.width - 5, 20, scale, g);
    }

    /**
	 * Repaints the position when it changes on the tree without direct user intervention
	 */
    public void repaintPosition() {
        if (overGraphics != null && tree != null) {
            overGraphics.setColor(transparent);
            tree.paintLastPosition(overGraphics);
            overGraphics.setColor(positionColor);
            tree.paintPosition(overGraphics);
        }
    }

    /**
	 * Required by the ComponentListener interface, used when the component is resized to repaint the tree using the new size
	 * @param e The ComponentEvent
	 */
    public void componentResized(ComponentEvent e) {
        createTreeImage();
    }

    /**
	 * Required by the ComponentListener interface, not used
	 * @param e The ComponentEvent
	 */
    public void componentHidden(ComponentEvent e) {
    }

    /**
	 * Required by the ComponentListener interface, not used
	 * @param e The ComponentEvent
	 */
    public void componentMoved(ComponentEvent e) {
    }

    /**
	 * Required by the ComponentListener interface, not used
	 * @param e The ComponentEvent
	 */
    public void componentShown(ComponentEvent e) {
    }

    /**
	 * Required by the Runnable interface, this thread runs 30 times per second and processes any mouse moves and clicks. Only the last recorded event of each kind is process to
	 * prevent from overloading the CPU. If there have been any changes, the accidents are repainted and so is this component.
	 */
    public void run() {
        long sleepTime = 1000 / FPS;
        while (true) {
            if (newMove) {
                if (tree != null) {
                    overGraphics.setColor(transparent);
                    overGraphics.fill(new Rectangle(0, 0, size.width, size.height));
                    tree.setMarker(moveX, moveY);
                    paintAccidents();
                }
                newMove = false;
            }
            if (newPress) {
                if (tree != null) {
                    if (pressButton == MouseEvent.BUTTON3) {
                        tree.setDestination(pressX, pressY);
                    } else {
                        tree.setOrigin(pressX, pressY);
                        tree.setPosition(pressX, pressY);
                    }
                    paintAccidents();
                }
                newPress = false;
            }
            if (!newMove && !newPress) repaintPosition();
            repaint();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ie) {
            }
        }
    }
}
