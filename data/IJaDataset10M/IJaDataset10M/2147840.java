package br.ufmg.catustec.arangiSecurity.controller;

import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;
import br.ufmg.catustec.arangi.commons.BasicException;
import br.ufmg.catustec.arangi.controller.IApplicationContext;
import br.ufmg.catustec.arangi.controller.Message;
import br.ufmg.catustec.arangi.controller.bean.ControllerBean;
import br.ufmg.catustec.arangi.dto.BasicDTO;
import br.ufmg.catustec.arangiSecurity.dto.User;

@ManagedBean
@ApplicationScoped
public class UserController extends AppController {

    protected static Logger log = Logger.getLogger(UserController.class);

    @Override
    protected List<Message> validateDTO(IApplicationContext context, ControllerBean controllerBean, BasicDTO dto) throws BasicException {
        User user = (User) controllerBean.getDto();
        String email = user.getEmail();
        if (!email.trim().equals("")) {
            String invalidChars = " /:,;()='�`^~��������������+&#$";
            for (int i = 0; i < invalidChars.length(); i++) {
                char badChar = invalidChars.charAt(i);
                if (email.indexOf(badChar, 0) > -1) {
                    controllerBean.addMessage("msgInvalidEmail", new String[] { "" }, Message.TYPE_ERROR, "ArangiSecurityResources");
                }
            }
            int atPos = email.indexOf("@", 1);
            if (atPos == -1) {
                controllerBean.addMessage("msgInvalidEmail", new String[] { "" }, Message.TYPE_ERROR, "ArangiSecurityResources");
            } else if (email.indexOf("@", atPos + 1) > -1) {
                controllerBean.addMessage("msgInvalidEmail", new String[] { "" }, Message.TYPE_ERROR, "ArangiSecurityResources");
            } else if (email.indexOf(".", atPos) == -1) {
                controllerBean.addMessage("msgInvalidEmail", new String[] { "" }, Message.TYPE_ERROR, "ArangiSecurityResources");
            } else if (email.charAt(atPos + 1) == '.') {
                controllerBean.addMessage("msgInvalidEmail", new String[] { "" }, Message.TYPE_ERROR, "ArangiSecurityResources");
            } else if (email.endsWith(".")) {
                controllerBean.addMessage("msgInvalidEmail", new String[] { "" }, Message.TYPE_ERROR, "ArangiSecurityResources");
            }
        }
        return super.validateDTO(context, controllerBean, dto);
    }

    @Override
    protected ControllerBean afterCancel(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        controllerBean.setForward("/auth/UserSearch.xhtml?event=new&viewID=POPUP");
        return controllerBean;
    }

    @Override
    protected ControllerBean afterOpen(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        controllerBean.setForward("/auth/UserSearch.xhtml?event=new&viewID=POPUP");
        return controllerBean;
    }
}
