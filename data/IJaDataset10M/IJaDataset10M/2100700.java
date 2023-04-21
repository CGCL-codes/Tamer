package glowaxes.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MapUtils {

    private static String bar = "||||||||||" + "||||||||||";

    private static String singleBar = "|";

    public static void addBars(Map<Object, Object>[] mapArray, Set<String> keys) {
        addBars(mapArray, keys, 0);
    }

    @SuppressWarnings("unchecked")
    public static void addBars(Map<Object, Object>[] mapArray, Set<String> keys, double defaultDouble) {
        for (String key : keys) {
            double maxValue = 0;
            double[] values = new double[mapArray.length];
            for (int row = 0; row < mapArray.length; row++) {
                Map myMap = mapArray[row];
                double dValue = TypeConverter.getDouble(myMap.get(key), defaultDouble);
                values[row] = dValue;
                maxValue = (dValue > maxValue) ? dValue : maxValue;
            }
            for (int row = 0; row < mapArray.length; row++) {
                long fctn = Math.round(bar.length() * values[row] / maxValue);
                String bars = (fctn <= 0) ? singleBar : bar.substring(0, (int) (fctn > bar.length() ? bar.length() : fctn));
                mapArray[row].put(key + "_bar", bars);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static Map[] addDoubleTotals(Map[] mapArray, Set<String> keys) {
        return addDoubleTotals(mapArray, keys, "");
    }

    @SuppressWarnings("unchecked")
    public static Map[] addDoubleTotals(Map[] mapArray, Set<String> keys, String nullString) {
        LinkedHashMap<String, Comparable> newMapArray = new LinkedHashMap<String, Comparable>();
        LinkedHashMap[] result = new LinkedHashMap[mapArray.length + 1];
        for (String key : keys) {
            double total = 0;
            for (int row = 0; row < mapArray.length; row++) {
                Map myMap = mapArray[row];
                double dValue = TypeConverter.getDouble(myMap.get(key), 0);
                total += dValue;
                result[row] = new LinkedHashMap<String, Comparable>(mapArray[row]);
            }
            if (newMapArray == null) newMapArray = new LinkedHashMap<String, Comparable>();
            newMapArray.put(key, total);
        }
        Set<String> lastKeys = mapArray[mapArray.length - 1].keySet();
        for (String key : lastKeys) {
            if (!newMapArray.containsKey(key)) newMapArray.put(key, nullString);
        }
        result[result.length - 1] = newMapArray;
        return result;
    }

    @SuppressWarnings("unchecked")
    public static void addEmptyKey(Map[] mapArray, String key) {
        for (int i = 0; i < mapArray.length; i++) {
            if (mapArray[i].get(key) == null) mapArray[i].put(key, "");
        }
    }

    @SuppressWarnings("unchecked")
    public static Map[] addLongTotals(Map[] mapArray, Set<String> keys) {
        return addLongTotals(mapArray, keys, "");
    }

    @SuppressWarnings("unchecked")
    public static Map[] addLongTotals(Map[] mapArray, Set<String> keys, String nullString) {
        LinkedHashMap<String, Comparable> newMapArray = new LinkedHashMap<String, Comparable>();
        LinkedHashMap[] result = new LinkedHashMap[mapArray.length + 1];
        for (String key : keys) {
            long total = 0;
            for (int row = 0; row < mapArray.length; row++) {
                Map myMap = mapArray[row];
                long dValue = TypeConverter.getLong(myMap.get(key), 0);
                total += dValue;
                result[row] = new LinkedHashMap<String, Comparable>(mapArray[row]);
            }
            if (newMapArray == null) newMapArray = new LinkedHashMap<String, Comparable>();
            newMapArray.put(key, total);
        }
        Set<String> lastKeys = mapArray[mapArray.length - 1].keySet();
        for (String key : lastKeys) {
            if (!newMapArray.containsKey(key)) newMapArray.put(key, nullString);
        }
        result[result.length - 1] = newMapArray;
        return result;
    }

    @SuppressWarnings("unchecked")
    public static void addNumbers(Map[] mapArray) {
        addNumbers(mapArray, "nr", true);
    }

    @SuppressWarnings("unchecked")
    public static void addNumbers(Map[] mapArray, String key, boolean reverse) {
        for (int row = 0; row < mapArray.length; row++) {
            Map myMap = mapArray[row];
            int index = row + 1;
            if (reverse) index = mapArray.length - row;
            myMap.put(key, index);
        }
    }

    @SuppressWarnings("unchecked")
    public static void addPercentage(Map<Object, Object>[] mapArray, Set<String> keys) {
        for (String key : keys) {
            double total = 0;
            double[] values = new double[mapArray.length];
            for (int row = 0; row < mapArray.length; row++) {
                Map myMap = mapArray[row];
                double dValue = TypeConverter.getDouble(myMap.get(key), 0);
                values[row] = dValue;
                total += dValue;
            }
            for (int row = 0; row < mapArray.length; row++) {
                double percentage = (Math.round(values[row] * 1000 / total)) / 10d;
                mapArray[row].put(key + "_pct", percentage + "%");
            }
        }
    }

    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static String dbCreateMap(Map[] mapArray) {
        if (mapArray == null || mapArray.length == 0) {
            return null;
        }
        MapUtils.strict(mapArray);
        Set keys = mapArray[0].keySet();
        String keyString = "";
        String indexString = "";
        Iterator iterator = keys.iterator();
        ArrayList<String> keyList = new ArrayList<String>();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (!keyString.isEmpty()) {
                keyString += ", ";
            }
            String type = "TEXT NULL";
            String indexLength = "(200)";
            Object value = mapArray[0].get(key);
            if (value instanceof String) {
                type = "TEXT NULL";
                indexLength = "(200)";
            } else if (value instanceof java.math.BigDecimal) {
                type = "DECIMAL NULL";
                indexLength = "";
            } else if (value instanceof Boolean) {
                type = "BIT NULL";
                indexLength = "";
            } else if (value instanceof Short) {
                type = "INTEGER NULL";
                indexLength = "";
            } else if (value instanceof Integer) {
                type = "INTEGER NULL";
                indexLength = "";
            } else if (value instanceof Long) {
                type = "BIGINT NULL";
                indexLength = "";
            } else if (value instanceof Float) {
                type = "REAL NULL";
                indexLength = "";
            } else if (value instanceof Double) {
                type = "FLOAT NULL";
                indexLength = "";
            } else if (value instanceof Date) {
                type = "DATETIME NULL";
                indexLength = "";
            } else if (value instanceof Time) {
                type = "TIME NULL";
                indexLength = "";
            } else if (value instanceof Timestamp) {
                type = "TIMESTAMP NULL";
                indexLength = "";
            }
            keyString += "`" + key + "` " + type;
            keyList.add(key);
            indexString += ", ";
            indexString += "KEY `" + key + "` (`" + key + "`" + indexLength + ")";
        }
        String table = "xxtablexx";
        String createString = "DROP TEMPORARY TABLE IF EXISTS " + table + "\n;\nCREATE TEMPORARY TABLE IF NOT EXISTS " + table + " (" + keyString + indexString + ") ENGINE=MyISAM DEFAULT CHARSET=utf8" + "\n;\n";
        return createString;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> dbInsertMap(Map[] mapArray) {
        if (mapArray == null) return null;
        MapUtils.strict(mapArray);
        Set keys = mapArray[0].keySet();
        String keyString = "";
        Iterator iterator = keys.iterator();
        ArrayList<String> keyList = new ArrayList<String>();
        ArrayList<Object> valueList = new ArrayList<Object>();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (!keyString.isEmpty()) {
                keyString += ", ";
            }
            keyString += "`" + key + "`";
            keyList.add(key);
        }
        keyString = "(" + keyString + ")";
        String valueString = "";
        for (int i = 0; i < mapArray.length; i++) {
            if (!valueString.isEmpty()) {
                valueString += ",";
            }
            Map myMap = mapArray[i];
            String valueRow = "";
            for (int j = 0; j < keyList.size(); j++) {
                Object value = myMap.get(keyList.get(j));
                if (valueRow.isEmpty()) {
                    valueRow += "(";
                } else {
                    valueRow += ", ";
                }
                valueRow += "?";
                valueList.add(value);
            }
            valueRow += ")";
            valueString += valueRow;
        }
        String table = "xxtablexx";
        String insert = "\nINSERT INTO " + table + " " + keyString + " VALUES " + valueString + "\n;\n";
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("inserts", insert);
        map.put("values", valueList);
        return map;
    }

    /**
     * Divides two columns (numerator/ denominator) and puts it in the map array
     * as fractionName
     */
    @SuppressWarnings("unchecked")
    public static void divide(Map[] mapArray, String numerator, String denominator, String fractionName) {
        double fraction = 0;
        for (int row = 0; row < mapArray.length; row++) {
            Map<String, Double> myMap = mapArray[row];
            if (myMap.get(numerator) != null && myMap.get(denominator) != null) {
                double nValue = TypeConverter.getDouble(myMap.get(numerator), 0);
                double dValue = TypeConverter.getDouble(myMap.get(denominator), 0);
                fraction = nValue / dValue;
                myMap.put(fractionName, fraction);
            } else {
                myMap.put(fractionName, null);
            }
        }
    }

    /**
     * Removes a the last key in the row, replacing it with empty space. Adding
     * method to remove key from last row (used for removing a total needed for
     * calculation something).
     */
    @SuppressWarnings("unchecked")
    public static void emptyLastKey(Map[] mapArray, String key) {
        mapArray[mapArray.length - 1].put(key, "");
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Map[] sortme1 = new LinkedHashMap[4];
        sortme1[0] = new LinkedHashMap();
        sortme1[0].put("one", "a");
        sortme1[0].put("two", "7d");
        sortme1[0].put("threeA", "r");
        sortme1[0].put("three", "r");
        sortme1[0].put("four", "w");
        sortme1[1] = new LinkedHashMap();
        sortme1[1].put("one", "a");
        sortme1[1].put("two", "10d");
        sortme1[1].put("threeA", "r");
        sortme1[1].put("threeC", "r");
        sortme1[1].put("three", "r");
        sortme1[1].put("four", "w");
        sortme1[2] = new LinkedHashMap();
        sortme1[2].put("one", "another");
        sortme1[2].put("two", "10d");
        sortme1[2].put("threeA", "r");
        sortme1[2].put("threeC", "r");
        sortme1[2].put("three", "r");
        sortme1[2].put("four", "w");
        sortme1[3] = new LinkedHashMap();
        sortme1[3].put("one", "a");
        sortme1[3].put("two", "2d");
        sortme1[3].put("threeA", "r");
        sortme1[3].put("threeC", "r");
        sortme1[3].put("three", "r");
        sortme1[3].put("four", "w");
        Map[] sortme2 = new LinkedHashMap[4];
        sortme2[0] = new LinkedHashMap();
        sortme2[0].put("1one", "a");
        sortme2[0].put("two", "7d");
        sortme2[0].put("1threeA", "r");
        sortme2[0].put("1three", "r");
        sortme2[0].put("1four", "w");
        sortme2[1] = new LinkedHashMap();
        sortme2[1].put("1one", "a");
        sortme2[1].put("two", "10d");
        sortme2[1].put("1threeA", "r");
        sortme2[1].put("1threeC", "r");
        sortme2[1].put("1three", "r");
        sortme2[1].put("1four", "w");
        sortme2[2] = new LinkedHashMap();
        sortme2[2].put("1one", "2another");
        sortme2[2].put("two", "10d");
        sortme2[2].put("1threeA", "2r");
        sortme2[2].put("1threeC", "2r");
        sortme2[2].put("1three", "2r");
        sortme2[2].put("1four", "2w");
        sortme2[3] = new LinkedHashMap();
        sortme2[3].put("1one", "a");
        sortme2[3].put("two", "2d");
        sortme2[3].put("1threeA", "r");
        sortme2[3].put("1threeC", "r");
        sortme2[3].put("1three", "r");
        sortme2[3].put("1four", "w");
        MapUtils.dbCreateMap(sortme2);
        MapUtils.dbInsertMap(sortme2);
        LinkedHashSet<String> hs = new LinkedHashSet<String>();
        hs.add("two");
        hs.add("four");
        sortme1 = MapUtils.merge(sortme1, sortme2, "two");
        System.out.println("hi: " + Arrays.toString(sortme1));
        System.out.println(sortme1.length);
        hs.add("four");
        hs.add("one");
        MapUtils.orderMap(sortme1, hs);
        System.out.println(Arrays.toString(sortme1));
        System.out.println(sortme1.length);
        boolean stop = true;
        if (stop) return;
        LinkedHashMap<String, String> sortme3 = new LinkedHashMap<String, String>();
        sortme3.put("one", "011");
        sortme3.put("two", "a");
        sortme3.put("three", "033");
        sortme3.put("four", "044");
        LinkedHashMap<String, String> sortme4 = new LinkedHashMap<String, String>();
        sortme4.put("one", "00111");
        sortme4.put("two", "c");
        sortme4.put("three", "00333");
        sortme4.put("four", "00444");
        LinkedHashMap[] sortArray = new LinkedHashMap[3];
        sortArray[1] = sortme3;
        sortArray[2] = sortme4;
        System.out.println("------------------");
        Map[] newArray = SortUtils.sort(sortArray, "two");
        for (int i = 0; i < sortArray.length; i++) {
            System.out.println(newArray[i]);
        }
    }

    /**
     * Merges two map arrays using indexKey as an index. Unpredictable results
     * if there are no unique values on indexKey. mapArray1 is used as a base
     * for looping and mapArray2 key value pairs are added
     */
    @SuppressWarnings("unchecked")
    public static Map[] merge(Map[] mapArray1, Map[] mapArray2, String indexKey) {
        Map<Object, Map> mapKey = new HashMap<Object, Map>();
        for (int row = 0; row < mapArray2.length; row++) {
            Map myMap = mapArray2[row];
            mapKey.put(myMap.get(indexKey), myMap);
        }
        LinkedHashMap[] result = new LinkedHashMap[mapArray1.length];
        for (int row = 0; row < mapArray1.length; row++) {
            Map myMap = mapArray1[row];
            Object index = myMap.get(indexKey);
            Map mergeRow = mapKey.get(index);
            if (mergeRow == null) {
                mergeRow = new LinkedHashMap();
            }
            mergeRow.putAll(myMap);
            result[row] = new LinkedHashMap(mergeRow);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static void orderMap(Map<Object, Object>[] mapArray, Set<String> keys) {
        for (int row = 0; row < mapArray.length; row++) {
            Map newMap = new LinkedHashMap();
            Set<Object> keySet = mapArray[row].keySet();
            for (String key : keys) {
                if (mapArray[row].containsKey(key)) newMap.put(key, mapArray[row].get(key));
                keySet.remove(key);
            }
            for (Object key : keySet) {
                newMap.put(key, mapArray[row].get(key));
            }
            mapArray[row] = newMap;
        }
    }

    @SuppressWarnings("unchecked")
    public static void removeKey(Map[] mapArray, String key) {
        for (int row = 0; row < mapArray.length; row++) {
            Map myMap = mapArray[row];
            myMap.remove(key);
        }
    }

    /**
     * Renames a key
     */
    @SuppressWarnings("unchecked")
    public static void rename(Map[] mapArray, String oldKeyName, String newKeyName) {
        for (int row = 0; row < mapArray.length; row++) {
            Map myMap = mapArray[row];
            myMap.put(newKeyName, myMap.get(oldKeyName));
            myMap.remove(oldKeyName);
        }
    }

    public static void setBarChar(String aBarChar) {
        bar = aBarChar;
    }

    public static void setSingleBarChar(String aBarChar) {
        singleBar = aBarChar;
    }

    @SuppressWarnings("unchecked")
    public static Map[] sort(Map[] mapArray, String key) {
        return sort(mapArray, key, false);
    }

    @SuppressWarnings("unchecked")
    public static Map[] sort(Map[] mapArray, String key, boolean reverse) {
        Map<Integer, Object> sortByValue = new HashMap<Integer, Object>();
        for (int row = 0; row < mapArray.length; row++) {
            Map myMap = mapArray[row];
            sortByValue.put(row, myMap.get(key));
        }
        sortByValue = sortMapByValue(sortByValue, reverse);
        LinkedHashMap[] result = new LinkedHashMap[mapArray.length];
        int row = 0;
        for (Iterator<Integer> it = sortByValue.keySet().iterator(); it.hasNext(); ) {
            result[row++] = new LinkedHashMap(mapArray[it.next()]);
        }
        return result;
    }

    public static Map sortMapByValue(Map map) {
        return sortMapByValue(map, false);
    }

    @SuppressWarnings("unchecked")
    public static Map sortMapByValue(Map map, final boolean reverse) {
        List list = new ArrayList(map.entrySet());
        Collections.sort(list, new Comparator() {

            public int compare(Object o2, Object o1) {
                if (reverse) {
                    return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
                } else {
                    return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
                }
            }
        });
        Map result = new LinkedHashMap<Integer, Object>();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * Splits a map array based on the splitKey and by default a | token
     */
    @SuppressWarnings("unchecked")
    public static Map[] split(Map[] mapArray, String splitKey, String appendKeyName) {
        if (mapArray == null || mapArray.length == 0) {
            return mapArray;
        }
        int row = 0;
        while (row < mapArray.length) {
            Map<Object, Object> map = mapArray[row];
            String value = (String) map.get(splitKey);
            if (value == null) {
                break;
            }
            int index = value.indexOf("|");
            if (value != null && index > 0) {
                for (Object key : map.keySet().toArray()) {
                    Object item = map.get(key);
                    map.remove(key);
                    if (key.toString().equals(splitKey)) {
                        map.put(key, value.substring(index + 1));
                        map.put(appendKeyName, value.substring(0, index));
                    } else {
                        map.put(key, item);
                    }
                }
            } else if (value != null && index == -1) {
                for (Object key : map.keySet().toArray()) {
                    Object item = map.get(key);
                    map.remove(key);
                    if (key.toString().equals(splitKey)) {
                        map.put(key, value);
                        map.put(appendKeyName, "");
                    } else {
                        map.put(key, item);
                    }
                }
            }
            row++;
        }
        return mapArray;
    }

    @SuppressWarnings("unchecked")
    public static Set<Object> strict(Map[] mapArray) {
        Set<Object> keys = new LinkedHashSet<Object>();
        for (int j = 0; j < mapArray.length; j++) {
            for (Object key : mapArray[j].keySet()) {
                if (!keys.contains(key)) {
                    keys.add(key);
                }
            }
        }
        for (int j = 0; j < mapArray.length; j++) {
            for (Iterator<Object> iter = keys.iterator(); iter.hasNext(); ) {
                Object key = iter.next();
                if (!mapArray[j].containsKey(key)) {
                    mapArray[j].put(key, null);
                }
            }
        }
        return keys;
    }

    /**
     * Instantiates a new sort utils.
     */
    private MapUtils() {
    }
}
