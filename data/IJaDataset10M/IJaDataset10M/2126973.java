package gov.sns.xal.smf.data;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

/**
 * OpticsSwitcher is a dialog that allows the user to select an optics file as the default
 * optics.
 *
 * @author  tap
 * @since May 28, 2004
 */
public class OpticsSwitcher extends JDialog {

    protected AcceleratorChooser accelChooser;

    protected JTextField pathField;

    protected JButton revertButton;

    protected JButton commitButton;

    /** Constructor */
    public OpticsSwitcher() {
        super();
        setup();
    }

    /** 
	 * Constructor 
	 * @param owner the window that owns this dialog
	 */
    public OpticsSwitcher(Dialog owner) {
        super(owner);
        setup();
    }

    /** 
	 * Constructor 
	 * @param owner the window that owns this dialog
	 */
    public OpticsSwitcher(Frame owner) {
        super(owner);
        setup();
    }

    /** 
	 * Constructor 
	 * @param owner the window that owns this dialog
	 * @param modal true for a modal dialog and false for a non-modal dialog
	 */
    public OpticsSwitcher(Dialog owner, boolean modal) {
        super(owner, modal);
        setup();
    }

    /** 
	 * Constructor 
	 * @param owner the window that owns this dialog
	 * @param modal true for a modal dialog and false for a non-modal dialog
	 */
    public OpticsSwitcher(Frame owner, boolean modal) {
        super(owner, modal);
        setup();
    }

    /**
	 * Setup the switcher with the default title
	 */
    protected void setup() {
        setup("Set the Default Optics");
    }

    /**
	 * Setup the switcher
	 * @param title the title to appear in the dialog box
	 */
    protected void setup(final String title) {
        setTitle(title);
        initComponents();
        accelChooser = AcceleratorChooser.getChooser();
    }

    /**
	 * Show the dialog near the specified view
	 * @param view the view near which to show this dialog
	 */
    public void showNear(Component view) {
        setLocationRelativeTo(view);
        setVisible(true);
    }

    /**
	 * Show the dialog near the dialog's owner
	 */
    public void showNearOwner() {
        showNear(getOwner());
    }

    /**
	 * Get the path to the default optics file
	 * @return the path to the default optics file
	 */
    public String getDefaultOpticsPath() {
        return AcceleratorChooser.defaultPath();
    }

    /** 
	 * Create the main view
     */
    protected void initComponents() {
        final Box mainView = new Box(BoxLayout.Y_AXIS);
        getContentPane().add(mainView);
        Box labelRow = new Box(BoxLayout.X_AXIS);
        mainView.add(labelRow);
        labelRow.add(new JLabel("Path to Default Optics:"));
        labelRow.add(Box.createHorizontalGlue());
        Box pathRow = new Box(BoxLayout.X_AXIS);
        mainView.add(pathRow);
        pathField = new JTextField(AcceleratorChooser.defaultPath());
        pathField.setColumns(40);
        pathField.setMaximumSize(pathField.getPreferredSize());
        pathRow.add(pathField);
        pathField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                textChanged(evt);
            }

            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                textChanged(evt);
            }

            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                textChanged(evt);
            }
        });
        final JButton browseButton = new JButton("Browse");
        pathRow.add(browseButton);
        browseButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent event) {
                browseButtonAction(event);
            }
        });
        final Box commitRow = new Box(BoxLayout.X_AXIS);
        mainView.add(commitRow);
        commitRow.add(Box.createHorizontalGlue());
        JButton closeButton = new JButton("Close");
        closeButton.setToolTipText("Close the dialog without applying any uncommitted changes.");
        commitRow.add(closeButton);
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
        revertButton = new JButton("Revert");
        revertButton.setToolTipText("Revert the display back to the default optics settings.");
        commitRow.add(revertButton);
        revertButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                pathField.setText(AcceleratorChooser.defaultPath());
                updateView();
            }
        });
        commitButton = new JButton("Make Default");
        commitButton.setToolTipText("Commit the selected path to become the default optics path.");
        commitRow.add(commitButton);
        commitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                final String path = pathField.getText();
                if (new File(path).exists()) {
                    accelChooser.setDefaultPath(path);
                } else {
                    String title = "Error: File does not exist...";
                    String message = "The specified file does not exist.\nThe path will not be set.";
                    int messageType = JOptionPane.ERROR_MESSAGE;
                    JOptionPane.showMessageDialog(OpticsSwitcher.this, message, title, messageType);
                }
                updateView();
            }
        });
        updateView();
        pack();
        setResizable(false);
    }

    /** 
	 * Browse button action to spawn the file selector
	 * @param event the action event
	 */
    protected void browseButtonAction(ActionEvent event) {
        accelChooser.showWithOwner(this);
        if (accelChooser.approved()) {
            File file = accelChooser.selection();
            pathField.setText(file.getAbsolutePath());
        }
    }

    /** 
	 * Handle the text changed event
	 * @param event the document event
	 */
    protected void textChanged(DocumentEvent event) {
        updateView();
    }

    /** 
	 * Update the view to reflect the model 
	 */
    protected void updateView() {
        boolean textSame = pathField.getText().equals(AcceleratorChooser.defaultPath());
        commitButton.setEnabled(!textSame);
        revertButton.setEnabled(!textSame);
    }
}

/** 
 * Display an open dialog box so the user can pick an accelerator input file 
 */
class AcceleratorChooser extends JFileChooser {

    /** global accelerator chooser */
    protected static AcceleratorChooser chooser;

    protected int status;

    /**
	 * static initializer
	 */
    static {
        chooser = new AcceleratorChooser();
    }

    /**
	 * Constructor
	 */
    public AcceleratorChooser() {
        super();
        setFileFilter(new javax.swing.filechooser.FileFilter() {

            @Override
            public boolean accept(File file) {
                String name = file.getName().toLowerCase();
                if (file.isDirectory() || name.endsWith("xal")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Optics Files";
            }
        });
        String defaultFilePath = defaultPath();
        if (defaultFilePath != null) {
            File defaultFile = new File(defaultFilePath);
            setSelectedFile(defaultFile);
        }
    }

    /**
	 * Get the globally accessible accelerator chooser
	 * @return the accelerator chooser
	 */
    public static AcceleratorChooser getChooser() {
        return chooser;
    }

    /**
	 * Show the chooser relative to the owner
	 * @param owner the owner of the file chooser
	 */
    public void showWithOwner(javax.swing.JDialog owner) {
        status = showOpenDialog(owner);
    }

    /**
	 * Get the file selection
	 * @return the user's file selection
	 */
    public File selection() {
        return getSelectedFile();
    }

    /**
	 * Get the default optics path
	 * @return the default optics path
	 */
    public static String defaultPath() {
        return XMLDataManager.defaultPath();
    }

    /**
	 * Set the default optics path
	 * @param path the default optics path
	 */
    public static void setDefaultPath(String path) {
        XMLDataManager.setDefaultPath(path);
    }

    /**
	 * Determine if the file selection was approved by the user
	 * @return true if the user approved the file selection and false if not
	 */
    public boolean approved() {
        return status == JFileChooser.APPROVE_OPTION;
    }

    /**
	 * Determine if the file selection was cancelled by the user
	 * @return true if the user cancelled the file selection and false if not
	 */
    public boolean canceled() {
        return status == JFileChooser.CANCEL_OPTION;
    }
}
