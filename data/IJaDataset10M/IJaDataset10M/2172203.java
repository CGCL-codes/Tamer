package net.sourceforge.jhelpdev;

import java.io.File;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.sourceforge.jhelpdev.action.CreateAllAction;
import net.sourceforge.jhelpdev.action.CreateMapAction;
import net.sourceforge.jhelpdev.action.ExportHsAction;
import net.sourceforge.jhelpdev.action.ExportIndexAction;
import net.sourceforge.jhelpdev.action.ExportTOCAction;
import net.sourceforge.jhelpdev.action.HelperMethods;
import net.sourceforge.jhelpdev.action.ImportIndexAction;
import net.sourceforge.jhelpdev.action.ImportTOCAction;
import net.sourceforge.jhelpdev.action.JHelpViewerAction;

/**
 * User dialog for configuration settings related to a project.
 * 
 * @author <a href="mailto:mk@mk-home.de">Markus Kraetzig </a>
 */
final class ConfigureDialog extends javax.swing.JDialog {

    private static final String defaultImage = "images/popup.gif";

    private static final String defaultProjectName = "MyHelp";

    private static final String defaultTopTarget = "index.html";

    private static boolean isCanceled = false;

    private boolean isNew = true;

    private String message = "message";

    private JButton ivjBrowseDir = null;

    private JPanel ivjJDialogContentPane = null;

    private JLabel ivjJLabel1 = null;

    private JLabel ivjJLabel3 = null;

    private JTextField ivjTopField = null;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private JTextField ivjProjectDirField = null;

    private JLabel ivjJLabel11 = null;

    private JTextField ivjProjectNameField = null;

    private JButton ivjCancel = null;

    private JButton ivjOK = null;

    private JLabel ivjJLabel4 = null;

    private JTextField ivjPopupIconField = null;

    private JFileChooser ivjJFileChooser = null;

    private JLabel ivjJLabel41 = null;

    private JComboBox ivjEncodingsCombo = null;

    class IvjEventHandler implements java.awt.event.ActionListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == ConfigureDialog.this.getBrowseDir()) connEtoC1();
            if (e.getSource() == ConfigureDialog.this.getOK()) connEtoC2();
            if (e.getSource() == ConfigureDialog.this.getCancel()) connEtoC3();
            if (e.getSource() == ConfigureDialog.this.getProjectNameField()) connEtoC4();
            if (e.getSource() == ConfigureDialog.this.getProjectDirField()) connEtoC5();
            if (e.getSource() == ConfigureDialog.this.getTopField()) connEtoC7();
            if (e.getSource() == ConfigureDialog.this.getPopupIconField()) connEtoC8();
        }

        ;
    }

    ;

    /**
     * ConfigureDialog constructor comment.
     */
    public ConfigureDialog() {
        super();
        initialize();
    }

    /**
     * ConfigureDialog constructor comment.
     * 
     * @param owner
     *            java.awt.Frame
     * @param modal
     *            boolean
     */
    public ConfigureDialog(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
     * Comment
     */
    private void browseDir_ActionEvents() {
        if (JHelpDevFrame.HOMEDIR != null) if (new File(JHelpDevFrame.HOMEDIR).isDirectory() || new File(JHelpDevFrame.HOMEDIR).isFile()) getJFileChooser().setCurrentDirectory(new File(JHelpDevFrame.HOMEDIR));
        getJFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = getJFileChooser().showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = getJFileChooser().getSelectedFile();
            getProjectDirField().setText(file.toString());
        }
        return;
    }

    /**
     * Comment
     */
    private void cancel_ActionEvents() {
        isCanceled = true;
        setVisible(false);
        return;
    }

    /**
     * Comment
     */
    private boolean checkInput() {
        String projectdir = getProjectDirField().getText().trim();
        String top = getTopField().getText().trim();
        String pop = getPopupIconField().getText().trim();
        if (getProjectNameField().getText().trim().length() == 0) {
            message = "Project name cannot be empty.";
            return false;
        }
        if (projectdir.length() == 0) {
            message = "Project directory cannot be empty.";
            return false;
        }
        File proFile = new File(projectdir);
        if (!proFile.isDirectory()) {
            if (!proFile.mkdirs()) {
                message = "Project directory \"" + proFile + "\" cannot be created.";
                return false;
            }
        }
        if (top.length() > 0) if (!new File(projectdir + File.separator + top).isFile()) {
            new File(projectdir + File.separator + top).getParentFile().mkdirs();
            HelperMethods.copyResourceFileTo(projectdir + File.separator + top, "/templates/index.html");
        }
        if (pop.length() > 0) if (!new File(projectdir + File.separator + pop).isFile()) {
            new File(projectdir + File.separator + pop).getParentFile().mkdirs();
            HelperMethods.copyResourceFileTo(projectdir + File.separator + pop, "/templates/popup.gif");
        }
        return true;
    }

    private void connEtoC1() {
        try {
            this.browseDir_ActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC2() {
        try {
            this.oK_ActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC3() {
        try {
            this.cancel_ActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC4() {
        try {
            this.oK_ActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC5() {
        try {
            this.oK_ActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC7() {
        try {
            this.oK_ActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC8() {
        try {
            this.oK_ActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private javax.swing.JButton getBrowseDir() {
        if (ivjBrowseDir == null) {
            try {
                ivjBrowseDir = new javax.swing.JButton();
                ivjBrowseDir.setName("BrowseDir");
                ivjBrowseDir.setText("Browse");
                ivjBrowseDir.setBounds(532, 103, 85, 25);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjBrowseDir;
    }

    private javax.swing.JButton getCancel() {
        if (ivjCancel == null) {
            try {
                ivjCancel = new javax.swing.JButton();
                ivjCancel.setName("Cancel");
                ivjCancel.setText("Cancel");
                ivjCancel.setBounds(397, 360, 85, 25);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCancel;
    }

    private javax.swing.JComboBox getEncodingsCombo() {
        if (ivjEncodingsCombo == null) {
            try {
                ivjEncodingsCombo = new javax.swing.JComboBox();
                ivjEncodingsCombo.setName("EncodingsCombo");
                ivjEncodingsCombo.setBounds(20, 300, 150, 23);
                for (Iterator it = ConfigHolder.AVAILABLE_ENCODINGS.iterator(); it.hasNext(); ) ivjEncodingsCombo.addItem(it.next());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjEncodingsCombo;
    }

    private javax.swing.JPanel getJDialogContentPane() {
        if (ivjJDialogContentPane == null) {
            try {
                ivjJDialogContentPane = new javax.swing.JPanel();
                ivjJDialogContentPane.setName("JDialogContentPane");
                ivjJDialogContentPane.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
                ivjJDialogContentPane.setLayout(null);
                getJDialogContentPane().add(getProjectDirField(), getProjectDirField().getName());
                getJDialogContentPane().add(getJLabel1(), getJLabel1().getName());
                getJDialogContentPane().add(getBrowseDir(), getBrowseDir().getName());
                getJDialogContentPane().add(getJLabel3(), getJLabel3().getName());
                getJDialogContentPane().add(getTopField(), getTopField().getName());
                getJDialogContentPane().add(getJLabel11(), getJLabel11().getName());
                getJDialogContentPane().add(getProjectNameField(), getProjectNameField().getName());
                getJDialogContentPane().add(getOK(), getOK().getName());
                getJDialogContentPane().add(getCancel(), getCancel().getName());
                getJDialogContentPane().add(getJLabel4(), getJLabel4().getName());
                getJDialogContentPane().add(getPopupIconField(), getPopupIconField().getName());
                getJDialogContentPane().add(getJLabel41(), getJLabel41().getName());
                getJDialogContentPane().add(getEncodingsCombo(), getEncodingsCombo().getName());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJDialogContentPane;
    }

    private javax.swing.JFileChooser getJFileChooser() {
        if (ivjJFileChooser == null) {
            try {
                ivjJFileChooser = new javax.swing.JFileChooser();
                ivjJFileChooser.setName("JFileChooser");
                ivjJFileChooser.setBounds(254, 521, 500, 300);
                ivjJFileChooser.setDialogTitle("Select Project Directory");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJFileChooser;
    }

    private javax.swing.JLabel getJLabel1() {
        if (ivjJLabel1 == null) {
            try {
                ivjJLabel1 = new javax.swing.JLabel();
                ivjJLabel1.setName("JLabel1");
                ivjJLabel1.setText("The project directory ");
                ivjJLabel1.setBounds(20, 79, 377, 14);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel1;
    }

    private javax.swing.JLabel getJLabel11() {
        if (ivjJLabel11 == null) {
            try {
                ivjJLabel11 = new javax.swing.JLabel();
                ivjJLabel11.setName("JLabel11");
                ivjJLabel11.setText("The project name (for file naming conventions)");
                ivjJLabel11.setBounds(20, 15, 377, 14);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel11;
    }

    private javax.swing.JLabel getJLabel3() {
        if (ivjJLabel3 == null) {
            try {
                ivjJLabel3 = new javax.swing.JLabel();
                ivjJLabel3.setName("JLabel3");
                ivjJLabel3.setText("The HTML file in the project directory displayed as default (TOP target)");
                ivjJLabel3.setBounds(20, 143, 515, 14);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel3;
    }

    private javax.swing.JLabel getJLabel4() {
        if (ivjJLabel4 == null) {
            try {
                ivjJLabel4 = new javax.swing.JLabel();
                ivjJLabel4.setName("JLabel4");
                ivjJLabel4.setText("Special target popup_icon (can be empty, path relative to project directory)");
                ivjJLabel4.setBounds(20, 207, 563, 14);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel4;
    }

    private javax.swing.JLabel getJLabel41() {
        if (ivjJLabel41 == null) {
            try {
                ivjJLabel41 = new javax.swing.JLabel();
                ivjJLabel41.setName("JLabel41");
                ivjJLabel41.setText("Character encoding for XML files");
                ivjJLabel41.setBounds(20, 271, 563, 14);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel41;
    }

    private javax.swing.JButton getOK() {
        if (ivjOK == null) {
            try {
                ivjOK = new javax.swing.JButton();
                ivjOK.setName("OK");
                ivjOK.setText("OK");
                ivjOK.setBounds(156, 360, 85, 25);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjOK;
    }

    private javax.swing.JTextField getPopupIconField() {
        if (ivjPopupIconField == null) {
            try {
                ivjPopupIconField = new javax.swing.JTextField();
                ivjPopupIconField.setName("PopupIconField");
                ivjPopupIconField.setText("images/popup.gif");
                ivjPopupIconField.setBounds(20, 236, 502, 20);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjPopupIconField;
    }

    private javax.swing.JTextField getProjectDirField() {
        if (ivjProjectDirField == null) {
            try {
                ivjProjectDirField = new javax.swing.JTextField();
                ivjProjectDirField.setName("ProjectDirField");
                ivjProjectDirField.setText("c:\\temp\\myhelpset");
                ivjProjectDirField.setBounds(20, 108, 502, 20);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjProjectDirField;
    }

    /**
     * Comment
     */
    private javax.swing.JTextField getProjectNameField() {
        if (ivjProjectNameField == null) {
            try {
                ivjProjectNameField = new javax.swing.JTextField();
                ivjProjectNameField.setName("ProjectNameField");
                ivjProjectNameField.setText("myhelpset");
                ivjProjectNameField.setBounds(20, 44, 502, 20);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjProjectNameField;
    }

    private javax.swing.JTextField getTopField() {
        if (ivjTopField == null) {
            try {
                ivjTopField = new javax.swing.JTextField();
                ivjTopField.setName("TopField");
                ivjTopField.setText("index.html");
                ivjTopField.setBounds(20, 172, 502, 20);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTopField;
    }

    /**
     * Called whenever the part throws an exception.
     * 
     * @param exception
     *            java.lang.Throwable
     */
    private void handleException(java.lang.Throwable exception) {
        System.out.println("--------- UNCAUGHT EXCEPTION ---------");
        exception.printStackTrace(System.out);
    }

    private void initConnections() throws java.lang.Exception {
        getBrowseDir().addActionListener(ivjEventHandler);
        getOK().addActionListener(ivjEventHandler);
        getCancel().addActionListener(ivjEventHandler);
        getProjectNameField().addActionListener(ivjEventHandler);
        getProjectDirField().addActionListener(ivjEventHandler);
        getTopField().addActionListener(ivjEventHandler);
        getPopupIconField().addActionListener(ivjEventHandler);
    }

    private void initialize() {
        try {
            setName("ConfigureDialog");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setResizable(false);
            setSize(638, 420);
            setTitle("Configure JavaHelp Directory Settings");
            setContentPane(getJDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
        setTextFields();
    }

    /**
     * Returns whether dialog was cancelled or not.
     * 
     * @return boolean
     */
    public static boolean isCanceled() {
        return isCanceled;
    }

    /**
     * main entrypoint - starts the part when it is run as an application
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(java.lang.String[] args) {
        try {
            ConfigureDialog aConfigureDialog;
            aConfigureDialog = new ConfigureDialog();
            aConfigureDialog.setModal(true);
            aConfigureDialog.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aConfigureDialog.setVisible(true);
            java.awt.Insets insets = aConfigureDialog.getInsets();
            aConfigureDialog.setSize(aConfigureDialog.getWidth() + insets.left + insets.right, aConfigureDialog.getHeight() + insets.top + insets.bottom);
            aConfigureDialog.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Exception occurred in main() of javax.swing.JDialog");
            exception.printStackTrace(System.out);
        }
    }

    /**
     * Comment
     */
    private void oK_ActionEvents() {
        if (!checkInput()) {
            JOptionPane.showConfirmDialog(this, message, "Configuration Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }
        String oldMap = ConfigHolder.CONF.getProjectDir();
        String oldTop = ConfigHolder.CONF.getTopTarget();
        String oldPop = ConfigHolder.CONF.getPopupImage();
        String oldTitle = ConfigHolder.CONF.getProjectName();
        String oldEncoding = ConfigHolder.CONF.getXmlEncoding();
        if (getProjectNameField().getText().trim().length() > 0) ConfigHolder.CONF.setProjectName(getProjectNameField().getText().trim());
        ConfigHolder.CONF.setTopTarget(getTopField().getText().trim());
        if (getProjectDirField().getText().trim().length() > 0) ConfigHolder.CONF.setProjectDir(getProjectDirField().getText().trim());
        ConfigHolder.CONF.setPopupImage(getPopupIconField().getText().trim());
        ConfigHolder.CONF.setXmlEncoding(getEncodingsCombo().getSelectedItem().toString());
        isCanceled = false;
        setVisible(false);
        setTextFields();
        if (isNew) {
            ImportIndexAction.doIt();
            ImportTOCAction.doIt();
            CreateMapAction.doIt();
            if (TOCEditorPanel.getTOCTree().getRootNode().getChildCount() == 0) TOCEditorPanel.getTOCTree().setTreeContents(CreateMapAction.getGeneratedRoot());
            CreateAllAction.doIt();
            JHelpDevFrame.getAJHelpDevToolFrame().setAllEnabled(true);
            JHelpViewerAction.doIt();
        } else if (!oldMap.equals(ConfigHolder.CONF.getProjectDir()) || !oldTop.equals(ConfigHolder.CONF.getTopTarget()) || !oldPop.equals(ConfigHolder.CONF.getPopupImage()) || !oldTitle.equals(ConfigHolder.CONF.getProjectName()) || !oldEncoding.equals(ConfigHolder.CONF.getXmlEncoding())) {
            CreateMapAction.doIt();
            ExportHsAction.doIt();
            ExportIndexAction.doIt();
            ExportTOCAction.doIt();
        }
    }

    /**
     * Sets the new property.
     * 
     * @param newIsNew
     *            boolean
     */
    public void setIsNew(boolean newIsNew) {
        isNew = newIsNew;
        if (JHelpDevFrame.HOMEDIR != null) getProjectDirField().setText(JHelpDevFrame.HOMEDIR); else getProjectDirField().setText("");
        getTopField().setText(defaultTopTarget);
        getPopupIconField().setText(defaultImage);
        getProjectNameField().setText(defaultProjectName);
    }

    /**
     * Sets all textfields with the respective settings.
     */
    public void setTextFields() {
        getProjectNameField().setText(ConfigHolder.CONF.getProjectName());
        getTopField().setText(ConfigHolder.CONF.getTopTarget());
        getProjectDirField().setText(ConfigHolder.CONF.getProjectDir());
        getPopupIconField().setText(ConfigHolder.CONF.getPopupImage());
        getEncodingsCombo().setSelectedItem(ConfigHolder.CONF.getXmlEncoding());
    }
}
