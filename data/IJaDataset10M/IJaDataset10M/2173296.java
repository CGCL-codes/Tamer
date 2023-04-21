package main.client.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

class Userlist extends Observable {

    private Map<String, String> userlist = new HashMap<String, String>();

    /**
     * Setzt die den Status auf changed und benachritigt
     * alle Observer
     */
    protected void updated() {
        setChanged();
        notifyObservers(asArray());
    }

    /**
     * Fuegt einen user und host in die userlist ein
     *
     * @param host
     * @param user
     */
    protected void addUser(String host, String user) {
        userlist.put(host, user);
    }

    /**
     * Loescht alle Eintraege aus der Map
     */
    protected void clear() {
        userlist.clear();
    }

    /**
     * Gibt die userlist als 2-dimensionales
     * String-Array zurueck
     *
     * @return userlistArray
     */
    protected synchronized String[][] asArray() {
        String[][] ul = new String[userlist.size()][2];
        int i = 0;
        for (Entry<String, String> e : userlist.entrySet()) {
            ul[i][0] = e.getKey();
            ul[i][1] = e.getValue();
            i++;
        }
        return ul;
    }
}
