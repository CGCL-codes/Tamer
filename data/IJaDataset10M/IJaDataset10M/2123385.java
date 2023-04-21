package fMessenger.gui.actions;

import org.eclipse.jface.window.ApplicationWindow;

/**
 * @author oli
 */
public class DisconnectAction extends AbstractAction {

    public DisconnectAction(ApplicationWindow window) {
        super(window);
        setText("Trennen@SHIFT+CTRL+S");
    }
}
