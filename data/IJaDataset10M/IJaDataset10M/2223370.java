package fMessenger.gui.actions;

import org.eclipse.jface.window.ApplicationWindow;

/**
 * @author oli
 */
public class NewServerAction extends AbstractAction {

    public NewServerAction(ApplicationWindow window) {
        super(window);
        setText("Neuer Server...@CTRL+N");
    }
}
