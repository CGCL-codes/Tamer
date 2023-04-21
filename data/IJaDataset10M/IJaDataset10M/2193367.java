package mp3.extras.configuration;

import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import mp3.extras.Config;

/**
 *
 * @author user
 */
public class ConfigComponentThreadDepth implements ConfigComponent<Integer> {

    private JLabel label = null;

    private JSpinner spiner = null;

    public ConfigComponentThreadDepth() {
    }

    private void init() {
        label = new JLabel(ResourceBundle.getBundle("Bundle").getString("ConfigGUI.jLabelProfundidad.text"));
        spiner = new JSpinner(new SpinnerNumberModel(Config.defaultSearchDepth, 0, 16, 1));
        reloadComponentInfo();
    }

    @Override
    public void reloadComponentInfo() {
        if (getFromConfig().intValue() < 0 || getFromConfig().intValue() > 16) spiner.setValue(Config.defaultSearchDepth); else spiner.setValue(getFromConfig());
    }

    @Override
    public String getTabName() {
        return ResourceBundle.getBundle("Bundle").getString("ConfigGUI.jPanelBusqueda.TabConstraints.tabTitle");
    }

    @Override
    public JLabel getLabelComponent() {
        if (label == null || spiner == null) init();
        return label;
    }

    @Override
    public JComponent getCustomComponent() {
        if (label == null || spiner == null) init();
        return spiner;
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
    public Integer getFromConfig() {
        try {
            Config conf = Config.getConfig();
            return new Integer(conf.get("MaximumDepth"));
        } catch (Exception ex) {
            return new Integer(Config.defaultSearchDepth);
        }
    }

    @Override
    public void saveToConfig(Config conf) {
        conf.set("MaximumDepth", ((Integer) spiner.getValue()).toString());
    }

    @Override
    public void setParent(Component parent) {
    }

    @Override
    public int getPreferedPosition() {
        return 2;
    }
}
