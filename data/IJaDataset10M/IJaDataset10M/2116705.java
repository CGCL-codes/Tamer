package ircam.jmax.editors.console;

import ircam.jmax.*;
import ircam.jmax.fts.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import tcl.lang.*;

public class Console extends JPanel {

    StringBuffer itsSbuf = new StringBuffer();

    ConsoleTextArea itsTextArea;

    Interp itsInterp;

    PrintStream itsPrintStream;

    ConsoleKeyListener itsKeyListener;

    KeyListener itsContainer;

    public Console(Interp i) {
        StringBuffer itsSbuf = new StringBuffer();
        itsTextArea = new ConsoleTextArea(40, 40);
        itsInterp = i;
        setLayout(new BorderLayout());
        add("Center", itsTextArea);
        itsKeyListener = new ConsoleKeyListener(this);
        itsTextArea.addKeyListener(itsKeyListener);
        itsTextArea.addMouseListener(new ConsoleMouseListener(this));
        itsPrintStream = new PrintStream(new ConsoleWriter(this));
    }

    void setContainer(KeyListener theContainer) {
        itsContainer = theContainer;
        itsTextArea.addKeyListener(itsContainer);
    }

    public PrintStream getPrintStream() {
        return itsPrintStream;
    }

    public ConsoleTextArea getTextArea() {
        return itsTextArea;
    }

    /**
     * Utility function that allows the insertion of text
     * in such a way that the next carriage return will EXECUTE it.
     * Example: copy and paste.
     */
    void putInKeyboardBuffer(String s) {
        itsKeyListener.sbuf.append(s);
        Put(s);
    }

    /**
     * Utility function that allows the insertion of text
     * in such a way that the next carriage return will NOT execute it.
     * (Example: system messages)
     */
    private void putLine(String s) {
        itsTextArea.append(s + "\n");
        itsKeyListener.intercept += s.length();
    }

    public void Put(String s) {
        itsTextArea.append(s);
        itsKeyListener.intercept += s.length();
    }

    public void lineReadyNotify(String s) {
        itsSbuf.append(s);
        processLine();
    }

    private void processLine() {
        String s = itsSbuf.toString();
        if (itsInterp.commandComplete(s)) {
            try {
                itsInterp.eval(s);
                String result = itsInterp.getResult().toString();
                if (result.length() > 0) {
                    putLine(result);
                }
            } catch (TclException e) {
                if (e.getCompletionCode() == TCL.RETURN) {
                    putLine(itsInterp.getResult().toString());
                } else if (e.getCompletionCode() == TCL.ERROR) {
                    putLine(itsInterp.getResult().toString());
                } else {
                    putLine("command returned bad code: " + e.getCompletionCode());
                }
            }
            itsSbuf.setLength(0);
            Put("% ");
        } else {
            Put("> ");
        }
    }
}
