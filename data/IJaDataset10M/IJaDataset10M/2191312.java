package apollo.dataadapter.ensj.controller;

import java.awt.event.*;
import apollo.dataadapter.ensj.*;
import apollo.dataadapter.ensj.model.*;
import apollo.dataadapter.ensj.view.*;

public class KeyEventRouter extends EventRouter implements KeyListener {

    public KeyEventRouter(Controller handler, String key) {
        super(handler, key);
    }

    public void keyPressed(KeyEvent event) {
    }

    public void keyReleased(KeyEvent event) {
    }

    public void keyTyped(KeyEvent event) {
        getHandler().handleEventForKey(getKey());
    }
}
