package meteor.gui.configuration;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import meteor.dao.VideoAnalysisDAO;
import meteor.gui.ImageTools;
import meteor.gui.MeteorGUI;
import meteor.gui.SpringUtilities;
import meteor.model.videoanalysis.PluginModel;
import meteor.model.videoanalysis.PluginTypeModel;

public class PluginDialog extends JDialog {

    private PluginModel pluginModel;

    private JComboBox pluginTypeCombo;

    private JTextField pluginNameField;

    private VideoAnalysisDAO videoAnalysisDAO;

    private boolean okPressed = false;

    public PluginDialog(String title, PluginModel pluginModel, VideoAnalysisDAO videoAnalysisDAO) {
        super(MeteorGUI.getInstance(), title, true);
        this.pluginModel = pluginModel;
        this.videoAnalysisDAO = videoAnalysisDAO;
        initGUI();
    }

    private void initGUI() {
        JPanel pluginPanel = new JPanel(new SpringLayout());
        pluginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        JLabel pluginTypeLabel = new JLabel("Plugin type:");
        pluginTypeCombo = new JComboBox();
        JLabel pluginNameLabel = new JLabel("Plugin name:");
        pluginNameField = new JTextField(20);
        for (PluginTypeModel ptm : videoAnalysisDAO.getPluginTypes()) {
            pluginTypeCombo.addItem(ptm);
        }
        pluginPanel.add(pluginTypeLabel);
        pluginPanel.add(pluginTypeCombo);
        pluginPanel.add(pluginNameLabel);
        pluginPanel.add(pluginNameField);
        SpringUtilities.makeCompactGrid(pluginPanel, 2, 2, 0, 0, 5, 5);
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("OK", ImageTools.createImageIcon("ok16.png"));
        JButton cancelButton = new JButton("Cancel", ImageTools.createImageIcon("cancel16.png"));
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pluginModel = new PluginModel((PluginTypeModel) pluginTypeCombo.getSelectedItem(), pluginNameField.getText());
                okPressed = true;
                PluginDialog.this.setVisible(false);
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PluginDialog.this.setVisible(false);
            }
        });
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        add(pluginPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);
        setLocationByPlatform(true);
        pack();
        setVisible(true);
    }

    public PluginModel getPluginModel() {
        return pluginModel;
    }

    public boolean isOkPressed() {
        return okPressed;
    }
}
