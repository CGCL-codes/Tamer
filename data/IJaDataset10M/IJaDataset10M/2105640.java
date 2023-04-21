package org.regilo.content.editor.pages.actions;

import java.util.Properties;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.regilo.content.RegiloContentImages;
import org.regilo.content.editor.widgets.AllActionConstants;
import org.regilo.content.editor.widgets.ComposerStatus;
import org.regilo.content.editor.widgets.EventConstants;
import org.regilo.content.editor.widgets.HtmlComposer;
import org.regilo.content.editor.widgets.JavaScriptCommands;
import org.regilo.content.editor.widgets.PropertyConstants;

/**
 * 
 * @author Tom Seidel <tom.seidel@spiritlink.de>
 * 
 */
public class UnderlineAction extends Action implements Listener {

    private HtmlComposer composer = null;

    public UnderlineAction(HtmlComposer composer) {
        super("", IAction.AS_CHECK_BOX);
        setImageDescriptor(RegiloContentImages.getInstance().DESC_TOOLBAR_UNDERLINE);
        this.composer = composer;
        this.composer.addListener(EventConstants.UNDERLINE, this);
    }

    @Override
    public void run() {
        this.composer.execute(JavaScriptCommands.UNDERLINE);
    }

    public void handleEvent(Event event) {
        Properties props = (Properties) event.data;
        if (ComposerStatus.SELECTED.equals(props.getProperty(PropertyConstants.STATUS))) {
            setChecked(true);
        } else if (ComposerStatus.NORMAL.equals(props.getProperty(PropertyConstants.STATUS))) {
            setChecked(false);
        } else if (event.type == EventConstants.ALL && AllActionConstants.RESET_ALL.equals(props.getProperty(PropertyConstants.COMMAND))) {
            setChecked(false);
        }
    }
}
