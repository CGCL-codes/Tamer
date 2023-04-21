package chsec.gui.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import chsec.domain.User;
import chsec.service.AAService;

@Component("pwdChng")
public class ChngPassDlgCtrlImp implements AppShell, ChngPassDlgCtrl {

    private static final long serialVersionUID = 1L;

    private ChngPassDlg dlgWin;

    private AAService dataSvc;

    private User currUser;

    public ChngPassDlgCtrlImp() {
    }

    public void run(User aUser) {
        currUser = aUser;
        dlgWin.show();
    }

    public User getCurrentUser() {
        return currUser;
    }

    public void cancelNotify() {
        dlgWin.hide();
    }

    public void okNotify() {
        if (!dlgWin.getOldPwd().equals(currUser.getPwd())) {
            dlgWin.showErrorMsg("Invalid old password!");
            return;
        }
        String npwd1 = dlgWin.getNewPwd1();
        String npwd2 = dlgWin.getNewPwd2();
        if (!npwd1.equals(npwd2)) {
            dlgWin.showErrorMsg("Password entered second time did not match\nthe one you entered first time.");
        } else if (npwd1.length() < 6 || npwd1.length() > 20) {
            dlgWin.showErrorMsg("The password should be between\n" + "6 and 20 characters long!");
        } else if (isWeakPwd(npwd1)) {
            dlgWin.showErrorMsg("The password should have at least one\n" + "non letter character!");
        } else {
            currUser.setPwd(npwd1);
            currUser.setPwdChngReq(false);
            dataSvc.storeUser(currUser);
            dlgWin.hide();
        }
    }

    @Autowired
    public void setDlgWin(ChngPassDlg dlgWin) {
        this.dlgWin = dlgWin;
        if (dlgWin instanceof ChngPassDlgImp) ((ChngPassDlgImp) this.dlgWin).setController(this);
    }

    @Autowired
    public void setDataSvc(AAService dataSvc) {
        this.dataSvc = dataSvc;
    }

    private boolean isWeakPwd(String pwd) {
        boolean hasNonLetter = false;
        for (int i = 0; i < pwd.length() && !hasNonLetter; i++) {
            hasNonLetter = !Character.isLetter(pwd.charAt(i));
        }
        return hasNonLetter == false;
    }
}
