package org.imlinker.buddy;

import java.util.ArrayList;
import java.util.HashMap;
import org.imlinker.event.Event;
import org.imlinker.event.EventListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class ChatAdapter {

    public ChatAdapter(XMPPConnection connection) {
        this.connection = connection;
        initActions();
        chatMap = new HashMap<String, Chat>();
    }

    private void initActions() {
        connection.addPacketListener(new PacketListener() {

            @Override
            public void processPacket(Packet p) {
                Message m = (Message) p;
                ArrayList<EventListener> list = listeners.get(m.getFrom());
                if (list != null) {
                    for (EventListener listener : list) {
                        listener.eventRecieved(new Event("", m.getBody()));
                    }
                }
                int i = m.getFrom().indexOf("/");
                String user = m.getFrom().substring(0, i);
                list = listeners.get(user);
                if (list != null) {
                    for (EventListener listener : list) {
                        listener.eventRecieved(new Event("", m.getBody()));
                    }
                }
            }
        }, new MessageTypeFilter(Message.Type.chat));
    }

    public void addListener(String user, EventListener listener) {
        ArrayList<EventListener> list = listeners.get(user);
        if (list == null) {
            list = new ArrayList<EventListener>();
            listeners.put(user, list);
        }
        list.add(listener);
    }

    public void removeListener(String user, EventListener listener) {
        ArrayList<EventListener> list = listeners.get(user);
        if (list != null) {
            list.remove(listener);
        }
    }

    public void clearListener(String user) {
        listeners.remove(user);
    }

    public void sendMessage(String user, String m) {
        Chat chat;
        if ((chat = chatMap.get(user)) == null) {
            chat = connection.getChatManager().createChat(user, null);
            chatMap.put(user, chat);
        }
        try {
            chat.sendMessage(m);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, ArrayList<EventListener>> listeners = new HashMap<String, ArrayList<EventListener>>();

    private XMPPConnection connection;

    private HashMap<String, Chat> chatMap;
}
