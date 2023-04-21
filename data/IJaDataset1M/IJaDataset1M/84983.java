package br.ufmg.saotome.arangiSecurity.controller;

import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;
import br.ufmg.saotome.arangi.commons.BasicException;
import br.ufmg.saotome.arangi.commons.NVLHelper;
import br.ufmg.saotome.arangi.controller.BasicButtonBarController;
import br.ufmg.saotome.arangi.controller.IApplicationContext;
import br.ufmg.saotome.arangi.controller.bean.Message;
import br.ufmg.saotome.arangiSecurity.controller.logic.UserReg;
import br.ufmg.saotome.arangiSecurity.dto.User;

@ManagedBean
@ApplicationScoped
public class UserController extends AppController<User, UserReg> {

    protected static Logger log = Logger.getLogger(UserController.class);

    @Override
    protected List<Message> validateDTO(IApplicationContext context, UserReg logic, User user) throws BasicException {
        String email = user.getEmail();
        if (!email.trim().equals("")) {
            String invalidChars = " /:,;()='�`^~��������������+&#$";
            for (int i = 0; i < invalidChars.length(); i++) {
                char badChar = invalidChars.charAt(i);
                if (email.indexOf(badChar, 0) > -1) {
                    logic.addMessage("msgInvalidEmail", new String[] { "" }, Message.TYPE_ERROR, "ArangiSecurityResources");
                }
            }
            int atPos = email.indexOf("@", 1);
            if (atPos == -1) {
                logic.addMessage("msgInvalidEmail", new String[] { "" }, Message.TYPE_ERROR, "ArangiSecurityResources");
            } else if (email.indexOf("@", atPos + 1) > -1) {
                logic.addMessage("msgInvalidEmail", new String[] { "" }, Message.TYPE_ERROR, "ArangiSecurityResources");
            } else if (email.indexOf(".", atPos) == -1) {
                logic.addMessage("msgInvalidEmail", new String[] { "" }, Message.TYPE_ERROR, "ArangiSecurityResources");
            } else if (email.charAt(atPos + 1) == '.') {
                logic.addMessage("msgInvalidEmail", new String[] { "" }, Message.TYPE_ERROR, "ArangiSecurityResources");
            } else if (email.endsWith(".")) {
                logic.addMessage("msgInvalidEmail", new String[] { "" }, Message.TYPE_ERROR, "ArangiSecurityResources");
            }
        }
        return super.validateDTO(context, logic, user);
    }

    @Override
    protected UserReg afterCancel(IApplicationContext context, UserReg logic) throws BasicException {
        logic.setForward("/UserSearch.xhtml?event=new&viewID=POPUP&faces-redirect=true");
        return logic;
    }

    @Override
    protected UserReg afterOpen(IApplicationContext context, UserReg logic) throws BasicException {
        logic.setForward("/UserSearch.xhtml?event=new&viewID=POPUP&faces-redirect=true");
        return logic;
    }

    @Override
    protected UserReg beforeSave(IApplicationContext context, UserReg logic, User user) throws BasicException {
        boolean dataReadOnly = verifyUserDataReadOnly(context);
        user.setDataReadOnly(dataReadOnly);
        return logic;
    }

    private boolean verifyUserDataReadOnly(IApplicationContext context) {
        boolean dataReadOnly = false;
        String aux = context.getInitParameter("arangiSecurityUserDataReadOnly");
        if (!NVLHelper.isEmpty(aux)) {
            try {
                dataReadOnly = new Boolean(aux);
            } catch (Exception e) {
                dataReadOnly = false;
            }
        }
        return dataReadOnly;
    }

    @Override
    protected void afterRenderButtons(IApplicationContext context, UserReg logic) {
        BasicButtonBarController bbc = logic.getButtonBarController();
        if (verifyUserDataReadOnly(context)) {
            bbc.setButtonNewVisible(false);
            bbc.setButtonDeleteVisible(false);
        }
    }

    @Override
    protected UserReg afterEdit(IApplicationContext context, UserReg logic) throws BasicException {
        logic.setUserDataReadOnly(verifyUserDataReadOnly(context));
        return logic;
    }
}
