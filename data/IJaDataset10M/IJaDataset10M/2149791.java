package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.BackupUtils;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionCreateNewBackup extends AbstractAction {

    public ActionCreateNewBackup() {
        this(32, 32);
    }

    public ActionCreateNewBackup(int width, int height) {
        super(Translations.getString("action.create_new_backup"), ImageUtils.getResourceImage("save.png", width, height));
        this.putValue(SHORT_DESCRIPTION, Translations.getString("action.create_new_backup"));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        ActionCreateNewBackup.createNewBackup(true);
    }

    public static boolean createNewBackup(boolean feedback) {
        boolean result = BackupUtils.getInstance().createNewBackup();
        if (!feedback) return result;
        if (result) {
            JOptionPane.showMessageDialog(MainFrame.getInstance().getFrame(), Translations.getString("action.create_new_backup.success"), Translations.getString("general.information"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            ErrorInfo info = new ErrorInfo(Translations.getString("general.error"), Translations.getString("action.create_new_backup.failure"), null, null, null, null, null);
            JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
        }
        return result;
    }
}
