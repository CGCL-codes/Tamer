package pl.org.minions.stigma.client.ui.swing;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JInternalFrame;

/**
 * Utility class. Reacts to resize event by packing the
 * referenced internal frame.
 */
public class InternalFramePacker extends ComponentAdapter {

    private JInternalFrame frame;

    /**
     * Constructor.
     * @param frame
     *            frame to pack upon resize events
     */
    public InternalFramePacker(JInternalFrame frame) {
        this.frame = frame;
    }

    /** {@inheritDoc} */
    @Override
    public void componentResized(ComponentEvent e) {
        frame.pack();
    }
}
