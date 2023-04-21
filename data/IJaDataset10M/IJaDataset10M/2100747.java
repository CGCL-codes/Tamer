package util.jterm.taimp;

import util.jterm.ITextArea;
import java.awt.TextArea;
import java.awt.Color;

/**
 * An AWT based implementation of ITextArea.
 *
 * @author Carlos Rueda
 * @version 0.1 Jan/13/2000
 * @version 0.2 2000-01-17
 */
public class AWTTextArea extends TextArea implements ITextArea {

    public AWTTextArea() {
        super();
        setBackground(Color.black);
        setForeground(Color.lightGray);
    }

    public void copy() {
    }

    public void cut() {
    }

    public void paste() {
    }
}
