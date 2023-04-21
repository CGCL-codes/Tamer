package edu.arsc.fullmetal.client;

import edu.arsc.fullmetal.commons.ControlType;
import edu.arsc.fullmetal.commons.ChatStanza;
import edu.arsc.fullmetal.commons.ConnectRobotStanza;
import edu.arsc.fullmetal.commons.ErrorStanza;
import edu.arsc.fullmetal.commons.ProtocolException;
import edu.arsc.fullmetal.commons.ProtocolStanza;
import edu.arsc.fullmetal.commons.RobotCommandStanza;
import edu.arsc.fullmetal.commons.RobotElement;
import edu.arsc.fullmetal.commons.RobotConnectionStateStanza;
import edu.arsc.fullmetal.commons.RobotsList;
import edu.arsc.fullmetal.commons.RobotCapabilities;
import edu.arsc.fullmetal.commons.ServerInfo;
import edu.arsc.fullmetal.commons.UserElement;
import edu.arsc.fullmetal.commons.UsersList;
import edu.arsc.fullmetal.commons.ProtocolStanza;
import edu.arsc.fullmetal.commons.ProtocolException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import javax.naming.event.*;
import org.jdom.JDOMException;
import edu.arsc.fullmetal.commons.ProtocolException;
import java.lang.Thread.*;

/**
 * Main class, program entry point.
 * 
 * @author rturnq
 */
public class Main {

    private static final MainFrame mainFrame = new MainFrame();

    public static void main(String[] args) {
        mainFrame.setVisible(true);
    }

    static MainFrame getMainFrame() {
        return mainFrame;
    }
}

/**
 * Responsible for instantiating the window as well as
 * being the top level of GUI components as well as
 * containing most of the event handlers.
 * 
 * @author rturnq
 */
class MainFrame extends JFrame implements ComponentListener, WindowListener, MouseMotionListener {

    private BackgroundPanel bgPanel;

    private DevicePanel devicePanel;

    private ControlPanel controlPanel;

    private StatusPanel statusPanel;

    private VideoPanel videoPanel;

    private ChatPanel chatPanel;

    private LoginPanel loginPanel;

    private ConfirmDialog changeDevice;

    private CommentDialog commentDialog;

    private JPanel mainPanel;

    private ServerPoller serverPoller;

    private CardLayout cl;

    private String currentCard;

    private static final String LOGIN_SCREEN = "login card";

    private static final String MAIN_SCREEN = "main card";

    private static final int LOGIN_FADE_OUT = 1;

    private static final int LOGOUT_FADE_OUT = 2;

    private static final int ERROR_FADE_OUT = 3;

    private static final int NO_FADE_OUT = 0;

    private JMenuBar menuBar;

    private myGlassPane glass;

    private String userName;

    private String currentDevice;

    private boolean showCommentDialog;

    /**
	 * Constructs a new {@code MainFrame} class which instantiates
	 * a new window and sets up the components of the GUI.
	 * 
	 */
    public MainFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Full Metal Project");
        setSize(1000, 650);
        addWindowListener(this);
        setLayout(new BorderLayout(0, 0));
        showCommentDialog = true;
        serverPoller = null;
        devicePanel = new DevicePanel();
        devicePanel.addSelectionListener(new DeviceSelectListener());
        Set<String> clawSet = new HashSet<String>();
        clawSet.add(ControlType.CLAW.getCapabilityName());
        Set<String> forkSet = new HashSet<String>();
        forkSet.add(ControlType.FORKLIFT.getCapabilityName());
        Set<String> specSet = new HashSet<String>();
        RobotCapabilities cap1 = new RobotCapabilities(specSet);
        RobotCapabilities cap2 = new RobotCapabilities(clawSet);
        RobotCapabilities cap3 = new RobotCapabilities(forkSet);
        RobotElement testRobot1 = new RobotElement("Spectator", cap1);
        RobotElement testRobot2 = new RobotElement("Test Robot (CLAW)", cap2);
        RobotElement testRobot3 = new RobotElement("Test Robot (FORK)", cap3);
        devicePanel.addDevice(testRobot1);
        devicePanel.addDevice(testRobot2);
        devicePanel.addDevice(testRobot3);
        controlPanel = new ControlPanel();
        ForkControlListener fcl = new ForkControlListener();
        controlPanel.addForkControlListener(fcl, fcl);
        controlPanel.addClawControlListener(new ClawControlListener());
        controlPanel.addMoveControlListener(new MoveControlListener());
        controlPanel.addTurnControlListener(new TurnControlListener());
        controlPanel.addSelectionListener(new VideoSelectionListener());
        statusPanel = new StatusPanel();
        videoPanel = new VideoPanel();
        videoPanel.addButtonListener(new VideoButtonListener());
        chatPanel = new ChatPanel();
        ChatSendListener csl = new ChatSendListener();
        chatPanel.addSendListener(csl, csl);
        loginPanel = new LoginPanel();
        loginPanel.addLoginListener(new LoginListener());
        changeDevice = new ConfirmDialog(this, "Change Device Confirmation");
        commentDialog = new CommentDialog(this, "Leave us a Comment");
        Image bgImage = createImage("img/bg_steel.jpg").getImage();
        bgPanel = new BackgroundPanel(bgImage);
        bgPanel.setLayout(new BorderLayout(0, 0));
        bgPanel.setLayout(new CardLayout(0, 0));
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(485, 650));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 13, 13));
        leftPanel.add(devicePanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 13)));
        leftPanel.add(controlPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 13)));
        leftPanel.add(statusPanel);
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(472, 650));
        rightPanel.setLayout(new BorderLayout(0, 13));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 13, 0));
        rightPanel.add(videoPanel, BorderLayout.CENTER);
        rightPanel.add(chatPanel, BorderLayout.PAGE_END);
        mainPanel = new JPanel();
        mainPanel.setMinimumSize(new Dimension(1000, 650));
        mainPanel.setPreferredSize(new Dimension(1000, 650));
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 13, 0, 13));
        mainPanel.add(leftPanel, BorderLayout.LINE_START);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        bgPanel.add(mainPanel, MAIN_SCREEN);
        bgPanel.add(loginPanel, LOGIN_SCREEN);
        add(bgPanel, BorderLayout.CENTER);
        cl = (CardLayout) bgPanel.getLayout();
        cl.show(bgPanel, LOGIN_SCREEN);
        currentCard = LOGIN_SCREEN;
        devicePanel.setCurrentDevice(currentDevice);
        glass = new myGlassPane(this);
        setGlassPane(glass);
        glass.setVisible(true);
        bgPanel.setVisible(true);
        addMouseMotionListener(this);
        addComponentListener(this);
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.setVisible(false);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenuItem logoutItem = new JMenuItem(" Logout ");
        JMenuItem exitItem = new JMenuItem(" Exit ");
        fileMenu.add(logoutItem);
        fileMenu.add(exitItem);
        logoutItem.addActionListener(new LogoutListener());
        exitItem.addActionListener(new ExitListener());
    }

    /**
     * Makes an image given a path to the image file.  Will test for the
	 * existance of the file.
     * 
     * @param path
     *     The filepath to the image file.
	 * @return An <code>ImageIcon</code> with the given image.  Null if the image doesn't exist.
	 * 
     */
    private ImageIcon createImage(String path) {
        URL imgURL = MainFrame.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.out.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Trys to connect to the server with the given server
	 * and user information
     * 
     * @param serverAddress
     *     The URL of the server.
	 * @param creds
     *     Username and password for the user wrapped in a {@see PasswordAuthentication} object.
	 * @return a {@see ServerConnection} object with the given
	 * connection information or null if the connection could
	 * not be established.
	 * 
     */
    private ServerConnection connect(URL serverAddress, PasswordAuthentication creds) {
        try {
            statusPanel.writeStatus("Connecting to: " + serverAddress.toString());
            ServerConnection conn = new ServerConnection(serverAddress, creds);
            ServerInfo inf = conn.getInfo();
            statusPanel.writeStatus("Connected to server.");
            statusPanel.writeStatus("\tName: " + inf.getName());
            statusPanel.writeStatus("\tURL: " + inf.getURL().toString());
            return conn;
        } catch (JDOMException ex) {
            statusPanel.writeStatus("Parsing error: " + ex.getMessage());
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ProtocolException ex) {
            statusPanel.writeStatus("Protocol error: " + ex.getMessage());
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (MalformedURLException ex) {
            statusPanel.writeStatus("Malformed server URL: " + serverAddress);
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IllegalArgumentException ex) {
            statusPanel.writeStatus("Problem: " + ex.getMessage());
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (UnknownHostException ex) {
            statusPanel.writeStatus("Unknown host: " + ex.getMessage());
            Logger.getLogger(Main.class.getName()).log(Level.WARNING, null, ex);
            logout(ERROR_FADE_OUT, "Unable to contact the server");
            return null;
        } catch (ConnectException ex) {
            statusPanel.writeStatus("Could not connect to server: " + ex.getMessage());
            Logger.getLogger(Main.class.getName()).log(Level.WARNING, null, ex);
            logout(ERROR_FADE_OUT, "Unable to contact the server");
            return null;
        } catch (IOException ex) {
            statusPanel.writeStatus("I/O exception occured: " + ex.getMessage());
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Trys to disconnect from the server by not polling anymore.
     * 
	 * @return if it successfull disconnected.
	 * 
     */
    private boolean disconnect() {
        if (serverPoller != null) serverPoller.stopPolling();
        return true;
    }

    /**
     * Sends a chat message by adding to the poll command queue
	 * and appending it to the chat window.
     * 
	 * @param sendString
	 *		The message to be sent.
	 * 
     */
    private void sendChatText(String sendString) {
        statusPanel.writeStatus("Sending Chat Text: " + sendString);
        serverPoller.addQueueItem(new ChatStanza(sendString));
    }

    /**
     * Logs-out with the given logout type and no message.
     * 
	 * @param type
	 *		The type of logout from the following:
	 *		LOGIN_FADE_OUT, LOGOUT_FADE_OUT, ERROR_FADE_OUT, NO_FADE_OUT.
	 * 
     */
    private void logout(int type) {
        logout(type, "");
    }

    /**
     * Logs-out with the given logout type and a given message.
     * 
	 * @param type
	 *		The type of logout from the following:
	 *		LOGIN_FADE_OUT, LOGOUT_FADE_OUT, ERROR_FADE_OUT, NO_FADE_OUT.
	 * @param msg
	 *		The message to be displayed on the login screen after logout.
	 * 
     */
    private void logout(int type, String msg) {
        disconnect();
        currentCard = LOGIN_SCREEN;
        if (glass.isStopped()) glass.startThread();
        glass.fadeOut(type);
        glass.setError(msg);
    }

    /**
     * Opend the comment dialog box
     * 
	 * 
     */
    private void openCommentDialog() {
        commentDialog.pack();
        if (showCommentDialog) {
            Window owner = commentDialog.getOwner();
            commentDialog.setLocationRelativeTo(owner);
            commentDialog.setVisible(true);
            if (commentDialog.getReturn() == JOptionPane.OK_OPTION) {
                String commentText = commentDialog.getComment();
                if (commentText.length() > 0) {
                    System.out.println(commentText);
                }
            }
        }
    }

    /**
     * Handles a single {@see ProtocolStanza}.
     * 
	 * @param responseStanza
	 *		The stanza to be handled
	 *		
	 * 
     */
    public void handleResponse(ProtocolStanza responseStanza) {
        List<ProtocolStanza> listWrapper = new LinkedList<ProtocolStanza>();
        listWrapper.add(responseStanza);
        handleResponse(listWrapper);
    }

    /**
     * Handles a list of  {@see ProtocolStanza} elements.
     * 
	 * @param responseStanzas
	 *		A list of stanzas to be handled
	 *		
	 * 
     */
    public void handleResponse(List<ProtocolStanza> responseStanzas) {
        for (int i = 0; i < responseStanzas.size(); i++) {
            ProtocolStanza response = responseStanzas.get(i);
            if (response instanceof ChatStanza) {
                ChatStanza cs = (ChatStanza) response;
                statusPanel.writeStatus("Server returned a Chat Stanza");
                if (cs.getContent() != null && cs.getAuthor() != null) chatPanel.appendEchoText(cs.getContent(), cs.getAuthor()); else if (cs.getContent() == null && cs.getAuthor() != null) chatPanel.appendEchoText("Content is NULL", cs.getAuthor()); else if (cs.getContent() != null && cs.getAuthor() == null) chatPanel.appendEchoText(cs.getContent(), "Mr. Null"); else chatPanel.appendEchoText("Content is NULL", "Mr. Null");
            } else if (response instanceof UsersList) {
                UsersList ul = (UsersList) response;
                statusPanel.writeStatus("Server returned a User List Stanza");
                List<UserElement> userList = ul.getUsers();
                if (userList != null) chatPanel.addUserList(userList); else {
                    List nullList = new LinkedList<UserElement>();
                    nullList.add(new UserElement("Mr. Null", "Mr. Null", false));
                    chatPanel.addUserList(nullList);
                }
            } else if (response instanceof RobotsList) {
                RobotsList rl = (RobotsList) response;
                statusPanel.writeStatus("Server returned a Robot List Stanza");
                List<RobotElement> robotList = rl.getRobots();
                if (robotList != null) {
                    devicePanel.addDeviceList(robotList);
                    controlPanel.addDeviceList(robotList);
                } else {
                    List<RobotElement> nullList = new LinkedList<RobotElement>();
                    nullList.add(new RobotElement("Null-bot", new HashSet<String>()));
                    devicePanel.addDeviceList(nullList);
                    controlPanel.addDeviceList(nullList);
                }
            } else if (response instanceof RobotConnectionStateStanza) {
                RobotConnectionStateStanza rcs = (RobotConnectionStateStanza) response;
                statusPanel.writeStatus("Server returned a Conn-State Stanza");
            } else if (response instanceof ErrorStanza) {
                ErrorStanza es = (ErrorStanza) response;
                statusPanel.writeStatus("Server returned an Error Stanza\n\t" + es.getMessage());
                if (es.getType().equals(ErrorStanza.ERR_BAD_AUTH)) logout(ERROR_FADE_OUT, "Invalid username or password");
            } else {
                statusPanel.writeStatus("Server returned something else");
            }
        }
    }

    public void componentResized(ComponentEvent e) {
        Dimension d = this.getSize();
        int width = (int) d.getWidth();
        int height = (int) d.getHeight();
        if (height < 650) {
            this.setSize(width, 650);
        }
        if (width < 1000) {
            this.setSize(1000, height);
        }
        videoPanel.resizeVideo();
        statusPanel.writeStatus("");
        chatPanel.appendEchoText("", "");
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
        videoPanel.resizeVideo();
    }

    public void windowActivated(WindowEvent e) {
        if (changeDevice.isVisible()) changeDevice.setVisible(true);
        if (commentDialog.isVisible()) commentDialog.setVisible(true);
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
        videoPanel.resizeVideo();
    }

    public void windowClosing(WindowEvent e) {
        if (currentCard.equals(MAIN_SCREEN)) openCommentDialog();
        disconnect();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        videoPanel.showCameraButtons(false);
    }

    public void mouseDragged(MouseEvent e) {
    }

    class LoginListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            String[] vals = loginPanel.getVals();
            URL server = null;
            controlPanel.reset();
            statusPanel.reset();
            chatPanel.reset();
            commentDialog.reset();
            try {
                server = new URL(vals[2]);
            } catch (MalformedURLException e) {
                server = null;
            }
            if (server != null) {
                if (vals[0].length() > 0 && vals[1].length() > 0) {
                    loginPanel.disableFields(true);
                    glass.drawLoadingDots(true);
                    glass.startThread();
                    PasswordAuthentication credentials = new PasswordAuthentication(vals[0], vals[1].toCharArray());
                    ServerConnection conn = connect(server, credentials);
                    if (conn == null) {
                        logout(ERROR_FADE_OUT, "Unable to contact the server");
                    } else {
                        glass.fadeOut(LOGIN_FADE_OUT);
                        serverPoller = new ServerPoller(conn);
                        serverPoller.setPeriod(500);
                        serverPoller.startPolling();
                        userName = credentials.getUserName();
                    }
                } else {
                    loginPanel.setError("Missing name or password");
                }
            } else {
                loginPanel.setError("Invalid server address");
            }
        }
    }

    class LogoutListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            statusPanel.writeStatus("Disconnecting");
            openCommentDialog();
            logout(LOGOUT_FADE_OUT, "hello");
        }
    }

    class ExitListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            openCommentDialog();
            disconnect();
            System.exit(0);
        }
    }

    class DeviceSelectListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            RobotElement newDevice = devicePanel.getSelection();
            if (newDevice != null) {
                Window owner = changeDevice.getOwner();
                changeDevice.setLocationRelativeTo(owner);
                if (newDevice.equals(currentDevice)) {
                    changeDevice.setMessage("You are already connected to this device.\nPress Connect to reconnect to this device.");
                } else {
                    changeDevice.setMessage("Are you sure you want to connect to this device?");
                }
                changeDevice.setVisible(true);
                if (changeDevice.getReturn() == JOptionPane.OK_OPTION) {
                    currentDevice = newDevice.getName();
                    RobotCapabilities capabilities = newDevice.getCapabilities();
                    statusPanel.writeStatus("New Device Selected: " + currentDevice);
                    serverPoller.addQueueItem(new ConnectRobotStanza(currentDevice));
                    devicePanel.setCurrentDevice(currentDevice);
                    if (capabilities.supportsControlType(ControlType.FORKLIFT)) controlPanel.swapControlMode(ControlPanel.FORK_MODE); else if (capabilities.supportsControlType(ControlType.CLAW)) controlPanel.swapControlMode(ControlPanel.CLAW_MODE); else controlPanel.swapControlMode(ControlPanel.SPECTATOR_MODE);
                }
            }
        }
    }

    class ForkControlListener implements MouseListener, MouseMotionListener {

        int percent = 0;

        public void mouseDragged(MouseEvent e) {
            percent = controlPanel.slideForkControl(e.getX());
        }

        public void mousePressed(MouseEvent e) {
            percent = controlPanel.slideForkControl(e.getX());
        }

        public void mouseReleased(MouseEvent e) {
            statusPanel.writeStatus("Robot Forklift to: " + percent + "%");
            serverPoller.addQueueItem(new RobotCommandStanza(ControlType.FORKLIFT, percent));
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

    class ClawControlListener implements ActionListener {

        boolean open = false;

        public void actionPerformed(ActionEvent e) {
            open = !open;
            controlPanel.setClawText(open);
            if (open) {
                statusPanel.writeStatus("Robot Claw to: open");
                serverPoller.addQueueItem(new RobotCommandStanza(ControlType.CLAW, 100));
            } else {
                statusPanel.writeStatus("Robot Claw to: closed");
                serverPoller.addQueueItem(new RobotCommandStanza(ControlType.CLAW, 0));
            }
        }
    }

    class MoveControlListener implements MouseListener {

        int magnitude = 0;

        public void mousePressed(MouseEvent e) {
            magnitude = controlPanel.getMoveAmount(e.getY());
            statusPanel.writeStatus("Robot Move by: " + magnitude + " units");
            serverPoller.addQueueItem(new RobotCommandStanza(ControlType.FORWARD_BACKWARD, magnitude));
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

    class TurnControlListener implements MouseListener {

        double angle = 0;

        public void mousePressed(MouseEvent e) {
            angle = controlPanel.getTurnAmount(e.getX(), e.getY());
            statusPanel.writeStatus("Robot Turn by: " + (int) angle + " deg");
            serverPoller.addQueueItem(new RobotCommandStanza(ControlType.ROTATE, (int) angle));
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

    class VideoSelectionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            RobotElement newVideoSource = controlPanel.getSelection();
            String newVideoSourceName = newVideoSource.getName();
            if (newVideoSource != null) {
                statusPanel.writeStatus("Video Source to: " + newVideoSourceName);
                serverPoller.addQueueItem(new ConnectRobotStanza(newVideoSourceName));
            }
        }
    }

    class VideoButtonListener implements MouseListener {

        private int tiltAmount = 0;

        private int panAmount = 0;

        public void mouseEntered(MouseEvent e) {
            videoPanel.showCameraButtons(true);
            if (e.getSource() instanceof JLabel) videoPanel.swapButtonImage((JLabel) e.getSource(), VideoPanel.MOUSE_OVER);
        }

        public void mouseExited(MouseEvent e) {
            if (e.getSource() instanceof JLabel) videoPanel.swapButtonImage((JLabel) e.getSource(), VideoPanel.MOUSE_OUT);
        }

        public void mousePressed(MouseEvent e) {
            int buttonID = -1;
            if (e.getSource() instanceof JLabel) {
                videoPanel.swapButtonImage((JLabel) e.getSource(), VideoPanel.MOUSE_DOWN);
                buttonID = videoPanel.getButton((JLabel) e.getSource());
            }
            switch(buttonID) {
                case VideoPanel.CAMERA_UP:
                    statusPanel.writeStatus("Robot Camera Tilt: up");
                    serverPoller.addQueueItem(new RobotCommandStanza(ControlType.CAMERA_TILT, ++tiltAmount));
                    break;
                case VideoPanel.CAMERA_DOWN:
                    statusPanel.writeStatus("Robot Camera Tilt: down");
                    serverPoller.addQueueItem(new RobotCommandStanza(ControlType.CAMERA_TILT, --tiltAmount));
                    break;
                case VideoPanel.CAMERA_LEFT:
                    statusPanel.writeStatus("Robot Camera Pan: left");
                    serverPoller.addQueueItem(new RobotCommandStanza(ControlType.CAMERA_PAN, --panAmount));
                    break;
                case VideoPanel.CAMERA_RIGHT:
                    statusPanel.writeStatus("Robot Camera Pan: right");
                    serverPoller.addQueueItem(new RobotCommandStanza(ControlType.CAMERA_PAN, ++panAmount));
                    break;
                case VideoPanel.CAMERA_CENTER:
                    statusPanel.writeStatus("Robot Camera Pan: center");
                    panAmount = 0;
                    serverPoller.addQueueItem(new RobotCommandStanza(ControlType.CAMERA_PAN, panAmount));
                    break;
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.getSource() instanceof JLabel) videoPanel.swapButtonImage((JLabel) e.getSource(), VideoPanel.MOUSE_OVER);
        }

        public void mouseClicked(MouseEvent e) {
        }
    }

    class ChatSendListener implements KeyListener, ActionListener {

        boolean ctrl = false;

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) ctrl = true;
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) ctrl = false;
        }

        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == KeyEvent.VK_ENTER && !ctrl) {
                String sendString = chatPanel.getSendText();
                if (sendString.length() > 0) {
                    sendString = sendString.substring(0, sendString.length() - 1);
                    chatPanel.appendEchoText(sendString, userName);
                    chatPanel.clearSendText();
                    sendChatText(sendString);
                }
            } else if (e.getKeyChar() == KeyEvent.VK_ENTER && ctrl) {
                chatPanel.appendSendText("\n");
            }
        }

        public void actionPerformed(ActionEvent e) {
            String sendString = chatPanel.getSendText();
            if (sendString.length() > 0) {
                chatPanel.appendEchoText(sendString, userName);
                chatPanel.clearSendText();
                sendChatText(sendString);
            }
        }
    }

    class myGlassPane extends JComponent implements Runnable {

        private boolean drawDots, stopped;

        private Component owner;

        private int step, opacity, fadeSpeed, fadeAmount, fadeType;

        private Thread glassThread;

        private String errorMsg;

        public myGlassPane(Component owner) {
            this.owner = owner;
            step = 0;
            opacity = 0;
            fadeSpeed = 1;
            fadeAmount = 2;
            fadeType = NO_FADE_OUT;
            drawDots = false;
            stopped = true;
            errorMsg = "";
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(new Color(225, 230, 235, opacity));
            g.fillRect(0, 0, owner.getWidth(), owner.getHeight());
            if (drawDots) {
                g.setColor(new Color(255, 255, 255, opacity));
                int w = owner.getWidth() / 2;
                int h = owner.getHeight() / 2;
                h -= h / 10;
                int growth = 5;
                int baseSize = 9;
                int[] size = { baseSize, baseSize, baseSize, baseSize, baseSize, 0 };
                int[] offset = { 0, 0, 0, 0, 0, 0 };
                size[step] += growth;
                offset[step] = (size[step] - baseSize) / 2;
                g.fillOval(w - 40 - offset[0], h - offset[0], size[0], size[0]);
                g.fillOval(w - 20 - offset[1], h - offset[1], size[1], size[1]);
                g.fillOval(w - offset[2], h - offset[2], size[2], size[2]);
                g.fillOval(w + 20 - offset[3], h - offset[3], size[3], size[3]);
                g.fillOval(w + 40 - offset[4], h - offset[4], size[4], size[4]);
            }
        }

        public void drawLoadingDots(boolean b) {
            drawDots = b;
        }

        public void stepDots() {
            step++;
            if (step > 5) step = 0;
        }

        public void stopThread() {
            stopped = true;
        }

        private void startThread() {
            stopped = false;
            glassThread = new Thread(this);
            glassThread.start();
        }

        public void run() {
            int count = 0;
            while (!stopped) {
                try {
                    Thread.sleep(15);
                    count++;
                } catch (InterruptedException e) {
                }
                opacity += fadeAmount * fadeSpeed;
                if (count == 25) {
                    count = 0;
                    stepDots();
                }
                if (fadeAmount < 0 && opacity <= 0) {
                    stopThread();
                    opacity = 0;
                    fadeAmount = 2;
                } else if (opacity >= 255) {
                    opacity = 255;
                    if (fadeType == LOGIN_FADE_OUT) {
                        fadeAmount = -2;
                        menuBar.setVisible(true);
                        loginPanel.setError("");
                        cl.show(bgPanel, MAIN_SCREEN);
                        currentCard = MAIN_SCREEN;
                    } else if (fadeType == LOGOUT_FADE_OUT) {
                        fadeAmount = -2;
                        menuBar.setVisible(false);
                        loginPanel.disableFields(false);
                        loginPanel.reset();
                        cl.show(bgPanel, LOGIN_SCREEN);
                    } else if (fadeType == ERROR_FADE_OUT) {
                        fadeAmount = -2;
                        menuBar.setVisible(false);
                        loginPanel.disableFields(false);
                        loginPanel.reset();
                        loginPanel.setError(errorMsg);
                        cl.show(bgPanel, LOGIN_SCREEN);
                    } else fadeAmount = 0;
                }
                if (opacity > 240) videoPanel.resizeVideo();
                repaint();
            }
        }

        public boolean isStopped() {
            return stopped;
        }

        public void fadeOut(int type) {
            fadeType = type;
            fadeAmount = 2;
        }

        public int getOpacity() {
            return opacity;
        }

        public void setError(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }
}
