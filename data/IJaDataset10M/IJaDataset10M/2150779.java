package net.zehrer.vse.controller.action;

import net.zehrer.vse.common.IResource;
import net.zehrer.vse.common.IWindow;
import org.eclipse.jface.action.Action;

public class ExitAction extends Action {

    private IWindow window = null;

    public ExitAction() {
    }

    /**
	 * Set window context.
	 * 
	 * @param window
	 */
    public void setWindow(IWindow window) {
        this.window = window;
    }

    public void setResource(IResource resource) {
        setText(resource.getString("menu.File.Exit.text"));
        setToolTipText(resource.getString("menu.File.Exit.tooltip"));
    }

    @Override
    public void run() {
        window.close();
    }
}
