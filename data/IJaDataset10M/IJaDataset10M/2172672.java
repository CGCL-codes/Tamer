package picasatagstopictures.util.logging;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.logging.*;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.File;
import java.net.URL;
import picasatagstopictures.util.OwnPreferences;

/**
 *
 * @author  wiedthom
 */
public class JFrameLogginConfigurator extends javax.swing.JFrame {

    private static final String FRAME_X = "frame_logging_configurator_frame_x";

    private static final String FRAME_Y = "frame_logging_configurator_frame_y";

    private static final String FRAME_W = "frame_logging_configurator_frame_w";

    private static final String FRAME_H = "frame_logging_configurator_frame_h";

    private static final String USE_FILE_HANDLER = "frame_logging_configurator_use_file_handler";

    private static final String USE_CONSOLE_HANDLER = "frame_logging_configurator_use_console_handler";

    private static final String LOG_LEVEL = "frame_logging_configurator_log_level";

    private static final String LOG_DIRECTORY = "frame_logging_configurator_log_directory";

    private static final String LOG_FILE_NAME = "frame_logging_configurator_log_file_name";

    private static final String LOG_FILE_COUNT = "frame_logging_configurator_log_file_count";

    private static final String LOG_FILE_SIZE = "frame_logging_configurator_log_file_size";

    private static final String LOG_FILE_APPEND = "frame_logging_configurator_log_file_append";

    private static final String FORMATTER = "frame_logging_configurator_log_formatter";

    private static final String LOG_CONFIG_FILE = "frame_logging_configurator_log_config_file";

    private static JFrameLogginConfigurator instance;

    private Logger logger;

    public static JFrameLogginConfigurator getInstance() {
        if (instance == null) {
            instance = new JFrameLogginConfigurator();
        }
        return instance;
    }

    /** Creates new form JFrameLogginConfigurator */
    private JFrameLogginConfigurator() {
        logger = Logger.getLogger(this.getClass().getName());
        initComponents();
        this.getRootPane().setDefaultButton(this.jButtonOK);
        this.fillLogLevels();
        this.fillFormaters();
        this.setDefaultValues();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanelMain = new javax.swing.JPanel();
        jPanelCheckboxes = new javax.swing.JPanel();
        jCheckBoxUseFileHandler = new javax.swing.JCheckBox();
        jCheckBoxUseConsoleHandler = new javax.swing.JCheckBox();
        jLabelLoglevel = new javax.swing.JLabel();
        jComboBoxLogLevel = new javax.swing.JComboBox();
        jPanelFileHandler = new javax.swing.JPanel();
        jLabelDirecory = new javax.swing.JLabel();
        jTextFieldDirectory = new javax.swing.JTextField();
        jButtonDirectory = new javax.swing.JButton();
        jButtonDirectoryDefault = new javax.swing.JButton();
        jLabelFileName = new javax.swing.JLabel();
        jTextFieldFileName = new javax.swing.JTextField();
        jButtonFileDefault = new javax.swing.JButton();
        jLabelMaxFileCount = new javax.swing.JLabel();
        jTextFieldMaxFileCount = new javax.swing.JTextField();
        jLabelMaxFileSize = new javax.swing.JLabel();
        jTextFieldMaxFileSize = new javax.swing.JTextField();
        jCheckBoxAppendFile = new javax.swing.JCheckBox();
        jPanelFormater = new javax.swing.JPanel();
        jLabelFormatter = new javax.swing.JLabel();
        jTextFieldFormatter = new javax.swing.JTextField();
        jComboBoxFormatter = new javax.swing.JComboBox();
        jPanelLoggingConfFile = new javax.swing.JPanel();
        jLabeLogConfigFile = new javax.swing.JLabel();
        jTextFieldLogConfigFile = new javax.swing.JTextField();
        jButtonLogConfigFile = new javax.swing.JButton();
        jButtonLoggingConfigFileDefault = new javax.swing.JButton();
        jPanelButtons = new javax.swing.JPanel();
        jButtonDefault = new javax.swing.JButton();
        jPanelSpace = new javax.swing.JPanel();
        jButtonCancel = new javax.swing.JButton();
        jButtonOK = new javax.swing.JButton();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        setTitle("Logging Configuration");
        addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        jPanelMain.setLayout(new java.awt.GridBagLayout());
        jPanelCheckboxes.setLayout(new java.awt.GridBagLayout());
        jCheckBoxUseFileHandler.setMnemonic('F');
        jCheckBoxUseFileHandler.setText("Write Messages to File");
        jCheckBoxUseFileHandler.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUseFileHandlerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelCheckboxes.add(jCheckBoxUseFileHandler, gridBagConstraints);
        jCheckBoxUseConsoleHandler.setMnemonic('W');
        jCheckBoxUseConsoleHandler.setText("Write Messages to the Console");
        jCheckBoxUseConsoleHandler.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUseConsoleHandlerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelCheckboxes.add(jCheckBoxUseConsoleHandler, gridBagConstraints);
        jLabelLoglevel.setText("Log Level");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelCheckboxes.add(jLabelLoglevel, gridBagConstraints);
        jComboBoxLogLevel.setMinimumSize(new java.awt.Dimension(100, 20));
        jComboBoxLogLevel.setPreferredSize(new java.awt.Dimension(100, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelCheckboxes.add(jComboBoxLogLevel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 11, 0);
        jPanelMain.add(jPanelCheckboxes, gridBagConstraints);
        jPanelFileHandler.setLayout(new java.awt.GridBagLayout());
        jPanelFileHandler.setBorder(new javax.swing.border.TitledBorder("Log Files"));
        jLabelDirecory.setText("Directory");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanelFileHandler.add(jLabelDirecory, gridBagConstraints);
        jTextFieldDirectory.setMinimumSize(new java.awt.Dimension(500, 20));
        jTextFieldDirectory.setPreferredSize(new java.awt.Dimension(500, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        jPanelFileHandler.add(jTextFieldDirectory, gridBagConstraints);
        jButtonDirectory.setText("...");
        jButtonDirectory.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDirectoryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanelFileHandler.add(jButtonDirectory, gridBagConstraints);
        jButtonDirectoryDefault.setText("Default");
        jButtonDirectoryDefault.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDirectoryDefaultActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanelFileHandler.add(jButtonDirectoryDefault, gridBagConstraints);
        jLabelFileName.setText("File Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanelFileHandler.add(jLabelFileName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        jPanelFileHandler.add(jTextFieldFileName, gridBagConstraints);
        jButtonFileDefault.setText("Default");
        jButtonFileDefault.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileDefaultActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanelFileHandler.add(jButtonFileDefault, gridBagConstraints);
        jLabelMaxFileCount.setText("Count");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanelFileHandler.add(jLabelMaxFileCount, gridBagConstraints);
        jTextFieldMaxFileCount.setMinimumSize(new java.awt.Dimension(100, 20));
        jTextFieldMaxFileCount.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanelFileHandler.add(jTextFieldMaxFileCount, gridBagConstraints);
        jLabelMaxFileSize.setText("Size (Byte)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanelFileHandler.add(jLabelMaxFileSize, gridBagConstraints);
        jTextFieldMaxFileSize.setMinimumSize(new java.awt.Dimension(100, 20));
        jTextFieldMaxFileSize.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanelFileHandler.add(jTextFieldMaxFileSize, gridBagConstraints);
        jCheckBoxAppendFile.setMnemonic('F');
        jCheckBoxAppendFile.setText("Append File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelFileHandler.add(jCheckBoxAppendFile, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanelMain.add(jPanelFileHandler, gridBagConstraints);
        jPanelFormater.setLayout(new java.awt.GridBagLayout());
        jPanelFormater.setBorder(new javax.swing.border.TitledBorder("Formatter"));
        jLabelFormatter.setText("Formatter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanelFormater.add(jLabelFormatter, gridBagConstraints);
        jTextFieldFormatter.setMinimumSize(new java.awt.Dimension(100, 20));
        jTextFieldFormatter.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanelFormater.add(jTextFieldFormatter, gridBagConstraints);
        jComboBoxFormatter.setMinimumSize(new java.awt.Dimension(300, 22));
        jComboBoxFormatter.setPreferredSize(new java.awt.Dimension(300, 22));
        jComboBoxFormatter.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFormatterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        jPanelFormater.add(jComboBoxFormatter, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelMain.add(jPanelFormater, gridBagConstraints);
        jPanelLoggingConfFile.setLayout(new java.awt.GridBagLayout());
        jPanelLoggingConfFile.setBorder(new javax.swing.border.TitledBorder("Configuration File"));
        jLabeLogConfigFile.setText("Store this Configuration in File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        jPanelLoggingConfFile.add(jLabeLogConfigFile, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        jPanelLoggingConfFile.add(jTextFieldLogConfigFile, gridBagConstraints);
        jButtonLogConfigFile.setText("...");
        jButtonLogConfigFile.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogConfigFileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        jPanelLoggingConfFile.add(jButtonLogConfigFile, gridBagConstraints);
        jButtonLoggingConfigFileDefault.setText("Default");
        jButtonLoggingConfigFileDefault.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoggingConfigFileDefaultActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanelLoggingConfFile.add(jButtonLoggingConfigFileDefault, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelMain.add(jPanelLoggingConfFile, gridBagConstraints);
        jPanelButtons.setLayout(new java.awt.GridBagLayout());
        jButtonDefault.setMnemonic('D');
        jButtonDefault.setText("Default");
        jButtonDefault.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDefaultActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        jPanelButtons.add(jButtonDefault, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelButtons.add(jPanelSpace, gridBagConstraints);
        jButtonCancel.setMnemonic('C');
        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 7);
        jPanelButtons.add(jButtonCancel, gridBagConstraints);
        jButtonOK.setMnemonic('O');
        jButtonOK.setText("OK");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        jPanelButtons.add(jButtonOK, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        jPanelMain.add(jPanelButtons, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 12, 12);
        getContentPane().add(jPanelMain, gridBagConstraints);
        pack();
    }

    private void jCheckBoxUseConsoleHandlerActionPerformed(java.awt.event.ActionEvent evt) {
        this.enableDisableComponents();
    }

    private void jCheckBoxUseFileHandlerActionPerformed(java.awt.event.ActionEvent evt) {
        this.enableDisableComponents();
    }

    private void formComponentShown(java.awt.event.ComponentEvent evt) {
        this.loadUserPrefsGUI();
    }

    private void jButtonLogConfigFileActionPerformed(java.awt.event.ActionEvent evt) {
        String file = this.jTextFieldLogConfigFile.getText();
        String f = this.chooseFile(file);
        if (f != null) {
            this.jTextFieldLogConfigFile.setText(f);
        }
    }

    private void jButtonDirectoryActionPerformed(java.awt.event.ActionEvent evt) {
        String directory = this.jTextFieldDirectory.getText();
        String dir = this.chooseDirectory(directory);
        if (dir != null) {
            this.jTextFieldDirectory.setText(dir);
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        this.closeMySelf();
    }

    private void jButtonLoggingConfigFileDefaultActionPerformed(java.awt.event.ActionEvent evt) {
        this.setLoggingConfigurationFileToDefault();
    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.closeMySelf();
    }

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {
        if (!this.save()) {
            return;
        }
        this.closeMySelf();
    }

    private void jComboBoxFormatterActionPerformed(java.awt.event.ActionEvent evt) {
        String value = (String) this.jComboBoxFormatter.getSelectedItem();
        this.jTextFieldFormatter.setText(value);
    }

    private void jButtonDefaultActionPerformed(java.awt.event.ActionEvent evt) {
        this.setDefaultValues();
        this.enableDisableComponents();
    }

    private void jButtonDirectoryDefaultActionPerformed(java.awt.event.ActionEvent evt) {
        this.setDirectoryToDefault();
    }

    private void jButtonFileDefaultActionPerformed(java.awt.event.ActionEvent evt) {
        this.setFileNameToDefault();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new JFrameLogginConfigurator().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButtonCancel;

    private javax.swing.JButton jButtonDefault;

    private javax.swing.JButton jButtonDirectory;

    private javax.swing.JButton jButtonDirectoryDefault;

    private javax.swing.JButton jButtonFileDefault;

    private javax.swing.JButton jButtonLogConfigFile;

    private javax.swing.JButton jButtonLoggingConfigFileDefault;

    private javax.swing.JButton jButtonOK;

    private javax.swing.JCheckBox jCheckBoxAppendFile;

    private javax.swing.JCheckBox jCheckBoxUseConsoleHandler;

    private javax.swing.JCheckBox jCheckBoxUseFileHandler;

    private javax.swing.JComboBox jComboBoxFormatter;

    private javax.swing.JComboBox jComboBoxLogLevel;

    private javax.swing.JLabel jLabeLogConfigFile;

    private javax.swing.JLabel jLabelDirecory;

    private javax.swing.JLabel jLabelFileName;

    private javax.swing.JLabel jLabelFormatter;

    private javax.swing.JLabel jLabelLoglevel;

    private javax.swing.JLabel jLabelMaxFileCount;

    private javax.swing.JLabel jLabelMaxFileSize;

    private javax.swing.JPanel jPanelButtons;

    private javax.swing.JPanel jPanelCheckboxes;

    private javax.swing.JPanel jPanelFileHandler;

    private javax.swing.JPanel jPanelFormater;

    private javax.swing.JPanel jPanelLoggingConfFile;

    private javax.swing.JPanel jPanelMain;

    private javax.swing.JPanel jPanelSpace;

    private javax.swing.JTextField jTextFieldDirectory;

    private javax.swing.JTextField jTextFieldFileName;

    private javax.swing.JTextField jTextFieldFormatter;

    private javax.swing.JTextField jTextFieldLogConfigFile;

    private javax.swing.JTextField jTextFieldMaxFileCount;

    private javax.swing.JTextField jTextFieldMaxFileSize;

    public static Image getImage(String url) {
        URL u = null;
        try {
            u = JFrameLogginConfigurator.class.getResource(url);
            String file = u.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Image image = Toolkit.getDefaultToolkit().getImage(u);
        return image;
    }

    private void setFileNameToDefault() {
        logger.finest("Set default value in text field for file name to 'java.%g.log' ...");
        this.jTextFieldFileName.setText("java.%g.log");
    }

    private void setDirectoryToDefault() {
        String userHome = System.getProperty("user.home");
        logger.finest("Set default value in text field for directory to '" + userHome + "' ...");
        this.jTextFieldDirectory.setText(userHome);
    }

    private void setLoggingConfigurationFileToDefault() {
        LoggingInitializer initializer = new LoggingInitializer();
        String file = initializer.getLoggingConfigurationFile();
        if (file == null) {
            String userHome = System.getProperty("user.home");
            file = userHome + File.separator + LoggingInitializer.FILE_NAME;
        }
        this.jTextFieldLogConfigFile.setText(file);
    }

    private void setDefaultValues() {
        this.jCheckBoxUseFileHandler.setSelected(false);
        this.jCheckBoxUseConsoleHandler.setSelected(false);
        this.jComboBoxLogLevel.setSelectedItem("INFO");
        this.setDirectoryToDefault();
        this.setFileNameToDefault();
        this.jTextFieldMaxFileCount.setText("10");
        this.jTextFieldMaxFileSize.setText("10000000");
        this.jCheckBoxAppendFile.setSelected(false);
        this.jComboBoxFormatter.setSelectedItem("picasatagstopictures.util.logging.SingleLineFormatter");
        this.setLoggingConfigurationFileToDefault();
    }

    private void fillLogLevels() {
        logger.finest("Fill loglevels...");
        this.jComboBoxLogLevel.addItem("OFF");
        this.jComboBoxLogLevel.addItem("SEVERE");
        this.jComboBoxLogLevel.addItem("WARNING");
        this.jComboBoxLogLevel.addItem("CONFIG");
        this.jComboBoxLogLevel.addItem("INFO");
        this.jComboBoxLogLevel.addItem("FINE");
        this.jComboBoxLogLevel.addItem("FINER");
        this.jComboBoxLogLevel.addItem("FINEST");
        this.jComboBoxLogLevel.addItem("ALL");
    }

    private void enableDisableComponents() {
        this.jTextFieldDirectory.setEnabled(this.jCheckBoxUseFileHandler.isSelected());
        this.jButtonDirectory.setEnabled(this.jCheckBoxUseFileHandler.isSelected());
        this.jButtonDirectoryDefault.setEnabled(this.jCheckBoxUseFileHandler.isSelected());
        this.jTextFieldFileName.setEnabled(this.jCheckBoxUseFileHandler.isSelected());
        this.jButtonFileDefault.setEnabled(this.jCheckBoxUseFileHandler.isSelected());
        this.jTextFieldMaxFileCount.setEnabled(this.jCheckBoxUseFileHandler.isSelected());
        this.jTextFieldMaxFileSize.setEnabled(this.jCheckBoxUseFileHandler.isSelected());
        this.jCheckBoxAppendFile.setEnabled(this.jCheckBoxUseFileHandler.isSelected());
        boolean enable = true;
        if (!this.jCheckBoxUseFileHandler.isSelected() && !this.jCheckBoxUseConsoleHandler.isSelected()) {
            enable = false;
        }
        this.jComboBoxLogLevel.setEnabled(enable);
        this.jTextFieldFormatter.setEnabled(enable);
        this.jComboBoxFormatter.setEnabled(enable);
        this.jTextFieldLogConfigFile.setEnabled(enable);
        this.jButtonLogConfigFile.setEnabled(enable);
        this.jButtonLoggingConfigFileDefault.setEnabled(enable);
    }

    private void fillFormaters() {
        this.jComboBoxFormatter.addItem("picasatagstopictures.util.logging.SingleLineFormatter");
        this.jComboBoxFormatter.addItem("java.util.logging.SimpleFormatter");
        this.jComboBoxFormatter.addItem("java.util.logging.XMLFormatter");
    }

    /**
     * Loads the stored frame position and dimension or packs if never used.
     * Fills the components like text fielsd. Leaves the default values if no
     * value is found in the user preferences. The values where set prior to this
     * operation to default values.
     */
    private void loadUserPrefsGUI() {
        OwnPreferences prefs = OwnPreferences.userNodeForPackage(this.getClass());
        int x = prefs.getInt(JFrameLogginConfigurator.FRAME_X, 10);
        int y = prefs.getInt(JFrameLogginConfigurator.FRAME_Y, 10);
        int w = prefs.getInt(JFrameLogginConfigurator.FRAME_W, 10);
        int h = prefs.getInt(JFrameLogginConfigurator.FRAME_H, 10);
        String s = prefs.get(JFrameLogginConfigurator.USE_FILE_HANDLER, "");
        if (!"".equals(s)) {
            this.jCheckBoxUseFileHandler.setSelected(false);
            if (s.equalsIgnoreCase("true")) {
                this.jCheckBoxUseFileHandler.setSelected(true);
            }
        }
        s = prefs.get(JFrameLogginConfigurator.USE_CONSOLE_HANDLER, "");
        if (!"".equals(s)) {
            this.jCheckBoxUseConsoleHandler.setSelected(false);
            if (s.equalsIgnoreCase("true")) {
                this.jCheckBoxUseConsoleHandler.setSelected(true);
            }
        }
        s = prefs.get(JFrameLogginConfigurator.LOG_LEVEL, "");
        if (!"".equals(s)) {
            this.jComboBoxLogLevel.setSelectedItem(s);
        }
        s = prefs.get(JFrameLogginConfigurator.LOG_DIRECTORY, "");
        if (!"".equals(s)) {
            this.jTextFieldDirectory.setText(s);
        }
        s = prefs.get(JFrameLogginConfigurator.LOG_FILE_NAME, "");
        if (!"".equals(s)) {
            this.jTextFieldFileName.setText(s);
        }
        s = prefs.get(JFrameLogginConfigurator.LOG_FILE_COUNT, "");
        if (!"".equals(s)) {
            this.jTextFieldMaxFileCount.setText(s);
        }
        s = prefs.get(JFrameLogginConfigurator.LOG_FILE_SIZE, "");
        if (!"".equals(s)) {
            this.jTextFieldMaxFileSize.setText(s);
        }
        s = prefs.get(JFrameLogginConfigurator.LOG_FILE_APPEND, "");
        if (!"".equals(s)) {
            this.jCheckBoxAppendFile.setSelected(false);
            if (s.equalsIgnoreCase("true")) {
                this.jCheckBoxAppendFile.setSelected(true);
            }
        }
        s = prefs.get(JFrameLogginConfigurator.FORMATTER, "");
        if (!"".equals(s)) {
            this.jTextFieldFormatter.setText(s);
        }
        s = prefs.get(JFrameLogginConfigurator.LOG_CONFIG_FILE, "");
        if (!"".equals(s)) {
            this.jTextFieldLogConfigFile.setText(s);
        }
        this.pack();
        if (x == 10 && y == 10) {
            this.setLocationRelativeTo(null);
        } else {
            this.setLocation(x, y);
            this.setSize(w, h);
        }
        this.enableDisableComponents();
    }

    /**
     * Checks wether the class for the Formatter (given in GUI)
     * exists
     * @return true if the class for the formatter is in the classpath.
     */
    private boolean checkFormatterClass() {
        String className = this.jTextFieldFormatter.getText();
        boolean classWasFound = false;
        try {
            Class c = Class.forName(className);
            classWasFound = true;
        } catch (java.lang.ClassNotFoundException e) {
            logger.fine("Class for foramtter not found in classpath: '" + className + "'.");
        }
        return classWasFound;
    }

    private boolean save() {
        boolean classWasFound = this.checkFormatterClass();
        if (!classWasFound) {
            String className = this.jTextFieldFormatter.getText();
            this.popError("Formatter class not found in classpath.\n\n" + "Class name is:\n" + className);
        }
        OwnPreferences prefs = OwnPreferences.userNodeForPackage(this.getClass());
        boolean b = this.jCheckBoxUseFileHandler.isSelected();
        prefs.put(JFrameLogginConfigurator.USE_FILE_HANDLER, Boolean.toString(b));
        b = this.jCheckBoxUseConsoleHandler.isSelected();
        prefs.put(JFrameLogginConfigurator.USE_CONSOLE_HANDLER, Boolean.toString(b));
        String s = (String) this.jComboBoxLogLevel.getSelectedItem();
        prefs.put(JFrameLogginConfigurator.LOG_LEVEL, s);
        s = this.jTextFieldDirectory.getText();
        prefs.put(JFrameLogginConfigurator.LOG_DIRECTORY, s);
        s = this.jTextFieldFileName.getText();
        prefs.put(JFrameLogginConfigurator.LOG_FILE_NAME, s);
        s = this.jTextFieldMaxFileCount.getText();
        prefs.put(JFrameLogginConfigurator.LOG_FILE_COUNT, s);
        s = this.jTextFieldMaxFileSize.getText();
        prefs.put(JFrameLogginConfigurator.LOG_FILE_SIZE, s);
        b = this.jCheckBoxAppendFile.isSelected();
        prefs.put(JFrameLogginConfigurator.LOG_FILE_APPEND, Boolean.toString(b));
        s = this.jTextFieldFormatter.getText();
        prefs.put(JFrameLogginConfigurator.FORMATTER, s);
        s = this.jTextFieldLogConfigFile.getText();
        prefs.put(JFrameLogginConfigurator.LOG_CONFIG_FILE, s);
        return this.modifyLoggingConfigurationFile();
    }

    /**
     * Replaces value with values from GUI in logging.properties
     */
    private boolean modifyLoggingConfigurationFile() {
        this.logger.finer("About to modify configuration file for logging...");
        LoggingInitializer initializer = new LoggingInitializer();
        String file = initializer.getLoggingConfigurationFile();
        if (file == null) {
            this.popError("Could not find a file 'logging.properties'.");
            this.logger.fine("Could not find a file 'logging.properties'.");
            return false;
        }
        picasatagstopictures.util.FileUtil util = new picasatagstopictures.util.FileUtil();
        picasatagstopictures.util.PropertiesReader propsReader = new picasatagstopictures.util.PropertiesReader();
        String fileFromGUI = this.jTextFieldLogConfigFile.getText();
        if (!file.equals(fileFromGUI)) {
            try {
                util.copyFile(file, fileFromGUI);
            } catch (Exception e) {
                this.popError("Failed to copy old config file.\n\n" + "Source file is:\n" + file + "\n\nTarget file is:\n" + fileFromGUI + "\n\nOriginal error message is:\n" + e.getMessage());
                return false;
            }
        }
        this.logger.finer("About to modify configuration file '" + fileFromGUI + "'...");
        this.logger.finest("Modify FileHandler and ConsoleHandler...");
        String checkSearchExpression = "handlers";
        String result = null;
        try {
            result = propsReader.getProperty(fileFromGUI, checkSearchExpression);
        } catch (Exception e) {
            this.popError("Failed to find a key in the logging config file.\n\n" + "The key is:\n" + fileFromGUI + "'\n\n" + "The file is:\n" + fileFromGUI + "'");
            return false;
        }
        if (result == null) {
            try {
                util.appendLineToFile(file, "handlers=");
            } catch (Exception e) {
                this.popError("Failed to append a line to file.\n\n'" + fileFromGUI + "'");
                return false;
            }
        }
        String appendReplacementSearchExpression = "(handlers\\s*?=)(.*)";
        String toAppend = "";
        if (this.jCheckBoxUseConsoleHandler.isSelected()) {
            toAppend = "java.util.logging.ConsoleHandler";
        }
        if (this.jCheckBoxUseFileHandler.isSelected()) {
            if (!toAppend.equals("")) {
                toAppend = toAppend + ",java.util.logging.FileHandler";
            } else {
                toAppend = "java.util.logging.FileHandler";
            }
        }
        String replacement = "$1" + toAppend;
        picasatagstopictures.util.AppendReplacementInFile replacer = new picasatagstopictures.util.AppendReplacementInFile(fileFromGUI, appendReplacementSearchExpression, replacement);
        try {
            replacer.replace();
        } catch (Exception e) {
            this.popError("Failed to replace a value in a file.\n\n" + "The file is\n'" + fileFromGUI + "'\n\n" + "The search expression is\n'" + appendReplacementSearchExpression + "'\n\n" + "The replacement is\n'" + replacement + "'");
            return false;
        }
        this.logger.finer("About to modify loglevel in file: '" + fileFromGUI + "'...");
        checkSearchExpression = "\\W*\\.level|java\\.util\\.logging\\.ConsoleHandler\\.level";
        result = null;
        try {
            result = propsReader.getProperty(fileFromGUI, checkSearchExpression);
        } catch (Exception e) {
            this.popError("Failed to find a key in the logging config file.\n\n" + "The key is:\n" + fileFromGUI + "'\n\n" + "The file is:\n" + fileFromGUI + "'");
            return false;
        }
        if (result == null) {
            try {
                util.appendLineToFile(file, ".level=");
            } catch (Exception e) {
                this.popError("Failed to append a line to file.\n\n'" + fileFromGUI + "'");
                return false;
            }
        }
        appendReplacementSearchExpression = "([^\\w]\\.level\\s*?=|java\\.util\\.logging\\.ConsoleHandler\\.level\\s*?=)(.*)";
        toAppend = (String) this.jComboBoxLogLevel.getSelectedItem();
        replacement = "$1" + toAppend;
        replacer = new picasatagstopictures.util.AppendReplacementInFile(fileFromGUI, appendReplacementSearchExpression, replacement);
        try {
            replacer.replace();
        } catch (Exception e) {
            this.popError("Failed to replace a value in a file.\n\n" + "The file is\n'" + fileFromGUI + "'\n\n" + "The search expression is\n'" + appendReplacementSearchExpression + "'\n\n" + "The replacement is\n'" + replacement + "'");
            return false;
        }
        this.logger.finer("About to modify FileHandler pattern in file: '" + fileFromGUI + "'...");
        checkSearchExpression = "java\\.util\\.logging\\.FileHandler\\.pattern";
        result = null;
        try {
            result = propsReader.getProperty(fileFromGUI, checkSearchExpression);
        } catch (Exception e) {
            this.popError("Failed to find a key in the logging config file.\n\n" + "The key is:\n" + fileFromGUI + "'\n\n" + "The file is:\n" + fileFromGUI + "'");
            return false;
        }
        if (result == null) {
            try {
                util.appendLineToFile(file, "java.util.logging.FileHandler.pattern=");
            } catch (Exception e) {
                this.popError("Failed to append a line to file.\n\n'" + fileFromGUI + "'");
                return false;
            }
        }
        appendReplacementSearchExpression = "(java\\.util\\.logging\\.FileHandler\\.pattern\\s*?=)(.*)";
        String directory = this.jTextFieldDirectory.getText();
        String fileName = this.jTextFieldFileName.getText();
        toAppend = directory + File.separator + fileName;
        File tmpFile = new File(directory);
        if (!tmpFile.exists()) {
            this.popError("The directory for the file handler does not exist.\n\n'" + directory + "'");
            return false;
        }
        replacement = "$1" + toAppend;
        replacement = replacement.replaceAll("\\\\", "/");
        replacer = new picasatagstopictures.util.AppendReplacementInFile(fileFromGUI, appendReplacementSearchExpression, replacement);
        try {
            replacer.replace();
        } catch (Exception e) {
            this.popError("Failed to replace a value in a file.\n\n" + "The file is\n'" + fileFromGUI + "'\n\n" + "The search expression is\n'" + appendReplacementSearchExpression + "'\n\n" + "The replacement is\n'" + replacement + "'");
            return false;
        }
        this.logger.finer("About to modify file count in file: '" + fileFromGUI + "'...");
        checkSearchExpression = "java\\.util\\.logging\\.FileHandler\\.count";
        result = null;
        try {
            result = propsReader.getProperty(fileFromGUI, checkSearchExpression);
        } catch (Exception e) {
            this.popError("Failed to find a key in the logging config file.\n\n" + "The key is:\n" + fileFromGUI + "'\n\n" + "The file is:\n" + fileFromGUI + "'");
            return false;
        }
        if (result == null) {
            try {
                util.appendLineToFile(file, "java.util.logging.FileHandler.count=");
            } catch (Exception e) {
                this.popError("Failed to append a line to file.\n\n'" + fileFromGUI + "'");
                return false;
            }
        }
        appendReplacementSearchExpression = "(java\\.util\\.logging\\.FileHandler\\.count\\s*?=)(.*)";
        toAppend = this.jTextFieldMaxFileCount.getText();
        try {
            Integer.parseInt(toAppend);
        } catch (Exception e) {
            this.popError("Failed parse file count.\n\n'" + toAppend + "'");
            return false;
        }
        replacement = "$1" + toAppend;
        replacer = new picasatagstopictures.util.AppendReplacementInFile(fileFromGUI, appendReplacementSearchExpression, replacement);
        try {
            replacer.replace();
        } catch (Exception e) {
            this.popError("Failed to replace a value in a file.\n\n" + "The file is\n'" + fileFromGUI + "'\n\n" + "The search expression is\n'" + appendReplacementSearchExpression + "'\n\n" + "The replacement is\n'" + replacement + "'");
            return false;
        }
        this.logger.finer("About to modify file size in file: '" + fileFromGUI + "'...");
        checkSearchExpression = "java\\.util\\.logging\\.FileHandler\\.limit";
        result = null;
        try {
            result = propsReader.getProperty(fileFromGUI, checkSearchExpression);
        } catch (Exception e) {
            this.popError("Failed to find a key in the logging config file.\n\n" + "The key is:\n" + fileFromGUI + "'\n\n" + "The file is:\n" + fileFromGUI + "'");
            return false;
        }
        if (result == null) {
            try {
                util.appendLineToFile(file, "java.util.logging.FileHandler.limit=");
            } catch (Exception e) {
                this.popError("Failed to append a line to file.\n\n'" + fileFromGUI + "'");
                return false;
            }
        }
        appendReplacementSearchExpression = "(java\\.util\\.logging\\.FileHandler\\.limit\\s*?=)(.*)";
        toAppend = this.jTextFieldMaxFileSize.getText();
        int size = 0;
        try {
            size = Integer.parseInt(toAppend);
        } catch (Exception e) {
            this.popError("Failed parse file count.\n\n'" + toAppend + "'");
            return false;
        }
        if (size < 10000) {
            this.popError("Your file size is less than 10 KB.\n\n'" + size + "'");
            return false;
        }
        if (size > 10000000) {
            this.popError("Your file size is greater than 10 MB.\n\n'" + size + "'");
            return false;
        }
        replacement = "$1" + toAppend;
        replacer = new picasatagstopictures.util.AppendReplacementInFile(fileFromGUI, appendReplacementSearchExpression, replacement);
        try {
            replacer.replace();
        } catch (Exception e) {
            this.popError("Failed to replace a value in a file.\n\n" + "The file is\n'" + fileFromGUI + "'\n\n" + "The search expression is\n'" + appendReplacementSearchExpression + "'\n\n" + "The replacement is\n'" + replacement + "'");
            return false;
        }
        this.logger.finer("About to modify 'append file' in file: '" + fileFromGUI + "'...");
        checkSearchExpression = "java\\.util\\.logging\\.FileHandler\\.append";
        result = null;
        try {
            result = propsReader.getProperty(fileFromGUI, checkSearchExpression);
        } catch (Exception e) {
            this.popError("Failed to find a key in the logging config file.\n\n" + "The key is:\n" + fileFromGUI + "'\n\n" + "The file is:\n" + fileFromGUI + "'");
            return false;
        }
        if (result == null) {
            try {
                util.appendLineToFile(file, "java.util.logging.FileHandler.append=");
            } catch (Exception e) {
                this.popError("Failed to append a line to file.\n\n'" + fileFromGUI + "'");
                return false;
            }
        }
        appendReplacementSearchExpression = "(java\\.util\\.logging\\.FileHandler\\.append\\s*?=)(.*)";
        boolean b = this.jCheckBoxAppendFile.isSelected();
        toAppend = Boolean.toString(b);
        replacement = "$1" + toAppend;
        replacer = new picasatagstopictures.util.AppendReplacementInFile(fileFromGUI, appendReplacementSearchExpression, replacement);
        try {
            replacer.replace();
        } catch (Exception e) {
            this.popError("Failed to replace a value in a file.\n\n" + "The file is\n'" + fileFromGUI + "'\n\n" + "The search expression is\n'" + appendReplacementSearchExpression + "'\n\n" + "The replacement is\n'" + replacement + "'");
            return false;
        }
        this.logger.finer("About to modify the file formatter in file: '" + fileFromGUI + "'...");
        checkSearchExpression = "java\\.util\\.logging\\.FileHandler\\.formatter";
        result = null;
        try {
            result = propsReader.getProperty(fileFromGUI, checkSearchExpression);
        } catch (Exception e) {
            this.popError("Failed to find a key in the logging config file.\n\n" + "The key is:\n" + fileFromGUI + "'\n\n" + "The file is:\n" + fileFromGUI + "'");
            return false;
        }
        if (result == null) {
            try {
                util.appendLineToFile(file, "java.util.logging.FileHandler.formatter=");
            } catch (Exception e) {
                this.popError("Failed to append a line to file.\n\n'" + fileFromGUI + "'");
                return false;
            }
        }
        appendReplacementSearchExpression = "(java\\.util\\.logging\\.FileHandler\\.formatter\\s*?=)(.*)";
        toAppend = this.jTextFieldFormatter.getText();
        replacement = "$1" + toAppend;
        replacer = new picasatagstopictures.util.AppendReplacementInFile(fileFromGUI, appendReplacementSearchExpression, replacement);
        try {
            replacer.replace();
        } catch (Exception e) {
            this.popError("Failed to replace a value in a file.\n\n" + "The file is\n'" + fileFromGUI + "'\n\n" + "The search expression is\n'" + appendReplacementSearchExpression + "'\n\n" + "The replacement is\n'" + replacement + "'");
            return false;
        }
        this.logger.finer("About to modify the console formatter in file: '" + fileFromGUI + "'...");
        checkSearchExpression = "java\\.util\\.logging\\.ConsoleHandler\\.formatter";
        result = null;
        try {
            result = propsReader.getProperty(fileFromGUI, checkSearchExpression);
        } catch (Exception e) {
            this.popError("Failed to find a key in the logging config file.\n\n" + "The key is:\n" + fileFromGUI + "'\n\n" + "The file is:\n" + fileFromGUI + "'");
            return false;
        }
        if (result == null) {
            try {
                util.appendLineToFile(file, "java.util.logging.ConsoleHandler.formatter=");
            } catch (Exception e) {
                this.popError("Failed to append a line to file.\n\n'" + fileFromGUI + "'");
                return false;
            }
        }
        appendReplacementSearchExpression = "(java\\.util\\.logging\\.ConsoleHandler\\.formatter\\s*?=)(.*)";
        toAppend = this.jTextFieldFormatter.getText();
        replacement = "$1" + toAppend;
        replacer = new picasatagstopictures.util.AppendReplacementInFile(fileFromGUI, appendReplacementSearchExpression, replacement);
        try {
            replacer.replace();
        } catch (Exception e) {
            this.popError("Failed to replace a value in a file.\n\n" + "The file is\n'" + fileFromGUI + "'\n\n" + "The search expression is\n'" + appendReplacementSearchExpression + "'\n\n" + "The replacement is\n'" + replacement + "'");
            return false;
        }
        return this.reloadLoggingConfiguration(fileFromGUI);
    }

    /**
     * Reloads the logging configuration using the modified 
     * file logging.properties.
     */
    private boolean reloadLoggingConfiguration(String fileFromGUI) {
        LoggingInitializer initializer = new LoggingInitializer();
        if (fileFromGUI == null) {
            this.popError("Could not find a file '" + fileFromGUI + "'.");
            this.logger.fine("Could not find a file '" + fileFromGUI + "'.");
            return false;
        }
        try {
            initializer.initLogging(fileFromGUI);
        } catch (Exception e) {
            this.popError("Failed to reload logging configuration\n\n" + "The original error message is:\n'" + e.getMessage() + "'");
            return false;
        }
        return true;
    }

    private String chooseFile(String f) {
        File file = null;
        File dir = null;
        if (f != null && !"".equals(f)) {
            file = new File(f);
            if (file.exists()) {
            } else {
                dir = file.getParentFile();
                file = null;
            }
        }
        JFileChooser chooser = new JFileChooser();
        if (file != null) {
            chooser.setSelectedFile(file);
        }
        if (dir != null) {
            chooser.setCurrentDirectory(dir);
        }
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String choosenFile = chooser.getSelectedFile().getAbsolutePath();
            return choosenFile;
        }
        return null;
    }

    private String chooseDirectory(String directory) {
        File dir = null;
        if (directory != null && !"".equals(directory)) {
            dir = new File(directory);
            if (!dir.exists()) {
                dir = null;
            }
        }
        JFileChooser chooser = new JFileChooser();
        if (dir != null) {
            chooser.setCurrentDirectory(dir);
        }
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String file = chooser.getSelectedFile().getAbsolutePath();
            return file;
        }
        return null;
    }

    private void saveGUISettings() {
        logger.finer("Saving GUI settings (position, size) to user preferences...");
        int x = this.getX();
        int y = this.getY();
        java.awt.Dimension d = this.getSize();
        int h = (int) d.getHeight();
        int w = (int) d.getWidth();
        OwnPreferences prefs = OwnPreferences.userNodeForPackage(this.getClass());
        prefs.putInt(JFrameLogginConfigurator.FRAME_X, x);
        prefs.putInt(JFrameLogginConfigurator.FRAME_Y, y);
        prefs.putInt(JFrameLogginConfigurator.FRAME_H, h);
        prefs.putInt(JFrameLogginConfigurator.FRAME_W, w);
    }

    private void closeMySelf() {
        logger.finer("About to close frame...");
        this.saveGUISettings();
        logger.fine("Hiding...");
        this.setVisible(false);
    }

    private void popError(String message) {
        logger.finer("Pop-up error message... '" + message + "'");
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
