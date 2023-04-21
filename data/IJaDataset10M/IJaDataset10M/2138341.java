package dmsgui;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import net.sf.dms.lib.DMSException;
import net.sf.dms.lib.DMSManagerFactory;
import net.sf.dms.lib.filetype.FileType;

/**
 *
 * @author  daniel
 */
public class FileTypeFrame extends javax.swing.JFrame {

    private FileType fileType = null;

    /** Creates new form FileTypeFrame */
    public FileTypeFrame() {
        initComponents();
        setVisible(true);
    }

    public FileTypeFrame(FileType fileType) {
        initComponents();
        setVisible(true);
        this.fileType = fileType;
        headingLabel.setText(headingLabel.getText() + fileType.getName());
        nameTextField.setText(fileType.getName());
        try {
            pathToApplicationTextField.setText(fileType.getApplication().getCanonicalPath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "The specified application could not be found. Please choose a new one", "Error", JOptionPane.ERROR_MESSAGE);
        }
        parametersTextField.setText(fileType.getParameter());
    }

    private void initComponents() {
        headingLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pathToApplicationTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        parametersTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edit File Type");
        setResizable(false);
        headingLabel.setText("Properties of the file type ");
        jLabel2.setText("Name:");
        jLabel3.setText("Path to application which is used to edit documents of this type:");
        browseButton.setText("Browse...");
        browseButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });
        jLabel4.setText("Parameters to edit documents of this type:");
        parametersTextField.setText("\"%s\"");
        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 2, 13));
        jLabel5.setText("Note: %s will be replaced with the path to the respective file.");
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(headingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE).addComponent(parametersTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)).addComponent(jLabel3).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(pathToApplicationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(browseButton)).addComponent(jLabel4).addComponent(jLabel5).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(okButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(applyButton))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(headingLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(browseButton).addComponent(pathToApplicationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(parametersTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(applyButton).addComponent(cancelButton).addComponent(okButton)).addContainerGap()));
        pack();
    }

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        ExampleFileFilter fileFilter = new ExampleFileFilter();
        fileFilter.addExtension("exe");
        fileFilter.addExtension("bat");
        fileFilter.setDescription("Executables");
        fileFilter.setExtensionListInDescription(true);
        chooser.addChoosableFileFilter(chooser.getAcceptAllFileFilter());
        chooser.setFileFilter(fileFilter);
        int returnVal = chooser.showDialog(null, "Choose");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                pathToApplicationTextField.setText(chooser.getSelectedFile().getCanonicalPath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Cannot read from disk.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (apply()) {
            dispose();
        }
    }

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {
        apply();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private boolean apply() {
        if (fileType == null) {
            try {
                fileType = DMSManagerFactory.getFileTypeManager().create(nameTextField.getText(), new File(pathToApplicationTextField.getText()), parametersTextField.getText());
            } catch (DMSException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                if (ex.getError().equals(DMSException.Error.PARAMETER_INVALID)) {
                    parametersTextField.setText(parametersTextField.getText() + "\"%s\"");
                    return apply();
                }
                return false;
            }
        } else {
            try {
                fileType.setApplication(new File(pathToApplicationTextField.getText()));
            } catch (DMSException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            try {
                fileType.setName(nameTextField.getText());
            } catch (DMSException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            try {
                fileType.setParameter(parametersTextField.getText());
            } catch (DMSException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                if (ex.getError().equals(DMSException.Error.PARAMETER_INVALID)) {
                    parametersTextField.setText(parametersTextField.getText() + "\"%s\"");
                    return apply();
                }
                return false;
            }
        }
        RefreshBus.getBus().refreshAll();
        return true;
    }

    private javax.swing.JButton applyButton;

    private javax.swing.JButton browseButton;

    private javax.swing.JButton cancelButton;

    private javax.swing.JLabel headingLabel;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JTextField nameTextField;

    private javax.swing.JButton okButton;

    private javax.swing.JTextField parametersTextField;

    private javax.swing.JTextField pathToApplicationTextField;
}
