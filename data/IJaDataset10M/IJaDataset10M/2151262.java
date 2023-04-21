package net.taylor.jsf;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Logs the lifecycle of a JSF.
 * 
 * @author jgilbert
 * @version $Id: PhaseLogger.java,v 1.2 2006/10/26 20:52:37 jgilbert01 Exp $
 * 
 */
public class PhaseLogger implements PhaseListener {

    private static final long serialVersionUID = 1L;

    private static final Log logger = LogFactory.getLog(PhaseLogger.class);

    public void afterPhase(PhaseEvent e) {
        logger.info("After: " + toString(e));
        logger.info("CurrentInstance: " + toString(FacesContext.getCurrentInstance()));
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        logger.info("UserPrincipal: " + ec.getUserPrincipal());
        printMaps(e);
    }

    public void beforePhase(PhaseEvent e) {
        logger.info("Before: " + toString(e));
        logger.info("CurrentInstance: " + toString(FacesContext.getCurrentInstance()));
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        logger.info("UserPrincipal: " + ec.getUserPrincipal());
        printMaps(e);
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    private String toString(Object o) {
        return ToStringBuilder.reflectionToString(o, ToStringStyle.MULTI_LINE_STYLE);
    }

    private void printMaps(PhaseEvent e) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        Map map = null;
        map = ec.getApplicationMap();
        printMap(map, "ApplicationMap: ");
        map = ec.getInitParameterMap();
        printMap(map, "InitParameterMap: ");
        map = ec.getSessionMap();
        printMap(map, "SessionMap: ");
        map = ec.getRequestMap();
        printMap(map, "RequestMap: ");
        map = ec.getRequestCookieMap();
        printMap(map, "RequestCookieMap: ");
        map = ec.getRequestHeaderMap();
        printMap(map, "RequestHeaderMap: ");
        map = ec.getRequestHeaderValuesMap();
        printMap(map, "RequestHeaderValuesMap: ");
        map = ec.getRequestParameterMap();
        printMap(map, "RequestParameterMap: ");
        map = ec.getRequestParameterValuesMap();
        printMap(map, "RequestParameterValuesMap: ");
    }

    private void printMap(Map map, String prefix) {
        Iterator i = map.keySet().iterator();
        while (i.hasNext()) {
            String name = (String) i.next();
            logger.info(prefix + name + " : " + map.get(name) + " : " + ((map.get(name) instanceof Serializable) ? "Serializable" : "Non-Serializable"));
        }
    }
}
