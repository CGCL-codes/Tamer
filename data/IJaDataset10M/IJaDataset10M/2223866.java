package com.ma.j2mesync.google.ui;

import com.ma.j2mesync.google.GooglePIMImporter;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 *
 * @author Marius Stroe
 */
public class ImportResultsForm extends Form implements CommandListener {

    private final Command cmdExit = new Command("Exit", Command.EXIT, 2);

    private final Command cmdBack = new Command("Start Page", Command.BACK, 1);

    GoogleSync midlet;

    Displayable caller;

    public ImportResultsForm(GoogleSync midlet, Displayable caller) {
        super("Contacts Imported");
        this.midlet = midlet;
        this.caller = caller;
    }

    public void importContacts(GooglePIMImporter pimImport) {
        addCommand(cmdExit);
        addCommand(cmdBack);
        setCommandListener(this);
    }

    public void commandAction(Command command, final Displayable d) {
        if (command == cmdExit) {
            midlet.exit();
        } else if (command == cmdBack) {
            midlet.displayLoginForm();
        }
    }
}
