package net.diet_rich.jabak.main.jabak;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import net.diet_rich.jabak.main.GUICommon;
import net.diet_rich.jabak.main.GUISettings;
import net.diet_rich.util.ChangeListener;
import net.diet_rich.util.Settings;
import net.diet_rich.util.io.GZipCountStream;

public class JabakGUI {

    private GUISettings stringSettings;

    private JabakSettings settings;

    private JFrame jabakGUI = null;

    private JPanel jabakGUIContentPane = null;

    private JTabbedPane jabakGUITabbedPane = null;

    private JPanel basicSettingsPanel = null;

    private JPanel advancedSettingsPanel = null;

    private JPanel progressPanel = null;

    private JLabel sourceLabel = null;

    private JTextField sourceTextField = null;

    private JButton sourceChooseButton = null;

    private JLabel targetLabel = null;

    private JTextField targetTextField = null;

    private JButton targetChooseButton = null;

    private JLabel backupSizeLabel = null;

    private JTextField backupSizeTextField = null;

    private JLabel dataFileSizeLabel = null;

    private JTextField dataFileSizeTextField = null;

    private JLabel backupNameLabel = null;

    private JLabel commentLabel = null;

    private JTextField backupNameTextField = null;

    private JTextArea commentTextArea = null;

    private JScrollPane commentScrollPane = null;

    private JLabel databaseLabel = null;

    private JTextField loadDBTextField = null;

    private JButton loadDBChooseButton = null;

    private JButton startButton = null;

    private JTextArea MessageTextArea = null;

    private JScrollPane MessageScrollPane = null;

    private JPanel progressGUIPanel = null;

    /**
	 * This method initializes jFrame	
	 * 	
	 * @return javax.swing.JFrame	
	 */
    private JFrame getJabakGUI() {
        if (jabakGUI == null) {
            jabakGUI = new JFrame();
            jabakGUI.setSize(600, 450);
            jabakGUI.setLocationRelativeTo(null);
            jabakGUI.setResizable(false);
            jabakGUI.setTitle("jabak Java Backup");
            jabakGUI.setContentPane(getJabakGUIContentPane());
            jabakGUI.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
        return jabakGUI;
    }

    /**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJabakGUIContentPane() {
        if (jabakGUIContentPane == null) {
            jabakGUIContentPane = new JPanel();
            jabakGUIContentPane.setLayout(new BorderLayout());
            jabakGUIContentPane.add(getJabakGUITabbedPane(), BorderLayout.CENTER);
        }
        return jabakGUIContentPane;
    }

    /**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
    private JTabbedPane getJabakGUITabbedPane() {
        if (jabakGUITabbedPane == null) {
            jabakGUITabbedPane = new JTabbedPane();
            jabakGUITabbedPane.addTab("basic", null, getBasicSettingsPanel(), null);
            jabakGUITabbedPane.addTab("advanced", null, getAdvancedSettingsPanel(), null);
            jabakGUITabbedPane.addTab("run", null, getRunPanel(), null);
        }
        return jabakGUITabbedPane;
    }

    /**
	 * This method initializes basicSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getBasicSettingsPanel() {
        if (basicSettingsPanel == null) {
            databaseLabel = new JLabel();
            databaseLabel.setBounds(new Rectangle(25, 125, 115, 25));
            databaseLabel.setText("load database:");
            databaseLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            commentLabel = new JLabel();
            commentLabel.setBounds(new Rectangle(25, 275, 115, 25));
            commentLabel.setText("backup comment:");
            commentLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            backupNameLabel = new JLabel();
            backupNameLabel.setBounds(new Rectangle(25, 225, 115, 25));
            backupNameLabel.setText("backup name:");
            backupNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            dataFileSizeLabel = new JLabel();
            dataFileSizeLabel.setBounds(new Rectangle(325, 175, 115, 25));
            dataFileSizeLabel.setText("data file size:");
            dataFileSizeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            backupSizeLabel = new JLabel();
            backupSizeLabel.setBounds(new Rectangle(25, 175, 115, 25));
            backupSizeLabel.setText("backup size:");
            backupSizeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            targetLabel = new JLabel();
            targetLabel.setBounds(new Rectangle(25, 75, 115, 25));
            targetLabel.setText("target directory:");
            targetLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            sourceLabel = new JLabel();
            sourceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            sourceLabel.setBounds(new Rectangle(25, 25, 115, 25));
            sourceLabel.setText("source directory:");
            basicSettingsPanel = new JPanel();
            basicSettingsPanel.setLayout(null);
            basicSettingsPanel.add(sourceLabel, null);
            basicSettingsPanel.add(getSourceTextField(), null);
            basicSettingsPanel.add(getSourceChooseButton(), null);
            basicSettingsPanel.add(targetLabel, null);
            basicSettingsPanel.add(getTargetTextField(), null);
            basicSettingsPanel.add(getTargetChooseButton(), null);
            basicSettingsPanel.add(backupSizeLabel, null);
            basicSettingsPanel.add(getBackupSizeTextField(), null);
            basicSettingsPanel.add(dataFileSizeLabel, null);
            basicSettingsPanel.add(getDataFileSizeTextField(), null);
            basicSettingsPanel.add(backupNameLabel, null);
            basicSettingsPanel.add(commentLabel, null);
            basicSettingsPanel.add(getBackupNameTextField(), null);
            basicSettingsPanel.add(getCommentScrollPane(), null);
            basicSettingsPanel.add(databaseLabel, null);
            basicSettingsPanel.add(getLoadDBTextField(), null);
            basicSettingsPanel.add(getLoadDBChooseButton(), null);
        }
        return basicSettingsPanel;
    }

    /**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getAdvancedSettingsPanel() {
        if (advancedSettingsPanel == null) {
            advancedSettingsPanel = new JPanel();
            advancedSettingsPanel.setLayout(null);
        }
        return advancedSettingsPanel;
    }

    /**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getRunPanel() {
        if (progressPanel == null) {
            progressPanel = new JPanel();
            progressPanel.setLayout(null);
            progressPanel.add(getStartButton(), null);
            progressPanel.add(getMessageScrollPane(), null);
            progressPanel.add(getProgressGUIPanel(), null);
        }
        return progressPanel;
    }

    /**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getSourceTextField() {
        if (sourceTextField == null) {
            sourceTextField = new JTextField();
            sourceTextField.setBounds(new Rectangle(150, 25, 390, 25));
            sourceTextField.setToolTipText("setting: " + JabakSwitches.source);
            sourceTextField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent arg0) {
                    settings.source.stringSet(sourceTextField.getText());
                }
            });
        }
        return sourceTextField;
    }

    /**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getSourceChooseButton() {
        if (sourceChooseButton == null) {
            sourceChooseButton = new JButton();
            sourceChooseButton.setBounds(new Rectangle(550, 25, 25, 25));
            sourceChooseButton.setText("...");
            sourceChooseButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                }
            });
        }
        return sourceChooseButton;
    }

    /**
	 * This method initializes targetTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getTargetTextField() {
        if (targetTextField == null) {
            targetTextField = new JTextField();
            targetTextField.setBounds(new Rectangle(150, 75, 390, 25));
            targetTextField.setToolTipText("setting: " + JabakSwitches.target);
            targetTextField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent arg0) {
                    stringSettings.set(JabakSwitches.target, targetTextField.getText());
                }
            });
            GUICommon.setFileChangeListener(stringSettings, JabakSwitches.target, targetTextField, JFileChooser.DIRECTORIES_ONLY);
        }
        return targetTextField;
    }

    /**
	 * This method initializes targetChooseButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getTargetChooseButton() {
        if (targetChooseButton == null) {
            targetChooseButton = new JButton();
            targetChooseButton.setBounds(new Rectangle(550, 75, 25, 25));
            targetChooseButton.setText("...");
            targetChooseButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    GUICommon.fileChooser(stringSettings, JabakSwitches.target, "choose as target", targetChooseButton, JFileChooser.DIRECTORIES_ONLY);
                }
            });
        }
        return targetChooseButton;
    }

    /**
	 * This method initializes backupSizeTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getBackupSizeTextField() {
        if (backupSizeTextField == null) {
            backupSizeTextField = new JTextField();
            backupSizeTextField.setBounds(new Rectangle(150, 175, 125, 25));
            backupSizeTextField.setToolTipText("setting: " + JabakSwitches.backupsize);
            backupSizeTextField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent arg0) {
                    stringSettings.set(JabakSwitches.backupsize, backupSizeTextField.getText());
                }
            });
            ChangeListener<String> listener = new ChangeListener<String>() {

                public void changed(String key) {
                    if (!key.equals(JabakSwitches.backupsize)) return;
                    String number = stringSettings.get(key);
                    if (number == null) return;
                    if (!backupSizeTextField.getText().equals(number)) backupSizeTextField.setText(number);
                    try {
                        long size = Long.parseLong(number);
                        if (size < 10 * GZipCountStream.minFileSize && size != 0) {
                            backupSizeTextField.setBackground(new Color(255, 192, 160));
                            return;
                        }
                    } catch (NumberFormatException e) {
                        backupSizeTextField.setBackground(new Color(255, 192, 160));
                        return;
                    }
                    backupSizeTextField.setBackground(Color.white);
                }
            };
            stringSettings.addChangeListener(listener);
            listener.changed(JabakSwitches.backupsize);
        }
        return backupSizeTextField;
    }

    /**
	 * This method initializes dataFileSizeTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getDataFileSizeTextField() {
        if (dataFileSizeTextField == null) {
            dataFileSizeTextField = new JTextField();
            dataFileSizeTextField.setBounds(new Rectangle(450, 175, 125, 25));
            dataFileSizeTextField.setToolTipText("setting: " + JabakSwitches.datafilelength);
            dataFileSizeTextField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent arg0) {
                    stringSettings.set(JabakSwitches.datafilelength, dataFileSizeTextField.getText());
                }
            });
            ChangeListener<String> listener = new ChangeListener<String>() {

                public void changed(String key) {
                    if (!key.equals(JabakSwitches.datafilelength)) return;
                    String number = stringSettings.get(key);
                    if (number == null) {
                        dataFileSizeTextField.setBackground(new Color(255, 192, 160));
                        return;
                    }
                    if (!dataFileSizeTextField.getText().equals(number)) dataFileSizeTextField.setText(number);
                    try {
                        long size = Long.parseLong(number);
                        if (size < GZipCountStream.minFileSize) {
                            dataFileSizeTextField.setBackground(new Color(255, 192, 160));
                            return;
                        }
                    } catch (NumberFormatException e) {
                        dataFileSizeTextField.setBackground(new Color(255, 192, 160));
                        return;
                    }
                    dataFileSizeTextField.setBackground(Color.white);
                }
            };
            stringSettings.addChangeListener(listener);
            listener.changed(JabakSwitches.datafilelength);
        }
        return dataFileSizeTextField;
    }

    /**
	 * This method initializes backupNameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getBackupNameTextField() {
        if (backupNameTextField == null) {
            backupNameTextField = new JTextField();
            backupNameTextField.setBounds(new Rectangle(150, 225, 425, 25));
            backupNameTextField.setToolTipText("setting: " + JabakSwitches.name);
            backupNameTextField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent arg0) {
                    stringSettings.set(JabakSwitches.name, backupNameTextField.getText());
                }
            });
            backupNameTextField.setText(stringSettings.get(JabakSwitches.name));
        }
        return backupNameTextField;
    }

    /**
	 * This method initializes commentTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
    private JTextArea getCommentTextArea() {
        if (commentTextArea == null) {
            commentTextArea = new JTextArea();
            commentTextArea.setToolTipText("setting: " + JabakSwitches.comment);
            commentTextArea.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent arg0) {
                    stringSettings.set(JabakSwitches.comment, commentTextArea.getText());
                }
            });
            commentTextArea.setText(stringSettings.get(JabakSwitches.comment));
        }
        return commentTextArea;
    }

    /**
	 * This method initializes commentScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getCommentScrollPane() {
        if (commentScrollPane == null) {
            commentScrollPane = new JScrollPane();
            commentScrollPane.setBounds(new Rectangle(150, 275, 425, 100));
            commentScrollPane.setViewportView(getCommentTextArea());
        }
        return commentScrollPane;
    }

    /**
	 * This method initializes loadDBTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getLoadDBTextField() {
        if (loadDBTextField == null) {
            loadDBTextField = new JTextField();
            loadDBTextField.setBounds(new Rectangle(150, 125, 390, 25));
            loadDBTextField.setToolTipText("setting: " + JabakSwitches.database);
            loadDBTextField.addCaretListener(new CaretListener() {

                public void caretUpdate(CaretEvent arg0) {
                    stringSettings.set(JabakSwitches.database, loadDBTextField.getText());
                }
            });
            GUICommon.setFileChangeListener(stringSettings, JabakSwitches.database, loadDBTextField, JFileChooser.FILES_ONLY);
        }
        return loadDBTextField;
    }

    /**
	 * This method initializes loadDBChooseButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getLoadDBChooseButton() {
        if (loadDBChooseButton == null) {
            loadDBChooseButton = new JButton();
            loadDBChooseButton.setBounds(new Rectangle(550, 125, 25, 25));
            loadDBChooseButton.setText("...");
            loadDBChooseButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    GUICommon.fileChooser(stringSettings, JabakSwitches.database, "choose base DB", loadDBChooseButton, JFileChooser.FILES_ONLY);
                }
            });
        }
        return loadDBChooseButton;
    }

    /**
	 * This method initializes startButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getStartButton() {
        if (startButton == null) {
            startButton = new JButton();
            startButton.setBounds(new Rectangle(425, 350, 125, 25));
            startButton.setText("start backup");
            startButton.addActionListener(new java.awt.event.ActionListener() {

                boolean started = false;

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (started) {
                        System.exit(0);
                    }
                    started = true;
                    startButton.setText(started ? "stop backup" : "start backup");
                    jabakGUITabbedPane.setEnabledAt(0, !started);
                    jabakGUITabbedPane.setEnabledAt(1, !started);
                    startButton.setEnabled(false);
                    startButton.setText("running");
                    SwingWorker<Exception, Void> worker = new SwingWorker<Exception, Void>() {

                        @Override
                        protected Exception doInBackground() throws Exception {
                            try {
                                stringSettings.setObject(JabakSwitches.GUI.guiFrame, jabakGUI);
                                stringSettings.setObject(JabakSwitches.GUI.progressPanel, progressGUIPanel);
                                Temp temp = new Temp();
                                temp.stringSettings = stringSettings;
                                temp.settings = settings;
                                return new Jabak().call(temp);
                            } catch (Exception e) {
                                return e;
                            }
                        }

                        @Override
                        protected void done() {
                            startButton.setEnabled(true);
                            startButton.setText("finish");
                            Exception e = null;
                            try {
                                e = get();
                            } catch (Exception ex) {
                                e = ex;
                            }
                            if (e != null) {
                                StringWriter writer = new StringWriter();
                                e.printStackTrace(new PrintWriter(writer));
                                MessageTextArea.setText(writer.toString());
                                startButton.setBackground(Color.red);
                            }
                        }
                    };
                    worker.execute();
                }
            });
        }
        return startButton;
    }

    public JabakGUI(Settings strSet, JabakSettings set) throws Exception {
        settings = set;
        stringSettings = new GUISettings();
        stringSettings.setDefault(strSet);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                getJabakGUI().setVisible(true);
            }
        });
    }

    /**
	 * This method initializes MessageTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
    private JTextArea getMessageTextArea() {
        if (MessageTextArea == null) {
            MessageTextArea = new JTextArea();
            MessageTextArea.setToolTipText("progress and result messages");
            MessageTextArea.setEditable(false);
        }
        return MessageTextArea;
    }

    /**
	 * This method initializes MessageScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getMessageScrollPane() {
        if (MessageScrollPane == null) {
            MessageScrollPane = new JScrollPane();
            MessageScrollPane.setBounds(new Rectangle(25, 25, 540, 75));
            MessageScrollPane.setViewportView(getMessageTextArea());
        }
        return MessageScrollPane;
    }

    /**
	 * This method initializes progressGUIPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getProgressGUIPanel() {
        if (progressGUIPanel == null) {
            progressGUIPanel = new JPanel();
            progressGUIPanel.setLayout(new GridBagLayout());
            progressGUIPanel.setBounds(new Rectangle(200, 125, 200, 200));
        }
        return progressGUIPanel;
    }

    public static void main(String[] args) throws Exception {
        new JabakGUI(null, null);
    }
}
