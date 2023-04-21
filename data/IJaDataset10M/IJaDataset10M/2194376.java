package net.sourceforge.argval.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class CollectionUtil {

    private static CollectionUtilConfig defaultConfig = new CollectionUtilConfig();

    /**
     * Puts all the instance of the collection in a String array. Calls on each instance
     * of the given Set the <code>toString()</code>.
     * 
     * @param  set  The input collection.
     * @return  The array in which each instance of the Set is represented as a String.
     */
    public static String[] toStringArray(Set<?> set) {
        if (set == null || set.size() == 0) {
            return new String[0];
        }
        String[] strArray = new String[set.size()];
        int index = 0;
        for (Iterator<?> iter = set.iterator(); iter.hasNext(); ) {
            strArray[index++] = iter.next().toString();
        }
        return strArray;
    }

    public static String[] toArray(List<?> list) {
        String[] strArray = new String[list.size()];
        for (int index = 0; index < strArray.length; index++) {
            strArray[index] = list.get(index).toString();
        }
        return strArray;
    }

    public static String toString(Object[] nameArray, Object[] valueArray) {
        return toString(nameArray, valueArray, defaultConfig);
    }

    public static String toString(Object[] nameArray, Object[] valueArray, CollectionUtilConfig config) {
        return toString(new StringBuffer(), nameArray, valueArray, config);
    }

    public static String toString(StringBuffer strBuf, Object[] nameArray, Object[] valueArray, CollectionUtilConfig config) {
        if (valueArray == null || valueArray.length == 0) {
            return "";
        }
        if (nameArray == null || nameArray.length == 0) {
            return toString(strBuf, valueArray, config);
        }
        if (nameArray.length != valueArray.length) {
            return toString(strBuf, valueArray, config);
        }
        strBuf.append(config.getArrayPrefix());
        boolean isFirst = true;
        for (int index = 0; index < valueArray.length; index++) {
            if (!isFirst) {
                strBuf.append(config.getArrayValueSeparator());
            }
            strBuf.append(config.getArrayKeyPrefix()).append(nameArray[index]).append(config.getArrayKeyPostfix());
            strBuf.append(config.getKeyValueSeparator()).append(config.getArrayValuePrefix());
            strBuf.append(valueArray[index]).append(config.getArrayValuePostfix());
            isFirst = false;
        }
        strBuf.append(config.getArrayPostfix());
        return strBuf.toString();
    }

    public static String toString(Object[] input) {
        return toString(input, defaultConfig);
    }

    public static String toString(Object[] input, CollectionUtilConfig config) {
        return toString(new StringBuffer(), input, config);
    }

    public static String toString(StringBuffer strBuf, Object[] input, CollectionUtilConfig config) {
        if (input == null || input.length == 0) {
            return null;
        }
        strBuf.append(config.getArrayPrefix());
        boolean isFirst = true;
        for (int index = 0; index < input.length; index++) {
            if (!isFirst) {
                strBuf.append(config.getArrayValueSeparator());
            }
            if (input[index] != null) {
                strBuf.append(config.getArrayValuePrefix());
                strBuf.append(input[index]);
                strBuf.append(config.getArrayValuePostfix());
            } else {
                strBuf.append(config.getNullValue());
            }
            isFirst = false;
        }
        strBuf.append(config.getArrayPostfix());
        return strBuf.toString();
    }

    public static Object[] toArray(Set<?> set) {
        Object[] objArray = new Object[set.size()];
        int index = 0;
        for (Iterator<?> iter = set.iterator(); iter.hasNext(); ) {
            objArray[index++] = iter.next();
        }
        return objArray;
    }

    public static String toString(String name, String[] nameArray, Object[] valueArray) {
        return toString(new StringBuffer(), name, nameArray, valueArray).toString();
    }

    public static StringBuffer toString(StringBuffer strBuf, String name, String[] nameArray, Object[] valueArray) {
        return toString(strBuf, name, nameArray, valueArray, defaultConfig);
    }

    public static StringBuffer toString(StringBuffer strBuf, String name, String[] nameArray, Object[] valueArray, CollectionUtilConfig config) {
        strBuf.append(name);
        toString(strBuf, nameArray, valueArray, config);
        return strBuf;
    }

    public static String toString(Map<?, ?> patternMap) {
        return toString(patternMap, defaultConfig);
    }

    public static String toString(Map<?, ?> patternMap, CollectionUtilConfig config) {
        return toString(new StringBuffer(), patternMap, config).toString();
    }

    public static StringBuffer toString(StringBuffer strBuf, Map<?, ?> patternMap, CollectionUtilConfig config) {
        if (patternMap == null || patternMap.size() == 0) {
            return null;
        }
        strBuf.append(config.getArrayPrefix());
        boolean isFirst = true;
        for (Iterator<Object> iter = new TreeSet<Object>(patternMap.keySet()).iterator(); iter.hasNext(); ) {
            Object key = iter.next();
            if (!isFirst) {
                strBuf.append(config.getArrayValueSeparator());
            }
            strBuf.append(config.getArrayKeyPrefix());
            strBuf.append(key.toString());
            strBuf.append(config.getArrayKeyPostfix());
            strBuf.append(config.getKeyValueSeparator());
            if (patternMap.get(key) != null) {
                strBuf.append(config.getArrayValuePrefix());
                strBuf.append(patternMap.get(key));
                strBuf.append(config.getArrayValuePostfix());
            } else {
                strBuf.append(config.getNullValue());
            }
            isFirst = false;
        }
        strBuf.append(config.getArrayPostfix());
        return strBuf;
    }

    public static String toString(byte[] byteArray) {
        String[] byteStrArray = new String[byteArray.length];
        for (int index = 0; index < byteArray.length; index++) {
            int xx = new Byte(byteArray[index]).intValue();
            xx = (xx < 0) ? xx + 256 : xx;
            byteStrArray[index] = String.valueOf(xx);
        }
        return toString(byteStrArray);
    }

    public static List<String> stringToList(String multipleValues, String separator) {
        return (List<String>) stringToCollection(new ArrayList<String>(), multipleValues, separator);
    }

    public static Set<String> stringToSet(String multipleValues, String separator) {
        return (Set<String>) stringToCollection(new TreeSet<String>(), multipleValues, separator);
    }

    public static Collection<String> stringToCollection(Collection<String> coll, String multipleValues, String separator) {
        if (multipleValues != null) {
            StringTokenizer tokenizer = (separator == null) ? new StringTokenizer(multipleValues) : new StringTokenizer(multipleValues, separator);
            while (tokenizer.hasMoreTokens()) {
                coll.add(tokenizer.nextToken().trim());
            }
        }
        return coll;
    }

    public static String toString(Set<?> messageSet) {
        return toString(new StringBuffer(), new ArrayList<Object>(messageSet), defaultConfig);
    }

    public static String toString(List<?> messageList) {
        return toString(new StringBuffer(), messageList, defaultConfig);
    }

    public static String toString(List<?> messageList, CollectionUtilConfig config) {
        return toString(new StringBuffer(), messageList, config);
    }

    public static String toString(StringBuffer strBuf, List<?> inputList, CollectionUtilConfig config) {
        if (inputList == null || inputList.size() == 0) {
            return null;
        }
        strBuf.append(config.getArrayPrefix());
        boolean isFirst = true;
        for (int index = 0; index < inputList.size(); index++) {
            if (!isFirst) {
                strBuf.append(config.getArrayValueSeparator());
            }
            if (inputList.get(index) != null) {
                strBuf.append(config.getArrayValuePrefix());
                strBuf.append(inputList.get(index));
                strBuf.append(config.getArrayValuePostfix());
            } else {
                strBuf.append(config.getNullValue());
            }
            isFirst = false;
        }
        strBuf.append(config.getArrayPostfix());
        return strBuf.toString();
    }

    public static List<String> toList(String[] stringArray) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < stringArray.length; index++) {
            list.add(stringArray[index]);
        }
        return list;
    }
}
