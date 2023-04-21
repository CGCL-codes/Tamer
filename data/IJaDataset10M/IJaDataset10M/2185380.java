package be.vds.jtbdive.core.view.wizards.install.comm;

import info.clearthought.layout.TableLayout;
import java.awt.Component;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import org.netbeans.spi.wizard.WizardPage;
import be.vds.jtbdive.core.utils.LanguageManager;
import be.vds.jtbdive.core.utils.PropertiesManager;
import be.vds.jtbdive.core.utils.SystemConstants;
import be.vds.jtbdive.core.view.component.FileSelectionComponent;

public class InstallCommDetailWizardPage extends WizardPage {

    private JComboBox osCb;

    public InstallCommDetailWizardPage() {
        init();
    }

    private void init() {
        double[] cols = { 5, TableLayout.MINIMUM, 5, TableLayout.PREFERRED, 5 };
        double[] rows = { 5, TableLayout.PREFERRED, 5, TableLayout.PREFERRED, TableLayout.FILL, 5 };
        TableLayout tl = new TableLayout(cols, rows);
        setLayout(tl);
        this.add(createSystemLabel(), "1, 1");
        this.add(createJREPathLabel(), "1, 3");
        this.add(createSystemComponent(), "3, 1");
        this.add(createJREPathComponent(), "3, 3");
    }

    private Component createJREPathComponent() {
        FileSelectionComponent fsc = new FileSelectionComponent();
        String s = PropertiesManager.getJREUsed();
        fsc.getFileTf().setName("jre.path");
        fsc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fsc.setFile(new File(s));
        return fsc;
    }

    private Component createJREPathLabel() {
        JLabel label = new JLabel(LanguageManager.getKey("jre.used"));
        return label;
    }

    private Component createSystemComponent() {
        osCb = new JComboBox(SystemConstants.getOsTypes().toArray());
        osCb.setSelectedIndex(-1);
        osCb.setName("os.type");
        String s = PropertiesManager.getOSType();
        if (null != s) {
            osCb.setSelectedItem(s);
        }
        return osCb;
    }

    private Component createSystemLabel() {
        JLabel label = new JLabel(LanguageManager.getKey("os.type"));
        return label;
    }

    public static final String getDescription() {
        return LanguageManager.getKey("install.com");
    }

    @Override
    protected String validateContents(Component component, Object event) {
        if (osCb.getSelectedIndex() <= -1) {
            return LanguageManager.getKey("choose.os.type");
        }
        return null;
    }
}
