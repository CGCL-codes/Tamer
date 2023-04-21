package lib.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * A panel that allows the user to enter a filename, either in a textfield
 * or using a file dialog.
 * 
 * @author pja
 *
 */
public class FilePanel extends JPanel implements ActionListener {

    private JTextField fileField = new JTextField(10);

    private JFileChooser jfc = new JFileChooser();

    private JButton button = new JButton("..");

    private boolean openDialog = true;

    private JLabel label;

    /**
   * Constructor 
   * 
   * Now default file.
   */
    public FilePanel() {
        this("");
    }

    /**
   * Constructor
   * 
   * @param selectedFile This will be displayed as the selected file initially.
   */
    public FilePanel(String selectedFile) {
        this(selectedFile, null);
    }

    /**
   * Get the JFileChooser used in this panel.
   *  
   * @return the JFileChooser;
   */
    public JFileChooser getJFileChooser() {
        return jfc;
    }

    /**
   * Constructor
   * 
   * @param selectedFile This will be displayed as the selected file initially.
   * @param labelText    The text for a label for the panel. May be null if no label is required.
   */
    public FilePanel(String selectedFile, String labelText) {
        setLayout(new BorderLayout());
        setSelectedFile(selectedFile);
        setLabel(labelText);
        add(fileField, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
        button.addActionListener(this);
    }

    /**
   * Set the label text
   * 
   * @param labelText the label text
   */
    public void setLabel(String labelText) {
        if (labelText != null && labelText.length() > 0) {
            if (label == null) {
                label = new JLabel(labelText);
            }
            add(label, BorderLayout.NORTH);
        } else {
            if (label != null) {
                remove(label);
                label = null;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        int response = -1;
        try {
            jfc.setSelectedFile(new File(getSelectedFile()));
        } catch (NullPointerException ex) {
        }
        if (openDialog) response = jfc.showOpenDialog(this); else response = jfc.showSaveDialog(this);
        if (response == JFileChooser.APPROVE_OPTION) {
            setSelectedFile(jfc.getSelectedFile().getAbsolutePath());
        }
    }

    public void setToolTipText(String tooltip) {
        super.setToolTipText(tooltip);
        fileField.setToolTipText(tooltip);
        button.setToolTipText(tooltip);
    }

    /**
   * Set the number of columns for the text field holding the filename
   * @param cols
   */
    public void setColumns(int cols) {
        fileField.setColumns(cols);
    }

    /**
   * Get the selected file.
   * 
   * @return The selected file.
   */
    public String getSelectedFile() {
        return fileField.getText();
    }

    /**
   * Set the selected file.
   * 
   * @param selectedFile
   */
    public void setSelectedFile(String selectedFile) {
        fileField.setText(selectedFile);
    }

    /**
   * Open the dialog.
   * 
   * @param openDialog
   */
    public void setOpenDialog(boolean openDialog) {
        this.openDialog = openDialog;
    }

    public void setEnabled(boolean enabled) {
        fileField.setEnabled(enabled);
        button.setEnabled(enabled);
        if (label != null) {
            label.setEnabled(enabled);
        }
    }
}
