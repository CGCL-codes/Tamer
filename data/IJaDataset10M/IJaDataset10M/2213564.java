package com.gm.core.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.gm.core.collections.CollectionsUtils;
import com.gm.core.lang.ArrayUtils;
import com.gm.core.lang.BooleanUtils;
import com.gm.core.lang.CharUtils;
import com.gm.core.lang.NumberUtils;
import com.gm.core.lang.StringUtils;
import com.gm.core.lang.DateTime.DateUtils;

public class StringConvertUtils {

    @SuppressWarnings("unchecked")
    public static <T> T convertStringToObject(String str, Class<T> c) {
        Object o = null;
        if (String.class.equals(c)) {
            o = str;
        } else if (Integer.class.equals(c) || int.class.equals(c)) {
            o = NumberUtils.toIntStrict(str);
        } else if (Double.class.equals(c) || double.class.equals(c)) {
            o = NumberUtils.toDoubleStrict(str);
        } else if (Float.class.equals(c) || float.class.equals(c)) {
            o = NumberUtils.toFloatStrict(str);
        } else if (Long.class.equals(c) || long.class.equals(c)) {
            o = NumberUtils.toLongStrict(str);
        } else if (Short.class.equals(c) || short.class.equals(c)) {
            o = NumberUtils.toShortStrict(str);
        } else if (Boolean.class.equals(c) || boolean.class.equals(c)) {
            o = BooleanUtils.toBooleanStrict(str);
        } else if (Byte.class.equals(c) || byte.class.equals(c)) {
            o = Byte.valueOf(str);
        } else if (Character.class.equals(c) || char.class.equals(c)) {
            o = CharUtils.toChar(str);
        } else if (Date.class.equals(c)) {
            o = DateUtils.toDate(str);
        } else if (List.class.equals(c) || ArrayList.class.equals(c)) {
            String[] strs = StringUtils.split(str, ',', true);
            o = CollectionsUtils.toList(strs);
        } else if (String[].class.equals(c)) {
            o = ArrayUtils.toStringArray(str);
        } else if (int[].class.equals(c) || Integer[].class.equals(c)) {
            o = ArrayUtils.toIntArray(str);
        } else if (long[].class.equals(c) || Long[].class.equals(c)) {
            o = ArrayUtils.toLongArray(str);
        } else if (short[].class.equals(c) || Short[].class.equals(c)) {
            o = ArrayUtils.toShortArray(str);
        } else if (float[].class.equals(c) || Float[].class.equals(c)) {
            o = ArrayUtils.toFloatArray(str);
        } else if (double[].class.equals(c) || Double[].class.equals(c)) {
            o = ArrayUtils.toDoubleArray(str);
        } else if (byte[].class.equals(c) || Byte[].class.equals(c)) {
            o = ArrayUtils.toByteArray(str);
        } else if (boolean[].class.equals(c) || Boolean[].class.equals(c)) {
            o = ArrayUtils.toBooleanArray(str);
        } else if (char[].class.equals(c) || Character[].class.equals(c)) {
            o = ArrayUtils.toCharArray(str);
        } else {
            throw new IllegalArgumentException(String.format("String %s can not convert to %s", str, c.getName()));
        }
        return (T) o;
    }

    public static Object[] convertStringArrayToObjectArray(String[] params, Class<?>[] paramTypes) {
        Object[] result = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            result[i] = StringConvertUtils.convertStringToObject(params[i], paramTypes[i]);
        }
        return result;
    }
}
