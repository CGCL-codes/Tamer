package com.sdi.pws.gui.action;

import com.sdi.pws.db.PwsField;
import com.sdi.pws.db.PwsRecord;
import com.sdi.pws.gui.ClipboardMonitor;
import com.sdi.pws.gui.RecordSelector;
import com.sdi.pws.gui.GuiUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CopyUid extends AbstractAction {

    private RecordSelector selector;

    private ClipboardMonitor monitor;

    public CopyUid(RecordSelector aSelector, ClipboardMonitor aMonitor) {
        super(GuiUtil.getText("action.copyuid"));
        selector = aSelector;
        monitor = aMonitor;
        final ImageIcon lUIDIcon = new ImageIcon(CopyUid.class.getClassLoader().getResource("assets/uid.png"));
        putValue(SMALL_ICON, lUIDIcon);
        setEnabled(selector.isInfoAvailable());
        selector.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                CopyUid.this.setEnabled(selector.isInfoAvailable());
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (selector.isInfoAvailable()) {
            final PwsRecord lRecord = selector.getSelectedRecord();
            String lUid = null;
            if (lRecord.hasType(PwsField.FIELD_UID)) try {
                lUid = lRecord.get(PwsField.FIELD_UID).getAsString();
            } catch (Exception eIgnore) {
            }
            monitor.grabOwnership();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(lUid), monitor);
        }
    }
}
