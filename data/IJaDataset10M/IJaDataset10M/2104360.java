package gpsExtractor.gui;

import gpsExtractor.storage.PreferencesHolder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: kostya
 * Date: Oct 16, 2010
 * Time: 10:26:57 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class PreferencesDialog extends JDialog implements ActionListener {

    private JButton okButton;

    private JButton cancelButton;

    private JPanel buttonsPanel;

    protected JPanel centralPanel;

    public PreferencesDialog(String title) {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setTitle(title);
        this.add(createSouthButtons(), BorderLayout.SOUTH);
        this.setAlwaysOnTop(true);
        this.setMinimumSize(new Dimension(200, 120));
        this.setResizable(true);
        this.setVisible(true);
    }

    private Component createSouthButtons() {
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(okButton);
        buttonsPanel.setMaximumSize(new Dimension(250, 25));
        return buttonsPanel;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}
