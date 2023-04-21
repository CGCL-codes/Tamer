package jadaclient.gui;

import com.golden.gamedev.GameObject;
import com.golden.gamedev.gui.TButton;
import com.golden.gamedev.gui.toolkit.GraphicsUtil;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 *
 * @author Cris
 * @version 1.0
 * 
 */
public class JadaArrowButton extends TButton {

    /**
     * Arrows images
     */
    private BufferedImage arrow, arrowclick;

    /**
     * Angles used in the computation of orientation
     */
    private int[] angles = new int[] { -90, -270, -180, 0 };

    /**
     * Constant guidance upwards
     */
    public static byte ArrowUP = 0;

    /**
     * Constant guidance down
     */
    public static byte ArrowDown = 1;

    /**
     * Constant guidance left
     */
    public static byte ArrowLeft = 2;

    /**
     * Constant guidance right
     */
    public static byte ArrowRight = 3;

    /**
     * Listener who governs the actions
     */
    private JadaActionListener list;

    /**
     * Build a new JadaArrowButton
     * @param owern the gameobject where button is placed
     * @param x x position
     * @param y y position
     * @param w Width
     * @param h Height
     * @param orient the Arrow orientation 
     * @see JadaArrowButton#ArrowDown JadaArrowButton#ArrowUP JadaArrowButton#ArrowLeft JadaArrowButton#ArrowRight
     */
    public JadaArrowButton(GameObject owern, int x, int y, int w, int h, byte orient) {
        super("", x, y, w, h);
        customRendering = true;
        AffineTransform rt = new AffineTransform();
        this.arrow = owern.getImage("res/GUI/arrow.png");
        this.arrowclick = owern.getImage("res/GUI/arrowclick.png");
        rt.scale((double) w / arrow.getWidth(), (double) h / arrow.getHeight());
        rt.rotate(angles[orient] * Math.PI / 180.0, this.arrow.getHeight() / 2, this.arrow.getWidth() / 2);
        BufferedImageOp bio;
        bio = new AffineTransformOp(rt, AffineTransformOp.TYPE_BILINEAR);
        arrow = bio.filter(arrow, null);
        arrowclick = bio.filter(arrowclick, null);
    }

    protected void createCustomUI(int w, int h) {
        this.ui = GraphicsUtil.createImage(2, w, h, Transparency.BITMASK);
        Graphics2D g = ui[0].createGraphics();
        g.drawImage(arrow, 0, 0, null);
        g.dispose();
        Graphics2D g2 = ui[1].createGraphics();
        g2.drawImage(arrowclick, 0, 0, null);
        g2.dispose();
    }

    protected void processCustomUI() {
    }

    protected void renderCustomUI(Graphics2D g, int x, int y, int w, int h) {
        g.drawImage((isMousePressed()) ? ui[0] : ui[1], x, y, null);
    }

    /**
     * Set new IconArrow
     * @param newArrow 
     */
    public void setCostumizeArrow(BufferedImage newArrow) {
        this.arrow = newArrow;
    }

    @Override
    protected void processKeyPressed() {
        super.processKeyPressed();
    }

    /**
     * Set the ActionListener
     * @param list 
     * @see JadaActionListener
     */
    public void setActionListener(JadaActionListener list) {
        this.list = list;
    }

    /**
     * Perform the action should never used out of this class because the ActionListener hadle this jet
     */
    @Override
    public void doAction() {
        if (list != null) list.action(this);
    }
}
