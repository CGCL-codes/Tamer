package org.makagiga.commons;

import static org.makagiga.commons.UI._;
import java.awt.SystemTray;
import org.makagiga.commons.swing.MButton;
import org.makagiga.commons.swing.MLinkButton;
import org.makagiga.commons.swing.MMenu;
import org.makagiga.commons.swing.MPanel;
import org.makagiga.commons.swing.MSettingsPage;
import org.makagiga.commons.swing.MSystemTray;
import org.makagiga.commons.swing.MText;
import org.makagiga.commons.swing.MTextField;
import org.makagiga.commons.swing.MTip;
import org.makagiga.commons.swing.MWrapperPanel;

/**
 * The "General" settings.
 * 
 * @since 3.0, 4.0 (org.makagiga.commons package)
 */
public class GeneralSettings extends MSettingsPage {

    private boolean oldSystemTray;

    private MTextField browserField;

    /**
	 * Constructs a "General" settings.
	 */
    public GeneralSettings() {
        super(_("General"));
    }

    @Override
    public MSettingsPage createAdvancedPage() {
        return new Advanced();
    }

    @Override
    protected void onClose() {
        super.onClose();
        browserField = null;
    }

    @Override
    protected void onInit() {
        bind(MApplication.confirmExit, _("Confirm Exit"));
        boolean systemTraySupported = SystemTray.isSupported();
        addSeparator(_("System Tray"));
        oldSystemTray = UI.systemTray.get();
        bind(UI.systemTray, _("Show System Tray Icon")).setEnabled(systemTraySupported);
        bind(UI.hideMainWindowInTray, _("Hide Main Window in Tray")).setEnabled(systemTraySupported);
        if (systemTraySupported) bind(UI.systemTray, "selected", UI.hideMainWindowInTray, "enabled");
        if (!OS.isWindows()) {
            addSeparator(_("Browser"));
            MPanel p = (MPanel) bind(OS.openCommand, _("Open Links/Files With:"));
            browserField = MWrapperPanel.getWrappedView(p);
            browserField.setAutoCompletion("browser");
            browserField.setColumns(25);
            UI.setHTMLHelp(browserField, _("Enter a command used to open external files<br>and web addresses.") + "<br>" + "<br>" + _("{0} - the link address", "<b>%u</b>"));
            MText.setUserMenu(browserField, new MText.UserMenu<MTextField>() {

                @Override
                public void onUserMenu(final MTextField textField, final MMenu menu) {
                    menu.addTitle(_("Command"));
                    menu.add(new SetOpenCommandAction(textField, "KDE", "kde-open"));
                    menu.add(new SetOpenCommandAction(textField, "GNOME", "gnome-open"));
                    menu.add(new SetOpenCommandAction(textField, "Xfce", "exo-open"));
                    menu.add(new SetOpenCommandAction(textField, "Firefox", "firefox"));
                    menu.add(new SetOpenCommandAction(textField, "Auto", "xdg-open"));
                    menu.add(new SetOpenCommandAction(textField, "Default", ""));
                }
            });
            MLinkButton testButton = new MLinkButton() {

                @Override
                protected void onClick() {
                    String oldLauncher = OS.openCommand.get();
                    try {
                        String newLauncher = GeneralSettings.this.browserField.getText();
                        OS.openCommand.set(newLauncher);
                        super.onClick();
                    } finally {
                        OS.openCommand.set(oldLauncher);
                    }
                }
            };
            testButton.setText(_("Test"));
            testButton.setURI(MApplication.getHomePage());
            p.addEast(testButton);
            addGap();
            MTip tip = new MTip(MTip.Direction.TOP);
            tip.setTipText(_("Right-click to display menu with predefined values."));
            add(tip);
        }
    }

    @Override
    protected void onOK() {
        if (browserField != null) browserField.saveAutoCompletion();
        if (!UI.systemTray.equalsValue(oldSystemTray)) {
            oldSystemTray = UI.systemTray.get();
            MSystemTray.setVisible(UI.systemTray.get());
        }
    }

    public static class Advanced extends MSettingsPage {

        public Advanced() {
            super(_("General"));
            add(new MButton(_("Enable All Tips")) {

                @Override
                protected void onClick() {
                    MTip.reset();
                    this.setEnabled(false);
                    this.setIcon(MIcon.small("ui/ok"));
                }
            });
        }
    }

    private static final class SetOpenCommandAction extends MText.InsertAction {

        public SetOpenCommandAction(final MTextField textField, final String name, final String command) {
            super(textField, name + " (" + command + ")", command);
        }
    }
}
