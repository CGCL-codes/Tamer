package ircam.jmax.editors.explode.actions;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import ircam.jmax.*;
import ircam.jmax.editors.explode.*;
import ircam.jmax.toolkit.*;
import ircam.jmax.toolkit.actions.*;

public class UndoAction extends EditorAction {

    public void doAction(EditorContainer container) {
        ((ExplodePanel) container.getEditor()).Undo();
    }
}
