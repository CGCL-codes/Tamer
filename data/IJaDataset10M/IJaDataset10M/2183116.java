package com.cyberypower.sjs.jabber;

import com.cyberypower.sjs.common.Config;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.*;
import java.util.HashMap;
import java.util.Map;

public class JabberServerSocket {

    private JabberAddress bindAddr;

    private XMPPConnection connection;

    private ChatManager chatmanager;

    public JabberServerSocket(JabberAddress bindAddr) {
        this.bindAddr = bindAddr;
        initConnection();
    }

    private Logger log = Logger.getLogger(this.getClass());

    private void initConnection() {
        connection = new XMPPConnection(bindAddr.getHost());
        try {
            connection.connect();
            connection.login(bindAddr.getUser(), bindAddr.getPass());
        } catch (XMPPException e) {
            log.fatal(e, e);
            System.exit(4);
        }
        XMPPConnection.DEBUG_ENABLED = true;
        log.info("Jabber socket ready : " + bindAddr);
        chatmanager = connection.getChatManager();
        chatmanager.addChatListener(new ChatManagerListener() {

            private Logger log = Logger.getLogger(this.getClass());

            public void chatCreated(Chat chat, boolean b) {
                if (b) return;
                log.debug("new JABER connetion (" + chat.getThreadID() + ") local=" + b);
                ch = chat;
                System.out.println("notifyAll ");
                synchronized (flagServer) {
                    pleaseWaitServer = false;
                    flagServer.notifyAll();
                }
            }
        });
    }

    private Map<String, JabberClientSocket> jcss = new HashMap<String, JabberClientSocket>();

    private Chat ch = null;

    private final Object flagServer = new Object();

    public void close() {
    }

    private boolean pleaseWaitServer = false;

    public synchronized JabberClientSocket accept() {
        synchronized (flagServer) {
            try {
                pleaseWaitServer = true;
                while (pleaseWaitServer) {
                    flagServer.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        JabberClientSocket jcs = new JabberClientSocket(new JabberAddress(ch.getParticipant()), ch);
        jcss.put(ch.getThreadID(), jcs);
        jcs.sendText(Config.CHAT_INITED_APPROVED);
        return jcs;
    }

    public JabberClientSocket connect(JabberAddress ja) {
        return new JabberClientSocket(ja, chatmanager.createChat(ja.toString(), null));
    }
}
