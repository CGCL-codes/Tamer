package com.jkompare;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
class KompareFilesSelector extends JDialog {

    private static final Dimension DEFAULT_SIZE = new Dimension(600, 170);

    private final JFileChooser filesChooser = new JFileChooser();

    private final JTextField leftFileField = new JTextField(), rightFileField = new JTextField();

    private final JComboBox leftFileEncoding = new JComboBox(new Vector<Charset>(Charset.availableCharsets().values())), rightFileEncoding = new JComboBox(new Vector<Charset>(Charset.availableCharsets().values()));

    private final JButton okButton = new JButton(KompareConfiguration.TRANSLATOR.getString("filesselector.ok.button"));

    private boolean wasCancelled;

    KompareFilesSelector(JFrame owner) {
        super(owner, true);
        setTitle(KompareConfiguration.TRANSLATOR.getString("filesselector.title"));
        JPanel leftFilePanel = new JPanel(new BorderLayout());
        JPanel rightFilePanel = new JPanel(new BorderLayout());
        leftFilePanel.add(new JLabel(KompareConfiguration.TRANSLATOR.getString("filesselector.leftfile.label")), BorderLayout.WEST);
        rightFilePanel.add(new JLabel(KompareConfiguration.TRANSLATOR.getString("filesselector.rightfile.label")), BorderLayout.WEST);
        leftFilePanel.add(leftFileField);
        rightFilePanel.add(rightFileField);
        JButton leftFileBrowseButton = new JButton(KompareConfiguration.TRANSLATOR.getString("filesselector.leftfile-browse.button"));
        JButton rightFileBrowseButton = new JButton(KompareConfiguration.TRANSLATOR.getString("filesselector.rightfile-browse.button"));
        leftFileBrowseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (filesChooser.showOpenDialog(KompareFilesSelector.this) == JFileChooser.APPROVE_OPTION) leftFileField.setText(filesChooser.getSelectedFile().getAbsolutePath());
                okButton.setEnabled(leftFileField.getText().length() > 0 && rightFileField.getText().length() > 0 && getLeftFile().exists() && getRightFile().exists());
            }
        });
        rightFileBrowseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (filesChooser.showOpenDialog(KompareFilesSelector.this) == JFileChooser.APPROVE_OPTION) rightFileField.setText(filesChooser.getSelectedFile().getAbsolutePath());
                okButton.setEnabled(leftFileField.getText().length() > 0 && rightFileField.getText().length() > 0 && getLeftFile().exists() && getRightFile().exists());
            }
        });
        leftFilePanel.add(leftFileBrowseButton, BorderLayout.EAST);
        rightFilePanel.add(rightFileBrowseButton, BorderLayout.EAST);
        JPanel leftFileRow = new JPanel(new BorderLayout());
        JPanel rightFileRow = new JPanel(new BorderLayout());
        leftFileRow.add(leftFilePanel);
        rightFileRow.add(rightFilePanel);
        leftFileRow.add(leftFileEncoding, BorderLayout.EAST);
        rightFileRow.add(rightFileEncoding, BorderLayout.EAST);
        leftFileEncoding.setBorder(new EmptyBorder(0, 10, 0, 0));
        rightFileEncoding.setBorder(new EmptyBorder(0, 10, 0, 0));
        leftFileRow.setBorder(new EmptyBorder(10, 10, 5, 10));
        rightFileRow.setBorder(new EmptyBorder(0, 10, 5, 10));
        JPanel rowsPanel = newJRowPanel(new JLabel(KompareConfiguration.TRANSLATOR.getString("filesselector.intro.label")), leftFileRow, rightFileRow);
        rowsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton cancelButton = new JButton(KompareConfiguration.TRANSLATOR.getString("filesselector.cancel.button"));
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                wasCancelled = false;
                setVisible(false);
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        KeyAdapter okButtonEnabler = new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                okButton.setEnabled(leftFileField.getText().length() > 0 && rightFileField.getText().length() > 0 && getLeftFile().exists() && getRightFile().exists());
            }
        };
        leftFileField.addKeyListener(okButtonEnabler);
        rightFileField.addKeyListener(okButtonEnabler);
        okButton.setEnabled(false);
        JPanel flowButtonsPanel = new JPanel(new FlowLayout());
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3));
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(new JPanel());
        buttonsPanel.add(okButton);
        flowButtonsPanel.add(buttonsPanel);
        add(rowsPanel);
        add(flowButtonsPanel, BorderLayout.SOUTH);
    }

    boolean open() {
        leftFileField.setText("");
        rightFileField.setText("");
        leftFileEncoding.setSelectedItem(KompareConfiguration.DEFAULT_ENCODING);
        rightFileEncoding.setSelectedItem(KompareConfiguration.DEFAULT_ENCODING);
        setSize(DEFAULT_SIZE);
        setLocation(getOwner().getX() + (getOwner().getWidth() - DEFAULT_SIZE.width) / 2, getOwner().getY() + (getOwner().getHeight() - DEFAULT_SIZE.height) / 2);
        wasCancelled = true;
        setVisible(true);
        if (!wasCancelled && getLeftFile().equals(getRightFile())) {
            JOptionPane.showMessageDialog(getOwner(), KompareConfiguration.TRANSLATOR.getString("filesselector.samefiles.message"), KompareConfiguration.TRANSLATOR.getString("filesselector.samefiles.title"), JOptionPane.WARNING_MESSAGE);
        }
        return !wasCancelled;
    }

    File getLeftFile() {
        return new File(leftFileField.getText());
    }

    File getRightFile() {
        return new File(rightFileField.getText());
    }

    Charset getLeftFileEncoding() {
        return (Charset) leftFileEncoding.getSelectedItem();
    }

    Charset getRightFileEncoding() {
        return (Charset) rightFileEncoding.getSelectedItem();
    }

    private static JPanel newJRowPanel(JComponent... components) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel p = mainPanel;
        for (JComponent c : components) {
            p.add(c, BorderLayout.NORTH);
            JPanel newP = new JPanel(new BorderLayout());
            p.add(newP);
            p = newP;
        }
        return mainPanel;
    }
}
