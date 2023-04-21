package arm.beans;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class CountVector implements Cloneable {

    private SortedMap map;

    private SortedMap remove;

    private SortedMap patternMap;

    private CountVector() {
    }

    private CountVector(SortedMap map) {
        this.map = map;
    }

    /**
     * Creates an object that tracks the number of bonds in a collection
     * of Compounds by bond "code".  A bond code is an ordered concatonation 
     * of the atomic symbols belonging to the atoms on either side of the bond.
     * 
     * @param compounds array of Compound objects; usually reactants or products
     */
    public CountVector(Compound[] compounds) {
        ArrayList al = getCodes(compounds);
        this.map = createMap(al);
    }

    private ArrayList getCodes(Compound[] compounds) {
        ArrayList al = new ArrayList();
        for (int i = 0; i < compounds.length; i++) {
            Iterator iter = compounds[i].getBonds().iterator();
            while (iter.hasNext()) {
                Bond b = (Bond) iter.next();
                al.add(b.getCode());
            }
        }
        return al;
    }

    private SortedMap createMap(ArrayList list) {
        SortedMap map = new TreeMap();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            String code = (String) iter.next();
            if (map.containsKey(code)) {
                int val = ((Integer) map.get(code)).intValue();
                map.put(code, new Integer(++val));
            } else {
                map.put(code, new Integer(1));
            }
        }
        return map;
    }

    /**
     * Returns the Vector keys as an array of String objects.  These represent
     * the "column names" in the map.
     * 
     * @return array of vector keys
     */
    public String[] getBondCodes() {
        int i = 0;
        String[] codes = new String[map.keySet().size()];
        Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            codes[i++] = key;
        }
        return codes;
    }

    /**
     * Creates a new CountVector by subtracting the input vector from this
     * vector.  The current object and the subtrahend are not changed.  
     * This method supports vectors of differing sizes and/or keys.  The 
     * resulting vector will contain all keys found in both vectors.
     * NOTE: Vector subtraction does NOT support negative values.  All
     * negative results become 0.
     * 
     * @param subtrahend vector to be subtracted from this vector
     * @return newly created difference vector
     */
    public CountVector newBySubtraction(CountVector subtrahend) {
        SortedMap result = new TreeMap();
        result.putAll(this.map);
        result.putAll(subtrahend.map);
        Iterator iter = result.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Object minuendObject = map.get(key);
            Object subtrahendObject = subtrahend.map.get(key);
            if (minuendObject == null) {
                result.put(key, new Integer(0));
            } else if (subtrahendObject == null) {
                result.put(key, minuendObject);
            } else {
                Integer min = (Integer) minuendObject;
                Integer sub = (Integer) subtrahendObject;
                if (min.compareTo(sub) < 0) {
                    result.put(key, new Integer(0));
                } else {
                    Integer diff = new Integer(min.intValue() - sub.intValue());
                    result.put(key, diff);
                }
            }
        }
        return new CountVector(result);
    }

    /**
     * Creates a new CountVector by adding this vector to the input vector.
     * The current object and the addend are not changed.  This method 
     * supports vectors of differing sizes and/or keys.  The resulting
     * vector will contain all keys found in both vectors.
     * 
     * @param addend vector to be added to this vector
     * @return a newly created vector sum
     */
    public CountVector newByAddition(CountVector addend) {
        SortedMap result = new TreeMap();
        result.putAll(this.map);
        result.putAll(addend.map);
        Iterator iter = result.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Object augendObject = map.get(key);
            Object addendObject = addend.map.get(key);
            if (augendObject == null) {
                result.put(key, addendObject);
            } else if (addendObject == null) {
                result.put(key, augendObject);
            } else {
                Integer aug = (Integer) augendObject;
                Integer add = (Integer) addendObject;
                Integer sum = new Integer(aug.intValue() + add.intValue());
                result.put(key, sum);
            }
        }
        return new CountVector(result);
    }

    /**
     * Creates a new CountVector by taking the minumum value for each bond
     * code relative to the input vector.  The current object and the other 
     * are not changed.  This method supports vectors of differing sizes 
     * and/or keys.  The resulting vector will contain all keys found in 
     * both vectors.
     * 
     * @param other vector to be compared to this vector
     * @return a newly created min vector
     */
    public CountVector newByMinimum(CountVector other) {
        SortedMap result = new TreeMap();
        result.putAll(this.map);
        result.putAll(other.map);
        Iterator iter = result.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Object thisValueObject = map.get(key);
            Object otherValueObject = other.map.get(key);
            if (thisValueObject == null) {
                result.put(key, new Integer(0));
            } else if (otherValueObject == null) {
                result.put(key, new Integer(0));
            } else {
                Integer thisInt = (Integer) thisValueObject;
                Integer otherInt = (Integer) otherValueObject;
                if (thisInt.compareTo(otherInt) <= 0) {
                    result.put(key, thisInt);
                } else {
                    result.put(key, otherInt);
                }
            }
        }
        return new CountVector(result);
    }

    /**
     * Returns the integer value found for the given key.
     * @param key bond code
     * @return number of bonds
     */
    public int get(String key) {
        Integer i = (Integer) map.get(key);
        return i.intValue();
    }

    /**
     * Returns the values in bond code order.
     * @return array of int values
     */
    public int[] getValues() {
        String[] bcodes = getBondCodes();
        int[] array = new int[bcodes.length];
        for (int i = 0; i < bcodes.length; i++) {
            array[i] = get(bcodes[i]);
        }
        return array;
    }

    /**
     * Adds the input values to the Vector.  Be sure the input array is in
     * the order specified by getBondCodes.
     * 
     * @param values will be added to existing values by key (bond code)
     */
    public void add(int[] values) {
        if (values.length != map.keySet().size()) {
            throw new RuntimeException("vector addition requires objects of equal length");
        }
        int i = 0;
        Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Integer newValue = new Integer(get(key) + values[i++]);
            map.put(key, newValue);
        }
    }

    /**
     * Generates all possible BitSet patterns formed by removing the number
     * and type of bonds specified by the input vector from this vector.
     * The lists of BitSets can be retrieved by getBitPatterns.
     * 
     * @param cv vector containing the number of bonds to be "cut" by bond code
     */
    public void generateBitPatterns(CountVector cv) {
        if (!mapSizesEqual(cv)) {
            if (map.size() > cv.map.size()) {
                throw new RuntimeException("not enough bond codes found");
            } else {
                String[] bcodes = cv.getBondCodes();
                for (int i = 0; i < bcodes.length; i++) {
                    if (!map.containsKey(bcodes[i])) {
                        map.put(bcodes[i], new Integer(0));
                    }
                }
                if (!mapSizesEqual(cv)) {
                    throw new RuntimeException("mismatched bond codes");
                }
            }
        }
        this.remove = cv.map;
        this.patternMap = new TreeMap();
        this.patternMap.putAll(this.remove);
        initPatterns();
    }

    private void initPatterns() {
        String[] keys = getBondCodes();
        for (int i = 0; i < keys.length; i++) {
            Integer numBondsInt = (Integer) map.get(keys[i]);
            Integer numRemInt = (Integer) remove.get(keys[i]);
            int numBonds = numBondsInt.intValue();
            int level = numRemInt.intValue();
            ArrayList al = new ArrayList();
            if (level > 0) {
                genAllBitPatterns(al, new BitSet(numBonds), 0, numBonds, 1, level);
            } else {
                al.add(new BitSet(numBonds));
            }
            patternMap.put(keys[i], al);
        }
    }

    private void genAllBitPatterns(ArrayList list, BitSet set, int start, int end, int depth, int max) {
        while (start < end) {
            set.set(start);
            if (depth < max) {
                genAllBitPatterns(list, set, start + 1, end, depth + 1, max);
            } else {
                list.add(set.clone());
            }
            set.clear(start);
            start++;
        }
    }

    /**
     * Return all BitSets produced by generateBitPatterns.
     * 
     * @param key bond code
     * @return list of one or BitSets that represent ways to cut the specified
     * kind of bond
     */
    public List getBitPatterns(String key) {
        if (patternMap == null) {
            return null;
        }
        return (List) patternMap.get(key);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Integer value = (Integer) map.get(key);
            if (value == null) {
                sb.append("null");
            } else {
                sb.append(value.intValue());
            }
            if (iter.hasNext()) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof CountVector) {
            CountVector cv = (CountVector) o;
            if (!mapSizesEqual(cv)) {
                return false;
            }
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                Object vo = map.get(key);
                Object ovo = cv.map.get(key);
                if (ovo == null || !(ovo instanceof Integer)) {
                    return false;
                }
                Integer v = (Integer) vo;
                Integer ov = (Integer) ovo;
                if (v.compareTo(ov) != 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Object clone() {
        CountVector cv = new CountVector();
        cv.map = (TreeMap) ((TreeMap) this.map).clone();
        if (this.remove != null) {
            cv.remove = (TreeMap) ((TreeMap) this.remove).clone();
        }
        if (this.patternMap != null) {
            cv.patternMap = (TreeMap) ((TreeMap) this.patternMap).clone();
        }
        return cv;
    }

    private boolean mapSizesEqual(CountVector other) {
        if (!(other instanceof CountVector)) {
            return false;
        }
        if (map == null || other == null) {
            return false;
        }
        int size = map.keySet().size();
        if (size != other.map.keySet().size()) {
            return false;
        }
        return true;
    }
}
