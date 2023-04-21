package widget;

import java.awt.event.*;
import dscript.connect.*;

public class DKeyListener extends Dustyable implements KeyListener {

    public void keyPressed(KeyEvent ke) {
        processKeyEvent(ke, 'P');
    }

    public void keyTyped(KeyEvent ke) {
        processKeyEvent(ke, 'T');
    }

    public void keyReleased(KeyEvent ke) {
        processKeyEvent(ke, 'R');
    }

    public void processKeyEvent(KeyEvent ke, char event) {
    }
}
