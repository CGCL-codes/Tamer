package edu.princeton.wordnet.wnscope.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Show a component with a label above it.
 * 
 * @author <a href="bbou@ac-toulouse.fr">Bernard Bou</a>
 */
public class LabelledComponent extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
	 * Label
	 */
    private final JLabel theLabel;

    /**
	 * Wrapped component
	 */
    protected Component theComponent;

    /**
	 * Constructor
	 * 
	 * @param thisLabel
	 *            label
	 * @param thisComponent
	 *            text component
	 */
    public LabelledComponent(final String thisLabel, final Component thisComponent) {
        this.theLabel = new JLabel(thisLabel);
        this.theComponent = thisComponent;
        initialize();
    }

    /**
	 * Initialize
	 */
    protected void initialize() {
        setLayout(new BorderLayout());
        add(this.theLabel, BorderLayout.NORTH);
        add(wrapComponent(this.theComponent), BorderLayout.CENTER);
    }

    /**
	 * Overridden when embedded component undergoes further wrapping
	 * 
	 * @param thisComponent
	 * @return wrapped component
	 */
    protected Component wrapComponent(final Component thisComponent) {
        return thisComponent;
    }

    /**
	 * Get embedded component
	 * 
	 * @return embedded component
	 */
    public Component getComponent() {
        return this.theComponent;
    }
}
