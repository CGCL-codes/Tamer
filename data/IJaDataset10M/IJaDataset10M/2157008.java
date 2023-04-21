package de.jtdev.jfilenotify.testing;

import de.jtdev.jfilenotify.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ListenerTest extends JFrame {

    private JFrame thisFrame = this;

    private JPanel framePanel = new JPanel();

    private JPanel leftPanel = new JPanel();

    private JPanel filenamePanel = new JPanel();

    private JLabel filenameLabel = new JLabel();

    private JTextField filenameField = new JTextField();

    private JButton filenameButton = new JButton();

    private JPanel optionPanel = new JPanel();

    private JLabel optionsLabel = new JLabel();

    private JCheckBox modifiedCheckBox = new JCheckBox();

    private JCheckBox attributesChangedCheckBox = new JCheckBox();

    private JCheckBox subfileCreatedCheckBox = new JCheckBox();

    private JCheckBox subfileDeletedCheckBox = new JCheckBox();

    private JPanel centerPanel = new JPanel();

    private JPanel listenerListPanel = new JPanel();

    private JScrollPane listenerListScrollPane = new JScrollPane();

    private JList listenerList = new JList();

    private JPanel listenerButtonPanel = new JPanel();

    private JButton addListenerButton = new JButton();

    private JButton removeListenerButton = new JButton();

    private JPanel eventListPanel = new JPanel();

    private JScrollPane eventListScrollPane = new JScrollPane();

    private JList eventList = new JList();

    private JPanel eventButtonPanel = new JPanel();

    private JButton clearEventListButton = new JButton();

    private DefaultListModel listenerListModel = new DefaultListModel();

    private DefaultListModel eventListModel = new DefaultListModel();

    private FileNotifyService fileService;

    public ListenerTest() {
        loadConfig();
        initService();
        initComponents();
        initEvents();
    }

    private String fileNameString = "";

    private File configDir;

    private File configFile;

    private void loadConfig() {
        String homeDirString = System.getProperty("user.home");
        configDir = new File(homeDirString, ".jfilenotify-testing");
        configFile = new File(configDir, "config");
        if (configFile.exists()) {
            Properties p = new Properties();
            InputStream in = null;
            try {
                in = new FileInputStream(configFile);
                p.load(in);
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                    }
                }
            }
            fileNameString = p.getProperty("filename");
            filenameField.setText(fileNameString);
        }
    }

    private void storeConfig() {
        if (!configDir.exists()) configDir.mkdir();
        FileOutputStream out = null;
        try {
            Properties p = new Properties();
            p.setProperty("filename", filenameField.getText());
            out = new FileOutputStream(configFile);
            p.store(out, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private void initService() {
        try {
            fileService = FileNotifyServiceFactory.createNotifyService();
        } catch (FileNotifyException ex) {
            ex.printStackTrace();
            exitApplication();
        }
    }

    private void applyOptionChange(JCheckBox[] boxes) {
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].setBorder(new EmptyBorder(2, 2, 2, 2));
            boxes[i].setMargin(new Insets(0, 0, 0, 0));
        }
    }

    private void initComponents() {
        GridBagLayout gbLayout;
        GridBagConstraints gbConstraints;
        thisFrame.setTitle("JFileNotify - ListenerTest");
        Container contentPane = this.getContentPane();
        contentPane.add(framePanel, BorderLayout.CENTER);
        EmptyBorder windowGap = new EmptyBorder(10, 10, 10, 10);
        int panelPadding = 5;
        framePanel.setBorder(windowGap);
        framePanel.setLayout(new BorderLayout(panelPadding, panelPadding));
        framePanel.add(leftPanel, BorderLayout.WEST);
        framePanel.add(centerPanel, BorderLayout.CENTER);
        gbLayout = new GridBagLayout();
        leftPanel.setLayout(gbLayout);
        gbConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, panelPadding, 0), 0, 0);
        gbLayout.addLayoutComponent(filenamePanel, gbConstraints);
        leftPanel.add(filenamePanel);
        gbConstraints = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        gbLayout.addLayoutComponent(optionPanel, gbConstraints);
        leftPanel.add(optionPanel);
        gbLayout = new GridBagLayout();
        filenamePanel.setLayout(gbLayout);
        gbConstraints = new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        gbLayout.addLayoutComponent(filenameLabel, gbConstraints);
        filenamePanel.add(filenameLabel);
        gbConstraints = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, panelPadding), 0, 0);
        gbLayout.addLayoutComponent(filenameField, gbConstraints);
        filenamePanel.add(filenameField);
        gbConstraints = new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        gbLayout.addLayoutComponent(filenameButton, gbConstraints);
        filenamePanel.add(filenameButton);
        filenameLabel.setText("Filename:");
        filenameField.setColumns(15);
        filenameButton.setText("...");
        gbLayout = new GridBagLayout();
        optionPanel.setLayout(gbLayout);
        gbConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        gbLayout.addLayoutComponent(optionsLabel, gbConstraints);
        optionPanel.add(optionsLabel);
        gbConstraints = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        gbLayout.addLayoutComponent(modifiedCheckBox, gbConstraints);
        optionPanel.add(modifiedCheckBox);
        gbConstraints = new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        gbLayout.addLayoutComponent(attributesChangedCheckBox, gbConstraints);
        optionPanel.add(attributesChangedCheckBox);
        gbConstraints = new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        gbLayout.addLayoutComponent(subfileCreatedCheckBox, gbConstraints);
        optionPanel.add(subfileCreatedCheckBox);
        gbConstraints = new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        gbLayout.addLayoutComponent(subfileDeletedCheckBox, gbConstraints);
        optionPanel.add(subfileDeletedCheckBox);
        optionsLabel.setText("Mask options:");
        modifiedCheckBox.setText("0x01 MODIFIED");
        attributesChangedCheckBox.setText("0x02 ATTRIBUTES_CHANGED");
        subfileCreatedCheckBox.setText("0x04 SUBFILE_CREATED");
        subfileDeletedCheckBox.setText("0x08 SUBFILE_DELETED");
        applyOptionChange(new JCheckBox[] { modifiedCheckBox, attributesChangedCheckBox, subfileCreatedCheckBox, subfileDeletedCheckBox });
        gbLayout = new GridBagLayout();
        centerPanel.setLayout(gbLayout);
        gbConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.4, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, panelPadding, 0), 0, 0);
        gbLayout.addLayoutComponent(listenerListPanel, gbConstraints);
        centerPanel.add(listenerListPanel);
        gbConstraints = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, panelPadding, 0), 0, 0);
        gbLayout.addLayoutComponent(listenerButtonPanel, gbConstraints);
        centerPanel.add(listenerButtonPanel);
        gbConstraints = new GridBagConstraints(0, 2, 1, 1, 1.0, 0.6, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, panelPadding, 0), 0, 0);
        gbLayout.addLayoutComponent(eventListPanel, gbConstraints);
        centerPanel.add(eventListPanel);
        gbConstraints = new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        gbLayout.addLayoutComponent(eventButtonPanel, gbConstraints);
        centerPanel.add(eventButtonPanel);
        listenerListPanel.setLayout(new BorderLayout());
        listenerListPanel.add(listenerListScrollPane, BorderLayout.CENTER);
        listenerListScrollPane.setViewportView(listenerList);
        listenerList.setModel(listenerListModel);
        listenerButtonPanel.setLayout(new BoxLayout(listenerButtonPanel, BoxLayout.X_AXIS));
        listenerButtonPanel.add(addListenerButton);
        listenerButtonPanel.add(Box.createHorizontalStrut(panelPadding));
        listenerButtonPanel.add(removeListenerButton);
        addListenerButton.setText("Add Listener");
        removeListenerButton.setText("Remove Listener");
        eventListPanel.setLayout(new BorderLayout());
        eventListPanel.add(eventListScrollPane, BorderLayout.CENTER);
        eventListScrollPane.setViewportView(eventList);
        eventList.setModel(eventListModel);
        eventButtonPanel.setLayout(new BoxLayout(eventButtonPanel, BoxLayout.X_AXIS));
        eventButtonPanel.add(clearEventListButton);
        clearEventListButton.setText("Clear Eventlist");
        this.pack();
    }

    private static int listenerNumber = 1;

    private void initEvents() {
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent event) {
                exitApplication();
            }
        });
        filenameButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = new JFileChooser();
                chooser.setAcceptAllFileFilterUsed(true);
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setMultiSelectionEnabled(false);
                File currentFile = new File(filenameField.getText());
                if (currentFile.exists()) {
                    chooser.setSelectedFile(currentFile);
                }
                int ret = chooser.showOpenDialog(thisFrame);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    filenameField.setText(chooser.getSelectedFile().getPath());
                }
            }
        });
        addListenerButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                String fileName = filenameField.getText();
                File file = new File(fileName);
                int mask = 0x00000000;
                if (modifiedCheckBox.isSelected()) mask |= FileNotifyConstants.WATCH_MODIFICATIONS;
                if (attributesChangedCheckBox.isSelected()) mask |= FileNotifyConstants.WATCH_ATTRIBUTES;
                if (subfileCreatedCheckBox.isSelected()) mask |= FileNotifyConstants.WATCH_SUBFILE_CREATIONS;
                if (subfileDeletedCheckBox.isSelected()) mask |= FileNotifyConstants.WATCH_SUBFILE_DELETIONS;
                try {
                    FileNotifyListener listener = new FileNotifyListener(file, mask) {

                        private String name = "Listener " + listenerNumber++;

                        public void notificationRecieved(final FileNotifyEvent event) {
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    String eventString = null;
                                    if ((event.getEventMask() & FileNotifyConstants.MODIFIED_EVENT) != 0) {
                                        eventString = "\"" + event.getChangedFile().getPath() + "\" modified";
                                    } else if ((event.getEventMask() & FileNotifyConstants.ATTRIBUTES_CHANGED_EVENT) != 0) {
                                        eventString = "\"" + event.getChangedFile() + "\" attributes changed";
                                    } else if ((event.getEventMask() & FileNotifyConstants.SUBFILE_CREATED_EVENT) != 0) {
                                        eventString = "\"" + event.getChangedFile() + "\" was created";
                                    } else if ((event.getEventMask() & FileNotifyConstants.SUBFILE_DELETED_EVENT) != 0) {
                                        eventString = "\"" + event.getChangedFile() + "\" was deleted";
                                    }
                                    eventListModel.addElement(name + " " + eventString);
                                }
                            });
                        }

                        public void discarded(final FileNotifyEvent event) {
                            final FileNotifyListener thisListener = this;
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    listenerListModel.removeElement(thisListener);
                                    String eventString = name + " discarded: mask=" + Integer.toHexString(event.getEventMask()) + " fileName=" + event.getChangedFile();
                                    eventListModel.addElement(eventString);
                                }
                            });
                        }

                        public String toString() {
                            return name + ": fileName=" + getFile() + " mask=" + Integer.toHexString(getMask());
                        }
                    };
                    fileService.addFileNotifyListener(listener);
                    listenerListModel.addElement(listener);
                } catch (FileNotifyException ex) {
                    JOptionPane.showMessageDialog(thisFrame, ex.getMessage());
                }
            }
        });
        removeListenerButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                int index = listenerList.getSelectedIndex();
                if (index >= 0) {
                    FileNotifyListener listener = (FileNotifyListener) listenerListModel.remove(index);
                    try {
                        if (!fileService.removeFileNotifyListener(listener)) {
                            JOptionPane.showMessageDialog(thisFrame, "Listener did not exist!!");
                        }
                    } catch (FileNotifyException ex) {
                        JOptionPane.showMessageDialog(thisFrame, ex.getMessage());
                    }
                }
            }
        });
        clearEventListButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                eventListModel.clear();
            }
        });
    }

    private void exitApplication() {
        storeConfig();
        dispose();
        if (fileService != null) {
            try {
                fileService.dispose();
            } catch (FileNotifyException ex) {
                System.out.println("FileNotifyService not disposed");
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ListenerTest().setVisible(true);
            }
        });
    }
}
