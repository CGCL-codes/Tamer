package cerebralnexus.applet.view;

import cerebralnexus.applet.control.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import javax.swing.JToggleButton;

/**
 * Custom toggle button that is used to easily create buttons with images, or
 * buttons filled with solid colors.
 * @author Brent Couvrette
 */
public class CustomToggleButton extends JToggleButton {

    /**
     * Type constant indicating that the button will be filled with a solid
     * color.
     */
    public static final int COLOR_BUTTON = 0;

    /**
     * Type constant indicating that the button will be filled with an image.
     */
    public static final int PICTURE_BUTTON = 1;

    private int type;

    private BufferedImage image;

    private TexturePaint paint;

    /**
     * Creates a new CustomToggleButton of the given type.
     * @param type One of the type constants given in this class.
     */
    public CustomToggleButton(int type) {
        this.type = type;
        image = null;
        paint = null;
        if (type == PICTURE_BUTTON) {
            this.setMinimumSize(new Dimension(BrainStormApplet.BUTTON_DEFAULT, BrainStormApplet.BUTTON_DEFAULT));
            this.setPreferredSize(new Dimension(BrainStormApplet.BUTTON_DEFAULT, BrainStormApplet.BUTTON_DEFAULT));
            this.setMaximumSize(new Dimension(BrainStormApplet.BUTTON_DEFAULT, BrainStormApplet.BUTTON_DEFAULT));
        }
    }

    /**
     * Returns the image that this button would use if it was a picture button.
     * @return This button's image.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Sets the image that this button will use if it is a picture button.
     * @param image The image that should be used for this button.
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Paints the button either with the solid color or the image.
     * @param g The graphics object used to paint with.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        switch(type) {
            case COLOR_BUTTON:
                g2d.setPaint(this.getBackground());
                g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
                if (isSelected()) {
                    g2d.setStroke(new BasicStroke(2.0f));
                    g2d.drawRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
                }
                if (!this.getText().equals("")) {
                    g.setColor(Color.BLACK);
                    int fontHeight = g.getFontMetrics().getAscent();
                    int y = ((this.getHeight() - fontHeight) / 2) + fontHeight;
                    int fontWidth = g.getFontMetrics().stringWidth(this.getText());
                    int x = (this.getWidth() - fontWidth) / 2;
                    g.drawString(this.getText(), x, y);
                }
                break;
            case PICTURE_BUTTON:
                if (image != null) {
                    paint = new TexturePaint(image, new Rectangle(0, 0, this.getWidth(), this.getHeight()));
                    g2d.setPaint(paint);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
                break;
        }
    }
}
