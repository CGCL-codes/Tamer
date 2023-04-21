package jat.oppoc.sunjai;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/**
 * A output widget used to display a colormap, derived from the
 * javax.swing.JComponent, and can be used in any context that calls for a
 * JComponent.
 *
 * @author Dennis Sigel
 */
public class Colorbar extends JComponent {

    protected int componentWidth;

    protected int componentHeight;

    protected int direction = SwingConstants.HORIZONTAL;

    public static int RedColorComp = 0;

    public static int GreenColorComp = 1;

    public static int BlueColorComp = 2;

    public static int GrayColorComp = 3;

    /**
	 * colorComp  = 0 => RedColorComp
	 * colorComp  = 1 => GreenColorComp
	 * colorComp  = 2 => BlueColorComp
	 * colorComp  = 3 => GrayColorComp
	 */
    protected int colorComp = 0;

    /** Brightness control */
    protected byte[][] lut;

    /**
	 * Default constructor
	 */
    public Colorbar() {
        lut = new byte[3][256];
        for (int i = 0; i < 256; i++) {
            lut[0][i] = (byte) i;
            lut[1][i] = (byte) i;
            lut[2][i] = (byte) i;
        }
        setSize(getWidth(), 25);
    }

    /** 
	 * Constructs a Colorbar object 
	 *
	 * @param d used for orientation
	 */
    public Colorbar(int d) {
        lut = new byte[3][256];
        for (int i = 0; i < 256; i++) {
            lut[0][i] = (byte) i;
            lut[1][i] = (byte) i;
            lut[2][i] = (byte) i;
        }
        direction = d;
    }

    public void setColorComp(int acol) {
        colorComp = acol;
    }

    /** changes the contents of the lookup table */
    public synchronized void setLut(byte[][] newlut) {
        for (int i = 0; i < newlut[0].length; i++) {
            lut[0][i] = newlut[0][i];
            lut[1][i] = newlut[1][i];
            lut[2][i] = newlut[2][i];
        }
        repaint();
    }

    /** Records a new size.  Called by the AWT. */
    public void setBounds(int x, int y, int width, int height) {
        componentWidth = width;
        componentHeight = getHeight();
        super.setBounds(x, y, width, height);
    }

    /**
	 * Paint the Colorbar onto a Graphics object.
	 */
    public synchronized void paintComponent(Graphics g) {
        Graphics2D g2D = null;
        if (g instanceof Graphics2D) {
            g2D = (Graphics2D) g;
        } else {
            return;
        }
        g2D.setColor(getBackground());
        g2D.fillRect(0, 0, componentWidth, componentHeight);
        if (direction == SwingConstants.HORIZONTAL) {
            float slope = (float) componentWidth / 256.0F;
            for (int n = 0; n < lut[0].length; n++) {
                int w = componentWidth - (int) ((float) n * slope);
                int v = lut[0].length - n - 1;
                int red = lut[0][v] & 0xFF;
                int green = lut[1][v] & 0xFF;
                int blue = lut[2][v] & 0xFF;
                if (colorComp == Colorbar.RedColorComp) g.setColor(new Color(red, 0, 0)); else if (colorComp == Colorbar.GreenColorComp) g.setColor(new Color(0, green, 0)); else if (colorComp == Colorbar.BlueColorComp) g.setColor(new Color(0, 0, blue)); else g.setColor(new Color(red, green, blue));
                g.fillRect(0, 0, w, componentHeight);
            }
        } else if (direction == SwingConstants.VERTICAL) {
            float slope = (float) componentHeight / 256.0F;
            for (int n = 0; n < lut[0].length; n++) {
                int h = componentHeight - (int) ((float) n * slope);
                int red = lut[0][n] & 0xFF;
                int green = lut[1][n] & 0xFF;
                int blue = lut[2][n] & 0xFF;
                if (colorComp == Colorbar.RedColorComp) g.setColor(new Color(red, 0, 0)); else if (colorComp == Colorbar.GreenColorComp) g.setColor(new Color(0, green, 0)); else if (colorComp == Colorbar.BlueColorComp) g.setColor(new Color(0, 0, blue)); else g.setColor(new Color(red, green, blue));
                g.fillRect(0, 0, componentWidth, h);
            }
        }
    }
}
