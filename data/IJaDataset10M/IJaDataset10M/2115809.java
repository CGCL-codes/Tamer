package com.peterhi.net.conv;

import static com.peterhi.util.LogMacros.*;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;
import com.peterhi.net.packet.Packet;
import com.peterhi.util.ByteArray;
import com.peterhi.util.IO;

/**
 *
 * @author HAI YUN TAO
 */
public class Converter {

    public static final Logger logger = getLogger("converter");

    public static final String METHOD_NAME = "getID";

    private static final Converter instance = new Converter();

    public static final Converter getInstance() {
        return instance;
    }

    private Hashtable<Integer, Class<?>> registry;

    protected Converter() {
        registry = new Hashtable<Integer, Class<?>>();
    }

    public Converter put(Integer id, Class<?> c) {
        try {
            Object o = c.newInstance();
            if (o instanceof Convertible) {
                if (id != null) {
                    registry.put(id, c);
                } else {
                    Method m = c.getMethod(METHOD_NAME);
                    Integer defaultId = (Integer) m.invoke(o);
                    registry.put(defaultId, c);
                }
            }
        } catch (Exception ex) {
            throwing(logger, "Converter", "put", ex, c);
        }
        return this;
    }

    public Class<?> get(Integer b) {
        return registry.get(b);
    }

    public Integer get(Class<?> c) {
        for (Enumeration<Integer> e = registry.keys(); e.hasMoreElements(); ) {
            Integer cur = e.nextElement();
            Class<?> curClass = registry.get(cur);
            if (curClass == c) {
                return cur;
            }
        }
        return null;
    }

    public void load(Properties properties) {
        try {
            for (Enumeration<Object> e = properties.keys(); e.hasMoreElements(); ) {
                String cur = (String) e.nextElement();
                String className = cur.replaceFirst("conv.", "");
                Integer classId = Integer.parseInt(properties.getProperty(cur).trim());
                Class<?> c = Class.forName(className);
                put(classId, c);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int size() {
        return registry.size();
    }

    public Iterator<Class<?>> iterator() {
        return registry.values().iterator();
    }

    /**
     * Convenient method for debugging
     * 
     * @param conv
     * @return
     */
    public String toString(Convertible conv) {
        try {
            String ret = "";
            Class<?> cls = conv.getClass();
            ret += cls.getName() + "\n";
            Field[] fields = cls.getFields();
            ret += fields.length + " fields in total\n";
            for (int i = 0; i < fields.length; i++) {
                Field cur = fields[i];
                if (Modifier.isPublic(cur.getModifiers()) && (!Modifier.isStatic(cur.getModifiers()))) {
                    if (cur.getType().isArray()) {
                        ret += "\t" + cur.getName() + ", array of " + cur.getType().getComponentType() + "\n";
                        Object[] array = (Object[]) cur.get(conv);
                        for (int j = 0; j < array.length; j++) {
                            ret += "\t\t" + array[j] + "\n";
                        }
                    } else {
                        ret += "\t" + cur.getName() + ": " + cur.get(conv) + "\n";
                    }
                }
            }
            return ret;
        } catch (Exception ex) {
            return ex.toString() + " - " + ex.getMessage();
        }
    }

    public Packet[] convertAndSplit(Packet borrow, Convertible conv) throws IOException, IllegalArgumentException, IllegalAccessException, CloneNotSupportedException {
        byte[] data = convert(conv);
        ByteArray[] arrs = ByteArray.chop(data, Packet.PACKET_SIZE - Packet.HEADER_SIZE);
        Packet[] ret = new Packet[arrs.length];
        for (int i = 0; i < arrs.length; i++) {
            ret[i] = (Packet) borrow.clone();
            ret[i].setAck(false);
            ret[i].setIndex(i);
            ret[i].setCount(arrs.length);
            ret[i].setMessageId(get(conv.getClass()));
            ret[i].setData(arrs[i].get());
        }
        return ret;
    }

    public byte[] convert(Convertible conv) throws IOException, IllegalArgumentException, IllegalAccessException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bout);
        out.writeInt(get(conv.getClass()));
        Field[] fields = conv.getClass().getFields();
        Arrays.sort(fields, new Sorter());
        for (int i = 0; i < fields.length; i++) {
            Field cur = fields[i];
            Class<?> fieldType = cur.getType();
            if (Modifier.isPublic(cur.getModifiers()) && (!Modifier.isStatic(cur.getModifiers()))) {
                if (fieldType == boolean.class) {
                    out.writeBoolean(cur.getBoolean(conv));
                } else if (fieldType == byte.class) {
                    out.writeByte(cur.getByte(conv));
                } else if (fieldType == short.class) {
                    out.writeShort(cur.getShort(conv));
                } else if (fieldType == char.class) {
                    out.writeChar(cur.getChar(conv));
                } else if (fieldType == int.class) {
                    out.writeInt(cur.getInt(conv));
                } else if (fieldType == long.class) {
                    out.writeLong(cur.getLong(conv));
                } else if (fieldType == float.class) {
                    out.writeFloat(cur.getFloat(conv));
                } else if (fieldType == double.class) {
                    out.writeDouble(cur.getDouble(conv));
                } else if (fieldType == String.class) {
                    IO.writeString(out, (String) cur.get(conv));
                } else if (fieldType == Color.class) {
                    IO.writeColor(out, (Color) cur.get(conv));
                } else if (fieldType.isArray()) {
                    Class<?> arrType = fieldType.getComponentType();
                    Object array = cur.get(conv);
                    if (arrType == boolean.class) {
                        IO.writeBooleanArray(out, (boolean[]) array);
                    } else if (arrType == byte.class) {
                        IO.writeByteArray(out, (byte[]) array);
                    } else if (arrType == short.class) {
                        IO.writeShortArray(out, (short[]) array);
                    } else if (arrType == char.class) {
                        IO.writeCharArray(out, (char[]) array);
                    } else if (arrType == int.class) {
                        IO.writeIntArray(out, (int[]) array);
                    } else if (arrType == long.class) {
                        IO.writeLongArray(out, (long[]) array);
                    } else if (arrType == float.class) {
                        IO.writeFloatArray(out, (float[]) array);
                    } else if (arrType == double.class) {
                        IO.writeDoubleArray(out, (double[]) array);
                    } else if (arrType == String.class) {
                        IO.writeStringArray(out, (String[]) array);
                    }
                }
            }
        }
        return bout.toByteArray();
    }

    /**
     * Convert back
     * @param data
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Convertible revert(byte[] data) throws IOException, InstantiationException, IllegalAccessException {
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        DataInputStream in = new DataInputStream(bin);
        int id = in.readInt();
        Class<?> convType = get(Integer.valueOf(id));
        if (convType != null) {
            Convertible conv = (Convertible) convType.newInstance();
            Field[] fields = convType.getFields();
            Arrays.sort(fields, new Sorter());
            for (int i = 0; i < fields.length; i++) {
                Field cur = fields[i];
                Class<?> fieldType = cur.getType();
                if (Modifier.isPublic(cur.getModifiers()) && (!Modifier.isStatic(cur.getModifiers()))) {
                    if (fieldType == boolean.class) {
                        cur.setBoolean(conv, in.readBoolean());
                    } else if (fieldType == byte.class) {
                        cur.setByte(conv, in.readByte());
                    } else if (fieldType == short.class) {
                        cur.setShort(conv, in.readShort());
                    } else if (fieldType == char.class) {
                        cur.setChar(conv, in.readChar());
                    } else if (fieldType == int.class) {
                        cur.setInt(conv, in.readInt());
                    } else if (fieldType == long.class) {
                        cur.setLong(conv, in.readLong());
                    } else if (fieldType == float.class) {
                        cur.setFloat(conv, in.readFloat());
                    } else if (fieldType == double.class) {
                        cur.setDouble(conv, in.readDouble());
                    } else if (fieldType == String.class) {
                        cur.set(conv, IO.readString(in));
                    } else if (fieldType == Color.class) {
                        cur.set(conv, IO.readColor(in));
                    } else if (fieldType.isArray()) {
                        Class<?> arrType = fieldType.getComponentType();
                        if (arrType == boolean.class) {
                            cur.set(conv, IO.readBooleanArray(in));
                        } else if (arrType == byte.class) {
                            cur.set(conv, IO.readByteArray(in));
                        } else if (arrType == short.class) {
                            cur.set(conv, IO.readShortArray(in));
                        } else if (arrType == char.class) {
                            cur.set(conv, IO.readCharArray(in));
                        } else if (arrType == int.class) {
                            cur.set(conv, IO.readIntArray(in));
                        } else if (arrType == long.class) {
                            cur.set(conv, IO.readLongArray(in));
                        } else if (arrType == float.class) {
                            cur.set(conv, IO.readFloatArray(in));
                        } else if (arrType == double.class) {
                            cur.set(conv, IO.readDoubleArray(in));
                        } else if (arrType == String.class) {
                            cur.set(conv, IO.readStringArray(in));
                        }
                    }
                }
            }
            return conv;
        } else {
            return null;
        }
    }

    public Convertible revert(Packet[] packets) throws IOException, IllegalArgumentException, InstantiationException, IllegalAccessException {
        ByteArray[] arrs = new ByteArray[packets.length];
        for (int i = 0; i < arrs.length; i++) {
            arrs[i] = new ByteArray(packets[i].getData());
        }
        ByteArray full = ByteArray.squeeze(arrs);
        return revert(full.get());
    }

    class Sorter implements Comparator<Field> {

        public int compare(Field f1, Field f2) {
            return f1.getName().compareTo(f2.getName());
        }
    }
}
