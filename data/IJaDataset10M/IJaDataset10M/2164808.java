package fildiv.jremcntl.client.core;

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;

public class JRemForm extends Form {

    private Vector commands;

    public JRemForm(String name) {
        super(name);
        commands = new Vector();
    }

    public void addCommand(Command c) {
        super.addCommand(c);
        if (existsCommand(c)) return;
        commands.addElement(c);
    }

    private boolean existsCommand(Command c) {
        for (int index = 0; index < commands.size(); ++index) {
            Command cmd = (Command) commands.elementAt(index);
            if (cmd.equals(c)) return true;
        }
        return false;
    }

    public void removeCommand(Command c) {
        super.removeCommand(c);
        commands.removeElement(c);
    }

    public Vector getCommands() {
        return commands;
    }

    public void cancelCommands() {
        for (int index = 0; index < commands.size(); ++index) {
            Command c = (Command) commands.elementAt(index);
            super.removeCommand(c);
        }
    }

    public void restoreCommands() {
        for (int index = 0; index < commands.size(); ++index) {
            Command c = (Command) commands.elementAt(index);
            super.addCommand(c);
        }
    }
}
