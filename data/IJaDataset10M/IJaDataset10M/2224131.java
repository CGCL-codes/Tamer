package DE.FhG.IGD.semoa.bin.starter;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

/**
 * This is a set of static utility methods.
 *
 * @author Matthias Pressfreund
 * @version "$Id: Utils.java 1463 2004-08-16 10:48:52Z jpeters $"
 */
public class Utils {

    /** Hidden unused construction */
    private Utils() {
    }

    /**
     * This is a convenience method for adding a <code>Component</code>
     * into a <code>Container</code> by usage of the 
     * {@link java.awt.GridBagLayout}.
     */
    public static void addConstrained(Container container, Component component, int gridx, int gridy, int gridwidth, int gridheight, int fill, int anchor, double weightx, double weighty, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.fill = fill;
        gbc.anchor = anchor;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.insets = insets;
        container.add(component, gbc);
    }

    /**
     * Place the given component in the center of the screen.
     *
     * @param c The component to be centered
     */
    public static void center(Component c) {
        Dimension screensize;
        Dimension framesize;
        screensize = Toolkit.getDefaultToolkit().getScreenSize();
        framesize = c.getSize();
        c.setLocation((screensize.width - framesize.width) / 2, (screensize.height - framesize.height) / 2);
    }
}
