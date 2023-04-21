package com.ma.j2mesync.google.ui;

import com.ma.j2mesync.google.GooglePIMImporter;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 *
 * @author Marius Stroe
 */
public class GoogleResultsForm extends Form implements CommandListener {

    private final Command cmdImport = new Command("Import", Command.OK, 2);

    private final Command cmdBack = new Command("Back", Command.BACK, 1);

    GoogleSync midlet;

    Vector googleContacts;

    Displayable caller;

    boolean[] options;

    public GoogleResultsForm(GoogleSync midlet, Displayable caller, Vector googleContacts, boolean[] options) {
        super("Google Contacts");
        this.googleContacts = googleContacts;
        this.midlet = midlet;
        this.caller = caller;
        this.options = options;
        if (options[0]) {
            append("Pressing 'Import' will delete all your contacts on the mobile device and replace them with your google contacts!\n");
        } else {
            append("Pressing 'Import' will import your google contacts to your phone address book.\n");
        }
        append(googleContacts.size() + " google contacts found.");
        append("\n");
        System.gc();
        addCommand(cmdImport);
        addCommand(cmdBack);
        setCommandListener(this);
    }

    public void commandAction(Command command, final Displayable d) {
        if (command == cmdImport) {
            final Form form = new Form("Importing contacts...");
            Display.getDisplay(midlet).setCurrent(form);
            new Thread(new Runnable() {

                public void run() {
                    try {
                        ImportResultsForm importForm = new ImportResultsForm(midlet, d);
                        Display.getDisplay(midlet).setCurrent(importForm);
                        GooglePIMImporter pimImport = new GooglePIMImporter(googleContacts, importForm, options);
                        importForm.importContacts(pimImport);
                    } catch (Throwable e) {
                        midlet.reportException(e, caller);
                    }
                }
            }).start();
        } else {
            if (command == cmdBack) {
                Display.getDisplay(midlet).setCurrent(caller);
            }
        }
    }
}
