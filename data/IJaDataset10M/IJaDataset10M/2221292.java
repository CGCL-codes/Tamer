package com.organic.maynard.outliner.scripting.script;

import com.organic.maynard.outliner.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class ScriptsButtonCellEditor extends ButtonCellEditor {

    public ScriptsButtonCellEditor() {
        super(new JCheckBox());
    }

    protected void doEditing() {
        if (this.col == 0) {
            ScriptsTable.runScript(this.row);
        } else if (this.col == 1) {
            ScriptsTable.updateScript(this.row);
        }
    }
}
