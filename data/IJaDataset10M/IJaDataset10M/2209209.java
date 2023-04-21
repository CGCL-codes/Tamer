package net.sf.doolin.gui.swing;

import java.awt.FlowLayout;
import java.util.LinkedHashMap;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import net.sf.doolin.gui.action.support.SwingAction;
import org.apache.commons.lang.StringUtils;

/**
 * Bar of buttons
 * 
 * @author Damien Coraboeuf
 * 
 */
public class JButtonBar extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private final LinkedHashMap<String, JButton> buttons;

    /**
	 * Constructor
	 */
    public JButtonBar() {
        super(new FlowLayout(FlowLayout.TRAILING));
        this.buttons = new LinkedHashMap<String, JButton>();
    }

    /**
	 * Looks for a button with the given name
	 * 
	 * @param name
	 *            Button name
	 * @return Button or <code>null</code> if not found
	 */
    public JButton getButton(String name) {
        return this.buttons.get(name);
    }

    /**
	 * Adds a button to this bar using an action.
	 * 
	 * @param swingAction
	 *            Action to create the button from
	 * @return Created button
	 */
    public JButton add(Action swingAction) {
        String name = (String) swingAction.getValue(SwingAction.ID_KEY);
        JButton button = new JButton(swingAction);
        button.setName(name);
        add(button);
        if (StringUtils.isNotBlank(name)) {
            this.buttons.put(name, button);
            KeyStroke key = (KeyStroke) swingAction.getValue(Action.ACCELERATOR_KEY);
            if (key != null) {
                button.registerKeyboardAction(swingAction, key, JButton.WHEN_IN_FOCUSED_WINDOW);
            }
        }
        return button;
    }

    /**
	 * Removes all buttons from this bar
	 */
    public void clear() {
        this.buttons.clear();
        removeAll();
    }
}
