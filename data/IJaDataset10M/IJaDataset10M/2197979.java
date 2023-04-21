package mp3.extras.configuration;

import java.awt.Component;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import mp3.extras.Config;
import mp3.extras.Utilidades;

/**
 *
 * @author user
 */
public class ConfigComponentIconSet implements ConfigComponent<String> {

    private JLabel label = null;

    private JComboBox combo = null;

    public ConfigComponentIconSet() {
    }

    private void init() {
        label = new JLabel(java.util.ResourceBundle.getBundle("Bundle").getString("ConfigGUI.jLabelIcons.text"));
        combo = new JComboBox(new DefaultComboBoxModel());
        reloadComponentInfo();
    }

    @Override
    public void reloadComponentInfo() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) combo.getModel();
        model.removeAllElements();
        model.addElement("default");
        File iconDir = new File(Utilidades.getExecutionFolder() + File.separator + "icons");
        if (!iconDir.exists()) return;
        for (File dir : iconDir.listFiles()) {
            if (dir.isDirectory()) {
                model.addElement(dir.getName());
            }
        }
        combo.setSelectedItem(getFromConfig());
    }

    @Override
    public String getTabName() {
        return ResourceBundle.getBundle("Bundle").getString("ConfigGUI.jPanelThemes.TabConstraints.tabTitle");
    }

    @Override
    public JLabel getLabelComponent() {
        if (label == null || combo == null) init();
        return label;
    }

    @Override
    public JComponent getCustomComponent() {
        if (label == null || combo == null) init();
        return combo;
    }

    @Override
    public boolean isComplexComponent() {
        return false;
    }

    @Override
    public JComponent getComplexComponent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setParent(Component parent) {
    }

    @Override
    public String getFromConfig() {
        try {
            return Config.getConfig().get("IconSet");
        } catch (Exception ex) {
            return Config.defaultIconSet;
        }
    }

    @Override
    public void saveToConfig(Config conf) {
        conf.set("IconSet", combo.getSelectedItem().toString());
    }

    @Override
    public int getPreferedPosition() {
        return 4;
    }
}
