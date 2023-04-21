package gov.sns.apps.jeri.apps.signalfunctions;

import gov.sns.apps.jeri.application.JeriDialog;
import gov.sns.apps.jeri.data.DBFile;
import java.awt.Frame;
import java.awt.Dimension;
import java.io.File;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Provides an interface for creating a new <CODE>DBFile</CODE>.
 * 
 * @author Chris Fowlkes
 */
public class NewDBFileDialog extends JeriDialog {

    private JPanel outerButtonPanel = new JPanel();

    private JPanel innerButtonPanel = new JPanel();

    private BorderLayout outerButtonPanelLayout = new BorderLayout();

    private GridLayout innerButtonPanelLayout = new GridLayout();

    private JButton okButton = new JButton();

    private JButton cancelButton = new JButton();

    private JPanel fieldPanel = new JPanel();

    private GridLayout fieldPanelLayout = new GridLayout();

    private JPanel labelPanel = new JPanel();

    private GridLayout labelPanelLayout = new GridLayout();

    private JLabel directoryLabel = new JLabel();

    private JLabel fileNameLabel = new JLabel();

    private JTextField directoryNameField = new JTextField();

    private JTextField fileNameField = new JTextField();

    private JPanel browseButtonPanel = new JPanel();

    private GridLayout browseButtonPanelLayout = new GridLayout();

    private JPanel directoryFieldPanel = new JPanel();

    private JButton browseButton = new JButton();

    private BorderLayout directoryPanelLayout = new BorderLayout();

    /**
   * Returned by the <CODE>getResult()</CODE> method if the dialog was closed
   * with the ok button.
   */
    public static final int OK = 0;

    /**
   * Returned by the <CODE>getResult()</CODE> method if the dialog was not 
   * closed with the ok button.
   */
    public static final int CANCEL = 1;

    /**
   * Holds the value representing the button used close the dialog.
   */
    private int result = CANCEL;

    /**
   * Holds the dialog used by the user to browse for a directory.
   */
    private JFileChooser browseDialog;

    /**
   * Creates a new <CODE>NewDBFileDialog</CODE>.
   * 
   * @param parent The parent window.
   * @param title The title for the dialog.
   * @param modal Pass as <CODE>true</CODE> for a modal dialog.
   */
    public NewDBFileDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
            pack();
            fileNameField.getDocument().addDocumentListener(new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    enableOKButton();
                }

                public void insertUpdate(DocumentEvent e) {
                    enableOKButton();
                }

                public void removeUpdate(DocumentEvent e) {
                    enableOKButton();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Component initialization.
   * 
   * @throws java.lang.Exception Thrown on initialization error.
   */
    private void jbInit() throws Exception {
        this.setSize(new Dimension(400, 130));
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                this_windowClosing(e);
            }
        });
        outerButtonPanel.setLayout(outerButtonPanelLayout);
        outerButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        innerButtonPanel.setLayout(innerButtonPanelLayout);
        innerButtonPanelLayout.setHgap(5);
        okButton.setText("OK");
        okButton.setMnemonic('O');
        okButton.setEnabled(false);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.setMnemonic('C');
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });
        fieldPanel.setLayout(fieldPanelLayout);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        fieldPanelLayout.setRows(2);
        fieldPanelLayout.setVgap(5);
        labelPanel.setLayout(labelPanelLayout);
        labelPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        labelPanelLayout.setRows(2);
        labelPanelLayout.setVgap(5);
        directoryLabel.setText("Directory:");
        directoryLabel.setLabelFor(directoryNameField);
        directoryLabel.setDisplayedMnemonic('D');
        fileNameLabel.setText("File Name:");
        fileNameLabel.setLabelFor(fileNameField);
        fileNameLabel.setDisplayedMnemonic('F');
        directoryNameField.setColumns(35);
        browseButtonPanel.setLayout(browseButtonPanelLayout);
        browseButtonPanelLayout.setRows(2);
        directoryFieldPanel.setLayout(directoryPanelLayout);
        browseButton.setText("Browse...");
        browseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                browseButton_actionPerformed(e);
            }
        });
        directoryPanelLayout.setHgap(5);
        innerButtonPanel.add(okButton, null);
        innerButtonPanel.add(cancelButton, null);
        outerButtonPanel.add(innerButtonPanel, BorderLayout.EAST);
        this.getContentPane().add(outerButtonPanel, BorderLayout.SOUTH);
        fieldPanel.add(directoryFieldPanel, null);
        fieldPanel.add(fileNameField, null);
        this.getContentPane().add(fieldPanel, BorderLayout.CENTER);
        labelPanel.add(directoryLabel, null);
        labelPanel.add(fileNameLabel, null);
        this.getContentPane().add(labelPanel, BorderLayout.WEST);
        directoryFieldPanel.add(directoryNameField, BorderLayout.CENTER);
        directoryFieldPanel.add(browseButton, BorderLayout.EAST);
        this.getContentPane().add(browseButtonPanel, BorderLayout.EAST);
    }

    /**
   * Determines the button used to close the dialog. Returns <CODE>OK</CODE> if
   * the dialog was closed with the OK button, <CODE>CANCEL</CODE> otherwise.
   * 
   * @return An <CODE>int</CODE> used to determine the button used to close the dialog.
   */
    public int getResult() {
        return result;
    }

    /**
   * Returns the values entered by the user in a <CODE>DBFile</CODE>.
   * 
   * @return The <CODE>DBFile</CODE> created by the user.
   */
    public DBFile getDBFile() {
        return new DBFile(fileNameField.getText(), directoryNameField.getText());
    }

    /**
   * Called when the browse button is clicked. This method allows the user to 
   * browse for a directory.
   * 
   * @param e The <CODE>ActionEvent</CODE> that caused the invocation of this method.
   */
    private void browseButton_actionPerformed(ActionEvent e) {
        Properties settings = getApplicationProperties();
        StringBuffer buffer = new StringBuffer(getClass().getName());
        buffer.append(".directory");
        String propertyName = buffer.toString();
        String directoryName = directoryNameField.getText().trim();
        if (browseDialog == null) {
            browseDialog = new JFileChooser();
            browseDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (directoryName.equals("")) {
                String newDirectoryName = settings.getProperty(propertyName);
                if (directoryName != null) directoryName = newDirectoryName;
            }
        }
        if (!directoryName.equals("")) browseDialog.setSelectedFile(new File(directoryName));
        browseDialog.setSelectedFile(new File(directoryName));
        if (browseDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            directoryName = browseDialog.getSelectedFile().getAbsolutePath();
            directoryNameField.setText(directoryName);
            settings.setProperty(propertyName, directoryName);
        }
    }

    /**
   * Called when the OK button is clicked. Closes the dialog.
   * 
   * @param e The <CODE>ActionEvent</CODE> that caused the invocation of this method.
   */
    private void okButton_actionPerformed(ActionEvent e) {
        result = OK;
        setVisible(false);
    }

    /**
   * Called when the cancel button is clicked. Closes the dialog.
   * 
   * @param e the <CODE>ActionEvent</CODE> that caused the invocatoin of this method.
   */
    private void cancelButton_actionPerformed(ActionEvent e) {
        result = CANCEL;
        setVisible(false);
    }

    /**
   * Called when the window is closed with the button in the title bar. This 
   * method cancels the dialog.
   * 
   * @param e The <CODE>WindowEvent</CODE> that caused the invocation of this method.
   */
    private void this_windowClosing(WindowEvent e) {
        result = CANCEL;
        setVisible(false);
    }

    /**
   * Enables or disables the OK button.
   */
    private void enableOKButton() {
        okButton.setEnabled(!okButton.getText().trim().equals(""));
    }
}
