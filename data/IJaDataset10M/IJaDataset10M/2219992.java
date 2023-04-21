package org.alesv.common.webapp.session;

import javax.servlet.http.HttpSession;

/**
 * Clase que se encarga de gestionar los objetos que se guardan en la sesión. <br>
 * 
 * @see <a href="SessionObject.html">SessionObject</a>, <a
 *      href="SessionAttribute.html">SessionAttribute</a>
 */
public class SessionManager {

    /**
	 * Objeto que se guarda en sesión y que contiene al resto de objetos.
	 */
    private SessionObject sessionObject;

    /**
	 * Nombre del objeto que se guarda en sesión.
	 */
    private static String SESSION_OBJECT_NAME = "org.alesv.common.webapp.session.SessionObject";

    /**
	 * Constructor.
	 * 
	 * @param session
	 *            Sesión HTTP.
	 */
    public SessionManager(HttpSession session) {
        prepararObjeto(session);
    }

    /**
	 * Crea y devuelve un objeto gestor de la sesión.
	 * 
	 * @param session
	 *            Sessión HTTP.
	 * @return Objeto gestor de la sesión.
	 */
    public static SessionManager getSessionManager(HttpSession session) {
        return session != null ? new SessionManager(session) : null;
    }

    /**
	 * Inicializa el objeto que se guardará en la sesión.
	 * 
	 * @param session
	 *            Sesión HTTP.
	 */
    private void prepararObjeto(HttpSession session) {
        sessionObject = (SessionObject) session.getAttribute(SESSION_OBJECT_NAME);
        if (sessionObject == null) {
            sessionObject = new SessionObject();
            session.setAttribute(SESSION_OBJECT_NAME, sessionObject);
        }
    }

    /**
	 * Añade un objeto a la sesión.
	 * 
	 * @param name
	 *            Nombre del objeto.
	 * @param o
	 *            Objeto.
	 */
    public void setAttribute(String name, Object o) {
        sessionObject.setAttribute(name, o);
    }

    /**
	 * Obtiene un objeto de la sesión.
	 * 
	 * @param name
	 *            Nombre del objeto.
	 * @return Objeto.
	 */
    public Object getAttribute(String name) {
        return sessionObject.getAttribute(name);
    }

    /**
	 * Elimina un objeto de la sesión.
	 * 
	 * @param name
	 *            Nombre del objeto.
	 */
    public void removeAttribute(String name) {
        sessionObject.removeAttribute(name);
    }

    /**
	 * Limpia el objeto de sesión.
	 */
    public void clear(HttpSession session) {
        sessionObject = new SessionObject();
        session.setAttribute(SESSION_OBJECT_NAME, sessionObject);
    }
}
