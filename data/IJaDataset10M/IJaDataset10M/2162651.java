package de.spiritlink.richhtml4eclipse.example.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import de.spiritlink.richhtml4eclipse.widgets.AllActionConstants;
import de.spiritlink.richhtml4eclipse.widgets.ComposerStatus;
import de.spiritlink.richhtml4eclipse.widgets.EventConstants;
import de.spiritlink.richhtml4eclipse.widgets.HtmlComposer;
import de.spiritlink.richhtml4eclipse.widgets.JavaScriptCommands;

/**
 * 
 * @author Tom Seidel <tom.seidel@spiritlink.de>
 * 
 */
public class CleanupAction extends Action {

    private HtmlComposer composer = null;

    public CleanupAction(HtmlComposer composer) {
        super("", IAction.AS_CHECK_BOX);
        setImageDescriptor(ImageHelper.getTinyMceImage("cleanup.gif"));
        this.composer = composer;
    }

    @Override
    public void run() {
        this.composer.execute(JavaScriptCommands.CLEANUP);
    }
}
