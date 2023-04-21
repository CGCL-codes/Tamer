package pointright2.fwx;

import iwork.eheap2.*;

public class EventServer extends Thread {

    EventHandler m_handler;

    String m_host;

    int m_port;

    EventHeap m_eventHeap = null;

    public EventServer(String host, EventHandler handler) throws NumberFormatException {
        int colon = host.indexOf(':');
        if (colon >= 0) {
            m_host = host.substring(0, colon);
            m_port = Integer.parseInt(host.substring(colon + 1));
        } else {
            m_host = host;
            m_port = -1;
        }
        m_handler = handler;
    }

    public void run() {
        try {
            Event templates[] = m_handler.createTemplates();
            m_eventHeap = new EventHeap(m_host, m_port);
            m_handler.onConnected();
            while (!Thread.interrupted()) {
                Event results[] = m_eventHeap.waitForEvent(templates);
                if (results != null && results[0] != null) {
                    m_handler.onEvent(results[0]);
                }
            }
        } catch (Exception ex) {
            m_handler.onFailed(ex);
        }
    }

    public EventHeap getEventHeap() {
        return m_eventHeap;
    }
}
