package br.com.mcampos.controller;

import br.com.mcampos.dto.user.UserDocumentDTO;
import br.com.mcampos.exception.ApplicationException;
import br.com.mcampos.util.business.LoginLocator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

public class ChangePasswordController extends BaseLoginOptionsController {

    protected Textbox identity;

    protected Textbox old_password;

    protected Textbox password;

    protected Textbox re_password;

    private Panel titleChangePassword;

    private Label changePasswdLabelMsg;

    private Label labelIdentification;

    private Label labelOldPassword;

    private Label labelNewPassword;

    private Label labelRepassword;

    protected static String loginCookieName = "LoginCookieName";

    protected LoginLocator locator;

    public ChangePasswordController() {
        super();
    }

    public ChangePasswordController(char c) {
        super(c);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        String csLogin = getCookie(loginCookieName);
        if (csLogin != null && csLogin.isEmpty() == false) {
            identity.setValue(csLogin);
            old_password.setFocus(true);
        } else {
            identity.setFocus(true);
        }
        setLabel(titleChangePassword);
        setLabel(changePasswdLabelMsg);
        setLabel(labelIdentification);
        setLabel(labelOldPassword);
        setLabel(labelNewPassword);
        setLabel(labelRepassword);
    }

    public void onClick$cmdSubmit() {
        String csIdentification;
        String csPassword, csRePassword, csOldPassword;
        csOldPassword = old_password.getValue();
        csPassword = password.getValue();
        csRePassword = re_password.getValue();
        if (csPassword.equals(csRePassword) == false) {
            showErrorMessage("As novas senhas informadas são diferentes");
            return;
        }
        if (validateCaptcha()) {
            csIdentification = identity.getValue();
            try {
                getLocator().changePassword(UserDocumentDTO.createUserDocumentEmail(csIdentification), csOldPassword, csPassword);
                Sessions.getCurrent().invalidate();
                gotoPage("/password_changed.zul");
            } catch (ApplicationException e) {
                showErrorMessage(e.getMessage());
            }
        }
    }
}
