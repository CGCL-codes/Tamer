package linker.plugin;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import linker.gui.WindowSaver;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

/**
 * The twitter like server.
 * 
 * @version 2008-05-29
 * @author Jianfeng tujf.cn@gmail.com
 * 
 */
public class Linter extends Plugin implements ActionListener {

    /**
	 * The you say label. TODO Should be translated.
	 */
    private final String youSayLabel = "You: ";

    /**
	 * Update field.
	 */
    private JTextField updateField = new JTextField();

    /**
	 * The list used to show messages.
	 */
    private JList list = new JList();

    /**
	 * List model.
	 */
    private DefaultListModel listModel = new DefaultListModel();

    private HashMap<String, String> sendMap = new HashMap<String, String>();

    /**
	 * The saved chat to recieve message.
	 */
    private ArrayList<Chat> chatList = new ArrayList<Chat>();

    /**
	 * The robotManager.
	 */
    private LinterRobotManager manager = null;

    /**
	 * The managerClass.
	 */
    private Class managerClass;

    /**
	 * The window.
	 */
    private JFrame window = new JFrame("Linter");

    /**
	 * 
	 */
    public Linter() {
        super("Linter");
        int n = getElementSize("linter.recieve");
        for (int i = 0; i < n; i++) {
            String name = getValue("linter.recieve.robot" + i);
            this.addRobot(name);
        }
        window.setName("Linter");
        Container container = window.getContentPane();
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton manageButton = new JButton("Manage");
        manageButton.addActionListener(this);
        manageButton.setMnemonic(KeyEvent.VK_M);
        panel.add(manageButton);
        JButton replyButton = new JButton("Reply");
        replyButton.addActionListener(this);
        replyButton.setMnemonic(KeyEvent.VK_R);
        panel.add(replyButton);
        JButton shareButton = new JButton("Share");
        shareButton.addActionListener(this);
        shareButton.setMnemonic(KeyEvent.VK_S);
        panel.add(shareButton);
        container.add(panel, BorderLayout.NORTH);
        list.setModel(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        container.add(new JScrollPane(list), BorderLayout.CENTER);
        updateField.addActionListener(this);
        container.add(updateField, BorderLayout.SOUTH);
        window.setSize(300, 300);
        window.setLocationRelativeTo(null);
        try {
            WindowSaver.loadSettings(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
        window.setVisible(true);
    }

    /**
	 * Set new Path.
	 * 
	 * @param path
	 *            The plugin dir path.
	 */
    public final void setPath(final String path) {
        super.setPath(path);
        managerClass = PluginClassLoader.getClassLoader().findClass(new File(path + File.separator + "LinterRobotManager.class"));
        window.setIconImage(Toolkit.getDefaultToolkit().createImage(path + File.separator + "icon.png"));
    }

    public final void saveRobots() {
        deleteValue("linter.recieve");
        int n = chatList.size();
        for (int i = 0; i < n; i++) {
            setValue("linter.recieve.robot" + i, chatList.get(i).getParticipant());
        }
    }

    /**
	 * Recive Cha message.
	 * 
	 * @param message
	 *            The recieved message.
	 * @return The react message.
	 */
    public final String recieveChatMessage(final Message message) {
        if (!window.isVisible()) {
            this.addMessageNotifier();
        }
        String body = message.getBody();
        listModel.insertElementAt(message.getBody(), 0);
        sendMap.put(body, message.getFrom());
        return Plugin.NOCHAT;
    }

    /**
	 * ActionPerformed.
	 * 
	 * @param e
	 *            ActionEvent.
	 */
    public final void actionPerformed(final ActionEvent e) {
        if (e.getSource() == updateField) {
            String message = updateField.getText();
            update(message);
        } else if (e.getActionCommand() == "Reply") {
            if (list.getSelectedIndex() < 0) {
                return;
            }
            String message = (String) list.getSelectedValue();
            if (message.startsWith(youSayLabel)) {
                return;
            }
            updateField.setText("@" + message.substring(0, message.indexOf(":")) + " ");
            updateField.grabFocus();
            updateField.setCaretPosition(updateField.getText().length());
        } else if (e.getActionCommand() == "Share") {
            if (list.getSelectedIndex() < 0) {
                return;
            }
            String message = (String) list.getSelectedValue();
            if (message.startsWith(youSayLabel)) {
                return;
            }
            update("(Share):" + message.substring(message.indexOf(" ")));
        } else if (e.getActionCommand() == "Manage") {
            manageRobots();
        }
    }

    /**
	 * Get robots manager window.
	 * 
	 */
    public final void manageRobots() {
        if (manager == null) {
            try {
                manager = (LinterRobotManager) managerClass.newInstance();
                manager.setLinter(this);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        manager.setVisible(true);
    }

    /**
	 * Close Manager.
	 */
    public final void closeManager() {
        manager = null;
    }

    /**
	 * 
	 * @param robot
	 *            The robot to be added.
	 */
    public final void addRobot(final String robot) {
        addPluginChatListener(robot);
        chatList.add(getChat(robot));
    }

    /**
	 * Remove Robot.
	 * 
	 * @param robot
	 *            The robot should be removed.
	 */
    public final void removeRobot(final String robot) {
        for (Chat chat : chatList) {
            if (chat.getParticipant().indexOf(robot) > 0) {
                chatList.remove(chat);
                break;
            }
        }
        removePluginChatListener(robot);
    }

    /**
	 * Update message.
	 * 
	 * @param message
	 *            The new message.
	 */
    private void update(final String message) {
        try {
            for (int i = 0; i < chatList.size(); i++) {
                chatList.get(i).sendMessage(message);
            }
        } catch (XMPPException ex) {
            ex.printStackTrace();
        }
        listModel.insertElementAt(youSayLabel + message, 0);
        updateField.setText("");
    }

    /**
	 * Show window.
	 * 
	 */
    public final void showWindow() {
        removeMessageNotifier();
        window.setVisible(true);
    }

    /**
	 * DoubleClick event.
	 * 
	 */
    public final void doubleClick() {
        showWindow();
    }

    /**
	 * Get robots.
	 * 
	 * @return The robots.
	 */
    public final ArrayList getRobots() {
        return chatList;
    }
}
