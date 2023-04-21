package org.wisigoth.chat.client.jabber;

import org.wisigoth.chat.client.Session.ConnectionState;
import org.wisigoth.chat.client.jabber.xml.JabberInputHandler;

public class InputThread extends Thread {

    private final SessionImpl session;

    private final InPacketQueue packetQ;

    public InputThread(SessionImpl session) {
        this.session = session;
        this.packetQ = new InPacketQueue();
        InQueueThread qThread = new InQueueThread(this.packetQ);
        qThread.setDaemon(true);
        qThread.addListener(new OpenStreamHandler(), "stream:stream");
        qThread.addListener(new CloseStreamHandler(), "/stream:stream");
        qThread.addListener(session.getDispatcher(), "message");
        qThread.start();
    }

    @Override
    public void run() {
        try {
            JabberInputHandler handler = new JabberInputHandler(this.packetQ);
            handler.process(this.session);
        } catch (Exception ex) {
            if (this.session.getConnectionState() == ConnectionState.ONLINE) {
                try {
                    this.session.disconnect();
                } catch (Exception eex) {
                }
            }
        }
    }
}
