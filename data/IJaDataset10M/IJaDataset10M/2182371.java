package org.jbudget.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;
import org.jbudget.util.PaintUtils;

/** Creates a button filled by a black triangle pointint to the given direction */
public class TriangleButton extends JButton implements MouseListener {

    /** Image buffer for normal button state. */
    private BufferedImage normalButtonImage = null;

    /** Image buffer for rollover button state. */
    private BufferedImage rolloverButtonImage = null;

    /** Image buffer for pressed button state. */
    private BufferedImage pressedButtonImage = null;

    /** Direction of the button. */
    private final int direction;

    /** Button state. */
    private boolean isPressed = false;

    /** Button state. */
    private boolean isInRollover = false;

    /** Text Color */
    private Color foregroundColor = UIManager.getColor("Button.foreground");

    /** Polygon that describes the triangle that is drawn on the button. */
    private Polygon triangle;

    /** Creates a triangle button that points to a given direction. */
    public TriangleButton(int direction) {
        this.direction = direction;
        this.addMouseListener(this);
        setBorder(null);
        setPreferredSize(new Dimension(10, 10));
    }

    /** Main Painting method. */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (normalButtonImage == null || getWidth() != normalButtonImage.getWidth() || getHeight() != normalButtonImage.getHeight()) {
            update();
        }
        if (isPressed) g.drawImage(pressedButtonImage, 0, 0, getWidth(), getHeight(), this); else if (isInRollover) g.drawImage(rolloverButtonImage, 0, 0, getWidth(), getHeight(), this); else g.drawImage(normalButtonImage, 0, 0, getWidth(), getHeight(), this);
    }

    /** Recreates the polygon that represents the triangle that will be 
     * drawn on the button.
     */
    private void createTriangle() {
        int[] xValues = new int[3];
        int[] yValues = new int[3];
        int x = 0;
        int y = 0;
        int width = getWidth();
        int height = getHeight();
        int dx = (int) (width * 0.1);
        if (dx == 0) dx = 1;
        int dy = (int) (height * 0.1);
        if (dy == 0) dy = 1;
        if (direction == NORTH) {
            xValues[0] = x + dx;
            yValues[0] = y + height - dy;
            xValues[1] = x + width - dx;
            yValues[1] = yValues[0];
            xValues[2] = x + width / 2;
            yValues[2] = y + dy;
            triangle = new Polygon(xValues, yValues, 3);
        } else if (direction == SOUTH) {
            xValues[0] = x + dx;
            yValues[0] = y + dy;
            xValues[1] = x + width - dx;
            yValues[1] = yValues[0];
            xValues[2] = x + width / 2;
            yValues[2] = y + height - dy;
            triangle = new Polygon(xValues, yValues, 3);
        } else if (direction == EAST) {
            xValues[0] = x + dx;
            yValues[0] = y + dy;
            xValues[1] = xValues[0];
            yValues[1] = y + height - dy;
            xValues[2] = x + width - dx;
            yValues[2] = y + height / 2;
            triangle = new Polygon(xValues, yValues, 3);
        } else if (direction == WEST) {
            xValues[0] = x + width - dx;
            yValues[0] = y + dy;
            xValues[1] = xValues[0];
            yValues[1] = y + height - dy;
            xValues[2] = x + dx;
            yValues[2] = y + height / 2;
            triangle = new Polygon(xValues, yValues, 3);
        } else throw new RuntimeException("Invalid Button Direction in TriangeButton.");
    }

    /** Recreates and updates all buffered images. */
    private void update() {
        createTriangle();
        allocateBuffers();
        updateNormalBuffer();
        updateRolloverBuffer();
        updatePressedBuffer();
    }

    /** Allocates internal buffers. */
    private void allocateBuffers() {
        normalButtonImage = PaintUtils.createCompatibleImage(getWidth(), getHeight());
        rolloverButtonImage = PaintUtils.createCompatibleImage(getWidth(), getHeight());
        pressedButtonImage = PaintUtils.createCompatibleImage(getWidth(), getHeight());
    }

    /** Paints normal button to the normalButtonImage. */
    private void updateNormalBuffer() {
        Graphics2D g = (Graphics2D) normalButtonImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        int dy = (int) (getHeight() * 0.05);
        if (dy > 5) dy = 5;
        if (dy == 0) dy = 1;
        int dx = (int) (getWidth() * 0.05);
        if (dx > 5) dx = 5;
        if (dx == 0) dx = 1;
        Color shadowColor = new Color(foregroundColor.getRed(), foregroundColor.getGreen(), foregroundColor.getBlue(), 100);
        g.setColor(shadowColor);
        triangle.translate(dx, dy);
        g.fillPolygon(triangle);
        triangle.translate(-dx, -dy);
        g.setColor(foregroundColor);
        g.fillPolygon(triangle);
    }

    /** Paints rollover button to the rolloverButtonImage. */
    private void updateRolloverBuffer() {
        Graphics2D g = (Graphics2D) rolloverButtonImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getBackground().brighter());
        g.fillRect(0, 0, getWidth(), getHeight());
        int dy = (int) (getHeight() * 0.05);
        if (dy > 5) dy = 5;
        if (dy == 0) dy = 1;
        int dx = (int) (getWidth() * 0.05);
        if (dx > 5) dx = 5;
        if (dx == 0) dx = 1;
        Color shadowColor = new Color(foregroundColor.getRed(), foregroundColor.getGreen(), foregroundColor.getBlue(), 100);
        g.setColor(shadowColor);
        triangle.translate(dx, dy);
        g.fillPolygon(triangle);
        triangle.translate(-dx, -dy);
        g.setColor(foregroundColor);
        g.fillPolygon(triangle);
    }

    /** Paints pressed button to the pressedButtonImage. */
    private void updatePressedBuffer() {
        Graphics2D g = (Graphics2D) pressedButtonImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getBackground().darker());
        g.fillRect(0, 0, getWidth(), getHeight());
        int dy = (int) (getHeight() * 0.05);
        if (dy > 5) dy = 5;
        if (dy == 0) dy = 1;
        int dx = (int) (getWidth() * 0.05);
        if (dx > 5) dx = 5;
        if (dx == 0) dx = 1;
        Color shadowColor = new Color(foregroundColor.getRed(), foregroundColor.getGreen(), foregroundColor.getBlue(), 100);
        g.setColor(shadowColor);
        triangle.translate(dx, dy);
        g.fillPolygon(triangle);
        triangle.translate(-dx, -dy);
        g.setColor(foregroundColor);
        g.fillPolygon(triangle);
    }

    public void mouseClicked(MouseEvent ev) {
    }

    public void mousePressed(MouseEvent ev) {
        isPressed = true;
        repaint();
    }

    public void mouseReleased(MouseEvent ev) {
        isPressed = false;
        repaint();
    }

    public void mouseEntered(MouseEvent ev) {
        isInRollover = true;
        repaint();
    }

    public void mouseExited(MouseEvent ev) {
        isInRollover = false;
        repaint();
    }

    @Override
    public void updateUI() {
        setUI((ButtonUI) UIManager.getUI(this));
        this.setForeground(UIManager.getColor("control"));
        foregroundColor = UIManager.getColor("control");
    }
}
