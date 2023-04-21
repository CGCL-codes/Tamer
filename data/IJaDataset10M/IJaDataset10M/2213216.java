package com.qbrowser.property;

import com.qbrowser.QBrowserV2;
import com.qbrowser.util.QBrowserUtil;
import java.util.ResourceBundle;
import javax.swing.JComboBox;

/**
 *
 * @author takemura
 */
public class PropertyUtil {

    static ResourceBundle resources = QBrowserV2.resources;

    public static void validateJMSHeaderValueType(String key, Object value) throws QBrowserPropertyException {
        if (key == null) return;
        if ((key != null) && (value == null)) {
            throw new QBrowserPropertyException("Q0005" + QBrowserV2.MAGIC_SEPARATOR + key + QBrowserV2.MAGIC_SEPARATOR + "null");
        } else if (key.equals("JMSExpiration")) {
            testInt(key, value);
        } else if (key.equals("JMSPriority")) {
            testInt(key, value);
        } else if (key.equals("JMSCorrelationID")) {
            testString(key, value);
        } else if (key.equals("JMSType")) {
            testString(key, value);
        } else if (key.equals("JMSReplyTo")) {
            testString(key, value);
        } else {
            throw new QBrowserPropertyException("Q0006" + QBrowserV2.MAGIC_SEPARATOR + key + QBrowserV2.MAGIC_SEPARATOR + value);
        }
    }

    static void testInt(String key, Object value) throws QBrowserPropertyException {
        if (value instanceof String) {
            try {
                Integer.parseInt((String) value);
            } catch (NumberFormatException nfe) {
                throw new QBrowserPropertyException("Q0007" + QBrowserV2.MAGIC_SEPARATOR + key + QBrowserV2.MAGIC_SEPARATOR + value);
            }
        } else if (value instanceof Integer) {
            return;
        } else if (value instanceof Long) {
            return;
        } else {
            throw new QBrowserPropertyException("Q0007" + QBrowserV2.MAGIC_SEPARATOR + key + QBrowserV2.MAGIC_SEPARATOR + value);
        }
    }

    static void testString(String key, Object value) throws QBrowserPropertyException {
        if (value instanceof String) {
            if (((String) value).length() == 0) {
                throw new QBrowserPropertyException("Q0008" + QBrowserV2.MAGIC_SEPARATOR + key + QBrowserV2.MAGIC_SEPARATOR + value);
            }
        } else {
            throw new QBrowserPropertyException("Q0008" + QBrowserV2.MAGIC_SEPARATOR + key + QBrowserV2.MAGIC_SEPARATOR + value);
        }
    }

    static void testBoolean(String key, Object value) throws QBrowserPropertyException {
        if (value instanceof String) {
            try {
                Boolean.parseBoolean((String) value);
            } catch (Exception fe) {
                throw new QBrowserPropertyException("Q0009" + QBrowserV2.MAGIC_SEPARATOR + key + QBrowserV2.MAGIC_SEPARATOR + value);
            }
        } else if (value instanceof Boolean) {
            return;
        } else {
            throw new QBrowserPropertyException("Q0009" + QBrowserV2.MAGIC_SEPARATOR + key + QBrowserV2.MAGIC_SEPARATOR + value);
        }
    }

    public static String selfDescribe(Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append(resources.getString("qkey.msg.msg185")).append(" = ");
        if (obj == null) {
            sb.append("NULL");
        } else if ((obj instanceof String) || (obj instanceof Integer) || (obj instanceof Boolean) || (obj instanceof Byte) || (obj instanceof Character) || (obj instanceof Double) || (obj instanceof Float) || (obj instanceof Long) || (obj instanceof Short)) {
            sb.append(obj.toString()).append(" (").append(obj.getClass().getName()).append(resources.getString("qkey.msg.msg183")).append(")");
        } else if (obj instanceof byte[]) {
            byte[] bytearray = (byte[]) obj;
            boolean isOmitted = false;
            int targetlength = bytearray.length;
            if (targetlength > 1000) {
                targetlength = 1000;
                isOmitted = true;
            }
            sb.append(" ").append(resources.getString("qkey.msg.msg186")).append(" = ").append(bytearray.length).append(resources.getString("qkey.msg.msg187")).append(" (byte[]").append(resources.getString("qkey.msg.msg183")).append(")").append("\n").append(QBrowserUtil.toHexDump(bytearray, targetlength));
            if (isOmitted) {
                sb.append("... \n").append(resources.getString("qkey.msg.msg211"));
            }
        } else {
            sb.append("Object (").append(obj.getClass().getName()).append(resources.getString("qkey.msg.msg183")).append(")");
        }
        return sb.toString();
    }

    public static JComboBox getUserPropTypeComboBox() {
        JComboBox newjcb = new JComboBox();
        initUserPropTypeComboBox(newjcb);
        return newjcb;
    }

    private static void initUserPropTypeComboBox(JComboBox value) {
        value.addItem(Property.INT_TYPE);
        value.addItem(Property.STRING_TYPE);
        value.addItem(Property.BOOLEAN_TYPE);
        value.addItem(Property.BYTE_TYPE);
        value.addItem(Property.DOUBLE_TYPE);
        value.addItem(Property.FLOAT_TYPE);
        value.addItem(Property.LONG_TYPE);
        value.addItem(Property.SHORT_TYPE);
        value.setSelectedIndex(0);
    }
}
