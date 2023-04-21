package it.webscience.uima.hpm.datatypes;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
    private List<Attachment> attachments;

    /**
     * Metadata associati all'evento.
     */
    private List<EventMetadata> eventMetadata;

    /** mappa contenente le proprietà dell'evento.
     * key: chiave della proprietà
     * value: valore della proprietà
     */
    private Hashtable<String, String> properties;

    /**
     * Costruttore.
     */
    public Event() {
        super();
        attachments = new ArrayList<Attachment>();
        eventMetadata = new ArrayList<EventMetadata>();
        properties = new Hashtable<String, String>();
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
    public final List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * @param in the attachments to set
     */
    public final void setAttachments(final List<Attachment> in) {
        this.attachments = in;
    }

    /**
     * @return the eventMetadata
     */
    public final List<EventMetadata> getEventMetadata() {
        return eventMetadata;
    }

    /**
     * @param in the eventMetadata to set
     */
    public final void setEventMetadata(final List<EventMetadata> in) {
        this.eventMetadata = in;
    }

    /**
     * aggiunge l'event metadata alla lista.
     * @param in oggetto EventMetadata
     */
    public final void addEventMetadata(final EventMetadata in) {
        eventMetadata.add(in);
        in.setEvent(this);
    }

    /**
     * @return the properties
     */
    public Hashtable<String, String> getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(Hashtable<String, String> properties) {
        this.properties = properties;
    }
}
