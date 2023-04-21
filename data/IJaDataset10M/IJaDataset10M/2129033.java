package de.spiritlink.richhtml4eclipse.example.actions;

import java.util.Properties;
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
import de.spiritlink.richhtml4eclipse.widgets.PropertyConstants;

/**
 * 
 * @author Tom Seidel <tom.seidel@spiritlink.de>
 * 
 */
public class InsertColumnAfterAction extends Action implements Listener {

    private HtmlComposer composer = null;

    public InsertColumnAfterAction(HtmlComposer composer) {
        super("", IAction.AS_PUSH_BUTTON);
        setImageDescriptor(ImageHelper.getTinyMceImage("table_insert_col_after.gif"));
        this.composer = composer;
        this.composer.addListener(EventConstants.INSERT_COLUMN_AFTER, this);
    }

    @Override
    public void run() {
        this.composer.execute(JavaScriptCommands.INSERT_COLUMN_AFTER);
    }

    public void handleEvent(Event event) {
        Properties props = (Properties) event.data;
        if (ComposerStatus.NORMAL.equals(props.getProperty(PropertyConstants.STATUS))) {
            setEnabled(true);
        } else if (ComposerStatus.DISABLED.equals(props.getProperty(PropertyConstants.STATUS))) {
            setEnabled(false);
        } else if (event.type == EventConstants.ALL && AllActionConstants.RESET_ALL.equals(props.getProperty(PropertyConstants.COMMAND))) {
            setEnabled(false);
        }
    }
}
