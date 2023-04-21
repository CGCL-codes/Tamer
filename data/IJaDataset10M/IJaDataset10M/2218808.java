package org.pointrel.pointrel20090201.configuration;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.pointrel.pointrel20090201.core.Session;

public class SessionConfigurationChooser extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel mainContentPane = null;

    private JPanel okCancelPanel = null;

    private JButton cancelButton = null;

    private JButton okButton = null;

    private JPanel summaryLabel = null;

    private JComboBox configurationComboBox = null;

    private JButton newButton = null;

    private JButton deleteButton = null;

    private JButton renameButton = null;

    private JLabel configurationLabel = null;

    private JButton copyButton = null;

    private JScrollPane summaryTextScrollPane = null;

    private JTextArea summaryTextArea = null;

    private JPanel renameCopyNewDeletePanel = null;

    private JLabel jLabel = null;

    private JLabel topLabel = null;

    private JButton changeArchivesButton = null;

    private JButton changeLicensesButton = null;

    private JButton changeAuthorsButton = null;

    private JPanel editItemPanel = null;

    private JPanel bottomPanel = null;

    boolean okStatus = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                SessionConfigurationChooser thisClass = new SessionConfigurationChooser(null);
                thisClass.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
                thisClass.setModal(true);
                thisClass.setVisible(true);
                System.exit(0);
            }
        });
    }

    public SessionConfigurationChooser(JFrame parent) {
        super(parent);
        initialize();
    }

    private void initialize() {
        this.setSize(697, 406);
        this.setModal(true);
        this.setContentPane(getMainContentPane());
        this.setTitle("Session Configuration Chooser");
        ArrayList<SessionConfiguration> configurations = SessionConfigurationUtilities.getInstance().sessionConfigurations;
        for (SessionConfiguration sessionConfiguration : configurations) {
            configurationComboBox.addItem(sessionConfiguration);
        }
    }

    public void setConfigurationList() {
        ArrayList<SessionConfiguration> configurations = SessionConfigurationUtilities.getInstance().sessionConfigurations;
        configurations.clear();
        for (int i = 0; i < configurationComboBox.getItemCount(); i++) {
            configurations.add((SessionConfiguration) configurationComboBox.getItemAt(i));
        }
    }

    public static Session askUserForSessionDetails() {
        System.out.println("Chooser is not fully functional yet; changes are not saved permanently; needs improving.");
        SessionConfigurationChooser chooser = new SessionConfigurationChooser(null);
        chooser.setVisible(true);
        if (!chooser.okStatus) return null;
        System.out.println("Proceeding...");
        SessionConfiguration sessionConfiguration = (SessionConfiguration) chooser.configurationComboBox.getSelectedItem();
        if (sessionConfiguration == null) return null;
        System.out.println("configuration type: " + sessionConfiguration.name);
        return sessionConfiguration.makeSession();
    }

    private JPanel getOkCancelPanel() {
        if (okCancelPanel == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(FlowLayout.RIGHT);
            flowLayout.setHgap(20);
            okCancelPanel = new JPanel();
            okCancelPanel.setLayout(flowLayout);
            okCancelPanel.add(getCancelButton(), null);
            okCancelPanel.add(getOkButton(), null);
        }
        return okCancelPanel;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    SessionConfigurationChooser.this.setVisible(false);
                }
            });
        }
        return cancelButton;
    }

    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText("OK");
            okButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    okStatus = true;
                    setConfigurationList();
                    SessionConfigurationChooser.this.setVisible(false);
                }
            });
        }
        return okButton;
    }

    private JPanel getSummaryLabel() {
        if (summaryLabel == null) {
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.gridx = 0;
            gridBagConstraints31.gridwidth = 3;
            gridBagConstraints31.gridy = 0;
            topLabel = new JLabel();
            topLabel.setText("Which configuration should be used for the current session?");
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 0;
            gridBagConstraints21.gridy = 3;
            jLabel = new JLabel();
            jLabel.setText("Summary (below)");
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
            gridBagConstraints23.gridx = 0;
            gridBagConstraints23.gridy = 1;
            configurationLabel = new JLabel();
            configurationLabel.setText("Configuration");
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
            gridBagConstraints19.fill = GridBagConstraints.BOTH;
            gridBagConstraints19.gridy = 1;
            gridBagConstraints19.gridx = 1;
            gridBagConstraints19.weightx = 1.0;
            summaryLabel = new JPanel();
            summaryLabel.setLayout(new GridBagLayout());
            summaryLabel.add(getConfigurationComboBox(), gridBagConstraints19);
            summaryLabel.add(configurationLabel, gridBagConstraints23);
            summaryLabel.add(getRenameCopyNewDeletePanel(), gridBagConstraints);
            summaryLabel.add(jLabel, gridBagConstraints21);
            summaryLabel.add(topLabel, gridBagConstraints31);
        }
        return summaryLabel;
    }

    private JComboBox getConfigurationComboBox() {
        if (configurationComboBox == null) {
            configurationComboBox = new JComboBox();
            configurationComboBox.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    updateSummary();
                }
            });
        }
        return configurationComboBox;
    }

    private JButton getNewButton() {
        if (newButton == null) {
            newButton = new JButton();
            newButton.setText("New...");
            newButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String name = JOptionPane.showInputDialog("Enter a name");
                    if (name == null) return;
                    SessionConfiguration sessionConfiguration = new SessionConfiguration();
                    sessionConfiguration.name = name;
                    configurationComboBox.addItem(sessionConfiguration);
                    configurationComboBox.setSelectedItem(sessionConfiguration);
                }
            });
        }
        return newButton;
    }

    private JButton getDeleteButton() {
        if (deleteButton == null) {
            deleteButton = new JButton();
            deleteButton.setText("Delete");
            deleteButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    SessionConfiguration sessionConfiguration = (SessionConfiguration) configurationComboBox.getSelectedItem();
                    int confirmResult = JOptionPane.showConfirmDialog(SessionConfigurationChooser.this, "Delete?\n" + sessionConfiguration.name);
                    if (confirmResult != JOptionPane.OK_OPTION) return;
                    configurationComboBox.removeItem(sessionConfiguration);
                }
            });
        }
        return deleteButton;
    }

    private JButton getRenameButton() {
        if (renameButton == null) {
            renameButton = new JButton();
            renameButton.setText("Rename...");
            renameButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    SessionConfiguration sessionConfiguration = (SessionConfiguration) configurationComboBox.getSelectedItem();
                    String name = JOptionPane.showInputDialog("Enter a name", sessionConfiguration.name);
                    if (name == null) return;
                    sessionConfiguration.name = name;
                    configurationComboBox.invalidate();
                }
            });
        }
        return renameButton;
    }

    private JButton getCopyButton() {
        if (copyButton == null) {
            copyButton = new JButton();
            copyButton.setText("Copy...");
            copyButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    SessionConfiguration sessionConfiguration = (SessionConfiguration) configurationComboBox.getSelectedItem();
                    String name = JOptionPane.showInputDialog("Enter a new name", "Copy of " + sessionConfiguration.name);
                    if (name == null) return;
                    SessionConfiguration newSessionConfiguration = (SessionConfiguration) sessionConfiguration.clone();
                    newSessionConfiguration.name = name;
                    configurationComboBox.addItem(newSessionConfiguration);
                    configurationComboBox.setSelectedItem(newSessionConfiguration);
                }
            });
        }
        return copyButton;
    }

    private JScrollPane getSummaryTextScrollPane() {
        if (summaryTextScrollPane == null) {
            summaryTextScrollPane = new JScrollPane();
            summaryTextScrollPane.setViewportView(getSummaryTextArea());
        }
        return summaryTextScrollPane;
    }

    private JTextArea getSummaryTextArea() {
        if (summaryTextArea == null) {
            summaryTextArea = new JTextArea();
            summaryTextArea.setLineWrap(true);
        }
        return summaryTextArea;
    }

    private JPanel getRenameCopyNewDeletePanel() {
        if (renameCopyNewDeletePanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = -1;
            gridBagConstraints4.gridy = -1;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = -1;
            gridBagConstraints3.gridy = -1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = -1;
            gridBagConstraints2.gridy = -1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = -1;
            gridBagConstraints1.gridy = -1;
            renameCopyNewDeletePanel = new JPanel();
            renameCopyNewDeletePanel.setLayout(new GridBagLayout());
            renameCopyNewDeletePanel.add(getRenameButton(), gridBagConstraints2);
            renameCopyNewDeletePanel.add(getCopyButton(), gridBagConstraints3);
            renameCopyNewDeletePanel.add(getNewButton(), gridBagConstraints1);
            renameCopyNewDeletePanel.add(getDeleteButton(), gridBagConstraints4);
        }
        return renameCopyNewDeletePanel;
    }

    private JButton getChangeArchivesButton() {
        if (changeArchivesButton == null) {
            changeArchivesButton = new JButton();
            changeArchivesButton.setText("Change Archives...");
            changeArchivesButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    changeArchivesPressed();
                }
            });
        }
        return changeArchivesButton;
    }

    private void changeArchivesPressed() {
        SessionConfiguration configuration = (SessionConfiguration) configurationComboBox.getSelectedItem();
        ArrayList<ArchiveAccessSpecificationWrapper> list = configuration.archiveAccessSpecificationWrappers;
        ListEditorTableModelForArchiveAccessSpecificationWrapper listEditorTableModel = new ListEditorTableModelForArchiveAccessSpecificationWrapper(list, SessionConfigurationUtilities.getInstance().archiveAccessSpecifications);
        ListEditor listEditor = new ListEditor("List Builder", listEditorTableModel, ListEditor.LAYOUT_GUI_FOR_BUILDING, true);
        listEditor.setVisible(true);
        if (listEditor.okResult) {
            System.out.println("OK result");
            updateSummary();
        }
    }

    private JButton getChangeLicensesButton() {
        if (changeLicensesButton == null) {
            changeLicensesButton = new JButton();
            changeLicensesButton.setText("Change Licenses...");
            changeLicensesButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    SessionConfiguration configuration = (SessionConfiguration) configurationComboBox.getSelectedItem();
                    LicenseUtilities.editLicenseList(SessionConfigurationChooser.this, configuration.licensesGranted);
                    updateSummary();
                }
            });
        }
        return changeLicensesButton;
    }

    private JButton getChangeAuthorsButton() {
        if (changeAuthorsButton == null) {
            changeAuthorsButton = new JButton();
            changeAuthorsButton.setText("Change Authors...");
            changeAuthorsButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    SessionConfiguration sessionConfiguration = (SessionConfiguration) configurationComboBox.getSelectedItem();
                    AuthorUtilities.editAuthorsList(SessionConfigurationChooser.this, sessionConfiguration.authors);
                    updateSummary();
                }
            });
        }
        return changeAuthorsButton;
    }

    private JPanel getEditItemPanel() {
        if (editItemPanel == null) {
            editItemPanel = new JPanel();
            editItemPanel.setLayout(new GridBagLayout());
            editItemPanel.add(getChangeAuthorsButton(), new GridBagConstraints());
            editItemPanel.add(getChangeLicensesButton(), new GridBagConstraints());
            editItemPanel.add(getChangeArchivesButton(), new GridBagConstraints());
        }
        return editItemPanel;
    }

    private JPanel getBottomPanel() {
        if (bottomPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = -1;
            gridBagConstraints5.gridy = -1;
            bottomPanel = new JPanel();
            bottomPanel.setLayout(new BoxLayout(getBottomPanel(), BoxLayout.Y_AXIS));
            bottomPanel.add(getEditItemPanel(), null);
            bottomPanel.add(getOkCancelPanel(), null);
        }
        return bottomPanel;
    }

    private JPanel getMainContentPane() {
        if (mainContentPane == null) {
            mainContentPane = new JPanel();
            mainContentPane.setLayout(new BorderLayout());
            mainContentPane.add(getBottomPanel(), BorderLayout.SOUTH);
            mainContentPane.add(getSummaryLabel(), BorderLayout.NORTH);
            mainContentPane.add(getSummaryTextScrollPane(), BorderLayout.CENTER);
        }
        return mainContentPane;
    }

    private void updateSummary() {
        SessionConfiguration configuration = (SessionConfiguration) configurationComboBox.getSelectedItem();
        if (configuration != null) {
            summaryTextArea.setText(configuration.summary());
        } else {
            summaryTextArea.setText("");
        }
    }
}
