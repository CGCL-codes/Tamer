package com.limegroup.gnutella.gui.menu;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Random;
import javax.swing.Action;
import javax.swing.Icon;
import org.limewire.util.OSUtils;
import com.limegroup.gnutella.gui.GUIMediator;
import com.limegroup.gnutella.gui.GUIUtils;
import com.limegroup.gnutella.gui.I18n;
import com.limegroup.gnutella.gui.IconManager;
import com.limegroup.gnutella.gui.TipOfTheDayMediator;
import com.limegroup.gnutella.gui.actions.AbstractAction;
import com.limegroup.gnutella.gui.actions.OpenLinkAction;
import com.limegroup.gnutella.gui.notify.Notification;
import com.limegroup.gnutella.gui.notify.NotifyUserProxy;
import com.limegroup.gnutella.util.LimeWireUtils;

final class HelpMenu extends AbstractMenu {

    /**
	 * Creates a new <tt>HelpMenu</tt>, using the <tt>key</tt> 
	 * argument for setting the locale-specific title and 
	 * accessibility text.
	 *
	 * @param key the key for locale-specific string resources unique
	 *            to the menu
	 */
    HelpMenu() {
        super(I18n.tr("&Help"));
        addMenuItem(new OpenLinkAction("http://www.frostwire.com/?id=support", I18n.tr("&Using FrostWire"), I18n.tr("Help for Using FrostWire")));
        addMenuItem(new ShowTipOfTheDayAction());
        addMenuItem(new OpenLinkAction("http://www.frostwire.com/?id=faq", I18n.tr("&FAQ"), I18n.tr("Frequently Asked Questions for FrostWire")));
        addMenuItem(new OpenLinkAction("http://www.frostwire.com/forum/viewforum.php?f=1", I18n.tr("Foru&m"), I18n.tr("Access the FrostWire Users\' Forum")));
        addMenuItem(new OpenLinkAction("http://www.frostclick.com/wp/?from=frostwire" + com.limegroup.gnutella.util.LimeWireUtils.getLimeWireVersion(), I18n.tr("FrostClick.com"), I18n.tr("Free Legal Downloads")));
        addMenuItem(new OpenLinkAction("http://www.frostwire.com/twitter", I18n.tr("Follow us on Twitter"), I18n.tr("Follow us on Twitter")));
        if (!OSUtils.isMacOSX()) {
            addSeparator();
            addMenuItem(new ShowAboutDialogAction());
        }
        if (LimeWireUtils.isTestingVersion()) {
            addSeparator();
            addMenuItem(new ShowNotificationAction());
            addMenuItem(new GenerateUncaughtErrorAction());
        }
    }

    /**
	 * Displays the TOTD window.
	 */
    private static class ShowTipOfTheDayAction extends AbstractAction {

        public ShowTipOfTheDayAction() {
            super(I18n.tr("Tip of the &Day"));
            putValue(LONG_DESCRIPTION, I18n.tr("Show the Tip of the Day Window"));
        }

        public void actionPerformed(ActionEvent e) {
            TipOfTheDayMediator.instance().displayTipWindow();
        }
    }

    /**
	 * Opens an error report, for testing.
	 */
    private static class GenerateUncaughtErrorAction extends AbstractAction {

        public GenerateUncaughtErrorAction() {
            super(I18n.tr("Generate &Error"));
            putValue(LONG_DESCRIPTION, I18n.tr("Generate a Popup Error for Testing"));
        }

        public void actionPerformed(ActionEvent e) {
            throw new RuntimeException("Generated Error");
        }
    }

    /**
	 * Shows the about window with more information about the application.
	 */
    private static class ShowAboutDialogAction extends AbstractAction {

        public ShowAboutDialogAction() {
            super(I18n.tr("&About FrostWire"));
            putValue(LONG_DESCRIPTION, I18n.tr("Information about FrostWire"));
        }

        public void actionPerformed(ActionEvent e) {
            GUIMediator.showAboutWindow();
        }
    }

    /**
     * Shows a notification.
     */
    private static class ShowNotificationAction extends AbstractAction {

        public ShowNotificationAction() {
            putValue(Action.NAME, "Show Notification");
        }

        public void actionPerformed(ActionEvent e) {
            if (new Random().nextBoolean()) {
                Icon icon = IconManager.instance().getIconForFile(new File("frostwire.exe"));
                Notification notification = new Notification("This is a very looooooooooooooooooooooooooooooooong message.", icon, this);
                NotifyUserProxy.instance().showMessage(notification);
            } else if (new Random().nextBoolean()) {
                Icon icon = IconManager.instance().getIconForFile(new File("frostwire.html"));
                Notification notification = new Notification("This is a another very loooong  loooong loooong loooong loooong loooong loooong loooong loooong message.", icon, this);
                NotifyUserProxy.instance().showMessage(notification);
            } else {
                Icon icon = IconManager.instance().getIconForFile(new File("frostwire.html"));
                Notification notification = new Notification("Short message.", icon, this);
                NotifyUserProxy.instance().showMessage(notification);
            }
        }
    }
}
