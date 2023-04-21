package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import com.leclercb.taskunifier.gui.actions.ActionCreateNewBackup;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationField;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.DefaultConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.fields.backup.AutoBackupEveryFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.backup.BackupListFieldType;
import com.leclercb.taskunifier.gui.components.configuration.fields.backup.KeepBackupsFieldType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.BackupUtils;

public class BackupConfigurationPanel extends DefaultConfigurationPanel {

    public BackupConfigurationPanel(ConfigurationGroup configuration) {
        super(configuration, "configuration_backup");
        this.initialize();
        this.pack();
    }

    private void initialize() {
        this.addField(new ConfigurationField("CREATE_NEW_BACKUP", null, new ConfigurationFieldType.Button(new ActionCreateNewBackup(22, 22))));
        this.addField(new ConfigurationField("SEPARATOR_1", null, new ConfigurationFieldType.Separator()));
        this.addField(new ConfigurationField("BACKUP_BEFORE_SYNC", Translations.getString("configuration.backup.backup_before_sync"), new ConfigurationFieldType.CheckBox(Main.getSettings(), "general.backup.backup_before_sync")));
        this.addField(new ConfigurationField("AUTO_BACKUP", Translations.getString("configuration.backup.auto_backup_every"), new AutoBackupEveryFieldType()));
        this.addField(new ConfigurationField("KEEP_BACKUPS", Translations.getString("configuration.backup.keep_backups"), new KeepBackupsFieldType()));
        this.addField(new ConfigurationField("SEPARATOR_2", null, new ConfigurationFieldType.Separator()));
        this.addField(new ConfigurationField("BACKUP_LIST", Translations.getString("configuration.backup.backup_list"), new BackupListFieldType()));
        final JComboBox backupList = (JComboBox) this.getField("BACKUP_LIST").getType().getFieldComponent();
        this.addField(new ConfigurationField("RESTORE_BACKUP", null, new ConfigurationFieldType.Button(Translations.getString("configuration.backup.restore_backup"), new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (backupList.getSelectedItem() == null) {
                    ErrorInfo info = new ErrorInfo(Translations.getString("general.error"), Translations.getString("error.no_backup_selected"), null, null, null, null, null);
                    JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
                    return;
                }
                BackupUtils.getInstance().createNewBackup();
                BackupUtils.getInstance().restoreBackup((String) backupList.getSelectedItem());
            }
        })));
    }
}
