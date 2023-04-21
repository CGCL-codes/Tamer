package com.leclercb.taskunifier.gui.commons.models;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.gui.utils.BackupUtils;

public class BackupModel extends DefaultComboBoxModel implements ListChangeListener {

    public BackupModel(boolean firstNull) {
        List<String> backups = BackupUtils.getInstance().getBackupList();
        if (firstNull) this.addElement(null);
        for (String backup : backups) this.addElement(backup);
        BackupUtils.getInstance().addListChangeListener(this);
    }

    @Override
    public void listChange(ListChangeEvent evt) {
        String backup = (String) evt.getValue();
        if (evt.getChangeType() == ListChangeEvent.VALUE_ADDED) this.addElement(backup); else if (evt.getChangeType() == ListChangeEvent.VALUE_REMOVED) this.removeElement(backup);
    }
}
