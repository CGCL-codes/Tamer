package jms4sqs.messages;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import jms4sqs.SqsException;
import org.apache.commons.collections.iterators.IteratorEnumeration;

/**
 * @author colincrist@hermesjms.com
 * @version $Id: MapMessageImpl.java,v 1.4 2004/09/16 20:30:49 colincrist Exp $
 */
public class MapMessageImpl extends MessageImpl implements MapMessage {

    private Map body = new HashMap();

    /**
     *  
     */
    public MapMessageImpl() {
        super();
    }

    public void clearBody() throws JMSException {
        body.clear();
    }

    public boolean getBoolean(String arg0) throws JMSException {
        if (body.containsKey(arg0) && body.get(arg0) instanceof Boolean) {
            return ((Boolean) body.get(arg0)).booleanValue();
        } else {
            throw new SqsException("No such property " + arg0);
        }
    }

    public byte getByte(String arg0) throws JMSException {
        if (body.containsKey(arg0) && body.get(arg0) instanceof Byte) {
            return ((Byte) body.get(arg0)).byteValue();
        } else {
            throw new SqsException("No such property " + arg0);
        }
    }

    public byte[] getBytes(String arg0) throws JMSException {
        if (body.containsKey(arg0) && body.get(arg0) instanceof byte[]) {
            return ((byte[]) body.get(arg0));
        } else {
            throw new SqsException("No such property " + arg0);
        }
    }

    public char getChar(String arg0) throws JMSException {
        if (body.containsKey(arg0) && body.get(arg0) instanceof Character) {
            return ((Character) body.get(arg0)).charValue();
        } else {
            throw new SqsException("No such property " + arg0);
        }
    }

    public double getDouble(String arg0) throws JMSException {
        if (body.containsKey(arg0) && body.get(arg0) instanceof Double) {
            return ((Double) body.get(arg0)).doubleValue();
        } else {
            throw new SqsException("No such property " + arg0);
        }
    }

    public float getFloat(String arg0) throws JMSException {
        if (body.containsKey(arg0) && body.get(arg0) instanceof Float) {
            return ((Float) body.get(arg0)).floatValue();
        } else {
            throw new SqsException("No such property " + arg0);
        }
    }

    public int getInt(String arg0) throws JMSException {
        if (body.containsKey(arg0) && body.get(arg0) instanceof Integer) {
            return ((Integer) body.get(arg0)).intValue();
        } else {
            throw new SqsException("No such property " + arg0);
        }
    }

    public long getLong(String arg0) throws JMSException {
        if (body.containsKey(arg0) && body.get(arg0) instanceof Long) {
            return ((Long) body.get(arg0)).longValue();
        } else {
            throw new SqsException("No such property " + arg0);
        }
    }

    public Enumeration getMapNames() throws JMSException {
        return new IteratorEnumeration(body.keySet().iterator());
    }

    public Object getObject(String arg0) throws JMSException {
        if (body.containsKey(arg0)) {
            return body.get(arg0);
        } else {
            throw new SqsException("No such property " + arg0);
        }
    }

    public short getShort(String arg0) throws JMSException {
        if (body.containsKey(arg0) && body.get(arg0) instanceof Short) {
            return ((Short) body.get(arg0)).shortValue();
        } else {
            throw new SqsException("No such property " + arg0);
        }
    }

    public String getString(String arg0) throws JMSException {
        if (body.containsKey(arg0) && body.get(arg0) instanceof String) {
            return (String) body.get(arg0);
        } else {
            throw new SqsException("No such property " + arg0);
        }
    }

    public boolean itemExists(String arg0) throws JMSException {
        return body.containsKey(arg0);
    }

    public void setBoolean(String arg0, boolean arg1) throws JMSException {
        body.put(arg0, new Boolean(arg1));
    }

    public void setByte(String arg0, byte arg1) throws JMSException {
        body.put(arg0, new Byte(arg1));
    }

    public void setBytes(String arg0, byte[] arg1) throws JMSException {
        body.put(arg0, arg1);
    }

    public void setBytes(String arg0, byte[] arg1, int arg2, int arg3) throws JMSException {
        body.put(arg0, arg1);
    }

    public void setChar(String arg0, char arg1) throws JMSException {
        body.put(arg0, new Character(arg1));
    }

    public void setDouble(String arg0, double arg1) throws JMSException {
        body.put(arg0, new Double(arg1));
    }

    public void setFloat(String arg0, float arg1) throws JMSException {
        body.put(arg0, new Float(arg1));
    }

    public void setInt(String arg0, int arg1) throws JMSException {
        body.put(arg0, new Integer(arg1));
    }

    public void setLong(String arg0, long arg1) throws JMSException {
        body.put(arg0, new Long(arg1));
    }

    public void setObject(String arg0, Object arg1) throws JMSException {
        body.put(arg0, arg1);
    }

    public void setShort(String arg0, short arg1) throws JMSException {
        body.put(arg0, new Short(arg1));
    }

    public void setString(String arg0, String arg1) throws JMSException {
        body.put(arg0, arg1);
    }
}
