package com.jot.system.visitors;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.jot.system.Ack;
import com.jot.system.JotContext;
import com.jot.system.Mid;
import com.jot.system.WidgetServerBucket;
import com.jot.system.nio.JotSocket;
import com.jot.system.pjson.Guid;

public class UserSessionSocket {

    public static Logger logger = Logger.getLogger(UserSessionSocket.class);

    private static class WidgetRecord {

        public WidgetRecord() {
            jport = null;
        }

        JotSocket jport;
    }

    public Guid session;

    public JotSocket socket;

    private Map<Guid, WidgetRecord> widgets = new HashMap<Guid, WidgetRecord>(4);

    /** return the socket of the widget server to use
     * 
     * @param widget
     * @return
     * @throws Exception 
     */
    public JotSocket findDestWidgetServer(JotContext g, Guid widget) throws Exception {
        WidgetRecord wrec = widgets.get(widget);
        if (wrec == null) {
            if (widgets.size() > 5000) throw new Exception("Too many widgets in USS");
            wrec = new WidgetRecord();
            Guid widgetServer = null;
            WidgetServerBucket bucket = g.widgetServers.getBucketMgr().find(widget);
            int index = rand.next(bucket.ports.length);
            if (index >= bucket.ports.length || index < 0) logger.error("why do I EVER get this?? ");
            Mid port = bucket.ports[index];
            widgetServer = port.name;
            wrec.jport = g.getPeerPort(widgetServer);
            widgets.put(widget, wrec);
        }
        return wrec.jport;
    }

    /**
     * write a message to our widget server that is serving widget
     * 
     * @param widget
     * @param bytes
     * @throws Exception
     */
    public void addAndWrite(JotContext g, Guid widget, byte[] bytes) throws Exception {
        JotSocket jport = findDestWidgetServer(g, widget);
        if (logger.isTraceEnabled()) logger.trace("writing save message to " + jport.peer.getName());
        jport.write(bytes);
    }

    public void remove(Guid widget) {
        WidgetRecord w = widgets.get(widget);
        Ack msg = new Ack(w.jport);
        msg.forgetSession(session);
        if (logger.isTraceEnabled()) logger.trace("Session server disconnecting session/widget " + session + "/" + widget);
    }

    public void shutdown(JotContext g) throws Exception {
        for (Guid guid : widgets.keySet()) {
            remove(guid);
        }
    }

    static UssRandGenerator rand = new UssRandGenerator();

    static class UssRandGenerator {

        long rand = 1234567891;

        UssRandGenerator() {
        }

        public int next(int max) {
            if (max == 0) return 0;
            rand = rand * 1103515245L + 12345L;
            long tmp = Math.abs(rand);
            int res = (int) (tmp % max);
            return res;
        }
    }
}
