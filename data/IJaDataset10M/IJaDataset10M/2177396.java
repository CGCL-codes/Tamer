package com.ikarkharkov.dictour.ui;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterAction extends Action {

    private static String publickey = "6LcaNAAAAAAAADzf0k9tjKwh0cdURTuI1Mvwe7JT";

    private static String privatekey = "6LcaNAAAAAAAAJ_mnqOI87W6981bO-0201YNeiMK";

    private static ReCaptcha instance = ReCaptchaFactory.newReCaptcha(publickey, privatekey, false);

    public static ReCaptcha getReCaptchaInstance() {
        return instance;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return (mapping.findForward("success"));
    }
}
