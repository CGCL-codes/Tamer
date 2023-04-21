package clonefinder;

import java.util.EventObject;

/**
 * Eventi generati dalla o per la piattaforma, come ad esempio riconnessione 
 * alla rete JXTA
 * @author origama
 */
public class JXTAPlatformEvent extends EventObject {

    public enum platformEvents {

        CONNECT, RECONNECT, DISCONNECT, FINDPEERS, FINDFILES
    }

    ;

    platformEvents event;

    /**
     * Il costruttore dell'evento di Piattaforma
     * @param source ID dell'oggetto che ha lanciato l'evento
     * @param ev L'evento da segnalare
     */
    public JXTAPlatformEvent(Object source, platformEvents ev) {
        super(source);
        event = ev;
    }

    /**
     * Ritorna l'evento segnalato
     * @return event evento da segnalare
     */
    public platformEvents getEvent() {
        return event;
    }

    ;
}
