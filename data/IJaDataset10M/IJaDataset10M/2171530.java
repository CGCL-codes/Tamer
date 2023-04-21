package com.kitten.gui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.kitten.IKittenListen2GuiObject;
import com.kitten.constants.KittenConstants;
import com.kitten.utilities.KittenProperties;

public class KittenImportDialog extends JDialog implements IKittenLayoutIssues {

    private KittenProperties kittenProperties;

    private JButton[] buttonArray = new JButton[2];

    private String[][] buttons;

    private JPanel southPanel = new JPanel(new BorderLayout());

    private JLabel pathLabel, separationCharLabel, importTableLabel;

    private JTextField importPathField = new JTextField(15);

    private JTextField tableField = new JTextField(10);

    private JTextField separationCharField = new JTextField(KittenConstants.DEFAULT_SEPARATION_CHARACTER_IMPORT, 1);

    private JButton showFileChooserButton;

    private JCheckBox stopOnerrorCheckBox;

    public KittenImportDialog(KittenProperties kittenProperties) throws HeadlessException {
        super();
        this.kittenProperties = kittenProperties;
        buttons = new String[][] { { KittenConstants.TABLE_POPUP_IMPORT_OK, KittenConstants.TABLE_POPUP_IMPORT_CANCEL }, { kittenProperties.getApplicationProperty("okLabel"), kittenProperties.getApplicationProperty("cancelLabel") } };
    }

    protected void makePanel(IKittenListen2GuiObject listener) {
        for (int i = 0; i < buttons[0].length; i++) {
            buttonArray[i] = new JButton(buttons[1][i]);
        }
        super.setModal(true);
        pathLabel = new JLabel(kittenProperties.getApplicationProperty("KittenExportFileLabel"));
        importTableLabel = new JLabel(kittenProperties.getApplicationProperty("importTableLabel"));
        separationCharLabel = new JLabel(kittenProperties.getApplicationProperty("KittenSeparationCharLabel"));
        pathLabel.setFont(labelFont);
        importTableLabel.setFont(labelFont);
        separationCharLabel.setFont(labelFont);
        showFileChooserButton = new JButton(kittenProperties.getApplicationProperty("chooseFileLabel"));
        stopOnerrorCheckBox = new JCheckBox(kittenProperties.getApplicationProperty("stopOnerrorLabel"), true);
        stopOnerrorCheckBox.setFont(checkBoxFont);
        buttonActionListeners(listener);
        buttonKeyListeners(listener);
        this.setLocation(100, 200);
        this.setTitle(kittenProperties.getApplicationProperty("importTitle"));
        this.setSize(Integer.parseInt(kittenProperties.getApplicationProperty("popupFrameWidth")), Integer.parseInt(kittenProperties.getApplicationProperty("popupFrameHeight")));
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel subPanel = new JPanel();
        subPanel.add(pathLabel);
        subPanel.add(importPathField);
        subPanel.add(showFileChooserButton);
        subPanel.add(importTableLabel);
        subPanel.add(tableField);
        showFileChooserButton.setFont(buttonNormalFont);
        showFileChooserButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showFileDialog();
            }
        });
        showFileChooserButton.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KittenConstants.KEYCODE_ENTER) {
                    showFileDialog();
                }
            }

            public void keyTyped(KeyEvent ke) {
            }

            public void keyReleased(KeyEvent ke) {
            }
        });
        subPanel.add(separationCharLabel);
        subPanel.add(separationCharField);
        subPanel.add(stopOnerrorCheckBox);
        subPanel.add(buttonArray[0]);
        subPanel.add(buttonArray[1]);
        panel.add(BorderLayout.CENTER, subPanel);
        this.getContentPane().add(panel);
        super.pack();
        this.setVisible(true);
    }

    protected void buttonActionListeners(ActionListener al) {
        for (int i = 0; i < buttons[0].length; i++) {
            buttonArray[i].setActionCommand(buttons[0][i]);
            buttonArray[i].addActionListener(al);
            buttonArray[i].setPreferredSize(buttonNormalDimension);
            buttonArray[i].setFont(buttonNormalFont);
            switch(i) {
                case 0:
                    buttonArray[i].setMnemonic(KeyEvent.VK_O);
                    break;
                case 1:
                    buttonArray[i].setMnemonic(KeyEvent.VK_C);
                    break;
            }
        }
    }

    protected void buttonKeyListeners(KeyListener kl) {
        for (int i = 0; i < buttons[0].length; i++) {
            buttonArray[i].setName(buttons[0][i]);
            buttonArray[i].addKeyListener(kl);
        }
    }

    protected String getTableName() {
        return tableField.getText();
    }

    public String getSeparationChar() {
        return separationCharField.getText();
    }

    public String getFileName() {
        return importPathField.getText();
    }

    public boolean isStopOnError() {
        return stopOnerrorCheckBox.isSelected();
    }

    private void showFileDialog() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFont(fileChooserFont);
        int returnVal = chooser.showOpenDialog(KittenImportDialog.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                importPathField.setText(chooser.getSelectedFile().getCanonicalPath());
            } catch (Exception ed) {
            }
        }
    }
}
