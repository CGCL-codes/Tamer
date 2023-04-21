package com.juanfrivaldes.cio2005.web;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 * @author root
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InscribirseForm extends ActionForm {

    /**
		 * @return Returns the email.
		 */
    public String getEmail() {
        return email;
    }

    /**
		 * @param email The email to set.
		 */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
		 * @return Returns the password.
		 */
    public String getPassword() {
        return password;
    }

    /**
		 * @param password The password to set.
		 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 3256723961709410102L;

    private static Log log = LogFactory.getLog(RegistroUsuarioForm.class);

    private String email;

    private String password;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        email = null;
        password = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (email == null || email.trim().equals("")) {
            errors.add("email", new ActionMessage("registroUsuario.email.problema"));
        } else if (password == null || password.trim().equals("") || password.trim().length() < 6) {
            errors.add("nif", new ActionMessage("registroUsuario.password.problema"));
        }
        return errors;
    }
}
