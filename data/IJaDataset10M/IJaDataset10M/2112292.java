package com.smssalama.gui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import com.smssalama.AlertManager;
import com.smssalama.ImageResources;
import com.smssalama.SMSSalama;
import com.smssalama.i18n.CommandLabels;
import com.smssalama.i18n.Labels;
import com.smssalama.i18n.ScreenTitles;
import com.smssalama.storage.sms.SMSMessageFilter;

/**
 * Displays the initial menu after sign in.
 * 
 * @author Arnold P. Minde
 */
public class Home extends List implements CommandListener {

    private static Home homeScreen;

    private int MESSAGES_ITEM = 0;

    private int CONTACTS_ITEM = 0;

    private int SETTINGS_ITEM = 0;

    private int HELP_ITEM = 0;

    private Messages messages = null;

    private Contacts contacts = null;

    private Settings settings = null;

    private Help help = null;

    public Home() {
        super(ScreenTitles.screenTitles().home(), Choice.IMPLICIT);
        final CommandLabels CMD_LABELS = CommandLabels.cmdLabels();
        final ScreenTitles SCREEN_TITLES = ScreenTitles.screenTitles();
        this.MESSAGES_ITEM = this.append(SCREEN_TITLES.messages(), ImageResources.getImage(ImageResources.IMG_MESSAGES));
        this.CONTACTS_ITEM = this.append(SCREEN_TITLES.contacts(), ImageResources.getImage(ImageResources.IMG_CONTACTS));
        this.SETTINGS_ITEM = this.append(SCREEN_TITLES.settings(), ImageResources.getImage(ImageResources.IMG_SETTINGS));
        this.HELP_ITEM = this.append(SCREEN_TITLES.help(), ImageResources.getImage(ImageResources.IMG_HELP));
        setCommandListener(this);
        Command exitCmd = new Command(CMD_LABELS.exit(), Command.EXIT, 1);
        Command openCmd = new Command(CMD_LABELS.open(), Command.ITEM, 1);
        this.setSelectCommand(openCmd);
        addCommand(exitCmd);
    }

    public static synchronized Home getHomeScreen() {
        if (Home.homeScreen == null) {
            Home.homeScreen = new Home();
        }
        return Home.homeScreen;
    }

    public static void setHomeScreen(Home homeScreen) {
        Home.homeScreen = homeScreen;
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command.getCommandType() == Command.EXIT) {
            SMSSalama.quitApp();
        } else if (command.getCommandType() == Command.ITEM) {
            if (this.getSelectedIndex() == this.MESSAGES_ITEM) {
                this.messages = new Messages(this, Labels.labels().messages(), SMSMessageFilter.PASSTHROUGH);
                AlertManager.showDisplayable(this.messages);
            } else if (this.getSelectedIndex() == this.CONTACTS_ITEM) {
                this.contacts = new Contacts();
                AlertManager.showDisplayable(this.contacts);
            } else if (this.getSelectedIndex() == this.SETTINGS_ITEM) {
                if (this.settings == null) {
                    this.settings = new Settings();
                }
                AlertManager.showDisplayable(this.settings);
            } else if (this.getSelectedIndex() == this.HELP_ITEM) {
                if (this.help == null) {
                    this.help = new Help();
                }
                AlertManager.showDisplayable(this.help);
            }
        }
    }
}
