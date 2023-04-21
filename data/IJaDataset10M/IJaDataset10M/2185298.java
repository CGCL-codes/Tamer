package jmax.editors.patcher.menus;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.event.*;
import jmax.fts.*;
import jmax.mda.*;
import jmax.utils.*;
import jmax.editors.patcher.*;
import jmax.editors.patcher.actions.*;

/** Implement the patcher editor Format Menu */
public class FormatMenu extends PatcherMenu {

    ErmesSketchWindow editor;

    public FormatMenu(ErmesSketchWindow editor) {
        super("Format");
        this.editor = editor;
        setHorizontalTextPosition(AbstractButton.LEFT);
        add(new AlignMenu(editor));
        addSeparator();
    }
}
