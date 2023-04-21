package org.ox.framework.business;

import java.util.HashMap;
import java.util.Map;
import org.ox.framework.security.*;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.ox.framework.web.responses.defaultResponse;
import org.ox.framework.web.singleton.oxf2Context;
import org.springframework.stereotype.Service;

/**
 *
 * @author admin
 */
@Service
public class userBusiness extends BusinessBase {

    private static Logger logger = Logger.getLogger(userBusiness.class);

    public void login() {
        UserInfo uinfo = null;
        ServletContext sc = getRequest().getSession().getServletContext();
        logger.debug("Obteniendo contexto");
        AuthProvider authProvider = (AuthProvider) oxf2Context.getService("authProvider");
        synchronized (authProvider) {
            logger.debug("Ejecutando autentificación");
            try {
                uinfo = authProvider.doAuthentification(getRequest());
            } catch (oxSecurityException cse) {
                if (!"BAD".equals(cse.getMessage())) {
                    throw new oxSecurityException("No se ha podido autentificar", "ERR-AUTH", cse);
                } else {
                    getDefautResponse().setMessage("Error: Usuario o password incorrectos.");
                    getDefautResponse().setRenderer("rendererStructuredData");
                    getDefautResponse().setContentType("text/xml");
                    Map map = new HashMap();
                    map.put("result", "LOGIN KO");
                    getDefautResponse().setData(map);
                    return;
                }
            }
        }
        getRequest().getSession().setAttribute(oxSecurityFilter.CON_USERINFO, uinfo);
        defaultResponse defaultResponse = getDefautResponse();
        Map map = new HashMap();
        map.put("result", "LOGIN OK");
        map.put("jsessionid", getRequest().getSession().getId());
        defaultResponse.setData(map);
        defaultResponse.setContentType("text/xml");
        defaultResponse.setMessage("Ha sido logado correctamente: " + uinfo.getNombre());
        defaultResponse.setRenderer("rendererStructuredData");
    }
}
