package org.myrobotlab.framework;

import java.io.Serializable;
import org.apache.log4j.Logger;

public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Logger LOG = Logger.getLogger(Property.class);

    public int ID;

    public String domain;

    public String name;

    public String value;

    public Property() {
        domain = new String();
        name = new String();
        value = new String();
    }

    public Property(final Property other) {
        this();
        set(other);
    }

    public void set(final Property other) {
        ID = other.ID;
        domain = other.domain;
        name = other.name;
        value = other.value;
    }

    public static String scope() {
        String ret = new String("myrobotlab");
        return ret;
    }

    ;

    public static String name() {
        if (LOG.isDebugEnabled()) {
            StringBuilder logString = new StringBuilder("Property.getName()()");
            LOG.debug(logString);
        }
        String ret = new String("Property");
        return ret;
    }

    ;

    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("{");
        ret.append("\"ID\":\"" + ID + "\"");
        ret.append("\"domain\":" + "\"" + domain + "\"");
        ret.append("\"name\":" + "\"" + name + "\"");
        ret.append("\"value\":" + "\"" + value + "\"");
        ret.append("}");
        return ret.toString();
    }
}
