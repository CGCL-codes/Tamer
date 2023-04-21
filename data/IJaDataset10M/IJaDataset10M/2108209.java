package core.data_tier.entities;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Třída reprezentující účastníka volební akce. Má několik povinným a nepovinných
 * parametrů. Každý účastník má jméno, email, číslo OP, presenci vázanou k dané akci
 * @author Lahvi
 */
public class Participant {

    private String firstName;

    private String lastName;

    private String login;

    private String passwd;

    private String email;

    private long cardID;

    private long id;

    private Additional addParams;

    private Map<Long, Presence> presence;

    private boolean completeReg;

    /**
     * Konstruktor vytvářejecí účatníka se všemi povinnými i nepovinnými parametry.
     * @param firstName Křestní jméno účastníka. Povinné.
     * @param lastName Příjmení účastníka. Povinné.
     * @param login Přihlašovací jméno účastníka. Nepovinné.
     * @param passwd Heslo účastníka. Nepovinné.
     * @param email Email účastníka. Povinné.
     * @param cardID číslo OP účastníka. Povinné.
     * @param id jednoznačné ID účastníka. Povinné.
     * @param addParams Objekt s dodatečnými údaji o účastníkovi. Nepovinné.
     */
    public Participant(String firstName, String lastName, String email, long cardID, long id, Additional addParams, long actionID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cardID = cardID;
        this.id = id;
        this.addParams = addParams;
        presence = new TreeMap<Long, Presence>();
        Presence p = new Presence(actionID);
        presence.put(actionID, p);
        if (login != null && passwd != null) completeReg = true; else completeReg = false;
    }

    public Participant(String firstName, String lastName, String email, long cardID, long id, Additional addParams, Collection<Long> actionIDs) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cardID = cardID;
        this.id = id;
        this.addParams = addParams;
        presence = new TreeMap<Long, Presence>();
        for (Long actionID : actionIDs) {
            Presence p = new Presence(actionID);
            presence.put(actionID, p);
        }
        if (login != null && passwd != null) completeReg = true; else completeReg = false;
    }

    public Additional getAddParams() {
        return addParams;
    }

    public void setAddParams(Additional addParams) {
        this.addParams = addParams;
    }

    public long getCardID() {
        return cardID;
    }

    public void setCardID(long cardID) {
        this.cardID = cardID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCompleteReg() {
        return completeReg;
    }

    public void setCompleteReg() {
        this.completeReg = true;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return passwd;
    }

    public void setPassword(String passwd) {
        this.passwd = passwd;
    }

    public Presence getPresence(long id) {
        return presence.get(id);
    }

    public Collection<Long> getActionIDs() {
        return presence.keySet();
    }

    public void addAction(long id) {
        Presence pres = new Presence(id);
        presence.put(id, pres);
    }

    public void removeAction(long id) {
        presence.remove(id);
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Jméno: " + firstName + " " + lastName + (completeReg ? ", Login: " + login : ", Nezaregistrovaný");
    }
}
