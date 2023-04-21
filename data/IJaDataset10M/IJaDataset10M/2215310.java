package com.centropresse.struts.action;

import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.centropresse.dao.ClienteDAO;
import com.centropresse.dto.Cliente;
import com.centropresse.struts.form.RecuperaPasswordForm;
import com.centropresse.util.Constants;
import com.centropresse.util.LogFactory;

/** 
 * MyEclipse Struts
 * Creation date: 03-26-2007
 * 
 * XDoclet definition:
 * @struts.action path="/sendpassword" name="sendpasswordForm" input="/form/sendpassword.jsp" scope="request" validate="true"
 */
public class RecuperaPasswordAction extends Action {

    private static final String nameClass = "RegistrazioneAction";

    public static Logger logger = LogFactory.getWebLogger();

    private static String prefixLogClass = Constants.APPLICATION_CODE_BUSINESS + "." + nameClass + ".class";

    ActionForward forward;

    Connection conn;

    Cliente cliente = new Cliente();

    /** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        RecuperaPasswordForm recuperaPasswordForm = (RecuperaPasswordForm) form;
        String prefixLogMethod = prefixLogClass + "::execute - ";
        logger.debug(prefixLogMethod + "BEGIN");
        try {
            cliente.setUsername_cliente(recuperaPasswordForm.getUsername());
            cliente.setPostaelettronica_cliente(recuperaPasswordForm.getEmail());
            ClienteDAO clienteDao = new ClienteDAO(conn);
            String password = (String) clienteDao.getPassword(cliente);
            logger.debug(prefixLogMethod + " ottenuta identita' del cliente e salvata in sessione");
            return mapping.findForward("recuperaPasswordOK");
        } catch (Exception e) {
            logger.fatal(prefixLogMethod + " Exception ");
            e.printStackTrace();
            return mapping.findForward("error.general.exception");
        } finally {
            logger.debug(prefixLogMethod + "END");
        }
    }
}
