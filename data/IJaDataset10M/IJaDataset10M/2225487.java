package com.daffodilwoods.mail.action;

import java.awt.Event;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.daffodilwoods.mail.gui.DTable;
import javax.swing.event.CaretListener;
import com.daffodilwoods.mail.frame.*;

/**
 *
 * <p> </p>
 * <p> </p>
 * <p>Copyright: Copyright (c) 2004 Daffodil Software Ltd.</p>
 * <p>Company:  Daffodil Software Ltd.</p>
 * @author Alok Sarawat <alok.sarawat@daffodildb.com>
 * @version 1.0
 */
public class CopyAction extends AbstractAction implements CaretListener {

    public CopyAction() {
        super("copy");
        putValue(Action.SHORT_DESCRIPTION, "Copy selected text");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('C', Event.CTRL_MASK, false));
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent event) {
        JEditorPane ep = MainPanel.getInstance().getMessageComponent().getEditorPane();
        if (ep.isShowing()) ep.copy();
    }

    /**
   * caretUpdate
   *
   * @param e CaretEvent
   */
    public void caretUpdate(CaretEvent e) {
        int dot = e.getDot();
        int mark = e.getMark();
        if (dot == mark) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }
    }
}
