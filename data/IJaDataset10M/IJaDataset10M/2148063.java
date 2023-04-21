package net.sf.opentranquera.integration.notifier.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.opentranquera.integration.notifier.Event;
import net.sf.opentranquera.integration.notifier.Notifier;

/**
 * Esta clase mantiene la configuracion cargada a traves de un
 * {@link net.sf.opentranquera.integration.notifier.config.ConfigReader}. Es la clase principal de configuraci�n
 * y por la cual se tiene acceso a los datos de configuraci�n de los eventos y notificadores.
 * 
 * @author <a href="mailto:rdiegoc@gmail.com">Diego Campodonico</a><br>
 */
public class Config {

    /**
     * listado de eventos (eventName -> Event Object)
     */
    private Map events = new HashMap();

    /**
     * Common elements
     */
    private CommonConfig commons;

    /**
     * Add a new event
     */
    public void addEvent(Event event) {
        this.events.put(event.getName(), event);
    }

    /**
     * Devuelve los eventos configurados en forma de <code>java.util.Map</code>
     */
    public Map getEvents() {
        return this.events;
    }

    /**
     * @param common The common to set.
     */
    public void setCommons(CommonConfig commons) {
        this.commons = commons;
    }

    /**
     * @return Returns the commons.
     */
    public CommonConfig getCommons() {
        return commons;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Events:");
        for (Iterator iter = this.events.keySet().iterator(); iter.hasNext(); ) {
            String eventName = (String) iter.next();
            Event event = this.getEvent(eventName);
            sb.append("\n");
            sb.append(event.toString());
        }
        return sb.toString();
    }

    /**
     * Devuelve un Notifier de un evento
     * @param eventName
     * @param notifierName
     * @return
     */
    public Notifier getNotifier(String eventName, String notifierName) {
        Event event = this.getEvent(eventName);
        return event.getNotifier(notifierName);
    }

    /**
     * Busca un evento por su nombre
     * @param eventName
     * @return
     */
    public Event getEvent(String eventName) {
        return (Event) this.events.get(eventName);
    }

    /**
     * Busca una chain por su nombre
     */
    public FilterChain getFilterChain(String name) {
        return this.commons.getChain(name);
    }
}
