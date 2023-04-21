package ch.nostromo.lib.swing;

import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import ch.nostromo.lib.swing.interfaces.Initializable;
import ch.nostromo.lib.swing.interfaces.ParentControl;

/**
 * The Class NosPanel.
 */
public abstract class NosPanel extends JPanel implements Initializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
   * Instantiates a new nos panel.
   */
    public NosPanel() {
    }

    /**
   * Instantiates a new nos panel.
   * 
   * @param layout the layout
   */
    public NosPanel(LayoutManager layout) {
        super(layout);
    }

    /**
   * Instantiates a new nos panel.
   * 
   * @param isDoubleBuffered the is double buffered
   */
    public NosPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    /**
   * Instantiates a new nos panel.
   * 
   * @param layout the layout
   * @param isDoubleBuffered the is double buffered
   */
    public NosPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    /**
   * Gets the nos parent control.
   * 
   * @return the nos parent control
   */
    private ParentControl getNosParentControl() {
        boolean follow = true;
        Component currentComponent = this;
        while (follow) {
            currentComponent = currentComponent.getParent();
            if (currentComponent == null) {
                return null;
            } else if (currentComponent instanceof ParentControl) {
                return (ParentControl) currentComponent;
            }
        }
        return null;
    }

    /**
   * Nos close.
   */
    protected void nosClose() {
        ParentControl npc = getNosParentControl();
        if (npc != null) {
            npc.nosClose();
        }
    }
}
