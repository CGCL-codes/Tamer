package org.ozoneDB.adminGui.main;

import org.ozoneDB.adminGui.res.Images;
import org.ozoneDB.adminGui.widget.MessageBox;
import org.ozoneDB.adminGui.util.ThreadWorker;
import org.ozoneDB.ExternalDatabase;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Per Nyfelt
 */
public class ConnectionDialog extends JDialog {

    private static final Dimension DIMENSION_DIALOG = new Dimension(450, 300);

    private static final Dimension DIMENSION_BUTTON = new Dimension(20, 10);

    private JTextField dbURLTextField;

    private ExternalDatabase db;

    private boolean dbHasChanged = false;

    private String url;

    public ConnectionDialog(JFrame owner, String title) {
        super(owner, title, true);
        layoutDialog();
    }

    public boolean isDbChanged() {
        return dbHasChanged;
    }

    public ExternalDatabase getDb() {
        return db;
    }

    public String getUrl() {
        return url;
    }

    private void layoutDialog() {
        setSize(DIMENSION_DIALOG);
        setLocationRelativeTo(null);
        this.addWindowListener(new ConnectionDialog.CloseWindowListener());
        Container cp = getContentPane();
        cp.setBackground(Color.white);
        JLabel logo = new JLabel(new ImageIcon(this.getClass().getResource(Images.IMAGE_CONNECT)));
        cp.add(logo, BorderLayout.NORTH);
        cp.add(createInputPanel(), BorderLayout.CENTER);
        cp.add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(Color.white);
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.anchor = GridBagConstraints.EAST;
        gbcLabel.insets = new Insets(10, 0, 0, 5);
        gbcLabel.weightx = 0;
        gbcLabel.weighty = 0;
        GridBagConstraints gbcValue = new GridBagConstraints();
        gbcValue.anchor = GridBagConstraints.WEST;
        gbcValue.insets = new Insets(10, 0, 0, 0);
        gbcValue.weightx = 0;
        gbcValue.weighty = 0;
        JLabel dbURLLabel = new JLabel("DB URL");
        dbURLLabel.setForeground(Color.darkGray);
        inputPanel.add(dbURLLabel, gbcLabel);
        dbURLTextField = new JTextField("ozonedb:remote://localhost:3333");
        inputPanel.add(dbURLTextField, gbcValue);
        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.white);
        JButton logonButton = new JButton("Connect");
        logonButton.setMinimumSize(DIMENSION_BUTTON);
        logonButton.setDefaultCapable(true);
        buttonPanel.add(logonButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setMinimumSize(DIMENSION_BUTTON);
        buttonPanel.add(cancelButton);
        logonButton.addActionListener(new ConnectionDialog.ConnectListener());
        cancelButton.addActionListener(new ConnectionDialog.cancelListener());
        getRootPane().setDefaultButton(logonButton);
        return buttonPanel;
    }

    private class ConnectListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            ThreadWorker worker = new ThreadWorker() {

                public Object construct() {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    return dbURLTextField.getText();
                }

                public void finished() {
                    String dbURL = (String) get();
                    if (dbURL.equals("")) {
                        setCursor(Cursor.getDefaultCursor());
                        MessageBox.showError("Error opening db", "No URL specified");
                        ConnectionDialog.this.setCursor(Cursor.getDefaultCursor());
                        return;
                    }
                    try {
                        db = ExternalDatabase.openDatabase(dbURL);
                        dbHasChanged = true;
                        url = dbURL;
                        ConnectionDialog.this.setVisible(false);
                        ConnectionDialog.this.setCursor(Cursor.getDefaultCursor());
                        setCursor(Cursor.getDefaultCursor());
                    } catch (Exception e) {
                        e.printStackTrace();
                        setCursor(Cursor.getDefaultCursor());
                        MessageBox.showError("Error opening db", e.toString());
                        ConnectionDialog.this.setCursor(Cursor.getDefaultCursor());
                    }
                }
            };
            worker.start();
        }
    }

    private class cancelListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            ConnectionDialog.this.setVisible(false);
            ConnectionDialog.this.dispose();
        }
    }

    private class CloseWindowListener extends WindowAdapter {

        public void windowClosing(WindowEvent we) {
            ConnectionDialog.this.setVisible(false);
            ConnectionDialog.this.dispose();
        }
    }
}
