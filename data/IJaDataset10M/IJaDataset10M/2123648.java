package it.hotel.system;

import it.hotel.model.abstrakt.manager.AbstractService;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Classe di utilità.
 * @version 1.0
 * @author E.Santoboni
 */
public class HotelWebApplicationUtils {

    /**
	 * Restituisce un servizio di sistema.
	 * @param serviceName Il nome del servizio richiesto.
	 * @param request La request.
	 * @return Il servizio richiesto.
	 */
    public static AbstractService getService(String serviceName, HttpServletRequest request) {
        WebApplicationContext wac = getWebApplicationContext(request);
        return getService(serviceName, wac);
    }

    /**
	 * Restituisce un servizio di sistema.
	 * Il seguente metodo Ã¨ in uso ai tag jsp del sistema.
	 * @param serviceName Il nome del servizio richiesto.
	 * @param pageContext Il Contesto di pagina,
	 * @return Il servizio richiesto.
	 */
    public static AbstractService getService(String serviceName, PageContext pageContext) {
        WebApplicationContext wac = getWebApplicationContext(pageContext.getServletContext());
        return getService(serviceName, wac);
    }

    /**
	 * Restituisce il WebApplicationContext del sistema.
	 * @param request La request.
	 * @return Il WebApplicationContext del sistema.
	 */
    public static WebApplicationContext getWebApplicationContext(HttpServletRequest request) {
        ServletContext svCtx = request.getSession().getServletContext();
        WebApplicationContext wac = getWebApplicationContext(svCtx);
        return wac;
    }

    private static WebApplicationContext getWebApplicationContext(ServletContext svCtx) {
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(svCtx);
        return wac;
    }

    private static AbstractService getService(String serviceName, WebApplicationContext wac) {
        return (AbstractService) wac.getBean(serviceName);
    }

    /**
	 * Esegue il refresh del sistema.
	 * @param request La request.
	 * @throws Throwable In caso di errori in fase di aggiornamento del sistema.
	 */
    public static void executeSystemRefresh(HttpServletRequest request) throws Throwable {
        WebApplicationContext wac = getWebApplicationContext(request);
        String[] defNames = wac.getBeanDefinitionNames();
        for (int i = 0; i < defNames.length; i++) {
            Object bean = null;
            try {
                bean = wac.getBean(defNames[i]);
            } catch (Throwable t) {
                SystemUtils.logThrowable(t, HotelWebApplicationUtils.class, "executeSystemRefresh");
                bean = null;
            }
            if (bean != null && bean instanceof AbstractService) {
                ((AbstractService) bean).refresh();
            }
        }
    }
}
