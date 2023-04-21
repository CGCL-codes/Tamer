package com;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author stallionf1
 */
public final class TemporaryStorage {

    private static TemporaryStorage _instance = null;

    ArrayList<Message> messages = new ArrayList<Message>();

    private TemporaryStorage() {
    }

    public static synchronized TemporaryStorage getInstance() {
        if (_instance == null) {
            _instance = new TemporaryStorage();
        }
        return _instance;
    }

    public synchronized void addMessage(Message m) {
        if (messages.size() == 5) {
            messages.remove(0);
        }
        messages.add(m);
    }

    public synchronized JSONObject getMessages() {
        JSONObject json = new JSONObject();
        JSONObject innerJson;
        JSONArray array = new JSONArray();
        if (!messages.isEmpty()) {
            for (Message m : messages) {
                try {
                    innerJson = new JSONObject();
                    innerJson.put("guid", m.getGUID());
                    innerJson.put("author", m.getAUTHOR());
                    innerJson.put("message", m.getMESSAGE_TEXT());
                    innerJson.put("message_time", m.getMESSAGE_TIME());
                    array.put(innerJson);
                    json.put("messages", array);
                } catch (JSONException ex) {
                    Logger.getLogger(TemporaryStorage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            return new DBConnector().getFirstMessages();
        }
        return json;
    }

    public synchronized JSONObject getMessages(String GUID) {
        System.out.println(messages.size());
        JSONObject json = new JSONObject();
        JSONObject innerJson;
        JSONArray array = new JSONArray();
        if (!messages.isEmpty()) {
            boolean message_exists = false;
            for (Message m : messages) {
                try {
                    if (GUID.equals(m.getGUID())) {
                        message_exists = true;
                    }
                    if (message_exists) {
                        innerJson = new JSONObject();
                        innerJson.put("guid", m.getGUID());
                        innerJson.put("author", m.getAUTHOR());
                        innerJson.put("message", m.getMESSAGE_TEXT());
                        innerJson.put("message_time", m.getMESSAGE_TIME());
                        array.put(innerJson);
                        json.put("messages", array);
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(TemporaryStorage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (!message_exists) {
                json = new DBConnector().getMessagesFromId(GUID);
            }
        }
        return json;
    }
}
