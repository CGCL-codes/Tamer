package model.utils;

import model.Message;

public class MessageCoDec {

    public static String codeMessage(Message m) {
        String result = new String();
        if (m.getType() != null && m.getAction() != null && m.getParams() != null) {
            result += "#" + m.getType() + "#" + m.getAction();
            for (String s : m.getParams()) {
                result += "#" + s;
            }
            return result;
        }
        return null;
    }

    public static Message decodeMessage(String s) {
        String[] objects = s.split("#");
        String type;
        String action;
        String[] params;
        if (objects.length > 2) {
            type = objects[1];
            action = objects[2];
            params = new String[objects.length - 3];
            int j = 0;
            for (int i = 3; i < objects.length; i++) {
                params[j] = objects[i];
                j++;
            }
            return new Message(type, action, params);
        }
        return null;
    }
}
