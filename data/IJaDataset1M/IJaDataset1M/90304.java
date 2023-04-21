package net.sf.jmp3renamer.plugins.DoubleFinder.gui;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import net.sf.jmp3renamer.I18N;
import net.sf.jmp3renamer.gui.PluginConfigGUI;
import net.sf.jmp3renamer.plugins.DoubleFinder.DoubleFinder;

public class DoubleFinderConfigPanel extends JPanel implements PluginConfigGUI {

    private JCheckBox cMD5 = new JCheckBox(I18N.translate("use_md5"));

    private JCheckBox cFilename = new JCheckBox(I18N.translate("use_filename"));

    private JCheckBox cTag = new JCheckBox(I18N.translate("use_tag"));

    private String name;

    public DoubleFinderConfigPanel(String name) {
        this.name = name;
        initGUI();
    }

    private void initGUI() {
        setBorder(BorderFactory.createTitledBorder("DoubleFinder"));
        setLayout(new VerticalFlowLayout(10, 10));
        add(cMD5);
        add(cFilename);
        add(cTag);
        cFilename.setText(I18N.translate("use_filename"));
    }

    public void loadProperties() {
        cMD5.setSelected(DoubleFinder.getProperty("use_md5").equals("true"));
        cFilename.setSelected(DoubleFinder.getProperty("use_filename").equals("true"));
        cTag.setSelected(DoubleFinder.getProperty("use_tag").equals("true"));
    }

    public void applyProperties() {
        DoubleFinder.setProperty("use_md5", Boolean.toString(cMD5.isSelected()));
        DoubleFinder.setProperty("use_filename", Boolean.toString(cFilename.isSelected()));
        DoubleFinder.setProperty("use_tag", Boolean.toString(cTag.isSelected()));
    }

    public String getDisplayName() {
        return name;
    }

    public String toString() {
        return getDisplayName();
    }
}
