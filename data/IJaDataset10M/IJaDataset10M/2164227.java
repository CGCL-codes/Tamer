package net.sourceforge.jcv.controller.members.lostpass;

import com.salmonllc.html.events.PageEvent;
import com.salmonllc.html.events.SubmitEvent;
import net.sourceforge.jcv.controller.members.MembersBaseController;
import net.sourceforge.jcv.model.UserModel;
import net.sourceforge.jcv.services.EmailService;
import net.sourceforge.jcv.util.PropsManager;

public class LostPassController extends MembersBaseController {

    public com.salmonllc.html.HtmlSubmitButton _cmdResetPassword;

    public com.salmonllc.jsp.JspForm _frmLostPass;

    public com.salmonllc.html.HtmlTextEdit _txtEmail;

    public com.salmonllc.jsp.JspDisplayBox _dBoxLostPass;

    public net.sourceforge.jcv.model.ResetPasswordModel _dsResetPassword;

    public void memberInitialize() {
        addPageListener(this);
        _cmdResetPassword.addSubmitListener(this);
        _cmdResetPassword.setAccessKey(PropsManager.getLangValue(PROP_BUTTON_RESET_PASS_ACCESSKEY, this));
        _frmLostPass.addSubmitListener(this);
        _cmdResetPassword.addSubmitListener(this);
        _frmLostPass.addSubmitListener(this);
        _dBoxLostPass.setHeadingCaption(PropsManager.getLangValue(PROP_HEADING_CAPTION_LOST_PASS, this));
    }

    public void memberPageRequested(PageEvent p) {
        if (!isReferredByCurrentPage() || _dsResetPassword.getRowCount() == 0) {
            _txtEmail.setFocus();
            _dsResetPassword.reset();
            _dsResetPassword.insertRow();
        }
    }

    public boolean memberSubmitPerformed(SubmitEvent e) throws Exception {
        if (e.getSource() == _cmdResetPassword || e.getSource() == _frmLostPass) {
            resetPassword();
            return false;
        }
        return true;
    }

    /**
     * Called after the reset password button of the lost pass display box has been pressed.
     *
     * @throws Exception
     */
    private void resetPassword() throws Exception {
        UserModel dsUser = new UserModel(getApplicationName());
        dsUser.retrieveByEmailID(_dsResetPassword.getEmail());
        if (dsUser.getRowCount() > 0) {
            String password = dsUser.resetPassword();
            boolean emailSent = EmailService.sendLostPassEmail(password, dsUser.getUsrUserid(), dsUser.getPersonEmail(), getApplicationName(), getLanguagePreferences());
            if (emailSent) {
                writeScript("alert(\"" + PropsManager.getLangValue("EmailSent.Feedback", this) + "\");" + "document.location.replace('" + PAGE_LOGIN + "?" + REQUEST_USER_ID + "=" + dsUser.getUsrUserid() + "');");
            } else {
                writeScript("alert(\"Email couldn't be send\");");
                _txtEmail.setFocus();
            }
        } else {
            writeScript("alert(\"Email couldn't be send\");");
            _txtEmail.setFocus();
        }
    }
}
