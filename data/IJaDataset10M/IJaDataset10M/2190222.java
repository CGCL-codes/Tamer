package br.com.wepa.webapps.orca.controle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import br.com.wepa.webapps.orca.controle.actions.login.LoginAction;
import br.com.wepa.webapps.orca.controle.actions.login.UsuarioSessao;
import br.com.wepa.webapps.orca.controle.exception.SessaoExpiradaException;
import br.com.wepa.webapps.orca.controle.filters.SegurancaFilter;
import br.com.wepa.webapps.security.WebControl;
import br.com.wepa.webapps.security.keys.AuthenticationKey;
import br.com.wepa.webapps.security.keys.PermissionKey;

public class ControleWeb implements ConstantesControle {

    private static WebControl webControl = new WebControl();

    private static SegurancaFilter seguranca;

    public static void setSeguranca(SegurancaFilter filter) {
        seguranca = filter;
    }

    public static void doAuthentication(HttpServletRequest request, AuthenticationKey key) {
        seguranca.doAuthentication(request, key);
    }

    /**
	 * Monta PermissionKey para controlar as struts dispatch actions
	 * <action>.do?method=<method>
	 * 
	 * @param path
	 * @param method
	 * @return
	 */
    public static PermissionKey criaActionPermissionKey(String path, String method) {
        return new PermissionKey(ControleWeb.mountActionCall(path, method, false));
    }

    /**
	 * Monta chamada a uma a��o com este path e este m�todo. <action>.do?method=<method>
	 * @param path
	 * @param method
	 * @param se true, retorna o caminho com contexto: <context><action>.do?method=<method>
	 * @return
	 */
    public static String mountActionCall(String path, String method, boolean contextRelative) {
        String actionCall = "";
        if (path == null) return null; else {
            actionCall += path;
            if (method != null) {
                actionCall += "?" + PARAMETER + "=" + method;
            }
        }
        return contextRelative ? getCurrentRequest().getContextPath() + actionCall : actionCall;
    }

    /**
	 * Monta uma string que representa a a��o invocada por este request. 
	 * @param request
	 * @return  <action>.do?method=<method>
	 */
    public static String mountActionCall(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getParameter(PARAMETER);
        return mountActionCall(path, method, false);
    }

    public static boolean isActionCall(String urlPath) {
        return urlPath.indexOf(ACTIONSUFIX) != -1;
    }

    public static boolean isJspCall(String urlPath) {
        return urlPath.indexOf(ACTIONSUFIX) != -1;
    }

    public static UsuarioSessao getUsuarioSessaoCorrente() throws Exception {
        return getUsuarioSessao(getCurrentRequest(), getCurrentResponse());
    }

    public static HttpSession getSessaoCorrente() throws Exception {
        return getCurrentRequest().getSession();
    }

    public static UsuarioSessao getUsuarioSessao(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UsuarioSessao user = (UsuarioSessao) session.getAttribute(LoginAction.USUARIOWEB);
        if (user == null) {
            throw new SessaoExpiradaException();
        }
        return user;
    }

    /**
	 * Returns an ActionForward with the path specified to call an action
	 * 
	 * @param actionCall
	 * @return
	 */
    public static ActionForward newActionForward(String actionCall) {
        return new ActionForward(actionCall);
    }

    public static void setCurrents(HttpServletRequest request, HttpServletResponse response) {
        webControl.setCurrents(request, response);
    }

    public static HttpServletRequest getCurrentRequest() {
        return webControl.getCurrentRequest();
    }

    public static HttpServletResponse getCurrentResponse() {
        return webControl.getCurrentResponse();
    }

    /**
	 * Sets the attribute in the context specified
	 * @param attributeName
	 * @param value
	 * @param request
	 * @param scope
	 */
    public static void setInScope(String attributeName, Object value, HttpServletRequest request, String scope) {
        WebControl.setInScope(attributeName, value, request, scope);
    }

    /**
	 * Gets the attribute in the context specified, null otherwise
	 * @param attributeName
	 * @param request
	 * @param scope
	 * @return
	 */
    public static Object getInScope(String attributeName, HttpServletRequest request, String scope) {
        return WebControl.getInScope(attributeName, request, scope);
    }
}
