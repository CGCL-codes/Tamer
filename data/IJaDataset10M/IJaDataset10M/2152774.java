package jmax.editors.patcher.actions;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import jmax.commons.*;
import jmax.editors.patcher.*;

public class PasteAction extends MenuAction {

    public PasteAction(ErmesSketchWindow editor) {
        super(editor);
    }

    public void doMenuAction(ErmesSketchWindow editor) throws MaxError {
        editor.Paste();
    }
}
