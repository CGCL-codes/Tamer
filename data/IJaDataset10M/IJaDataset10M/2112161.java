package com.thegreatchina.im.msn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class MessageHistory {

    protected static Logger logger = Logger.getLogger(MessageHistory.class);

    private Map<String, List<Message>> map = new HashMap<String, List<Message>>();

    public void add(String account, Message m) {
        logger.debug("MessageHistory.add(..");
        List<Message> list = map.get(account);
        if (list == null) {
            list = new ArrayList<Message>();
            map.put(account, list);
        }
        list.add(m);
    }

    public List<Message> getMessages(String account) {
        List<Message> list = map.get(account);
        if (list == null) {
            list = new ArrayList<Message>();
        }
        return list;
    }

    /**
	 * 
	 * @return map<contact, key>
	 */
    public Set<String> getAccountHasNewMessages() {
        logger.debug("MessageHistory.getAccountHasNewMessages(.." + map.size());
        Set<String> msgs = new HashSet<String>();
        for (String account : map.keySet()) {
            List<Message> list = map.get(account);
            for (Message m : list) {
                if (m.isNew()) {
                    msgs.add(account);
                    break;
                }
            }
        }
        return msgs;
    }

    public void acknowledgeMessage(int trId, boolean acknowledged) {
        for (List<Message> l : map.values()) {
            for (Message m : l) {
                if (m.getTrId() == trId) {
                    m.setAck(acknowledged);
                }
            }
        }
    }

    public Map<String, List<Message>> getMap() {
        return map;
    }

    public String toString() {
        String str = new String();
        for (String key : map.keySet()) {
            str += " with Account:" + key + "\n";
            for (Message m : map.get(key)) {
                str += "\t" + m.toString() + "\n";
            }
            str += "\n";
        }
        return str;
    }
}
