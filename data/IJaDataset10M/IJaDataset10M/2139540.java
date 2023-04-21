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
public class BulletListAction extends Action implements Listener {

    private HtmlComposer composer = null;

    public BulletListAction(final HtmlComposer composer) {
        super("", IAction.AS_CHECK_BOX);
        setToolTipText("Bullet List");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("de.spiritlink.richhtml4eclipse", "tiny_mce/jscripts/tiny_mce/themes/advanced/images/bullist.gif"));
        this.composer = composer;
        this.composer.addListener(EventConstants.BULLIST, this);
    }

    @Override
    public void run() {
        this.composer.execute(JavaScriptCommands.BULLIST);
    }

    public void handleEvent(final Event event) {
        Properties evtProps = (Properties) event.data;
        if (event.type != EventConstants.ALL) {
            setChecked(ComposerStatus.SELECTED.equals(evtProps.getProperty(PropertyConstants.STATUS)));
        } else if (evtProps.getProperty(PropertyConstants.COMMAND).equals(AllActionConstants.RESET_ALL)) {
            setChecked(false);
        }
    }
}
