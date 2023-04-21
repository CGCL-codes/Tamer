package net.face2face.plugins.news;

import java.util.*;
import javax.swing.text.*;
import net.face2face.core.plugins.ServicePluginEditorPanel;
import net.face2face.core.services.Service;

/**
 *
 * @author  Patrice
 */
public class NewsEditorPanel extends ServicePluginEditorPanel {

    /** Creates new form SearchServicesPanel */
    public NewsEditorPanel() {
        initComponents();
        dateDisplayLabel.setText(String.format("%1$tA %1$td %1$tB %1$tY %1$tT", System.currentTimeMillis()));
    }

    public java.util.Map getProperties() {
        Map properties = new HashMap();
        properties.put("date", Long.toString(System.currentTimeMillis()));
        String body = bodyTextPane.getText();
        if (body != null) properties.put("body", body);
        return properties;
    }

    public void showService(Service service) {
        String body = (String) service.getProperties().get("body");
        if (body != null) bodyTextPane.setText(body);
        String dateString = (String) service.getProperties().get("date");
        Date date = new Date(Long.parseLong(dateString));
        if (date != null) dateDisplayLabel.setText(String.format("%1$tA %1$td %1$tB %1$tY %1$tT", date));
        this.invalidate();
    }

    public void setEditable(boolean isEditable) {
        bodyTextPane.setEditable(isEditable);
        bodyTextPane.setEnabled(isEditable);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        pluginPanel = new javax.swing.JPanel();
        dateDisplayLabel = new javax.swing.JLabel();
        bodyScrollPane = new javax.swing.JScrollPane();
        bodyTextPane = new javax.swing.JTextPane();
        setLayout(new java.awt.BorderLayout());
        pluginPanel.setLayout(new java.awt.GridBagLayout());
        dateDisplayLabel.setText("date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pluginPanel.add(dateDisplayLabel, gridBagConstraints);
        bodyScrollPane.setMinimumSize(new java.awt.Dimension(300, 100));
        bodyScrollPane.setPreferredSize(new java.awt.Dimension(300, 100));
        bodyScrollPane.setViewportView(bodyTextPane);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pluginPanel.add(bodyScrollPane, gridBagConstraints);
        add(pluginPanel, java.awt.BorderLayout.CENTER);
    }

    javax.swing.JScrollPane bodyScrollPane;

    javax.swing.JTextPane bodyTextPane;

    javax.swing.JLabel dateDisplayLabel;

    javax.swing.JPanel pluginPanel;
}
