package com.daffodilwoods.mail.action;

import com.daffodilwoods.mail.Main;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import javax.swing.KeyStroke;
import com.daffodilwoods.mail.frame.*;
import com.daffodilwoods.mail.gui.*;
import com.daffodilwoods.mail.tools.MailBO;
import java.sql.SQLException;

/**
 *
 * <p> </p>
 * <p> </p>
 * <p>Copyright: Copyright (c) 2004 Daffodil Software Ltd.</p>
 * <p>Company:  Daffodil Software Ltd.</p>
 * @author Alok Sarawat <alok.sarawat@daffodildb.com>
 * @version 1.0
 */
public class MarkAllReadAction extends AbstractAction {

    public MarkAllReadAction() {
        super("Mark all read");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('R', InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK, false));
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent event) {
        MessageComponent mc = Main.getInstance().getMainPanel().getMessageComponent();
        try {
            int[] id = mc.getAllMailID();
            if (id.length > 0) {
                MailBO.getInstance().changeStatusOfMail(id, 'T');
                mc.setAllRead();
            } else {
                Utility.showMessage(mc, "No mail in this folder", "", 1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Utility.showMessage(mc, ex.getMessage(), "SQL Error", 0);
        }
    }
}
