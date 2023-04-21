package com.cosylab.vdct.events.commands;

import com.cosylab.vdct.events.Command;
import javax.swing.JComponent;

/**
 * Insert the type's description here.
 * Creation date: (21.12.2000 22:42:23)
 * @author Matej Sekoranja
 */
public class NullCommand extends Command {

    private JComponent component;

    /**
 * Insert the method's description here.
 * Creation date: (21.12.2000 22:43:26)
 * @param component javax.swing.JComponent
 */
    public NullCommand(JComponent component) {
        this.component = component;
    }

    /**
 * Insert the method's description here.
 * Creation date: (21.12.2000 22:42:23)
 */
    public void execute() {
    }

    /**
 * Insert the method's description here.
 * Creation date: (25.12.2000 17:33:51)
 * @return javax.swing.JComponent
 */
    public JComponent getComponent() {
        return component;
    }
}
