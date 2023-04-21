package myapp.actionform;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class LoginForm extends ActionForm {

    private static Logger logger = Logger.getLogger(LoginForm.class);

    private String usuario;

    private String contrasena;

    public ActionErrors validate(final ActionMapping actionMapping, final HttpServletRequest httpServletRequest) {
        ActionErrors errors = new ActionErrors();
        logger.debug("user ---> " + this.getUsuario());
        if (usuario == null) {
            errors.add("usuario", new ActionMessage("error.login.usuarioInvalido"));
        }
        if (contrasena == null) {
            errors.add("contrasena", new ActionMessage("error.login.usuarioInvalido"));
        }
        return errors;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(final String contrasena) {
        this.contrasena = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(final String usuario) {
        this.usuario = usuario;
    }
}
