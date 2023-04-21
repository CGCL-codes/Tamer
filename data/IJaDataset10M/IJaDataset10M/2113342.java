package core;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public interface Protocols {

    void connect();

    void disconnect();

    /**
 * Odbiera przychodzace wiadomosci, i przesyla je dalej przez MsgEvent
 */
    public void odbior();

    public Typ getTyp();

    /**
 * @param to
 * @param s
 * Wysylanie wiadomosci
 */
    public void sendMessage(String to, String s);

    ArrayList<Kontakt> getKontakty();

    /**
 * @param st
 * Ustawia presence (stan)
 */
    public void setPresence(Stan st);

    /**
 * @param st
 * @param opis
 * Ustawia presence; stan i opis 
 */
    public void setPresence(Stan st, String opis);

    public void addMsgEventListener(MsgListener listener);

    public void removeMsgEventListener(MsgListener listener);

    abstract void addPropertyChangeListener(PropertyChangeListener l);

    public void removePropertyChangeListener(PropertyChangeListener l);

    /**
 * @return
 * Zwraca aktualny swoj stan
 */
    public Stan getActStan();

    /**
 * @param jid
 * @param name
 * Dodaje nowy kontakt (na serwerze)
 */
    void addKontakt(String jid, String name);

    /**
 * @param jid
 * @param name
 * Usuwa kontakt z serverwa
 */
    void removeKontakt(String jid, String name);

    /**
 * @param kontakty
 * Pobiera dla kontaktow
 */
    public void chPresence(ArrayList<Kontakt> kontakty);

    /**
 * @return
 * Zwraca true jezeli jest polaczenie
 */
    public boolean isConnected();
}
