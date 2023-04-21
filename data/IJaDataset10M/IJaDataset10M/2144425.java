package org.nonhtmlmail.ftp.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.text.SimpleDateFormat;
import org.nonhtmlmail.util.Logging;
import org.nonhtmlmail.util.Config;
import org.nonhtmlmail.util.MessageDialog;

public class JS_FTP extends Frame implements IFileDisplayParent {

    private static final String ABOUT_TEXT = "JS-FTP Version 1.5.1\n\n" + "http://js-ftp.sourceforge.net/\n" + "mailto:colin@nonhtmlmail.org\n\n" + "JS-FTP is a simple FTP client written in Java. JS-FTP's GUI was developed in\n" + "the pre 1.2 swinging Java days so it works with JRE 1.0 or later.\n\n" + "JS-FTP is open source, free, and comes with a GNU GENERAL PUBLIC LICENSE.\n";

    private Button aboutButton = new Button("About");

    private Button uploadButton = new Button("-->");

    private Button connectButton = new Button("Connect");

    private Button exitButton = new Button("Exit");

    private Button downloadButton = new Button("<--");

    private CheckboxGroup transferModeGroup = new CheckboxGroup();

    private Checkbox asciiCheckbox = new Checkbox("ASCII", false, transferModeGroup);

    private Checkbox binaryCheckbox = new Checkbox("Binary", true, transferModeGroup);

    private TextArea messageTextArea = new TextArea(3, 30);

    private LocalFileDisplay localFileDisplay = new LocalFileDisplay(this);

    private RemoteFileDisplay remoteFileDisplay = new RemoteFileDisplay(this);

    private Panel commandButtonPanel = new Panel(new GridLayout());

    private Panel fileTypePanel = new Panel(new FlowLayout());

    private Panel fileDisplayPanel = new Panel(new GridBagLayout());

    private Panel mainBottomSectionPanel = new Panel(new BorderLayout());

    public JS_FTP() {
        setLayout(new BorderLayout());
        setBackground(Color.lightGray);
        setTitle("JS-FTP");
        commandButtonPanel.add(connectButton);
        commandButtonPanel.add(aboutButton);
        commandButtonPanel.add(exitButton);
        fileTypePanel.add(asciiCheckbox);
        fileTypePanel.add(binaryCheckbox);
        messageTextArea.setBackground(Color.white);
        localFileDisplay.setFileSystemLocationLabelText("Local System");
        remoteFileDisplay.setFileSystemLocationLabelText("Remote System");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        fileDisplayPanel.add(localFileDisplay, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        fileDisplayPanel.add(remoteFileDisplay, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weightx = 0.0;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(0, 4, 2, 4);
        fileDisplayPanel.add(downloadButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 0.0;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(2, 4, 0, 4);
        fileDisplayPanel.add(uploadButton, gbc);
        mainBottomSectionPanel.add("South", commandButtonPanel);
        mainBottomSectionPanel.add("North", fileTypePanel);
        mainBottomSectionPanel.add("Center", messageTextArea);
        add("South", mainBottomSectionPanel);
        add("Center", fileDisplayPanel);
        pack();
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dispose();
                System.exit(0);
            }
        });
        exitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                savePosition();
                setVisible(false);
                dispose();
                System.exit(0);
            }
        });
        connectButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (connectButton.getLabel().equals("Connect")) {
                    if (remoteFileDisplay.connect()) {
                        connectButton.setLabel("Disconnect");
                    }
                } else {
                    remoteFileDisplay.disconnect();
                    connectButton.setLabel("Connect");
                }
            }
        });
        aboutButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aboutBox();
            }
        });
        uploadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                uploadFiles();
            }
        });
        downloadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                downloadFiles();
            }
        });
    }

    private void setFontAll(Container container, Font font) {
        container.setFont(font);
        for (int i = 0; i < container.getComponentCount(); i++) {
            Component subComponent = container.getComponent(i);
            subComponent.setFont(font);
            try {
                setFontAll((Container) subComponent, font);
            } catch (ClassCastException e) {
            }
        }
    }

    private void aboutBox() {
        MessageDialog dlg = new MessageDialog(this, "About JS-FTP", ABOUT_TEXT, 12, 80);
        dlg.setLocation(getBounds().x + 25, getBounds().y + 25);
        dlg.setVisible(true);
    }

    private void uploadFiles() {
        if (connectButton.getLabel().equals("Connect")) {
            message("Connect to a server first!");
            return;
        }
        FileDisplayItem[] items = localFileDisplay.getSelectedItems();
        if (items == null || items.length == 0) {
            message("Nothing is selected for upload!");
            return;
        }
        for (int i = 0; i < items.length; i++) uploadFile(items[i]);
    }

    private void uploadFile(FileDisplayItem item) {
        if (item.isDir()) {
            message("Item selected is a directory!");
            return;
        }
        String localPath = localFileDisplay.getLocalPath();
        remoteFileDisplay.upload(localPath + "/" + item.getFileName(), item.getFileName(), binaryCheckbox.getState());
    }

    private void downloadFiles() {
        if (connectButton.getLabel().equals("Connect")) {
            message("Connect to a server first!");
            return;
        }
        FileDisplayItem[] items = remoteFileDisplay.getSelectedItems();
        if (items == null || items.length == 0) {
            message("Nothing is selected for download!");
            return;
        }
        for (int i = 0; i < items.length; i++) downloadFile(items[i]);
    }

    private void downloadFile(FileDisplayItem item) {
        if (item.isDir()) {
            message("Item selected is a directory!");
            return;
        }
        String localPath = localFileDisplay.getLocalPath();
        remoteFileDisplay.download(item.getFileName(), localPath + "/" + item.getFileName(), binaryCheckbox.getState());
        localFileDisplay.refresh();
    }

    private void savePosition() {
        Rectangle bounds = getBounds();
        Config.put("LastX", "" + bounds.x);
        Config.put("LastY", "" + bounds.y);
        Config.put("LastWidth", "" + bounds.width);
        Config.put("LastHeight", "" + bounds.height);
        Config.put("LastLocalDir", localFileDisplay.getDir());
        Config.save();
    }

    private void saveSafeParams() {
        Config.put("LastX", "0");
        Config.put("LastY", "0");
        Config.put("LastWidth", "300");
        Config.put("LastHeight", "300");
        Config.save();
    }

    private void setPosition() {
        try {
            int x = Integer.parseInt(Config.get("LastX"));
            int y = Integer.parseInt(Config.get("LastY"));
            int width = Integer.parseInt(Config.get("LastWidth"));
            int height = Integer.parseInt(Config.get("LastHeight"));
            setBounds(x, y, width, height);
        } catch (NumberFormatException e) {
            Logging.trace("Found an invalid number or no number.", e);
        }
    }

    public Frame getFrame() {
        return this;
    }

    public void message(String text) {
        messageTextArea.append(text + "\n");
    }

    public void localSetDir(String path) {
        if (path.indexOf('\\') >= 0) {
            path = "/";
        }
        localFileDisplay.setDir(path);
    }

    public static void main(java.lang.String[] args) {
        Logging.init("JS_FTP");
        JS_FTP ftpClient = new JS_FTP();
        ftpClient.setPosition();
        String lastDir = Config.get("LastLocalDir");
        if (lastDir == null) {
            lastDir = System.getProperty("user.home");
        }
        ftpClient.saveSafeParams();
        ftpClient.localSetDir(lastDir);
        ftpClient.setVisible(true);
    }
}
