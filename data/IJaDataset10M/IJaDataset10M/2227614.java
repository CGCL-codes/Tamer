package modules.construct;

import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JScrollPane;

/**
 * A JScrollPane that reacts faster to mouse-scrolling.
 * 
 * @author Sven Schneider
 * @since 0.1.1
 */
public class FastScrollPane extends JScrollPane {

    /** A generated serialVersionUID. */
    private static final long serialVersionUID = 5419650914038391109L;

    /**
	 * Construtor, eneloping the passed Component.
	 * <p>
	 * Additionally to the super-constructor, a mousewheellistener is added.
	 * 
	 * @param comp
	 *            the component that will be contained in this ScrollPane.
	 */
    protected FastScrollPane(Component comp) {
        super(comp);
        addMouseWheelListener(new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent evt) {
                verticalScrollBar.setValue(verticalScrollBar.getValue() + evt.getUnitsToScroll() * 10);
            }
        });
    }
}
