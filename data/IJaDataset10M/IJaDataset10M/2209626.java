package org.remus.infomngmnt.common.ui.richtext.actions;

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
public class SupAction extends Action implements Listener {

    private HtmlComposer composer = null;

    public SupAction(final HtmlComposer composer) {
        super("Superscript", IAction.AS_CHECK_BOX);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("de.spiritlink.richhtml4eclipse", "tiny_mce/jscripts/tiny_mce/themes/advanced/images/sup.gif"));
        this.composer = composer;
        this.composer.addListener(EventConstants.SUP, this);
    }

    @Override
    public void run() {
        this.composer.execute(JavaScriptCommands.SUP);
    }

    public void handleEvent(final Event event) {
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
