package it.secondlifelab.p2pSL.jInterface;

import javax.swing.JTextArea;
import it.secondlifelab.p2pSL.jserver.Debug;

/**
 * @author hachreak
 *
 * visualize the debug information
 */
public class VisualDebug implements Debug {

    private JTextArea jtextarea;

    public VisualDebug(JTextArea jtextarea) {
        this.jtextarea = jtextarea;
    }

    public void print(String text) {
        jtextarea.setText(jtextarea.getText() + text + "\n");
    }

    public void enable(boolean enable) {
    }

    public void isEnabled() {
    }
}
