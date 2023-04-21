package de.herberlin.server.common.event;

import java.util.*;
import javax.swing.table.TableModel;

/**
 *
 * @author Hans Joachim Herbertz
 * @created 08.02.2003
 */
public class EventDispatcher {

    private static LogTableModel model = null;

    private static int noConnections = 0;

    private static LogTableModel getLogTableModel() {
        if (model == null) {
            model = new LogTableModel();
        }
        return model;
    }

    public static void add(BasicEvent ev) {
        if (ev instanceof ApplicationEvent) {
            ApplicationEvent appEv = (ApplicationEvent) ev;
            switch(appEv.getStatus()) {
                case ApplicationEvent.SERVER_STOPPED:
                case ApplicationEvent.SERVER_STARTED:
                    noConnections = 0;
                    break;
                case ApplicationEvent.CONNECTION_ESTABLISHED:
                    noConnections++;
                    break;
                case ApplicationEvent.CONNECTION_CLOSED:
                    noConnections = Math.min(0, noConnections--);
                    break;
                default:
                    break;
            }
            fireApplicationEvent(appEv);
        }
        getLogTableModel().add(ev);
    }

    /**
	 * Returns the number of connections open for the server.
	 */
    public static int getNoConnections() {
        return noConnections;
    }

    public static TableModel getModel() {
        return getLogTableModel();
    }

    /**
	 * Application Event listeners handling. */
    private static List listeners = new LinkedList();

    public static void addApplicationListener(ApplicationEventListener listener) {
        listeners.add(listener);
    }

    public static void removeApplicationListener(ApplicationEventListener listener) {
        listeners.remove(listener);
    }

    private static void fireApplicationEvent(ApplicationEvent ev) {
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
            ApplicationEventListener listener = (ApplicationEventListener) it.next();
            listener.applicationStateChanged(ev);
        }
    }
}
