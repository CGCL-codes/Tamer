package ags.ui.host;

import ags.controller.Configurator;
import ags.controller.FileType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

/**
 * This component provides a text field for the manual input of a file, as well
 * as an associated 'Browse' button, allowing the user to browse for a file and
 * have its location show up automatically in the text field.
 *
 * --borrowed and modified by Brendan Robert
 *
 * @author Eelke Spaak
 * @see javax.swing.JFileChooser
 */
class FileComponent extends javax.swing.JPanel implements ActionListener {

    Field backingField;

    public void actionPerformed(ActionEvent e) {
        Configurator.setVariable(backingField, textField.getText());
        System.out.println(backingField.getName() + " -> " + textField.getText());
    }

    public void synchronizeValue() {
        try {
            Object value = backingField.get(null);
            if (value == null) {
                setText("");
            } else {
                setText(String.valueOf(value));
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(StringComponent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(StringComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int TEXT_FIELD_WIDTH = 150;

    /** Creates new form JFileField */
    public FileComponent(Field f) {
        backingField = f;
        FileType type = f.getAnnotation(FileType.class);
        if (".".equals(type.value())) {
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY;
        } else {
            setFileTypeName(type.value());
            setExtensionFilter(type.value());
        }
        initComponents();
        textField.addActionListener(this);
        synchronizeValue();
    }

    private String extensionFilter;

    private String fileTypeName;

    private int fileSelectionMode = JFileChooser.FILES_ONLY;

    private void initComponents() {
        textField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        browseButton.setText("...");
        browseButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(layout.createSequentialGroup().add(textField, GroupLayout.DEFAULT_SIZE, TEXT_FIELD_WIDTH, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(browseButton)));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(layout.createParallelGroup(GroupLayout.BASELINE).add(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(browseButton)));
        Style.apply(textField);
        Style.apply(browseButton);
    }

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        File currentDirectory = new File(".");
        JFileChooser chooser = new JFileChooser();
        Style.apply(chooser);
        chooser.setFileSelectionMode(fileSelectionMode);
        if (extensionFilter != null && fileTypeName != null) {
            FileFilter filter = new FileFilter() {

                String[] extensions = extensionFilter.toLowerCase().split(",");

                @Override
                public boolean accept(File f) {
                    for (int i = 0; i < extensions.length; i++) {
                        if (f.getPath().toLowerCase().endsWith(extensions[i])) return true;
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return fileTypeName;
                }
            };
            chooser.setFileFilter(filter);
        }
        try {
            File f = new File(textField.getText());
            if (f.exists()) {
                if (f.isDirectory()) {
                    chooser.setCurrentDirectory(f);
                } else {
                    chooser.setCurrentDirectory(f.getParentFile());
                    chooser.setSelectedFile(f);
                }
            } else {
                chooser.setCurrentDirectory(currentDirectory);
            }
        } catch (Exception ignore) {
        }
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = chooser.getSelectedFile();
                if (selectedFile.getCanonicalPath().startsWith(currentDirectory.getCanonicalPath())) {
                    String use = selectedFile.getCanonicalPath().substring(currentDirectory.getCanonicalPath().length() + 1);
                    textField.setText(use);
                } else {
                    textField.setText(selectedFile.getPath());
                }
            } catch (IOException ex) {
                Logger.getLogger(FileComponent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Returns the value of the text field.
     *
     * @return the value of the text field
     */
    public String getText() {
        return textField.getText();
    }

    /**
     * Sets the value of the text field.
     *
     * @param the value to put in the text field
     */
    public void setText(String text) {
        textField.setText(text);
    }

    /**
     * Returns the extension filter (a comma-separated string of extensions)
     * that the JFileChooser should use when browsing for a file.
     *
     * @return the extension filter
     */
    public String getExtensionFilter() {
        return extensionFilter;
    }

    /**
     * Sets the extension filter (a comma-separated string of extensions)
     * that the JFileChooser should use when browsing for a file.
     *
     * @param extensionFilter the extension filter
     */
    public void setExtensionFilter(String extensionFilter) {
        this.extensionFilter = extensionFilter;
    }

    /**
     * Returns the description of the file types the JFileChooser should be
     * browsing for.
     *
     * @return the file type description
     */
    public String getFileTypeName() {
        return fileTypeName;
    }

    /**
     * Sets the description of the file types the JFileChooser should be
     * browsing for.
     *
     * @param fileTypeName the file type description
     */
    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    /**
     * Returns the file selection mode to be used by the JFileChooser.
     *
     * @return the type of files to be displayed
     * @see javax.swing.JFileChooser#getFileSelectionMode()
     */
    public int getFileSelectionMode() {
        return fileSelectionMode;
    }

    /**
     * Sets the file selection mode to be used by the JFileChooser.
     *
     * @param fileSelectionMode the type of files to be displayed
     * @see javax.swing.JFileChooser#setFileSelectionMode(int)
     */
    public void setFileSelectionMode(int fileSelectionMode) {
        this.fileSelectionMode = fileSelectionMode;
    }

    private javax.swing.JButton browseButton;

    private javax.swing.JTextField textField;
}
