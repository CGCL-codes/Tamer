package com.handy.webwork.action;

import java.util.HashMap;

/**
 * 错误消息包。
 * 
 * @author rocken.zeng@gmail.com
 * 
 */
public class ActionMessage {

    public HashMap<String, String> message = new HashMap<String, String>();

    public void put(String key, String value) {
        message.put(key, value);
    }

    public String get(String key) {
        return message.get(key);
    }

    public HashMap<String, String> getMessage() {
        return message;
    }

    public void setMessage(HashMap<String, String> message) {
        this.message = message;
    }

    public int size() {
        return message.size();
    }

    public boolean isEmpty() {
        return message.isEmpty();
    }
}
