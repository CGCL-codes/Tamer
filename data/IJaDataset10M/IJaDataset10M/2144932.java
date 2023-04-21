package com.peterhi.net.server.sgs;

import java.io.Serializable;
import java.util.Hashtable;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ManagedObject;

public class SGSChannelData implements Serializable, ManagedObject {

    public static final String BINDING_PREFIX = "chnl_";

    /**
	 * Serializable ID
	 */
    private static final long serialVersionUID = -3421148067311005613L;

    public static final String NAME = "name";

    public static final String NSERVER_CHANNEL_ID = "ncid";

    public static final String PASSWORD = "password";

    public static final String ALLOW_OR_BLOCK = "allowOrBlock";

    public static final String NAME_LIST = "nameList";

    public static final String MODERATOR = "moderator";

    public static final String TEACHER_LIST = "teacherList";

    public static final String WHITEBOARD_DATA = "whiteboardData";

    private Hashtable<String, Serializable> properties;

    public SGSChannelData() {
        properties = new Hashtable<String, Serializable>();
    }

    public SGSChannelData(Hashtable<String, Serializable> properties) {
        this.properties = properties;
    }

    public void set(String key, Serializable data) {
        if (data == null) {
            properties.remove(key);
        } else {
            properties.put(key, data);
        }
    }

    public Serializable get(String key) {
        return properties.get(key);
    }

    public String getName() {
        return get(NAME, String.class);
    }

    public Channel getChannel() {
        return AppContext.getChannelManager().getChannel(getName());
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T get(String key, Class<T> cls) {
        return (T) get(key);
    }

    public String getBinding() {
        return BINDING_PREFIX + get(NAME, String.class);
    }

    public static String getBinding(String name) {
        return BINDING_PREFIX + name;
    }

    public static boolean isBoundAs(String name) {
        return name.startsWith(BINDING_PREFIX);
    }
}
