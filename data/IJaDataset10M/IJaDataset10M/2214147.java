package treemap;

import java.awt.*;
import javax.swing.*;

/**
 * The TMSDSimple implements a view displaying 
 * the activity taking place.
 * The TMSDSimple is configurable to indicate what the activity is.
 * <P>
 * Here, increment() does nothing.
 */
class TMSDSimple extends TMStatusDisplay {

    private JLabel label = null;

    /**
     * Constructor.
     *
     * @param status    the status text
     */
    TMSDSimple(String status) {
        super(new FlowLayout());
        label = new JLabel(status);
        add(label);
        Dimension d = label.getPreferredSize();
        setPreferredSize(d);
        setSize(d);
        setMaximumSize(d);
    }

    /**
     * Increments the display. 
     * Does nothing here.
     */
    void increment() {
    }

    /**
     * Do a deep clone of the TMSDSimple.
     *
     * @return    a deep clone
     */
    TMStatusDisplay deepClone() {
        TMSDSimple clone = new TMSDSimple(label.getText());
        return clone;
    }

    /**
     * Returns the TMSDSimple in a String.
     *
     * @return    the status
     */
    public String toString() {
        return label.getText();
    }
}
