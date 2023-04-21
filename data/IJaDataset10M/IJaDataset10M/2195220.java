package ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import ui.Messages;
import ui.command.CommandExecutor;
import ui.command.IO.ExportUserAsVCardCommand;
import ui.command.creation.ShowUserAddressBookDialogCommand;
import ui.command.creation.ShowUserDocumentDialogCommand;
import ui.dialog.DocumentDialog;
import base.user.Document;
import base.user.User;
import base.user.UserCredential;
import base.user.UserFactory;
import base.user.UserGender;
import com.toedter.calendar.JDateChooser;

@SuppressWarnings("serial")
public class UserPanel extends JPanel {

    private static final transient Logger logger = Logger.getLogger(UserPanel.class.getName());

    private JPanel topPanel;

    private JPanel bottomPanel;

    private JPanel contentPanel;

    private JPanel buttonPanel;

    private JButton documentButton;

    private JButton exportVCardButton;

    private JButton addressBookButton;

    private JPanel namePanel;

    private JTextField nameTextField;

    private JPanel surnamePanel;

    private JTextField surnameTextField;

    private JPanel birthdayPanel;

    private JDateChooser birthdayDateChooser;

    private JPanel imagePanel;

    private JPanel nicknamePanel;

    private JTextField nicknameTextField;

    private JPanel passwordPanel;

    private JPasswordField passwordField;

    private JPanel retypedPasswordPanel;

    private JPasswordField retypedPasswordField;

    private JPanel credentialPanel;

    private JComboBox credentialComboBox;

    private JPanel genderPanel;

    private JComboBox genderComboBox;

    private final DocumentDialog documentDialog;

    private final User user;

    public UserPanel(User user) {
        this.user = user;
        documentDialog = new DocumentDialog(user == null ? user = UserFactory.newUser() : user, user.getDocument());
        initialize();
    }

    protected void initialize() {
        this.setBorder(new TitledBorder(Messages.getString("common.user")));
        this.setLayout(new BorderLayout());
        this.add(getContentPanel(), BorderLayout.CENTER);
    }

    /**
	 * @return Returns the credentialPanel.
	 */
    protected JPanel getCredentialPanel() {
        if (credentialPanel == null) {
            credentialPanel = new JPanel();
            credentialPanel.setLayout(new BorderLayout());
            TitledBorder titledBorder = new TitledBorder(Messages.getString("common.credential"));
            credentialPanel.setBorder(titledBorder);
            credentialPanel.add(getCredentialComboBox(), BorderLayout.CENTER);
        }
        return credentialPanel;
    }

    /**
	 * @return Returns the credentialComboBox.
	 */
    protected JComboBox getCredentialComboBox() {
        if (credentialComboBox == null) {
            credentialComboBox = new JComboBox();
            for (int i = 0; i < UserCredential.USER_CREDENTIAL.length; i++) credentialComboBox.addItem(UserCredential.USER_CREDENTIAL[i]);
            credentialComboBox.setSelectedItem(user == null ? UserCredential.USER_CREDENTIAL[UserCredential.USER] : user.getCredential());
        }
        return credentialComboBox;
    }

    /**
	 * @return Returns the genderPanel.
	 */
    protected JPanel getGenderPanel() {
        if (genderPanel == null) {
            genderPanel = new JPanel();
            genderPanel.setLayout(new BorderLayout());
            TitledBorder titledBorder = new TitledBorder(Messages.getString("common.gender"));
            genderPanel.setBorder(titledBorder);
            genderPanel.add(getGenderComboBox(), BorderLayout.CENTER);
        }
        return genderPanel;
    }

    /**
	 * @return Returns the genderComboBox.
	 */
    protected JComboBox getGenderComboBox() {
        if (genderComboBox == null) {
            genderComboBox = new JComboBox();
            for (int i = 0; i < UserGender.USER_GENDER.length; i++) genderComboBox.addItem(UserGender.USER_GENDER[i]);
            genderComboBox.setSelectedItem(user == null ? UserGender.USER_GENDER[UserGender.MALE] : user.getGender());
        }
        return genderComboBox;
    }

    /**
	 * @return Returns the namePanel.
	 */
    protected JPanel getNamePanel() {
        if (namePanel == null) {
            namePanel = new JPanel();
            namePanel.setPreferredSize(new Dimension(100, 30));
            namePanel.setLayout(new BorderLayout());
            TitledBorder titledBorder = new TitledBorder(Messages.getString("common.name"));
            namePanel.setBorder(titledBorder);
            namePanel.add(getNameTextField(), BorderLayout.CENTER);
        }
        return namePanel;
    }

    /**
	 * @return Returns the nameTextField.
	 */
    protected JTextField getNameTextField() {
        if (nameTextField == null) {
            nameTextField = new JTextField();
            nameTextField.setText(user == null ? "" : user.getName());
            nameTextField.setDragEnabled(true);
        }
        return nameTextField;
    }

    /**
	 * @return Returns the nicknamePanel.
	 */
    protected JPanel getNicknamePanel() {
        if (nicknamePanel == null) {
            nicknamePanel = new JPanel();
            nicknamePanel.setLayout(new BorderLayout());
            TitledBorder titledBorder = new TitledBorder(Messages.getString("common.nickname"));
            nicknamePanel.setBorder(titledBorder);
            nicknamePanel.add(getNicknameTextField(), BorderLayout.CENTER);
        }
        return nicknamePanel;
    }

    /**
	 * @return Returns the nicknameTextField.
	 */
    protected JTextField getNicknameTextField() {
        if (nicknameTextField == null) {
            nicknameTextField = new JTextField();
            nicknameTextField.setText(user == null ? "" : user.getNickname());
        }
        return nicknameTextField;
    }

    /**
	 * @return Returns the passwordPanel.
	 */
    protected JPanel getPasswordPanel() {
        if (passwordPanel == null) {
            passwordPanel = new JPanel();
            passwordPanel.setLayout(new BorderLayout());
            TitledBorder titledBorder = new TitledBorder(Messages.getString("common.password"));
            passwordPanel.setBorder(titledBorder);
            passwordPanel.add(getPasswordField(), BorderLayout.CENTER);
        }
        return passwordPanel;
    }

    /**
	 * @return Returns the passwordField.
	 */
    protected JPasswordField getPasswordField() {
        if (passwordField == null) {
            passwordField = new JPasswordField();
            passwordField.setText(user == null ? "" : user.getPassword());
        }
        return passwordField;
    }

    /**
	 * @return Returns the retypedPasswordPanel.
	 */
    protected JPanel getRetypedPasswordPanel() {
        if (retypedPasswordPanel == null) {
            retypedPasswordPanel = new JPanel();
            retypedPasswordPanel.setLayout(new BorderLayout());
            TitledBorder titledBorder = new TitledBorder(Messages.getString("common.password.retype"));
            retypedPasswordPanel.setBorder(titledBorder);
            retypedPasswordPanel.add(getRetypedPasswordField(), BorderLayout.CENTER);
        }
        return retypedPasswordPanel;
    }

    /**
	 * @return Returns the retypedPasswordField.
	 */
    protected JPasswordField getRetypedPasswordField() {
        if (retypedPasswordField == null) {
            retypedPasswordField = new JPasswordField();
            retypedPasswordField.setText(user == null ? "" : user.getPassword());
        }
        return retypedPasswordField;
    }

    /**
	 * @return Returns the surnamePanel.
	 */
    protected JPanel getSurnamePanel() {
        if (surnamePanel == null) {
            surnamePanel = new JPanel();
            surnamePanel.setLayout(new BorderLayout());
            TitledBorder titledBorder = new TitledBorder(Messages.getString("common.surname"));
            surnamePanel.setBorder(titledBorder);
            surnamePanel.add(getSurnameTextField(), BorderLayout.CENTER);
        }
        return surnamePanel;
    }

    /**
	 * @return Returns the surnameTextField.
	 */
    protected JTextField getSurnameTextField() {
        if (surnameTextField == null) {
            surnameTextField = new JTextField();
            surnameTextField.setText(user == null ? "" : user.getSurname());
        }
        return surnameTextField;
    }

    /**
	 * @return Returns the contentPanel.
	 */
    protected JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new GridLayout(2, 1));
            contentPanel.add(getTopPanel());
            contentPanel.add(getBottomPanel());
        }
        return contentPanel;
    }

    /**
	 * @return Returns the birthdayPanel.
	 */
    protected JPanel getBirthdayPanel() {
        if (birthdayPanel == null) {
            birthdayPanel = new JPanel();
            TitledBorder titledBorder = new TitledBorder(Messages.getString("common.birthday"));
            birthdayPanel.setBorder(titledBorder);
            birthdayPanel.setLayout(new BorderLayout());
            birthdayPanel.add(getBirthdayDateChooser());
        }
        return birthdayPanel;
    }

    /**
	 * @return Returns the birthdayDateChooser.
	 */
    protected JDateChooser getBirthdayDateChooser() {
        if (birthdayDateChooser == null) {
            birthdayDateChooser = new JDateChooser();
            birthdayDateChooser.setDate(user == null ? new Date() : user.getBirthday());
        }
        return birthdayDateChooser;
    }

    /**
	 * @return Returns the imagePanel.
	 */
    protected JPanel getImagePanel() {
        if (imagePanel == null) {
            imagePanel = new ImagePanel(user == null ? "" : user.getImagePath());
            TitledBorder titledBorder = new TitledBorder(Messages.getString("common.photo"));
            imagePanel.setBorder(titledBorder);
        }
        return imagePanel;
    }

    /**
	 * @return Returns the buttonPanel.
	 */
    protected JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.setBorder(new EtchedBorder());
            buttonPanel.setLayout(new GridLayout(5, 1));
            buttonPanel.add(new JLabel(""));
            buttonPanel.add(getDocumentButton());
            buttonPanel.add(getAddressBookButton());
            buttonPanel.add(getExportVCardButton());
            buttonPanel.add(new JLabel(""));
        }
        return buttonPanel;
    }

    public void clearUserData() {
        getNameTextField().setText("");
        getSurnameTextField().setText("");
        getBirthdayDateChooser().setDate(new Date());
        getNicknameTextField().setText("");
        getPasswordField().setText("");
        getRetypedPasswordField().setText("");
    }

    /**
	 * @return Returns the documentButton.
	 */
    protected JButton getDocumentButton() {
        if (documentButton == null) {
            documentButton = new JButton();
            documentButton.setToolTipText(Messages.getString("panel.userpanel.userdocument"));
            documentButton.setIcon(new ImageIcon(this.getClass().getResource("/icon/16x16/apps/accessories-text-editor.png")));
            documentButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    logger.debug("actionPerformed documentButton");
                    setUserDocumentError(false);
                    ShowUserDocumentDialogCommand showUserDocumentDialogCommand = new ShowUserDocumentDialogCommand(documentDialog, user);
                    CommandExecutor.getInstance().executeCommand(showUserDocumentDialogCommand, false);
                }
            });
        }
        return documentButton;
    }

    public String getUserName() {
        return getNameTextField().getText();
    }

    public String getUserSurname() {
        return getSurnameTextField().getText();
    }

    public Date getUserBirthday() {
        return getBirthdayDateChooser().getDate();
    }

    public String getUserNickname() {
        return getNicknameTextField().getText();
    }

    public String getUserPassword() {
        return new String(getPasswordField().getPassword());
    }

    public String getRetypedPassword() {
        return new String(getRetypedPasswordField().getPassword());
    }

    public String getUserCredential() {
        return (String) getCredentialComboBox().getSelectedItem();
    }

    public String getUserGender() {
        return (String) getGenderComboBox().getSelectedItem();
    }

    public String getUserImagePath() {
        return ((ImagePanel) getImagePanel()).getImagePath();
    }

    public Document getUserDocument() {
        return documentDialog == null ? null : documentDialog.getDocument();
    }

    public void setUserNameError(boolean isError) {
        if (isError) ((TitledBorder) this.getNamePanel().getBorder()).setTitleColor(Color.RED); else ((TitledBorder) this.getNamePanel().getBorder()).setTitleColor(new TitledBorder("").getTitleColor());
        this.getNamePanel().updateUI();
    }

    public void setUserSurnameError(boolean isError) {
        if (isError) ((TitledBorder) this.getSurnamePanel().getBorder()).setTitleColor(Color.RED); else ((TitledBorder) this.getSurnamePanel().getBorder()).setTitleColor(new TitledBorder("").getTitleColor());
        this.getSurnamePanel().updateUI();
    }

    public void setUserBirthdayError(boolean isError) {
        if (isError) ((TitledBorder) this.getBirthdayPanel().getBorder()).setTitleColor(Color.RED); else ((TitledBorder) this.getBirthdayPanel().getBorder()).setTitleColor(new TitledBorder("").getTitleColor());
        this.getBirthdayPanel().updateUI();
    }

    public void setUserNicknameError(boolean isError) {
        if (isError) ((TitledBorder) this.getNicknamePanel().getBorder()).setTitleColor(Color.RED); else ((TitledBorder) this.getNicknamePanel().getBorder()).setTitleColor(new TitledBorder("").getTitleColor());
        this.getNicknamePanel().updateUI();
    }

    public void setUserPasswordError(boolean isError) {
        if (isError) {
            ((TitledBorder) this.getPasswordPanel().getBorder()).setTitleColor(Color.RED);
            ((TitledBorder) this.getRetypedPasswordPanel().getBorder()).setTitleColor(Color.RED);
        } else {
            ((TitledBorder) this.getPasswordPanel().getBorder()).setTitleColor(new TitledBorder("").getTitleColor());
            ((TitledBorder) this.getRetypedPasswordPanel().getBorder()).setTitleColor(new TitledBorder("").getTitleColor());
        }
        this.getPasswordPanel().updateUI();
        this.getRetypedPasswordPanel().updateUI();
    }

    public void setUserDocumentError(boolean isError) {
        if (isError) this.getDocumentButton().setBackground(Color.RED); else this.getDocumentButton().setBackground(new JButton().getBackground());
    }

    /**
	 * @return Returns the addressBookButton.
	 */
    protected JButton getAddressBookButton() {
        if (addressBookButton == null) {
            addressBookButton = new JButton();
            addressBookButton.setToolTipText(Messages.getString("common.addressbook"));
            addressBookButton.setIcon(new ImageIcon(this.getClass().getResource("/icon/16x16/actions/address-book-new.png")));
            addressBookButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    logger.debug("actionPerformed documentButton");
                    CommandExecutor.getInstance().executeCommand(new ShowUserAddressBookDialogCommand(user), false);
                }
            });
        }
        return addressBookButton;
    }

    /**
	 * @return Returns the exportVCardButton.
	 */
    protected JButton getExportVCardButton() {
        if (exportVCardButton == null) {
            exportVCardButton = new JButton();
            exportVCardButton.setToolTipText(Messages.getString("common.export.vcard"));
            exportVCardButton.setIcon(new ImageIcon(this.getClass().getResource("/icon/16x16/actions/contact-new.png")));
            exportVCardButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    logger.debug("actionPerformed exportVCardButton");
                    CommandExecutor.getInstance().executeCommand(new ExportUserAsVCardCommand(user), false);
                }
            });
        }
        return exportVCardButton;
    }

    /**
	 * @return Returns the bottomPanel.
	 */
    protected JPanel getBottomPanel() {
        if (bottomPanel == null) {
            bottomPanel = new JPanel();
            bottomPanel.setLayout(new BorderLayout());
            JPanel bottomLeftPanel = new JPanel();
            bottomLeftPanel.setLayout(new GridLayout(4, 1));
            bottomLeftPanel.add(getNicknamePanel());
            bottomLeftPanel.add(getPasswordPanel());
            bottomLeftPanel.add(getRetypedPasswordPanel());
            bottomLeftPanel.add(getCredentialPanel());
            JPanel bottomRightPanel = new JPanel();
            bottomRightPanel.setLayout(new BorderLayout());
            bottomRightPanel.add(getButtonPanel(), BorderLayout.CENTER);
            bottomPanel.add(bottomLeftPanel, BorderLayout.CENTER);
            bottomPanel.add(bottomRightPanel, BorderLayout.EAST);
        }
        return bottomPanel;
    }

    /**
	 * @return Returns the topPanel.
	 */
    protected JPanel getTopPanel() {
        if (topPanel == null) {
            topPanel = new JPanel();
            topPanel.setLayout(new GridLayout(1, 2));
            JPanel topLeftPanel = new JPanel();
            topLeftPanel.setLayout(new GridLayout(4, 1));
            topLeftPanel.add(getNamePanel(), null);
            topLeftPanel.add(getSurnamePanel(), null);
            topLeftPanel.add(getGenderPanel(), null);
            topLeftPanel.add(getBirthdayPanel(), null);
            JPanel topRightPanel = new JPanel();
            topRightPanel.setLayout(new BorderLayout());
            topRightPanel.add(getImagePanel(), BorderLayout.CENTER);
            JSplitPane splitPane = new JSplitPane();
            splitPane.setLeftComponent(topLeftPanel);
            splitPane.setRightComponent(topRightPanel);
            splitPane.setOneTouchExpandable(true);
            topPanel.add(splitPane);
        }
        return topPanel;
    }
}
