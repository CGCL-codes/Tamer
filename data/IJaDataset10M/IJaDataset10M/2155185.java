package org.jbuzz.awt.event;

/**
 * @author Lung-Kai Cheng (lkcheng@users.sourceforge.net)
 */
public class ActionEvent extends Event {

    String command;

    public ActionEvent(Object source, String command) {
        super(source);
        this.command = command;
    }

    public String getActionCommand() {
        return this.command;
    }
}
