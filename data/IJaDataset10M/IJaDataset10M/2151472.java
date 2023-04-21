package org.openXpertya.wstore;

import java.util.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.apache.ecs.xhtml.*;
import org.openXpertya.util.*;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya
 */
public class LoginLinkTag extends TagSupport {

    /** Descripción de Campos */
    protected static CLogger log = CLogger.getCLogger(LoginLinkTag.class);

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws JspException
     */
    public int doStartTag() throws JspException {
        Properties ctx = JSPEnv.getCtx((HttpServletRequest) pageContext.getRequest());
        WebUser wu = getWebUser(ctx);
        if (wu == null) {
            pageContext.getSession().removeAttribute(WebUser.NAME);
        } else {
            pageContext.getSession().setAttribute(WebUser.NAME, wu);
        }
        String serverContext = ctx.getProperty(JSPEnv.CTX_SERVER_CONTEXT);
        HtmlCode html = null;
        if ((wu != null) && wu.isValid()) {
            html = getWelcomeLink(serverContext, wu);
        } else {
            html = getLoginLink(serverContext);
        }
        JspWriter out = pageContext.getOut();
        html.output(out);
        return (SKIP_BODY);
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws JspException
     */
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     *
     * @return
     */
    private WebUser getWebUser(Properties ctx) {
        String address = pageContext.getRequest().getRemoteAddr();
        WebUser wu = (WebUser) pageContext.getSession().getAttribute(WebUser.NAME);
        if (wu != null) {
            log.finest("(" + address + ") - SessionContext: " + wu);
        } else {
            wu = (WebUser) pageContext.getAttribute(WebUser.NAME);
            if (wu != null) {
                log.finest("(" + address + ") - Context: " + wu);
            }
        }
        if (wu != null) {
            return wu;
        }
        String cookieUser = JSPEnv.getCookieWebUser((HttpServletRequest) pageContext.getRequest());
        if ((cookieUser == null) || (cookieUser.trim().length() == 0)) {
            log.finer("(" + address + ") - no cookie");
        } else {
            wu = WebUser.get(ctx, cookieUser);
            log.finer("(" + address + ") - Cookie: " + wu);
        }
        if (wu != null) {
            return wu;
        }
        return null;
    }

    /**
     * Descripción de Método
     *
     *
     * @param serverContext
     *
     * @return
     */
    private HtmlCode getLoginLink(String serverContext) {
        HtmlCode retValue = new HtmlCode();
        input button = new input(input.TYPE_BUTTON, "Login", "Identificarse");
        button.setOnClick("window.top.location.replace('https://" + serverContext + "/loginServlet');");
        button.setClass("button");
        retValue.addElement(button);
        retValue.addElement(" ");
        return retValue;
    }

    /**
     * Descripción de Método
     *
     *
     * @param serverContext
     * @param wu
     *
     * @return
     */
    private HtmlCode getWelcomeLink(String serverContext, WebUser wu) {
        HtmlCode retValue = new HtmlCode();
        if (wu.isLoggedIn()) {
            String msg = "<strong>Bienvenido " + wu.getName() + "</strong>";
            div wrap = new div();
            wrap.setClass("loginmenu");
            div div = new div();
            div.addElement(msg);
            div.setClass("loginbutton");
            wrap.addElement(div);
            img img = new img("checkEmail.png");
            a a = new a("#");
            if (!wu.isEMailVerified()) {
                img.setAlt("Verificar correo electr&oacute;nico");
                img.setTitle("Verificar correo electr&oacute;nico");
                img.setAlign("absmiddle");
                img.setBorder(0);
                a.setOnClick("window.top.location.replace('emailVerify.jsp');");
                a.addElement(img);
                a.addElement("Verificar correo electr&oacute;nico");
                a.addAttribute("title", "Verificar correo electr&oacute;nico");
                div = new div();
                div.addElement(a);
                div.setClass("loginbutton");
                wrap.addElement(div);
            }
            img = new img("account.png");
            img.setAlt("Ver cuenta");
            img.setTitle("Ver cuenta");
            img.setAlign("absmiddle");
            img.setBorder(0);
            a = new a("#");
            a.setOnClick("window.top.location.replace('login.jsp');");
            a.addElement(img);
            a.addElement("Ver cuenta");
            a.addAttribute("title", "Ver cuenta");
            div = new div();
            div.addElement(a);
            div.setClass("loginbutton");
            wrap.addElement(div);
            img = new img("logout.png");
            img.setAlt("Cerrar sesi&oacute;n");
            img.setTitle("Cerrar sesi&oacute;n");
            img.setAlign("absmiddle");
            img.setBorder(0);
            a = new a("#");
            a.setOnClick("window.top.location.replace('loginServlet?mode=logout');");
            a.addElement(img);
            a.addElement("Cerrar sesi&oacute;n");
            a.addAttribute("title", "Cerrar sesi&oacute;n");
            div = new div();
            div.addElement(a);
            div.setClass("loginbutton");
            wrap.addElement(div);
            retValue.addElement(wrap);
        } else {
            input button = new input(input.TYPE_BUTTON, "Login", "Identificarse");
            button.setOnClick("window.top.location.replace('https://" + serverContext + "/login.jsp');");
            button.setClass("button");
            retValue.addElement(button);
        }
        retValue.addElement(" ");
        return retValue;
    }
}
