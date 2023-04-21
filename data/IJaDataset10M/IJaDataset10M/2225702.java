package usercontrol;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.servlet.http.HttpSession;

public class Attendants extends Observable {

    private Vector<Client> attendants;

    public Attendants() {
        if (attendants == null) {
            attendants = new Vector<Client>();
        }
    }

    public synchronized void connected(Client client) {
        this.addObserver((Observer) client);
        attendants.add(client);
        super.setChanged();
        super.notifyObservers(this);
    }

    public synchronized void expired(Client client) {
        this.deleteObserver((Observer) client);
        attendants.remove(client);
        super.setChanged();
        super.notifyObservers(this);
    }

    public synchronized boolean attendant(HttpSession session) {
        for (Iterator<Client> it = attendants.iterator(); it.hasNext(); ) {
            Client elem = it.next();
            if (elem.getSession().getId().equals(session.getId())) {
                return true;
            }
        }
        return false;
    }

    public synchronized Client get(HttpSession session) {
        for (Iterator<Client> it = attendants.iterator(); it.hasNext(); ) {
            Client elem = it.next();
            if (elem.getSession().getId().equals(session.getId())) {
                return elem;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String tmp = "";
        for (Iterator<Client> it = attendants.iterator(); it.hasNext(); ) {
            tmp += it.next().toString();
        }
        return tmp;
    }

    public synchronized Vector<Client> getClients() {
        return attendants;
    }
}
