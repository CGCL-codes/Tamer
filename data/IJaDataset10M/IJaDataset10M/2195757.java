package jm.action;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import jm.form.CadastrarUsuarioForm;
import javax.servlet.*;
import javax.servlet.http.*;

public class CadastrarUsuarioAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Logger log = Logger.getLogger(CadastrarUsuarioAction.class);
        CadastrarUsuarioForm cadastrarUsuarioForm = (CadastrarUsuarioForm) form;
        log.error("Teste de log4j mail");
        return mapping.findForward("telaCadastro");
    }
}
