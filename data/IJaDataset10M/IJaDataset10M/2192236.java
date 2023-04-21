package net.sf.doolin.gui.action.path.statusbar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import net.sf.doolin.gui.action.ActionContext;
import net.sf.doolin.gui.action.GUIAction;
import org.springframework.beans.factory.annotation.Required;

/**
 * Status bar component that displays a {@link JLabel}, whose content is bound
 * to the application using a {@link LabelConnector}.
 * 
 * @author Damien Coraboeuf
 * 
 */
public class StatusBarLabel implements StatusBarComponent {

    private GUIAction action;

    private LabelConnector connector;

    /**
	 * Uses the {@link #setConnector(LabelConnector) connector} to bind the
	 * label test
	 * 
	 * @param actionContext
	 *            Action context to bind into
	 * @param label
	 *            Label to bind
	 * @see LabelConnector#connect(ActionContext, JLabel)
	 */
    protected void connect(ActionContext actionContext, JLabel label) {
        this.connector.connect(actionContext, label);
    }

    @Override
    public JComponent createComponent(final ActionContext actionContext) {
        JLabel label = new JLabel();
        connect(actionContext, label);
        if (this.action != null) {
            label.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        StatusBarLabel.this.action.execute(actionContext);
                    }
                }
            });
        }
        return label;
    }

    /**
	 * Gets the action to execute when the user clicks on the label.
	 * 
	 * @return Action or <code>null</code> if no action must be executed
	 */
    public GUIAction getAction() {
        return this.action;
    }

    /**
	 * Gets the connector for the label text
	 * 
	 * @return Label connector
	 */
    public LabelConnector getConnector() {
        return this.connector;
    }

    /**
	 * Sets the action to execute when the user clicks on the label.
	 * 
	 * @param action
	 *            Action or <code>null</code> if no action must be executed
	 */
    public void setAction(GUIAction action) {
        this.action = action;
    }

    /**
	 * Sets the connector for the label text
	 * 
	 * @param connector
	 *            Label connector
	 */
    @Required
    public void setConnector(LabelConnector connector) {
        this.connector = connector;
    }
}
