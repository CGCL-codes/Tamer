package it.webscience.kpeople.be;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author depascalis
 */
public class CommunicationEvent extends Attachment {

    /**
     * Oggetto della mail.
     */
    private String object;

    /**
     * Body della mail.
     */
    private String body = null;

    /**
     * Lista di Business entity relativa agli utenti che sono in CC
     * alla mail.
     */
    private List<User> ccUser = new ArrayList<User>();

    /**
     * Lista di Business entity relativa agli utenti che sono in CCn.
     */
    private List<User> ccnUser = new ArrayList<User>();

    /**
     * Business entity relativa all'utente che ha spedito la mail.
     */
    private User userFrom;

    /**
     *  Business Entity relativa all'utente a cui � diretta la mail.
     */
    private List<User> toUser = new ArrayList<User>();

    /**
     * Data di creazione della mail.
     */
    private Date creationDate;

    /**
     * Costruttore della classe.
     */
    public CommunicationEvent() {
        super();
    }

    /**
     * @param in business entity relativa all'utente
     * receiver diretto della mail.
     */
    public final void setToUser(final List<User> in) {
        this.toUser = in;
    }

    /**
     * @return business entity relativa all'utente receiver della mail.
     */
    public final List<User> getToUser() {
        return toUser;
    }

    /**
     * @param in Lista di utenti cc receivers della mail
     */
    public final void setCcUser(final List<User> in) {
        this.ccUser = in;
    }

    /**
     * @return lista degli utenti cc receivers della mail.
     */
    public final List<User> getCcUser() {
        return ccUser;
    }

    /**
     * @return body della mail.
     */
    public final String getBody() {
        return body;
    }

    /**
     * @param in body della mail.
     */
    public final void setBody(final String in) {
        this.body = in;
    }

    /**
     * @return data di creazione della mail
     */
    public final Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param in data di creazione della mail.
     */
    public final void setCreationDate(final Date in) {
        this.creationDate = in;
    }

    /**
     * @param in utente che ha inviato la mail.
     */
    public final void setUserFrom(final User in) {
        this.userFrom = in;
    }

    /**
     * @return utente che ha inviato la mail.
     */
    public final User getUserFrom() {
        return userFrom;
    }

    /**
     * @param in lista degli utenti in CCn.
     */
    public final void setCcnUser(final List<User> in) {
        this.ccnUser = in;
    }

    /**
     * @return lista degli utenti in CCn.
     */
    public final List<User> getCcnUser() {
        return ccnUser;
    }

    /**
     * @param in oggetto della mail.
     */
    public final void setObject(final String in) {
        this.object = in;
    }

    /**
     * @return oggetto della mail.
     */
    public final String getObject() {
        return object;
    }
}
