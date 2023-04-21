package com.beam.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.beam.impl.bookmark.Bookmark;

/**
 * 
 * ����������������Ļ���ʵ�ֺ�һЩ��صĹ��ܷ���
 * 
 * @author beam
 * Dec 2, 2010  2:09:34 PM
 */
public class ObjectUtil {

    protected static final Log logger = LogFactory.getLog(ObjectUtil.class);

    /**
	 * �ж϶����Ƿ�Ϊnull
	 * @param obj
	 * @return
	 */
    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    /**
	 * �ж϶����Ƿ�Ϊnull
	 * @param obj
	 * @return
	 */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
	 * ���ֶ�ƥ���equals
	 * @param own
	 * @param otherObject
	 * @param fieldNames
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
    public static boolean equals(Object own, Object otherObject, String[] fieldNames) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        if (!equalsFilter(own, otherObject)) return false;
        Field[] ownFields = Reflect.getFields(own, fieldNames);
        Field[] otherFields = Reflect.getFields(otherObject, fieldNames);
        return equals(own, otherObject, ownFields, otherFields);
    }

    /**
	 * ʵ���equals , ȫƥ��ģʽ , ���е��ֶζ���Ҫ���.
	 * @param own
	 * @param otherObject
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
    public static boolean equals(Object own, Object otherObject) throws IllegalArgumentException, IllegalAccessException {
        if (!equalsFilter(own, otherObject)) return false;
        Field[] ownFields = Reflect.getFields(own);
        Field[] otherFields = Reflect.getFields(otherObject);
        return equals(own, otherObject, ownFields, otherFields);
    }

    /**
	 * equals��filter��,֤���������пɱ���
	 * @param own
	 * @param otherObject
	 * @return
	 */
    public static boolean equalsFilter(Object own, Object otherObject) {
        if (own == otherObject) {
            return true;
        }
        if (otherObject == null || own == null) {
            return false;
        }
        if (own.getClass() != otherObject.getClass()) {
            return false;
        }
        return true;
    }

    /**
	 * ʵ���equals , ȫƥ��ģʽ , ���е��ֶζ���Ҫ���.
	 * @param own
	 * @param otherObject
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
    public static boolean equals(Object own, Object otherObject, Field[] ownFields, Field[] otherFields) throws IllegalArgumentException, IllegalAccessException {
        if (!equalsFilter(own, otherObject)) return false;
        for (int i = 0; i < ownFields.length; i++) {
            Object ownValue = Reflect.getValue(own, ownFields[i]);
            Object otherValue = Reflect.getValue(otherObject, otherFields[i]);
            if (ObjectUtil.isEmpty(ownValue) && ObjectUtil.isEmpty(otherValue)) return true;
            if (ObjectUtil.isEmpty(ownValue) || ObjectUtil.isEmpty(otherValue)) return false;
            if (Reflect.getClassez(ownValue).isArray()) {
                int ownLength = Array.getLength(ownValue);
                if (ownLength != Array.getLength(otherValue)) return false;
                for (int j = 0; j < Array.getLength(ownValue); j++) {
                    if (!Array.get(otherValue, j).equals(Array.get(ownValue, j))) return false;
                }
            } else if (!ownValue.equals(otherValue)) return false;
        }
        return true;
    }

    /**
	 * ������
	 * @param <T>
	 * @param obj
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    @SuppressWarnings("unchecked")
    public static <T> T deepClone(Object obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        out.close();
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bin);
        Object ret = in.readObject();
        in.close();
        return (T) ret;
    }

    /**
	 * ʵ���toString
	 * @param obj
	 * @return
	 */
    public static String toString(Object obj, String... fieldName) {
        if (obj == null) return "null";
        Class<?> cl = Reflect.getClassez(obj);
        if (cl == String.class) return (String) obj;
        if (cl.isArray()) {
            String r = cl.getComponentType() + "[]{";
            for (int i = 0; i < Array.getLength(obj); i++) {
                if (i > 0) r += ",";
                Object val = Array.get(obj, i);
                if (cl.getComponentType().isPrimitive()) {
                    r += val;
                } else {
                    r += toString(val);
                }
            }
            return r + "}";
        }
        String r = cl.getName();
        r += "{";
        boolean first = true;
        Field[] fields = Reflect.getFields(cl);
        if (!isEmpty(fieldName)) {
            try {
                fields = Reflect.getFields(obj, fieldName);
            } catch (Exception e) {
                logger.error(e);
            }
        } else {
            fields = Reflect.getFields(cl);
        }
        boolean hasField = false;
        for (Field f : fields) {
            if (!Modifier.isStatic(f.getModifiers())) {
                if (!r.endsWith("[")) {
                    if (!first) {
                        r += ",";
                    } else {
                        r += "[";
                        first = false;
                    }
                    r += f.getName() + "=";
                    hasField = true;
                }
                try {
                    Class<?> t = f.getType();
                    Object val = Reflect.getValue(obj, f);
                    if (t.isPrimitive()) r += val; else r += val;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (hasField) r += "]}";
        return r;
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(1);
        Bookmark clone = bookmark.clone();
        System.err.println(bookmark);
        System.err.println(clone);
        System.err.println(clone.equals(bookmark));
    }
}
