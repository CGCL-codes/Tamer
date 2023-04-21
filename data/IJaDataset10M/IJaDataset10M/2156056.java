package sts.gui.actions;

import javax.swing.*;
import sts.gui.Main;
import sts.gui.bindings.*;
import sts.gui.contact.*;
import sts.hibernate.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *
 * @author ken
 */
public class EditContactAction extends AbstractAction implements ListSelectionListener {

    /** Singleton instance. */
    private static EditContactAction me;

    private ContactTable contactTable;

    /** Private constructor enforces singleton pattern. */
    private EditContactAction(ContactTable table) {
        this.contactTable = table;
        this.putValue(Action.NAME, "Edit Contact...");
    }

    /** Returns a reference to the only isntance of this class. */
    public static EditContactAction onlyInstance(ContactTable table) {
        if (me == null) me = new EditContactAction(table);
        return me;
    }

    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        setEnabled(contactTable.getSelectedRowCount() == 1);
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        Contact c = (Contact) contactTable.getRowModel().getRowAt(contactTable.getSelectedRow());
        ContactDialog d = new ContactDialog(ContactsFrame.onlyInstance(), c);
        d.init();
        kellinwood.meshi.form.FormUtils.setModalDialogVisible(d);
    }
}
