package net.sourceforge.sdm.model;

import java.util.*;
import net.sourceforge.sdm.controller.*;
import net.sourceforge.sdm.ui.config.*;
import net.sourceforge.sdm.util.*;

public class PasswordGeneratorObservable extends Observable {

    CreatePwd cp;

    LoginEntry selectedEntry;

    CreatePwdDialogProperties props;

    public PasswordGeneratorObservable() {
        init();
    }

    public void generate() {
        super.setChanged();
        CreatePwdDialogProperties props = (CreatePwdDialogProperties) SDMController.getConfiguration(new CreatePwdDialogProperties());
        String pwd = cp.create("", props.getPasswordLength(), props.isUseSpecialChar(), props.isUseNumers(), props.isUseLetterLowercase(), props.isUseLetterUppercase(), props.isForceFirstChar());
        notifyObservers(pwd);
    }

    private void init() {
        cp = new CreatePwd();
    }

    public void setPwd(String pwd) {
        super.setChanged();
        notifyObservers(pwd);
    }
}
