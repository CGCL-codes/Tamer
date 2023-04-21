package net.sf.imageCave;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.File;
import java.io.InputStream;
import java.io.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;

/**
 * ImageCave.java
 * @author Stuart T. Tett
 * @author Justin M. Rasmussen
 * @version 0.1 22 Jan, 2004
 */
public class ImageCave extends JFrame implements TreeSelectionListener {

    private void recursiveRemoveDir(File directory) {
        String[] filelist = directory.list();
        File tmpFile = null;
        for (int i = 0; i < filelist.length; i++) {
            tmpFile = new File(directory.getAbsolutePath(), filelist[i]);
            if (tmpFile.isDirectory()) {
                recursiveRemoveDir(tmpFile);
            } else if (tmpFile.isFile()) {
                tmpFile.delete();
            }
        }
        directory.delete();
    }

    private Client clientInterface;

    private JDesktopPane desktop;

    private JTree directoryList;

    private JScrollPane directoryStuff;

    private JPanel test;

    private JFrame doLogin;

    private boolean loggedIn;

    private JFileChooser fileChooser;

    String userName;

    DefaultMutableTreeNode dirRoot;

    DefaultTreeModel treeModel;

    boolean overwrite;

    String PATH = ".";

    public ImageCave() {
        super("Image Cave");
        clientInterface = new Client();
        userName = "";
        fileChooser = new JFileChooser(PATH);
        fileChooser.setFileFilter(new myFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.remove(0);
        fileChooser.remove(1);
        fileChooser.setFileView(new myFileView());
        fileChooser.setBorder(null);
        loggedIn = false;
        SpringLayout layout = new SpringLayout();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(50, 50, screenSize.width / 2 + 100, screenSize.height / 2 + 100);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        desktop = new JDesktopPane();
        desktop.setBackground(Color.gray);
        setContentPane(desktop);
        desktop.setLayout(layout);
        userName = login();
        dirRoot = new DefaultMutableTreeNode("Users:");
        dirRoot = getDirStruct("clientImages", dirRoot);
        treeModel = new DefaultTreeModel(dirRoot);
        directoryList = new JTree(treeModel);
        directoryList.setEditable(true);
        directoryList.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        directoryStuff = new JScrollPane(directoryList);
        desktop.add(fileChooser);
        test = new JPanel(new GridLayout(0, 1));
        directoryList.addTreeSelectionListener(this);
        test.add(directoryStuff);
        test.setBounds(0, 0, 0, this.getHeight());
        desktop.add(test);
        fileChooser.setVisible(false);
        layout.putConstraint(SpringLayout.WEST, test, 0, SpringLayout.WEST, desktop);
        layout.putConstraint(SpringLayout.SOUTH, test, 0, SpringLayout.SOUTH, desktop);
        layout.putConstraint(SpringLayout.NORTH, test, 0, SpringLayout.NORTH, desktop);
        layout.putConstraint(SpringLayout.EAST, fileChooser, 0, SpringLayout.EAST, desktop);
        layout.putConstraint(SpringLayout.WEST, fileChooser, 0, SpringLayout.EAST, test);
        layout.putConstraint(SpringLayout.SOUTH, fileChooser, 0, SpringLayout.SOUTH, desktop);
        layout.putConstraint(SpringLayout.NORTH, fileChooser, 0, SpringLayout.NORTH, desktop);
        setJMenuBar(createMenuBar());
        desktop.putClientProperty("JDesktopPane.dragMode", "outline");
        fileChooser.setCurrentDirectory(new File("clientImages\\" + userName));
        fileChooser.setVisible(true);
        this.setVisible(true);
        this.addWindowListener(new WindowListener() {

            public void windowClosing(WindowEvent e) {
                clientInterface.quit();
                File cache = new File("clientImages\\");
                recursiveRemoveDir(cache);
                if (!cache.exists()) {
                    cache.mkdir();
                }
                System.exit(0);
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }
        });
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem menuItem = new JMenuItem("Add Picture");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                final File source;
                JFileChooser theFChooser = new JFileChooser();
                theFChooser.setFileFilter(new myFilter2());
                theFChooser.setAcceptAllFileFilterUsed(false);
                int returnVal = theFChooser.showDialog(desktop, "Upload");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    source = theFChooser.getSelectedFile();
                    final File dest = new File(fileChooser.getCurrentDirectory() + "\\" + source.getName());
                    if (dest.exists()) {
                        overwrite = false;
                        final JFrame frame = new JFrame("File exists");
                        JButton okay = new JButton("Okay");
                        JButton cancel = new JButton("Cancel");
                        JLabel label = new JLabel("File exists.  Overwrite?");
                        JPanel buttonPanel = new JPanel();
                        Container contentPane = frame.getContentPane();
                        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
                        contentPane.add(label);
                        buttonPanel.add(okay);
                        buttonPanel.add(cancel);
                        contentPane.add(buttonPanel);
                        frame.setVisible(true);
                        frame.pack();
                        okay.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent evt) {
                                File destRemove = new File(dest.getAbsolutePath());
                                destRemove.delete();
                                fileChooser.rescanCurrentDirectory();
                                writeFile(source, dest);
                                frame.dispose();
                            }
                        });
                        cancel.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent evt) {
                                frame.dispose();
                            }
                        });
                        frame.addWindowFocusListener(new WindowFocusListener() {

                            public void windowLostFocus(WindowEvent evt) {
                                frame.requestFocus();
                            }

                            public void windowGainedFocus(WindowEvent evt) {
                            }
                        });
                    } else writeFile(source, dest);
                } else {
                }
            }
        });
        menuItem = new JMenuItem("Remove Picture");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                JFileChooser theFChooser = new JFileChooser(fileChooser.getCurrentDirectory());
                theFChooser.setFileFilter(new myFilter2());
                theFChooser.setAcceptAllFileFilterUsed(false);
                int returnVal = theFChooser.showDialog(desktop, "Delete");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = theFChooser.getSelectedFile();
                    String thePath = file.getPath();
                    String filename = thePath.substring(thePath.indexOf("clientImages") + 13);
                    if (clientInterface.delete(filename)) file.delete(); else exceptionHandler("Error deleting file");
                    fileChooser.rescanCurrentDirectory();
                } else {
                }
            }
        });
        menuItem = new JMenuItem("Make Directory");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                final JFrame mkdir = new JFrame();
                JLabel dirLabel = new JLabel("Directory Name:");
                final JTextField dirInput = new JTextField(15);
                mkdir.setDefaultCloseOperation(EXIT_ON_CLOSE);
                mkdir.setResizable(false);
                JButton create = new JButton("Create");
                JButton cancel = new JButton("Close");
                Container contentPane = mkdir.getContentPane();
                contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
                mkdir.setBounds(200, 200, 300, 300);
                JPanel dirPanel = new JPanel();
                JPanel buttonPanel = new JPanel();
                dirPanel.add(dirLabel);
                dirPanel.add(dirInput);
                buttonPanel.add(create);
                buttonPanel.add(cancel);
                mkdir.setTitle("Make Directory");
                contentPane.add(dirPanel);
                contentPane.add(Box.createRigidArea(new Dimension(0, 50)));
                contentPane.add(buttonPanel);
                mkdir.pack();
                mkdir.setVisible(true);
                cancel.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        mkdir.dispose();
                    }
                });
                create.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        File newFile = new File(fileChooser.getCurrentDirectory() + "\\" + dirInput.getText() + "\\empty.txt");
                        File newDir = new File(fileChooser.getCurrentDirectory() + "\\" + dirInput.getText());
                        if (!newFile.exists()) {
                            newDir.mkdirs();
                            try {
                                newFile.createNewFile();
                            } catch (Exception e) {
                                System.out.println("Failed to create new file");
                            }
                            String thePath = new String(newFile.getPath());
                            if (thePath.indexOf("ClientImages") != -1) thePath = thePath.substring(thePath.indexOf("ClientImages") + 13); else if (thePath.indexOf("clientImages") != -1) thePath = thePath.substring(thePath.indexOf("clientImages") + 13);
                            if (!clientInterface.upload(thePath)) {
                                recursiveRemoveDir(newDir);
                            } else {
                                clientInterface.delete(thePath);
                            }
                        }
                    }
                });
            }
        });
        menuItem = new JMenuItem("Remove Directory");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                final JFrame mkdir = new JFrame();
                JLabel dirLabel = new JLabel("Directory Name:");
                final JTextField dirInput = new JTextField(15);
                mkdir.setDefaultCloseOperation(EXIT_ON_CLOSE);
                mkdir.setResizable(false);
                JButton create = new JButton("Remove");
                JButton cancel = new JButton("Close");
                Container contentPane = mkdir.getContentPane();
                contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
                mkdir.setBounds(200, 200, 300, 300);
                JPanel dirPanel = new JPanel();
                JPanel buttonPanel = new JPanel();
                dirPanel.add(dirLabel);
                dirPanel.add(dirInput);
                buttonPanel.add(create);
                buttonPanel.add(cancel);
                mkdir.setTitle("Remove Directory");
                contentPane.add(dirPanel);
                contentPane.add(Box.createRigidArea(new Dimension(0, 50)));
                contentPane.add(buttonPanel);
                mkdir.pack();
                mkdir.setVisible(true);
                cancel.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        mkdir.dispose();
                    }
                });
                create.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        File newDir = new File(fileChooser.getCurrentDirectory() + "\\" + dirInput.getText());
                        if (newDir.exists()) {
                            String thePath = new String(newDir.getPath());
                            if (thePath.indexOf("ClientImages") != -1) thePath = thePath.substring(thePath.indexOf("ClientImages") + 13); else if (thePath.indexOf("clientImages") != -1) thePath = thePath.substring(thePath.indexOf("clientImages") + 13);
                            if (clientInterface.delete(thePath)) {
                                recursiveRemoveDir(newDir);
                            }
                        }
                    }
                });
            }
        });
        menuItem = new JMenuItem("Exit");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                clientInterface.quit();
                File cache = new File("clientImages\\");
                recursiveRemoveDir(cache);
                if (!cache.exists()) {
                    cache.mkdir();
                }
                System.exit(0);
            }
        });
        menuBar.add(menu);
        menu = new JMenu("User");
        menuItem = new JMenuItem("Change Password");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                final JFrame theFrame = new JFrame();
                JLabel userLabel = new JLabel("new password:");
                JLabel userLabel2 = new JLabel("confirm:");
                final JPasswordField userInput = new JPasswordField(15);
                final JPasswordField userInput2 = new JPasswordField(15);
                theFrame.setResizable(false);
                JButton chPass = new JButton("Okay");
                JButton cancel = new JButton("Cancel");
                Container contentPane = theFrame.getContentPane();
                contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
                theFrame.setBounds(200, 200, 300, 300);
                JPanel userPanel = new JPanel();
                JPanel userPanel2 = new JPanel();
                JPanel buttonPanel = new JPanel();
                userPanel.add(userLabel);
                userPanel.add(userInput);
                userPanel2.add(userLabel2);
                userPanel2.add(userInput2);
                buttonPanel.add(chPass);
                buttonPanel.add(cancel);
                theFrame.setTitle("Change Password");
                contentPane.add(userPanel);
                contentPane.add(userPanel2);
                contentPane.add(Box.createRigidArea(new Dimension(0, 50)));
                contentPane.add(buttonPanel);
                theFrame.pack();
                theFrame.setVisible(true);
                cancel.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        theFrame.dispose();
                    }
                });
                chPass.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        String passW = "";
                        for (int i = 0; i < userInput.getPassword().length; i++) {
                            passW = passW + userInput.getPassword()[i];
                        }
                        String passW2 = "";
                        for (int i = 0; i < userInput2.getPassword().length; i++) {
                            passW2 = passW2 + userInput2.getPassword()[i];
                        }
                        if (passW.equals(passW2)) {
                            clientInterface.changePassword(passW);
                            theFrame.dispose();
                        }
                    }
                });
            }
        });
        menuItem = new JMenuItem("Add Friend");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                addRemFriendUI("Add Friend");
            }
        });
        menuItem = new JMenuItem("Remove Friend");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                addRemFriendUI("Remove Friend");
            }
        });
        menuBar.add(menu);
        return menuBar;
    }

    private String login() {
        doLogin = new JFrame();
        JLabel userLabel = new JLabel("username:");
        JLabel passLabel = new JLabel("password:");
        JLabel serverLabel = new JLabel("server:");
        final JTextField userInput = new JTextField(15);
        final JPasswordField passInput = new JPasswordField(15);
        final JTextField serverInput = new JTextField(17);
        doLogin.setResizable(false);
        JButton signIn = new JButton("Sign in");
        JButton cancel = new JButton("Cancel");
        JButton newAcct = new JButton("New Account");
        Container contentPane = doLogin.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        doLogin.setBounds(200, 200, 300, 300);
        JPanel userPanel = new JPanel();
        JPanel passPanel = new JPanel();
        JPanel serverPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        userPanel.add(userLabel);
        userPanel.add(userInput);
        passPanel.add(passLabel);
        passPanel.add(passInput);
        serverPanel.add(serverLabel);
        serverPanel.add(serverInput);
        buttonPanel.add(signIn);
        buttonPanel.add(cancel);
        buttonPanel.add(newAcct);
        doLogin.setTitle("Login");
        contentPane.add(userPanel);
        contentPane.add(passPanel);
        contentPane.add(serverPanel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 50)));
        contentPane.add(buttonPanel);
        signIn.getRootPane().setDefaultButton(signIn);
        doLogin.pack();
        doLogin.setVisible(true);
        signIn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (!clientInterface.connect(serverInput.getText())) {
                    System.out.println("Unable to connect");
                    clientInterface.quit();
                    System.exit(0);
                }
                String passW = "";
                for (int i = 0; i < passInput.getPassword().length; i++) {
                    passW = passW + passInput.getPassword()[i];
                }
                if (!clientInterface.login(userInput.getText(), passW)) {
                    final JFrame invLogin = new JFrame("Invalid Login");
                    JButton close = new JButton("Okay");
                    JLabel invLabel = new JLabel("Invalid username or password");
                    JPanel top = new JPanel();
                    JPanel bottom = new JPanel();
                    invLogin.setResizable(false);
                    top.add(invLabel);
                    bottom.add(close);
                    Container contentPane = new Container();
                    contentPane = invLogin.getContentPane();
                    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
                    invLogin.setBounds(300, 200, 200, 100);
                    contentPane.add(top);
                    contentPane.add(bottom);
                    invLogin.pack();
                    close.getRootPane().setDefaultButton(close);
                    close.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            invLogin.dispose();
                        }
                    });
                    invLogin.addWindowFocusListener(new WindowFocusListener() {

                        public void windowLostFocus(WindowEvent evt) {
                            invLogin.requestFocus();
                        }

                        public void windowGainedFocus(WindowEvent evt) {
                        }
                    });
                    invLogin.setVisible(true);
                    passInput.setText("");
                    clientInterface.quit();
                } else {
                    loggedIn = true;
                    doLogin.dispose();
                }
            }
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                clientInterface.quit();
                File cache = new File("clientImages\\");
                recursiveRemoveDir(cache);
                if (!cache.exists()) {
                    cache.mkdir();
                }
                System.exit(0);
            }
        });
        newAcct.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                System.out.println("New Account added");
            }
        });
        doLogin.addWindowListener(new WindowListener() {

            public void windowClosing(WindowEvent e) {
                clientInterface.quit();
                File cache = new File("clientImages\\");
                recursiveRemoveDir(cache);
                if (!cache.exists()) {
                    cache.mkdir();
                }
                System.exit(0);
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }
        });
        while (!loggedIn) ;
        return userInput.getText();
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) directoryList.getLastSelectedPathComponent();
        DefaultMutableTreeNode traveler = node;
        String selDir = "";
        if (traveler == null) {
            return;
        }
        for (int x = traveler.getLevel(); x > 0; x--) {
            selDir = traveler.getUserObject() + "\\" + selDir;
            traveler = (DefaultMutableTreeNode) traveler.getParent();
        }
        clientInterface.download(selDir);
        fileChooser.setCurrentDirectory(new File("clientImages\\" + selDir));
        if (true) {
            File userDirs = new File("clientImages\\" + selDir);
            String thePath = "clientImages\\" + selDir;
            String[] users;
            users = userDirs.list();
            int i = 0;
            if (users != null) for (int q = 0; q < node.getChildCount(); q++) {
            }
            int found = 0;
            for (int j = 0; j < node.getChildCount(); j++) {
                found = 0;
                for (int q = 0; q < users.length; q++) {
                    if (users[q].equals(node.getChildAt(j).toString())) {
                        found = 1;
                    }
                }
                if (found == 0) {
                    node.remove(j);
                    treeModel.reload(node);
                    treeModel.nodeChanged(node);
                    directoryList.scrollPathToVisible(new TreePath(node.getPath()));
                }
            }
            while (i < users.length && users.length != node.getChildCount()) {
                File tempU = new File(thePath + "\\" + users[i]);
                if (tempU.isDirectory()) {
                    DefaultMutableTreeNode newUser = new DefaultMutableTreeNode(tempU.getName());
                    int matches = 0;
                    for (int q = 0; q < node.getChildCount(); q++) {
                        if (node.getChildAt(q).toString().equals(newUser.toString())) {
                            matches++;
                        }
                    }
                    if (matches == 0) {
                        treeModel.insertNodeInto(newUser, node, node.getChildCount());
                        treeModel.nodeChanged(node);
                        directoryList.scrollPathToVisible(new TreePath(node.getPath()));
                    }
                }
                i++;
            }
            test.revalidate();
            test.repaint();
        }
    }

    private void exceptionHandler(String message) {
        final JFrame errFrame = new JFrame();
        JLabel userLabel = new JLabel(message);
        errFrame.setResizable(false);
        JButton okayButton = new JButton("Okay");
        Container contentPane = errFrame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        errFrame.setBounds(200, 200, 300, 300);
        JPanel userPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        userPanel.add(userLabel);
        buttonPanel.add(okayButton);
        errFrame.setTitle("Error");
        contentPane.add(userPanel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 50)));
        contentPane.add(buttonPanel);
        errFrame.pack();
        errFrame.setVisible(true);
        okayButton.getRootPane().setDefaultButton(okayButton);
        okayButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                errFrame.dispose();
            }
        });
        errFrame.addWindowFocusListener(new WindowFocusListener() {

            public void windowLostFocus(WindowEvent evt) {
                errFrame.requestFocus();
            }

            public void windowGainedFocus(WindowEvent evt) {
            }
        });
    }

    private void writeFile(File source, File dest) {
        String thePath;
        byte theByte = 0;
        FileInputStream fr = null;
        FileOutputStream fw = null;
        BufferedInputStream br = null;
        BufferedOutputStream bw = null;
        DataInputStream dIn = null;
        DataOutputStream dOut = null;
        overwrite = true;
        try {
            fr = new FileInputStream(source);
            fw = new FileOutputStream(dest);
            br = new BufferedInputStream(fr);
            bw = new BufferedOutputStream(fw);
            dIn = new DataInputStream(br);
            dOut = new DataOutputStream(bw);
            int fileLength = (int) source.length();
            char charBuff[] = new char[fileLength];
            try {
                while (true) {
                    theByte = dIn.readByte();
                    dOut.write(theByte);
                }
            } catch (EOFException eof) {
                bw.flush();
                bw.close();
                dIn.close();
                dOut.flush();
                dOut.close();
            }
            thePath = dest.getPath();
            thePath = thePath.substring(thePath.indexOf("clientImages") + 13);
            if (!clientInterface.upload(thePath)) {
                exceptionHandler("Unable to upload file");
                dest.delete();
            }
        } catch (FileNotFoundException e) {
            exceptionHandler(((Exception) e).getMessage());
        } catch (IOException e) {
            exceptionHandler(((Exception) e).getMessage());
        }
        fileChooser.rescanCurrentDirectory();
    }

    private DefaultMutableTreeNode getDirStruct(String thePath, DefaultMutableTreeNode root) {
        DefaultMutableTreeNode transverser = root;
        Vector userList = new Vector();
        JTree userTree;
        JScrollPane userPane;
        File userDirs = new File(thePath + "\\");
        String[] users;
        users = userDirs.list();
        int i = 0;
        if (users != null) while (i < users.length) {
            File tempU = new File(thePath + "\\" + users[i]);
            if (tempU.isDirectory()) {
                DefaultMutableTreeNode newUser = new DefaultMutableTreeNode(tempU.getName());
                root.add(newUser);
                clientInterface.download(users[i]);
                String[] albums;
                albums = tempU.list();
                int j = 0;
                while (j < albums.length) {
                    File tempA = new File(thePath + "\\" + users[i] + "\\" + albums[j]);
                    if (tempA.isDirectory()) {
                        DefaultMutableTreeNode newAlbum = new DefaultMutableTreeNode(tempA.getName());
                        newUser.add(newAlbum);
                    }
                    j++;
                }
            }
            i++;
        }
        return root;
    }

    private JTree getUserList() {
        Vector userList = new Vector();
        JTree userTree;
        JScrollPane userPane;
        File hi = new File("clientImages\\");
        String[] list;
        list = hi.list();
        int x = 0;
        while (x < list.length) {
            File tempF = new File("clientImages\\" + list[x]);
            if (tempF.isDirectory()) {
                if (!tempF.getName().equals(userName)) userList.add(tempF.getName());
            }
            x++;
        }
        userTree = new JTree(userList);
        return userTree;
    }

    private JTree getAlbumList() {
        Vector albumList = new Vector();
        JTree albumTree;
        JScrollPane albumPane;
        File hi = new File("clientImages\\" + userName + "\\");
        String[] list;
        list = hi.list();
        int x = 0;
        while (x < list.length) {
            File tempF = new File("clientImages\\" + userName + "\\" + list[x]);
            if (tempF.isDirectory()) albumList.add(tempF.getName());
            x++;
        }
        albumTree = new JTree(albumList);
        return albumTree;
    }

    private void addRemFriendUI(final String which) {
        final JFrame theFrame = new JFrame(which);
        Container contentPane = theFrame.getContentPane();
        final JTree userTree = getUserList();
        final JTree albumTree = getAlbumList();
        JScrollPane userPane = new JScrollPane(userTree);
        JScrollPane albumPane = new JScrollPane(albumTree);
        JButton addremButton = new JButton(which);
        JButton cancelButton = new JButton("Cancel");
        JPanel userAlbumPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        userAlbumPanel.add(userPane);
        userAlbumPanel.add(albumPane);
        GridLayout layout = new GridLayout(0, 2);
        userAlbumPanel.setLayout(layout);
        buttonPanel.add(addremButton);
        buttonPanel.add(cancelButton);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.add(userAlbumPanel);
        contentPane.add(buttonPanel);
        theFrame.setBounds(300, 300, 200, 200);
        theFrame.setVisible(true);
        addremButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Object[] users = userTree.getSelectionPaths();
                Object[] albums = albumTree.getSelectionPaths();
                int i = 0;
                int j = 0;
                if ((users != null) && (albums != null)) {
                    for (i = 0; i < users.length; i++) {
                        for (j = 0; j < albums.length; j++) {
                            if (which.equals("Add Friend")) clientInterface.addFriend(albums[j].toString().substring(7, albums[j].toString().length() - 1), users[i].toString().substring(7, users[i].toString().length() - 1)); else clientInterface.rmFriend(albums[j].toString().substring(7, albums[j].toString().length() - 1), users[i].toString().substring(7, users[i].toString().length() - 1));
                        }
                    }
                } else exceptionHandler("No user or album selected");
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                theFrame.dispose();
            }
        });
        theFrame.addWindowFocusListener(new WindowFocusListener() {

            public void windowLostFocus(WindowEvent evt) {
                if (!evt.getOppositeWindow().getName().equals("frame0")) theFrame.requestFocus();
            }

            public void windowGainedFocus(WindowEvent evt) {
            }
        });
    }

    public static void main(String[] args) {
        ImageCave ic = new ImageCave();
    }
}
