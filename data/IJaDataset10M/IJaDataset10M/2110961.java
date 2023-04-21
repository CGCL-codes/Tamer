package de.herberlin.pss.model;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.log4j.Logger;

public abstract class BasicVO<E extends Enum<E>> implements Serializable {

    protected Logger logger = Logger.getLogger(getClass());

    protected Properties props = null;

    public abstract String getTableName();

    public abstract String getPrimaryKeyName();

    public void setProperties(Properties props) {
        this.props = props;
    }

    public String get(E key) {
        if (props != null) {
            return props.getProperty(key.name());
        } else {
            return null;
        }
    }

    public void set(E key, Object value) {
        if (key == null || value == null) return;
        if (props == null) props = new Properties();
        props.setProperty(key.name(), value + "");
    }

    public Integer getInt(E key) {
        String v = get(key);
        if (v != null) {
            return new Integer(v);
        } else {
            return null;
        }
    }

    public String createInsertQuery() {
        if (props == null) {
            return null;
        }
        StringBuffer keys = new StringBuffer();
        StringBuffer values = new StringBuffer();
        Enumeration<Object> en = props.keys();
        while (en.hasMoreElements()) {
            String key = en.nextElement() + "";
            String val = props.getProperty(key);
            val = fixDBChars(val);
            keys.append(",");
            keys.append(key);
            values.append(",");
            if (key.startsWith("c")) {
                values.append("'");
                values.append(val);
                values.append("'");
            } else {
                values.append(val);
            }
        }
        StringBuffer query = new StringBuffer();
        query.append("insert into ");
        query.append(getTableName());
        query.append("(");
        query.append(keys.substring(1));
        query.append(") values (");
        query.append(values.substring(1));
        query.append(");");
        String result = query.toString();
        logger.info("Query=" + result);
        return result;
    }

    public String createDeleteQuery() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("delete from ");
        buffer.append(getTableName());
        buffer.append(" where ");
        buffer.append(getPrimaryKeyName());
        buffer.append(" = ");
        buffer.append(props.getProperty(getPrimaryKeyName()));
        String result = buffer.toString();
        logger.info("Query=" + result);
        return result;
    }

    private String fixDBChars(String in) {
        return in.replaceAll("'", " ");
    }
}
