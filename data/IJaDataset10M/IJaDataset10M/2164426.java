package gui.picker.degrade;

import gui.picker.slider.MySlider;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import javax.swing.JSlider;
import color.HSLColor;

/**
 * Create a color gradian L&F from H,S,0 to H,S,1.
 * 
 * @author Desprez Jean-Marc
 * 
 */
public class BriDegrade extends MySlider {

    private static final long serialVersionUID = 1L;

    /**
   * Constructor. The given color is linked, not duplicated.
   * 
   * @param color
   *          The color used for the color gradation.
   * @param b
   *          The slider used with this L&F.
   */
    public BriDegrade(final HSLColor color, final JSlider b) {
        super(color, b);
    }

    private Color getMiddle() {
        HSLColor c = new HSLColor(color.getHue(), color.getSaturation(), 0.5);
        return new Color(c.getColor());
    }

    @Override
    protected void doPaintTrack(final Graphics2D g2d) {
        g2d.setPaint(new GradientPaint(0, 0, getBegin(), SIZE_W / 2, 0, getMiddle()));
        g2d.fillRect(0, 0, SIZE_W / 2, SIZE_H);
        g2d.setPaint(new GradientPaint(SIZE_W / 2, 0, getMiddle(), SIZE_W, 0, getEnd()));
        g2d.fillRect(SIZE_W / 2, 0, SIZE_W / 2, SIZE_H);
    }

    @Override
    protected Color getBegin() {
        HSLColor c = new HSLColor(color.getHue(), color.getSaturation(), 0);
        return new Color(c.getColor());
    }

    @Override
    protected Color getEnd() {
        HSLColor c = new HSLColor(color.getHue(), color.getSaturation(), 1);
        return new Color(c.getColor());
    }
}
