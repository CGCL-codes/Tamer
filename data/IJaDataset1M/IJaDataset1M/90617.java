package jsslib.util;

import java.util.Vector;

/**
 *
 * @author  Robert Schuster
 * 
 * @info    Erläuterung in UserEvent.java
 * 
 */
public class UserEventListener {

    protected Vector listener = new Vector();

    public void remove(UserEventListener l) {
        listener.remove(l);
    }

    public void removeAll() {
        listener.removeAllElements();
    }

    public void UserEventPerformed(UserEvent e) {
        for (int i = 0; i < listener.size(); i++) {
            try {
                ((UserEventListener) listener.elementAt(i)).UserEventPerformed(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void add(UserEventListener a) {
        if (!listener.contains(a)) listener.addElement(a);
    }
}
