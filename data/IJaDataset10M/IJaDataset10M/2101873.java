package org.jopenray.server.thinclient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ThinClientPropertiesPanel extends JPanel implements DocumentListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6917316985852961439L;

    private JTextField textName;

    Thread th;

    private ThinClient currentClient;

    private JTextField textScreenWidth;

    private JTextField textScreenHeight;

    public ThinClientPropertiesPanel(final ThinClient client) {
        if (client == null) {
            throw new IllegalArgumentException("DisplayClient null");
        }
        this.currentClient = client;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);
        c.anchor = GridBagConstraints.WEST;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        this.add(new JLabel("Name"), c);
        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        textName = new JTextField();
        textName.setText(client.getName());
        this.add(textName, c);
        c.gridy++;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        this.add(new JLabel("Id"), c);
        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        final JLabel labelId = new JLabel();
        labelId.setText(client.getSerialNumber());
        this.add(labelId, c);
        c.gridy++;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        this.add(new JLabel("Screen width"), c);
        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        textScreenWidth = new JTextField();
        textScreenWidth.setText(String.valueOf(client.getScreenWidth()));
        this.add(textScreenWidth, c);
        c.gridy++;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        this.add(new JLabel("Screen height"), c);
        c.gridx++;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        textScreenHeight = new JTextField();
        textScreenHeight.setText(String.valueOf(client.getScreenHeight()));
        this.add(textScreenHeight, c);
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridy++;
        final ClientPropertiesTableModel dm = new ClientPropertiesTableModel(client);
        JTable t = new JTable(dm);
        this.add(new JScrollPane(t), c);
        th = new Thread() {

            @Override
            public void run() {
                while (true) {
                    final String text = client.getSerialNumber() + " toSend:" + client.getMessageToSendCount();
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            labelId.setText(text);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        th.start();
        textName.getDocument().addDocumentListener(this);
        textScreenWidth.getDocument().addDocumentListener(this);
        textScreenHeight.getDocument().addDocumentListener(this);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        commitChanges();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        commitChanges();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        commitChanges();
    }

    private void commitChanges() {
        currentClient.setName(textName.getText());
        try {
            if (textScreenWidth.getText().length() > 0) {
                currentClient.setScreenWidth(Integer.parseInt(textScreenWidth.getText()));
            }
            if (textScreenHeight.getText().length() > 0) {
                currentClient.setScreenHeight(Integer.parseInt(textScreenHeight.getText()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ThinClientManager.getInstance().update(currentClient);
    }
}
