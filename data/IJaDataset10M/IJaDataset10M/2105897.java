package user_personalInformation;

import gui_mainFrame.PatientWindow_existingPatient;
import input_Output.ImportFiles;
import input_Output.SaveFiles;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import language.Messages;
import main.ISettings;

/**
 * 
 * @author debous
 * @Create 07/03/2011
 * @lastUpdate 02/04/2011
 * 
 * Modify the user logins
 */
public class ModifyLogins extends JFrame implements ISettings {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5610849750735204260L;

    /**
	 * The user language
	 */
    private String userLanguage;

    /**
	 * The login label
	 */
    private JLabel login;

    /**
	 * The login text field
	 */
    private JTextField loginField;

    /**
	 * The password label
	 */
    private JLabel password;

    /**
	 * The password text field
	 */
    private JTextField passwordField;

    /**
	 * The status
	 */
    private JLabel statut;

    /**
	 * The modify button
	 */
    private JButton modify;

    /**
	 * The cancel button
	 */
    private JButton cancel;

    /**
	 * The user information panel
	 */
    private JPanel panelUserInfo;

    /**
	 * The button panel
	 */
    private JPanel panelButtons;

    /**
	 * The global panel
	 */
    private JPanel panelGlobal;

    /**
	 * Import files
	 */
    private ImportFiles userSettings;

    /**
	 * The container
	 */
    private Container pane;

    /**
	 * Login title
	 */
    private static final String LOGIN_TITLE = Messages.getInstance().getString("ModifyLogins.0");

    /**
	 * Password title
	 */
    private static final String PASSWORD_TITLE = Messages.getInstance().getString("ModifyLogins.1");

    /**
	 * Status title
	 */
    private static final String STATUS = Messages.getInstance().getString("ModifyLogins.2");

    /**
	 * Modify button title
	 */
    private static final String MODIFY_BUTTON_TITLE = Messages.getInstance().getString("ModifyLogins.3");

    /**
	 * Modify success
	 */
    private static final String MODIFY_SUCCESS = Messages.getInstance().getString("ModifyLogins.4");

    /**
	 * Cancel button title
	 */
    private static final String CANCEL_BUTTON_TITLE = Messages.getInstance().getString("ModifyLogins.5");

    /**
	 * Constructor
	 * @param patient The patient user
	 * @param previousFrame The previous frame
	 */
    public ModifyLogins(Patient patient, PatientWindow_existingPatient previousFrame) {
        super(patient.getPersonFirstName() + " " + patient.getPersonLastName().toUpperCase());
        this.userSettings = new ImportFiles();
        pane = this.getContentPane();
        createComponents();
        createPanels();
        this.setResizable(false);
        this.setSize(new Dimension(400, 150));
        this.setLocationRelativeTo(previousFrame);
        this.setVisible(true);
    }

    /**
	 * Create components
	 */
    public void createComponents() {
        this.login = new JLabel(LOGIN_TITLE);
        this.loginField = new JTextField();
        this.password = new JLabel(PASSWORD_TITLE);
        this.passwordField = new JTextField();
        if (userSettings.checkExistingSettingsFile() == 1) {
            loginField.setText(userSettings.getSettingsFile().getLogin());
            passwordField.setText(userSettings.getSettingsFile().getPassword());
            if (userSettings.getSettingsFile().isSaveLanguage()) userLanguage = userSettings.getSettingsFile().getLanguage();
        }
        this.statut = new JLabel(STATUS);
        this.modify = new JButton(MODIFY_BUTTON_TITLE, new ImageIcon(this.getClass().getResource(PATH_MODIFY_SETTINGS_BUTTON)));
        this.modify.setHorizontalTextPosition(AbstractButton.RIGHT);
        this.modify.setVerticalTextPosition(AbstractButton.CENTER);
        this.modify.setToolTipText(MODIFY_BUTTON_TITLE);
        modify.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                modify.setForeground(BUTTON_MOUSE_ENTERED);
            }

            public void mouseExited(MouseEvent arg0) {
                modify.setForeground(BUTTON_MOUSE_EXITED);
            }

            public void mousePressed(MouseEvent arg0) {
                modify.setForeground(BUTTON_MOUSE_PRESSED);
            }
        });
        modify.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (userSettings.checkExistingSettingsFile() == 1) {
                    if (!loginField.getText().equals(userSettings.getSettingsFile().getLogin()) || !passwordField.getText().equals(userSettings.getSettingsFile().getPassword())) {
                        if (userSettings.getSettingsFile().isSaveLanguage()) {
                            if (userSettings.getSettingsFile().isSaveLoginSettings()) {
                                new SaveFiles().saveUserSettings(new Settings(loginField.getText(), passwordField.getText(), userLanguage, true, true));
                                ModifyLogins.this.statut.setText(MODIFY_SUCCESS);
                            } else {
                                new SaveFiles().saveUserSettings(new Settings(loginField.getText(), passwordField.getText(), userLanguage, true));
                                ModifyLogins.this.statut.setText(MODIFY_SUCCESS);
                            }
                        } else {
                            if (userSettings.getSettingsFile().isSaveLoginSettings()) {
                                new SaveFiles().saveUserSettings(new Settings(loginField.getText(), passwordField.getText(), true));
                                ModifyLogins.this.statut.setText(MODIFY_SUCCESS);
                            } else {
                                new SaveFiles().saveUserSettings(new Settings(loginField.getText(), passwordField.getText()));
                                ModifyLogins.this.statut.setText(MODIFY_SUCCESS);
                            }
                        }
                    }
                } else {
                    new SaveFiles().saveUserSettings(new Settings(loginField.getText(), passwordField.getText()));
                    ModifyLogins.this.statut.setText(MODIFY_SUCCESS);
                }
            }
        });
        this.cancel = new JButton(CANCEL_BUTTON_TITLE, new ImageIcon(this.getClass().getResource(PATH_CANCEL_SETTINGS_BUTTON)));
        this.cancel.setHorizontalTextPosition(AbstractButton.RIGHT);
        this.cancel.setVerticalTextPosition(AbstractButton.CENTER);
        this.cancel.setToolTipText(CANCEL_BUTTON_TITLE);
        cancel.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                cancel.setForeground(BUTTON_MOUSE_ENTERED);
            }

            public void mouseExited(MouseEvent arg0) {
                cancel.setForeground(BUTTON_MOUSE_EXITED);
            }

            public void mousePressed(MouseEvent arg0) {
                cancel.setForeground(BUTTON_MOUSE_PRESSED);
            }
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                ModifyLogins.this.setVisible(false);
            }
        });
    }

    /**
	 * Create panels
	 */
    private void createPanels() {
        this.panelUserInfo = new JPanel(new GridLayout(3, 4));
        panelUserInfo.add(login);
        panelUserInfo.add(loginField);
        panelUserInfo.add(password);
        panelUserInfo.add(passwordField);
        panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(modify);
        panelButtons.add(cancel);
        this.panelGlobal = new JPanel(new BorderLayout());
        panelGlobal.add(panelUserInfo, BorderLayout.NORTH);
        panelGlobal.add(panelButtons, BorderLayout.CENTER);
        panelGlobal.add(statut, BorderLayout.SOUTH);
        pane.add(panelGlobal);
    }
}
