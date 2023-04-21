package hx.front.view.login;

import hx.engine.IPersonalityEngine;
import hx.engine.IUserEngine;
import hx.model.dto.Personality;
import hx.model.dto.User;
import hx.model.util.HXKeys;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Pantalla de acceso a la aplicación, el usuario deberá teclear su nombre y
 * contraseña para poder avanzar. También aquí se muestra el estado del turno
 * indicando las casas que han terminado y las que no lo han hecho así como los
 * mensajes de los príncipes de las distintas casas. Habrá un menú que de acceso
 * a otras pantallas de información.
 * 
 * @author kineas
 * 
 */
public class Login extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 1L;

    private String usuario = null;

    private String clave = null;

    private Map<String, Object> sesion;

    private IUserEngine userEngine;

    private IPersonalityEngine personalityEngine;

    public String execute() throws Exception {
        return "login";
    }

    /**
	 * De momento no validamos, pero la lógica (a falta del encriptado) está hecha.
	 * @return
	 * @throws Exception
	 */
    public String validar() throws Exception {
        User user = userEngine.getById(getUsuario());
        if (user != null && getClave().equals(user.getPass())) {
            if (user.getPersonality() != null) {
                Personality personality = personalityEngine.getById(user.getPersonality().getId());
                sesion.put(HXKeys.LOGGED_USER, personality.getName());
                sesion.put(HXKeys.LOGGED_HOUSE, personality.getHouse());
                return "loginOk";
            } else {
                return "notAssignedYet";
            }
        }
        return "loginOk";
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getClave() {
        return clave;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public IUserEngine getUserEngine() {
        return userEngine;
    }

    public void setUserEngine(IUserEngine userEngine) {
        this.userEngine = userEngine;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.sesion = session;
    }

    public IPersonalityEngine getPersonalityEngine() {
        return personalityEngine;
    }

    public void setPersonalityEngine(IPersonalityEngine personalityEngine) {
        this.personalityEngine = personalityEngine;
    }
}
