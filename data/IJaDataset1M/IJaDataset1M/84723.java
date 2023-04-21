package org.tolven.mobile.client.view;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import org.tolven.mobile.client.comm.Session;

public abstract class ModalPage implements CommandListener {

    Displayable previous;

    Display display;

    Form form;

    Session session;

    Command cmdBack = new Command("Back", Command.BACK, 2);

    public ModalPage(String formName, Display display, Session session) {
        this.session = session;
        this.display = display;
        previous = display.getCurrent();
        form = new Form(formName);
        form.addCommand(cmdBack);
        form.setCommandListener(this);
        buildForm(form);
        display.setCurrent(form);
    }

    protected abstract void buildForm(Form form);

    public void commandAction(Command command, Displayable displayable) {
        if (command == cmdBack) {
            display.setCurrent(previous);
        }
    }
}
