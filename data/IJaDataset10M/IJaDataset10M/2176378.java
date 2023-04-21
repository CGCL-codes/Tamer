package eu.keep.uphec.mainwindow.menubar.renderfile.emulation_settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import eu.keep.uphec.mainwindow.menubar.renderfile.ManualConfigurationDialog;

/**
 * The SaveRootButtonListener class defines the activities following the firing 
 * of the save_button button in a EditRootDialog instance
 * 
 *  @author Antonio Ciuffreda 
 */
public class SaveRootButtonListener implements ActionListener {

    private ManualConfigurationDialog manual_config_dialog;

    private EmulationConfigurationDialog manual_settings_config_dialog;

    private EditRootDialog edit_dialog;

    private int configuration_value;

    private Map<String, List<Map<String, String>>> config;

    private String root_config_file_value;

    private String root_digobj_value;

    private String root_config_dir_value;

    public SaveRootButtonListener(ManualConfigurationDialog manual_config_dialog, EmulationConfigurationDialog manual_settings_config_dialog, EditRootDialog edit_dialog) {
        this.manual_config_dialog = manual_config_dialog;
        this.manual_settings_config_dialog = manual_settings_config_dialog;
        this.edit_dialog = edit_dialog;
    }

    public void actionPerformed(ActionEvent e) {
        configuration_value = manual_settings_config_dialog.configuration_value;
        config = manual_settings_config_dialog.config;
        root_config_file_value = edit_dialog.root_config_file.getText();
        root_digobj_value = edit_dialog.root_digobj.getText();
        root_config_dir_value = edit_dialog.root_config_dir.getText();
        config.get("root").get(0).put("configFile", root_config_file_value);
        config.get("root").get(0).put("digobj", root_digobj_value);
        config.get("root").get(0).put("configDir", root_config_dir_value);
        try {
            manual_config_dialog.model.setEmuConfig(config, configuration_value);
            manual_settings_config_dialog.root_config.setText(root_config_file_value);
            manual_settings_config_dialog.root_digobj.setText(root_digobj_value);
            manual_settings_config_dialog.root_config_dir.setText(root_config_dir_value);
            manual_settings_config_dialog.validate();
            manual_settings_config_dialog.repaint();
            edit_dialog.dialog.dispose();
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(manual_config_dialog.internal_frame.getRootPane(), "A I/O error occurred, the application could not save the edited information.");
        }
    }
}
