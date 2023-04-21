package com.datas.form.dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import com.datas.bean.model.system.SysUser;
import com.datas.component.AdvancedTextField;
import com.datas.component.PasswordField;
import com.datas.form.common.CommonDialog;
import com.datas.form.frame.main.MainFrame;
import com.datas.form.util.FormUtility;
import cz.autel.dmi.HIGConstraints;
import cz.autel.dmi.HIGLayout;

/**
 * 
 * @author kimi
 * 
 */
@SuppressWarnings("serial")
public class PasswordDialog extends CommonDialog implements ActionListener, WindowListener, KeyListener, FocusListener {

    private JLabel userNameLabel;

    private JLabel userPasswordLabel;

    private PasswordField userPasswordField;

    private AdvancedTextField userNameTextField;

    private JButton applyButton;

    private JButton exitButton;

    private HIGLayout higLayout;

    private HIGConstraints higConstraints;

    public PasswordDialog() {
        init();
    }

    @Override
    protected void init() {
        setLayout();
        setBehavior();
        setLookAndFeel();
    }

    @Override
    protected void setLayout() {
        createDialog();
        createComponents();
        int width[] = { 10, 100, 20, 100, 10 };
        int height[] = { 10, 20, 10, 20, 10, 20, 10, 20, 10 };
        higLayout = new HIGLayout(width, height);
        higConstraints = new HIGConstraints();
        this.setLayout(higLayout);
        this.add(userNameLabel, higConstraints.rcwh(2, 2, 1, 1));
        this.add(userNameTextField, higConstraints.rcwh(2, 4, 1, 1));
        this.add(userPasswordLabel, higConstraints.rcwh(4, 2, 1, 1));
        this.add(userPasswordField, higConstraints.rcwh(4, 4, 1, 1));
        this.add(applyButton, higConstraints.rcwh(6, 2, 1, 1));
        this.add(exitButton, higConstraints.rcwh(6, 4, 1, 1));
        setDefaultLocation();
        userPasswordField.requestFocus();
    }

    @Override
    protected void setBehavior() {
        this.addWindowListener(this);
        applyButton.addActionListener(this);
        exitButton.addActionListener(this);
        userNameTextField.addActionListener(this);
        userPasswordField.addActionListener(this);
        userNameTextField.addKeyListener(this);
        userPasswordField.addKeyListener(this);
        userNameTextField.addFocusListener(this);
        userPasswordField.addFocusListener(this);
    }

    @Override
    protected void setLookAndFeel() {
        userNameLabel.setHorizontalAlignment(JLabel.RIGHT);
        userPasswordLabel.setHorizontalAlignment(JLabel.RIGHT);
        userNameLabel.setFont(getServiceContainer().getLookAndFeelService().getFontLabelSmallBold());
        userNameTextField.setFont(getServiceContainer().getLookAndFeelService().getFontTextField());
        userPasswordLabel.setFont(getServiceContainer().getLookAndFeelService().getFontLabelSmallBold());
        userPasswordField.setFont(getServiceContainer().getLookAndFeelService().getFontTextField());
        applyButton.setFont(getServiceContainer().getLookAndFeelService().getFontButton());
        exitButton.setFont(getServiceContainer().getLookAndFeelService().getFontButton());
        userNameTextField.setBackground(getServiceContainer().getLookAndFeelService().getColorTextFieldNotNull());
        userPasswordField.setBackground(getServiceContainer().getLookAndFeelService().getColorTextFieldNotNull());
        userNameTextField.requestFocus();
    }

    private void createDialog() {
        this.setModal(true);
        this.setResizable(false);
        this.setPreferredSize(new Dimension(250, 130));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void createComponents() {
        userNameLabel = new JLabel(getServiceContainer().getLocalizationService().getTranslation(getClass(), "USERNAME"));
        userPasswordLabel = new JLabel(getServiceContainer().getLocalizationService().getTranslation(getClass(), "PASSWORD"));
        userNameTextField = new AdvancedTextField(0, -1);
        userPasswordField = new PasswordField();
        applyButton = new JButton(getServiceContainer().getLocalizationService().getTranslation("BUTTON_APPLY_INSERT"));
        exitButton = new JButton(getServiceContainer().getLocalizationService().getTranslation("BUTTON_CANCEL"));
    }

    private void apply() {
        SysUser sysUser = new SysUser();
        sysUser.setIdUser(userNameTextField.getText());
        sysUser = getServiceContainer().getSystemService().selectObject(sysUser);
        Object[] options = { getServiceContainer().getLocalizationService().getTranslation("ANSWER_YES"), getServiceContainer().getLocalizationService().getTranslation("ANSWER_NO") };
        if (sysUser == null) {
            int response = JOptionPane.showOptionDialog(null, getServiceContainer().getLocalizationService().getTranslation(getClass(), "USER_NOT_EXISTS"), getServiceContainer().getLocalizationService().getTranslation(getClass(), "LOGIN_FAILED"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (response == JOptionPane.YES_OPTION) {
                clearFields(true, true);
            } else {
                exit();
            }
        } else {
            if (sysUser.getStatus()) {
                if (!userPasswordField.getEncryptedPassword().equalsIgnoreCase(sysUser.getPassword())) {
                    int response = JOptionPane.showOptionDialog(null, getServiceContainer().getLocalizationService().getTranslation(getClass(), "WRONG_PASSWORD"), getServiceContainer().getLocalizationService().getTranslation(getClass(), "LOGIN_FAILED"), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (response == JOptionPane.YES_OPTION) {
                        clearFields(false, true);
                    } else {
                        exit();
                    }
                } else {
                    MainFrame.getInstance().getLoginData().setSysUser(sysUser);
                    dispose();
                }
            } else {
                int response = JOptionPane.showOptionDialog(null, getServiceContainer().getLocalizationService().getTranslation(getClass(), "USER_NOT_ACTIVE"), getServiceContainer().getLocalizationService().getTranslation(getClass(), "LOGIN_FAILED"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (response == JOptionPane.YES_OPTION) {
                    clearFields(true, true);
                } else {
                    exit();
                }
            }
        }
    }

    private void exit() {
        System.exit(0);
    }

    private void clearFields(boolean username, boolean password) {
        if (username) {
            userNameTextField.clear();
            userNameTextField.setBackground(getServiceContainer().getLookAndFeelService().getColorTextFieldNotNull());
            userNameTextField.requestFocus();
        }
        if (password) {
            userPasswordField.setText(null);
            userPasswordField.setBackground(getServiceContainer().getLookAndFeelService().getColorTextFieldNotNull());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(applyButton)) apply(); else if (e.getSource().equals(exitButton)) exit(); else if (e.getSource().equals(userNameTextField)) apply(); else if (e.getSource().equals(userPasswordField)) apply();
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        FormUtility.validateColor(userNameTextField, e);
        FormUtility.validateColor(userPasswordField, e);
    }

    public void keyTyped(KeyEvent e) {
        if (e.getSource().equals(userNameTextField)) {
            e.setKeyChar(Character.toLowerCase(e.getKeyChar()));
        }
    }

    @SuppressWarnings("deprecation")
    public void focusGained(FocusEvent e) {
        if (e.getSource().equals(userNameTextField)) {
            userNameTextField.setBorder(getServiceContainer().getLookAndFeelService().getSelectedFieldBorder());
        } else if (e.getSource().equals(userPasswordField)) {
            userPasswordField.setBorder(getServiceContainer().getLookAndFeelService().getSelectedFieldBorder());
            if (userPasswordField.getText().length() != 0) {
                userPasswordField.setSelectionStart(0);
                userPasswordField.setSelectionEnd(userPasswordField.getText().length());
            }
        }
    }

    public void focusLost(FocusEvent e) {
        if (e.getSource().equals(userNameTextField)) {
            userNameTextField.setBorder(getServiceContainer().getLookAndFeelService().getDefaultTextFieldBorder());
        } else if (e.getSource().equals(userPasswordField)) {
            userPasswordField.setBorder(getServiceContainer().getLookAndFeelService().getDefaultTextFieldBorder());
        }
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        exit();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}
