package it.webscience.kpeople.service.datatypes;

import java.util.Date;
import java.util.Hashtable;

/**
 * Tabella EVENT.
 * @author dellanna
 *
 */
public class Event {

    /**
     * Chiave primaria.
     * Identificativo univoco per un record della tabella EVENT
     */
    private int idEvent;

    /**
     * Valore testuale in cui memorizzare un nome associato all'evento.
     */
    private String name;

    /**
     * Valore testuale.
     * Identificativo univoco per identificare un evento in tutte le
     * componenti del sistema.
     */
    private String hpmEventId;

    /**
     * Valore testuale.
     * Identificativo univoco per identificare un sistema verticale o un
     * componente responsabile della generazione di un evento in tutte le
     * componenti del sistema.
     */
    private String hpmSystemId;

    /**
     * Attachments associati all'evento.
     */
    private Attachment[] attachments;

    /**
     * Metadata associati all'evento.
     */
    private EventMetadata[] eventMetadata;

    /** Keywords associate al processo. */
    private Keyword[] keywords;

    /** Timestamp riferito alla prima azione effettuata sul record. */
    private Date firstActionDate;

    /** Riferimento numerico all'identificativo dell'utente che effettua
     * l'ultima azione sul record. */
    private Date lastActionDate;

    /**
     * Utente creatore dell'evento.
     */
    private User user;

    /**
     * Costruttore.
     */
    public Event() {
        super();
    }

    /**
     * @return the idEvent
     */
    public final int getIdEvent() {
        return idEvent;
    }

    /**
     * @param in the idEvent to set
     */
    public final void setIdEvent(final int in) {
        this.idEvent = in;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param in the name to set
     */
    public final void setName(final String in) {
        this.name = in;
    }

    /**
     * @return the hpmEventId
     */
    public final String getHpmEventId() {
        return hpmEventId;
    }

    /**
     * @param in the hpmEventId to set
     */
    public final void setHpmEventId(final String in) {
        this.hpmEventId = in;
    }

    /**
     * @return the hpmSystemId
     */
    public final String getHpmSystemId() {
        return hpmSystemId;
    }

    /**
     * @param in the hpmSystemId to set
     */
    public final void setHpmSystemId(final String in) {
        this.hpmSystemId = in;
    }

    /**
     * @return the attachments
     */
    public final Attachment[] getAttachments() {
        return attachments;
    }

    /**
     * @param in the attachments to set
     */
    public final void setAttachments(final Attachment[] in) {
        this.attachments = in;
    }

    /**
     * @return the eventMetadata
     */
    public final EventMetadata[] getEventMetadata() {
        return eventMetadata;
    }

    /**
     * @param in the eventMetadata to set
     */
    public final void setEventMetadata(final EventMetadata[] in) {
        this.eventMetadata = in;
    }

    /**
     * @return the keywords
     */
    public final Keyword[] getKeywords() {
        return keywords;
    }

    /**
     * @param in the keywords to set
     */
    public final void setKeywords(final Keyword[] in) {
        this.keywords = in;
    }

    /**
     * @return the firstActionDate
     */
    public final Date getFirstActionDate() {
        return firstActionDate;
    }

    /**
     * @param in the firstActionDate to set
     */
    public final void setFirstActionDate(final Date in) {
        this.firstActionDate = in;
    }

    /**
     * @return the lastActionDate
     */
    public final Date getLastActionDate() {
        return lastActionDate;
    }

    /**
     * @param in the lastActionDate to set
     */
    public final void setLastActionDate(final Date in) {
        this.lastActionDate = in;
    }

    /**
     * @param in oggetto User da testare.
     */
    public final void setUser(final User in) {
        this.user = in;
    }

    /**
     * @return oggetto User creatore dell'evento.
     */
    public final User getUser() {
        return user;
    }
}
