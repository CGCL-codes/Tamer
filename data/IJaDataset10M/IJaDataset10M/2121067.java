package plugins.collaboration.jabber.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * @author RReppel
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MapSharingWizardView extends JFrame {

    private static final int INFO_MESSAGE_HEIGHT = 20;

    private static final int INFO_MESSAGE_WIDTH = 120;

    private static final Color BACKGROUNDCOLOR = Color.lightGray;

    private static final int WIDTH = 400;

    private static final int HEIGHT = 180;

    private static final int BUTTON_WIDTH = 60;

    private static final int BUTTON_HEIGHT = 20;

    private static final int LABEL_WIDTH = 80;

    private static final int LABEL_HEIGHT = 20;

    private static final int TEXTFIELD_CHARACTERS = 25;

    private static final int TEXTFIELD_HEIGHT = 20;

    private static final int COMMANDPANEL_HEIGHT = 40;

    JButton closeBtn = new JButton("Close");

    JButton nextBtn = new JButton("Next");

    JButton backBtn = new JButton("Back");

    JPanel wizardButtonPanel = new JPanel(new SpringLayout());

    JPanel closeButtonPanel = new JPanel(new SpringLayout());

    JRadioButton shareMap = new JRadioButton("Share this map with another user.");

    JRadioButton waitForMapSharing = new JRadioButton("Wait for another user to share a map.");

    JPanel shareMapSelectionPanel = new JPanel();

    JTextField serverName = new JTextField("amessage.de", TEXTFIELD_CHARACTERS);

    JTextField userName = new JTextField("", TEXTFIELD_CHARACTERS);

    JPasswordField password = new JPasswordField("", TEXTFIELD_CHARACTERS);

    JPanel connectToServerPanel = new JPanel(new BorderLayout());

    JLabel mapShareUserNameLbl = new JLabel("Username:");

    JTextField mapShareUserName = new JTextField("@amessage.de", TEXTFIELD_CHARACTERS);

    JPanel selectUserPanel = new JPanel(new BorderLayout());

    JButton acceptBtn = new JButton("Accept");

    JButton declineBtn = new JButton("Decline");

    JPanel notificationMessagePanel = new JPanel(new BorderLayout());

    JPanel notificationMessageDisplayPanel = new JPanel();

    JLabel notificationMessage = new JLabel();

    JPanel acceptDeclineMessagePanel = new JPanel(new BorderLayout());

    JLabel acceptDeclineMessage = new JLabel();

    JPanel acceptDeclineMessageDisplayPanel = new JPanel();

    JPanel buttonPanel = new JPanel(new SpringLayout());

    public MapSharingWizardView() {
        setSize(WIDTH, HEIGHT);
        setBackground(BACKGROUNDCOLOR);
    }

    public void addCloseButtonMouseListener(MouseListener closeButtonMouseListener) {
        closeBtn.addMouseListener(closeButtonMouseListener);
    }

    public void addNextButtonMouseListener(MouseListener nextButtonMouseListener) {
        nextBtn.addMouseListener(nextButtonMouseListener);
    }

    public void addBackButtonMouseListener(MouseListener backButtonMouseListener) {
        backBtn.addMouseListener(backButtonMouseListener);
    }

    public void addAcceptButtonMouseListener(MouseListener acceptButtonMouseListener) {
        acceptBtn.addMouseListener(acceptButtonMouseListener);
    }

    public void addDeclineButtonMouseListener(MouseListener declineButtonMouseListener) {
        declineBtn.addMouseListener(declineButtonMouseListener);
    }

    /**
     * This is the GUI where the user chooses between sharing her/his map or
     * waiting for another user to share a map. It's the first screen of the
     * wizard.
     *  
     */
    private void buildShareMapSelectionGui() {
        JPanel shareMapEditPanel = new JPanel();
        ButtonGroup group = new ButtonGroup();
        setTitle("Share Map");
        shareMapSelectionPanel.setSize(WIDTH, HEIGHT);
        shareMapSelectionPanel.setBackground(BACKGROUNDCOLOR);
        SpringLayout layout = new SpringLayout();
        shareMapEditPanel.setLayout(layout);
        shareMapEditPanel.setBackground(BACKGROUNDCOLOR);
        shareMapEditPanel.setSize(WIDTH, HEIGHT - COMMANDPANEL_HEIGHT);
        shareMap.setSelected(true);
        shareMap.setBackground(BACKGROUNDCOLOR);
        waitForMapSharing.setBackground(BACKGROUNDCOLOR);
        group.add(waitForMapSharing);
        group.add(shareMap);
        shareMapEditPanel.add(waitForMapSharing);
        shareMapEditPanel.add(shareMap);
        SpringUtilities.makeCompactGrid(shareMapEditPanel, 2, 1, 15, 15, 10, 10);
        shareMapSelectionPanel.add(shareMapEditPanel, BorderLayout.NORTH);
        addWizardButtons(shareMapSelectionPanel, true);
        this.getContentPane().add(shareMapSelectionPanel);
    }

    /**
     * This builds the user interface which prompts the user for the Jabber
     * server name, a username and a password.
     */
    private void buildConnectToServerGui() {
        JLabel userNameLbl = new JLabel("Username:");
        JLabel passwordLbl = new JLabel("Password:");
        JLabel serverNameLbl = new JLabel("Server Name:");
        JPanel connectToServerEditPanel = new JPanel();
        setTitle("Connect To Jabber Instant Messaging Server");
        SpringLayout layout = new SpringLayout();
        connectToServerEditPanel.setLayout(layout);
        serverNameLbl.setSize(LABEL_WIDTH, LABEL_HEIGHT);
        userNameLbl.setSize(LABEL_WIDTH, LABEL_HEIGHT);
        passwordLbl.setSize(LABEL_WIDTH, LABEL_HEIGHT);
        serverName.setSize(TEXTFIELD_CHARACTERS, TEXTFIELD_HEIGHT);
        userName.setSize(TEXTFIELD_CHARACTERS, TEXTFIELD_HEIGHT);
        password.setSize(TEXTFIELD_CHARACTERS, TEXTFIELD_HEIGHT);
        connectToServerEditPanel.setBackground(BACKGROUNDCOLOR);
        connectToServerEditPanel.setSize(WIDTH, HEIGHT - COMMANDPANEL_HEIGHT);
        connectToServerEditPanel.add(serverNameLbl);
        connectToServerEditPanel.add(serverName);
        connectToServerEditPanel.add(userNameLbl);
        connectToServerEditPanel.add(userName);
        connectToServerEditPanel.add(passwordLbl);
        connectToServerEditPanel.add(password);
        SpringUtilities.makeCompactGrid(connectToServerEditPanel, 3, 2, 15, 15, 10, 10);
        connectToServerPanel.add(connectToServerEditPanel, BorderLayout.NORTH);
        addWizardButtons(connectToServerPanel, false);
        this.getContentPane().add(connectToServerPanel);
    }

    /**
     * Builds the interface which prompts a user to select somebody to share a
     * map with.
     *  
     */
    private void buildSelectUserGui() {
        JPanel selectUserEditPanel = new JPanel();
        setTitle("Select User (example: user@jabber.org)");
        selectUserPanel.setBackground(BACKGROUNDCOLOR);
        SpringLayout layout = new SpringLayout();
        selectUserEditPanel.setLayout(layout);
        mapShareUserNameLbl.setSize(LABEL_WIDTH, LABEL_HEIGHT);
        mapShareUserName.setSize(TEXTFIELD_CHARACTERS, TEXTFIELD_HEIGHT);
        selectUserEditPanel.setBackground(BACKGROUNDCOLOR);
        selectUserEditPanel.setSize(WIDTH, HEIGHT - COMMANDPANEL_HEIGHT);
        selectUserEditPanel.add(mapShareUserNameLbl);
        selectUserEditPanel.add(mapShareUserName);
        SpringUtilities.makeCompactGrid(selectUserEditPanel, 1, 2, 15, 15, 10, 10);
        selectUserPanel.add(selectUserEditPanel, BorderLayout.NORTH);
        addWizardButtons(selectUserPanel, true);
        this.getContentPane().add(selectUserPanel);
    }

    /**
     * Builds the interface which informs the user that a decision to accept map
     * sharing is pending.
     *  
     */
    private void buildNotificationMessageGui(String title, String message) {
        setTitle(title);
        notificationMessagePanel.setBackground(BACKGROUNDCOLOR);
        notificationMessageDisplayPanel.setSize(WIDTH, HEIGHT - COMMANDPANEL_HEIGHT);
        notificationMessageDisplayPanel.setBackground(BACKGROUNDCOLOR);
        SpringLayout layout = new SpringLayout();
        notificationMessageDisplayPanel.setLayout(layout);
        notificationMessageDisplayPanel.remove(notificationMessage);
        notificationMessage.setSize(INFO_MESSAGE_WIDTH, INFO_MESSAGE_HEIGHT);
        notificationMessage.setBackground(BACKGROUNDCOLOR);
        notificationMessage.setText(message);
        notificationMessageDisplayPanel.add(notificationMessage);
        SpringUtilities.makeCompactGrid(notificationMessageDisplayPanel, 1, 1, 50, 50, 10, 10);
        notificationMessagePanel.add(notificationMessageDisplayPanel, BorderLayout.NORTH);
        addCloseButton(notificationMessagePanel);
        this.getContentPane().add(notificationMessagePanel);
    }

    private void buildMapShareAcceptDeclineGui(String message) {
        acceptDeclineMessage.setText(message);
        setTitle("Map Sharing Request");
        acceptDeclineMessagePanel.setBackground(BACKGROUNDCOLOR);
        acceptDeclineMessageDisplayPanel.setSize(WIDTH, HEIGHT - COMMANDPANEL_HEIGHT);
        acceptDeclineMessageDisplayPanel.setBackground(BACKGROUNDCOLOR);
        SpringLayout layout = new SpringLayout();
        acceptDeclineMessageDisplayPanel.setLayout(layout);
        acceptDeclineMessage.setSize(INFO_MESSAGE_WIDTH, INFO_MESSAGE_HEIGHT);
        acceptDeclineMessage.setBackground(BACKGROUNDCOLOR);
        acceptDeclineMessageDisplayPanel.add(acceptDeclineMessage);
        SpringUtilities.makeCompactGrid(acceptDeclineMessageDisplayPanel, 1, 1, 50, 50, 10, 10);
        acceptDeclineMessagePanel.add(acceptDeclineMessageDisplayPanel, BorderLayout.NORTH);
        addAcceptDeclineButtons(acceptDeclineMessagePanel);
        this.getContentPane().add(acceptDeclineMessagePanel);
    }

    /**
     * Adds the "Close", "Back" and "Next" buttons.
     * 
     * @param panel
     * @param showBackButtion
     */
    private void addWizardButtons(JPanel panel, boolean showBackButton) {
        wizardButtonPanel.setSize(WIDTH, COMMANDPANEL_HEIGHT);
        wizardButtonPanel.setBackground(BACKGROUNDCOLOR);
        wizardButtonPanel.add(closeBtn);
        wizardButtonPanel.add(backBtn);
        backBtn.setVisible(showBackButton);
        wizardButtonPanel.add(nextBtn);
        SpringUtilities.makeCompactGrid(wizardButtonPanel, 1, 3, 80, 15, 30, 10);
        panel.add(wizardButtonPanel, BorderLayout.SOUTH);
    }

    /**
     * Adds the "Close", "Back" and "Next" buttons.
     * 
     * @param panel
     */
    private void addCloseButton(JPanel panel) {
        closeButtonPanel = new JPanel(new SpringLayout());
        closeButtonPanel.setSize(WIDTH, COMMANDPANEL_HEIGHT);
        closeButtonPanel.setBackground(BACKGROUNDCOLOR);
        closeButtonPanel.add(closeBtn);
        SpringUtilities.makeCompactGrid(closeButtonPanel, 1, 1, 80 + closeBtn.getWidth() + 30, 15, 30, 10);
        panel.add(closeButtonPanel, BorderLayout.SOUTH);
    }

    /**
     * Adds the "Accept" and "Decline" buttons.
     * 
     * @param panel
     */
    private void addAcceptDeclineButtons(JPanel panel) {
        buttonPanel.setBackground(BACKGROUNDCOLOR);
        buttonPanel.add(acceptBtn);
        buttonPanel.add(declineBtn);
        SpringUtilities.makeCompactGrid(buttonPanel, 1, 2, 80 + closeBtn.getWidth() + 30, 15, 30, 10);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Removes any previous user interfaces shown on the wizard's content pane
     *  
     */
    private void removeUIs() {
        this.getContentPane().remove(shareMapSelectionPanel);
        this.getContentPane().remove(connectToServerPanel);
        this.getContentPane().remove(selectUserPanel);
        this.getContentPane().remove(notificationMessagePanel);
        this.getContentPane().remove(acceptDeclineMessagePanel);
        shareMapSelectionPanel = new JPanel();
        connectToServerPanel = new JPanel(new BorderLayout());
        selectUserPanel = new JPanel(new BorderLayout());
        notificationMessagePanel = new JPanel(new BorderLayout());
        notificationMessage = new JLabel();
        notificationMessageDisplayPanel = new JPanel();
        acceptDeclineMessagePanel = new JPanel(new BorderLayout());
        acceptDeclineMessage = new JLabel();
        acceptDeclineMessageDisplayPanel = new JPanel();
        wizardButtonPanel = new JPanel(new SpringLayout());
        buttonPanel = new JPanel(new SpringLayout());
    }

    public void showMapSharingSelectionDialog() {
        removeUIs();
        buildShareMapSelectionGui();
        show();
        this.repaint();
    }

    public void showConnectToServerDialog() {
        removeUIs();
        buildConnectToServerGui();
        show();
        this.repaint();
    }

    public void showSelectUserDialog() {
        removeUIs();
        buildSelectUserGui();
        show();
        this.repaint();
    }

    public void showAwaitMapSharingMessage() {
        removeUIs();
        buildNotificationMessageGui("Share Map", "Waiting for user to accept invitation to share map...");
        show();
        this.repaint();
    }

    public void showSharingAcceptedMessage(String userName) {
        String message = "You are now sharing the current map with " + userName + ".";
        removeUIs();
        hide();
        buildNotificationMessageGui("Sharing Map", message);
        show();
        this.repaint();
    }

    public void showSharingDeclinedMessage(String userName) {
        String message = userName + " has declined your invitation to share a map.";
        removeUIs();
        buildNotificationMessageGui("Declined Sharing Map", message);
        show();
        this.repaint();
    }

    public void showSharingStoppedMessage(String userName) {
        String message = userName + " has stopped map sharing.";
        removeUIs();
        buildNotificationMessageGui("Stopped Sharing Map", message);
        show();
        this.repaint();
    }

    public void showMapShareAcceptDeclineMessage(String userName) {
        String message = userName + " wants to share a map with you.";
        removeUIs();
        buildMapShareAcceptDeclineGui(message);
        show();
        this.repaint();
    }

    /**
     * True if the user has chosen to share his own map with another user.
     * 
     * @return
     */
    public boolean isShareMapSelected() {
        return shareMap.isSelected();
    }

    /**
     * True if the user has chosen to wait for another user to share a map.
     * 
     * @return
     */
    public boolean isWaitForMapSharingSelected() {
        return waitForMapSharing.isSelected();
    }

    /**
     * The name of the Jabber server to which to connect.
     * 
     * @return
     */
    public String getServername() {
        return serverName.getText();
    }

    /**
     * The name with which the user with which the user should connect to the
     * Jabber server.
     * 
     * @return
     */
    public String getUsername() {
        return userName.getText();
    }

    /**
     * The password for connecting to the Jabber server.
     * 
     * @return
     */
    public String getPassword() {
        return password.getText();
    }

    /**
     * The user name of the use with whom to share a map, e.g.
     * "johnsmith@chat.jabbermind.com".
     * 
     * @return
     */
    public String getMapShareUserName() {
        return mapShareUserName.getText();
    }
}
