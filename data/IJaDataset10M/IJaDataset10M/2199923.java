package org.ddth.txbb.member.mvc;

import javax.servlet.ServletException;
import org.ddth.panda.BaseLanguage;
import org.ddth.skinengine.freemarker.FMElement;
import org.ddth.txbb.base.TXBBApp;
import org.ddth.txbb.base.TXBBConstants;
import org.ddth.txbb.base.ui.FormRenderer;

public class MemberResetPwdModeller extends AbstractMemberModeller {

    /**
	 * Auto-generated serial version UID.
	 */
    private static final long serialVersionUID = 1094294261166224920L;

    private static final String MODEL_FORM = "form";

    private FormRenderer form;

    public MemberResetPwdModeller(TXBBApp app, String domain, String action) {
        super(app, domain, action);
    }

    @Override
    public void perform() throws ServletException {
        super.perform();
        BaseLanguage language = _APP.getLanguage();
        String title = _APP.getConfig().getSiteName();
        if (title == null) title = "";
        title += " - " + language.getText(TXBBConstants.APP_DOMAIN_COMMON, "member");
        pageRenderer.setTitle(title);
    }

    @Override
    protected void modelCenterColumn() throws ServletException {
        if (form == null) form = (FormRenderer) getAttribute(MemberRegisterController.ATTR_FORM_RENDERER);
        FMElement skinColumnCenter = pageRenderer.createColumnCenter();
        skinColumnCenter.assignSkin(MODEL_FORM, form);
    }
}
