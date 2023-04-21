package ui.swing.actions;

import java.awt.event.ActionEvent;
import ui.swing.Desktop;

/**
 * The action that shows the jvdrums website.
 */
@SuppressWarnings("serial")
public final class WebsiteAction extends BaseAction {

    public WebsiteAction() {
        config.get("website").read(this);
        setEnabled(Desktop.isSupported());
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Desktop.browse("http://jvdrums.sourceforge.net");
    }
}
