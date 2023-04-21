package mosinstaller.swing;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.ButtonGroup;
import mosinstaller.swing.MultiLineLabelUI;
import java.awt.GridBagLayout;

/**
 * @author PLAGNIOL-VILLARD Jean-Christophe
 * @version 1.00
 */
public class JLicenceWizardPanel extends AbstractJLicenceWizardPanel {

    public JLicenceWizardPanel() {
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(20, 30, 10, 30);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.gridwidth = 2;
        tilte.setUI(new MultiLineLabelUI());
        tilte.setFont(JMOSI.getDefaultFont());
        gbl.setConstraints(tilte, gbc);
        add(tilte, gbc);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 30, 5, 30);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        editor.setEditable(false);
        spLicence.getViewport().setView(editor);
        gbl.setConstraints(spLicence, gbc);
        add(spLicence, gbc);
        gbc.insets = new Insets(0, 30, 0, 30);
        agreeRadio.setFont(JMOSI.getDefaultFont());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbl.setConstraints(agreeRadio, gbc);
        add(agreeRadio);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbl.setConstraints(nextLicence, gbc);
        nextLicence.setFont(JMOSI.getDefaultFont());
        add(nextLicence);
        gbc.insets = new Insets(0, 30, 20, 30);
        disagreeRadio.setFont(JMOSI.getDefaultFont());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbl.setConstraints(disagreeRadio, gbc);
        add(disagreeRadio);
        ButtonGroup bg = new ButtonGroup();
        bg.add(agreeRadio);
        bg.add(disagreeRadio);
        disagreeRadio.setSelected(true);
        setNextScrollVisible(true);
    }

    public JLicenceWizardPanel(File f) throws MalformedURLException, IOException {
        this();
        setFile(f);
    }
}
