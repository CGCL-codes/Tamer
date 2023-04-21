package org.codeheretic.jericho;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.accessibility.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import helma.xmlrpc.XmlRpcException;

public class ManilaGUI {

    private JFrame f = new JFrame("ManilaGUI");

    private JFrame postf = new JFrame("ManilaGUI Message Selector");

    private JTextArea textArea;

    private JTextField subjectField;

    private static ManilaDataHolder data;

    public ManilaGUI(JTextArea textArea, JTextField subjectField) {
        data = new ManilaDataHolder();
        this.textArea = textArea;
        this.subjectField = subjectField;
    }

    public ManilaDataHolder getDataHolder() {
        return data;
    }

    public void showManilaLoginScreen(String action) {
        try {
            JPanel userScreen = new JPanel();
            userScreen.setLayout(new BorderLayout());
            JPanel loginScreen = new JPanel();
            loginScreen.setLayout(new BorderLayout());
            JTextField userField = new JTextField(20);
            JPasswordField passField = new JPasswordField(20);
            JTextField urlField = new JTextField(40);
            JPanel userPanel = new JPanel();
            userPanel.add(new JLabel("Site Username"));
            userPanel.add(userField);
            JPanel passPanel = new JPanel();
            passPanel.add(new JLabel("Site Password"));
            passPanel.add(passField);
            JPanel urlPanel = new JPanel();
            urlPanel.add(new JLabel("Site Url"));
            urlPanel.add(urlField);
            JButton login = new JButton("Login");
            login.setActionCommand("login");
            login.addActionListener(new LoginAction(userField, passField, urlField, action));
            userScreen.add(userPanel, BorderLayout.NORTH);
            userScreen.add(passPanel);
            loginScreen.add(userScreen, BorderLayout.NORTH);
            loginScreen.add(urlPanel);
            loginScreen.add(login, BorderLayout.SOUTH);
            f.getContentPane().add(loginScreen);
            redrawScreen(f);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JMenu getMenu() {
        JMenu manila = new JMenu("Manila");
        manila.setMnemonic(KeyEvent.VK_M);
        manila.getAccessibleContext().setAccessibleDescription("Contains Manila related tasks");
        JMenuItem flipmenu = new JMenuItem("Flip Homepage", KeyEvent.VK_F);
        flipmenu.setActionCommand("fliphome");
        flipmenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        flipmenu.getAccessibleContext().setAccessibleDescription("Flip the current homepage");
        flipmenu.addActionListener(new FlipAction());
        manila.add(flipmenu);
        JMenuItem home = new JMenuItem("Edit Homepage", KeyEvent.VK_E);
        home.setActionCommand("edithome");
        home.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        home.getAccessibleContext().setAccessibleDescription("Edit the current homepage");
        home.addActionListener(new HomeAction());
        manila.add(home);
        JMenuItem post = new JMenuItem("Save Homepage", KeyEvent.VK_S);
        post.setActionCommand("savehome");
        post.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        post.getAccessibleContext().setAccessibleDescription("Save the current homepage");
        post.addActionListener(new PostAction());
        manila.add(post);
        manila.addSeparator();
        JMenuItem thread = new JMenuItem("Get Current Threads");
        thread.setActionCommand("thread");
        thread.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        thread.getAccessibleContext().setAccessibleDescription("Get a list of the current threads");
        thread.addActionListener(new HeaderAction());
        manila.add(thread);
        JMenuItem story = new JMenuItem("Get Current Stories");
        story.setActionCommand("story");
        story.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        story.getAccessibleContext().setAccessibleDescription("Get a list of the current stories");
        story.addActionListener(new HeaderAction());
        manila.add(story);
        manila.addSeparator();
        return manila;
    }

    public void redrawScreen(JFrame fr) {
        redrawScreen(fr, true);
    }

    public void redrawScreen(JFrame fr, boolean pack) {
        if (pack) {
            fr.pack();
        }
        fr.show();
    }

    void clearScreen() {
        f.hide();
    }

    void clearOtherScreen(JFrame fr) {
        fr.getContentPane().removeAll();
        fr.setJMenuBar(null);
    }

    public void getHomePage() {
        try {
            data.setHomepageMsgNum();
            data.setMsgNum(data.getHomepageMsgNum());
            Hashtable msg = data.getJManila().getMessage(data.getUsername(), data.getPassword(), data.getSitename(), data.getHomepageMsgNum());
            subjectField.setText((String) msg.get("subject"));
            String body = (String) msg.get("body");
            textArea.setText(body);
        } catch (XmlRpcException xre) {
            showErrorScreen(xre.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void flipHomePage() {
        String msg = "";
        int msg_type = JOptionPane.INFORMATION_MESSAGE;
        String title = "Homepage Flipped";
        try {
            boolean flip = data.getJManila().flipHomePage(data.getUsername(), data.getPassword(), data.getSitename());
            if (flip) {
                msg = "Your page was flipped";
                getHomePage();
            } else {
                msg = "There was an error during the flip process";
                msg_type = JOptionPane.ERROR_MESSAGE;
                title = "Error During Flip";
            }
            JOptionPane.showMessageDialog(f, msg, title, msg_type);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showErrorScreen(String msg) {
        JOptionPane.showMessageDialog(f, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    class LoginAction implements ActionListener {

        JTextField userField;

        JPasswordField passField;

        JTextField urlField;

        String action;

        public LoginAction(JTextField userField, JPasswordField passField, JTextField urlField, String action) {
            this.userField = userField;
            this.passField = passField;
            this.urlField = urlField;
            this.action = action;
        }

        public void actionPerformed(ActionEvent ev) {
            String errMsg = null;
            try {
                if (userField.getText() == null || userField.getText().equals("")) errMsg = "No Username Given";
                if (new String(passField.getPassword()) == null || new String(passField.getPassword()).equals("")) errMsg = "No Password Given";
                if (urlField.getText() == null || urlField.getText().equals("")) errMsg = "No URL Given";
                if (errMsg == null) {
                    String url = urlField.getText();
                    char tc = url.charAt(url.length() - 1);
                    if (tc != '/') url += "/";
                    data.setSite(url);
                    data.setUsername(userField.getText());
                    String pass = new String(passField.getPassword());
                    data.setPassword(pass);
                    data.setHomepageMsgNum();
                    clearScreen();
                    if (action.equals("flip")) flipHomePage(); else if (action.equals("gethome")) getHomePage(); else System.out.println("unknown actions");
                } else {
                    JOptionPane.showMessageDialog(f, errMsg, "Data Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class FlipAction implements ActionListener {

        public void actionPerformed(ActionEvent ev) {
            if (data.getUsername() != null) {
                flipHomePage();
            } else {
                showManilaLoginScreen("flip");
            }
        }
    }

    class PostAction implements ActionListener {

        public void actionPerformed(ActionEvent ev) {
            try {
                String body = textArea.getText();
                char[] bodyChars = body.toCharArray();
                String subject = subjectField.getText();
                StringBuffer bodyBuf = new StringBuffer();
                BufferedReader bufRead = new BufferedReader(new CharArrayReader(bodyChars));
                String line = "";
                while ((line = bufRead.readLine()) != null) {
                    bodyBuf.append(line + "\r\n");
                }
                Hashtable ret = data.getJManila().setMessage(data.getUsername(), data.getPassword(), data.getSitename(), data.getMsgNum(), subject, bodyBuf.toString());
                String msg = "";
                if (!ret.containsKey("faultCode")) {
                    msg = "Your post went through";
                } else {
                    msg = "There was an error during the posting process";
                }
                JOptionPane.showMessageDialog(f, msg, "Message Posted", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class HomeAction implements ActionListener {

        public void actionPerformed(ActionEvent ev) {
            try {
                if (data.getUsername() != null) getHomePage(); else showManilaLoginScreen("gethome");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class HeaderAction implements ActionListener {

        public void actionPerformed(ActionEvent ev) {
            try {
                Hashtable head = new Hashtable();
                String command = ev.getActionCommand();
                if (command.equals("head")) {
                    head = data.getJManila().getCurrentHeaders(data.getSitename(), data.getUsername(), data.getPassword());
                } else if (command.equals("thread")) {
                    head = data.getJManila().getCurrentThreads(data.getSitename(), data.getUsername(), data.getPassword());
                } else if (command.equals("story")) {
                    head = data.getJManila().getStoriesHeaders(data.getSitename(), data.getUsername(), data.getPassword());
                }
                int numPosts = head.size();
                String[] posts = new String[numPosts];
                int count = 0;
                for (Enumeration e = head.elements(); e.hasMoreElements(); ) {
                    Hashtable msg = (Hashtable) e.nextElement();
                    Integer msgNum = (Integer) msg.get("msgNum");
                    String subject = (String) msg.get("subject");
                    posts[count++] = msgNum + " - " + subject;
                }
                clearOtherScreen(postf);
                JList postlist = new JList(posts);
                postlist.setVisibleRowCount(posts.length);
                postlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                postlist.addListSelectionListener(new ChangePostAction());
                JScrollPane postScrollPane = new JScrollPane(postlist);
                postf.getContentPane().add(postScrollPane);
                redrawScreen(postf);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class ChangePostAction implements ActionListener, ListSelectionListener {

        public void actionPerformed(ActionEvent ev) {
            String command = ev.getActionCommand();
        }

        public void valueChanged(ListSelectionEvent ev) {
            if (!ev.getValueIsAdjusting()) {
                JList postlist = (JList) ev.getSource();
                String summary = (String) postlist.getSelectedValue();
                int spaceIndex = summary.indexOf(" ");
                String num = summary.substring(0, spaceIndex);
                int msgNum = (new Integer(num)).intValue();
                try {
                    Hashtable msg = data.getJManila().getMessage(data.getUsername(), data.getPassword(), data.getSitename(), msgNum);
                    subjectField.setText((String) msg.get("subject"));
                    String body = (String) msg.get("body");
                    textArea.setText(body);
                    data.setMsgNum(msgNum);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    class WindowCloser extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
