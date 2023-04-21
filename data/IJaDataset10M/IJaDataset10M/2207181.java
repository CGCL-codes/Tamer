package org.dicom4j.apps.commons.ui.setting;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import org.dicom4j.apps.commons.application.ApplicationConfiguration;
import org.dicom4j.apps.commons.ui.IconLibrary;
import org.dicom4j.apps.commons.ui.panel.SettingsPanel;
import org.dolmen.swing.dialogs.SaveCoseDialog;
import org.dolmen.swing.icons.DolmenIconLibrary;
import org.dolmen.swing.tables.models.PropertiesTableModel;
import org.dicom4j.apps.commons.application.I18n;
import org.dicom4j.apps.commons.application.I18nUpdateListener;
import org.dicom4j.apps.commons.application.I18nListeners;

/**
 * Dialog frame to manage application's settings
 *
 * @since 0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public class SettingsDialog extends SaveCoseDialog {

    private static final long serialVersionUID = 1L;

    private SettingsPanel settingsPanel = new SettingsPanel();

    private ApplicationSettingsPanel applicationSettingsPanel = new ApplicationSettingsPanel(this);

    private NetworkSettingsPanel networkSettingsPanel = new NetworkSettingsPanel();

    private DevicesSettingsPanel devicesSettingsPanel = new DevicesSettingsPanel();

    protected JTable table;

    private PropertiesTableModel propertiesTableModel;

    public JLabel statusLabel = new JLabel();

    private JButton saveButton;

    private JButton closeButton = new JButton(I18n.getTrans("close"), DolmenIconLibrary.CLOSE_16);

    private JButton applyButton;

    private JButton applicationCategorieJButton = new JButton(IconLibrary.settings_64);

    private JButton networkCategorieJButton = new JButton(IconLibrary.connect_to_network_64);

    private JButton devicesButton = new JButton(IconLibrary.nodes_64);

    JPanel center = new JPanel(new BorderLayout());

    JPanel contentPanel = new JPanel(new BorderLayout());

    private ApplicationConfiguration configuration;

    private StringBuilder errMsg = new StringBuilder();

    /**
	 * @return the configuration.
	 */
    public ApplicationConfiguration getConfiguration() {
        return configuration;
    }

    /**
	 * @param configuration The configuration to set.
	 */
    public void setConfiguration(ApplicationConfiguration configuration) {
        this.configuration = configuration;
        networkSettingsPanel.setUp(this.configuration);
        applicationSettingsPanel.loadValues(this.configuration);
    }

    public SettingsDialog(JFrame parent) {
        super(parent);
        setBounds(0, 0, 800, 400);
        setLayout(new BorderLayout());
        JToolBar categoriePanel = new JToolBar();
        categoriePanel.setFloatable(false);
        applicationCategorieJButton.setToolTipText(I18n.getTrans("applicationSettings"));
        applicationCategorieJButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                hideShowPanel(applicationSettingsPanel);
            }
        });
        categoriePanel.add(applicationCategorieJButton);
        networkCategorieJButton.setToolTipText(I18n.getTrans("networkSettings"));
        networkCategorieJButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                hideShowPanel(networkSettingsPanel);
            }
        });
        categoriePanel.add(networkCategorieJButton);
        devicesButton.setVisible(true);
        devicesButton.setToolTipText(I18n.getTrans("devicesSettings"));
        devicesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                hideShowPanel(devicesSettingsPanel);
            }
        });
        categoriePanel.add(devicesButton);
        propertiesTableModel = new PropertiesTableModel();
        table = new JTable(propertiesTableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        center.add(categoriePanel, BorderLayout.NORTH);
        center.add(contentPanel, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel();
        applyButton = new JButton(I18n.getTrans("apply"), DolmenIconLibrary.APPLY_16);
        buttonsPanel.add(applyButton);
        saveButton = new JButton(I18n.getTrans("save"), DolmenIconLibrary.SAVE_16);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(closeButton);
        JPanel south = new JPanel();
        south.setLayout(new BorderLayout());
        south.add(buttonsPanel, BorderLayout.CENTER);
        south.add(statusLabel, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
        applicationCategorieJButton.doClick();
        I18nListeners.newListen.addI18nListener(new I18nUpdateListener() {

            public void update() {
                closeButton.setText(I18n.getTrans("close"));
                applicationCategorieJButton.setToolTipText(I18n.getTrans("applicationSettings"));
                networkCategorieJButton.setToolTipText(I18n.getTrans("networkSettings"));
                devicesButton.setToolTipText(I18n.getTrans("devicesSettings"));
                applyButton.setText(I18n.getTrans("apply"));
                saveButton.setText(I18n.getTrans("save"));
            }
        });
    }

    /**
	 * @return Returns the closeButton.
	 */
    public JButton getCloseButton() {
        return closeButton;
    }

    public String getErrorMessage() {
        return errMsg.toString();
    }

    public boolean applyChanges() {
        boolean success = true;
        errMsg.delete(0, errMsg.length());
        if (!this.applicationSettingsPanel.commitSettings()) {
            success = false;
            errMsg.append(applicationSettingsPanel.getErrorMessage());
        }
        if (!this.networkSettingsPanel.commitSettings()) {
            success = false;
            errMsg.append("\n");
            errMsg.append(networkSettingsPanel.getErrorMessage());
        }
        return success;
    }

    public void setApplyAction(ActionListener aSaveAction) {
        applyButton.addActionListener(aSaveAction);
        applyButton.setVisible(true);
    }

    public void setSaveAction(ActionListener aSaveAction) {
        saveButton.addActionListener(aSaveAction);
        saveButton.setVisible(true);
    }

    public SettingsPanel getPanel() {
        return this.settingsPanel;
    }

    public void setProperties(Properties properties) {
        this.propertiesTableModel.setProperties(properties);
    }

    public void hideShowPanel(JPanel panelToShow) {
        panelToShow.setVisible(true);
        contentPanel.removeAll();
        contentPanel.add(panelToShow, BorderLayout.CENTER);
        contentPanel.revalidate();
        center.revalidate();
        repaint();
    }
}
