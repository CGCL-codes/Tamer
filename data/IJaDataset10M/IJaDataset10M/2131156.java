package vote;

import java.util.*;

public class Parameters extends java.util.Properties {

    public Parameters() {
        super();
    }

    public void insert(String[] cmd, String[] extras, Hashtable singles) {
        int extraindex = 0;
        int i = 0;
        while (i < cmd.length) {
            while ((extraindex < extras.length) && (containsKey(extras[extraindex]))) extraindex++;
            if (cmd[i].charAt(0) == '-') {
                String cmdname = cmd[i].substring(1).toLowerCase();
                if ((i + 1 == cmd.length) || (singles.containsKey(cmdname))) {
                    add(cmdname, "true");
                    i++;
                } else {
                    add(cmdname, cmd[i + 1]);
                    i += 2;
                }
            } else {
                if (extraindex >= extras.length) throw new IllegalArgumentException("Unexpected argument: " + cmd[i]); else add(extras[extraindex], cmd[i]);
                i++;
                extraindex++;
            }
        }
    }

    public String extract(String key, String defaults) {
        String ret = getProperty(key.toLowerCase(), defaults);
        remove(key.toLowerCase());
        return ret;
    }

    public String extract(String key) {
        String ret = getProperty(key.toLowerCase());
        if (null == ret) throw new IllegalArgumentException("Missing argument: " + key);
        remove(key.toLowerCase());
        return ret;
    }

    public void add(String key, String value) {
        if (containsKey(key.toLowerCase())) {
            throw new IllegalArgumentException("Repeated argument: " + key);
        }
        put(key.toLowerCase(), value);
    }

    public void checkEmpty() {
        if (!isEmpty()) {
            System.err.println(keys());
            throw new IllegalArgumentException("Illegal arguments found");
        }
    }
}
