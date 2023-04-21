package be.lassi.ui.base;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import be.lassi.preferences.DisplayPreferences;

/**
 * Component that will cover the components under the glasspane
 * with a transparant black rectangle.
 */
public class Cover extends JComponent {

    private final DisplayPreferences preferences;

    /**
     * Factor that determines the maximum coverage that will be used.
     * With a value of 1f the maximum would be to completely cover
     * the windows with the black rectangle.  A value of zero means
     * cover is completely transparant.
     */
    private static final float MAXIMUM_COVER = 0.85f;

    public Cover(final DisplayPreferences preferences) {
        this.preferences = preferences;
        preferences.addPropertyChangeListener(DisplayPreferences.COVERAGE, new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent evt) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(final Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int value = preferences.getCoverage();
        float floatValue = value / 100f * MAXIMUM_COVER;
        floatValue = (float) Math.pow(floatValue, 2);
        AlphaComposite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, floatValue);
        g2.setColor(Color.BLACK);
        g2.setComposite(c);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
